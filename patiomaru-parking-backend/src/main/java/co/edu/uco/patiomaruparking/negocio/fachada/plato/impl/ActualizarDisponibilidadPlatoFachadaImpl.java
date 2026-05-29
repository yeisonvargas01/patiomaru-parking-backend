package co.edu.uco.patiomaruparking.negocio.fachada.plato.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.PlatoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.PlatoDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.plato.impl.ActualizarDisponibilidadPlatoCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.plato.ActualizarDisponibilidadPlatoFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class ActualizarDisponibilidadPlatoFachadaImpl implements ActualizarDisponibilidadPlatoFachada {

	private static final Logger logger = LoggerFactory.getLogger(ActualizarDisponibilidadPlatoFachadaImpl.class);

	@Override
	public void ejecutar(final PlatoDTO datos) {
		logger.info("Iniciando fachada para actualizar disponibilidad del plato.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			daoFactory.iniciarTransaccion();

			var platoDominio = PlatoDTOAssembler.getInstance()
					.ensamblarDominio(datos);

			var casoUso = new ActualizarDisponibilidadPlatoCasoUsoImpl(daoFactory);

			casoUso.ejecutar(platoDominio);

			daoFactory.confirmarTransaccion();

			logger.info("Fachada para actualizar disponibilidad del plato finalizada satisfactoriamente.");

		} catch (final PatioMaruExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (final Exception excepcion) {
			daoFactory.cancelarTransaccion();

			logger.error("Se presentó un error inesperado actualizando la disponibilidad del plato.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible actualizar la disponibilidad del plato. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en ActualizarDisponibilidadPlatoFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}
}
