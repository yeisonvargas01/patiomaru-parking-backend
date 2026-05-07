package co.edu.uco.patiomaruparking.negocio.dominio;

import java.util.UUID;

import co.edu.uco.patiomaruparking.transversal.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.UtilUUID;

public class MesaDominio {

	private UUID id;
	private String nombre;

	private MesaDominio(final Builder builder) {
		setId(builder.id);
		setNombre(builder.nombre);
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

	public static class Builder {

		private UUID id;
		private String nombre;

		public Builder id(final UUID id) {
			this.id = id;
			return this;
		}

		public Builder nombre(final String nombre) {
			this.nombre = UtilTexto.aplicarTrim(nombre);
			return this;
		}

		public MesaDominio build() {
			return new MesaDominio(this);
		}
	}
}
