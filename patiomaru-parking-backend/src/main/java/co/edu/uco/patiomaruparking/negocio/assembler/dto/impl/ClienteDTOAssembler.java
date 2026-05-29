package co.edu.uco.patiomaruparking.negocio.assembler.dto.impl;

import co.edu.uco.patiomaruparking.dto.ClienteDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.DTOAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.ClienteDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class ClienteDTOAssembler implements DTOAssembler<ClienteDominio, ClienteDTO> {
	
	private static final ClienteDTOAssembler INSTANCE = new ClienteDTOAssembler();

    private ClienteDTOAssembler() {
        super();
    }

    public static ClienteDTOAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public ClienteDTO ensamblarDTO(final ClienteDominio dominio) {
        var clienteEnsamblar = UtilObjeto.obtenerValorDefecto(
                dominio,
                ClienteDominio.builder().build());

        return ClienteDTO.builder()
                .codigoCliente(clienteEnsamblar.getCodigoCliente())
                .nombre(clienteEnsamblar.getNombre())
                .telefono(clienteEnsamblar.getTelefono())
                .correoElectronico(clienteEnsamblar.getCorreoElectronico())
                .estado(clienteEnsamblar.getEstado())
                .build();
    }

    @Override
    public ClienteDominio ensamblarDominio(final ClienteDTO dto) {
        var clienteEnsamblar = UtilObjeto.obtenerValorDefecto(
                dto,
                ClienteDTO.builder().build());

        return ClienteDominio.builder()
                .codigoCliente(clienteEnsamblar.getCodigoCliente())
                .nombre(clienteEnsamblar.getNombre())
                .telefono(clienteEnsamblar.getTelefono())
                .correoElectronico(clienteEnsamblar.getCorreoElectronico())
                .estado(clienteEnsamblar.getEstado())
                .build();
    }
}