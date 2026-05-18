package co.edu.uco.patiomaruparking.entidad;

public class CiudadEntidad {

	private String codigoCiudad;
	private String nombre;

	private CiudadEntidad(final Builder builder) {
		setCodigoCiudad(builder.codigoCiudad);
		setNombre(builder.nombre);
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getCodigoCiudad() {
		return codigoCiudad;
	}

	private void setCodigoCiudad(final String codigoCiudad) {
		this.codigoCiudad = aplicarTrim(codigoCiudad);
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(final String nombre) {
		this.nombre = aplicarTrim(nombre);
	}

	public static class Builder {

		private String codigoCiudad;
		private String nombre;

		private Builder() {
			super();
		}

		public Builder codigoCiudad(final String codigoCiudad) {
			this.codigoCiudad = aplicarTrim(codigoCiudad);
			return this;
		}

		public Builder nombre(final String nombre) {
			this.nombre = aplicarTrim(nombre);
			return this;
		}

		public CiudadEntidad build() {
			return new CiudadEntidad(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}
