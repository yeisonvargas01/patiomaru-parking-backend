package co.edu.uco.patiomaruparking.negocio.casouso.pedido.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.DetallePedidoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.DetallePedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.pedido.ConsultarPedidoPorIdCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio;
import co.edu.uco.patiomaruparking.negocio.dominio.PedidoDominio;

public final class ConsultarPedidoPorIdCasoUsoImpl implements ConsultarPedidoPorIdCasoUso {

	private static final int LONGITUD_CODIGO_PEDIDO = 7;

	private final DAOFactory daoFactory;

	public ConsultarPedidoPorIdCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public PedidoDominio ejecutar(final String codigoPedido) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango.
		validarCodigoPedido(codigoPedido);

		// 2. Debe existir un pedido registrado con el identificador indicado.
		var pedidoEntidad = daoFactory.obtenerPedidoDAO().consultarPorId(codigoPedido.trim());

		if (Objects.isNull(pedidoEntidad)) {
			throw new RuntimeException("No existe un pedido registrado con el código indicado.");
		}

		// 3. Consultar los detalles asociados al pedido.
		var detalles = consultarDetallesDelPedido(codigoPedido.trim());

		var pedido = PedidoEntidadAssembler.getInstance().ensamblarDominio(pedidoEntidad);

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

	private void validarCodigoPedido(final String codigoPedido) {
		if (!tieneTexto(codigoPedido)) {
			throw new RuntimeException("El código del pedido es obligatorio.");
		}

		if (codigoPedido.trim().length() != LONGITUD_CODIGO_PEDIDO) {
			throw new RuntimeException("El código del pedido debe tener exactamente "
					+ LONGITUD_CODIGO_PEDIDO + " caracteres.");
		}
	}

	private List<DetallePedidoDominio> consultarDetallesDelPedido(final String codigoPedido) {
		var filtro = DetallePedidoEntidad.builder()
				.codigoPedido(codigoPedido)
				.build();

		var detallesEntidad = daoFactory.obtenerDetallePedidoDAO().consultar(filtro);

		if (Objects.isNull(detallesEntidad) || detallesEntidad.isEmpty()) {
			return List.of();
		}

		return detallesEntidad.stream()
				.map(DetallePedidoEntidadAssembler.getInstance()::ensamblarDominio)
				.collect(Collectors.toList());
	}

	private boolean tieneTexto(final String texto) {
		return Objects.nonNull(texto) && !texto.trim().isEmpty();
	}
}
