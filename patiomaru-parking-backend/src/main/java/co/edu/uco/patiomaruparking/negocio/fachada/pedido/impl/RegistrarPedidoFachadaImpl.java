package co.edu.uco.patiomaruparking.negocio.fachada.pedido.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.PedidoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.PedidoDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.pedido.impl.RegistrarPedidoCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.pedido.RegistrarPedidoFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class RegistrarPedidoFachadaImpl implements RegistrarPedidoFachada {

	private static final Logger logger = LoggerFactory.getLogger(RegistrarPedidoFachadaImpl.class);

	@Override
	public PedidoDTO ejecutar(final PedidoDTO datos) {
		logger.info("Iniciando fachada para registrar pedido.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			daoFactory.iniciarTransaccion();

			var pedidoDominio = PedidoDTOAssembler.getInstance()
					.ensamblarDominio(datos);

			var casoUso = new RegistrarPedidoCasoUsoImpl(daoFactory);

			var pedidoRegistrado = casoUso.ejecutar(pedidoDominio);

			var pedidoRespuesta = PedidoDTOAssembler.getInstance()
					.ensamblarDTO(pedidoRegistrado);

			daoFactory.confirmarTransaccion();

			logger.info("Fachada para registrar pedido finalizada satisfactoriamente.");

			return pedidoRespuesta;

		} catch (final PatioMaruExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (final Exception excepcion) {
			daoFactory.cancelarTransaccion();

			logger.error("Se presentó un error inesperado registrando el pedido.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible registrar el pedido. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en RegistrarPedidoFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}
}
