package co.edu.uco.patiomaruparking.negocio.casouso.plato.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.CategoriaEntidad;
import co.edu.uco.patiomaruparking.entidad.PlatoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PlatoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.plato.RegistrarPlatoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.PlatoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilCodigo;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class RegistrarPlatoCasoUsoImpl implements RegistrarPlatoCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(RegistrarPlatoCasoUsoImpl.class);

	private static final String PREFIJO_PLATO = "PLT";
	private static final int CANTIDAD_DIGITOS_PLATO = 3;
	private static final int LONGITUD_CODIGO_PLATO = 6;
	private static final int POSICION_INICIO_DIGITOS_PLATO = 3;

	private static final int LONGITUD_MINIMA_NOMBRE = 3;
	private static final int LONGITUD_MAXIMA_NOMBRE = 80;

	private static final int LONGITUD_CODIGO_CATEGORIA = 6;

	private static final int CANTIDAD_DECIMALES_PRECIO = 2;

	private final DAOFactory daoFactory;

	public RegistrarPlatoCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para registrar plato porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public PlatoDominio ejecutar(final PlatoDominio datos) {
		logger.info("Iniciando el registro de un plato.");

		var plato = UtilObjeto.obtenerValorDefecto(
				datos,
				PlatoDominio.builder().build());

		validarDatosConsistentes(plato);

		validarNoExistePlatoConMismoNombreYCategoria(plato);

		var codigoPlato = generarCodigoUnicoPlato();

		var platoPreparado = PlatoDominio.builder()
				.codigoPlato(codigoPlato)
				.nombre(UtilTexto.aplicarTrim(plato.getNombre()))
				.categoria(plato.getCategoria())
				.precioVenta(plato.getPrecioVenta())
				.estado(plato.getEstado())
				.build();

		guardar(platoPreparado);

		logger.info("Plato registrado satisfactoriamente.");

		return platoPreparado;
	}

	private void validarDatosConsistentes(final PlatoDominio plato) {
		validarNombre(plato.getNombre());
		validarCategoria(plato);
		validarPrecioVenta(plato.getPrecioVenta());
		validarEstado(plato.getEstado());
	}

	private void validarNombre(final String nombre) {
		if (!UtilTexto.tieneTexto(nombre)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El nombre del plato es obligatorio.");
		}

		var nombreSeguro = UtilTexto.aplicarTrim(nombre);

		if (nombreSeguro.length() < LONGITUD_MINIMA_NOMBRE
				|| nombreSeguro.length() > LONGITUD_MAXIMA_NOMBRE) {

			throw NegocioPatioMaruExcepcion.crear(
					"El nombre del plato debe tener entre "
							+ LONGITUD_MINIMA_NOMBRE + " y "
							+ LONGITUD_MAXIMA_NOMBRE + " caracteres.");
		}
	}

	private void validarCategoria(final PlatoDominio plato) {
		if (!UtilTexto.tieneTexto(plato.getCategoria().getCodigoCategoria())) {
			throw NegocioPatioMaruExcepcion.crear(
					"La categoría del plato es obligatoria.");
		}

		var codigoCategoria = UtilTexto.aplicarTrim(
				plato.getCategoria().getCodigoCategoria());

		if (codigoCategoria.length() != LONGITUD_CODIGO_CATEGORIA) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código de la categoría debe tener exactamente "
							+ LONGITUD_CODIGO_CATEGORIA + " caracteres.");
		}
	}

	private void validarPrecioVenta(final BigDecimal precioVenta) {
		if (UtilObjeto.esNulo(precioVenta)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El precio de venta del plato es obligatorio.");
		}

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

	private void validarEstado(final Boolean estado) {
		if (UtilObjeto.esNulo(estado)) {
			throw NegocioPatioMaruExcepcion.crear(
					"La disponibilidad inicial del plato es obligatoria.");
		}
	}

	private void validarNoExistePlatoConMismoNombreYCategoria(final PlatoDominio plato) {
		var filtro = PlatoEntidad.builder()
				.nombre(UtilTexto.aplicarTrim(plato.getNombre()))
				.categoria(CategoriaEntidad.builder()
						.codigoCategoria(UtilTexto.aplicarTrim(
								plato.getCategoria().getCodigoCategoria()))
						.build())
				.build();

		var resultados = UtilObjeto.obtenerValorDefecto(
				daoFactory.obtenerPlatoDAO().consultar(filtro),
				List.<PlatoEntidad>of());

		if (!resultados.isEmpty()) {
			throw NegocioPatioMaruExcepcion.crear(
					"Ya existe un plato registrado con el mismo nombre dentro de la misma categoría.");
		}
	}

	private String generarCodigoUnicoPlato() {
		String codigoPlato;
		PlatoEntidad platoExistente;

		do {
			codigoPlato = UtilCodigo.generarCodigo(
					PREFIJO_PLATO,
					CANTIDAD_DIGITOS_PLATO);

			validarFormatoCodigoPlatoGenerado(codigoPlato);

			platoExistente = daoFactory.obtenerPlatoDAO()
					.consultarPorId(codigoPlato);

		} while (UtilObjeto.noEsNulo(platoExistente)
				&& UtilTexto.tieneTexto(platoExistente.getCodigoPlato()));

		return codigoPlato;
	}

	private void validarFormatoCodigoPlatoGenerado(final String codigoPlato) {
		var codigoPlatoSeguro = UtilTexto.aplicarTrimConvertirMayusculas(codigoPlato);

		if (codigoPlatoSeguro.length() != LONGITUD_CODIGO_PLATO) {
			throw TransversalPatioMaruExcepcion.crear(
					"El código del plato generado no cumple con la longitud esperada.");
		}

		if (!codigoPlatoSeguro.startsWith(PREFIJO_PLATO)) {
			throw TransversalPatioMaruExcepcion.crear(
					"El código del plato generado no cumple con el prefijo esperado.");
		}

		if (!contieneSoloDigitos(
				codigoPlatoSeguro.substring(POSICION_INICIO_DIGITOS_PLATO))) {

			throw TransversalPatioMaruExcepcion.crear(
					"El código del plato generado no cumple con el formato esperado.");
		}
	}

	private void guardar(final PlatoDominio plato) {
		var platoEntidad = PlatoEntidadAssembler.getInstance()
				.ensamblarEntidad(plato);

		daoFactory.obtenerPlatoDAO()
				.registrar(platoEntidad);
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