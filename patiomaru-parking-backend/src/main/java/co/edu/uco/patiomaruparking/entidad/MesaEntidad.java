package co.edu.uco.patiomaruparking.entidad;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class MesaEntidad {

    private String codigoMesa = UtilTexto.TEXTO_VACIO;
    private String nombre = UtilTexto.TEXTO_VACIO;

    private MesaEntidad(final Builder builder) {
        setCodigoMesa(builder.codigoMesa);
        setNombre(builder.nombre);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCodigoMesa() {
        return codigoMesa;
    }

    private void setCodigoMesa(final String codigoMesa) {
        this.codigoMesa = UtilTexto.aplicarTrim(codigoMesa);
    }

    public String getNombre() {
        return nombre;
    }

    private void setNombre(final String nombre) {
        this.nombre = UtilTexto.aplicarTrim(nombre);
    }

    public static class Builder {

        private String codigoMesa = UtilTexto.TEXTO_VACIO;
        private String nombre = UtilTexto.TEXTO_VACIO;

        private Builder() {
            super();
        }

        public Builder codigoMesa(final String codigoMesa) {
            this.codigoMesa = UtilTexto.aplicarTrim(codigoMesa);
            return this;
        }

        public Builder nombre(final String nombre) {
            this.nombre = UtilTexto.aplicarTrim(nombre);
            return this;
        }

        public MesaEntidad build() {
            return new MesaEntidad(this);
        }
    }
}