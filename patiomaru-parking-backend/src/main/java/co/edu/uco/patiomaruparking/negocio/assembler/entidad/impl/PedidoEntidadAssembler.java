package co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl;

import java.util.List;

import co.edu.uco.patiomaruparking.entidad.DetallePedidoEntidad;
import co.edu.uco.patiomaruparking.entidad.PedidoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.EntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio;
import co.edu.uco.patiomaruparking.negocio.dominio.PedidoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class PedidoEntidadAssembler implements EntidadAssembler<PedidoDominio, PedidoEntidad> {

    private static final PedidoEntidadAssembler INSTANCE = new PedidoEntidadAssembler();

    private PedidoEntidadAssembler() {
        super();
    }

    public static PedidoEntidadAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public PedidoEntidad ensamblarEntidad(final PedidoDominio dominio) {
        var pedidoEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                PedidoDominio.builder().build());

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
                .detalles(ensamblarDetallesEntidad(pedidoEnsamblar.getDetalles()))
                .build();
    }

    @Override
    public PedidoDominio ensamblarDominio(final PedidoEntidad entidad) {
        var pedidoEnsamblar = UtilObjeto.obtenerValorDefecto(
                entidad,
                PedidoEntidad.builder().build());

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
                .detalles(ensamblarDetallesDominio(pedidoEnsamblar.getDetalles()))
                .build();
    }

    private List<DetallePedidoEntidad> ensamblarDetallesEntidad(
            final List<DetallePedidoDominio> detallesDominio) {

        var detalles = UtilObjeto.obtenerValorDefecto(
                detallesDominio,
                List.<DetallePedidoDominio>of());

        return detalles.stream()
                .map(DetallePedidoEntidadAssembler.getInstance()::ensamblarEntidad)
                .toList();
    }

    private List<DetallePedidoDominio> ensamblarDetallesDominio(
            final List<DetallePedidoEntidad> detallesEntidad) {

        var detalles = UtilObjeto.obtenerValorDefecto(
                detallesEntidad,
                List.<DetallePedidoEntidad>of());

        return detalles.stream()
                .map(DetallePedidoEntidadAssembler.getInstance()::ensamblarDominio)
                .toList();
    }
}