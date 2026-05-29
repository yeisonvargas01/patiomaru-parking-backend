package co.edu.uco.patiomaruparking.negocio.fachada.pedido.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.negocio.casouso.pedido.impl.CancelarPedidoCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.pedido.CancelarPedidoFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class CancelarPedidoFachadaImpl implements CancelarPedidoFachada {

	private static final Logger logger = LoggerFactory.getLogger(CancelarPedidoFachadaImpl.class);

	@Override
	public void ejecutar(final String codigoPedido) {
		logger.info("Iniciando fachada para cancelar pedido.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			daoFactory.iniciarTransaccion();

			var casoUso = new CancelarPedidoCasoUsoImpl(daoFactory);

			casoUso.ejecutar(codigoPedido);

			daoFactory.confirmarTransaccion();

			logger.info("Fachada para cancelar pedido finalizada satisfactoriamente.");

		} catch (final PatioMaruExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (final Exception excepcion) {
			daoFactory.cancelarTransaccion();

			logger.error("Se presentó un error inesperado cancelando el pedido.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible cancelar el pedido. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en CancelarPedidoFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}
}
