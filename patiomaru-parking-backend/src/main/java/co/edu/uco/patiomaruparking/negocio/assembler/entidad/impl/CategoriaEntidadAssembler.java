package co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl;

import co.edu.uco.patiomaruparking.entidad.CategoriaEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.EntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.CategoriaDominio;

public final class CategoriaEntidadAssembler implements EntidadAssembler<CategoriaDominio, CategoriaEntidad> {

	private static CategoriaEntidadAssembler INSTANCE = null;

	private CategoriaEntidadAssembler() {
		super();
	}

	public static synchronized CategoriaEntidadAssembler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CategoriaEntidadAssembler();
		}

		return INSTANCE;
	}

	@Override
	public CategoriaEntidad ensamblarEntidad(final CategoriaDominio dominio) {
		var categoriaEnsamblar = dominio == null ? CategoriaDominio.builder().build() : dominio;

		return CategoriaEntidad.builder()
				.codigoCategoria(categoriaEnsamblar.getCodigoCategoria())
				.nombre(categoriaEnsamblar.getNombre())
				.build();
	}

	@Override
	public CategoriaDominio ensamblarDominio(final CategoriaEntidad entidad) {
		var categoriaEnsamblar = entidad == null ? CategoriaEntidad.builder().build() : entidad;

		return CategoriaDominio.builder()
				.codigoCategoria(categoriaEnsamblar.getCodigoCategoria())
				.nombre(categoriaEnsamblar.getNombre())
				.build();
	}
}
