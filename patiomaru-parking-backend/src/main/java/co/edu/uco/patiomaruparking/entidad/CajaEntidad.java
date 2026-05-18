package co.edu.uco.patiomaruparking.entidad;

public class CajaEntidad {

	private String codigoCaja;
	private String nombre;
	private Boolean estado;
	private UbicacionEntidad ubicacion;

	private CajaEntidad(final Builder builder) {
		setCodigoCaja(builder.codigoCaja);
		setNombre(builder.nombre);
		setEstado(builder.estado);
		setUbicacion(builder.ubicacion);
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getCodigoCaja() {
		return codigoCaja;
	}

	private void setCodigoCaja(final String codigoCaja) {
		this.codigoCaja = aplicarTrim(codigoCaja);
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

	public UbicacionEntidad getUbicacion() {
		return ubicacion;
	}

	private void setUbicacion(final UbicacionEntidad ubicacion) {
		this.ubicacion = ubicacion == null ? UbicacionEntidad.builder().build() : ubicacion;
	}

	public static class Builder {

		private String codigoCaja;
		private String nombre;
		private Boolean estado;
		private UbicacionEntidad ubicacion;

		private Builder() {
			super();
		}

		public Builder codigoCaja(final String codigoCaja) {
			this.codigoCaja = aplicarTrim(codigoCaja);
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

		public Builder ubicacion(final UbicacionEntidad ubicacion) {
			this.ubicacion = ubicacion == null ? UbicacionEntidad.builder().build() : ubicacion;
			return this;
		}

		public CajaEntidad build() {
			return new CajaEntidad(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}
