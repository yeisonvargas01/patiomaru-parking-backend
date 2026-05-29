package co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.DetallePedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.ConsultarDetallePedidoPorIdCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class ConsultarDetallePedidoPorIdCasoUsoImpl implements ConsultarDetallePedidoPorIdCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(ConsultarDetallePedidoPorIdCasoUsoImpl.class);

	private static final String PREFIJO_DETALLE_PEDIDO = "DP";
	private static final int LONGITUD_CODIGO_DETALLE_PEDIDO = 5;
	private static final int POSICION_INICIO_DIGITOS_DETALLE_PEDIDO = 2;

	private final DAOFactory daoFactory;

	public ConsultarDetallePedidoPorIdCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para consultar detalle de pedido por identificador porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public DetallePedidoDominio ejecutar(final String codigoDetallePedido) {
		logger.info("Iniciando la consulta de un detalle de pedido por identificador.");

		var codigoDetallePedidoNormalizado = validarYNormalizarCodigoDetallePedido(codigoDetallePedido);

		var detallePedidoEntidad = daoFactory.obtenerDetallePedidoDAO()
				.consultarPorId(codigoDetallePedidoNormalizado);

		if (UtilObjeto.esNulo(detallePedidoEntidad)
				|| UtilTexto.esVacio(detallePedidoEntidad.getCodigoDetallePedido())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe un detalle de pedido registrado con el código indicado.");
		}

		var detallePedido = DetallePedidoEntidadAssembler.getInstance()
				.ensamblarDominio(detallePedidoEntidad);

		logger.info("Detalle de pedido consultado satisfactoriamente.");

		return detallePedido;
	}

	private String validarYNormalizarCodigoDetallePedido(final String codigoDetallePedido) {
		if (!UtilTexto.tieneTexto(codigoDetallePedido)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del detalle del pedido es obligatorio.");
		}

		var codigoDetallePedidoNormalizado = UtilTexto.aplicarTrim(codigoDetallePedido);

		if (codigoDetallePedidoNormalizado.length() != LONGITUD_CODIGO_DETALLE_PEDIDO) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del detalle del pedido debe tener exactamente "
							+ LONGITUD_CODIGO_DETALLE_PEDIDO + " caracteres.");
		}

		if (!iniciaConPrefijoDetallePedido(codigoDetallePedidoNormalizado)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del detalle del pedido debe iniciar con " + PREFIJO_DETALLE_PEDIDO + ".");
		}

		if (!contieneSoloDigitos(
				codigoDetallePedidoNormalizado.substring(POSICION_INICIO_DIGITOS_DETALLE_PEDIDO))) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del detalle del pedido debe tener el formato DP seguido de tres dígitos numéricos.");
		}

		return codigoDetallePedidoNormalizado;
	}

	private boolean iniciaConPrefijoDetallePedido(final String codigoDetallePedido) {
		var prefijo = codigoDetallePedido.substring(0, POSICION_INICIO_DIGITOS_DETALLE_PEDIDO);

		return UtilTexto.sonIgualesIgnorandoMayusculas(
				prefijo,
				PREFIJO_DETALLE_PEDIDO);
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