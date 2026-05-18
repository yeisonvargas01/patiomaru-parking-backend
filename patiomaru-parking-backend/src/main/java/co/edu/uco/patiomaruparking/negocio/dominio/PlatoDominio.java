package co.edu.uco.patiomaruparking.negocio.dominio;

import java.math.BigDecimal;

public class PlatoDominio {

	private String codigoPlato;
	private String nombre;
	private BigDecimal precioVenta;
	private Boolean estado;
	private CategoriaDominio categoria;

	private PlatoDominio(final Builder builder) {
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

	public CategoriaDominio getCategoria() {
		return categoria;
	}

	private void setCategoria(final CategoriaDominio categoria) {
		this.categoria = categoria == null ? CategoriaDominio.builder().build() : categoria;
	}

	public boolean tieneCodigo() {
		return !codigoPlato.isBlank();
	}

	public boolean estaDisponible() {
		return Boolean.TRUE.equals(estado);
	}

	public boolean tienePrecioVentaValido() {
		return precioVenta != null && precioVenta.compareTo(BigDecimal.ZERO) > 0;
	}

	public static class Builder {

		private String codigoPlato;
		private String nombre;
		private BigDecimal precioVenta;
		private Boolean estado;
		private CategoriaDominio categoria;

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

		public Builder categoria(final CategoriaDominio categoria) {
			this.categoria = categoria == null ? CategoriaDominio.builder().build() : categoria;
			return this;
		}

		public PlatoDominio build() {
			return new PlatoDominio(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}