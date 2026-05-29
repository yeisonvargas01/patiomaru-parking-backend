package co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl;

import co.edu.uco.patiomaruparking.entidad.EmpleadoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.EntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.EmpleadoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class EmpleadoEntidadAssembler implements EntidadAssembler<EmpleadoDominio, EmpleadoEntidad> {

    private static final EmpleadoEntidadAssembler INSTANCE = new EmpleadoEntidadAssembler();

    private EmpleadoEntidadAssembler() {
        super();
    }

    public static EmpleadoEntidadAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public EmpleadoEntidad ensamblarEntidad(final EmpleadoDominio dominio) {
        var empleadoEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                EmpleadoDominio.builder().build());

        return EmpleadoEntidad.builder()
                .codigoEmpleado(empleadoEnsamblar.getCodigoEmpleado())
                .numeroIdentificacion(empleadoEnsamblar.getNumeroIdentificacion())
                .primerNombre(empleadoEnsamblar.getPrimerNombre())
                .primerApellido(empleadoEnsamblar.getPrimerApellido())
                .segundoNombre(empleadoEnsamblar.getSegundoNombre())
                .segundoApellido(empleadoEnsamblar.getSegundoApellido())
                .fechaNacimiento(empleadoEnsamblar.getFechaNacimiento())
                .edad(empleadoEnsamblar.getEdad())
                .estado(empleadoEnsamblar.getEstado())
                .numeroTelefono(empleadoEnsamblar.getNumeroTelefono())
                .correoElectronico(empleadoEnsamblar.getCorreoElectronico())
                .direccionResidencia(empleadoEnsamblar.getDireccionResidencia())
                .ciudadResidencia(CiudadResidenciaEntidadAssembler.getInstance()
                        .ensamblarEntidad(empleadoEnsamblar.getCiudadResidencia()))
                .tipoDocumentoIdentificacion(TipoDocumentoIdentificacionEntidadAssembler.getInstance()
                        .ensamblarEntidad(empleadoEnsamblar.getTipoDocumentoIdentificacion()))
                .cargo(CargoEntidadAssembler.getInstance()
                        .ensamblarEntidad(empleadoEnsamblar.getCargo()))
                .build();
    }

    @Override
    public EmpleadoDominio ensamblarDominio(final EmpleadoEntidad entidad) {
        var empleadoEnsamblar = UtilObjeto.obtenerValorDefecto(
                entidad,
                EmpleadoEntidad.builder().build());

        return EmpleadoDominio.builder()
                .codigoEmpleado(empleadoEnsamblar.getCodigoEmpleado())
                .numeroIdentificacion(empleadoEnsamblar.getNumeroIdentificacion())
                .primerNombre(empleadoEnsamblar.getPrimerNombre())
                .primerApellido(empleadoEnsamblar.getPrimerApellido())
                .segundoNombre(empleadoEnsamblar.getSegundoNombre())
                .segundoApellido(empleadoEnsamblar.getSegundoApellido())
                .fechaNacimiento(empleadoEnsamblar.getFechaNacimiento())
                .edad(empleadoEnsamblar.getEdad())
                .estado(empleadoEnsamblar.getEstado())
                .numeroTelefono(empleadoEnsamblar.getNumeroTelefono())
                .correoElectronico(empleadoEnsamblar.getCorreoElectronico())
                .direccionResidencia(empleadoEnsamblar.getDireccionResidencia())
                .ciudadResidencia(CiudadResidenciaEntidadAssembler.getInstance()
                        .ensamblarDominio(empleadoEnsamblar.getCiudadResidencia()))
                .tipoDocumentoIdentificacion(TipoDocumentoIdentificacionEntidadAssembler.getInstance()
                        .ensamblarDominio(empleadoEnsamblar.getTipoDocumentoIdentificacion()))
                .cargo(CargoEntidadAssembler.getInstance()
                        .ensamblarDominio(empleadoEnsamblar.getCargo()))
                .build();
    }
}