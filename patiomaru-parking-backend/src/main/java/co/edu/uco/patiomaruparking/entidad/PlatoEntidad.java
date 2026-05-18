package co.edu.uco.patiomaruparking.entidad;

import java.math.BigDecimal;

public class PlatoEntidad {

	private String codigoPlato;
	private String nombre;
	private BigDecimal precioVenta;
	private Boolean estado;
	private CategoriaEntidad categoria;

	private PlatoEntidad(final Builder builder) {
		setCodigoPlato(builder.codigoPlato);
		setNombre(builder.nombre);
		setPrecioVenta(builder.precioVenta);
		setEstado(builder.estado);
		setCategoria(builder.categoria);
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getCodigoPlato() {
		return codigoPlato;
	}

	private void setCodigoPlato(final String codigoPlato) {
		this.codigoPlato = aplicarTrim(codigoPlato);
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(final String nombre) {
		this.nombre = aplicarTrim(nombre);
	}

	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	private void setPrecioVenta(final BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}

	public Boolean getEstado() {
		return estado;
	}

	private void setEstado(final Boolean estado) {
		this.estado = estado;
	}

	public CategoriaEntidad getCategoria() {
		return categoria;
	}

	private void setCategoria(final CategoriaEntidad categoria) {
		this.categoria = categoria == null ? CategoriaEntidad.builder().build() : categoria;
	}

	public static class Builder {

		private String codigoPlato;
		private String nombre;
		private BigDecimal precioVenta;
		private Boolean estado;
		private CategoriaEntidad categoria;

		private Builder() {
			super();
		}

		public Builder codigoPlato(final String codigoPlato) {
			this.codigoPlato = aplicarTrim(codigoPlato);
			return this;
		}

		public Builder nombre(final String nombre) {
			this.nombre = aplicarTrim(nombre);
			return this;
		}

		public Builder precioVenta(final BigDecimal precioVenta) {
			this.precioVenta = precioVenta;
			return this;
		}

		public Builder estado(final Boolean estado) {
			this.estado = estado;
			return this;
		}

		public Builder categoria(final CategoriaEntidad categoria) {
			this.categoria = categoria == null ? CategoriaEntidad.builder().build() : categoria;
			return this;
		}

		public PlatoEntidad build() {
			return new PlatoEntidad(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}