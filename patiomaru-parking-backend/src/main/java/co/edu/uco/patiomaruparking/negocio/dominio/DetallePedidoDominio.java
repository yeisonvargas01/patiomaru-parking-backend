package co.edu.uco.patiomaruparking.negocio.dominio;

import java.math.BigDecimal;
import java.util.UUID;

import co.edu.uco.patiomaruparking.transversal.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.UtilUUID;

public class DetallePedidoDominio {

	private static final BigDecimal SUBTOTAL_DEFECTO = BigDecimal.ZERO;

	private UUID id;
	private PedidoDominio pedido;
	private PlatoDominio plato;
	private int cantidad;
	private BigDecimal subtotal;

	private DetallePedidoDominio(final Builder builder) {
		setId(builder.id);
		setPedido(builder.pedido);
		setPlato(builder.plato);
		setCantidad(builder.cantidad);
		setSubtotal(builder.subtotal);
	}

	public UUID getId() {
		return id;
	}

	private void setId(final UUID id) {
		this.id = UtilUUID.obtenerValorDefecto(id);
	}

	public PedidoDominio getPedido() {
		return pedido;
	}

	private void setPedido(final PedidoDominio pedido) {
		this.pedido = UtilObjeto.obtenerValorDefecto(pedido, new PedidoDominio.Builder().build());
	}

	public PlatoDominio getPlato() {
		return plato;
	}

	private void setPlato(final PlatoDominio plato) {
		this.plato = UtilObjeto.obtenerValorDefecto(plato, new PlatoDominio.Builder().build());
	}

	public int getCantidad() {
		return cantidad;
	}

	private void setCantidad(final int cantidad) {
		this.cantidad = Math.max(0, cantidad);
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	private void setSubtotal(final BigDecimal subtotal) {
		BigDecimal subtotalSeguro = UtilObjeto.obtenerValorDefecto(subtotal, SUBTOTAL_DEFECTO);
		this.subtotal = subtotalSeguro.compareTo(BigDecimal.ZERO) < 0 ? SUBTOTAL_DEFECTO : subtotalSeguro;
	}

	public static class Builder {

		private UUID id;
		private PedidoDominio pedido;
		private PlatoDominio plato;
		private int cantidad;
		private BigDecimal subtotal;

		public Builder id(final UUID id) {
			this.id = id;
			return this;
		}

		public Builder pedido(final PedidoDominio pedido) {
			this.pedido = pedido;
			return this;
		}

		public Builder plato(final PlatoDominio plato) {
			this.plato = plato;
			return this;
		}

		public Builder cantidad(final int cantidad) {
			this.cantidad = Math.max(0, cantidad);
			return this;
		}

		public Builder subtotal(final BigDecimal subtotal) {
			BigDecimal subtotalSeguro = UtilObjeto.obtenerValorDefecto(subtotal, SUBTOTAL_DEFECTO);
			this.subtotal = subtotalSeguro.compareTo(BigDecimal.ZERO) < 0 ? SUBTOTAL_DEFECTO : subtotalSeguro;
			return this;
		}

		public DetallePedidoDominio build() {
			return new DetallePedidoDominio(this);
		}
	}
}
