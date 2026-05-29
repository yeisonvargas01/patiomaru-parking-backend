package co.edu.uco.patiomaruparking.negocio.fachada.plato.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.negocio.casouso.plato.impl.EliminarPlatoCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.plato.EliminarPlatoFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class EliminarPlatoFachadaImpl implements EliminarPlatoFachada {

	private static final Logger logger = LoggerFactory.getLogger(EliminarPlatoFachadaImpl.class);

	@Override
	public void ejecutar(final String codigoPlato) {
		logger.info("Iniciando fachada para eliminar plato.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			daoFactory.iniciarTransaccion();

			var casoUso = new EliminarPlatoCasoUsoImpl(daoFactory);

			casoUso.ejecutar(codigoPlato);

			daoFactory.confirmarTransaccion();

			logger.info("Fachada para eliminar plato finalizada satisfactoriamente.");

		} catch (final PatioMaruExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (final Exception excepcion) {
			daoFactory.cancelarTransaccion();

			logger.error("Se presentó un error inesperado eliminando el plato.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible eliminar el plato. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en EliminarPlatoFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}
}
