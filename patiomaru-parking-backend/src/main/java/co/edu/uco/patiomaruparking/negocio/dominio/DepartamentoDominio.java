package co.edu.uco.patiomaruparking.negocio.dominio;

import java.util.UUID;

import co.edu.uco.patiomaruparking.transversal.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.UtilUUID;

public class DepartamentoDominio {

	private UUID id;
	private PaisDominio pais;
	private String nombre;

	private DepartamentoDominio(final Builder builder) {
		setId(builder.id);
		setPais(builder.pais);
		setNombre(builder.nombre);
	}

	public UUID getId() {
		return id;
	}

	private void setId(final UUID id) {
		this.id = UtilUUID.obtenerValorDefecto(id);
	}

	public PaisDominio getPais() {
		return pais;
	}

	private void setPais(final PaisDominio pais) {
		this.pais = UtilObjeto.obtenerValorDefecto(pais, new PaisDominio.Builder().build());
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(final String nombre) {
		this.nombre = UtilTexto.aplicarTrim(nombre);
	}

	public static class Builder {

		private UUID id;
		private PaisDominio pais;
		private String nombre;

		public Builder id(final UUID id) {
			this.id = id;
			return this;
		}

		public Builder pais(final PaisDominio pais) {
			this.pais = pais;
			return this;
		}

		public Builder nombre(final String nombre) {
			this.nombre = UtilTexto.aplicarTrim(nombre);
			return this;
		}

		public DepartamentoDominio build() {
			return new DepartamentoDominio(this);
		}
	}
}
