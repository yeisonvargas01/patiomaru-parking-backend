package co.edu.uco.patiomaruparking.negocio.assembler.dto.impl;

import co.edu.uco.patiomaruparking.dto.CargoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.DTOAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.CargoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class CargoDTOAssembler implements DTOAssembler<CargoDominio, CargoDTO> {
	
	private static final CargoDTOAssembler INSTANCE = new CargoDTOAssembler();

    private CargoDTOAssembler() {
        super();
    }

    public static CargoDTOAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public CargoDTO ensamblarDTO(final CargoDominio dominio) {
        var cargoEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                CargoDominio.builder().build());

        return CargoDTO.builder()
                .codigoCargo(cargoEnsamblar.getCodigoCargo())
                .nombre(cargoEnsamblar.getNombre())
                .activo(cargoEnsamblar.getActivo())
                .build();
    }

    @Override
    public CargoDominio ensamblarDominio(final CargoDTO dto) {
        var cargoEnsamblar = UtilObjeto.obtenerValorDefecto(
                dto,
                CargoDTO.builder().build());

        return CargoDominio.builder()
                .codigoCargo(cargoEnsamblar.getCodigoCargo())
                .nombre(cargoEnsamblar.getNombre())
                .activo(cargoEnsamblar.getActivo())
                .build();
    }
}