package co.edu.uco.patiomaruparking.entidad;

public class CargoEntidad {

	private String codigoCargo;
	private String nombre;
	private Boolean estado;

	private CargoEntidad(final Builder builder) {
		setCodigoCargo(builder.codigoCargo);
		setNombre(builder.nombre);
		setEstado(builder.estado);
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getCodigoCargo() {
		return codigoCargo;
	}

	private void setCodigoCargo(final String codigoCargo) {
		this.codigoCargo = aplicarTrim(codigoCargo);
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(final String nombre) {
		this.nombre = aplicarTrim(nombre);
	}

	public Boolean getEstado() {
		return estado;
	}

	private void setEstado(final Boolean estado) {
		this.estado = estado;
	}

	public static class Builder {

		private String codigoCargo;
		private String nombre;
		private Boolean estado;

		private Builder() {
			super();
		}

		public Builder codigoCargo(final String codigoCargo) {
			this.codigoCargo = aplicarTrim(codigoCargo);
			return this;
		}

		public Builder nombre(final String nombre) {
			this.nombre = aplicarTrim(nombre);
			return this;
		}

		public Builder estado(final Boolean estado) {
			this.estado = estado;
			return this;
		}

		public CargoEntidad build() {
			return new CargoEntidad(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}
