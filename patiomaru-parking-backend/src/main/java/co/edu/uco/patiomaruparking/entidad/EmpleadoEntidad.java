package co.edu.uco.patiomaruparking.entidad;

import java.time.LocalDate;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class EmpleadoEntidad {

    private static final Integer EDAD_DEFECTO = 0;
    private static final Boolean ESTADO_DEFECTO = Boolean.TRUE;
    private static final LocalDate FECHA_NACIMIENTO_DEFECTO = LocalDate.MIN;

    private String codigoEmpleado = UtilTexto.TEXTO_VACIO;
    private String numeroIdentificacion = UtilTexto.TEXTO_VACIO;
    private String primerNombre = UtilTexto.TEXTO_VACIO;
    private String primerApellido = UtilTexto.TEXTO_VACIO;
    private String segundoNombre = UtilTexto.TEXTO_VACIO;
    private String segundoApellido = UtilTexto.TEXTO_VACIO;
    private LocalDate fechaNacimiento = FECHA_NACIMIENTO_DEFECTO;
    private Integer edad = EDAD_DEFECTO;
    private Boolean estado = ESTADO_DEFECTO;
    private String numeroTelefono = UtilTexto.TEXTO_VACIO;
    private String correoElectronico = UtilTexto.TEXTO_VACIO;
    private String direccionResidencia = UtilTexto.TEXTO_VACIO;
    private CiudadResidenciaEntidad ciudadResidencia = CiudadResidenciaEntidad.builder().build();
    private TipoDocumentoIdentificacionEntidad tipoDocumentoIdentificacion = TipoDocumentoIdentificacionEntidad.builder().build();
    private CargoEntidad cargo = CargoEntidad.builder().build();

    private EmpleadoEntidad(final Builder builder) {
        setCodigoEmpleado(builder.codigoEmpleado);
        setNumeroIdentificacion(builder.numeroIdentificacion);
        setPrimerNombre(builder.primerNombre);
        setPrimerApellido(builder.primerApellido);
        setSegundoNombre(builder.segundoNombre);
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

    private void setCodigoEmpleado(final String codigoEmpleado) {
        this.codigoEmpleado = UtilTexto.aplicarTrim(codigoEmpleado);
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    private void setNumeroIdentificacion(final String numeroIdentificacion) {
        this.numeroIdentificacion = UtilTexto.aplicarTrim(numeroIdentificacion);
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    private void setPrimerNombre(final String primerNombre) {
        this.primerNombre = UtilTexto.aplicarTrim(primerNombre);
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    private void setPrimerApellido(final String primerApellido) {
        this.primerApellido = UtilTexto.aplicarTrim(primerApellido);
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    private void setSegundoNombre(final String segundoNombre) {
        this.segundoNombre = UtilTexto.aplicarTrim(segundoNombre);
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    private void setSegundoApellido(final String segundoApellido) {
        this.segundoApellido = UtilTexto.aplicarTrim(segundoApellido);
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    private void setFechaNacimiento(final LocalDate fechaNacimiento) {
        this.fechaNacimiento = UtilObjeto.obtenerValorDefecto(
                fechaNacimiento,
                FECHA_NACIMIENTO_DEFECTO);
    }

    public Integer getEdad() {
        return edad;
    }

    private void setEdad(final Integer edad) {
        this.edad = UtilObjeto.obtenerValorDefecto(
                edad,
                EDAD_DEFECTO);
    }

    public Boolean getEstado() {
        return estado;
    }

    private void setEstado(final Boolean estado) {
        this.estado = UtilObjeto.obtenerValorDefecto(
                estado,
                ESTADO_DEFECTO);
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    private void setNumeroTelefono(final String numeroTelefono) {
        this.numeroTelefono = UtilTexto.aplicarTrim(numeroTelefono);
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    private void setCorreoElectronico(final String correoElectronico) {
        this.correoElectronico = UtilTexto.aplicarTrim(correoElectronico);
    }

    public String getDireccionResidencia() {
        return direccionResidencia;
    }

    private void setDireccionResidencia(final String direccionResidencia) {
        this.direccionResidencia = UtilTexto.aplicarTrim(direccionResidencia);
    }

    public CiudadResidenciaEntidad getCiudadResidencia() {
        return ciudadResidencia;
    }

    private void setCiudadResidencia(final CiudadResidenciaEntidad ciudadResidencia) {
        this.ciudadResidencia = UtilObjeto.obtenerValorDefecto(
                ciudadResidencia,
                CiudadResidenciaEntidad.builder().build());
    }

    public TipoDocumentoIdentificacionEntidad getTipoDocumentoIdentificacion() {
        return tipoDocumentoIdentificacion;
    }

    private void setTipoDocumentoIdentificacion(
            final TipoDocumentoIdentificacionEntidad tipoDocumentoIdentificacion) {
        this.tipoDocumentoIdentificacion = UtilObjeto.obtenerValorDefecto(
                tipoDocumentoIdentificacion,
                TipoDocumentoIdentificacionEntidad.builder().build());
    }

    public CargoEntidad getCargo() {
        return cargo;
    }

    private void setCargo(final CargoEntidad cargo) {
        this.cargo = UtilObjeto.obtenerValorDefecto(
                cargo,
                CargoEntidad.builder().build());
    }

    public static class Builder {

        private String codigoEmpleado = UtilTexto.TEXTO_VACIO;
        private String numeroIdentificacion = UtilTexto.TEXTO_VACIO;
        private String primerNombre = UtilTexto.TEXTO_VACIO;
        private String primerApellido = UtilTexto.TEXTO_VACIO;
        private String segundoNombre = UtilTexto.TEXTO_VACIO;
        private String segundoApellido = UtilTexto.TEXTO_VACIO;
        private LocalDate fechaNacimiento = FECHA_NACIMIENTO_DEFECTO;
        private Integer edad = EDAD_DEFECTO;
        private Boolean estado = ESTADO_DEFECTO;
        private String numeroTelefono = UtilTexto.TEXTO_VACIO;
        private String correoElectronico = UtilTexto.TEXTO_VACIO;
        private String direccionResidencia = UtilTexto.TEXTO_VACIO;
        private CiudadResidenciaEntidad ciudadResidencia = CiudadResidenciaEntidad.builder().build();
        private TipoDocumentoIdentificacionEntidad tipoDocumentoIdentificacion = TipoDocumentoIdentificacionEntidad.builder().build();
        private CargoEntidad cargo = CargoEntidad.builder().build();

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

        public Builder primerApellido(final String primerApellido) {
            this.primerApellido = UtilTexto.aplicarTrim(primerApellido);
            return this;
        }

        public Builder segundoNombre(final String segundoNombre) {
            this.segundoNombre = UtilTexto.aplicarTrim(segundoNombre);
            return this;
        }

        public Builder segundoApellido(final String segundoApellido) {
            this.segundoApellido = UtilTexto.aplicarTrim(segundoApellido);
            return this;
        }

        public Builder fechaNacimiento(final LocalDate fechaNacimiento) {
            this.fechaNacimiento = UtilObjeto.obtenerValorDefecto(
                    fechaNacimiento,
                    FECHA_NACIMIENTO_DEFECTO);
            return this;
        }

        public Builder edad(final Integer edad) {
            this.edad = UtilObjeto.obtenerValorDefecto(
                    edad,
                    EDAD_DEFECTO);
            return this;
        }

        public Builder estado(final Boolean estado) {
            this.estado = UtilObjeto.obtenerValorDefecto(
                    estado,
                    ESTADO_DEFECTO);
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

        public Builder ciudadResidencia(final CiudadResidenciaEntidad ciudadResidencia) {
            this.ciudadResidencia = UtilObjeto.obtenerValorDefecto(
                    ciudadResidencia,
                    CiudadResidenciaEntidad.builder().build());
            return this;
        }

        public Builder tipoDocumentoIdentificacion(
                final TipoDocumentoIdentificacionEntidad tipoDocumentoIdentificacion) {
            this.tipoDocumentoIdentificacion = UtilObjeto.obtenerValorDefecto(
                    tipoDocumentoIdentificacion,
                    TipoDocumentoIdentificacionEntidad.builder().build());
            return this;
        }

        public Builder cargo(final CargoEntidad cargo) {
            this.cargo = UtilObjeto.obtenerValorDefecto(
                    cargo,
                    CargoEntidad.builder().build());
            return this;
        }

        public EmpleadoEntidad build() {
            return new EmpleadoEntidad(this);
        }
    }
}