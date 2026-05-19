package co.edu.uco.patiomaruparking.negocio.assembler.dto.impl;

import co.edu.uco.patiomaruparking.dto.PlatoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.DTOAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.PlatoDominio;

public final class PlatoDTOAssembler implements DTOAssembler<PlatoDominio, PlatoDTO> {

	private static PlatoDTOAssembler INSTANCE = null;

	private PlatoDTOAssembler() {
		super();
	}

	public static synchronized PlatoDTOAssembler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PlatoDTOAssembler();
		}

		return INSTANCE;
	}

	@Override
	public PlatoDTO ensamblarDTO(final PlatoDominio dominio) {
		var platoEnsamblar = dominio == null ? PlatoDominio.builder().build() : dominio;

		return PlatoDTO.builder()
				.codigoPlato(platoEnsamblar.getCodigoPlato())
				.nombre(platoEnsamblar.getNombre())
				.categoria(CategoriaDTOAssembler.getInstance().ensamblarDTO(platoEnsamblar.getCategoria()))
				.precioVenta(platoEnsamblar.getPrecioVenta())
				.estado(platoEnsamblar.getEstado())
				.build();
	}

	@Override
	public PlatoDominio ensamblarDominio(final PlatoDTO dto) {
		var platoEnsamblar = dto == null ? PlatoDTO.builder().build() : dto;

		return PlatoDominio.builder()
				.codigoPlato(platoEnsamblar.getCodigoPlato())
				.nombre(platoEnsamblar.getNombre())
				.categoria(CategoriaDTOAssembler.getInstance().ensamblarDominio(platoEnsamblar.getCategoria()))
				.precioVenta(platoEnsamblar.getPrecioVenta())
				.estado(platoEnsamblar.getEstado())
				.build();
	}
}
