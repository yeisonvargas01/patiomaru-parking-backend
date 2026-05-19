package co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl;

import co.edu.uco.patiomaruparking.entidad.CiudadResidenciaEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.EntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.CiudadResidenciaDominio;

public final class CiudadResidenciaEntidadAssembler
		implements EntidadAssembler<CiudadResidenciaDominio, CiudadResidenciaEntidad> {

	private static CiudadResidenciaEntidadAssembler INSTANCE = null;

	private CiudadResidenciaEntidadAssembler() {
		super();
	}

	public static synchronized CiudadResidenciaEntidadAssembler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CiudadResidenciaEntidadAssembler();
		}

		return INSTANCE;
	}

	@Override
	public CiudadResidenciaEntidad ensamblarEntidad(final CiudadResidenciaDominio dominio) {
		var ciudadResidenciaEnsamblar = dominio == null
				? CiudadResidenciaDominio.builder().build()
				: dominio;

		return CiudadResidenciaEntidad.builder()
				.codigoCiudadResidencia(ciudadResidenciaEnsamblar.getCodigoCiudadResidencia())
				.nombre(ciudadResidenciaEnsamblar.getNombre())
				.build();
	}

	@Override
	public CiudadResidenciaDominio ensamblarDominio(final CiudadResidenciaEntidad entidad) {
		var ciudadResidenciaEnsamblar = entidad == null
				? CiudadResidenciaEntidad.builder().build()
				: entidad;

		return CiudadResidenciaDominio.builder()
				.codigoCiudadResidencia(ciudadResidenciaEnsamblar.getCodigoCiudadResidencia())
				.nombre(ciudadResidenciaEnsamblar.getNombre())
				.build();
	}
}
