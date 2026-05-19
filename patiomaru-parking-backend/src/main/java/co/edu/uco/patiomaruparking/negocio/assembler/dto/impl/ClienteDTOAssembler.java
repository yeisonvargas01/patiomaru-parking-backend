package co.edu.uco.patiomaruparking.negocio.assembler.dto.impl;

import co.edu.uco.patiomaruparking.dto.ClienteDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.DTOAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.ClienteDominio;

public final class ClienteDTOAssembler implements DTOAssembler<ClienteDominio, ClienteDTO> {

	private static ClienteDTOAssembler INSTANCE = null;

	private ClienteDTOAssembler() {
		super();
	}

	public static synchronized ClienteDTOAssembler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ClienteDTOAssembler();
		}

		return INSTANCE;
	}

	@Override
	public ClienteDTO ensamblarDTO(final ClienteDominio dominio) {
		var clienteEnsamblar = dominio == null ? ClienteDominio.builder().build() : dominio;

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
		var clienteEnsamblar = dto == null ? ClienteDTO.builder().build() : dto;

		return ClienteDominio.builder()
				.codigoCliente(clienteEnsamblar.getCodigoCliente())
				.nombre(clienteEnsamblar.getNombre())
				.telefono(clienteEnsamblar.getTelefono())
				.correoElectronico(clienteEnsamblar.getCorreoElectronico())
				.estado(clienteEnsamblar.getEstado())
				.build();
	}
}
