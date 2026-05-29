package co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl;

import co.edu.uco.patiomaruparking.entidad.DetallePedidoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.EntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class DetallePedidoEntidadAssembler
        implements EntidadAssembler<DetallePedidoDominio, DetallePedidoEntidad> {

    private static final DetallePedidoEntidadAssembler INSTANCE = new DetallePedidoEntidadAssembler();

    private DetallePedidoEntidadAssembler() {
        super();
    }

    public static DetallePedidoEntidadAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public DetallePedidoEntidad ensamblarEntidad(final DetallePedidoDominio dominio) {
        var detallePedidoEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                DetallePedidoDominio.builder().build());

        return DetallePedidoEntidad.builder()
                .codigoDetallePedido(detallePedidoEnsamblar.getCodigoDetallePedido())
                .cantidad(detallePedidoEnsamblar.getCantidad())
                .subtotal(detallePedidoEnsamblar.getSubtotal())
                .codigoPedido(detallePedidoEnsamblar.getCodigoPedido())
                .plato(PlatoEntidadAssembler.getInstance()
                        .ensamblarEntidad(detallePedidoEnsamblar.getPlato()))
                .build();
    }

    @Override
    public DetallePedidoDominio ensamblarDominio(final DetallePedidoEntidad entidad) {
        var detallePedidoEnsamblar = UtilObjeto.obtenerValorDefecto(
                entidad,
                DetallePedidoEntidad.builder().build());

        return DetallePedidoDominio.builder()
                .codigoDetallePedido(detallePedidoEnsamblar.getCodigoDetallePedido())
                .cantidad(detallePedidoEnsamblar.getCantidad())
                .subtotal(detallePedidoEnsamblar.getSubtotal())
                .codigoPedido(detallePedidoEnsamblar.getCodigoPedido())
                .plato(PlatoEntidadAssembler.getInstance()
                        .ensamblarDominio(detallePedidoEnsamblar.getPlato()))
                .build();
    }
}