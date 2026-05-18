package co.edu.uco.patiomaruparking.negocio.dominio;

public class InventarioDominio {

	private String codigoInventario;
	private String nombre;
	private UbicacionDominio ubicacion;
	private Boolean estado;

	private InventarioDominio(final Builder builder) {
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

	public UbicacionDominio getUbicacion() {
		return ubicacion;
	}

	private void setUbicacion(final UbicacionDominio ubicacion) {
		this.ubicacion = ubicacion == null ? UbicacionDominio.builder().build() : ubicacion;
	}

	public Boolean getEstado() {
		return estado;
	}

	private void setEstado(final Boolean estado) {
		this.estado = estado;
	}

	public boolean tieneCodigo() {
		return !codigoInventario.isBlank();
	}

	public boolean tieneNombre() {
		return !nombre.isBlank();
	}

	public boolean tieneUbicacion() {
		return ubicacion != null && ubicacion.tieneCodigo();
	}

	public boolean estaActivo() {
		return Boolean.TRUE.equals(estado);
	}

	public static class Builder {

		private String codigoInventario;
		private String nombre;
		private UbicacionDominio ubicacion;
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

		public Builder ubicacion(final UbicacionDominio ubicacion) {
			this.ubicacion = ubicacion == null ? UbicacionDominio.builder().build() : ubicacion;
			return this;
		}

		public Builder estado(final Boolean estado) {
			this.estado = estado;
			return this;
		}

		public InventarioDominio build() {
			return new InventarioDominio(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}