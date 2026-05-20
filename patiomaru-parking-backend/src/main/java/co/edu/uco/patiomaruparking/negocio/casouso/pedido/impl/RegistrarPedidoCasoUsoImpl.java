package co.edu.uco.patiomaruparking.negocio.casouso.pedido.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilCodigo;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public final class RegistrarPedidoCasoUsoImpl implements RegistrarPedidoCasoUso {

	private static final String ESTADO_PEDIDO_REGISTRADO = "REGISTRADO";

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
		if (UtilObjeto.esNulo(datos)) {
			throw new RuntimeException("Los datos del pedido son obligatorios.");
		}

		if (!UtilTexto.tieneTexto(datos.getTipoAtencion())) {
			throw new RuntimeException("El tipo de atención del pedido es obligatorio.");
		}

		validarLongitud(datos.getTipoAtencion(), 4, 11, "El tipo de atención");

		if (UtilObjeto.esNulo(datos.getCliente())
				|| !UtilTexto.tieneTexto(datos.getCliente().getCodigoCliente())) {
			throw new RuntimeException("El cliente del pedido es obligatorio.");
		}

		if (UtilObjeto.esNulo(datos.getEmpleado())
				|| !UtilTexto.tieneTexto(datos.getEmpleado().getCodigoEmpleado())) {
			throw new RuntimeException("El empleado que registra el pedido es obligatorio.");
		}

		if (UtilObjeto.esNulo(datos.getDetalles()) || datos.getDetalles().isEmpty()) {
			throw new RuntimeException("El pedido debe tener al menos un detalle registrado.");
		}
	}

	private void validarLongitud(final String valor, final int longitudMinima, final int longitudMaxima,
			final String nombreCampo) {

		var valorSeguro = UtilTexto.aplicarTrim(valor);

		if (valorSeguro.length() < longitudMinima || valorSeguro.length() > longitudMaxima) {
			throw new RuntimeException(nombreCampo + " debe tener entre " + longitudMinima + " y "
					+ longitudMaxima + " caracteres.");
		}
	}

	private LocalDate obtenerFechaRegistro(final PedidoDominio datos) {
		return UtilObjeto.noEsNulo(datos.getFechaRegistro()) ? datos.getFechaRegistro() : LocalDate.now();
	}

	private LocalTime obtenerHoraRegistro(final PedidoDominio datos) {
		return UtilObjeto.noEsNulo(datos.getHoraRegistro()) ? datos.getHoraRegistro().withNano(0)
				: LocalTime.now().withNano(0);
	}

	private ClienteDominio validarYObtenerCliente(final PedidoDominio datos) {
		var codigoCliente = UtilTexto.aplicarTrim(datos.getCliente().getCodigoCliente());

		var clienteEntidad = daoFactory.obtenerClienteDAO().consultarPorId(codigoCliente);

		if (UtilObjeto.esNulo(clienteEntidad)) {
			throw new RuntimeException("No existe un cliente registrado con el código indicado.");
		}

		var cliente = ClienteEntidadAssembler.getInstance().ensamblarDominio(clienteEntidad);

		if (!cliente.estaActivo()) {
			throw new RuntimeException("El cliente asociado al pedido no se encuentra activo.");
		}

		return cliente;
	}

	private EmpleadoDominio validarYObtenerEmpleado(final PedidoDominio datos) {
		var codigoEmpleado = UtilTexto.aplicarTrim(datos.getEmpleado().getCodigoEmpleado());

		var empleadoEntidad = daoFactory.obtenerEmpleadoDAO().consultarPorId(codigoEmpleado);

		if (UtilObjeto.esNulo(empleadoEntidad)) {
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
			if (UtilObjeto.noEsNulo(datos.getMesa())
					&& UtilTexto.tieneTexto(datos.getMesa().getCodigoMesa())) {

				var mesaEntidad = daoFactory.obtenerMesaDAO()
						.consultarPorId(UtilTexto.aplicarTrim(datos.getMesa().getCodigoMesa()));

				if (UtilObjeto.esNulo(mesaEntidad)) {
					throw new RuntimeException("No existe una mesa registrada con el código indicado.");
				}

				return MesaEntidadAssembler.getInstance().ensamblarDominio(mesaEntidad);
			}

			return MesaDominio.builder().build();
		}

		if (UtilObjeto.esNulo(datos.getMesa()) || !UtilTexto.tieneTexto(datos.getMesa().getCodigoMesa())) {
			throw new RuntimeException("La mesa es obligatoria cuando el tipo de atención es en mesa.");
		}

		var mesaEntidad = daoFactory.obtenerMesaDAO()
				.consultarPorId(UtilTexto.aplicarTrim(datos.getMesa().getCodigoMesa()));

		if (UtilObjeto.esNulo(mesaEntidad)) {
			throw new RuntimeException("No existe una mesa registrada con el código indicado.");
		}

		return MesaEntidadAssembler.getInstance().ensamblarDominio(mesaEntidad);
	}

	private boolean requiereMesa(final String tipoAtencion) {
		return UtilTexto.sonIgualesIgnorandoMayusculas(tipoAtencion, "MESA")
				|| UtilTexto.sonIgualesIgnorandoMayusculas(tipoAtencion, "EN MESA")
				|| UtilTexto.sonIgualesIgnorandoMayusculas(tipoAtencion, "SERVICIO MESA")
				|| UtilTexto.sonIgualesIgnorandoMayusculas(tipoAtencion, "SERVICIO EN MESA");
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

		if (UtilObjeto.esNulo(resultados) || resultados.isEmpty()) {
			return false;
		}

		for (PedidoEntidad pedido : resultados) {
			if (fechaIgual(pedido.getFechaRegistro(), fechaRegistro)
					&& horaIgual(pedido.getHoraRegistro(), horaRegistro)
					&& UtilTexto.sonIgualesIgnorandoMayusculas(pedido.getTipoAtencion(), tipoAtencion)
					&& UtilTexto.sonIgualesIgnorandoMayusculas(
							pedido.getCliente().getCodigoCliente(), cliente.getCodigoCliente())
					&& UtilTexto.sonIgualesIgnorandoMayusculas(
							pedido.getEmpleado().getCodigoEmpleado(), empleado.getCodigoEmpleado())
					&& UtilTexto.sonIgualesIgnorandoMayusculas(
							pedido.getMesa().getCodigoMesa(), mesa.getCodigoMesa())) {
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
		if (UtilObjeto.esNulo(detalle)) {
			throw new RuntimeException("La información del detalle del pedido es obligatoria.");
		}

		if (UtilObjeto.esNulo(detalle.getCantidad()) || detalle.getCantidad() <= 0) {
			throw new RuntimeException("La cantidad del detalle del pedido debe ser mayor que cero.");
		}

		if (UtilObjeto.esNulo(detalle.getPlato())
				|| !UtilTexto.tieneTexto(detalle.getPlato().getCodigoPlato())) {
			throw new RuntimeException("El plato del detalle del pedido es obligatorio.");
		}
	}

	private PlatoDominio validarYObtenerPlato(final DetallePedidoDominio detalle) {
		var codigoPlato = UtilTexto.aplicarTrim(detalle.getPlato().getCodigoPlato());

		var platoEntidad = daoFactory.obtenerPlatoDAO().consultarPorId(codigoPlato);

		if (UtilObjeto.esNulo(platoEntidad)) {
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
			codigoPedido = UtilCodigo.generarCodigo("PED");
			pedidoExistente = daoFactory.obtenerPedidoDAO().consultarPorId(codigoPedido);
		} while (UtilObjeto.noEsNulo(pedidoExistente));

		return codigoPedido;
	}

	private String generarCodigoUnicoDetallePedido() {
		String codigoDetallePedido;

		do {
			codigoDetallePedido = UtilCodigo.generarCodigo("DPE");
		} while (UtilObjeto.noEsNulo(
				daoFactory.obtenerDetallePedidoDAO().consultarPorId(codigoDetallePedido)));

		return codigoDetallePedido;
	}

	private boolean fechaIgual(final LocalDate fechaUno, final LocalDate fechaDos) {
		if (UtilObjeto.esNulo(fechaUno) && UtilObjeto.esNulo(fechaDos)) {
			return true;
		}

		if (UtilObjeto.esNulo(fechaUno) || UtilObjeto.esNulo(fechaDos)) {
			return false;
		}

		return fechaUno.equals(fechaDos);
	}

	private boolean horaIgual(final LocalTime horaUno, final LocalTime horaDos) {
		if (UtilObjeto.esNulo(horaUno) && UtilObjeto.esNulo(horaDos)) {
			return true;
		}

		if (UtilObjeto.esNulo(horaUno) || UtilObjeto.esNulo(horaDos)) {
			return false;
		}

		return horaUno.equals(horaDos);
	}
}