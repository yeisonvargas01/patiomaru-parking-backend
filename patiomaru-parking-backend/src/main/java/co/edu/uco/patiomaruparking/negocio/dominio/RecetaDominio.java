package co.edu.uco.patiomaruparking.negocio.dominio;

import java.util.UUID;

import co.edu.uco.patiomaruparking.transversal.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.UtilUUID;

public class RecetaDominio {

	private UUID id;
	private String nombre;
	private boolean estado;
	private PlatoDominio plato;

	private RecetaDominio(final Builder builder) {
		setId(builder.id);
		setNombre(builder.nombre);
		setEstado(builder.estado);
		setPlato(builder.plato);
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

	public boolean isEstado() {
		return estado;
	}

	private void setEstado(final boolean estado) {
		this.estado = estado;
	}

	public PlatoDominio getPlato() {
		return plato;
	}

	private void setPlato(final PlatoDominio plato) {
		this.plato = UtilObjeto.obtenerValorDefecto(plato, new PlatoDominio.Builder().build());
	}

	public static class Builder {

		private UUID id;
		private String nombre;
		private boolean estado;
		private PlatoDominio plato;

		public Builder id(final UUID id) {
			this.id = id;
			return this;
		}

		public Builder nombre(final String nombre) {
			this.nombre = UtilTexto.aplicarTrim(nombre);
			return this;
		}

		public Builder estado(final boolean estado) {
			this.estado = estado;
			return this;
		}

		public Builder plato(final PlatoDominio plato) {
			this.plato = plato;
			return this;
		}

		public RecetaDominio build() {
			return new RecetaDominio(this);
		}
	}
}
