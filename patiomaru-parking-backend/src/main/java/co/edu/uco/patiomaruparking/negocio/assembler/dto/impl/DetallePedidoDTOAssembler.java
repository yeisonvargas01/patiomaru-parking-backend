package co.edu.uco.patiomaruparking.negocio.assembler.dto.impl;

import co.edu.uco.patiomaruparking.dto.DetallePedidoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.DTOAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class DetallePedidoDTOAssembler implements DTOAssembler<DetallePedidoDominio, DetallePedidoDTO> {
	
	private static final DetallePedidoDTOAssembler INSTANCE = new DetallePedidoDTOAssembler();

    private DetallePedidoDTOAssembler() {
        super();
    }

    public static DetallePedidoDTOAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public DetallePedidoDTO ensamblarDTO(final DetallePedidoDominio dominio) {
        var detallePedidoEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                DetallePedidoDominio.builder().build());

        return DetallePedidoDTO.builder()
                .codigoDetallePedido(detallePedidoEnsamblar.getCodigoDetallePedido())
                .cantidad(detallePedidoEnsamblar.getCantidad())
                .subtotal(detallePedidoEnsamblar.getSubtotal())
                .codigoPedido(detallePedidoEnsamblar.getCodigoPedido())
                .plato(PlatoDTOAssembler.getInstance()
                        .ensamblarDTO(detallePedidoEnsamblar.getPlato()))
                .build();
    }

    @Override
    public DetallePedidoDominio ensamblarDominio(final DetallePedidoDTO dto) {
        var detallePedidoEnsamblar = UtilObjeto.obtenerValorDefecto(
                dto,
                DetallePedidoDTO.builder().build());

        return DetallePedidoDominio.builder()
                .codigoDetallePedido(detallePedidoEnsamblar.getCodigoDetallePedido())
                .cantidad(detallePedidoEnsamblar.getCantidad())
                .subtotal(detallePedidoEnsamblar.getSubtotal())
                .codigoPedido(detallePedidoEnsamblar.getCodigoPedido())
                .plato(PlatoDTOAssembler.getInstance()
                        .ensamblarDominio(detallePedidoEnsamblar.getPlato()))
                .build();
    }
}