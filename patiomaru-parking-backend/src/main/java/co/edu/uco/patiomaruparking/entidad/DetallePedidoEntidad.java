package co.edu.uco.patiomaruparking.entidad;

import java.math.BigDecimal;

public class DetallePedidoEntidad {

	private String codigoDetallePedido;
	private Integer cantidad;
	private BigDecimal subtotal;
	private String codigoPedido;
	private PlatoEntidad plato;

	private DetallePedidoEntidad(final Builder builder) {
		setCodigoDetallePedido(builder.codigoDetallePedido);
		setCantidad(builder.cantidad);
		setSubtotal(builder.subtotal);
		setCodigoPedido(builder.codigoPedido);
		setPlato(builder.plato);
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getCodigoDetallePedido() {
		return codigoDetallePedido;
	}

	private void setCodigoDetallePedido(final String codigoDetallePedido) {
		this.codigoDetallePedido = aplicarTrim(codigoDetallePedido);
	}

	public Integer getCantidad() {
		return cantidad;
	}

	private void setCantidad(final Integer cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	private void setSubtotal(final BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public String getCodigoPedido() {
		return codigoPedido;
	}

	private void setCodigoPedido(final String codigoPedido) {
		this.codigoPedido = aplicarTrim(codigoPedido);
	}

	public PlatoEntidad getPlato() {
		return plato;
	}

	private void setPlato(final PlatoEntidad plato) {
		this.plato = plato == null ? PlatoEntidad.builder().build() : plato;
	}

	public static class Builder {

		private String codigoDetallePedido;
		private Integer cantidad;
		private BigDecimal subtotal;
		private String codigoPedido;
		private PlatoEntidad plato;

		private Builder() {
			super();
		}

		public Builder codigoDetallePedido(final String codigoDetallePedido) {
			this.codigoDetallePedido = aplicarTrim(codigoDetallePedido);
			return this;
		}

		public Builder cantidad(final Integer cantidad) {
			this.cantidad = cantidad;
			return this;
		}

		public Builder subtotal(final BigDecimal subtotal) {
			this.subtotal = subtotal;
			return this;
		}

		public Builder codigoPedido(final String codigoPedido) {
			this.codigoPedido = aplicarTrim(codigoPedido);
			return this;
		}

		public Builder plato(final PlatoEntidad plato) {
			this.plato = plato == null ? PlatoEntidad.builder().build() : plato;
			return this;
		}

		public DetallePedidoEntidad build() {
			return new DetallePedidoEntidad(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}