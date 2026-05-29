package co.edu.uco.patiomaruparking.negocio.assembler.dto.impl;

import co.edu.uco.patiomaruparking.dto.MesaDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.DTOAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.MesaDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class MesaDTOAssembler implements DTOAssembler<MesaDominio, MesaDTO> {

    private static final MesaDTOAssembler INSTANCE = new MesaDTOAssembler();

    private MesaDTOAssembler() {
        super();
    }

    public static MesaDTOAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public MesaDTO ensamblarDTO(final MesaDominio dominio) {
        var mesaEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                MesaDominio.builder().build());

        return MesaDTO.builder()
                .codigoMesa(mesaEnsamblar.getCodigoMesa())
                .nombre(mesaEnsamblar.getNombre())
                .build();
    }

    @Override
    public MesaDominio ensamblarDominio(final MesaDTO dto) {
        var mesaEnsamblar = UtilObjeto.obtenerValorDefecto(
                dto,
                MesaDTO.builder().build());

        return MesaDominio.builder()
                .codigoMesa(mesaEnsamblar.getCodigoMesa())
                .nombre(mesaEnsamblar.getNombre())
                .build();
    }
}
