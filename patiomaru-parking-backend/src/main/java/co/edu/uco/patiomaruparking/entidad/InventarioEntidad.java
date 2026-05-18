package co.edu.uco.patiomaruparking.entidad;

public class InventarioEntidad {

	private String codigoInventario;
	private String nombre;
	private UbicacionEntidad ubicacion;
	private Boolean estado;

	private InventarioEntidad(final Builder builder) {
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

	public UbicacionEntidad getUbicacion() {
		return ubicacion;
	}

	private void setUbicacion(final UbicacionEntidad ubicacion) {
		this.ubicacion = ubicacion == null ? UbicacionEntidad.builder().build() : ubicacion;
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
		private UbicacionEntidad ubicacion;
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

		public Builder ubicacion(final UbicacionEntidad ubicacion) {
			this.ubicacion = ubicacion == null ? UbicacionEntidad.builder().build() : ubicacion;
			return this;
		}

		public Builder estado(final Boolean estado) {
			this.estado = estado;
			return this;
		}

		public InventarioEntidad build() {
			return new InventarioEntidad(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}
