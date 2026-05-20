package co.edu.uco.patiomaruparking.negocio.casouso.plato.impl;

import java.math.BigDecimal;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.PlatoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PlatoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.plato.RegistrarPlatoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.PlatoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilCodigo;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public final class RegistrarPlatoCasoUsoImpl implements RegistrarPlatoCasoUso {

	private static final int LONGITUD_CODIGO_PLATO = 6;
	private static final int LONGITUD_MAXIMA_NOMBRE = 80;
	private static final int LONGITUD_CODIGO_CATEGORIA = 6;

	private final DAOFactory daoFactory;

	public RegistrarPlatoCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public PlatoDominio ejecutar(final PlatoDominio datos) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango.
		validarDatosConsistentes(datos);

		// 2. No debe existir un plato con el mismo nombre dentro de la misma categoría.
		validarNoExistePlatoConMismoNombreYCategoria(datos);

		// 3. El código del plato debe ser único.
		var codigoPlato = generarCodigoUnicoPlato();

		var platoPreparado = PlatoDominio.builder()
				.codigoPlato(codigoPlato)
				.nombre(UtilTexto.aplicarTrim(datos.getNombre()))
				.categoria(datos.getCategoria())
				.precioVenta(datos.getPrecioVenta())
				.estado(datos.getEstado())
				.build();

		guardar(platoPreparado);

		return platoPreparado;
	}

	private void validarDatosConsistentes(final PlatoDominio datos) {
		if (UtilObjeto.esNulo(datos)) {
			throw new RuntimeException("Los datos del plato son obligatorios.");
		}

		if (!UtilTexto.tieneTexto(datos.getNombre())) {
			throw new RuntimeException("El nombre del plato es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(datos.getNombre()).length() > LONGITUD_MAXIMA_NOMBRE) {
			throw new RuntimeException("El nombre del plato no puede superar "
					+ LONGITUD_MAXIMA_NOMBRE + " caracteres.");
		}

		if (UtilObjeto.esNulo(datos.getPrecioVenta())) {
			throw new RuntimeException("El precio de venta del plato es obligatorio.");
		}

		if (datos.getPrecioVenta().compareTo(BigDecimal.ZERO) <= 0) {
			throw new RuntimeException("El precio de venta del plato debe ser mayor que cero.");
		}

		if (UtilObjeto.esNulo(datos.getEstado())) {
			throw new RuntimeException("El estado del plato es obligatorio.");
		}

		if (UtilObjeto.esNulo(datos.getCategoria())
				|| !UtilTexto.tieneTexto(datos.getCategoria().getCodigoCategoria())) {
			throw new RuntimeException("La categoría del plato es obligatoria.");
		}

		if (UtilTexto.aplicarTrim(datos.getCategoria().getCodigoCategoria()).length() != LONGITUD_CODIGO_CATEGORIA) {
			throw new RuntimeException("El código de la categoría debe tener exactamente "
					+ LONGITUD_CODIGO_CATEGORIA + " caracteres.");
		}
	}

	private void validarNoExistePlatoConMismoNombreYCategoria(final PlatoDominio datos) {
		var filtro = PlatoEntidad.builder()
				.nombre(UtilTexto.aplicarTrim(datos.getNombre()))
				.categoria(co.edu.uco.patiomaruparking.entidad.CategoriaEntidad.builder()
						.codigoCategoria(UtilTexto.aplicarTrim(datos.getCategoria().getCodigoCategoria()))
						.build())
				.build();

		var resultados = daoFactory.obtenerPlatoDAO().consultar(filtro);

		if (UtilObjeto.noEsNulo(resultados) && !resultados.isEmpty()) {
			throw new RuntimeException("Ya existe un plato registrado con el mismo nombre dentro de la misma categoría.");
		}
	}

	private String generarCodigoUnicoPlato() {
		String codigoPlato;
		PlatoEntidad platoExistente;

		do {
			codigoPlato = UtilCodigo.generarCodigo("PLT");

			if (UtilTexto.aplicarTrim(codigoPlato).length() != LONGITUD_CODIGO_PLATO) {
				throw new RuntimeException("El código del plato generado debe tener exactamente "
						+ LONGITUD_CODIGO_PLATO + " caracteres.");
			}

			platoExistente = daoFactory.obtenerPlatoDAO().consultarPorId(codigoPlato);

		} while (UtilObjeto.noEsNulo(platoExistente));

		return codigoPlato;
	}
	
	private void guardar(final PlatoDominio plato) {
		var platoEntidad = PlatoEntidadAssembler.getInstance().ensamblarEntidad(plato);
		daoFactory.obtenerPlatoDAO().registrar(platoEntidad);
	}
}
