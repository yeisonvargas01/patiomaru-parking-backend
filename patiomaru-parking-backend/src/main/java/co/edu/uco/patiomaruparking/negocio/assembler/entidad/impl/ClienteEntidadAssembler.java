package co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl;

import co.edu.uco.patiomaruparking.entidad.ClienteEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.EntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.ClienteDominio;

public final class ClienteEntidadAssembler implements EntidadAssembler<ClienteDominio, ClienteEntidad> {

	private static ClienteEntidadAssembler INSTANCE = null;

	private ClienteEntidadAssembler() {
		super();
	}

	public static synchronized ClienteEntidadAssembler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ClienteEntidadAssembler();
		}

		return INSTANCE;
	}

	@Override
	public ClienteEntidad ensamblarEntidad(final ClienteDominio dominio) {
		var clienteEnsamblar = dominio == null ? ClienteDominio.builder().build() : dominio;

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
		var clienteEnsamblar = entidad == null ? ClienteEntidad.builder().build() : entidad;

		return ClienteDominio.builder()
				.codigoCliente(clienteEnsamblar.getCodigoCliente())
				.nombre(clienteEnsamblar.getNombre())
				.telefono(clienteEnsamblar.getTelefono())
				.correoElectronico(clienteEnsamblar.getCorreoElectronico())
				.estado(clienteEnsamblar.getEstado())
				.build();
	}
}