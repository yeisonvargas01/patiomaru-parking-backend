package co.edu.uco.patiomaruparking.dto;

public class MesaDTO {

	private String codigoMesa;
	private String nombre;

	private MesaDTO(final Builder builder) {
		setCodigoMesa(builder.codigoMesa);
		setNombre(builder.nombre);
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getCodigoMesa() {
		return codigoMesa;
	}

	private void setCodigoMesa(final String codigoMesa) {
		this.codigoMesa = aplicarTrim(codigoMesa);
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(final String nombre) {
		this.nombre = aplicarTrim(nombre);
	}

	public static class Builder {

		private String codigoMesa;
		private String nombre;

		private Builder() {
			super();
		}

		public Builder codigoMesa(final String codigoMesa) {
			this.codigoMesa = aplicarTrim(codigoMesa);
			return this;
		}

		public Builder nombre(final String nombre) {
			this.nombre = aplicarTrim(nombre);
			return this;
		}

		public MesaDTO build() {
			return new MesaDTO(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}
