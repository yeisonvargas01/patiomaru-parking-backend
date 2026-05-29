package co.edu.uco.patiomaruparking.negocio.assembler.dto.impl;

import java.util.List;

import co.edu.uco.patiomaruparking.dto.DetallePedidoDTO;
import co.edu.uco.patiomaruparking.dto.PedidoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.DTOAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio;
import co.edu.uco.patiomaruparking.negocio.dominio.PedidoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class PedidoDTOAssembler implements DTOAssembler<PedidoDominio, PedidoDTO> {
	
	private static final PedidoDTOAssembler INSTANCE = new PedidoDTOAssembler();

    private PedidoDTOAssembler() {
        super();
    }

    public static PedidoDTOAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public PedidoDTO ensamblarDTO(final PedidoDominio dominio) {
        var pedidoEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                PedidoDominio.builder().build());

        return PedidoDTO.builder()
                .codigoPedido(pedidoEnsamblar.getCodigoPedido())
                .fechaRegistro(pedidoEnsamblar.getFechaRegistro())
                .horaRegistro(pedidoEnsamblar.getHoraRegistro())
                .tipoAtencion(pedidoEnsamblar.getTipoAtencion())
                .estado(pedidoEnsamblar.getEstado())
                .totalPedido(pedidoEnsamblar.getTotalPedido())
                .mesa(MesaDTOAssembler.getInstance()
                        .ensamblarDTO(pedidoEnsamblar.getMesa()))
                .cliente(ClienteDTOAssembler.getInstance()
                        .ensamblarDTO(pedidoEnsamblar.getCliente()))
                .empleado(EmpleadoDTOAssembler.getInstance()
                        .ensamblarDTO(pedidoEnsamblar.getEmpleado()))
                .detalles(ensamblarDetallesDTO(pedidoEnsamblar.getDetalles()))
                .build();
    }

    @Override
    public PedidoDominio ensamblarDominio(final PedidoDTO dto) {
        var pedidoEnsamblar = UtilObjeto.obtenerValorDefecto(
                dto,
                PedidoDTO.builder().build());

        return PedidoDominio.builder()
                .codigoPedido(pedidoEnsamblar.getCodigoPedido())
                .fechaRegistro(pedidoEnsamblar.getFechaRegistro())
                .horaRegistro(pedidoEnsamblar.getHoraRegistro())
                .tipoAtencion(pedidoEnsamblar.getTipoAtencion())
                .estado(pedidoEnsamblar.getEstado())
                .totalPedido(pedidoEnsamblar.getTotalPedido())
                .mesa(MesaDTOAssembler.getInstance()
                        .ensamblarDominio(pedidoEnsamblar.getMesa()))
                .cliente(ClienteDTOAssembler.getInstance()
                        .ensamblarDominio(pedidoEnsamblar.getCliente()))
                .empleado(EmpleadoDTOAssembler.getInstance()
                        .ensamblarDominio(pedidoEnsamblar.getEmpleado()))
                .detalles(ensamblarDetallesDominio(pedidoEnsamblar.getDetalles()))
                .build();
    }

    private List<DetallePedidoDTO> ensamblarDetallesDTO(
            final List<DetallePedidoDominio> detallesDominio) {

        var detalles = UtilObjeto.obtenerValorDefecto(
                detallesDominio,
                List.<DetallePedidoDominio>of());

        return detalles.stream()
                .map(DetallePedidoDTOAssembler.getInstance()::ensamblarDTO)
                .toList();
    }

    private List<DetallePedidoDominio> ensamblarDetallesDominio(
            final List<DetallePedidoDTO> detallesDTO) {

        var detalles = UtilObjeto.obtenerValorDefecto(
                detallesDTO,
                List.<DetallePedidoDTO>of());

        return detalles.stream()
                .map(DetallePedidoDTOAssembler.getInstance()::ensamblarDominio)
                .toList();
    }
}