package co.edu.uco.patiomaruparking.negocio.fachada.plato.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.PlatoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.PlatoDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.plato.impl.RegistrarPlatoCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.plato.RegistrarPlatoFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class RegistrarPlatoFachadaImpl implements RegistrarPlatoFachada {

	private static final Logger logger = LoggerFactory.getLogger(RegistrarPlatoFachadaImpl.class);

	@Override
	public void ejecutar(final PlatoDTO datos) {
		logger.info("Iniciando fachada para registrar plato.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			daoFactory.iniciarTransaccion();

			var platoDominio = PlatoDTOAssembler.getInstance()
					.ensamblarDominio(datos);

			var casoUso = new RegistrarPlatoCasoUsoImpl(daoFactory);

			casoUso.ejecutar(platoDominio);

			daoFactory.confirmarTransaccion();

			logger.info("Fachada para registrar plato finalizada satisfactoriamente.");

		} catch (final PatioMaruExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (final Exception excepcion) {
			daoFactory.cancelarTransaccion();

			logger.error("Se presentó un error inesperado registrando el plato.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible registrar el plato. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en RegistrarPlatoFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}
}
