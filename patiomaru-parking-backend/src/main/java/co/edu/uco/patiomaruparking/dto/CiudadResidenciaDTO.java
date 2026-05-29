package co.edu.uco.patiomaruparking.dto;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class CiudadResidenciaDTO {

    private String codigoCiudadResidencia = UtilTexto.TEXTO_VACIO;
    private String nombre = UtilTexto.TEXTO_VACIO;

    public CiudadResidenciaDTO() {
        super();
    }

    private CiudadResidenciaDTO(final Builder builder) {
        setCodigoCiudadResidencia(builder.codigoCiudadResidencia);
        setNombre(builder.nombre);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCodigoCiudadResidencia() {
        return codigoCiudadResidencia;
    }

    public void setCodigoCiudadResidencia(final String codigoCiudadResidencia) {
        this.codigoCiudadResidencia = UtilTexto.aplicarTrim(codigoCiudadResidencia);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = UtilTexto.aplicarTrim(nombre);
    }

    public static class Builder {

        private String codigoCiudadResidencia = UtilTexto.TEXTO_VACIO;
        private String nombre = UtilTexto.TEXTO_VACIO;

        private Builder() {
            super();
        }

        public Builder codigoCiudadResidencia(final String codigoCiudadResidencia) {
            this.codigoCiudadResidencia = UtilTexto.aplicarTrim(codigoCiudadResidencia);
            return this;
        }

        public Builder nombre(final String nombre) {
            this.nombre = UtilTexto.aplicarTrim(nombre);
            return this;
        }

        public CiudadResidenciaDTO build() {
            return new CiudadResidenciaDTO(this);
        }
    }
}