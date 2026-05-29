package co.edu.uco.patiomaruparking.negocio.casouso.pedido.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.DetallePedidoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.DetallePedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.pedido.ConsultarPedidoPorIdCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio;
import co.edu.uco.patiomaruparking.negocio.dominio.PedidoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;

public final class ConsultarPedidoPorIdCasoUsoImpl implements ConsultarPedidoPorIdCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(ConsultarPedidoPorIdCasoUsoImpl.class);

	private static final String PREFIJO_PEDIDO = "PEDD";
	private static final int LONGITUD_CODIGO_PEDIDO = 7;
	private static final int POSICION_INICIO_DIGITOS = 4;

	private final DAOFactory daoFactory;

	public ConsultarPedidoPorIdCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = UtilObjeto.obtenerValorDefecto(
				daoFactory,
				DAOFactory.getFactory());
	}

	@Override
	public PedidoDominio ejecutar(final String codigoPedido) {
		logger.info("Iniciando la consulta de un pedido por identificador.");

		var codigoPedidoNormalizado = validarYNormalizarCodigoPedido(codigoPedido);

		var pedidoEntidad = daoFactory.obtenerPedidoDAO()
				.consultarPorId(codigoPedidoNormalizado);

		if (UtilObjeto.esNulo(pedidoEntidad)
				|| UtilTexto.esVacio(pedidoEntidad.getCodigoPedido())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe un pedido registrado con el código indicado.");
		}

		var detalles = consultarDetallesDelPedido(codigoPedidoNormalizado);

		var pedido = PedidoEntidadAssembler.getInstance()
				.ensamblarDominio(pedidoEntidad);

		var pedidoConsultado = PedidoDominio.builder()
				.codigoPedido(pedido.getCodigoPedido())
				.fechaRegistro(pedido.getFechaRegistro())
				.horaRegistro(pedido.getHoraRegistro())
				.tipoAtencion(pedido.getTipoAtencion())
				.estado(pedido.getEstado())
				.totalPedido(pedido.getTotalPedido())
				.mesa(pedido.getMesa())
				.cliente(pedido.getCliente())
				.empleado(pedido.getEmpleado())
				.detalles(detalles)
				.build();

		logger.info("Pedido consultado satisfactoriamente.");

		return pedidoConsultado;
	}

	private String validarYNormalizarCodigoPedido(final String codigoPedido) {
		if (!UtilTexto.tieneTexto(codigoPedido)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido es obligatorio.");
		}

		var codigoPedidoNormalizado = UtilTexto.aplicarTrim(codigoPedido);

		if (codigoPedidoNormalizado.length() != LONGITUD_CODIGO_PEDIDO) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido debe tener exactamente "
							+ LONGITUD_CODIGO_PEDIDO + " caracteres.");
		}

		if (!codigoPedidoNormalizado.startsWith(PREFIJO_PEDIDO)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido debe iniciar con " + PREFIJO_PEDIDO + ".");
		}

		if (!contieneSoloDigitos(codigoPedidoNormalizado.substring(POSICION_INICIO_DIGITOS))) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido debe tener el formato PEDD seguido de tres dígitos numéricos.");
		}

		return codigoPedidoNormalizado;
	}

	private boolean contieneSoloDigitos(final String valor) {
		var valorSeguro = UtilTexto.aplicarTrim(valor);

		for (int indice = 0; indice < valorSeguro.length(); indice++) {
			if (!Character.isDigit(valorSeguro.charAt(indice))) {
				return false;
			}
		}

		return true;
	}

	private List<DetallePedidoDominio> consultarDetallesDelPedido(final String codigoPedido) {
		var filtro = DetallePedidoEntidad.builder()
				.codigoPedido(UtilTexto.aplicarTrim(codigoPedido))
				.build();

		var detallesEntidad = UtilObjeto.obtenerValorDefecto(
				daoFactory.obtenerDetallePedidoDAO().consultar(filtro),
				List.<DetallePedidoEntidad>of());

		var detalles = new ArrayList<DetallePedidoDominio>();

		for (DetallePedidoEntidad detalleEntidad : detallesEntidad) {
			var detalle = DetallePedidoEntidadAssembler.getInstance()
					.ensamblarDominio(detalleEntidad);

			detalles.add(detalle);
		}

		return detalles;
	}
}