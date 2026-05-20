package co.edu.uco.patiomaruparking.negocio.casouso.plato.impl;

import java.util.List;
import java.util.stream.Collectors;

import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PlatoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.plato.ConsultarPlatosCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.PlatoDominio;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public final class ConsultarPlatosCasoUsoImpl implements ConsultarPlatosCasoUso {

	private static final int LONGITUD_CODIGO_PLATO = 6;
	private static final int LONGITUD_MAXIMA_NOMBRE = 80;
	private static final int LONGITUD_CODIGO_CATEGORIA = 6;

	private final DAOFactory daoFactory;

	public ConsultarPlatosCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public List<PlatoDominio> ejecutar(final PlatoDominio filtro) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango cuando se envían filtros.
		validarFiltro(filtro);

		// 2. Consultar información de los platos según los filtros enviados.
		var filtroSeguro = UtilObjeto.esNulo(filtro)
				? PlatoDominio.builder().build()
				: filtro;

		var filtroEntidad = PlatoEntidadAssembler.getInstance().ensamblarEntidad(filtroSeguro);

		var platosEntidad = daoFactory.obtenerPlatoDAO().consultar(filtroEntidad);

		if (UtilObjeto.esNulo(platosEntidad) || platosEntidad.isEmpty()) {
			return List.of();
		}

		// 3. Ensamblar los resultados de Entidad a Dominio.
		return platosEntidad.stream()
				.map(PlatoEntidadAssembler.getInstance()::ensamblarDominio)
				.collect(Collectors.toList());
	}

	private void validarFiltro(final PlatoDominio filtro) {
		if (UtilObjeto.esNulo(filtro)) {
			return;
		}

		if (UtilTexto.tieneTexto(filtro.getCodigoPlato())
				&& UtilTexto.aplicarTrim(filtro.getCodigoPlato()).length() != LONGITUD_CODIGO_PLATO) {
			throw new RuntimeException("El código del plato debe tener exactamente "
					+ LONGITUD_CODIGO_PLATO + " caracteres.");
		}

		if (UtilTexto.tieneTexto(filtro.getNombre())
				&& UtilTexto.aplicarTrim(filtro.getNombre()).length() > LONGITUD_MAXIMA_NOMBRE) {
			throw new RuntimeException("El nombre del plato no puede superar "
					+ LONGITUD_MAXIMA_NOMBRE + " caracteres.");
		}

		if (UtilObjeto.noEsNulo(filtro.getPrecioVenta())
				&& filtro.getPrecioVenta().signum() <= 0) {
			throw new RuntimeException("El precio de venta del plato debe ser mayor que cero.");
		}

		if (UtilObjeto.noEsNulo(filtro.getCategoria())
				&& UtilTexto.tieneTexto(filtro.getCategoria().getCodigoCategoria())
				&& UtilTexto.aplicarTrim(filtro.getCategoria().getCodigoCategoria()).length() != LONGITUD_CODIGO_CATEGORIA) {
			throw new RuntimeException("El código de la categoría debe tener exactamente "
					+ LONGITUD_CODIGO_CATEGORIA + " caracteres.");
		}
	}
}