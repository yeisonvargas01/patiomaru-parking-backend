package co.edu.uco.patiomaruparking.entidad;

public class CategoriaEntidad {

	private String codigoCategoria;
	private String nombre;

	private CategoriaEntidad(final Builder builder) {
		setCodigoCategoria(builder.codigoCategoria);
		setNombre(builder.nombre);
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getCodigoCategoria() {
		return codigoCategoria;
	}

	private void setCodigoCategoria(final String codigoCategoria) {
		this.codigoCategoria = aplicarTrim(codigoCategoria);
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(final String nombre) {
		this.nombre = aplicarTrim(nombre);
	}

	public static class Builder {

		private String codigoCategoria;
		private String nombre;

		private Builder() {
			super();
		}

		public Builder codigoCategoria(final String codigoCategoria) {
			this.codigoCategoria = aplicarTrim(codigoCategoria);
			return this;
		}

		public Builder nombre(final String nombre) {
			this.nombre = aplicarTrim(nombre);
			return this;
		}

		public CategoriaEntidad build() {
			return new CategoriaEntidad(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}
