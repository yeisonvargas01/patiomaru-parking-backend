package co.edu.uco.patiomaruparking.negocio.fachada.cliente.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.ClienteDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.ClienteDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.cliente.impl.RegistrarClienteCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.cliente.RegistrarClienteFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class RegistrarClienteFachadaImpl implements RegistrarClienteFachada {

	private static final Logger logger = LoggerFactory.getLogger(RegistrarClienteFachadaImpl.class);

	@Override
	public void ejecutar(final ClienteDTO datos) {
		logger.info("Iniciando fachada para registrar cliente.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			daoFactory.iniciarTransaccion();

			var clienteDominio = ClienteDTOAssembler.getInstance()
					.ensamblarDominio(datos);

			var casoUso = new RegistrarClienteCasoUsoImpl(daoFactory);

			casoUso.ejecutar(clienteDominio);

			daoFactory.confirmarTransaccion();

			logger.info("Fachada para registrar cliente finalizada satisfactoriamente.");

		} catch (final PatioMaruExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (final Exception excepcion) {
			daoFactory.cancelarTransaccion();

			logger.error("Se presentó un error inesperado registrando el cliente.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible registrar el cliente. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en RegistrarClienteFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}
}
