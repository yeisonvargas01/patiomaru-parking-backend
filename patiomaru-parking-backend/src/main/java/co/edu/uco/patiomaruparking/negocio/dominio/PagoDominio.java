package co.edu.uco.patiomaruparking.negocio.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import co.edu.uco.patiomaruparking.transversal.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.UtilUUID;

public class PagoDominio {

	private static final LocalDate FECHA_DEFECTO = LocalDate.of(1900, 1, 1);
	private static final LocalTime HORA_DEFECTO = LocalTime.of(0, 0);
	private static final BigDecimal VALOR_PAGADO_DEFECTO = BigDecimal.ZERO;

	private UUID id;
	private LocalDate fechaPago;
	private LocalTime horaPago;
	private BigDecimal valorPagado;
	private String estadoPago;
	private PedidoDominio pedido;
	private MetodoPagoDominio metodoPago;
	private CajaDominio caja;

	private PagoDominio(final Builder builder) {
		setId(builder.id);
		setFechaPago(builder.fechaPago);
		setHoraPago(builder.horaPago);
		setValorPagado(builder.valorPagado);
		setEstadoPago(builder.estadoPago);
		setPedido(builder.pedido);
		setMetodoPago(builder.metodoPago);
		setCaja(builder.caja);
	}

	public UUID getId() {
		return id;
	}

	private void setId(final UUID id) {
		this.id = UtilUUID.obtenerValorDefecto(id);
	}

	public LocalDate getFechaPago() {
		return fechaPago;
	}

	private void setFechaPago(final LocalDate fechaPago) {
		this.fechaPago = UtilObjeto.obtenerValorDefecto(fechaPago, FECHA_DEFECTO);
	}

	public LocalTime getHoraPago() {
		return horaPago;
	}

	private void setHoraPago(final LocalTime horaPago) {
		this.horaPago = UtilObjeto.obtenerValorDefecto(horaPago, HORA_DEFECTO);
	}

	public BigDecimal getValorPagado() {
		return valorPagado;
	}

	private void setValorPagado(final BigDecimal valorPagado) {
		BigDecimal valorPagadoSeguro = UtilObjeto.obtenerValorDefecto(valorPagado, VALOR_PAGADO_DEFECTO);
		this.valorPagado = valorPagadoSeguro.compareTo(BigDecimal.ZERO) < 0
				? VALOR_PAGADO_DEFECTO
				: valorPagadoSeguro;
	}

	public String getEstadoPago() {
		return estadoPago;
	}

	private void setEstadoPago(final String estadoPago) {
		this.estadoPago = UtilTexto.aplicarTrim(estadoPago);
	}

	public PedidoDominio getPedido() {
		return pedido;
	}

	private void setPedido(final PedidoDominio pedido) {
		this.pedido = UtilObjeto.obtenerValorDefecto(pedido, new PedidoDominio.Builder().build());
	}

	public MetodoPagoDominio getMetodoPago() {
		return metodoPago;
	}

	private void setMetodoPago(final MetodoPagoDominio metodoPago) {
		this.metodoPago = UtilObjeto.obtenerValorDefecto(metodoPago, new MetodoPagoDominio.Builder().build());
	}

	public CajaDominio getCaja() {
		return caja;
	}

	private void setCaja(final CajaDominio caja) {
		this.caja = UtilObjeto.obtenerValorDefecto(caja, new CajaDominio.Builder().build());
	}

	public static class Builder {

		private UUID id;
		private LocalDate fechaPago;
		private LocalTime horaPago;
		private BigDecimal valorPagado;
		private String estadoPago;
		private PedidoDominio pedido;
		private MetodoPagoDominio metodoPago;
		private CajaDominio caja;

		public Builder id(final UUID id) {
			this.id = id;
			return this;
		}

		public Builder fechaPago(final LocalDate fechaPago) {
			this.fechaPago = fechaPago;
			return this;
		}

		public Builder horaPago(final LocalTime horaPago) {
			this.horaPago = horaPago;
			return this;
		}

		public Builder valorPagado(final BigDecimal valorPagado) {
			BigDecimal valorPagadoSeguro = UtilObjeto.obtenerValorDefecto(valorPagado, VALOR_PAGADO_DEFECTO);
			this.valorPagado = valorPagadoSeguro.compareTo(BigDecimal.ZERO) < 0
					? VALOR_PAGADO_DEFECTO
					: valorPagadoSeguro;
			return this;
		}

		public Builder estadoPago(final String estadoPago) {
			this.estadoPago = UtilTexto.aplicarTrim(estadoPago);
			return this;
		}

		public Builder pedido(final PedidoDominio pedido) {
			this.pedido = pedido;
			return this;
		}

		public Builder metodoPago(final MetodoPagoDominio metodoPago) {
			this.metodoPago = metodoPago;
			return this;
		}

		public Builder caja(final CajaDominio caja) {
			this.caja = caja;
			return this;
		}

		public PagoDominio build() {
			return new PagoDominio(this);
		}
	}
}
