package co.edu.uco.patiomaruparking.negocio.fachada.pedido.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.PedidoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.PedidoDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.pedido.impl.ConsultarPedidosCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.dominio.PedidoDominio;
import co.edu.uco.patiomaruparking.negocio.fachada.pedido.ConsultarPedidosFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class ConsultarPedidosFachadaImpl implements ConsultarPedidosFachada {

	private static final Logger logger = LoggerFactory.getLogger(ConsultarPedidosFachadaImpl.class);

	@Override
	public List<PedidoDTO> ejecutar(final PedidoDTO filtro) {
		logger.info("Iniciando fachada para consultar pedidos.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			var filtroDominio = PedidoDTOAssembler.getInstance()
					.ensamblarDominio(filtro);

			var casoUso = new ConsultarPedidosCasoUsoImpl(daoFactory);

			var pedidosDominio = UtilObjeto.obtenerValorDefecto(
					casoUso.ejecutar(filtroDominio),
					List.<PedidoDominio>of());

			var pedidosDTO = ensamblarRespuesta(pedidosDominio);

			logger.info("Fachada para consultar pedidos finalizada satisfactoriamente.");

			return pedidosDTO;

		} catch (final PatioMaruExcepcion excepcion) {
			throw excepcion;

		} catch (final Exception excepcion) {
			logger.error("Se presentó un error inesperado consultando los pedidos.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible consultar los pedidos. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en ConsultarPedidosFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}

	private List<PedidoDTO> ensamblarRespuesta(final List<PedidoDominio> pedidosDominio) {
		var pedidosDTO = new ArrayList<PedidoDTO>();

		for (PedidoDominio pedidoDominio : pedidosDominio) {
			var pedidoDTO = PedidoDTOAssembler.getInstance()
					.ensamblarDTO(pedidoDominio);

			pedidosDTO.add(pedidoDTO);
		}

		return pedidosDTO;
	}
}
