package co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl;

import co.edu.uco.patiomaruparking.entidad.CargoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.EntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.CargoDominio;

public final class CargoEntidadAssembler implements EntidadAssembler<CargoDominio, CargoEntidad> {

	private static CargoEntidadAssembler INSTANCE = null;

	private CargoEntidadAssembler() {
		super();
	}

	public static synchronized CargoEntidadAssembler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CargoEntidadAssembler();
		}

		return INSTANCE;
	}

	@Override
	public CargoEntidad ensamblarEntidad(final CargoDominio dominio) {
		var cargoEnsamblar = dominio == null ? CargoDominio.builder().build() : dominio;

		return CargoEntidad.builder()
				.codigoCargo(cargoEnsamblar.getCodigoCargo())
				.nombre(cargoEnsamblar.getNombre())
				.estado(cargoEnsamblar.getEstado())
				.build();
	}

	@Override
	public CargoDominio ensamblarDominio(final CargoEntidad entidad) {
		var cargoEnsamblar = entidad == null ? CargoEntidad.builder().build() : entidad;

		return CargoDominio.builder()
				.codigoCargo(cargoEnsamblar.getCodigoCargo())
				.nombre(cargoEnsamblar.getNombre())
				.estado(cargoEnsamblar.getEstado())
				.build();
	}
}
