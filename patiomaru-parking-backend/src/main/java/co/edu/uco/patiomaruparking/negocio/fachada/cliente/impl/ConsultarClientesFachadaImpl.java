package co.edu.uco.patiomaruparking.negocio.fachada.cliente.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.ClienteDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.ClienteDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.cliente.impl.ConsultarClientesCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.dominio.ClienteDominio;
import co.edu.uco.patiomaruparking.negocio.fachada.cliente.ConsultarClientesFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class ConsultarClientesFachadaImpl implements ConsultarClientesFachada {

	private static final Logger logger = LoggerFactory.getLogger(ConsultarClientesFachadaImpl.class);

	@Override
	public List<ClienteDTO> ejecutar(final ClienteDTO filtro) {
		logger.info("Iniciando fachada para consultar clientes.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			var filtroDominio = ClienteDTOAssembler.getInstance()
					.ensamblarDominio(filtro);

			var casoUso = new ConsultarClientesCasoUsoImpl(daoFactory);

			var clientesDominio = UtilObjeto.obtenerValorDefecto(
					casoUso.ejecutar(filtroDominio),
					List.<ClienteDominio>of());

			var clientesDTO = ensamblarRespuesta(clientesDominio);

			logger.info("Fachada para consultar clientes finalizada satisfactoriamente.");

			return clientesDTO;

		} catch (final PatioMaruExcepcion excepcion) {
			throw excepcion;

		} catch (final Exception excepcion) {
			logger.error("Se presentó un error inesperado consultando los clientes.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible consultar los clientes. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en ConsultarClientesFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}

	private List<ClienteDTO> ensamblarRespuesta(final List<ClienteDominio> clientesDominio) {
		var clientesDTO = new ArrayList<ClienteDTO>();

		for (ClienteDominio clienteDominio : clientesDominio) {
			var clienteDTO = ClienteDTOAssembler.getInstance()
					.ensamblarDTO(clienteDominio);

			clientesDTO.add(clienteDTO);
		}

		return clientesDTO;
	}
}
