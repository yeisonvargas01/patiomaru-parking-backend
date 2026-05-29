package co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl;

import co.edu.uco.patiomaruparking.entidad.CiudadResidenciaEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.EntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.CiudadResidenciaDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class CiudadResidenciaEntidadAssembler
        implements EntidadAssembler<CiudadResidenciaDominio, CiudadResidenciaEntidad> {

    private static final CiudadResidenciaEntidadAssembler INSTANCE = new CiudadResidenciaEntidadAssembler();

    private CiudadResidenciaEntidadAssembler() {
        super();
    }

    public static CiudadResidenciaEntidadAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public CiudadResidenciaEntidad ensamblarEntidad(final CiudadResidenciaDominio dominio) {
        var ciudadResidenciaEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                CiudadResidenciaDominio.builder().build());

        return CiudadResidenciaEntidad.builder()
                .codigoCiudadResidencia(ciudadResidenciaEnsamblar.getCodigoCiudadResidencia())
                .nombre(ciudadResidenciaEnsamblar.getNombre())
                .build();
    }

    @Override
    public CiudadResidenciaDominio ensamblarDominio(final CiudadResidenciaEntidad entidad) {
        var ciudadResidenciaEnsamblar = UtilObjeto.obtenerValorDefecto(
                entidad,
                CiudadResidenciaEntidad.builder().build());

        return CiudadResidenciaDominio.builder()
                .codigoCiudadResidencia(ciudadResidenciaEnsamblar.getCodigoCiudadResidencia())
                .nombre(ciudadResidenciaEnsamblar.getNombre())
                .build();
    }
}
