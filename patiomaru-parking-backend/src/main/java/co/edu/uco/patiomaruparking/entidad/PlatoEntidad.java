package co.edu.uco.patiomaruparking.entidad;

import java.math.BigDecimal;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class PlatoEntidad {

    private static final BigDecimal PRECIO_VENTA_DEFECTO = BigDecimal.ZERO;
    private static final Boolean ESTADO_DEFECTO = Boolean.TRUE;

    private String codigoPlato = UtilTexto.TEXTO_VACIO;
    private String nombre = UtilTexto.TEXTO_VACIO;
    private BigDecimal precioVenta = PRECIO_VENTA_DEFECTO;
    private Boolean estado = ESTADO_DEFECTO;
    private CategoriaEntidad categoria = CategoriaEntidad.builder().build();

    private PlatoEntidad(final Builder builder) {
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

    public CategoriaEntidad getCategoria() {
        return categoria;
    }

    private void setCategoria(final CategoriaEntidad categoria) {
        this.categoria = UtilObjeto.obtenerValorDefecto(
                categoria,
                CategoriaEntidad.builder().build());
    }

    public static class Builder {

        private String codigoPlato = UtilTexto.TEXTO_VACIO;
        private String nombre = UtilTexto.TEXTO_VACIO;
        private BigDecimal precioVenta = PRECIO_VENTA_DEFECTO;
        private Boolean estado = ESTADO_DEFECTO;
        private CategoriaEntidad categoria = CategoriaEntidad.builder().build();

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

        public Builder categoria(final CategoriaEntidad categoria) {
            this.categoria = UtilObjeto.obtenerValorDefecto(
                    categoria,
                    CategoriaEntidad.builder().build());
            return this;
        }

        public PlatoEntidad build() {
            return new PlatoEntidad(this);
        }
    }
}