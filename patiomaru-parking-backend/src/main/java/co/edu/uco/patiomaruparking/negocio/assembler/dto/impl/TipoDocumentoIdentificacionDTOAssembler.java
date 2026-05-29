package co.edu.uco.patiomaruparking.negocio.assembler.dto.impl;

import co.edu.uco.patiomaruparking.dto.TipoDocumentoIdentificacionDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.DTOAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.TipoDocumentoIdentificacionDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class TipoDocumentoIdentificacionDTOAssembler
        implements DTOAssembler<TipoDocumentoIdentificacionDominio, TipoDocumentoIdentificacionDTO> {
	
	private static final TipoDocumentoIdentificacionDTOAssembler INSTANCE = new TipoDocumentoIdentificacionDTOAssembler();

    private TipoDocumentoIdentificacionDTOAssembler() {
        super();
    }

    public static TipoDocumentoIdentificacionDTOAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public TipoDocumentoIdentificacionDTO ensamblarDTO(final TipoDocumentoIdentificacionDominio dominio) {
        var tipoDocumentoEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                TipoDocumentoIdentificacionDominio.builder().build());

        return TipoDocumentoIdentificacionDTO.builder()
                .codigoTipoDocumentoIdentificacion(tipoDocumentoEnsamblar.getCodigoTipoDocumentoIdentificacion())
                .nombre(tipoDocumentoEnsamblar.getNombre())
                .build();
    }

    @Override
    public TipoDocumentoIdentificacionDominio ensamblarDominio(final TipoDocumentoIdentificacionDTO dto) {
        var tipoDocumentoEnsamblar = UtilObjeto.obtenerValorDefecto(
                dto,
                TipoDocumentoIdentificacionDTO.builder().build());

        return TipoDocumentoIdentificacionDominio.builder()
                .codigoTipoDocumentoIdentificacion(tipoDocumentoEnsamblar.getCodigoTipoDocumentoIdentificacion())
                .nombre(tipoDocumentoEnsamblar.getNombre())
                .build();
    }
}
