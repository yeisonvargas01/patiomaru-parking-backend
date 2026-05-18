package co.edu.uco.patiomaruparking.negocio.dominio;

public class CiudadResidenciaDominio {

	private String codigoCiudadResidencia;
	private String nombre;

	private CiudadResidenciaDominio(final Builder builder) {
		setCodigoCiudadResidencia(builder.codigoCiudadResidencia);
		setNombre(builder.nombre);
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getCodigoCiudadResidencia() {
		return codigoCiudadResidencia;
	}

	private void setCodigoCiudadResidencia(final String codigoCiudadResidencia) {
		this.codigoCiudadResidencia = aplicarTrim(codigoCiudadResidencia);
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(final String nombre) {
		this.nombre = aplicarTrim(nombre);
	}

	public boolean tieneCodigo() {
		return !codigoCiudadResidencia.isBlank();
	}

	public boolean tieneNombre() {
		return !nombre.isBlank();
	}

	public static class Builder {

		private String codigoCiudadResidencia;
		private String nombre;

		private Builder() {
			super();
		}

		public Builder codigoCiudadResidencia(final String codigoCiudadResidencia) {
			this.codigoCiudadResidencia = aplicarTrim(codigoCiudadResidencia);
			return this;
		}

		public Builder nombre(final String nombre) {
			this.nombre = aplicarTrim(nombre);
			return this;
		}

		public CiudadResidenciaDominio build() {
			return new CiudadResidenciaDominio(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}