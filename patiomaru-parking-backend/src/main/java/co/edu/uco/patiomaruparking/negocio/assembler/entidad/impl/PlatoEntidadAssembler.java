package co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl;

import co.edu.uco.patiomaruparking.entidad.PlatoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.EntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.PlatoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class PlatoEntidadAssembler implements EntidadAssembler<PlatoDominio, PlatoEntidad> {

    private static final PlatoEntidadAssembler INSTANCE = new PlatoEntidadAssembler();

    private PlatoEntidadAssembler() {
        super();
    }

    public static PlatoEntidadAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public PlatoEntidad ensamblarEntidad(final PlatoDominio dominio) {
        var platoEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                PlatoDominio.builder().build());

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
        var platoEnsamblar = UtilObjeto.obtenerValorDefecto(
                entidad,
                PlatoEntidad.builder().build());

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