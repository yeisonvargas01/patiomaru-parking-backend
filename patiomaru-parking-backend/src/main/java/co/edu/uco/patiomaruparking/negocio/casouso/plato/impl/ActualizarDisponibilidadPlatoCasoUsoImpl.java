package co.edu.uco.patiomaruparking.negocio.casouso.plato.impl;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PlatoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.plato.ActualizarDisponibilidadPlatoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.PlatoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public final class ActualizarDisponibilidadPlatoCasoUsoImpl implements ActualizarDisponibilidadPlatoCasoUso {

	private static final int LONGITUD_CODIGO_PLATO = 6;

	private final DAOFactory daoFactory;

	public ActualizarDisponibilidadPlatoCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final PlatoDominio datos) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango.
		validarDatosConsistentes(datos);

		var codigoPlato = UtilTexto.aplicarTrim(datos.getCodigoPlato());
		var nuevaDisponibilidad = datos.getEstado();

		// 2. Debe existir el plato al que se le desea actualizar la disponibilidad.
		var platoEntidad = daoFactory.obtenerPlatoDAO().consultarPorId(codigoPlato);

		if (UtilObjeto.esNulo(platoEntidad)) {
			throw new RuntimeException("No existe un plato registrado con el código indicado.");
		}

		var platoActual = PlatoEntidadAssembler.getInstance().ensamblarDominio(platoEntidad);

		// 3. La nueva disponibilidad no debe ser igual a la disponibilidad actual.
		if (platoActual.getEstado().equals(nuevaDisponibilidad)) {
			throw new RuntimeException("El plato ya se encuentra con la disponibilidad indicada.");
		}

		// 4. Actualizar disponibilidad del plato.
		daoFactory.obtenerPlatoDAO().actualizarDisponibilidad(codigoPlato, nuevaDisponibilidad);
	}

	private void validarDatosConsistentes(final PlatoDominio datos) {
		if (UtilObjeto.esNulo(datos)) {
			throw new RuntimeException("Los datos para actualizar la disponibilidad del plato son obligatorios.");
		}

		if (!UtilTexto.tieneTexto(datos.getCodigoPlato())) {
			throw new RuntimeException("El código del plato es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(datos.getCodigoPlato()).length() != LONGITUD_CODIGO_PLATO) {
			throw new RuntimeException("El código del plato debe tener exactamente "
					+ LONGITUD_CODIGO_PLATO + " caracteres.");
		}

		if (UtilObjeto.esNulo(datos.getEstado())) {
			throw new RuntimeException("La disponibilidad del plato es obligatoria.");
		}
	}
}
