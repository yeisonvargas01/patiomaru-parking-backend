package co.edu.uco.patiomaruparking.dto;

public class InventarioDTO {

	private String codigoInventario;
	private String nombre;
	private UbicacionDTO ubicacion;
	private Boolean estado;

	private InventarioDTO(final Builder builder) {
		setCodigoInventario(builder.codigoInventario);
		setNombre(builder.nombre);
		setUbicacion(builder.ubicacion);
		setEstado(builder.estado);
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getCodigoInventario() {
		return codigoInventario;
	}

	private void setCodigoInventario(final String codigoInventario) {
		this.codigoInventario = aplicarTrim(codigoInventario);
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(final String nombre) {
		this.nombre = aplicarTrim(nombre);
	}

	public UbicacionDTO getUbicacion() {
		return ubicacion;
	}

	private void setUbicacion(final UbicacionDTO ubicacion) {
		this.ubicacion = ubicacion == null ? UbicacionDTO.builder().build() : ubicacion;
	}

	public Boolean getEstado() {
		return estado;
	}

	private void setEstado(final Boolean estado) {
		this.estado = estado;
	}

	public static class Builder {

		private String codigoInventario;
		private String nombre;
		private UbicacionDTO ubicacion;
		private Boolean estado;

		private Builder() {
			super();
		}

		public Builder codigoInventario(final String codigoInventario) {
			this.codigoInventario = aplicarTrim(codigoInventario);
			return this;
		}

		public Builder nombre(final String nombre) {
			this.nombre = aplicarTrim(nombre);
			return this;
		}

		public Builder ubicacion(final UbicacionDTO ubicacion) {
			this.ubicacion = ubicacion == null ? UbicacionDTO.builder().build() : ubicacion;
			return this;
		}

		public Builder estado(final Boolean estado) {
			this.estado = estado;
			return this;
		}

		public InventarioDTO build() {
			return new InventarioDTO(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}