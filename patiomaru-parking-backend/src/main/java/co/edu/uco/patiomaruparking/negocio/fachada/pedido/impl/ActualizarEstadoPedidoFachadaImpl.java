package co.edu.uco.patiomaruparking.negocio.fachada.pedido.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.PedidoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.PedidoDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.pedido.impl.ActualizarEstadoPedidoCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.pedido.ActualizarEstadoPedidoFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class ActualizarEstadoPedidoFachadaImpl implements ActualizarEstadoPedidoFachada {

	private static final Logger logger = LoggerFactory.getLogger(ActualizarEstadoPedidoFachadaImpl.class);

	@Override
	public void ejecutar(final PedidoDTO datos) {
		logger.info("Iniciando fachada para actualizar estado del pedido.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			daoFactory.iniciarTransaccion();

			var pedidoDominio = PedidoDTOAssembler.getInstance()
					.ensamblarDominio(datos);

			var casoUso = new ActualizarEstadoPedidoCasoUsoImpl(daoFactory);

			casoUso.ejecutar(pedidoDominio);

			daoFactory.confirmarTransaccion();

			logger.info("Fachada para actualizar estado del pedido finalizada satisfactoriamente.");

		} catch (final PatioMaruExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (final Exception excepcion) {
			daoFactory.cancelarTransaccion();

			logger.error("Se presentó un error inesperado actualizando el estado del pedido.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible actualizar el estado del pedido. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en ActualizarEstadoPedidoFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}
}
