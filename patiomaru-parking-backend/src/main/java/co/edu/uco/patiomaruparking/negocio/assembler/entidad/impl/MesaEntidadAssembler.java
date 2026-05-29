package co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl;

import co.edu.uco.patiomaruparking.entidad.MesaEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.EntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.MesaDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class MesaEntidadAssembler implements EntidadAssembler<MesaDominio, MesaEntidad> {

    private static final MesaEntidadAssembler INSTANCE = new MesaEntidadAssembler();

    private MesaEntidadAssembler() {
        super();
    }

    public static MesaEntidadAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public MesaEntidad ensamblarEntidad(final MesaDominio dominio) {
        var mesaEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                MesaDominio.builder().build());

        return MesaEntidad.builder()
                .codigoMesa(mesaEnsamblar.getCodigoMesa())
                .nombre(mesaEnsamblar.getNombre())
                .build();
    }

    @Override
    public MesaDominio ensamblarDominio(final MesaEntidad entidad) {
        var mesaEnsamblar = UtilObjeto.obtenerValorDefecto(
                entidad,
                MesaEntidad.builder().build());

        return MesaDominio.builder()
                .codigoMesa(mesaEnsamblar.getCodigoMesa())
                .nombre(mesaEnsamblar.getNombre())
                .build();
    }
}