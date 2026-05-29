package co.edu.uco.patiomaruparking.negocio.casouso.pedido.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.pedido.CancelarPedidoCasoUso;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;

public final class CancelarPedidoCasoUsoImpl implements CancelarPedidoCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(CancelarPedidoCasoUsoImpl.class);

	private static final String PREFIJO_PEDIDO = "PEDD";
	private static final int LONGITUD_CODIGO_PEDIDO = 7;
	private static final int POSICION_INICIO_DIGITOS_PEDIDO = 4;

	private static final String ESTADO_CANCELADO = "Cancelado";
	private static final String ESTADO_ENTREGADO = "Entregado";

	private final DAOFactory daoFactory;

	public CancelarPedidoCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = UtilObjeto.obtenerValorDefecto(
				daoFactory,
				DAOFactory.getFactory());
	}

	@Override
	public void ejecutar(final String codigoPedido) {
		logger.info("Iniciando la cancelación de un pedido.");

		var codigoPedidoNormalizado = validarYNormalizarCodigoPedido(codigoPedido);

		var pedidoEntidad = daoFactory.obtenerPedidoDAO()
				.consultarPorId(codigoPedidoNormalizado);

		if (UtilObjeto.esNulo(pedidoEntidad)
				|| UtilTexto.esVacio(pedidoEntidad.getCodigoPedido())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe un pedido registrado con el código indicado.");
		}

		var pedido = PedidoEntidadAssembler.getInstance()
				.ensamblarDominio(pedidoEntidad);

		validarPedidoCancelable(pedido.getEstado());

		daoFactory.obtenerPedidoDAO()
				.cancelar(codigoPedidoNormalizado);

		logger.info("Pedido cancelado satisfactoriamente.");
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

	private boolean contieneSoloDigitos(final String valor) {
		var valorSeguro = UtilTexto.aplicarTrim(valor);

		for (int indice = 0; indice < valorSeguro.length(); indice++) {
			if (!Character.isDigit(valorSeguro.charAt(indice))) {
				return false;
			}
		}

		return true;
	}

	private void validarPedidoCancelable(final String estadoActual) {
		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoActual, ESTADO_CANCELADO)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El pedido ya se encuentra cancelado.");
		}

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoActual, ESTADO_ENTREGADO)) {
			throw NegocioPatioMaruExcepcion.crear(
					"No es posible cancelar un pedido que ya fue entregado.");
		}
	}
}