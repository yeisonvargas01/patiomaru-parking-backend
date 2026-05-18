package co.edu.uco.patiomaruparking.entidad;

public class CiudadResidenciaEntidad {

	private String codigoCiudadResidencia;
	private String nombre;

	private CiudadResidenciaEntidad(final Builder builder) {
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

		public CiudadResidenciaEntidad build() {
			return new CiudadResidenciaEntidad(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}
