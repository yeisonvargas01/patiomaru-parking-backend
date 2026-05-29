package co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.DetallePedidoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.DetallePedidoDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.impl.ConsultarDetallePedidoPorIdCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.ConsultarDetallePedidoPorIdFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class ConsultarDetallePedidoPorIdFachadaImpl implements ConsultarDetallePedidoPorIdFachada {

	private static final Logger logger = LoggerFactory.getLogger(ConsultarDetallePedidoPorIdFachadaImpl.class);

	@Override
	public DetallePedidoDTO ejecutar(final String codigoDetallePedido) {
		logger.info("Iniciando fachada para consultar detalle de pedido por identificador.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			var casoUso = new ConsultarDetallePedidoPorIdCasoUsoImpl(daoFactory);

			var detallePedidoDominio = casoUso.ejecutar(codigoDetallePedido);

			var detallePedidoDTO = DetallePedidoDTOAssembler.getInstance()
					.ensamblarDTO(detallePedidoDominio);

			logger.info("Fachada para consultar detalle de pedido por identificador finalizada satisfactoriamente.");

			return detallePedidoDTO;

		} catch (final PatioMaruExcepcion excepcion) {
			throw excepcion;

		} catch (final Exception excepcion) {
			logger.error("Se presentó un error inesperado consultando el detalle de pedido por identificador.",
					excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible consultar el detalle del pedido. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en ConsultarDetallePedidoPorIdFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}
}
