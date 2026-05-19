package co.edu.uco.patiomaruparking.negocio.assembler.dto.impl;

import co.edu.uco.patiomaruparking.dto.MesaDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.DTOAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.MesaDominio;

public final class MesaDTOAssembler implements DTOAssembler<MesaDominio, MesaDTO> {

	private static MesaDTOAssembler INSTANCE = null;

	private MesaDTOAssembler() {
		super();
	}

	public static synchronized MesaDTOAssembler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MesaDTOAssembler();
		}

		return INSTANCE;
	}

	@Override
	public MesaDTO ensamblarDTO(final MesaDominio dominio) {
		var mesaEnsamblar = dominio == null ? MesaDominio.builder().build() : dominio;

		return MesaDTO.builder()
				.codigoMesa(mesaEnsamblar.getCodigoMesa())
				.nombre(mesaEnsamblar.getNombre())
				.build();
	}

	@Override
	public MesaDominio ensamblarDominio(final MesaDTO dto) {
		var mesaEnsamblar = dto == null ? MesaDTO.builder().build() : dto;

		return MesaDominio.builder()
				.codigoMesa(mesaEnsamblar.getCodigoMesa())
				.nombre(mesaEnsamblar.getNombre())
				.build();
	}
}
