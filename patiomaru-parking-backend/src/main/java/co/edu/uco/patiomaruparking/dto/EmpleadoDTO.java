package co.edu.uco.patiomaruparking.dto;

import java.time.LocalDate;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class EmpleadoDTO {

    private static final Integer EDAD_DEFECTO = 0;
    private static final Boolean ESTADO_DEFECTO = Boolean.TRUE;
    private static final LocalDate FECHA_DEFECTO = LocalDate.MIN;

    private String codigoEmpleado = UtilTexto.TEXTO_VACIO;
    private String numeroIdentificacion = UtilTexto.TEXTO_VACIO;
    private String primerNombre = UtilTexto.TEXTO_VACIO;
    private String segundoNombre = UtilTexto.TEXTO_VACIO;
    private String primerApellido = UtilTexto.TEXTO_VACIO;
    private String segundoApellido = UtilTexto.TEXTO_VACIO;
    private LocalDate fechaNacimiento = FECHA_DEFECTO;
    private Integer edad = EDAD_DEFECTO;
    private Boolean estado = ESTADO_DEFECTO;
    private String numeroTelefono = UtilTexto.TEXTO_VACIO;
    private String correoElectronico = UtilTexto.TEXTO_VACIO;
    private String direccionResidencia = UtilTexto.TEXTO_VACIO;
    private CiudadResidenciaDTO ciudadResidencia = CiudadResidenciaDTO.builder().build();
    private TipoDocumentoIdentificacionDTO tipoDocumentoIdentificacion = TipoDocumentoIdentificacionDTO.builder().build();
    private CargoDTO cargo = CargoDTO.builder().build();

    public EmpleadoDTO() {
        super();
    }

    private EmpleadoDTO(final Builder builder) {
        setCodigoEmpleado(builder.codigoEmpleado);
        setNumeroIdentificacion(builder.numeroIdentificacion);
        setPrimerNombre(builder.primerNombre);
        setSegundoNombre(builder.segundoNombre);
        setPrimerApellido(builder.primerApellido);
        setSegundoApellido(builder.segundoApellido);
        setFechaNacimiento(builder.fechaNacimiento);
        setEdad(builder.edad);
        setEstado(builder.estado);
        setNumeroTelefono(builder.numeroTelefono);
        setCorreoElectronico(builder.correoElectronico);
        setDireccionResidencia(builder.direccionResidencia);
        setCiudadResidencia(builder.ciudadResidencia);
        setTipoDocumentoIdentificacion(builder.tipoDocumentoIdentificacion);
        setCargo(builder.cargo);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(final String codigoEmpleado) {
        this.codigoEmpleado = UtilTexto.aplicarTrim(codigoEmpleado);
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(final String numeroIdentificacion) {
        this.numeroIdentificacion = UtilTexto.aplicarTrim(numeroIdentificacion);
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(final String primerNombre) {
        this.primerNombre = UtilTexto.aplicarTrim(primerNombre);
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(final String segundoNombre) {
        this.segundoNombre = UtilTexto.aplicarTrim(segundoNombre);
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(final String primerApellido) {
        this.primerApellido = UtilTexto.aplicarTrim(primerApellido);
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(final String segundoApellido) {
        this.segundoApellido = UtilTexto.aplicarTrim(segundoApellido);
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(final LocalDate fechaNacimiento) {
        this.fechaNacimiento = UtilObjeto.obtenerValorDefecto(fechaNacimiento, FECHA_DEFECTO);
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(final Integer edad) {
        this.edad = UtilObjeto.obtenerValorDefecto(edad, EDAD_DEFECTO);
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(final Boolean estado) {
        this.estado = UtilObjeto.obtenerValorDefecto(estado, ESTADO_DEFECTO);
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(final String numeroTelefono) {
        this.numeroTelefono = UtilTexto.aplicarTrim(numeroTelefono);
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(final String correoElectronico) {
        this.correoElectronico = UtilTexto.aplicarTrim(correoElectronico);
    }

    public String getDireccionResidencia() {
        return direccionResidencia;
    }

    public void setDireccionResidencia(final String direccionResidencia) {
        this.direccionResidencia = UtilTexto.aplicarTrim(direccionResidencia);
    }

    public CiudadResidenciaDTO getCiudadResidencia() {
        return ciudadResidencia;
    }

    public void setCiudadResidencia(final CiudadResidenciaDTO ciudadResidencia) {
        this.ciudadResidencia = UtilObjeto.obtenerValorDefecto(
                ciudadResidencia,
                CiudadResidenciaDTO.builder().build());
    }

    public TipoDocumentoIdentificacionDTO getTipoDocumentoIdentificacion() {
        return tipoDocumentoIdentificacion;
    }

    public void setTipoDocumentoIdentificacion(
            final TipoDocumentoIdentificacionDTO tipoDocumentoIdentificacion) {
        this.tipoDocumentoIdentificacion = UtilObjeto.obtenerValorDefecto(
                tipoDocumentoIdentificacion,
                TipoDocumentoIdentificacionDTO.builder().build());
    }

    public CargoDTO getCargo() {
        return cargo;
    }

    public void setCargo(final CargoDTO cargo) {
        this.cargo = UtilObjeto.obtenerValorDefecto(
                cargo,
                CargoDTO.builder().build());
    }

    public static class Builder {

        private String codigoEmpleado = UtilTexto.TEXTO_VACIO;
        private String numeroIdentificacion = UtilTexto.TEXTO_VACIO;
        private String primerNombre = UtilTexto.TEXTO_VACIO;
        private String segundoNombre = UtilTexto.TEXTO_VACIO;
        private String primerApellido = UtilTexto.TEXTO_VACIO;
        private String segundoApellido = UtilTexto.TEXTO_VACIO;
        private LocalDate fechaNacimiento = FECHA_DEFECTO;
        private Integer edad = EDAD_DEFECTO;
        private Boolean estado = ESTADO_DEFECTO;
        private String numeroTelefono = UtilTexto.TEXTO_VACIO;
        private String correoElectronico = UtilTexto.TEXTO_VACIO;
        private String direccionResidencia = UtilTexto.TEXTO_VACIO;
        private CiudadResidenciaDTO ciudadResidencia = CiudadResidenciaDTO.builder().build();
        private TipoDocumentoIdentificacionDTO tipoDocumentoIdentificacion = TipoDocumentoIdentificacionDTO.builder().build();
        private CargoDTO cargo = CargoDTO.builder().build();

        private Builder() {
            super();
        }

        public Builder codigoEmpleado(final String codigoEmpleado) {
            this.codigoEmpleado = UtilTexto.aplicarTrim(codigoEmpleado);
            return this;
        }

        public Builder numeroIdentificacion(final String numeroIdentificacion) {
            this.numeroIdentificacion = UtilTexto.aplicarTrim(numeroIdentificacion);
            return this;
        }

        public Builder primerNombre(final String primerNombre) {
            this.primerNombre = UtilTexto.aplicarTrim(primerNombre);
            return this;
        }

        public Builder segundoNombre(final String segundoNombre) {
            this.segundoNombre = UtilTexto.aplicarTrim(segundoNombre);
            return this;
        }

        public Builder primerApellido(final String primerApellido) {
            this.primerApellido = UtilTexto.aplicarTrim(primerApellido);
            return this;
        }

        public Builder segundoApellido(final String segundoApellido) {
            this.segundoApellido = UtilTexto.aplicarTrim(segundoApellido);
            return this;
        }

        public Builder fechaNacimiento(final LocalDate fechaNacimiento) {
            this.fechaNacimiento = UtilObjeto.obtenerValorDefecto(fechaNacimiento, FECHA_DEFECTO);
            return this;
        }

        public Builder edad(final Integer edad) {
            this.edad = UtilObjeto.obtenerValorDefecto(edad, EDAD_DEFECTO);
            return this;
        }

        public Builder estado(final Boolean estado) {
            this.estado = UtilObjeto.obtenerValorDefecto(estado, ESTADO_DEFECTO);
            return this;
        }

        public Builder numeroTelefono(final String numeroTelefono) {
            this.numeroTelefono = UtilTexto.aplicarTrim(numeroTelefono);
            return this;
        }

        public Builder correoElectronico(final String correoElectronico) {
            this.correoElectronico = UtilTexto.aplicarTrim(correoElectronico);
            return this;
        }

        public Builder direccionResidencia(final String direccionResidencia) {
            this.direccionResidencia = UtilTexto.aplicarTrim(direccionResidencia);
            return this;
        }

        public Builder ciudadResidencia(final CiudadResidenciaDTO ciudadResidencia) {
            this.ciudadResidencia = UtilObjeto.obtenerValorDefecto(
                    ciudadResidencia,
                    CiudadResidenciaDTO.builder().build());
            return this;
        }

        public Builder tipoDocumentoIdentificacion(
                final TipoDocumentoIdentificacionDTO tipoDocumentoIdentificacion) {
            this.tipoDocumentoIdentificacion = UtilObjeto.obtenerValorDefecto(
                    tipoDocumentoIdentificacion,
                    TipoDocumentoIdentificacionDTO.builder().build());
            return this;
        }

        public Builder cargo(final CargoDTO cargo) {
            this.cargo = UtilObjeto.obtenerValorDefecto(
                    cargo,
                    CargoDTO.builder().build());
            return this;
        }

        public EmpleadoDTO build() {
            return new EmpleadoDTO(this);
        }
    }
}