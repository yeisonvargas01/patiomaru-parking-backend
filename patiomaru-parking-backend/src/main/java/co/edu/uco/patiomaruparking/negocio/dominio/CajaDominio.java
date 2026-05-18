package co.edu.uco.patiomaruparking.negocio.dominio;

public class CajaDominio {

	private String codigoCaja;
	private String nombre;
	private Boolean estado;
	private UbicacionDominio ubicacion;

	private CajaDominio(final Builder builder) {
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

	public UbicacionDominio getUbicacion() {
		return ubicacion;
	}

	private void setUbicacion(final UbicacionDominio ubicacion) {
		this.ubicacion = ubicacion == null ? UbicacionDominio.builder().build() : ubicacion;
	}

	public boolean tieneCodigo() {
		return !codigoCaja.isBlank();
	}

	public boolean tieneNombre() {
		return !nombre.isBlank();
	}

	public boolean estaActiva() {
		return Boolean.TRUE.equals(estado);
	}

	public boolean tieneUbicacion() {
		return ubicacion != null && ubicacion.tieneCodigo();
	}

	public static class Builder {

		private String codigoCaja;
		private String nombre;
		private Boolean estado;
		private UbicacionDominio ubicacion;

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

		public Builder ubicacion(final UbicacionDominio ubicacion) {
			this.ubicacion = ubicacion == null ? UbicacionDominio.builder().build() : ubicacion;
			return this;
		}

		public CajaDominio build() {
			return new CajaDominio(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}