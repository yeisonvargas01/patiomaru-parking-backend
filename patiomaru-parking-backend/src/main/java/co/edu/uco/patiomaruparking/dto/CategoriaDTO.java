package co.edu.uco.patiomaruparking.dto;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class CategoriaDTO {

    private String codigoCategoria = UtilTexto.TEXTO_VACIO;
    private String nombre = UtilTexto.TEXTO_VACIO;

    public CategoriaDTO() {
        super();
    }

    private CategoriaDTO(final Builder builder) {
        setCodigoCategoria(builder.codigoCategoria);
        setNombre(builder.nombre);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(final String codigoCategoria) {
        this.codigoCategoria = UtilTexto.aplicarTrim(codigoCategoria);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = UtilTexto.aplicarTrim(nombre);
    }

    public static class Builder {

        private String codigoCategoria = UtilTexto.TEXTO_VACIO;
        private String nombre = UtilTexto.TEXTO_VACIO;

        private Builder() {
            super();
        }

        public Builder codigoCategoria(final String codigoCategoria) {
            this.codigoCategoria = UtilTexto.aplicarTrim(codigoCategoria);
            return this;
        }

        public Builder nombre(final String nombre) {
            this.nombre = UtilTexto.aplicarTrim(nombre);
            return this;
        }

        public CategoriaDTO build() {
            return new CategoriaDTO(this);
        }
    }
}