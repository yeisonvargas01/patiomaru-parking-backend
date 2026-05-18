package co.edu.uco.patiomaruparking.dto;

public class ClienteDTO {

	private String codigoCliente;
	private String nombre;
	private String telefono;
	private String correoElectronico;
	private Boolean estado;

	private ClienteDTO(final Builder builder) {
		setCodigoCliente(builder.codigoCliente);
		setNombre(builder.nombre);
		setTelefono(builder.telefono);
		setCorreoElectronico(builder.correoElectronico);
		setEstado(builder.estado);
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	private void setCodigoCliente(final String codigoCliente) {
		this.codigoCliente = aplicarTrim(codigoCliente);
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(final String nombre) {
		this.nombre = aplicarTrim(nombre);
	}

	public String getTelefono() {
		return telefono;
	}

	private void setTelefono(final String telefono) {
		this.telefono = aplicarTrim(telefono);
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	private void setCorreoElectronico(final String correoElectronico) {
		this.correoElectronico = aplicarTrim(correoElectronico);
	}

	public Boolean getEstado() {
		return estado;
	}

	private void setEstado(final Boolean estado) {
		this.estado = estado;
	}

	public static class Builder {

		private String codigoCliente;
		private String nombre;
		private String telefono;
		private String correoElectronico;
		private Boolean estado;

		private Builder() {
			super();
		}

		public Builder codigoCliente(final String codigoCliente) {
			this.codigoCliente = aplicarTrim(codigoCliente);
			return this;
		}

		public Builder nombre(final String nombre) {
			this.nombre = aplicarTrim(nombre);
			return this;
		}

		public Builder telefono(final String telefono) {
			this.telefono = aplicarTrim(telefono);
			return this;
		}

		public Builder correoElectronico(final String correoElectronico) {
			this.correoElectronico = aplicarTrim(correoElectronico);
			return this;
		}

		public Builder estado(final Boolean estado) {
			this.estado = estado;
			return this;
		}

		public ClienteDTO build() {
			return new ClienteDTO(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}