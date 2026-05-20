package co.edu.uco.patiomaruparking.negocio.casouso.plato.impl;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PlatoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.plato.ConsultarPlatoPorIdCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.PlatoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public final class ConsultarPlatoPorIdCasoUsoImpl implements ConsultarPlatoPorIdCasoUso {

	private static final int LONGITUD_CODIGO_PLATO = 6;

	private final DAOFactory daoFactory;

	public ConsultarPlatoPorIdCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public PlatoDominio ejecutar(final String codigoPlato) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango.
		validarCodigoPlato(codigoPlato);

		var codigoPlatoNormalizado = UtilTexto.aplicarTrim(codigoPlato);

		// 2. Debe existir un plato registrado con el código indicado.
		var platoEntidad = daoFactory.obtenerPlatoDAO().consultarPorId(codigoPlatoNormalizado);

		if (UtilObjeto.esNulo(platoEntidad)) {
			throw new RuntimeException("No existe un plato registrado con el código indicado.");
		}

		// 3. Ensamblar el plato de Entidad a Dominio.
		return PlatoEntidadAssembler.getInstance().ensamblarDominio(platoEntidad);
	}

	private void validarCodigoPlato(final String codigoPlato) {
		if (!UtilTexto.tieneTexto(codigoPlato)) {
			throw new RuntimeException("El código del plato es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(codigoPlato).length() != LONGITUD_CODIGO_PLATO) {
			throw new RuntimeException("El código del plato debe tener exactamente "
					+ LONGITUD_CODIGO_PLATO + " caracteres.");
		}
	}
}