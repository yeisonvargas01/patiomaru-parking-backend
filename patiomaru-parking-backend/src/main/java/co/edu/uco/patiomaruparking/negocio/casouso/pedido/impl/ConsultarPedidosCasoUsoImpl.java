package co.edu.uco.patiomaruparking.negocio.casouso.pedido.impl;

import java.util.List;
import java.util.stream.Collectors;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.DetallePedidoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.DetallePedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.pedido.ConsultarPedidosCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio;
import co.edu.uco.patiomaruparking.negocio.dominio.PedidoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public final class ConsultarPedidosCasoUsoImpl implements ConsultarPedidosCasoUso {

	private static final int LONGITUD_CODIGO_PEDIDO = 7;

	private final DAOFactory daoFactory;

	public ConsultarPedidosCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public List<PedidoDominio> ejecutar(final PedidoDominio filtro) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango cuando se envían filtros.
		validarFiltro(filtro);

		// 2. Consultar información de los pedidos según los filtros enviados.
		var filtroSeguro = UtilObjeto.esNulo(filtro)
				? PedidoDominio.builder().build()
				: filtro;

		var filtroEntidad = PedidoEntidadAssembler.getInstance().ensamblarEntidad(filtroSeguro);

		var pedidosEntidad = daoFactory.obtenerPedidoDAO().consultar(filtroEntidad);

		if (UtilObjeto.esNulo(pedidosEntidad) || pedidosEntidad.isEmpty()) {
			return List.of();
		}

		// 3. Ensamblar cada pedido con sus detalles asociados.
		return pedidosEntidad.stream()
				.map(PedidoEntidadAssembler.getInstance()::ensamblarDominio)
				.map(this::agregarDetallesAlPedido)
				.collect(Collectors.toList());
	}

	private void validarFiltro(final PedidoDominio filtro) {
		if (UtilObjeto.esNulo(filtro)) {
			return;
		}

		if (UtilTexto.tieneTexto(filtro.getCodigoPedido())
				&& UtilTexto.aplicarTrim(filtro.getCodigoPedido()).length() != LONGITUD_CODIGO_PEDIDO) {
			throw new RuntimeException("El código del pedido debe tener exactamente "
					+ LONGITUD_CODIGO_PEDIDO + " caracteres.");
		}

		if (UtilTexto.tieneTexto(filtro.getTipoAtencion())) {
			validarLongitud(filtro.getTipoAtencion(), 4, 11, "El tipo de atención");
		}

		if (UtilTexto.tieneTexto(filtro.getEstado())) {
			validarLongitud(filtro.getEstado(), 6, 14, "El estado del pedido");
		}
	}

	private void validarLongitud(final String valor, final int longitudMinima, final int longitudMaxima,
			final String nombreCampo) {

		var valorSeguro = UtilTexto.aplicarTrim(valor);

		if (valorSeguro.length() < longitudMinima || valorSeguro.length() > longitudMaxima) {
			throw new RuntimeException(nombreCampo + " debe tener entre " + longitudMinima + " y "
					+ longitudMaxima + " caracteres.");
		}
	}

	private PedidoDominio agregarDetallesAlPedido(final PedidoDominio pedido) {
		if (UtilObjeto.esNulo(pedido) || !UtilTexto.tieneTexto(pedido.getCodigoPedido())) {
			return pedido;
		}

		var detalles = consultarDetallesDelPedido(pedido.getCodigoPedido());

		return PedidoDominio.builder()
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
	}

	private List<DetallePedidoDominio> consultarDetallesDelPedido(final String codigoPedido) {
		var filtroDetalle = DetallePedidoEntidad.builder()
				.codigoPedido(UtilTexto.aplicarTrim(codigoPedido))
				.build();

		var detallesEntidad = daoFactory.obtenerDetallePedidoDAO().consultar(filtroDetalle);

		if (UtilObjeto.esNulo(detallesEntidad) || detallesEntidad.isEmpty()) {
			return List.of();
		}

		return detallesEntidad.stream()
				.map(DetallePedidoEntidadAssembler.getInstance()::ensamblarDominio)
				.collect(Collectors.toList());
	}
}