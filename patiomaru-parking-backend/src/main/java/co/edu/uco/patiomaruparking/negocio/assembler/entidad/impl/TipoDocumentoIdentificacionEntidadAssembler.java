package co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl;

import co.edu.uco.patiomaruparking.entidad.TipoDocumentoIdentificacionEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.EntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.TipoDocumentoIdentificacionDominio;

public final class TipoDocumentoIdentificacionEntidadAssembler
		implements EntidadAssembler<TipoDocumentoIdentificacionDominio, TipoDocumentoIdentificacionEntidad> {

	private static TipoDocumentoIdentificacionEntidadAssembler INSTANCE = null;

	private TipoDocumentoIdentificacionEntidadAssembler() {
		super();
	}

	public static synchronized TipoDocumentoIdentificacionEntidadAssembler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TipoDocumentoIdentificacionEntidadAssembler();
		}

		return INSTANCE;
	}

	@Override
	public TipoDocumentoIdentificacionEntidad ensamblarEntidad(final TipoDocumentoIdentificacionDominio dominio) {
		var tipoDocumentoEnsamblar = dominio == null
				? TipoDocumentoIdentificacionDominio.builder().build()
				: dominio;

		return TipoDocumentoIdentificacionEntidad.builder()
				.codigoTipoDocumentoIdentificacion(tipoDocumentoEnsamblar.getCodigoTipoDocumentoIdentificacion())
				.nombre(tipoDocumentoEnsamblar.getNombre())
				.build();
	}

	@Override
	public TipoDocumentoIdentificacionDominio ensamblarDominio(final TipoDocumentoIdentificacionEntidad entidad) {
		var tipoDocumentoEnsamblar = entidad == null
				? TipoDocumentoIdentificacionEntidad.builder().build()
				: entidad;

		return TipoDocumentoIdentificacionDominio.builder()
				.codigoTipoDocumentoIdentificacion(tipoDocumentoEnsamblar.getCodigoTipoDocumentoIdentificacion())
				.nombre(tipoDocumentoEnsamblar.getNombre())
				.build();
	}
}