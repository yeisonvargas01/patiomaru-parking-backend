package co.edu.uco.patiomaruparking.negocio.fachada.plato.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.PlatoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.PlatoDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.plato.impl.ConsultarPlatosCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.dominio.PlatoDominio;
import co.edu.uco.patiomaruparking.negocio.fachada.plato.ConsultarPlatosFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class ConsultarPlatosFachadaImpl implements ConsultarPlatosFachada {

	private static final Logger logger = LoggerFactory.getLogger(ConsultarPlatosFachadaImpl.class);

	@Override
	public List<PlatoDTO> ejecutar(final PlatoDTO filtro) {
		logger.info("Iniciando fachada para consultar platos.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			var filtroDominio = PlatoDTOAssembler.getInstance()
					.ensamblarDominio(filtro);

			var casoUso = new ConsultarPlatosCasoUsoImpl(daoFactory);

			var platosDominio = UtilObjeto.obtenerValorDefecto(
					casoUso.ejecutar(filtroDominio),
					List.<PlatoDominio>of());

			var platosDTO = ensamblarRespuesta(platosDominio);

			logger.info("Fachada para consultar platos finalizada satisfactoriamente.");

			return platosDTO;

		} catch (final PatioMaruExcepcion excepcion) {
			throw excepcion;

		} catch (final Exception excepcion) {
			logger.error("Se presentó un error inesperado consultando los platos.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible consultar los platos. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en ConsultarPlatosFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}

	private List<PlatoDTO> ensamblarRespuesta(final List<PlatoDominio> platosDominio) {
		var platosDTO = new ArrayList<PlatoDTO>();

		for (PlatoDominio platoDominio : platosDominio) {
			var platoDTO = PlatoDTOAssembler.getInstance()
					.ensamblarDTO(platoDominio);

			platosDTO.add(platoDTO);
		}

		return platosDTO;
	}
}
