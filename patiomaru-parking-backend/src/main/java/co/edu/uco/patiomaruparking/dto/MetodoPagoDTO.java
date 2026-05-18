package co.edu.uco.patiomaruparking.dto;

public class MetodoPagoDTO {

	private String codigoMetodoPago;
	private String nombre;

	private MetodoPagoDTO(final Builder builder) {
		setCodigoMetodoPago(builder.codigoMetodoPago);
		setNombre(builder.nombre);
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getCodigoMetodoPago() {
		return codigoMetodoPago;
	}

	private void setCodigoMetodoPago(final String codigoMetodoPago) {
		this.codigoMetodoPago = aplicarTrim(codigoMetodoPago);
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(final String nombre) {
		this.nombre = aplicarTrim(nombre);
	}

	public static class Builder {

		private String codigoMetodoPago;
		private String nombre;

		private Builder() {
			super();
		}

		public Builder codigoMetodoPago(final String codigoMetodoPago) {
			this.codigoMetodoPago = aplicarTrim(codigoMetodoPago);
			return this;
		}

		public Builder nombre(final String nombre) {
			this.nombre = aplicarTrim(nombre);
			return this;
		}

		public MetodoPagoDTO build() {
			return new MetodoPagoDTO(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}
