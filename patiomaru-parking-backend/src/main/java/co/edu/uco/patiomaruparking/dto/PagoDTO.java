package co.edu.uco.patiomaruparking.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class PagoDTO {

	private String codigoPago;
	private String codigoPedido;
	private CajaDTO caja;
	private MetodoPagoDTO metodoPago;
	private LocalDate fechaPago;
	private LocalTime horaPago;
	private BigDecimal valorPagado;
	private String estadoPago;

	private PagoDTO(final Builder builder) {
		setCodigoPago(builder.codigoPago);
		setCodigoPedido(builder.codigoPedido);
		setCaja(builder.caja);
		setMetodoPago(builder.metodoPago);
		setFechaPago(builder.fechaPago);
		setHoraPago(builder.horaPago);
		setValorPagado(builder.valorPagado);
		setEstadoPago(builder.estadoPago);
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getCodigoPago() {
		return codigoPago;
	}

	private void setCodigoPago(final String codigoPago) {
		this.codigoPago = aplicarTrim(codigoPago);
	}

	public String getCodigoPedido() {
		return codigoPedido;
	}

	private void setCodigoPedido(final String codigoPedido) {
		this.codigoPedido = aplicarTrim(codigoPedido);
	}

	public CajaDTO getCaja() {
		return caja;
	}

	private void setCaja(final CajaDTO caja) {
		this.caja = caja == null ? CajaDTO.builder().build() : caja;
	}

	public MetodoPagoDTO getMetodoPago() {
		return metodoPago;
	}

	private void setMetodoPago(final MetodoPagoDTO metodoPago) {
		this.metodoPago = metodoPago == null ? MetodoPagoDTO.builder().build() : metodoPago;
	}

	public LocalDate getFechaPago() {
		return fechaPago;
	}

	private void setFechaPago(final LocalDate fechaPago) {
		this.fechaPago = fechaPago;
	}

	public LocalTime getHoraPago() {
		return horaPago;
	}

	private void setHoraPago(final LocalTime horaPago) {
		this.horaPago = horaPago;
	}

	public BigDecimal getValorPagado() {
		return valorPagado;
	}

	private void setValorPagado(final BigDecimal valorPagado) {
		this.valorPagado = valorPagado;
	}

	public String getEstadoPago() {
		return estadoPago;
	}

	private void setEstadoPago(final String estadoPago) {
		this.estadoPago = aplicarTrim(estadoPago);
	}

	public static class Builder {

		private String codigoPago;
		private String codigoPedido;
		private CajaDTO caja;
		private MetodoPagoDTO metodoPago;
		private LocalDate fechaPago;
		private LocalTime horaPago;
		private BigDecimal valorPagado;
		private String estadoPago;

		private Builder() {
			super();
		}

		public Builder codigoPago(final String codigoPago) {
			this.codigoPago = aplicarTrim(codigoPago);
			return this;
		}

		public Builder codigoPedido(final String codigoPedido) {
			this.codigoPedido = aplicarTrim(codigoPedido);
			return this;
		}

		public Builder caja(final CajaDTO caja) {
			this.caja = caja == null ? CajaDTO.builder().build() : caja;
			return this;
		}

		public Builder metodoPago(final MetodoPagoDTO metodoPago) {
			this.metodoPago = metodoPago == null ? MetodoPagoDTO.builder().build() : metodoPago;
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
			this.valorPagado = valorPagado;
			return this;
		}

		public Builder estadoPago(final String estadoPago) {
			this.estadoPago = aplicarTrim(estadoPago);
			return this;
		}

		public PagoDTO build() {
			return new PagoDTO(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}
