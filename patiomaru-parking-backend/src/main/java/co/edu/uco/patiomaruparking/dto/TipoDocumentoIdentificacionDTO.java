package co.edu.uco.patiomaruparking.dto;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class TipoDocumentoIdentificacionDTO {

    private String codigoTipoDocumentoIdentificacion = UtilTexto.TEXTO_VACIO;
    private String nombre = UtilTexto.TEXTO_VACIO;

    public TipoDocumentoIdentificacionDTO() {
        super();
    }

    private TipoDocumentoIdentificacionDTO(final Builder builder) {
        setCodigoTipoDocumentoIdentificacion(builder.codigoTipoDocumentoIdentificacion);
        setNombre(builder.nombre);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCodigoTipoDocumentoIdentificacion() {
        return codigoTipoDocumentoIdentificacion;
    }

    public void setCodigoTipoDocumentoIdentificacion(final String codigoTipoDocumentoIdentificacion) {
        this.codigoTipoDocumentoIdentificacion = UtilTexto.aplicarTrim(codigoTipoDocumentoIdentificacion);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = UtilTexto.aplicarTrim(nombre);
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

        public TipoDocumentoIdentificacionDTO build() {
            return new TipoDocumentoIdentificacionDTO(this);
        }
    }
}