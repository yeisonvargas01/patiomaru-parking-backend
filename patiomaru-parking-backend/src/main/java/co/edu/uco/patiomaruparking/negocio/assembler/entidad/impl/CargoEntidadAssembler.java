package co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl;

import co.edu.uco.patiomaruparking.entidad.CargoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.EntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.CargoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class CargoEntidadAssembler implements EntidadAssembler<CargoDominio, CargoEntidad> {

    private static final CargoEntidadAssembler INSTANCE = new CargoEntidadAssembler();

    private CargoEntidadAssembler() {
        super();
    }

    public static CargoEntidadAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public CargoEntidad ensamblarEntidad(final CargoDominio dominio) {
        var cargoEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                CargoDominio.builder().build());

        return CargoEntidad.builder()
                .codigoCargo(cargoEnsamblar.getCodigoCargo())
                .nombre(cargoEnsamblar.getNombre())
                .activo(cargoEnsamblar.getActivo())
                .build();
    }

    @Override
    public CargoDominio ensamblarDominio(final CargoEntidad entidad) {
        var cargoEnsamblar = UtilObjeto.obtenerValorDefecto(
                entidad,
                CargoEntidad.builder().build());

        return CargoDominio.builder()
                .codigoCargo(cargoEnsamblar.getCodigoCargo())
                .nombre(cargoEnsamblar.getNombre())
                .activo(cargoEnsamblar.getActivo())
                .build();
    }
}