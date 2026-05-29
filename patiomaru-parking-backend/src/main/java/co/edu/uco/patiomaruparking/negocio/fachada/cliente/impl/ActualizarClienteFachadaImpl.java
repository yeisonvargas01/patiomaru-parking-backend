package co.edu.uco.patiomaruparking.negocio.fachada.cliente.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.ClienteDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.ClienteDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.cliente.impl.ActualizarClienteCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.cliente.ActualizarClienteFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class ActualizarClienteFachadaImpl implements ActualizarClienteFachada {

	private static final Logger logger = LoggerFactory.getLogger(ActualizarClienteFachadaImpl.class);

	@Override
	public void ejecutar(final ClienteDTO datos) {
		logger.info("Iniciando fachada para actualizar cliente.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			daoFactory.iniciarTransaccion();

			var clienteDominio = ClienteDTOAssembler.getInstance()
					.ensamblarDominio(datos);

			var casoUso = new ActualizarClienteCasoUsoImpl(daoFactory);

			casoUso.ejecutar(clienteDominio);

			daoFactory.confirmarTransaccion();

			logger.info("Fachada para actualizar cliente finalizada satisfactoriamente.");

		} catch (final PatioMaruExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (final Exception excepcion) {
			daoFactory.cancelarTransaccion();

			logger.error("Se presentó un error inesperado actualizando el cliente.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible actualizar el cliente. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en ActualizarClienteFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}
}
