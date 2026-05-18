package co.edu.uco.patiomaruparking.dto;

import java.math.BigDecimal;

public class PlatoDTO {

	private String codigoPlato;
	private String nombre;
	private String descripcion;
	private BigDecimal precioVenta;
	private Boolean disponible;
	private CategoriaDTO categoria;

	private PlatoDTO(final Builder builder) {
		setCodigoPlato(builder.codigoPlato);
		setNombre(builder.nombre);
		setDescripcion(builder.descripcion);
		setPrecioVenta(builder.precioVenta);
		setDisponible(builder.disponible);
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

	public String getDescripcion() {
		return descripcion;
	}

	private void setDescripcion(final String descripcion) {
		this.descripcion = aplicarTrim(descripcion);
	}

	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	private void setPrecioVenta(final BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}

	public Boolean getDisponible() {
		return disponible;
	}

	private void setDisponible(final Boolean disponible) {
		this.disponible = disponible;
	}

	public CategoriaDTO getCategoria() {
		return categoria;
	}

	private void setCategoria(final CategoriaDTO categoria) {
		this.categoria = categoria == null ? CategoriaDTO.builder().build() : categoria;
	}

	public static class Builder {

		private String codigoPlato;
		private String nombre;
		private String descripcion;
		private BigDecimal precioVenta;
		private Boolean disponible;
		private CategoriaDTO categoria;

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

		public Builder descripcion(final String descripcion) {
			this.descripcion = aplicarTrim(descripcion);
			return this;
		}

		public Builder precioVenta(final BigDecimal precioVenta) {
			this.precioVenta = precioVenta;
			return this;
		}

		public Builder disponible(final Boolean disponible) {
			this.disponible = disponible;
			return this;
		}

		public Builder categoria(final CategoriaDTO categoria) {
			this.categoria = categoria == null ? CategoriaDTO.builder().build() : categoria;
			return this;
		}

		public PlatoDTO build() {
			return new PlatoDTO(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}
