package co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl;

import co.edu.uco.patiomaruparking.entidad.MesaEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.EntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.MesaDominio;

public final class MesaEntidadAssembler implements EntidadAssembler<MesaDominio, MesaEntidad> {

	private static MesaEntidadAssembler INSTANCE = null;

	private MesaEntidadAssembler() {
		super();
	}

	public static synchronized MesaEntidadAssembler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MesaEntidadAssembler();
		}

		return INSTANCE;
	}

	@Override
	public MesaEntidad ensamblarEntidad(final MesaDominio dominio) {
		var mesaEnsamblar = dominio == null ? MesaDominio.builder().build() : dominio;

		return MesaEntidad.builder()
				.codigoMesa(mesaEnsamblar.getCodigoMesa())
				.nombre(mesaEnsamblar.getNombre())
				.build();
	}

	@Override
	public MesaDominio ensamblarDominio(final MesaEntidad entidad) {
		var mesaEnsamblar = entidad == null ? MesaEntidad.builder().build() : entidad;

		return MesaDominio.builder()
				.codigoMesa(mesaEnsamblar.getCodigoMesa())
				.nombre(mesaEnsamblar.getNombre())
				.build();
	}
}
