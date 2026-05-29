package co.edu.uco.patiomaruparking.negocio.casouso.pedido.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.pedido.ActualizarEstadoPedidoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.PedidoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;

public final class ActualizarEstadoPedidoCasoUsoImpl implements ActualizarEstadoPedidoCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(ActualizarEstadoPedidoCasoUsoImpl.class);

	private static final String PREFIJO_PEDIDO = "PEDD";
	private static final int LONGITUD_CODIGO_PEDIDO = 7;
	private static final int POSICION_INICIO_DIGITOS_PEDIDO = 4;

	private static final String ESTADO_REGISTRADO = "Registrado";
	private static final String ESTADO_EN_PREPARACION = "En preparación";
	private static final String ESTADO_PAGADO = "Pagado";
	private static final String ESTADO_ENTREGADO = "Entregado";
	private static final String ESTADO_CANCELADO = "Cancelado";

	private static final int LONGITUD_MINIMA_ESTADO = 6;
	private static final int LONGITUD_MAXIMA_ESTADO = 14;

	private final DAOFactory daoFactory;

	public ActualizarEstadoPedidoCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = UtilObjeto.obtenerValorDefecto(
				daoFactory,
				DAOFactory.getFactory());
	}

	@Override
	public void ejecutar(final PedidoDominio datos) {
		logger.info("Iniciando la actualización del estado de un pedido.");

		var pedidoActualizar = UtilObjeto.obtenerValorDefecto(
				datos,
				PedidoDominio.builder().build());

		var codigoPedido = validarYNormalizarCodigoPedido(
				pedidoActualizar.getCodigoPedido());

		var nuevoEstado = validarYNormalizarEstadoPedido(
				pedidoActualizar.getEstado());

		var pedidoEntidad = daoFactory.obtenerPedidoDAO()
				.consultarPorId(codigoPedido);

		if (UtilObjeto.esNulo(pedidoEntidad)
				|| UtilTexto.esVacio(pedidoEntidad.getCodigoPedido())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe un pedido registrado con el código indicado.");
		}

		var pedidoActual = PedidoEntidadAssembler.getInstance()
				.ensamblarDominio(pedidoEntidad);

		validarCambioEstadoPermitido(
				pedidoActual.getEstado(),
				nuevoEstado);

		daoFactory.obtenerPedidoDAO()
				.actualizarEstado(codigoPedido, nuevoEstado);

		logger.info("Estado del pedido actualizado satisfactoriamente.");
	}

	private String validarYNormalizarCodigoPedido(final String codigoPedido) {
		if (!UtilTexto.tieneTexto(codigoPedido)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido es obligatorio.");
		}

		var codigoPedidoNormalizado = UtilTexto.aplicarTrim(codigoPedido);

		if (codigoPedidoNormalizado.length() != LONGITUD_CODIGO_PEDIDO) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido debe tener exactamente "
							+ LONGITUD_CODIGO_PEDIDO + " caracteres.");
		}

		if (!codigoPedidoNormalizado.startsWith(PREFIJO_PEDIDO)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido debe iniciar con " + PREFIJO_PEDIDO + ".");
		}

		if (!contieneSoloDigitos(codigoPedidoNormalizado.substring(POSICION_INICIO_DIGITOS_PEDIDO))) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido debe tener el formato PEDD seguido de tres dígitos numéricos.");
		}

		return codigoPedidoNormalizado;
	}

	private String validarYNormalizarEstadoPedido(final String estado) {
		if (!UtilTexto.tieneTexto(estado)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El nuevo estado del pedido es obligatorio.");
		}

		var estadoNormalizado = normalizarEstadoPermitido(estado);

		validarLongitud(
				estadoNormalizado,
				LONGITUD_MINIMA_ESTADO,
				LONGITUD_MAXIMA_ESTADO,
				"El estado del pedido");

		if (!esEstadoActualizableValido(estadoNormalizado)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El estado del pedido solo puede ser Registrado, En preparación, Pagado o Entregado.");
		}

		return estadoNormalizado;
	}

	private String normalizarEstadoPermitido(final String estado) {
		if (UtilTexto.sonIgualesIgnorandoMayusculas(estado, ESTADO_REGISTRADO)) {
			return ESTADO_REGISTRADO;
		}

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estado, ESTADO_EN_PREPARACION)) {
			return ESTADO_EN_PREPARACION;
		}

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estado, ESTADO_PAGADO)) {
			return ESTADO_PAGADO;
		}

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estado, ESTADO_ENTREGADO)) {
			return ESTADO_ENTREGADO;
		}

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estado, ESTADO_CANCELADO)) {
			return ESTADO_CANCELADO;
		}

		return UtilTexto.aplicarTrim(estado);
	}

	private boolean esEstadoActualizableValido(final String estado) {
		return UtilTexto.sonIgualesIgnorandoMayusculas(estado, ESTADO_REGISTRADO)
				|| UtilTexto.sonIgualesIgnorandoMayusculas(estado, ESTADO_EN_PREPARACION)
				|| UtilTexto.sonIgualesIgnorandoMayusculas(estado, ESTADO_PAGADO)
				|| UtilTexto.sonIgualesIgnorandoMayusculas(estado, ESTADO_ENTREGADO);
	}

	private void validarCambioEstadoPermitido(
			final String estadoActual,
			final String nuevoEstado) {

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoActual, ESTADO_CANCELADO)) {
			throw NegocioPatioMaruExcepcion.crear(
					"No es posible actualizar el estado de un pedido cancelado.");
		}

		if (UtilTexto.sonIgualesIgnorandoMayusculas(nuevoEstado, ESTADO_CANCELADO)) {
			throw NegocioPatioMaruExcepcion.crear(
					"Para cancelar un pedido debe usar la operación Cancelar Pedido.");
		}

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoActual, nuevoEstado)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El pedido ya se encuentra en el estado indicado.");
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

	private boolean contieneSoloDigitos(final String valor) {
		var valorSeguro = UtilTexto.aplicarTrim(valor);

		for (int indice = 0; indice < valorSeguro.length(); indice++) {
			if (!Character.isDigit(valorSeguro.charAt(indice))) {
				return false;
			}
		}

		return true;
	}
}