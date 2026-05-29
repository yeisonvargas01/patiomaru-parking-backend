package co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.DetallePedidoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.DetallePedidoDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.impl.RegistrarDetallePedidoCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.RegistrarDetallePedidoFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class RegistrarDetallePedidoFachadaImpl implements RegistrarDetallePedidoFachada {

	private static final Logger logger = LoggerFactory.getLogger(RegistrarDetallePedidoFachadaImpl.class);

	@Override
	public DetallePedidoDTO ejecutar(final DetallePedidoDTO datos) {
		logger.info("Iniciando fachada para registrar detalle de pedido.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			daoFactory.iniciarTransaccion();

			var detallePedidoDominio = DetallePedidoDTOAssembler.getInstance()
					.ensamblarDominio(datos);

			var casoUso = new RegistrarDetallePedidoCasoUsoImpl(daoFactory);

			var detallePedidoRegistrado = casoUso.ejecutar(detallePedidoDominio);

			var detallePedidoRespuesta = DetallePedidoDTOAssembler.getInstance()
					.ensamblarDTO(detallePedidoRegistrado);

			daoFactory.confirmarTransaccion();

			logger.info("Fachada para registrar detalle de pedido finalizada satisfactoriamente.");

			return detallePedidoRespuesta;

		} catch (final PatioMaruExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (final Exception excepcion) {
			daoFactory.cancelarTransaccion();

			logger.error("Se presentó un error inesperado registrando el detalle de pedido.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible registrar el detalle del pedido. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en RegistrarDetallePedidoFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}
}
