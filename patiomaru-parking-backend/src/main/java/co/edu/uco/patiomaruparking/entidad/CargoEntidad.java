package co.edu.uco.patiomaruparking.entidad;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class CargoEntidad {

    private static final Boolean ACTIVO_DEFECTO = Boolean.TRUE;

    private String codigoCargo = UtilTexto.TEXTO_VACIO;
    private String nombre = UtilTexto.TEXTO_VACIO;
    private Boolean activo = ACTIVO_DEFECTO;

    private CargoEntidad(final Builder builder) {
        setCodigoCargo(builder.codigoCargo);
        setNombre(builder.nombre);
        setActivo(builder.activo);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCodigoCargo() {
        return codigoCargo;
    }

    private void setCodigoCargo(final String codigoCargo) {
        this.codigoCargo = UtilTexto.aplicarTrim(codigoCargo);
    }

    public String getNombre() {
        return nombre;
    }

    private void setNombre(final String nombre) {
        this.nombre = UtilTexto.aplicarTrim(nombre);
    }

    public Boolean getActivo() {
        return activo;
    }

    private void setActivo(final Boolean activo) {
        this.activo = UtilObjeto.obtenerValorDefecto(activo, ACTIVO_DEFECTO);
    }

    public static class Builder {

        private String codigoCargo = UtilTexto.TEXTO_VACIO;
        private String nombre = UtilTexto.TEXTO_VACIO;
        private Boolean activo = ACTIVO_DEFECTO;

        private Builder() {
            super();
        }

        public Builder codigoCargo(final String codigoCargo) {
            this.codigoCargo = UtilTexto.aplicarTrim(codigoCargo);
            return this;
        }

        public Builder nombre(final String nombre) {
            this.nombre = UtilTexto.aplicarTrim(nombre);
            return this;
        }

        public Builder activo(final Boolean activo) {
            this.activo = UtilObjeto.obtenerValorDefecto(activo, ACTIVO_DEFECTO);
            return this;
        }

        public CargoEntidad build() {
            return new CargoEntidad(this);
        }
    }
}