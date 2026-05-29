package co.edu.uco.patiomaruparking.negocio.assembler.dto.impl;

import co.edu.uco.patiomaruparking.dto.CategoriaDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.DTOAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.CategoriaDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class CategoriaDTOAssembler implements DTOAssembler<CategoriaDominio, CategoriaDTO> {
	
	private static final CategoriaDTOAssembler INSTANCE = new CategoriaDTOAssembler();

    private CategoriaDTOAssembler() {
        super();
    }

    public static CategoriaDTOAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public CategoriaDTO ensamblarDTO(final CategoriaDominio dominio) {
        var categoriaEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                CategoriaDominio.builder().build());

        return CategoriaDTO.builder()
                .codigoCategoria(categoriaEnsamblar.getCodigoCategoria())
                .nombre(categoriaEnsamblar.getNombre())
                .build();
    }

    @Override
    public CategoriaDominio ensamblarDominio(final CategoriaDTO dto) {
        var categoriaEnsamblar = UtilObjeto.obtenerValorDefecto(
                dto,
                CategoriaDTO.builder().build());

        return CategoriaDominio.builder()
                .codigoCategoria(categoriaEnsamblar.getCodigoCategoria())
                .nombre(categoriaEnsamblar.getNombre())
                .build();
    }
}