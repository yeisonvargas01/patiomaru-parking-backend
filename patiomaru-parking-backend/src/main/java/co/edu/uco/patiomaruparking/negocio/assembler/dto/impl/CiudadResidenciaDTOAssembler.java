package co.edu.uco.patiomaruparking.negocio.assembler.dto.impl;

import co.edu.uco.patiomaruparking.dto.CiudadResidenciaDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.DTOAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.CiudadResidenciaDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class CiudadResidenciaDTOAssembler implements DTOAssembler<CiudadResidenciaDominio, CiudadResidenciaDTO> {
	
	private static final CiudadResidenciaDTOAssembler INSTANCE = new CiudadResidenciaDTOAssembler();

    private CiudadResidenciaDTOAssembler() {
        super();
    }

    public static CiudadResidenciaDTOAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public CiudadResidenciaDTO ensamblarDTO(final CiudadResidenciaDominio dominio) {
        var ciudadResidenciaEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                CiudadResidenciaDominio.builder().build());

        return CiudadResidenciaDTO.builder()
                .codigoCiudadResidencia(ciudadResidenciaEnsamblar.getCodigoCiudadResidencia())
                .nombre(ciudadResidenciaEnsamblar.getNombre())
                .build();
    }

    @Override
    public CiudadResidenciaDominio ensamblarDominio(final CiudadResidenciaDTO dto) {
        var ciudadResidenciaEnsamblar = UtilObjeto.obtenerValorDefecto(
                dto,
                CiudadResidenciaDTO.builder().build());

        return CiudadResidenciaDominio.builder()
                .codigoCiudadResidencia(ciudadResidenciaEnsamblar.getCodigoCiudadResidencia())
                .nombre(ciudadResidenciaEnsamblar.getNombre())
                .build();
    }
}