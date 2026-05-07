package co.edu.uco.patiomaruparking.negocio.dominio;

import java.util.UUID;

import co.edu.uco.patiomaruparking.transversal.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.UtilUUID;

public class ClienteDominio {

	private UUID id;
	private String nombre;
	private String telefono;
	private String correoElectronico;
	private boolean estado;

	private ClienteDominio(final Builder builder) {
		setId(builder.id);
		setNombre(builder.nombre);
		setTelefono(builder.telefono);
		setCorreoElectronico(builder.correoElectronico);
		setEstado(builder.estado);
	}

	public UUID getId() {
		return id;
	}

	private void setId(final UUID id) {
		this.id = UtilUUID.obtenerValorDefecto(id);
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(final String nombre) {
		this.nombre = UtilTexto.aplicarTrim(nombre);
	}

	public String getTelefono() {
		return telefono;
	}

	private void setTelefono(final String telefono) {
		this.telefono = UtilTexto.aplicarTrim(telefono);
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	private void setCorreoElectronico(final String correoElectronico) {
		this.correoElectronico = UtilTexto.aplicarTrim(correoElectronico);
	}

	public boolean isEstado() {
		return estado;
	}

	private void setEstado(final boolean estado) {
		this.estado = estado;
	}

	public static class Builder {

		private UUID id;
		private String nombre;
		private String telefono;
		private String correoElectronico;
		private boolean estado;

		public Builder id(final UUID id) {
			this.id = id;
			return this;
		}

		public Builder nombre(final String nombre) {
			this.nombre = UtilTexto.aplicarTrim(nombre);
			return this;
		}

		public Builder telefono(final String telefono) {
			this.telefono = UtilTexto.aplicarTrim(telefono);
			return this;
		}

		public Builder correoElectronico(final String correoElectronico) {
			this.correoElectronico = UtilTexto.aplicarTrim(correoElectronico);
			return this;
		}

		public Builder estado(final boolean estado) {
			this.estado = estado;
			return this;
		}

		public ClienteDominio build() {
			return new ClienteDominio(this);
		}
	}
}
