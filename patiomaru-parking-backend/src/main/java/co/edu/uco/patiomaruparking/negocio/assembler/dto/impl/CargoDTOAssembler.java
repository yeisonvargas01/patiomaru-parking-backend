package co.edu.uco.patiomaruparking.negocio.assembler.dto.impl;

import co.edu.uco.patiomaruparking.dto.CargoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.DTOAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.CargoDominio;

public final class CargoDTOAssembler implements DTOAssembler<CargoDominio, CargoDTO> {

	private static CargoDTOAssembler INSTANCE = null;

	private CargoDTOAssembler() {
		super();
	}

	public static synchronized CargoDTOAssembler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CargoDTOAssembler();
		}

		return INSTANCE;
	}

	@Override
	public CargoDTO ensamblarDTO(final CargoDominio dominio) {
		var cargoEnsamblar = dominio == null ? CargoDominio.builder().build() : dominio;

		return CargoDTO.builder()
				.codigoCargo(cargoEnsamblar.getCodigoCargo())
				.nombre(cargoEnsamblar.getNombre())
				.estado(cargoEnsamblar.getEstado())
				.build();
	}

	@Override
	public CargoDominio ensamblarDominio(final CargoDTO dto) {
		var cargoEnsamblar = dto == null ? CargoDTO.builder().build() : dto;

		return CargoDominio.builder()
				.codigoCargo(cargoEnsamblar.getCodigoCargo())
				.nombre(cargoEnsamblar.getNombre())
				.estado(cargoEnsamblar.getEstado())
				.build();
	}
}
