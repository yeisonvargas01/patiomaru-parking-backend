package co.edu.uco.patiomaruparking.negocio.assembler.dto.impl;

import co.edu.uco.patiomaruparking.dto.TipoDocumentoIdentificacionDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.DTOAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.TipoDocumentoIdentificacionDominio;

public final class TipoDocumentoIdentificacionDTOAssembler
		implements DTOAssembler<TipoDocumentoIdentificacionDominio, TipoDocumentoIdentificacionDTO> {

	private static TipoDocumentoIdentificacionDTOAssembler INSTANCE = null;

	private TipoDocumentoIdentificacionDTOAssembler() {
		super();
	}

	public static synchronized TipoDocumentoIdentificacionDTOAssembler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TipoDocumentoIdentificacionDTOAssembler();
		}

		return INSTANCE;
	}

	@Override
	public TipoDocumentoIdentificacionDTO ensamblarDTO(final TipoDocumentoIdentificacionDominio dominio) {
		var tipoDocumentoEnsamblar = dominio == null
				? TipoDocumentoIdentificacionDominio.builder().build()
				: dominio;

		return TipoDocumentoIdentificacionDTO.builder()
				.codigoTipoDocumentoIdentificacion(tipoDocumentoEnsamblar.getCodigoTipoDocumentoIdentificacion())
				.nombre(tipoDocumentoEnsamblar.getNombre())
				.build();
	}

	@Override
	public TipoDocumentoIdentificacionDominio ensamblarDominio(final TipoDocumentoIdentificacionDTO dto) {
		var tipoDocumentoEnsamblar = dto == null
				? TipoDocumentoIdentificacionDTO.builder().build()
				: dto;

		return TipoDocumentoIdentificacionDominio.builder()
				.codigoTipoDocumentoIdentificacion(tipoDocumentoEnsamblar.getCodigoTipoDocumentoIdentificacion())
				.nombre(tipoDocumentoEnsamblar.getNombre())
				.build();
	}
}
