package co.edu.uco.patiomaruparking.negocio.dominio;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class ClienteDominio {

    private static final Boolean ESTADO_DEFECTO = Boolean.TRUE;

    private String codigoCliente = UtilTexto.TEXTO_VACIO;
    private String nombre = UtilTexto.TEXTO_VACIO;
    private String telefono = UtilTexto.TEXTO_VACIO;
    private String correoElectronico = UtilTexto.TEXTO_VACIO;
    private Boolean estado = ESTADO_DEFECTO;

    private ClienteDominio(final Builder builder) {
        setCodigoCliente(builder.codigoCliente);
        setNombre(builder.nombre);
        setTelefono(builder.telefono);
        setCorreoElectronico(builder.correoElectronico);
        setEstado(builder.estado);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    private void setCodigoCliente(final String codigoCliente) {
        this.codigoCliente = UtilTexto.aplicarTrim(codigoCliente);
    }

    public String getNombre() {
        return nombre;
    }

    private void setNombre(final String nombre) {
        this.nombre = UtilTexto.aplicarTrim(nombre);
    }

    public String getTelefono() {
        return telefono;
    }

    private void setTelefono(final String telefono) {
        this.telefono = UtilTexto.aplicarTrim(telefono);
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    private void setCorreoElectronico(final String correoElectronico) {
        this.correoElectronico = UtilTexto.aplicarTrim(correoElectronico);
    }

    public Boolean getEstado() {
        return estado;
    }

    private void setEstado(final Boolean estado) {
        this.estado = UtilObjeto.obtenerValorDefecto(
                estado,
                ESTADO_DEFECTO);
    }

    public boolean tieneCodigo() {
        return UtilTexto.tieneTexto(codigoCliente);
    }

    public boolean tieneNombre() {
        return UtilTexto.tieneTexto(nombre);
    }

    public boolean tieneTelefono() {
        return UtilTexto.tieneTexto(telefono);
    }

    public boolean tieneCorreoElectronico() {
        return UtilTexto.tieneTexto(correoElectronico);
    }

    public boolean estaActivo() {
        return Boolean.TRUE.equals(estado);
    }

    public static class Builder {

        private String codigoCliente = UtilTexto.TEXTO_VACIO;
        private String nombre = UtilTexto.TEXTO_VACIO;
        private String telefono = UtilTexto.TEXTO_VACIO;
        private String correoElectronico = UtilTexto.TEXTO_VACIO;
        private Boolean estado = ESTADO_DEFECTO;

        private Builder() {
            super();
        }

        public Builder codigoCliente(final String codigoCliente) {
            this.codigoCliente = UtilTexto.aplicarTrim(codigoCliente);
            return this;
        }

        public Builder nombre(final String nombre) {
            this.nombre = UtilTexto.aplicarTrim(nombre);
            return this;
        }

        public Builder telefono(final String telefono) {
            this.telefono = UtilTexto.aplicarTrim(telefono);
            return this;
        }

        public Builder correoElectronico(final String correoElectronico) {
            this.correoElectronico = UtilTexto.aplicarTrim(correoElectronico);
            return this;
        }

        public Builder estado(final Boolean estado) {
            this.estado = UtilObjeto.obtenerValorDefecto(
                    estado,
                    ESTADO_DEFECTO);
            return this;
        }

        public ClienteDominio build() {
            return new ClienteDominio(this);
        }
    }
}