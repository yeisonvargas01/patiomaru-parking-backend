package co.edu.uco.patiomaruparking.negocio.casouso.plato.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.CategoriaEntidad;
import co.edu.uco.patiomaruparking.entidad.PlatoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PlatoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.plato.ActualizarPlatoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.PlatoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class ActualizarPlatoCasoUsoImpl implements ActualizarPlatoCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(ActualizarPlatoCasoUsoImpl.class);

	private static final String PREFIJO_PLATO = "PLT";
	private static final int LONGITUD_CODIGO_PLATO = 6;
	private static final int POSICION_INICIO_DIGITOS_PLATO = 3;

	private static final int LONGITUD_MINIMA_NOMBRE = 3;
	private static final int LONGITUD_MAXIMA_NOMBRE = 80;

	private static final int LONGITUD_CODIGO_CATEGORIA = 6;

	private static final int CANTIDAD_DECIMALES_PRECIO = 2;

	private final DAOFactory daoFactory;

	public ActualizarPlatoCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para actualizar plato porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final PlatoDominio datos) {
		logger.info("Iniciando la actualización de un plato.");

		var platoActualizar = UtilObjeto.obtenerValorDefecto(
				datos,
				PlatoDominio.builder().build());

		var codigoPlato = validarYNormalizarCodigoPlato(
				platoActualizar.getCodigoPlato());

		validarDatosConsistentes(platoActualizar);

		var platoActual = validarYObtenerPlato(codigoPlato);

		validarNoExisteOtroPlatoConMismoNombreYCategoria(
				codigoPlato,
				platoActualizar);

		var platoActualizado = PlatoDominio.builder()
				.codigoPlato(codigoPlato)
				.nombre(UtilTexto.aplicarTrim(platoActualizar.getNombre()))
				.categoria(platoActualizar.getCategoria())
				.precioVenta(platoActualizar.getPrecioVenta())
				.estado(platoActual.getEstado())
				.build();

		actualizar(platoActualizado);

		logger.info("Plato actualizado satisfactoriamente.");
	}

	private String validarYNormalizarCodigoPlato(final String codigoPlato) {
		if (!UtilTexto.tieneTexto(codigoPlato)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del plato es obligatorio.");
		}

		var codigoPlatoNormalizado = UtilTexto.aplicarTrimConvertirMayusculas(codigoPlato);

		if (codigoPlatoNormalizado.length() != LONGITUD_CODIGO_PLATO) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del plato debe tener exactamente "
							+ LONGITUD_CODIGO_PLATO + " caracteres.");
		}

		if (!iniciaConPrefijoPlato(codigoPlatoNormalizado)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del plato debe iniciar con " + PREFIJO_PLATO + ".");
		}

		if (!contieneSoloDigitos(
				codigoPlatoNormalizado.substring(POSICION_INICIO_DIGITOS_PLATO))) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del plato debe tener el formato PLT seguido de tres dígitos numéricos.");
		}

		return codigoPlatoNormalizado;
	}

	private void validarDatosConsistentes(final PlatoDominio plato) {
		validarNombre(plato.getNombre());
		validarCategoria(plato);
		validarPrecioVenta(plato.getPrecioVenta());
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

	private PlatoDominio validarYObtenerPlato(final String codigoPlato) {
		var platoEntidad = daoFactory.obtenerPlatoDAO()
				.consultarPorId(codigoPlato);

		if (UtilObjeto.esNulo(platoEntidad)
				|| !UtilTexto.tieneTexto(platoEntidad.getCodigoPlato())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe un plato registrado con el código indicado.");
		}

		return PlatoEntidadAssembler.getInstance()
				.ensamblarDominio(platoEntidad);
	}

	private void validarNoExisteOtroPlatoConMismoNombreYCategoria(
			final String codigoPlato,
			final PlatoDominio plato) {

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

		for (PlatoEntidad platoEntidad : resultados) {
			var platoSeguro = UtilObjeto.obtenerValorDefecto(
					platoEntidad,
					PlatoEntidad.builder().build());

			if (!UtilTexto.sonIgualesIgnorandoMayusculas(
					platoSeguro.getCodigoPlato(),
					codigoPlato)) {

				throw NegocioPatioMaruExcepcion.crear(
						"Ya existe otro plato registrado con el mismo nombre dentro de la misma categoría.");
			}
		}
	}

	private void actualizar(final PlatoDominio plato) {
		var platoEntidad = PlatoEntidadAssembler.getInstance()
				.ensamblarEntidad(plato);

		daoFactory.obtenerPlatoDAO()
				.actualizar(platoEntidad);
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