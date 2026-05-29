package co.edu.uco.patiomaruparking.negocio.casouso.pedido.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.DetallePedidoEntidad;
import co.edu.uco.patiomaruparking.entidad.PedidoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.DetallePedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.pedido.ConsultarPedidosCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio;
import co.edu.uco.patiomaruparking.negocio.dominio.PedidoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;

public final class ConsultarPedidosCasoUsoImpl implements ConsultarPedidosCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(ConsultarPedidosCasoUsoImpl.class);

	private static final String PREFIJO_PEDIDO = "PEDD";
	private static final int LONGITUD_CODIGO_PEDIDO = 7;
	private static final int POSICION_INICIO_DIGITOS_PEDIDO = 4;

	private static final String TIPO_ATENCION_MESA = "Mesa";
	private static final String TIPO_ATENCION_PARA_LLEVAR = "Para llevar";

	private static final String ESTADO_REGISTRADO = "Registrado";
	private static final String ESTADO_PAGADO = "Pagado";
	private static final String ESTADO_ENTREGADO = "Entregado";
	private static final String ESTADO_EN_PREPARACION = "En preparación";

	private static final int LONGITUD_MINIMA_TIPO_ATENCION = 4;
	private static final int LONGITUD_MAXIMA_TIPO_ATENCION = 11;

	private static final int LONGITUD_MINIMA_ESTADO = 6;
	private static final int LONGITUD_MAXIMA_ESTADO = 14;

	private final DAOFactory daoFactory;

	public ConsultarPedidosCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = UtilObjeto.obtenerValorDefecto(
				daoFactory,
				DAOFactory.getFactory());
	}

	@Override
	public List<PedidoDominio> ejecutar(final PedidoDominio filtro) {
		logger.info("Iniciando la consulta de pedidos.");

		var filtroSeguro = UtilObjeto.obtenerValorDefecto(
				filtro,
				PedidoDominio.builder().build());

		validarFiltro(filtroSeguro);

		var filtroEntidad = PedidoEntidadAssembler.getInstance()
				.ensamblarEntidad(filtroSeguro);

		var pedidosEntidad = UtilObjeto.obtenerValorDefecto(
				daoFactory.obtenerPedidoDAO().consultar(filtroEntidad),
				List.<PedidoEntidad>of());

		var pedidos = new ArrayList<PedidoDominio>();

		for (PedidoEntidad pedidoEntidad : pedidosEntidad) {
			var pedido = PedidoEntidadAssembler.getInstance()
					.ensamblarDominio(pedidoEntidad);

			pedidos.add(agregarDetallesAlPedido(pedido));
		}

		logger.info("Consulta de pedidos finalizada satisfactoriamente.");

		return pedidos;
	}

	private void validarFiltro(final PedidoDominio filtro) {
		validarCodigoPedidoSiFueInformado(filtro);
		validarTipoAtencionSiFueInformado(filtro);
		validarEstadoSiFueInformado(filtro);
	}

	private void validarCodigoPedidoSiFueInformado(final PedidoDominio filtro) {
		if (!UtilTexto.tieneTexto(filtro.getCodigoPedido())) {
			return;
		}

		var codigoPedido = UtilTexto.aplicarTrim(filtro.getCodigoPedido());

		if (codigoPedido.length() != LONGITUD_CODIGO_PEDIDO) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido debe tener exactamente "
							+ LONGITUD_CODIGO_PEDIDO + " caracteres.");
		}

		if (!codigoPedido.startsWith(PREFIJO_PEDIDO)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido debe iniciar con " + PREFIJO_PEDIDO + ".");
		}

		if (!contieneSoloDigitos(codigoPedido.substring(POSICION_INICIO_DIGITOS_PEDIDO))) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido debe tener el formato PEDD seguido de tres dígitos numéricos.");
		}
	}

	private void validarTipoAtencionSiFueInformado(final PedidoDominio filtro) {
		if (!UtilTexto.tieneTexto(filtro.getTipoAtencion())) {
			return;
		}

		validarLongitud(
				filtro.getTipoAtencion(),
				LONGITUD_MINIMA_TIPO_ATENCION,
				LONGITUD_MAXIMA_TIPO_ATENCION,
				"El tipo de atención");

		if (!esTipoAtencionValido(filtro.getTipoAtencion())) {
			throw NegocioPatioMaruExcepcion.crear(
					"El tipo de atención del pedido solo puede ser Mesa o Para llevar.");
		}
	}

	private void validarEstadoSiFueInformado(final PedidoDominio filtro) {
		if (!UtilTexto.tieneTexto(filtro.getEstado())) {
			return;
		}

		validarLongitud(
				filtro.getEstado(),
				LONGITUD_MINIMA_ESTADO,
				LONGITUD_MAXIMA_ESTADO,
				"El estado del pedido");

		if (!esEstadoValido(filtro.getEstado())) {
			throw NegocioPatioMaruExcepcion.crear(
					"El estado del pedido solo puede ser Registrado, Pagado, Entregado o En preparación.");
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
		return UtilTexto.sonIgualesIgnorandoMayusculas(tipoAtencion, TIPO_ATENCION_MESA)
				|| UtilTexto.sonIgualesIgnorandoMayusculas(tipoAtencion, TIPO_ATENCION_PARA_LLEVAR);
	}

	private boolean esEstadoValido(final String estado) {
		return UtilTexto.sonIgualesIgnorandoMayusculas(estado, ESTADO_REGISTRADO)
				|| UtilTexto.sonIgualesIgnorandoMayusculas(estado, ESTADO_PAGADO)
				|| UtilTexto.sonIgualesIgnorandoMayusculas(estado, ESTADO_ENTREGADO)
				|| UtilTexto.sonIgualesIgnorandoMayusculas(estado, ESTADO_EN_PREPARACION);
	}

	private boolean contieneSoloDigitos(final String valor) {
		var valorSeguro = UtilTexto.aplicarTrim(valor);

		for (int indice = 0; indice < valorSeguro.length(); indice++) {
			if (!Character.isDigit(valorSeguro.charAt(indice))) {
				return false;
			}
		}

		return true;
	}

	private PedidoDominio agregarDetallesAlPedido(final PedidoDominio pedido) {
		var pedidoSeguro = UtilObjeto.obtenerValorDefecto(
				pedido,
				PedidoDominio.builder().build());

		if (!UtilTexto.tieneTexto(pedidoSeguro.getCodigoPedido())) {
			return pedidoSeguro;
		}

		var detalles = consultarDetallesDelPedido(pedidoSeguro.getCodigoPedido());

		return PedidoDominio.builder()
				.codigoPedido(pedidoSeguro.getCodigoPedido())
				.fechaRegistro(pedidoSeguro.getFechaRegistro())
				.horaRegistro(pedidoSeguro.getHoraRegistro())
				.tipoAtencion(pedidoSeguro.getTipoAtencion())
				.estado(pedidoSeguro.getEstado())
				.totalPedido(pedidoSeguro.getTotalPedido())
				.mesa(pedidoSeguro.getMesa())
				.cliente(pedidoSeguro.getCliente())
				.empleado(pedidoSeguro.getEmpleado())
				.detalles(detalles)
				.build();
	}

	private List<DetallePedidoDominio> consultarDetallesDelPedido(final String codigoPedido) {
		var filtroDetalle = DetallePedidoEntidad.builder()
				.codigoPedido(UtilTexto.aplicarTrim(codigoPedido))
				.build();

		var detallesEntidad = UtilObjeto.obtenerValorDefecto(
				daoFactory.obtenerDetallePedidoDAO().consultar(filtroDetalle),
				List.<DetallePedidoEntidad>of());

		var detalles = new ArrayList<DetallePedidoDominio>();

		for (DetallePedidoEntidad detalleEntidad : detallesEntidad) {
			var detalle = DetallePedidoEntidadAssembler.getInstance()
					.ensamblarDominio(detalleEntidad);

			detalles.add(detalle);
		}

		return detalles;
	}
}