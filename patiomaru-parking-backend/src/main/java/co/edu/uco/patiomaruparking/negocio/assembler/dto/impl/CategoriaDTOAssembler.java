package co.edu.uco.patiomaruparking.negocio.assembler.dto.impl;

import co.edu.uco.patiomaruparking.dto.CategoriaDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.DTOAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.CategoriaDominio;

public final class CategoriaDTOAssembler implements DTOAssembler<CategoriaDominio, CategoriaDTO> {

	private static CategoriaDTOAssembler INSTANCE = null;

	private CategoriaDTOAssembler() {
		super();
	}

	public static synchronized CategoriaDTOAssembler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CategoriaDTOAssembler();
		}

		return INSTANCE;
	}

	@Override
	public CategoriaDTO ensamblarDTO(final CategoriaDominio dominio) {
		var categoriaEnsamblar = dominio == null ? CategoriaDominio.builder().build() : dominio;

		return CategoriaDTO.builder()
				.codigoCategoria(categoriaEnsamblar.getCodigoCategoria())
				.nombre(categoriaEnsamblar.getNombre())
				.build();
	}

	@Override
	public CategoriaDominio ensamblarDominio(final CategoriaDTO dto) {
		var categoriaEnsamblar = dto == null ? CategoriaDTO.builder().build() : dto;

		return CategoriaDominio.builder()
				.codigoCategoria(categoriaEnsamblar.getCodigoCategoria())
				.nombre(categoriaEnsamblar.getNombre())
				.build();
	}
}
