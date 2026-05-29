package co.edu.uco.patiomaruparking.negocio.dominio;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class TipoDocumentoIdentificacionDominio {

    private String codigoTipoDocumentoIdentificacion = UtilTexto.TEXTO_VACIO;
    private String nombre = UtilTexto.TEXTO_VACIO;

    private TipoDocumentoIdentificacionDominio(final Builder builder) {
        setCodigoTipoDocumentoIdentificacion(builder.codigoTipoDocumentoIdentificacion);
        setNombre(builder.nombre);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCodigoTipoDocumentoIdentificacion() {
        return codigoTipoDocumentoIdentificacion;
    }

    private void setCodigoTipoDocumentoIdentificacion(final String codigoTipoDocumentoIdentificacion) {
        this.codigoTipoDocumentoIdentificacion = UtilTexto.aplicarTrim(codigoTipoDocumentoIdentificacion);
    }

    public String getNombre() {
        return nombre;
    }

    private void setNombre(final String nombre) {
        this.nombre = UtilTexto.aplicarTrim(nombre);
    }

    public boolean tieneCodigo() {
        return UtilTexto.tieneTexto(codigoTipoDocumentoIdentificacion);
    }

    public boolean tieneNombre() {
        return UtilTexto.tieneTexto(nombre);
    }

    public static class Builder {

        private String codigoTipoDocumentoIdentificacion = UtilTexto.TEXTO_VACIO;
        private String nombre = UtilTexto.TEXTO_VACIO;

        private Builder() {
            super();
        }

        public Builder codigoTipoDocumentoIdentificacion(final String codigoTipoDocumentoIdentificacion) {
            this.codigoTipoDocumentoIdentificacion = UtilTexto.aplicarTrim(codigoTipoDocumentoIdentificacion);
            return this;
        }

        public Builder nombre(final String nombre) {
            this.nombre = UtilTexto.aplicarTrim(nombre);
            return this;
        }

        public TipoDocumentoIdentificacionDominio build() {
            return new TipoDocumentoIdentificacionDominio(this);
        }
    }
}