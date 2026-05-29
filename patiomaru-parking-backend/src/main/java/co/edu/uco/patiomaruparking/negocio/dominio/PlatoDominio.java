package co.edu.uco.patiomaruparking.negocio.dominio;

import java.math.BigDecimal;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class PlatoDominio {

    private static final BigDecimal PRECIO_VENTA_DEFECTO = BigDecimal.ZERO;
    private static final Boolean ESTADO_DEFECTO = Boolean.TRUE;

    private String codigoPlato = UtilTexto.TEXTO_VACIO;
    private String nombre = UtilTexto.TEXTO_VACIO;
    private BigDecimal precioVenta = PRECIO_VENTA_DEFECTO;
    private Boolean estado = ESTADO_DEFECTO;
    private CategoriaDominio categoria = CategoriaDominio.builder().build();

    private PlatoDominio(final Builder builder) {
        setCodigoPlato(builder.codigoPlato);
        setNombre(builder.nombre);
        setPrecioVenta(builder.precioVenta);
        setEstado(builder.estado);
        setCategoria(builder.categoria);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCodigoPlato() {
        return codigoPlato;
    }

    private void setCodigoPlato(final String codigoPlato) {
        this.codigoPlato = UtilTexto.aplicarTrim(codigoPlato);
    }

    public String getNombre() {
        return nombre;
    }

    private void setNombre(final String nombre) {
        this.nombre = UtilTexto.aplicarTrim(nombre);
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    private void setPrecioVenta(final BigDecimal precioVenta) {
        this.precioVenta = UtilObjeto.obtenerValorDefecto(
                precioVenta,
                PRECIO_VENTA_DEFECTO);
    }

    public Boolean getEstado() {
        return estado;
    }

    private void setEstado(final Boolean estado) {
        this.estado = UtilObjeto.obtenerValorDefecto(
                estado,
                ESTADO_DEFECTO);
    }

    public CategoriaDominio getCategoria() {
        return categoria;
    }

    private void setCategoria(final CategoriaDominio categoria) {
        this.categoria = UtilObjeto.obtenerValorDefecto(
                categoria,
                CategoriaDominio.builder().build());
    }

    public boolean tieneCodigo() {
        return UtilTexto.tieneTexto(codigoPlato);
    }

    public boolean tieneNombre() {
        return UtilTexto.tieneTexto(nombre);
    }

    public boolean tieneCategoria() {
        return categoria.tieneCodigo();
    }

    public boolean estaDisponible() {
        return Boolean.TRUE.equals(estado);
    }

    public boolean tienePrecioVentaValido() {
        return precioVenta.compareTo(PRECIO_VENTA_DEFECTO) > 0;
    }

    public static class Builder {

        private String codigoPlato = UtilTexto.TEXTO_VACIO;
        private String nombre = UtilTexto.TEXTO_VACIO;
        private BigDecimal precioVenta = PRECIO_VENTA_DEFECTO;
        private Boolean estado = ESTADO_DEFECTO;
        private CategoriaDominio categoria = CategoriaDominio.builder().build();

        private Builder() {
            super();
        }

        public Builder codigoPlato(final String codigoPlato) {
            this.codigoPlato = UtilTexto.aplicarTrim(codigoPlato);
            return this;
        }

        public Builder nombre(final String nombre) {
            this.nombre = UtilTexto.aplicarTrim(nombre);
            return this;
        }

        public Builder precioVenta(final BigDecimal precioVenta) {
            this.precioVenta = UtilObjeto.obtenerValorDefecto(
                    precioVenta,
                    PRECIO_VENTA_DEFECTO);
            return this;
        }

        public Builder estado(final Boolean estado) {
            this.estado = UtilObjeto.obtenerValorDefecto(
                    estado,
                    ESTADO_DEFECTO);
            return this;
        }

        public Builder categoria(final CategoriaDominio categoria) {
            this.categoria = UtilObjeto.obtenerValorDefecto(
                    categoria,
                    CategoriaDominio.builder().build());
            return this;
        }

        public PlatoDominio build() {
            return new PlatoDominio(this);
        }
    }
}