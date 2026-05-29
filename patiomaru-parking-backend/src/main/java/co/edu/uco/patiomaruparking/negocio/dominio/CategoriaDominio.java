package co.edu.uco.patiomaruparking.negocio.dominio;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class CategoriaDominio {

    private String codigoCategoria = UtilTexto.TEXTO_VACIO;
    private String nombre = UtilTexto.TEXTO_VACIO;

    private CategoriaDominio(final Builder builder) {
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

    public boolean tieneCodigo() {
        return UtilTexto.tieneTexto(codigoCategoria);
    }

    public boolean tieneNombre() {
        return UtilTexto.tieneTexto(nombre);
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

        public CategoriaDominio build() {
            return new CategoriaDominio(this);
        }
    }
}