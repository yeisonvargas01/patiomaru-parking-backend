package co.edu.uco.patiomaruparking.dto;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class CargoDTO {

    private static final String TEXTO_VACIO = "";
    private static final Boolean ACTIVO_DEFECTO = Boolean.TRUE;

    private String codigoCargo = TEXTO_VACIO;
    private String nombre = TEXTO_VACIO;
    private Boolean activo = ACTIVO_DEFECTO;

    public CargoDTO() {
        super();
    }

    private CargoDTO(final Builder builder) {
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

    public void setCodigoCargo(final String codigoCargo) {
        this.codigoCargo = UtilTexto.aplicarTrim(codigoCargo);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = UtilTexto.aplicarTrim(nombre);
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(final Boolean activo) {
        this.activo = UtilObjeto.obtenerValorDefecto(activo, ACTIVO_DEFECTO);
    }

    public static class Builder {

        private String codigoCargo = TEXTO_VACIO;
        private String nombre = TEXTO_VACIO;
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

        public CargoDTO build() {
            return new CargoDTO(this);
        }
    }
}