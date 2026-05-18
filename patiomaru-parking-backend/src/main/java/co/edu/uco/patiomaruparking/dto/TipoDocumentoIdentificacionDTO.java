package co.edu.uco.patiomaruparking.dto;

public class TipoDocumentoIdentificacionDTO {

	private String codigoTipoDocumentoIdentificacion;
	private String nombre;

	private TipoDocumentoIdentificacionDTO(final Builder builder) {
		setCodigoTipoDocumentoIdentificacion(builder.codigoTipoDocumentoIdentificacion);
		setNombre(builder.nombre);
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getCodigoTipoDocumentoIdentificacion() {
		return codigoTipoDocumentoIdentificacion;
	}

	private void setCodigoTipoDocumentoIdentificacion(final String codigoTipoDocumentoIdentificacion) {
		this.codigoTipoDocumentoIdentificacion = aplicarTrim(codigoTipoDocumentoIdentificacion);
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(final String nombre) {
		this.nombre = aplicarTrim(nombre);
	}

	public static class Builder {

		private String codigoTipoDocumentoIdentificacion;
		private String nombre;

		private Builder() {
			super();
		}

		public Builder codigoTipoDocumentoIdentificacion(final String codigoTipoDocumentoIdentificacion) {
			this.codigoTipoDocumentoIdentificacion = aplicarTrim(codigoTipoDocumentoIdentificacion);
			return this;
		}

		public Builder nombre(final String nombre) {
			this.nombre = aplicarTrim(nombre);
			return this;
		}

		public TipoDocumentoIdentificacionDTO build() {
			return new TipoDocumentoIdentificacionDTO(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}
