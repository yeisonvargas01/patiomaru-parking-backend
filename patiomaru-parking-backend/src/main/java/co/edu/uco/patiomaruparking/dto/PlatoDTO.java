package co.edu.uco.patiomaruparking.dto;

import java.math.BigDecimal;

public class PlatoDTO {

	private String codigoPlato;
	private String nombre;
	private CategoriaDTO categoria;
	private BigDecimal precioVenta;
	private Boolean estado;

	private PlatoDTO(final Builder builder) {
		setCodigoPlato(builder.codigoPlato);
		setNombre(builder.nombre);
		setCategoria(builder.categoria);
		setPrecioVenta(builder.precioVenta);
		setEstado(builder.estado);
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

	public CategoriaDTO getCategoria() {
		return categoria;
	}

	private void setCategoria(final CategoriaDTO categoria) {
		this.categoria = categoria == null ? CategoriaDTO.builder().build() : categoria;
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

	public static class Builder {

		private String codigoPlato;
		private String nombre;
		private CategoriaDTO categoria;
		private BigDecimal precioVenta;
		private Boolean estado;

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

		public Builder categoria(final CategoriaDTO categoria) {
			this.categoria = categoria == null ? CategoriaDTO.builder().build() : categoria;
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

		public PlatoDTO build() {
			return new PlatoDTO(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}
