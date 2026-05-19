package co.edu.uco.patiomaruparking.negocio.assembler.dto.impl;

import co.edu.uco.patiomaruparking.dto.DetallePedidoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.DTOAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio;

public final class DetallePedidoDTOAssembler implements DTOAssembler<DetallePedidoDominio, DetallePedidoDTO> {

	private static DetallePedidoDTOAssembler INSTANCE = null;

	private DetallePedidoDTOAssembler() {
		super();
	}

	public static synchronized DetallePedidoDTOAssembler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DetallePedidoDTOAssembler();
		}

		return INSTANCE;
	}

	@Override
	public DetallePedidoDTO ensamblarDTO(final DetallePedidoDominio dominio) {
		var detallePedidoEnsamblar = dominio == null ? DetallePedidoDominio.builder().build() : dominio;

		return DetallePedidoDTO.builder()
				.codigoDetallePedido(detallePedidoEnsamblar.getCodigoDetallePedido())
				.cantidad(detallePedidoEnsamblar.getCantidad())
				.subtotal(detallePedidoEnsamblar.getSubtotal())
				.codigoPedido(detallePedidoEnsamblar.getCodigoPedido())
				.plato(PlatoDTOAssembler.getInstance().ensamblarDTO(detallePedidoEnsamblar.getPlato()))
				.build();
	}

	@Override
	public DetallePedidoDominio ensamblarDominio(final DetallePedidoDTO dto) {
		var detallePedidoEnsamblar = dto == null ? DetallePedidoDTO.builder().build() : dto;

		return DetallePedidoDominio.builder()
				.codigoDetallePedido(detallePedidoEnsamblar.getCodigoDetallePedido())
				.cantidad(detallePedidoEnsamblar.getCantidad())
				.subtotal(detallePedidoEnsamblar.getSubtotal())
				.codigoPedido(detallePedidoEnsamblar.getCodigoPedido())
				.plato(PlatoDTOAssembler.getInstance().ensamblarDominio(detallePedidoEnsamblar.getPlato()))
				.build();
	}
}
