package co.edu.uco.patiomaruparking.entidad;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class CategoriaEntidad {

    private String codigoCategoria = UtilTexto.TEXTO_VACIO;
    private String nombre = UtilTexto.TEXTO_VACIO;

    private CategoriaEntidad(final Builder builder) {
        setCodigoCategoria(builder.codigoCategoria);
        setNombre(builder.nombre);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCodigoCategoria() {
        return codigoCategoria;
    }

    private void setCodigoCategoria(final String codigoCategoria) {
        this.codigoCategoria = UtilTexto.aplicarTrim(codigoCategoria);
    }

    public String getNombre() {
        return nombre;
    }

    private void setNombre(final String nombre) {
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

        public CategoriaEntidad build() {
            return new CategoriaEntidad(this);
        }
    }
}