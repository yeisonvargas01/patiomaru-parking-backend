package co.edu.uco.patiomaruparking.negocio.assembler.dto.impl;

import co.edu.uco.patiomaruparking.dto.PlatoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.DTOAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.PlatoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class PlatoDTOAssembler implements DTOAssembler<PlatoDominio, PlatoDTO> {
	
	private static final PlatoDTOAssembler INSTANCE = new PlatoDTOAssembler();

    private PlatoDTOAssembler() {
        super();
    }

    public static PlatoDTOAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public PlatoDTO ensamblarDTO(final PlatoDominio dominio) {
        var platoEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                PlatoDominio.builder().build());

        return PlatoDTO.builder()
                .codigoPlato(platoEnsamblar.getCodigoPlato())
                .nombre(platoEnsamblar.getNombre())
                .categoria(CategoriaDTOAssembler.getInstance()
                        .ensamblarDTO(platoEnsamblar.getCategoria()))
                .precioVenta(platoEnsamblar.getPrecioVenta())
                .estado(platoEnsamblar.getEstado())
                .build();
    }

    @Override
    public PlatoDominio ensamblarDominio(final PlatoDTO dto) {
        var platoEnsamblar = UtilObjeto.obtenerValorDefecto(
                dto,
                PlatoDTO.builder().build());

        return PlatoDominio.builder()
                .codigoPlato(platoEnsamblar.getCodigoPlato())
                .nombre(platoEnsamblar.getNombre())
                .categoria(CategoriaDTOAssembler.getInstance()
                        .ensamblarDominio(platoEnsamblar.getCategoria()))
                .precioVenta(platoEnsamblar.getPrecioVenta())
                .estado(platoEnsamblar.getEstado())
                .build();
    }
}