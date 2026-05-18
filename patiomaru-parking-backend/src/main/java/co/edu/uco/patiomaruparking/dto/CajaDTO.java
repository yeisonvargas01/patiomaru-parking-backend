package co.edu.uco.patiomaruparking.dto;

public class CajaDTO {

	private String codigoCaja;
	private String nombre;
	private Boolean estado;
	private UbicacionDTO ubicacion;

	private CajaDTO(final Builder builder) {
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

	public UbicacionDTO getUbicacion() {
		return ubicacion;
	}

	private void setUbicacion(final UbicacionDTO ubicacion) {
		this.ubicacion = ubicacion == null ? UbicacionDTO.builder().build() : ubicacion;
	}

	public static class Builder {

		private String codigoCaja;
		private String nombre;
		private Boolean estado;
		private UbicacionDTO ubicacion;

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

		public Builder ubicacion(final UbicacionDTO ubicacion) {
			this.ubicacion = ubicacion == null ? UbicacionDTO.builder().build() : ubicacion;
			return this;
		}

		public CajaDTO build() {
			return new CajaDTO(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}
