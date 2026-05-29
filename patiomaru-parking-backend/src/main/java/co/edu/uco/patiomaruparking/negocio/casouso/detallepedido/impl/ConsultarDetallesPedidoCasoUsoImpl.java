package co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.DetallePedidoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.DetallePedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.ConsultarDetallesPedidoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class ConsultarDetallesPedidoCasoUsoImpl implements ConsultarDetallesPedidoCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(ConsultarDetallesPedidoCasoUsoImpl.class);

	private static final String PREFIJO_DETALLE_PEDIDO = "DP";
	private static final int LONGITUD_CODIGO_DETALLE_PEDIDO = 5;
	private static final int POSICION_INICIO_DIGITOS_DETALLE_PEDIDO = 2;

	private static final String PREFIJO_PEDIDO = "PEDD";
	private static final int LONGITUD_CODIGO_PEDIDO = 7;
	private static final int POSICION_INICIO_DIGITOS_PEDIDO = 4;

	private static final String PREFIJO_PLATO = "PLT";
	private static final int LONGITUD_CODIGO_PLATO = 6;
	private static final int POSICION_INICIO_DIGITOS_PLATO = 3;

	private static final int CANTIDAD_MINIMA_PERMITIDA = 1;

	private final DAOFactory daoFactory;

	public ConsultarDetallesPedidoCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para consultar detalles de pedido porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public List<DetallePedidoDominio> ejecutar(final DetallePedidoDominio filtro) {
		logger.info("Iniciando la consulta de detalles de pedido.");

		var filtroSeguro = UtilObjeto.obtenerValorDefecto(
				filtro,
				DetallePedidoDominio.builder().build());

		validarFiltro(filtroSeguro);

		var filtroEntidad = DetallePedidoEntidadAssembler.getInstance()
				.ensamblarEntidad(filtroSeguro);

		var detallesEntidad = UtilObjeto.obtenerValorDefecto(
				daoFactory.obtenerDetallePedidoDAO().consultar(filtroEntidad),
				List.<DetallePedidoEntidad>of());

		var detalles = new ArrayList<DetallePedidoDominio>();

		for (DetallePedidoEntidad detalleEntidad : detallesEntidad) {
			var detalle = DetallePedidoEntidadAssembler.getInstance()
					.ensamblarDominio(detalleEntidad);

			detalles.add(detalle);
		}

		logger.info("Consulta de detalles de pedido finalizada satisfactoriamente.");

		return detalles;
	}

	private void validarFiltro(final DetallePedidoDominio filtro) {
		validarCodigoDetallePedidoSiFueInformado(filtro);
		validarCodigoPedidoSiFueInformado(filtro);
		validarCantidadSiFueInformada(filtro);
		validarCodigoPlatoSiFueInformado(filtro);
	}

	private void validarCodigoDetallePedidoSiFueInformado(final DetallePedidoDominio filtro) {
		if (!UtilTexto.tieneTexto(filtro.getCodigoDetallePedido())) {
			return;
		}

		var codigoDetallePedido = UtilTexto.aplicarTrim(filtro.getCodigoDetallePedido());

		if (codigoDetallePedido.length() != LONGITUD_CODIGO_DETALLE_PEDIDO) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del detalle del pedido debe tener exactamente "
							+ LONGITUD_CODIGO_DETALLE_PEDIDO + " caracteres.");
		}

		if (!iniciaConPrefijo(
				codigoDetallePedido,
				PREFIJO_DETALLE_PEDIDO,
				POSICION_INICIO_DIGITOS_DETALLE_PEDIDO)) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del detalle del pedido debe iniciar con " + PREFIJO_DETALLE_PEDIDO + ".");
		}

		if (!contieneSoloDigitos(
				codigoDetallePedido.substring(POSICION_INICIO_DIGITOS_DETALLE_PEDIDO))) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del detalle del pedido debe tener el formato DP seguido de tres dígitos numéricos.");
		}
	}

	private void validarCodigoPedidoSiFueInformado(final DetallePedidoDominio filtro) {
		if (!UtilTexto.tieneTexto(filtro.getCodigoPedido())) {
			return;
		}

		var codigoPedido = UtilTexto.aplicarTrim(filtro.getCodigoPedido());

		if (codigoPedido.length() != LONGITUD_CODIGO_PEDIDO) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido debe tener exactamente "
							+ LONGITUD_CODIGO_PEDIDO + " caracteres.");
		}

		if (!iniciaConPrefijo(
				codigoPedido,
				PREFIJO_PEDIDO,
				POSICION_INICIO_DIGITOS_PEDIDO)) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido debe iniciar con " + PREFIJO_PEDIDO + ".");
		}

		if (!contieneSoloDigitos(
				codigoPedido.substring(POSICION_INICIO_DIGITOS_PEDIDO))) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido debe tener el formato PEDD seguido de tres dígitos numéricos.");
		}
	}

	private void validarCantidadSiFueInformada(final DetallePedidoDominio filtro) {
		if (UtilObjeto.esNulo(filtro.getCantidad())) {
			return;
		}

		if (filtro.getCantidad() < CANTIDAD_MINIMA_PERMITIDA) {
			throw NegocioPatioMaruExcepcion.crear(
					"La cantidad del detalle del pedido debe ser mayor que cero.");
		}
	}

	private void validarCodigoPlatoSiFueInformado(final DetallePedidoDominio filtro) {
		if (!UtilTexto.tieneTexto(filtro.getPlato().getCodigoPlato())) {
			return;
		}

		var codigoPlato = UtilTexto.aplicarTrim(
				filtro.getPlato().getCodigoPlato());

		if (codigoPlato.length() != LONGITUD_CODIGO_PLATO) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del plato debe tener exactamente "
							+ LONGITUD_CODIGO_PLATO + " caracteres.");
		}

		if (!iniciaConPrefijo(
				codigoPlato,
				PREFIJO_PLATO,
				POSICION_INICIO_DIGITOS_PLATO)) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del plato debe iniciar con " + PREFIJO_PLATO + ".");
		}

		if (!contieneSoloDigitos(
				codigoPlato.substring(POSICION_INICIO_DIGITOS_PLATO))) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del plato debe tener el formato PLT seguido de tres dígitos numéricos.");
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
