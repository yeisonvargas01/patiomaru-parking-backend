package co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl;

import co.edu.uco.patiomaruparking.entidad.CategoriaEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.EntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.CategoriaDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class CategoriaEntidadAssembler implements EntidadAssembler<CategoriaDominio, CategoriaEntidad> {

    private static final CategoriaEntidadAssembler INSTANCE = new CategoriaEntidadAssembler();

    private CategoriaEntidadAssembler() {
        super();
    }

    public static CategoriaEntidadAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public CategoriaEntidad ensamblarEntidad(final CategoriaDominio dominio) {
        var categoriaEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                CategoriaDominio.builder().build());

        return CategoriaEntidad.builder()
                .codigoCategoria(categoriaEnsamblar.getCodigoCategoria())
                .nombre(categoriaEnsamblar.getNombre())
                .build();
    }

    @Override
    public CategoriaDominio ensamblarDominio(final CategoriaEntidad entidad) {
        var categoriaEnsamblar = UtilObjeto.obtenerValorDefecto(
                entidad,
                CategoriaEntidad.builder().build());

        return CategoriaDominio.builder()
                .codigoCategoria(categoriaEnsamblar.getCodigoCategoria())
                .nombre(categoriaEnsamblar.getNombre())
                .build();
    }
}