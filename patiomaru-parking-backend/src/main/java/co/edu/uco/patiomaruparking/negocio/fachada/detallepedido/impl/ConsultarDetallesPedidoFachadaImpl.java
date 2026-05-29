package co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.DetallePedidoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.DetallePedidoDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.impl.ConsultarDetallesPedidoCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio;
import co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.ConsultarDetallesPedidoFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class ConsultarDetallesPedidoFachadaImpl implements ConsultarDetallesPedidoFachada {

	private static final Logger logger = LoggerFactory.getLogger(ConsultarDetallesPedidoFachadaImpl.class);

	@Override
	public List<DetallePedidoDTO> ejecutar(final DetallePedidoDTO filtro) {
		logger.info("Iniciando fachada para consultar detalles de pedido.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			var filtroDominio = DetallePedidoDTOAssembler.getInstance()
					.ensamblarDominio(filtro);

			var casoUso = new ConsultarDetallesPedidoCasoUsoImpl(daoFactory);

			var detallesDominio = UtilObjeto.obtenerValorDefecto(
					casoUso.ejecutar(filtroDominio),
					List.<DetallePedidoDominio>of());

			var detallesDTO = ensamblarRespuesta(detallesDominio);

			logger.info("Fachada para consultar detalles de pedido finalizada satisfactoriamente.");

			return detallesDTO;

		} catch (final PatioMaruExcepcion excepcion) {
			throw excepcion;

		} catch (final Exception excepcion) {
			logger.error("Se presentó un error inesperado consultando los detalles de pedido.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible consultar los detalles del pedido. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en ConsultarDetallesPedidoFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}

	private List<DetallePedidoDTO> ensamblarRespuesta(final List<DetallePedidoDominio> detallesDominio) {
		var detallesDTO = new ArrayList<DetallePedidoDTO>();

		for (DetallePedidoDominio detalleDominio : detallesDominio) {
			var detalleDTO = DetallePedidoDTOAssembler.getInstance()
					.ensamblarDTO(detalleDominio);

			detallesDTO.add(detalleDTO);
		}

		return detallesDTO;
	}
}
