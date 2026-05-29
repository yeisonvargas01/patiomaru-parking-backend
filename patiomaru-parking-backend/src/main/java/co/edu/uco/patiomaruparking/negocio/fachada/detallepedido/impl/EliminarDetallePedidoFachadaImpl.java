package co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.impl.EliminarDetallePedidoCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.EliminarDetallePedidoFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class EliminarDetallePedidoFachadaImpl implements EliminarDetallePedidoFachada {

	private static final Logger logger = LoggerFactory.getLogger(EliminarDetallePedidoFachadaImpl.class);

	@Override
	public void ejecutar(final String codigoDetallePedido) {
		logger.info("Iniciando fachada para eliminar detalle de pedido.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			daoFactory.iniciarTransaccion();

			var casoUso = new EliminarDetallePedidoCasoUsoImpl(daoFactory);

			casoUso.ejecutar(codigoDetallePedido);

			daoFactory.confirmarTransaccion();

			logger.info("Fachada para eliminar detalle de pedido finalizada satisfactoriamente.");

		} catch (final PatioMaruExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (final Exception excepcion) {
			daoFactory.cancelarTransaccion();

			logger.error("Se presentó un error inesperado eliminando el detalle de pedido.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible eliminar el detalle del pedido. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en EliminarDetallePedidoFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}
}