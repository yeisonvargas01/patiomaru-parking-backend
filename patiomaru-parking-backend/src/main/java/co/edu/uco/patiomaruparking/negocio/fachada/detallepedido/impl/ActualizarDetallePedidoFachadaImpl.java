package co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.DetallePedidoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.DetallePedidoDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.impl.ActualizarDetallePedidoCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.ActualizarDetallePedidoFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class ActualizarDetallePedidoFachadaImpl implements ActualizarDetallePedidoFachada {

	private static final Logger logger = LoggerFactory.getLogger(ActualizarDetallePedidoFachadaImpl.class);

	@Override
	public void ejecutar(final DetallePedidoDTO datos) {
		logger.info("Iniciando fachada para actualizar detalle de pedido.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			daoFactory.iniciarTransaccion();

			var detallePedidoDominio = DetallePedidoDTOAssembler.getInstance()
					.ensamblarDominio(datos);

			var casoUso = new ActualizarDetallePedidoCasoUsoImpl(daoFactory);

			casoUso.ejecutar(detallePedidoDominio);

			daoFactory.confirmarTransaccion();

			logger.info("Fachada para actualizar detalle de pedido finalizada satisfactoriamente.");

		} catch (final PatioMaruExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (final Exception excepcion) {
			daoFactory.cancelarTransaccion();

			logger.error("Se presentó un error inesperado actualizando el detalle de pedido.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible actualizar el detalle del pedido. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en ActualizarDetallePedidoFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}
}
