package co.edu.uco.patiomaruparking.negocio.casouso.pedido.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.DetallePedidoEntidad;
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
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;

public final class RegistrarPedidoCasoUsoImpl implements RegistrarPedidoCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(RegistrarPedidoCasoUsoImpl.class);

	private static final String PREFIJO_PEDIDO = "PEDD";
	private static final int CANTIDAD_DIGITOS_PEDIDO = 3;

	private static final String PREFIJO_DETALLE_PEDIDO = "DP";
	private static final int CANTIDAD_DIGITOS_DETALLE_PEDIDO = 3;

	private static final String ESTADO_PEDIDO_REGISTRADO = "Registrado";

	private static final String TIPO_ATENCION_MESA = "Mesa";
	private static final String TIPO_ATENCION_PARA_LLEVAR = "Para llevar";

	private static final String CODIGO_MESA_NO_APLICA = "MESA000";

	private static final int LONGITUD_MINIMA_TIPO_ATENCION = 4;
	private static final int LONGITUD_MAXIMA_TIPO_ATENCION = 11;
	private static final int CANTIDAD_MINIMA_PERMITIDA = 1;

	private final DAOFactory daoFactory;

	public RegistrarPedidoCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = UtilObjeto.obtenerValorDefecto(
				daoFactory,
				DAOFactory.getFactory());
	}

	@Override
	public PedidoDominio ejecutar(final PedidoDominio datos) {
		logger.info("Iniciando el registro de un pedido.");

		var pedido = UtilObjeto.obtenerValorDefecto(
				datos,
				PedidoDominio.builder().build());

		validarDatosConsistentesPedido(pedido);

		var fechaRegistro = obtenerFechaRegistro(pedido);
		var horaRegistro = obtenerHoraRegistro(pedido);

		var cliente = validarYObtenerCliente(pedido);
		var empleado = validarYObtenerEmpleado(pedido);
		var mesa = validarYObtenerMesaSiAplica(pedido);

		validarNoExistePedidoConMismaCombinacion(
				pedido,
				fechaRegistro,
				horaRegistro,
				cliente,
				empleado,
				mesa);

		var codigoPedido = generarCodigoUnicoPedido();

		var detallesPreparados = prepararDetallesPedido(
				pedido,
				codigoPedido);

		var totalPedido = calcularTotalPedido(detallesPreparados);

		var pedidoPreparado = PedidoDominio.builder()
				.codigoPedido(codigoPedido)
				.fechaRegistro(fechaRegistro)
				.horaRegistro(horaRegistro)
				.tipoAtencion(UtilTexto.aplicarTrim(pedido.getTipoAtencion()))
				.estado(ESTADO_PEDIDO_REGISTRADO)
				.totalPedido(totalPedido)
				.mesa(mesa)
				.cliente(cliente)
				.empleado(empleado)
				.detalles(detallesPreparados)
				.build();

		guardarPedido(pedidoPreparado);
		guardarDetallesPedido(detallesPreparados);

		logger.info("Pedido registrado satisfactoriamente.");

		return pedidoPreparado;
	}

	private void validarDatosConsistentesPedido(final PedidoDominio pedido) {
		validarTipoAtencion(pedido);
		validarClientePedido(pedido);
		validarEmpleadoPedido(pedido);
		validarDetallesPedido(pedido);
	}

	private void validarTipoAtencion(final PedidoDominio pedido) {
		if (!UtilTexto.tieneTexto(pedido.getTipoAtencion())) {
			throw NegocioPatioMaruExcepcion.crear(
					"El tipo de atención del pedido es obligatorio.");
		}

		validarLongitud(
				pedido.getTipoAtencion(),
				LONGITUD_MINIMA_TIPO_ATENCION,
				LONGITUD_MAXIMA_TIPO_ATENCION,
				"El tipo de atención");

		if (!esTipoAtencionValido(pedido.getTipoAtencion())) {
			throw NegocioPatioMaruExcepcion.crear(
					"El tipo de atención del pedido solo puede ser Mesa o Para llevar.");
		}
	}

	private void validarClientePedido(final PedidoDominio pedido) {
		if (!UtilTexto.tieneTexto(pedido.getCliente().getCodigoCliente())) {
			throw NegocioPatioMaruExcepcion.crear(
					"El cliente del pedido es obligatorio.");
		}
	}

	private void validarEmpleadoPedido(final PedidoDominio pedido) {
		if (!UtilTexto.tieneTexto(pedido.getEmpleado().getCodigoEmpleado())) {
			throw NegocioPatioMaruExcepcion.crear(
					"El empleado que registra el pedido es obligatorio.");
		}
	}

	private void validarDetallesPedido(final PedidoDominio pedido) {
		if (pedido.getDetalles().isEmpty()) {
			throw NegocioPatioMaruExcepcion.crear(
					"El pedido debe tener al menos un detalle registrado.");
		}
	}

	private void validarLongitud(
			final String valor,
			final int longitudMinima,
			final int longitudMaxima,
			final String nombreCampo) {

		var valorSeguro = UtilTexto.aplicarTrim(valor);

		if (valorSeguro.length() < longitudMinima || valorSeguro.length() > longitudMaxima) {
			throw NegocioPatioMaruExcepcion.crear(
					nombreCampo + " debe tener entre " + longitudMinima + " y "
							+ longitudMaxima + " caracteres.");
		}
	}

	private boolean esTipoAtencionValido(final String tipoAtencion) {
		return esAtencionEnMesa(tipoAtencion)
				|| esAtencionParaLlevar(tipoAtencion);
	}

	private boolean esAtencionEnMesa(final String tipoAtencion) {
		return UtilTexto.sonIgualesIgnorandoMayusculas(
				tipoAtencion,
				TIPO_ATENCION_MESA);
	}

	private boolean esAtencionParaLlevar(final String tipoAtencion) {
		return UtilTexto.sonIgualesIgnorandoMayusculas(
				tipoAtencion,
				TIPO_ATENCION_PARA_LLEVAR);
	}

	private LocalDate obtenerFechaRegistro(final PedidoDominio pedido) {
		return UtilObjeto.obtenerValorDefecto(
				pedido.getFechaRegistro(),
				LocalDate.now());
	}

	private LocalTime obtenerHoraRegistro(final PedidoDominio pedido) {
		var horaRegistro = UtilObjeto.obtenerValorDefecto(
				pedido.getHoraRegistro(),
				LocalTime.now());

		return horaRegistro.withNano(0);
	}

	private ClienteDominio validarYObtenerCliente(final PedidoDominio pedido) {
		var codigoCliente = UtilTexto.aplicarTrim(
				pedido.getCliente().getCodigoCliente());

		var clienteEntidad = daoFactory.obtenerClienteDAO()
				.consultarPorId(codigoCliente);

		if (UtilObjeto.esNulo(clienteEntidad)
				|| UtilTexto.esVacio(clienteEntidad.getCodigoCliente())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe un cliente registrado con el código indicado.");
		}

		var cliente = ClienteEntidadAssembler.getInstance()
				.ensamblarDominio(clienteEntidad);

		if (!cliente.estaActivo()) {
			throw NegocioPatioMaruExcepcion.crear(
					"El cliente asociado al pedido no se encuentra activo.");
		}

		return cliente;
	}

	private EmpleadoDominio validarYObtenerEmpleado(final PedidoDominio pedido) {
		var codigoEmpleado = UtilTexto.aplicarTrim(
				pedido.getEmpleado().getCodigoEmpleado());

		var empleadoEntidad = daoFactory.obtenerEmpleadoDAO()
				.consultarPorId(codigoEmpleado);

		if (UtilObjeto.esNulo(empleadoEntidad)
				|| UtilTexto.esVacio(empleadoEntidad.getCodigoEmpleado())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe un empleado registrado con el código indicado.");
		}

		var empleado = EmpleadoEntidadAssembler.getInstance()
				.ensamblarDominio(empleadoEntidad);

		if (!empleado.estaActivo()) {
			throw NegocioPatioMaruExcepcion.crear(
					"El empleado asociado al pedido no se encuentra activo.");
		}

		return empleado;
	}

	private MesaDominio validarYObtenerMesaSiAplica(final PedidoDominio pedido) {
		if (esAtencionParaLlevar(pedido.getTipoAtencion())) {
			return validarYObtenerMesaNoAplica();
		}

		if (!UtilTexto.tieneTexto(pedido.getMesa().getCodigoMesa())) {
			throw NegocioPatioMaruExcepcion.crear(
					"La mesa es obligatoria cuando el tipo de atención es Mesa.");
		}

		var codigoMesa = UtilTexto.aplicarTrim(
				pedido.getMesa().getCodigoMesa());

		return validarYObtenerMesaPorCodigo(codigoMesa);
	}

	private MesaDominio validarYObtenerMesaNoAplica() {
		return validarYObtenerMesaPorCodigo(CODIGO_MESA_NO_APLICA);
	}

	private MesaDominio validarYObtenerMesaPorCodigo(final String codigoMesa) {
		var mesaEntidad = daoFactory.obtenerMesaDAO()
				.consultarPorId(codigoMesa);

		if (UtilObjeto.esNulo(mesaEntidad)
				|| UtilTexto.esVacio(mesaEntidad.getCodigoMesa())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe una mesa registrada con el código indicado.");
		}

		return MesaEntidadAssembler.getInstance()
				.ensamblarDominio(mesaEntidad);
	}

	private void validarNoExistePedidoConMismaCombinacion(
			final PedidoDominio pedido,
			final LocalDate fechaRegistro,
			final LocalTime horaRegistro,
			final ClienteDominio cliente,
			final EmpleadoDominio empleado,
			final MesaDominio mesa) {

		var filtro = PedidoEntidad.builder()
				.fechaRegistro(fechaRegistro)
				.horaRegistro(horaRegistro)
				.tipoAtencion(pedido.getTipoAtencion())
				.cliente(ClienteEntidadAssembler.getInstance().ensamblarEntidad(cliente))
				.empleado(EmpleadoEntidadAssembler.getInstance().ensamblarEntidad(empleado))
				.mesa(MesaEntidadAssembler.getInstance().ensamblarEntidad(mesa))
				.build();

		var resultados = daoFactory.obtenerPedidoDAO().consultar(filtro);

		if (existePedidoConMismaCombinacion(
				resultados,
				fechaRegistro,
				horaRegistro,
				pedido.getTipoAtencion(),
				cliente,
				empleado,
				mesa)) {

			throw NegocioPatioMaruExcepcion.crear(
					"Ya existe un pedido registrado con la misma fecha, hora, tipo de atención, cliente, empleado y mesa.");
		}
	}

	private boolean existePedidoConMismaCombinacion(
			final List<PedidoEntidad> resultados,
			final LocalDate fechaRegistro,
			final LocalTime horaRegistro,
			final String tipoAtencion,
			final ClienteDominio cliente,
			final EmpleadoDominio empleado,
			final MesaDominio mesa) {

		var pedidos = UtilObjeto.obtenerValorDefecto(
				resultados,
				List.<PedidoEntidad>of());

		for (PedidoEntidad pedido : pedidos) {
			if (coincideCombinacionPedido(
					pedido,
					fechaRegistro,
					horaRegistro,
					tipoAtencion,
					cliente,
					empleado,
					mesa)) {

				return true;
			}
		}

		return false;
	}

	private boolean coincideCombinacionPedido(
			final PedidoEntidad pedido,
			final LocalDate fechaRegistro,
			final LocalTime horaRegistro,
			final String tipoAtencion,
			final ClienteDominio cliente,
			final EmpleadoDominio empleado,
			final MesaDominio mesa) {

		return fechaIgual(pedido.getFechaRegistro(), fechaRegistro)
				&& horaIgual(pedido.getHoraRegistro(), horaRegistro)
				&& UtilTexto.sonIgualesIgnorandoMayusculas(
						pedido.getTipoAtencion(),
						tipoAtencion)
				&& UtilTexto.sonIgualesIgnorandoMayusculas(
						pedido.getCliente().getCodigoCliente(),
						cliente.getCodigoCliente())
				&& UtilTexto.sonIgualesIgnorandoMayusculas(
						pedido.getEmpleado().getCodigoEmpleado(),
						empleado.getCodigoEmpleado())
				&& UtilTexto.sonIgualesIgnorandoMayusculas(
						pedido.getMesa().getCodigoMesa(),
						mesa.getCodigoMesa());
	}

	private List<DetallePedidoDominio> prepararDetallesPedido(
			final PedidoDominio pedido,
			final String codigoPedido) {

		var detallesPreparados = new ArrayList<DetallePedidoDominio>();
		var codigosDetalleGenerados = new ArrayList<String>();
		var codigosPlatoAgregados = new ArrayList<String>();

		for (DetallePedidoDominio detalle : pedido.getDetalles()) {
			var detalleSeguro = UtilObjeto.obtenerValorDefecto(
					detalle,
					DetallePedidoDominio.builder().build());

			validarDatosConsistentesDetalle(detalleSeguro);

			var plato = validarYObtenerPlato(detalleSeguro);
			var cantidad = obtenerCantidadDetalle(detalleSeguro);

			validarPlatoNoRepetidoEnPedido(
					codigosPlatoAgregados,
					plato.getCodigoPlato());

			var codigoDetallePedido = generarCodigoUnicoDetallePedido(
					codigosDetalleGenerados);

			var subtotal = calcularSubtotalDetalle(
					plato,
					cantidad);

			var detallePreparado = DetallePedidoDominio.builder()
					.codigoDetallePedido(codigoDetallePedido)
					.codigoPedido(codigoPedido)
					.cantidad(cantidad)
					.plato(plato)
					.subtotal(subtotal)
					.build();

			detallesPreparados.add(detallePreparado);
			codigosDetalleGenerados.add(codigoDetallePedido);
			codigosPlatoAgregados.add(plato.getCodigoPlato());
		}

		return detallesPreparados;
	}

	private void validarDatosConsistentesDetalle(final DetallePedidoDominio detalle) {
		var cantidad = obtenerCantidadDetalle(detalle);

		if (cantidad < CANTIDAD_MINIMA_PERMITIDA) {
			throw NegocioPatioMaruExcepcion.crear(
					"La cantidad del detalle del pedido debe ser mayor que cero.");
		}

		if (!UtilTexto.tieneTexto(detalle.getPlato().getCodigoPlato())) {
			throw NegocioPatioMaruExcepcion.crear(
					"El plato del detalle del pedido es obligatorio.");
		}
	}

	private Integer obtenerCantidadDetalle(final DetallePedidoDominio detalle) {
		return UtilObjeto.obtenerValorDefecto(
				detalle.getCantidad(),
				0);
	}

	private PlatoDominio validarYObtenerPlato(final DetallePedidoDominio detalle) {
		var codigoPlato = UtilTexto.aplicarTrim(
				detalle.getPlato().getCodigoPlato());

		var platoEntidad = daoFactory.obtenerPlatoDAO()
				.consultarPorId(codigoPlato);

		if (UtilObjeto.esNulo(platoEntidad)
				|| UtilTexto.esVacio(platoEntidad.getCodigoPlato())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe un plato registrado con el código indicado.");
		}

		var plato = PlatoEntidadAssembler.getInstance()
				.ensamblarDominio(platoEntidad);

		if (!plato.estaDisponible()) {
			throw NegocioPatioMaruExcepcion.crear(
					"El plato " + plato.getNombre() + " no se encuentra disponible.");
		}

		if (!plato.tienePrecioVentaValido()) {
			throw NegocioPatioMaruExcepcion.crear(
					"El plato " + plato.getNombre() + " no tiene un precio de venta válido.");
		}

		return plato;
	}

	private void validarPlatoNoRepetidoEnPedido(
			final List<String> codigosPlatoAgregados,
			final String codigoPlato) {

		for (String codigoPlatoAgregado : codigosPlatoAgregados) {
			if (UtilTexto.sonIgualesIgnorandoMayusculas(
					codigoPlatoAgregado,
					codigoPlato)) {

				throw NegocioPatioMaruExcepcion.crear(
						"No se puede registrar dos veces el mismo plato dentro del mismo pedido.");
			}
		}
	}

	private BigDecimal calcularSubtotalDetalle(
			final PlatoDominio plato,
			final Integer cantidad) {

		return plato.getPrecioVenta()
				.multiply(BigDecimal.valueOf(cantidad));
	}

	private BigDecimal calcularTotalPedido(final List<DetallePedidoDominio> detalles) {
		var total = BigDecimal.ZERO;

		for (DetallePedidoDominio detalle : detalles) {
			total = total.add(
					UtilObjeto.obtenerValorDefecto(
							detalle.getSubtotal(),
							BigDecimal.ZERO));
		}

		return total;
	}

	private void guardarPedido(final PedidoDominio pedido) {
		var pedidoEntidad = PedidoEntidadAssembler.getInstance()
				.ensamblarEntidad(pedido);

		daoFactory.obtenerPedidoDAO().registrar(pedidoEntidad);
	}

	private void guardarDetallesPedido(final List<DetallePedidoDominio> detalles) {
		for (DetallePedidoDominio detalle : detalles) {
			guardarDetallePedido(detalle);
		}
	}

	private void guardarDetallePedido(final DetallePedidoDominio detalle) {
		var detalleEntidad = DetallePedidoEntidadAssembler.getInstance()
				.ensamblarEntidad(detalle);

		daoFactory.obtenerDetallePedidoDAO()
				.registrar(detalleEntidad);
	}

	private String generarCodigoUnicoPedido() {
		String codigoPedido;
		PedidoEntidad pedidoExistente;

		do {
			codigoPedido = UtilCodigo.generarCodigo(
					PREFIJO_PEDIDO,
					CANTIDAD_DIGITOS_PEDIDO);

			pedidoExistente = daoFactory.obtenerPedidoDAO()
					.consultarPorId(codigoPedido);

		} while (UtilObjeto.noEsNulo(pedidoExistente)
				&& UtilTexto.tieneTexto(pedidoExistente.getCodigoPedido()));

		return codigoPedido;
	}

	private String generarCodigoUnicoDetallePedido(final List<String> codigosGenerados) {
		String codigoDetallePedido;
		DetallePedidoEntidad detalleExistente;

		do {
			codigoDetallePedido = UtilCodigo.generarCodigo(
					PREFIJO_DETALLE_PEDIDO,
					CANTIDAD_DIGITOS_DETALLE_PEDIDO);

			detalleExistente = daoFactory.obtenerDetallePedidoDAO()
					.consultarPorId(codigoDetallePedido);

		} while ((UtilObjeto.noEsNulo(detalleExistente)
				&& UtilTexto.tieneTexto(detalleExistente.getCodigoDetallePedido()))
				|| codigoYaFueGenerado(codigosGenerados, codigoDetallePedido));

		return codigoDetallePedido;
	}

	private boolean codigoYaFueGenerado(
			final List<String> codigosGenerados,
			final String codigoNuevo) {

		for (String codigoGenerado : codigosGenerados) {
			if (UtilTexto.sonIgualesIgnorandoMayusculas(
					codigoGenerado,
					codigoNuevo)) {

				return true;
			}
		}

		return false;
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