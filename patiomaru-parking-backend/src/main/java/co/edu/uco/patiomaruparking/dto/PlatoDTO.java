package co.edu.uco.patiomaruparking.dto;

import java.math.BigDecimal;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class PlatoDTO {

    private static final BigDecimal PRECIO_VENTA_DEFECTO = BigDecimal.ZERO;
    private static final Boolean ESTADO_DEFECTO = Boolean.TRUE;

    private String codigoPlato = UtilTexto.TEXTO_VACIO;
    private String nombre = UtilTexto.TEXTO_VACIO;
    private CategoriaDTO categoria = CategoriaDTO.builder().build();
    private BigDecimal precioVenta = PRECIO_VENTA_DEFECTO;
    private Boolean estado = ESTADO_DEFECTO;

    public PlatoDTO() {
        super();
    }

    private PlatoDTO(final Builder builder) {
        setCodigoPlato(builder.codigoPlato);
        setNombre(builder.nombre);
        setCategoria(builder.categoria);
        setPrecioVenta(builder.precioVenta);
        setEstado(builder.estado);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(final String codigoPlato) {
        this.codigoPlato = UtilTexto.aplicarTrim(codigoPlato);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = UtilTexto.aplicarTrim(nombre);
    }

    public CategoriaDTO getCategoria() {
        return categoria;
    }

    public void setCategoria(final CategoriaDTO categoria) {
        this.categoria = UtilObjeto.obtenerValorDefecto(
                categoria,
                CategoriaDTO.builder().build());
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(final BigDecimal precioVenta) {
        this.precioVenta = UtilObjeto.obtenerValorDefecto(
                precioVenta,
                PRECIO_VENTA_DEFECTO);
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(final Boolean estado) {
        this.estado = UtilObjeto.obtenerValorDefecto(
                estado,
                ESTADO_DEFECTO);
    }

    public static class Builder {

        private String codigoPlato = UtilTexto.TEXTO_VACIO;
        private String nombre = UtilTexto.TEXTO_VACIO;
        private CategoriaDTO categoria = CategoriaDTO.builder().build();
        private BigDecimal precioVenta = PRECIO_VENTA_DEFECTO;
        private Boolean estado = ESTADO_DEFECTO;

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

        public Builder categoria(final CategoriaDTO categoria) {
            this.categoria = UtilObjeto.obtenerValorDefecto(
                    categoria,
                    CategoriaDTO.builder().build());
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

        public PlatoDTO build() {
            return new PlatoDTO(this);
        }
    }
}