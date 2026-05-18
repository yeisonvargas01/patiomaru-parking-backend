package co.edu.uco.patiomaruparking.negocio.dominio;

import java.math.BigDecimal;

public class DetallePedidoDominio {

	private String codigoDetallePedido;
	private Integer cantidad;
	private BigDecimal subtotal;
	private String codigoPedido;
	private PlatoDominio plato;

	private DetallePedidoDominio(final Builder builder) {
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

	public PlatoDominio getPlato() {
		return plato;
	}

	private void setPlato(final PlatoDominio plato) {
		this.plato = plato == null ? PlatoDominio.builder().build() : plato;
	}

	public boolean tieneCodigo() {
		return !codigoDetallePedido.isBlank();
	}

	public boolean tieneCodigoPedido() {
		return !codigoPedido.isBlank();
	}

	public boolean tieneCantidadValida() {
		return cantidad != null && cantidad > 0;
	}

	public boolean tienePlato() {
		return plato != null && plato.tieneCodigo();
	}

	public BigDecimal calcularSubtotal() {
		if (!tieneCantidadValida() || plato == null || !plato.tienePrecioVentaValido()) {
			return BigDecimal.ZERO;
		}

		return plato.getPrecioVenta().multiply(BigDecimal.valueOf(cantidad));
	}

	public DetallePedidoDominio actualizarSubtotalCalculado() {
		return DetallePedidoDominio.builder()
				.codigoDetallePedido(getCodigoDetallePedido())
				.cantidad(getCantidad())
				.subtotal(calcularSubtotal())
				.codigoPedido(getCodigoPedido())
				.plato(getPlato())
				.build();
	}

	public static class Builder {

		private String codigoDetallePedido;
		private Integer cantidad;
		private BigDecimal subtotal;
		private String codigoPedido;
		private PlatoDominio plato;

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

		public Builder plato(final PlatoDominio plato) {
			this.plato = plato == null ? PlatoDominio.builder().build() : plato;
			return this;
		}

		public DetallePedidoDominio build() {
			return new DetallePedidoDominio(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}