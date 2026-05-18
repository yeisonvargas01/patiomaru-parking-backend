package co.edu.uco.patiomaruparking.entidad;

public class UbicacionEntidad {

	private String codigoUbicacion;
	private String nombre;

	private UbicacionEntidad(final Builder builder) {
		setCodigoUbicacion(builder.codigoUbicacion);
		setNombre(builder.nombre);
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getCodigoUbicacion() {
		return codigoUbicacion;
	}

	private void setCodigoUbicacion(final String codigoUbicacion) {
		this.codigoUbicacion = aplicarTrim(codigoUbicacion);
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(final String nombre) {
		this.nombre = aplicarTrim(nombre);
	}

	public static class Builder {

		private String codigoUbicacion;
		private String nombre;

		private Builder() {
			super();
		}

		public Builder codigoUbicacion(final String codigoUbicacion) {
			this.codigoUbicacion = aplicarTrim(codigoUbicacion);
			return this;
		}

		public Builder nombre(final String nombre) {
			this.nombre = aplicarTrim(nombre);
			return this;
		}

		public UbicacionEntidad build() {
			return new UbicacionEntidad(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}
