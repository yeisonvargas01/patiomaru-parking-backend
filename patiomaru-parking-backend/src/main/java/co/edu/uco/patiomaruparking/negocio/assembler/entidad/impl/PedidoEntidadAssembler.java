package co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl;

import java.util.List;
import java.util.stream.Collectors;

import co.edu.uco.patiomaruparking.entidad.DetallePedidoEntidad;
import co.edu.uco.patiomaruparking.entidad.PedidoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.EntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio;
import co.edu.uco.patiomaruparking.negocio.dominio.PedidoDominio;

public final class PedidoEntidadAssembler implements EntidadAssembler<PedidoDominio, PedidoEntidad> {

	private static PedidoEntidadAssembler INSTANCE = null;

	private PedidoEntidadAssembler() {
		super();
	}

	public static synchronized PedidoEntidadAssembler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PedidoEntidadAssembler();
		}

		return INSTANCE;
	}

	@Override
	public PedidoEntidad ensamblarEntidad(final PedidoDominio dominio) {
		var pedidoEnsamblar = dominio == null ? PedidoDominio.builder().build() : dominio;

		return PedidoEntidad.builder()
				.codigoPedido(pedidoEnsamblar.getCodigoPedido())
				.fechaRegistro(pedidoEnsamblar.getFechaRegistro())
				.horaRegistro(pedidoEnsamblar.getHoraRegistro())
				.tipoAtencion(pedidoEnsamblar.getTipoAtencion())
				.estado(pedidoEnsamblar.getEstado())
				.totalPedido(pedidoEnsamblar.getTotalPedido())
				.mesa(MesaEntidadAssembler.getInstance()
						.ensamblarEntidad(pedidoEnsamblar.getMesa()))
				.cliente(ClienteEntidadAssembler.getInstance()
						.ensamblarEntidad(pedidoEnsamblar.getCliente()))
				.empleado(EmpleadoEntidadAssembler.getInstance()
						.ensamblarEntidad(pedidoEnsamblar.getEmpleado()))
				.detalles(ensamblarDetallesEntidad(pedidoEnsamblar))
				.build();
	}

	@Override
	public PedidoDominio ensamblarDominio(final PedidoEntidad entidad) {
		var pedidoEnsamblar = entidad == null ? PedidoEntidad.builder().build() : entidad;

		return PedidoDominio.builder()
				.codigoPedido(pedidoEnsamblar.getCodigoPedido())
				.fechaRegistro(pedidoEnsamblar.getFechaRegistro())
				.horaRegistro(pedidoEnsamblar.getHoraRegistro())
				.tipoAtencion(pedidoEnsamblar.getTipoAtencion())
				.estado(pedidoEnsamblar.getEstado())
				.totalPedido(pedidoEnsamblar.getTotalPedido())
				.mesa(MesaEntidadAssembler.getInstance()
						.ensamblarDominio(pedidoEnsamblar.getMesa()))
				.cliente(ClienteEntidadAssembler.getInstance()
						.ensamblarDominio(pedidoEnsamblar.getCliente()))
				.empleado(EmpleadoEntidadAssembler.getInstance()
						.ensamblarDominio(pedidoEnsamblar.getEmpleado()))
				.detalles(ensamblarDetallesDominio(pedidoEnsamblar))
				.build();
	}

	private List<DetallePedidoEntidad> ensamblarDetallesEntidad(final PedidoDominio dominio) {
		if (dominio.getDetalles() == null) {
			return List.of();
		}

		return dominio.getDetalles()
				.stream()
				.map(DetallePedidoEntidadAssembler.getInstance()::ensamblarEntidad)
				.collect(Collectors.toList());
	}

	private List<DetallePedidoDominio> ensamblarDetallesDominio(final PedidoEntidad entidad) {
		if (entidad.getDetalles() == null) {
			return List.of();
		}

		return entidad.getDetalles()
				.stream()
				.map(DetallePedidoEntidadAssembler.getInstance()::ensamblarDominio)
				.collect(Collectors.toList());
	}
}