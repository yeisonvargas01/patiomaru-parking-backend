package co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl;

import co.edu.uco.patiomaruparking.entidad.ClienteEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.EntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.ClienteDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class ClienteEntidadAssembler implements EntidadAssembler<ClienteDominio, ClienteEntidad> {

    private static final ClienteEntidadAssembler INSTANCE = new ClienteEntidadAssembler();

    private ClienteEntidadAssembler() {
        super();
    }

    public static ClienteEntidadAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public ClienteEntidad ensamblarEntidad(final ClienteDominio dominio) {
        var clienteEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                ClienteDominio.builder().build());

        return ClienteEntidad.builder()
                .codigoCliente(clienteEnsamblar.getCodigoCliente())
                .nombre(clienteEnsamblar.getNombre())
                .telefono(clienteEnsamblar.getTelefono())
                .correoElectronico(clienteEnsamblar.getCorreoElectronico())
                .estado(clienteEnsamblar.getEstado())
                .build();
    }

    @Override
    public ClienteDominio ensamblarDominio(final ClienteEntidad entidad) {
        var clienteEnsamblar = UtilObjeto.obtenerValorDefecto(
                entidad,
                ClienteEntidad.builder().build());

        return ClienteDominio.builder()
                .codigoCliente(clienteEnsamblar.getCodigoCliente())
                .nombre(clienteEnsamblar.getNombre())
                .telefono(clienteEnsamblar.getTelefono())
                .correoElectronico(clienteEnsamblar.getCorreoElectronico())
                .estado(clienteEnsamblar.getEstado())
                .build();
    }
}