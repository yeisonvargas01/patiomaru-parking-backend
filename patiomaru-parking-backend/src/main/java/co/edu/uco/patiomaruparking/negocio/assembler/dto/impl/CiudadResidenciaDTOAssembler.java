package co.edu.uco.patiomaruparking.negocio.assembler.dto.impl;

import co.edu.uco.patiomaruparking.dto.CiudadResidenciaDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.DTOAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.CiudadResidenciaDominio;

public final class CiudadResidenciaDTOAssembler implements DTOAssembler<CiudadResidenciaDominio, CiudadResidenciaDTO> {

	private static CiudadResidenciaDTOAssembler INSTANCE = null;

	private CiudadResidenciaDTOAssembler() {
		super();
	}

	public static synchronized CiudadResidenciaDTOAssembler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CiudadResidenciaDTOAssembler();
		}

		return INSTANCE;
	}

	@Override
	public CiudadResidenciaDTO ensamblarDTO(final CiudadResidenciaDominio dominio) {
		var ciudadResidenciaEnsamblar = dominio == null ? CiudadResidenciaDominio.builder().build() : dominio;

		return CiudadResidenciaDTO.builder()
				.codigoCiudadResidencia(ciudadResidenciaEnsamblar.getCodigoCiudadResidencia())
				.nombre(ciudadResidenciaEnsamblar.getNombre())
				.build();
	}

	@Override
	public CiudadResidenciaDominio ensamblarDominio(final CiudadResidenciaDTO dto) {
		var ciudadResidenciaEnsamblar = dto == null ? CiudadResidenciaDTO.builder().build() : dto;

		return CiudadResidenciaDominio.builder()
				.codigoCiudadResidencia(ciudadResidenciaEnsamblar.getCodigoCiudadResidencia())
				.nombre(ciudadResidenciaEnsamblar.getNombre())
				.build();
	}
}
