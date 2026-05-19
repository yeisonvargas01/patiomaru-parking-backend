package co.edu.uco.patiomaruparking.negocio.assembler.dto.impl;

import java.util.List;
import java.util.stream.Collectors;

import co.edu.uco.patiomaruparking.dto.PedidoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.DTOAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.PedidoDominio;

public final class PedidoDTOAssembler implements DTOAssembler<PedidoDominio, PedidoDTO> {

	private static PedidoDTOAssembler INSTANCE = null;

	private PedidoDTOAssembler() {
		super();
	}

	public static synchronized PedidoDTOAssembler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PedidoDTOAssembler();
		}

		return INSTANCE;
	}

	@Override
	public PedidoDTO ensamblarDTO(final PedidoDominio dominio) {
		var pedidoEnsamblar = dominio == null ? PedidoDominio.builder().build() : dominio;

		return PedidoDTO.builder()
				.codigoPedido(pedidoEnsamblar.getCodigoPedido())
				.fechaRegistro(pedidoEnsamblar.getFechaRegistro())
				.horaRegistro(pedidoEnsamblar.getHoraRegistro())
				.tipoAtencion(pedidoEnsamblar.getTipoAtencion())
				.estado(pedidoEnsamblar.getEstado())
				.totalPedido(pedidoEnsamblar.getTotalPedido())
				.mesa(MesaDTOAssembler.getInstance().ensamblarDTO(pedidoEnsamblar.getMesa()))
				.cliente(ClienteDTOAssembler.getInstance().ensamblarDTO(pedidoEnsamblar.getCliente()))
				.empleado(EmpleadoDTOAssembler.getInstance().ensamblarDTO(pedidoEnsamblar.getEmpleado()))
				.detalles(ensamblarDetallesDTO(pedidoEnsamblar))
				.build();
	}

	@Override
	public PedidoDominio ensamblarDominio(final PedidoDTO dto) {
		var pedidoEnsamblar = dto == null ? PedidoDTO.builder().build() : dto;

		return PedidoDominio.builder()
				.codigoPedido(pedidoEnsamblar.getCodigoPedido())
				.fechaRegistro(pedidoEnsamblar.getFechaRegistro())
				.horaRegistro(pedidoEnsamblar.getHoraRegistro())
				.tipoAtencion(pedidoEnsamblar.getTipoAtencion())
				.estado(pedidoEnsamblar.getEstado())
				.totalPedido(pedidoEnsamblar.getTotalPedido())
				.mesa(MesaDTOAssembler.getInstance().ensamblarDominio(pedidoEnsamblar.getMesa()))
				.cliente(ClienteDTOAssembler.getInstance().ensamblarDominio(pedidoEnsamblar.getCliente()))
				.empleado(EmpleadoDTOAssembler.getInstance().ensamblarDominio(pedidoEnsamblar.getEmpleado()))
				.detalles(ensamblarDetallesDominio(pedidoEnsamblar))
				.build();
	}

	private List<co.edu.uco.patiomaruparking.dto.DetallePedidoDTO> ensamblarDetallesDTO(
			final PedidoDominio dominio) {

		if (dominio.getDetalles() == null) {
			return List.of();
		}

		return dominio.getDetalles()
				.stream()
				.map(DetallePedidoDTOAssembler.getInstance()::ensamblarDTO)
				.collect(Collectors.toList());
	}

	private List<co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio> ensamblarDetallesDominio(
			final PedidoDTO dto) {

		if (dto.getDetalles() == null) {
			return List.of();
		}

		return dto.getDetalles()
				.stream()
				.map(DetallePedidoDTOAssembler.getInstance()::ensamblarDominio)
				.collect(Collectors.toList());
	}
}
