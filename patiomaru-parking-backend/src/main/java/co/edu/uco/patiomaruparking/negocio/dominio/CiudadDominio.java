package co.edu.uco.patiomaruparking.negocio.dominio;

import java.util.UUID;

import co.edu.uco.patiomaruparking.transversal.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.UtilUUID;

public class CiudadDominio {

	private UUID id;
	private DepartamentoDominio departamento;
	private String nombre;

	private CiudadDominio(final Builder builder) {
		setId(builder.id);
		setDepartamento(builder.departamento);
		setNombre(builder.nombre);
	}

	public UUID getId() {
		return id;
	}

	private void setId(final UUID id) {
		this.id = UtilUUID.obtenerValorDefecto(id);
	}

	public DepartamentoDominio getDepartamento() {
		return departamento;
	}

	private void setDepartamento(final DepartamentoDominio departamento) {
		this.departamento = UtilObjeto.obtenerValorDefecto(departamento, new DepartamentoDominio.Builder().build());
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(final String nombre) {
		this.nombre = UtilTexto.aplicarTrim(nombre);
	}

	public static class Builder {

		private UUID id;
		private DepartamentoDominio departamento;
		private String nombre;

		public Builder id(final UUID id) {
			this.id = id;
			return this;
		}

		public Builder departamento(final DepartamentoDominio departamento) {
			this.departamento = departamento;
			return this;
		}

		public Builder nombre(final String nombre) {
			this.nombre = UtilTexto.aplicarTrim(nombre);
			return this;
		}

		public CiudadDominio build() {
			return new CiudadDominio(this);
		}
	}
}