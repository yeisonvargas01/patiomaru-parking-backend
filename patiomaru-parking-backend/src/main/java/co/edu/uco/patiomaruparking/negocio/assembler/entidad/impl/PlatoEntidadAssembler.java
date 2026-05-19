package co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl;

import co.edu.uco.patiomaruparking.entidad.PlatoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.EntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.PlatoDominio;

public final class PlatoEntidadAssembler implements EntidadAssembler<PlatoDominio, PlatoEntidad> {

	private static PlatoEntidadAssembler INSTANCE = null;

	private PlatoEntidadAssembler() {
		super();
	}

	public static synchronized PlatoEntidadAssembler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PlatoEntidadAssembler();
		}

		return INSTANCE;
	}

	@Override
	public PlatoEntidad ensamblarEntidad(final PlatoDominio dominio) {
		var platoEnsamblar = dominio == null ? PlatoDominio.builder().build() : dominio;

		return PlatoEntidad.builder()
				.codigoPlato(platoEnsamblar.getCodigoPlato())
				.nombre(platoEnsamblar.getNombre())
				.categoria(CategoriaEntidadAssembler.getInstance()
						.ensamblarEntidad(platoEnsamblar.getCategoria()))
				.precioVenta(platoEnsamblar.getPrecioVenta())
				.estado(platoEnsamblar.getEstado())
				.build();
	}

	@Override
	public PlatoDominio ensamblarDominio(final PlatoEntidad entidad) {
		var platoEnsamblar = entidad == null ? PlatoEntidad.builder().build() : entidad;

		return PlatoDominio.builder()
				.codigoPlato(platoEnsamblar.getCodigoPlato())
				.nombre(platoEnsamblar.getNombre())
				.categoria(CategoriaEntidadAssembler.getInstance()
						.ensamblarDominio(platoEnsamblar.getCategoria()))
				.precioVenta(platoEnsamblar.getPrecioVenta())
				.estado(platoEnsamblar.getEstado())
				.build();
	}
}
