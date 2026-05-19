package co.edu.uco.patiomaruparking.negocio.casouso.pedido.impl;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.PedidoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.ClienteEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.DetallePedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.EmpleadoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.MesaEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PlatoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.pedido.RegistrarPedidoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.ClienteDominio;
import co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio;
import co.edu.uco.patiomaruparking.negocio.dominio.EmpleadoDominio;
import co.edu.uco.patiomaruparking.negocio.dominio.MesaDominio;
import co.edu.uco.patiomaruparking.negocio.dominio.PedidoDominio;
import co.edu.uco.patiomaruparking.negocio.dominio.PlatoDominio;

public final class RegistrarPedidoCasoUsoImpl implements RegistrarPedidoCasoUso {

	private static final String ESTADO_PEDIDO_REGISTRADO = "REGISTRADO";
	private static final SecureRandom RANDOM = new SecureRandom();

	private final DAOFactory daoFactory;

	public RegistrarPedidoCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public PedidoDominio ejecutar(final PedidoDominio datos) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango.
		validarDatosConsistentes(datos);

		var fechaRegistro = obtenerFechaRegistro(datos);
		var horaRegistro = obtenerHoraRegistro(datos);

		// 2. Validación de objetos relacionados:
		// cliente existente y activo, empleado existente y activo, mesa existente cuando aplique.
		var cliente = validarYObtenerCliente(datos);
		var empleado = validarYObtenerEmpleado(datos);
		var mesa = validarYObtenerMesaSiAplica(datos);

		// 3. No debe existir un pedido con la misma combinación única documentada:
		// fechaRegistro + horaRegistro + tipoAtencion + cliente + empleado + mesa cuando aplique.
		validarNoExistePedidoConMismaCombinacion(datos, fechaRegistro, horaRegistro, cliente, empleado, mesa);

		// 4. El código del pedido debe ser único.
		var codigoPedido = generarCodigoUnicoPedido();

		// 5. Cada detalle del pedido debe tener un código único y datos consistentes.
		// 6. Cálculo de valores: subtotal por detalle y total del pedido.
		var detallesPreparados = prepararDetallesPedido(datos, codigoPedido);
		var totalPedido = calcularTotalPedido(detallesPreparados);

		var pedidoPreparado = PedidoDominio.builder()
				.codigoPedido(codigoPedido)
				.fechaRegistro(fechaRegistro)
				.horaRegistro(horaRegistro)
				.tipoAtencion(datos.getTipoAtencion())
				.estado(ESTADO_PEDIDO_REGISTRADO)
				.totalPedido(totalPedido)
				.mesa(mesa)
				.cliente(cliente)
				.empleado(empleado)
				.detalles(detallesPreparados)
				.build();

		guardarPedido(pedidoPreparado);
		guardarDetallesPedido(detallesPreparados);

		return pedidoPreparado;
	}

	private void validarDatosConsistentes(final PedidoDominio datos) {
		if (Objects.isNull(datos)) {
			throw new RuntimeException("Los datos del pedido son obligatorios.");
		}

		if (!tieneTexto(datos.getTipoAtencion())) {
			throw new RuntimeException("El tipo de atención del pedido es obligatorio.");
		}

		validarLongitud(datos.getTipoAtencion(), 4, 11, "El tipo de atención");

		if (Objects.isNull(datos.getCliente()) || !tieneTexto(datos.getCliente().getCodigoCliente())) {
			throw new RuntimeException("El cliente del pedido es obligatorio.");
		}

		if (Objects.isNull(datos.getEmpleado()) || !tieneTexto(datos.getEmpleado().getCodigoEmpleado())) {
			throw new RuntimeException("El empleado que registra el pedido es obligatorio.");
		}

		if (Objects.isNull(datos.getDetalles()) || datos.getDetalles().isEmpty()) {
			throw new RuntimeException("El pedido debe tener al menos un detalle registrado.");
		}
	}

	private void validarLongitud(final String valor, final int longitudMinima, final int longitudMaxima,
			final String nombreCampo) {

		if (valor.trim().length() < longitudMinima || valor.trim().length() > longitudMaxima) {
			throw new RuntimeException(nombreCampo + " debe tener entre " + longitudMinima + " y "
					+ longitudMaxima + " caracteres.");
		}
	}

	private LocalDate obtenerFechaRegistro(final PedidoDominio datos) {
		return Objects.nonNull(datos.getFechaRegistro()) ? datos.getFechaRegistro() : LocalDate.now();
	}

	private LocalTime obtenerHoraRegistro(final PedidoDominio datos) {
		return Objects.nonNull(datos.getHoraRegistro()) ? datos.getHoraRegistro().withNano(0)
				: LocalTime.now().withNano(0);
	}

	private ClienteDominio validarYObtenerCliente(final PedidoDominio datos) {
		var codigoCliente = datos.getCliente().getCodigoCliente();

		var clienteEntidad = daoFactory.obtenerClienteDAO().consultarPorId(codigoCliente);

		if (Objects.isNull(clienteEntidad)) {
			throw new RuntimeException("No existe un cliente registrado con el código indicado.");
		}

		var cliente = ClienteEntidadAssembler.getInstance().ensamblarDominio(clienteEntidad);

		if (!cliente.estaActivo()) {
			throw new RuntimeException("El cliente asociado al pedido no se encuentra activo.");
		}

		return cliente;
	}

	private EmpleadoDominio validarYObtenerEmpleado(final PedidoDominio datos) {
		var codigoEmpleado = datos.getEmpleado().getCodigoEmpleado();

		var empleadoEntidad = daoFactory.obtenerEmpleadoDAO().consultarPorId(codigoEmpleado);

		if (Objects.isNull(empleadoEntidad)) {
			throw new RuntimeException("No existe un empleado registrado con el código indicado.");
		}

		var empleado = EmpleadoEntidadAssembler.getInstance().ensamblarDominio(empleadoEntidad);

		if (!empleado.estaActivo()) {
			throw new RuntimeException("El empleado asociado al pedido no se encuentra activo.");
		}

		return empleado;
	}

	private MesaDominio validarYObtenerMesaSiAplica(final PedidoDominio datos) {
		if (!requiereMesa(datos.getTipoAtencion())) {
			if (Objects.nonNull(datos.getMesa()) && tieneTexto(datos.getMesa().getCodigoMesa())) {
				var mesaEntidad = daoFactory.obtenerMesaDAO().consultarPorId(datos.getMesa().getCodigoMesa());

				if (Objects.isNull(mesaEntidad)) {
					throw new RuntimeException("No existe una mesa registrada con el código indicado.");
				}

				return MesaEntidadAssembler.getInstance().ensamblarDominio(mesaEntidad);
			}

			return MesaDominio.builder().build();
		}

		if (Objects.isNull(datos.getMesa()) || !tieneTexto(datos.getMesa().getCodigoMesa())) {
			throw new RuntimeException("La mesa es obligatoria cuando el tipo de atención es en mesa.");
		}

		var mesaEntidad = daoFactory.obtenerMesaDAO().consultarPorId(datos.getMesa().getCodigoMesa());

		if (Objects.isNull(mesaEntidad)) {
			throw new RuntimeException("No existe una mesa registrada con el código indicado.");
		}

		return MesaEntidadAssembler.getInstance().ensamblarDominio(mesaEntidad);
	}

	private boolean requiereMesa(final String tipoAtencion) {
		return "MESA".equalsIgnoreCase(tipoAtencion)
				|| "EN MESA".equalsIgnoreCase(tipoAtencion)
				|| "SERVICIO MESA".equalsIgnoreCase(tipoAtencion)
				|| "SERVICIO EN MESA".equalsIgnoreCase(tipoAtencion);
	}

	private void validarNoExistePedidoConMismaCombinacion(final PedidoDominio datos, final LocalDate fechaRegistro,
			final LocalTime horaRegistro, final ClienteDominio cliente, final EmpleadoDominio empleado,
			final MesaDominio mesa) {

		var filtro = PedidoEntidad.builder()
				.fechaRegistro(fechaRegistro)
				.horaRegistro(horaRegistro)
				.tipoAtencion(datos.getTipoAtencion())
				.cliente(ClienteEntidadAssembler.getInstance().ensamblarEntidad(cliente))
				.empleado(EmpleadoEntidadAssembler.getInstance().ensamblarEntidad(empleado))
				.mesa(MesaEntidadAssembler.getInstance().ensamblarEntidad(mesa))
				.build();

		var resultados = daoFactory.obtenerPedidoDAO().consultar(filtro);

		if (existePedidoConMismaCombinacion(resultados, fechaRegistro, horaRegistro, datos.getTipoAtencion(),
				cliente, empleado, mesa)) {
			throw new RuntimeException(
					"Ya existe un pedido registrado con la misma fecha, hora, tipo de atención, cliente, empleado y mesa.");
		}
	}

	private boolean existePedidoConMismaCombinacion(final List<PedidoEntidad> resultados,
			final LocalDate fechaRegistro, final LocalTime horaRegistro, final String tipoAtencion,
			final ClienteDominio cliente, final EmpleadoDominio empleado, final MesaDominio mesa) {

		if (Objects.isNull(resultados) || resultados.isEmpty()) {
			return false;
		}

		for (PedidoEntidad pedido : resultados) {
			if (Objects.equals(pedido.getFechaRegistro(), fechaRegistro)
					&& Objects.equals(pedido.getHoraRegistro(), horaRegistro)
					&& textoIgual(pedido.getTipoAtencion(), tipoAtencion)
					&& textoIgual(pedido.getCliente().getCodigoCliente(), cliente.getCodigoCliente())
					&& textoIgual(pedido.getEmpleado().getCodigoEmpleado(), empleado.getCodigoEmpleado())
					&& textoIgual(pedido.getMesa().getCodigoMesa(), mesa.getCodigoMesa())) {
				return true;
			}
		}

		return false;
	}

	private ArrayList<DetallePedidoDominio> prepararDetallesPedido(final PedidoDominio datos,
			final String codigoPedido) {

		var detallesPreparados = new ArrayList<DetallePedidoDominio>();

		for (DetallePedidoDominio detalle : datos.getDetalles()) {
			validarDatosConsistentesDetalle(detalle);

			var plato = validarYObtenerPlato(detalle);

			var detallePreparado = DetallePedidoDominio.builder()
					.codigoDetallePedido(generarCodigoUnicoDetallePedido())
					.codigoPedido(codigoPedido)
					.cantidad(detalle.getCantidad())
					.plato(plato)
					.subtotal(plato.getPrecioVenta().multiply(BigDecimal.valueOf(detalle.getCantidad())))
					.build();

			detallesPreparados.add(detallePreparado);
		}

		return detallesPreparados;
	}

	private void validarDatosConsistentesDetalle(final DetallePedidoDominio detalle) {
		if (Objects.isNull(detalle)) {
			throw new RuntimeException("La información del detalle del pedido es obligatoria.");
		}

		if (Objects.isNull(detalle.getCantidad()) || detalle.getCantidad() <= 0) {
			throw new RuntimeException("La cantidad del detalle del pedido debe ser mayor que cero.");
		}

		if (Objects.isNull(detalle.getPlato()) || !tieneTexto(detalle.getPlato().getCodigoPlato())) {
			throw new RuntimeException("El plato del detalle del pedido es obligatorio.");
		}
	}

	private PlatoDominio validarYObtenerPlato(final DetallePedidoDominio detalle) {
		var codigoPlato = detalle.getPlato().getCodigoPlato();

		var platoEntidad = daoFactory.obtenerPlatoDAO().consultarPorId(codigoPlato);

		if (Objects.isNull(platoEntidad)) {
			throw new RuntimeException("No existe un plato registrado con el código indicado.");
		}

		var plato = PlatoEntidadAssembler.getInstance().ensamblarDominio(platoEntidad);

		if (!plato.estaDisponible()) {
			throw new RuntimeException("El plato " + plato.getNombre() + " no se encuentra disponible.");
		}

		if (!plato.tienePrecioVentaValido()) {
			throw new RuntimeException("El plato " + plato.getNombre() + " no tiene un precio de venta válido.");
		}

		return plato;
	}

	private BigDecimal calcularTotalPedido(final ArrayList<DetallePedidoDominio> detalles) {
		var total = BigDecimal.ZERO;

		for (DetallePedidoDominio detalle : detalles) {
			total = total.add(detalle.getSubtotal());
		}

		return total;
	}

	private void guardarPedido(final PedidoDominio pedido) {
		var pedidoEntidad = PedidoEntidadAssembler.getInstance().ensamblarEntidad(pedido);
		daoFactory.obtenerPedidoDAO().registrar(pedidoEntidad);
	}

	private void guardarDetallesPedido(final ArrayList<DetallePedidoDominio> detalles) {
		for (DetallePedidoDominio detalle : detalles) {
			var detalleEntidad = DetallePedidoEntidadAssembler.getInstance().ensamblarEntidad(detalle);
			daoFactory.obtenerDetallePedidoDAO().registrar(detalleEntidad);
		}
	}

	private String generarCodigoUnicoPedido() {
		String codigoPedido;
		PedidoEntidad pedidoExistente;

		do {
			codigoPedido = generarCodigo("PED");
			pedidoExistente = daoFactory.obtenerPedidoDAO().consultarPorId(codigoPedido);
		} while (Objects.nonNull(pedidoExistente));

		return codigoPedido;
	}

	private String generarCodigoUnicoDetallePedido() {
		String codigoDetallePedido;
		var detalleExistente = false;

		do {
			codigoDetallePedido = generarCodigo("DPE");
			detalleExistente = Objects.nonNull(
					daoFactory.obtenerDetallePedidoDAO().consultarPorId(codigoDetallePedido));
		} while (detalleExistente);

		return codigoDetallePedido;
	}

	private String generarCodigo(final String prefijo) {
		var numero = RANDOM.nextInt(10000);
		return prefijo + String.format("%04d", numero);
	}

	private boolean tieneTexto(final String texto) {
		return Objects.nonNull(texto) && !texto.trim().isEmpty();
	}

	private boolean textoIgual(final String textoUno, final String textoDos) {
		return Objects.toString(textoUno, "").trim()
				.equalsIgnoreCase(Objects.toString(textoDos, "").trim());
	}
}