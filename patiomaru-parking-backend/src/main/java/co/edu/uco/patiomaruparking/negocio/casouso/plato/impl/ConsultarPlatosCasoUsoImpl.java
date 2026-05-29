package co.edu.uco.patiomaruparking.negocio.casouso.plato.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.PlatoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PlatoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.plato.ConsultarPlatosCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.PlatoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class ConsultarPlatosCasoUsoImpl implements ConsultarPlatosCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(ConsultarPlatosCasoUsoImpl.class);

	private static final String PREFIJO_PLATO = "PLT";
	private static final int LONGITUD_CODIGO_PLATO = 6;
	private static final int POSICION_INICIO_DIGITOS_PLATO = 3;

	private static final int LONGITUD_MINIMA_NOMBRE = 3;
	private static final int LONGITUD_MAXIMA_NOMBRE = 80;

	private static final int LONGITUD_CODIGO_CATEGORIA = 6;

	private static final int CANTIDAD_DECIMALES_PRECIO = 2;

	private final DAOFactory daoFactory;

	public ConsultarPlatosCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para consultar platos porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public List<PlatoDominio> ejecutar(final PlatoDominio filtro) {
		logger.info("Iniciando la consulta de platos.");

		var filtroSeguro = UtilObjeto.obtenerValorDefecto(
				filtro,
				PlatoDominio.builder().build());

		validarFiltro(filtroSeguro);

		var filtroEntidad = PlatoEntidadAssembler.getInstance()
				.ensamblarEntidad(filtroSeguro);

		var platosEntidad = UtilObjeto.obtenerValorDefecto(
				daoFactory.obtenerPlatoDAO().consultar(filtroEntidad),
				List.<PlatoEntidad>of());

		var platos = new ArrayList<PlatoDominio>();

		for (PlatoEntidad platoEntidad : platosEntidad) {
			var plato = PlatoEntidadAssembler.getInstance()
					.ensamblarDominio(platoEntidad);

			platos.add(plato);
		}

		logger.info("Consulta de platos finalizada satisfactoriamente.");

		return platos;
	}

	private void validarFiltro(final PlatoDominio filtro) {
		validarCodigoPlatoSiFueInformado(filtro);
		validarNombreSiFueInformado(filtro);
		validarCategoriaSiFueInformada(filtro);
		validarPrecioVentaSiFueInformado(filtro);
	}

	private void validarCodigoPlatoSiFueInformado(final PlatoDominio filtro) {
		if (!UtilTexto.tieneTexto(filtro.getCodigoPlato())) {
			return;
		}

		var codigoPlato = UtilTexto.aplicarTrimConvertirMayusculas(filtro.getCodigoPlato());

		if (codigoPlato.length() != LONGITUD_CODIGO_PLATO) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del plato debe tener exactamente "
							+ LONGITUD_CODIGO_PLATO + " caracteres.");
		}

		if (!iniciaConPrefijoPlato(codigoPlato)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del plato debe iniciar con " + PREFIJO_PLATO + ".");
		}

		if (!contieneSoloDigitos(
				codigoPlato.substring(POSICION_INICIO_DIGITOS_PLATO))) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del plato debe tener el formato PLT seguido de tres dígitos numéricos.");
		}
	}

	private void validarNombreSiFueInformado(final PlatoDominio filtro) {
		if (!UtilTexto.tieneTexto(filtro.getNombre())) {
			return;
		}

		var nombre = UtilTexto.aplicarTrim(filtro.getNombre());

		if (nombre.length() < LONGITUD_MINIMA_NOMBRE
				|| nombre.length() > LONGITUD_MAXIMA_NOMBRE) {

			throw NegocioPatioMaruExcepcion.crear(
					"El nombre del plato debe tener entre "
							+ LONGITUD_MINIMA_NOMBRE + " y "
							+ LONGITUD_MAXIMA_NOMBRE + " caracteres.");
		}
	}

	private void validarCategoriaSiFueInformada(final PlatoDominio filtro) {
		if (!UtilTexto.tieneTexto(filtro.getCategoria().getCodigoCategoria())) {
			return;
		}

		var codigoCategoria = UtilTexto.aplicarTrim(
				filtro.getCategoria().getCodigoCategoria());

		if (codigoCategoria.length() != LONGITUD_CODIGO_CATEGORIA) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código de la categoría debe tener exactamente "
							+ LONGITUD_CODIGO_CATEGORIA + " caracteres.");
		}
	}

	private void validarPrecioVentaSiFueInformado(final PlatoDominio filtro) {
		if (UtilObjeto.esNulo(filtro.getPrecioVenta())) {
			return;
		}

		var precioVenta = filtro.getPrecioVenta();

		if (precioVenta.compareTo(BigDecimal.ZERO) <= 0) {
			throw NegocioPatioMaruExcepcion.crear(
					"El precio de venta del plato debe ser mayor que cero.");
		}

		if (precioVenta.scale() > CANTIDAD_DECIMALES_PRECIO) {
			throw NegocioPatioMaruExcepcion.crear(
					"El precio de venta del plato solo puede tener máximo "
							+ CANTIDAD_DECIMALES_PRECIO + " decimales.");
		}
	}

	private boolean iniciaConPrefijoPlato(final String codigoPlato) {
		var prefijo = codigoPlato.substring(0, POSICION_INICIO_DIGITOS_PLATO);

		return UtilTexto.sonIgualesIgnorandoMayusculas(
				prefijo,
				PREFIJO_PLATO);
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