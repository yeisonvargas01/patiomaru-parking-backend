package co.edu.uco.patiomaruparking.negocio.dominio;

import java.math.BigDecimal;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class DetallePedidoDominio {

    private static final Integer CANTIDAD_DEFECTO = 0;
    private static final BigDecimal SUBTOTAL_DEFECTO = BigDecimal.ZERO;

    private String codigoDetallePedido = UtilTexto.TEXTO_VACIO;
    private Integer cantidad = CANTIDAD_DEFECTO;
    private BigDecimal subtotal = SUBTOTAL_DEFECTO;
    private String codigoPedido = UtilTexto.TEXTO_VACIO;
    private PlatoDominio plato = PlatoDominio.builder().build();

    private DetallePedidoDominio(final Builder builder) {
        setCodigoDetallePedido(builder.codigoDetallePedido);
        setCantidad(builder.cantidad);
        setSubtotal(builder.subtotal);
        setCodigoPedido(builder.codigoPedido);
        setPlato(builder.plato);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCodigoDetallePedido() {
        return codigoDetallePedido;
    }

    private void setCodigoDetallePedido(final String codigoDetallePedido) {
        this.codigoDetallePedido = UtilTexto.aplicarTrim(codigoDetallePedido);
    }

    public Integer getCantidad() {
        return cantidad;
    }

    private void setCantidad(final Integer cantidad) {
        this.cantidad = UtilObjeto.obtenerValorDefecto(
                cantidad,
                CANTIDAD_DEFECTO);
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    private void setSubtotal(final BigDecimal subtotal) {
        this.subtotal = UtilObjeto.obtenerValorDefecto(
                subtotal,
                SUBTOTAL_DEFECTO);
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    private void setCodigoPedido(final String codigoPedido) {
        this.codigoPedido = UtilTexto.aplicarTrim(codigoPedido);
    }

    public PlatoDominio getPlato() {
        return plato;
    }

    private void setPlato(final PlatoDominio plato) {
        this.plato = UtilObjeto.obtenerValorDefecto(
                plato,
                PlatoDominio.builder().build());
    }

    public boolean tieneCodigo() {
        return UtilTexto.tieneTexto(codigoDetallePedido);
    }

    public boolean tieneCodigoPedido() {
        return UtilTexto.tieneTexto(codigoPedido);
    }

    public boolean tieneCantidadValida() {
        return cantidad > CANTIDAD_DEFECTO;
    }

    public boolean tieneSubtotalValido() {
        return subtotal.compareTo(SUBTOTAL_DEFECTO) >= 0;
    }

    public boolean tienePlato() {
        return plato.tieneCodigo();
    }

    public static class Builder {

        private String codigoDetallePedido = UtilTexto.TEXTO_VACIO;
        private Integer cantidad = CANTIDAD_DEFECTO;
        private BigDecimal subtotal = SUBTOTAL_DEFECTO;
        private String codigoPedido = UtilTexto.TEXTO_VACIO;
        private PlatoDominio plato = PlatoDominio.builder().build();

        private Builder() {
            super();
        }

        public Builder codigoDetallePedido(final String codigoDetallePedido) {
            this.codigoDetallePedido = UtilTexto.aplicarTrim(codigoDetallePedido);
            return this;
        }

        public Builder cantidad(final Integer cantidad) {
            this.cantidad = UtilObjeto.obtenerValorDefecto(
                    cantidad,
                    CANTIDAD_DEFECTO);
            return this;
        }

        public Builder subtotal(final BigDecimal subtotal) {
            this.subtotal = UtilObjeto.obtenerValorDefecto(
                    subtotal,
                    SUBTOTAL_DEFECTO);
            return this;
        }

        public Builder codigoPedido(final String codigoPedido) {
            this.codigoPedido = UtilTexto.aplicarTrim(codigoPedido);
            return this;
        }

        public Builder plato(final PlatoDominio plato) {
            this.plato = UtilObjeto.obtenerValorDefecto(
                    plato,
                    PlatoDominio.builder().build());
            return this;
        }

        public DetallePedidoDominio build() {
            return new DetallePedidoDominio(this);
        }
    }
}