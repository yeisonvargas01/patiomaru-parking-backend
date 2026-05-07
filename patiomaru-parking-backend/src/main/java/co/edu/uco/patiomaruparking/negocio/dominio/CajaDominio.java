package co.edu.uco.patiomaruparking.negocio.dominio;

import java.util.UUID;

import co.edu.uco.patiomaruparking.transversal.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.UtilUUID;

public class CajaDominio {

	private UUID id;
	private String nombre;
	private boolean estado;
	private UbicacionDominio ubicacion;

	private CajaDominio(final Builder builder) {
		setId(builder.id);
		setNombre(builder.nombre);
		setEstado(builder.estado);
		setUbicacion(builder.ubicacion);
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

	public UbicacionDominio getUbicacion() {
		return ubicacion;
	}

	private void setUbicacion(final UbicacionDominio ubicacion) {
		this.ubicacion = UtilObjeto.obtenerValorDefecto(ubicacion, new UbicacionDominio.Builder().build());
	}

	public static class Builder {

		private UUID id;
		private String nombre;
		private boolean estado;
		private UbicacionDominio ubicacion;

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

		public Builder ubicacion(final UbicacionDominio ubicacion) {
			this.ubicacion = ubicacion;
			return this;
		}

		public CajaDominio build() {
			return new CajaDominio(this);
		}
	}
}
