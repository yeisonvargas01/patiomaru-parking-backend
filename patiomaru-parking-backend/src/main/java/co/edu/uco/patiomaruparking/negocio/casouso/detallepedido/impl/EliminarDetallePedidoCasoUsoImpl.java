package co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.DetallePedidoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.EliminarDetallePedidoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.PedidoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class EliminarDetallePedidoCasoUsoImpl implements EliminarDetallePedidoCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(EliminarDetallePedidoCasoUsoImpl.class);

	private static final String PREFIJO_DETALLE_PEDIDO = "DP";
	private static final int LONGITUD_CODIGO_DETALLE_PEDIDO = 5;
	private static final int POSICION_INICIO_DIGITOS_DETALLE_PEDIDO = 2;

	private static final String PREFIJO_PEDIDO = "PEDD";
	private static final int LONGITUD_CODIGO_PEDIDO = 7;
	private static final int POSICION_INICIO_DIGITOS_PEDIDO = 4;

	private static final String ESTADO_REGISTRADO = "Registrado";

	private final DAOFactory daoFactory;

	public EliminarDetallePedidoCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para eliminar detalle de pedido porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final String codigoDetallePedido) {
		logger.info("Iniciando la eliminación de un detalle de pedido.");

		var codigoDetallePedidoNormalizado = validarYNormalizarCodigoDetallePedido(codigoDetallePedido);

		var detallePedidoEntidad = validarYObtenerDetallePedido(codigoDetallePedidoNormalizado);

		var codigoPedido = validarYNormalizarCodigoPedido(
				detallePedidoEntidad.getCodigoPedido());

		var pedido = validarYObtenerPedido(codigoPedido);

		validarPedidoPermiteEliminarDetalle(pedido);

		daoFactory.obtenerDetallePedidoDAO()
				.eliminar(codigoDetallePedidoNormalizado);

		logger.info("Detalle de pedido eliminado satisfactoriamente.");
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

		if (!iniciaConPrefijo(
				codigoDetallePedidoNormalizado,
				PREFIJO_DETALLE_PEDIDO,
				POSICION_INICIO_DIGITOS_DETALLE_PEDIDO)) {

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

	private DetallePedidoEntidad validarYObtenerDetallePedido(final String codigoDetallePedido) {
		var detallePedidoEntidad = daoFactory.obtenerDetallePedidoDAO()
				.consultarPorId(codigoDetallePedido);

		if (UtilObjeto.esNulo(detallePedidoEntidad)
				|| UtilTexto.esVacio(detallePedidoEntidad.getCodigoDetallePedido())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe un detalle de pedido registrado con el código indicado.");
		}

		return detallePedidoEntidad;
	}

	private String validarYNormalizarCodigoPedido(final String codigoPedido) {
		if (!UtilTexto.tieneTexto(codigoPedido)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El detalle del pedido no tiene un pedido asociado.");
		}

		var codigoPedidoNormalizado = UtilTexto.aplicarTrim(codigoPedido);

		if (codigoPedidoNormalizado.length() != LONGITUD_CODIGO_PEDIDO) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido asociado al detalle debe tener exactamente "
							+ LONGITUD_CODIGO_PEDIDO + " caracteres.");
		}

		if (!iniciaConPrefijo(
				codigoPedidoNormalizado,
				PREFIJO_PEDIDO,
				POSICION_INICIO_DIGITOS_PEDIDO)) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido asociado al detalle debe iniciar con " + PREFIJO_PEDIDO + ".");
		}

		if (!contieneSoloDigitos(
				codigoPedidoNormalizado.substring(POSICION_INICIO_DIGITOS_PEDIDO))) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido asociado al detalle debe tener el formato PEDD seguido de tres dígitos numéricos.");
		}

		return codigoPedidoNormalizado;
	}

	private PedidoDominio validarYObtenerPedido(final String codigoPedido) {
		var pedidoEntidad = daoFactory.obtenerPedidoDAO()
				.consultarPorId(codigoPedido);

		if (UtilObjeto.esNulo(pedidoEntidad)
				|| UtilTexto.esVacio(pedidoEntidad.getCodigoPedido())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe el pedido asociado al detalle indicado.");
		}

		return PedidoEntidadAssembler.getInstance()
				.ensamblarDominio(pedidoEntidad);
	}

	private void validarPedidoPermiteEliminarDetalle(final PedidoDominio pedido) {
		if (!UtilTexto.sonIgualesIgnorandoMayusculas(
				pedido.getEstado(),
				ESTADO_REGISTRADO)) {

			throw NegocioPatioMaruExcepcion.crear(
					"El detalle del pedido no puede quitarse porque el estado actual del pedido no permite modificar sus detalles.");
		}
	}

	private boolean iniciaConPrefijo(
			final String codigo,
			final String prefijoEsperado,
			final int posicionFinPrefijo) {

		var prefijo = UtilTexto.aplicarTrim(codigo)
				.substring(0, posicionFinPrefijo);

		return UtilTexto.sonIgualesIgnorandoMayusculas(
				prefijo,
				prefijoEsperado);
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