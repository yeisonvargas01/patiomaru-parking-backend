package co.edu.uco.patiomaruparking.negocio.fachada.cliente.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.ClienteDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.ClienteDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.cliente.impl.ConsultarClientePorIdCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.cliente.ConsultarClientePorIdFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class ConsultarClientePorIdFachadaImpl implements ConsultarClientePorIdFachada {

	private static final Logger logger = LoggerFactory.getLogger(ConsultarClientePorIdFachadaImpl.class);

	@Override
	public ClienteDTO ejecutar(final String codigoCliente) {
		logger.info("Iniciando fachada para consultar cliente por identificador.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			var casoUso = new ConsultarClientePorIdCasoUsoImpl(daoFactory);

			var clienteDominio = casoUso.ejecutar(codigoCliente);

			var clienteDTO = ClienteDTOAssembler.getInstance()
					.ensamblarDTO(clienteDominio);

			logger.info("Fachada para consultar cliente por identificador finalizada satisfactoriamente.");

			return clienteDTO;

		} catch (final PatioMaruExcepcion excepcion) {
			throw excepcion;

		} catch (final Exception excepcion) {
			logger.error("Se presentó un error inesperado consultando el cliente por identificador.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible consultar el cliente. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en ConsultarClientePorIdFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}
}