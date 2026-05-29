package co.edu.uco.patiomaruparking.negocio.fachada.cliente.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.ClienteDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.ClienteDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.cliente.impl.ActualizarEstadoClienteCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.cliente.ActualizarEstadoClienteFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class ActualizarEstadoClienteFachadaImpl implements ActualizarEstadoClienteFachada {

	private static final Logger logger = LoggerFactory.getLogger(ActualizarEstadoClienteFachadaImpl.class);

	@Override
	public void ejecutar(final ClienteDTO datos) {
		logger.info("Iniciando fachada para actualizar estado del cliente.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			daoFactory.iniciarTransaccion();

			var clienteDominio = ClienteDTOAssembler.getInstance()
					.ensamblarDominio(datos);

			var casoUso = new ActualizarEstadoClienteCasoUsoImpl(daoFactory);

			casoUso.ejecutar(clienteDominio);

			daoFactory.confirmarTransaccion();

			logger.info("Fachada para actualizar estado del cliente finalizada satisfactoriamente.");

		} catch (final PatioMaruExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (final Exception excepcion) {
			daoFactory.cancelarTransaccion();

			logger.error("Se presentó un error inesperado actualizando el estado del cliente.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible actualizar el estado del cliente. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en ActualizarEstadoClienteFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}
}
