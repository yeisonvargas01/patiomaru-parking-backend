package co.edu.uco.patiomaruparking.negocio.fachada.pedido.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.PedidoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.PedidoDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.pedido.impl.ConsultarPedidoPorIdCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.pedido.ConsultarPedidoPorIdFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class ConsultarPedidoPorIdFachadaImpl implements ConsultarPedidoPorIdFachada {

	private static final Logger logger = LoggerFactory.getLogger(ConsultarPedidoPorIdFachadaImpl.class);

	@Override
	public PedidoDTO ejecutar(final String codigoPedido) {
		logger.info("Iniciando fachada para consultar pedido por identificador.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			var casoUso = new ConsultarPedidoPorIdCasoUsoImpl(daoFactory);

			var pedidoDominio = casoUso.ejecutar(codigoPedido);

			var pedidoDTO = PedidoDTOAssembler.getInstance()
					.ensamblarDTO(pedidoDominio);

			logger.info("Fachada para consultar pedido por identificador finalizada satisfactoriamente.");

			return pedidoDTO;

		} catch (final PatioMaruExcepcion excepcion) {
			throw excepcion;

		} catch (final Exception excepcion) {
			logger.error("Se presentó un error inesperado consultando el pedido por identificador.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible consultar el pedido. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en ConsultarPedidoPorIdFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}
}
