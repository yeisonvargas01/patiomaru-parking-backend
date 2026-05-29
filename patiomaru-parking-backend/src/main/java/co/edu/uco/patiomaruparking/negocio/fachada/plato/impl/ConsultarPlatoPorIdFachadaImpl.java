package co.edu.uco.patiomaruparking.negocio.fachada.plato.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.PlatoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.PlatoDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.plato.impl.ConsultarPlatoPorIdCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.plato.ConsultarPlatoPorIdFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class ConsultarPlatoPorIdFachadaImpl implements ConsultarPlatoPorIdFachada {

	private static final Logger logger = LoggerFactory.getLogger(ConsultarPlatoPorIdFachadaImpl.class);

	@Override
	public PlatoDTO ejecutar(final String codigoPlato) {
		logger.info("Iniciando fachada para consultar plato por identificador.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			var casoUso = new ConsultarPlatoPorIdCasoUsoImpl(daoFactory);

			var platoDominio = casoUso.ejecutar(codigoPlato);

			var platoDTO = PlatoDTOAssembler.getInstance()
					.ensamblarDTO(platoDominio);

			logger.info("Fachada para consultar plato por identificador finalizada satisfactoriamente.");

			return platoDTO;

		} catch (final PatioMaruExcepcion excepcion) {
			throw excepcion;

		} catch (final Exception excepcion) {
			logger.error("Se presentó un error inesperado consultando el plato por identificador.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible consultar el plato. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en ConsultarPlatoPorIdFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}
}
