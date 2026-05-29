package co.edu.uco.patiomaruparking.negocio.assembler.dto.impl;

import co.edu.uco.patiomaruparking.dto.EmpleadoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.DTOAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.EmpleadoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class EmpleadoDTOAssembler implements DTOAssembler<EmpleadoDominio, EmpleadoDTO> {

    private static final EmpleadoDTOAssembler INSTANCE = new EmpleadoDTOAssembler();

    private EmpleadoDTOAssembler() {
        super();
    }

    public static EmpleadoDTOAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public EmpleadoDTO ensamblarDTO(final EmpleadoDominio dominio) {
        var empleadoEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                EmpleadoDominio.builder().build());

        return EmpleadoDTO.builder()
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
                .ciudadResidencia(CiudadResidenciaDTOAssembler.getInstance()
                        .ensamblarDTO(empleadoEnsamblar.getCiudadResidencia()))
                .tipoDocumentoIdentificacion(TipoDocumentoIdentificacionDTOAssembler.getInstance()
                        .ensamblarDTO(empleadoEnsamblar.getTipoDocumentoIdentificacion()))
                .cargo(CargoDTOAssembler.getInstance()
                        .ensamblarDTO(empleadoEnsamblar.getCargo()))
                .build();
    }

    @Override
    public EmpleadoDominio ensamblarDominio(final EmpleadoDTO dto) {
        var empleadoEnsamblar = UtilObjeto.obtenerValorDefecto(
                dto,
                EmpleadoDTO.builder().build());

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
                .ciudadResidencia(CiudadResidenciaDTOAssembler.getInstance()
                        .ensamblarDominio(empleadoEnsamblar.getCiudadResidencia()))
                .tipoDocumentoIdentificacion(TipoDocumentoIdentificacionDTOAssembler.getInstance()
                        .ensamblarDominio(empleadoEnsamblar.getTipoDocumentoIdentificacion()))
                .cargo(CargoDTOAssembler.getInstance()
                        .ensamblarDominio(empleadoEnsamblar.getCargo()))
                .build();
    }
}