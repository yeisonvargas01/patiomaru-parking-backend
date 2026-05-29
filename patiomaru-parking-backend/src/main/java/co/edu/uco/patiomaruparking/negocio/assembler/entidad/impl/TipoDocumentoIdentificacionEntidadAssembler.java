package co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl;

import co.edu.uco.patiomaruparking.entidad.TipoDocumentoIdentificacionEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.EntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.TipoDocumentoIdentificacionDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class TipoDocumentoIdentificacionEntidadAssembler
        implements EntidadAssembler<TipoDocumentoIdentificacionDominio, TipoDocumentoIdentificacionEntidad> {

    private static final TipoDocumentoIdentificacionEntidadAssembler INSTANCE =
            new TipoDocumentoIdentificacionEntidadAssembler();

    private TipoDocumentoIdentificacionEntidadAssembler() {
        super();
    }

    public static TipoDocumentoIdentificacionEntidadAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public TipoDocumentoIdentificacionEntidad ensamblarEntidad(
            final TipoDocumentoIdentificacionDominio dominio) {

        var tipoDocumentoEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                TipoDocumentoIdentificacionDominio.builder().build());

        return TipoDocumentoIdentificacionEntidad.builder()
                .codigoTipoDocumentoIdentificacion(
                        tipoDocumentoEnsamblar.getCodigoTipoDocumentoIdentificacion())
                .nombre(tipoDocumentoEnsamblar.getNombre())
                .build();
    }

    @Override
    public TipoDocumentoIdentificacionDominio ensamblarDominio(
            final TipoDocumentoIdentificacionEntidad entidad) {

        var tipoDocumentoEnsamblar = UtilObjeto.obtenerValorDefecto(
                entidad,
                TipoDocumentoIdentificacionEntidad.builder().build());

        return TipoDocumentoIdentificacionDominio.builder()
                .codigoTipoDocumentoIdentificacion(
                        tipoDocumentoEnsamblar.getCodigoTipoDocumentoIdentificacion())
                .nombre(tipoDocumentoEnsamblar.getNombre())
                .build();
    }
}