package co.edu.uco.patiomaruparking.entidad;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoEntidad {

	private String codigoPedido;
	private LocalDate fechaRegistro;
	private LocalTime horaRegistro;
	private String tipoAtencion;
	private String estado;
	private BigDecimal totalPedido;
	private MesaEntidad mesa;
	private ClienteEntidad cliente;
	private EmpleadoEntidad empleado;
	private List<DetallePedidoEntidad> detalles;
	private PagoEntidad pago;

	private PedidoEntidad(final Builder builder) {
		setCodigoPedido(builder.codigoPedido);
		setFechaRegistro(builder.fechaRegistro);
		setHoraRegistro(builder.horaRegistro);
		setTipoAtencion(builder.tipoAtencion);
		setEstado(builder.estado);
		setTotalPedido(builder.totalPedido);
		setMesa(builder.mesa);
		setCliente(builder.cliente);
		setEmpleado(builder.empleado);
		setDetalles(builder.detalles);
		setPago(builder.pago);
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getCodigoPedido() {
		return codigoPedido;
	}

	private void setCodigoPedido(final String codigoPedido) {
		this.codigoPedido = aplicarTrim(codigoPedido);
	}

	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}

	private void setFechaRegistro(final LocalDate fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public LocalTime getHoraRegistro() {
		return horaRegistro;
	}

	private void setHoraRegistro(final LocalTime horaRegistro) {
		this.horaRegistro = horaRegistro;
	}

	public String getTipoAtencion() {
		return tipoAtencion;
	}

	private void setTipoAtencion(final String tipoAtencion) {
		this.tipoAtencion = aplicarTrim(tipoAtencion);
	}

	public String getEstado() {
		return estado;
	}

	private void setEstado(final String estado) {
		this.estado = aplicarTrim(estado);
	}

	public BigDecimal getTotalPedido() {
		return totalPedido;
	}

	private void setTotalPedido(final BigDecimal totalPedido) {
		this.totalPedido = totalPedido;
	}

	public MesaEntidad getMesa() {
		return mesa;
	}

	private void setMesa(final MesaEntidad mesa) {
		this.mesa = mesa == null ? MesaEntidad.builder().build() : mesa;
	}

	public ClienteEntidad getCliente() {
		return cliente;
	}

	private void setCliente(final ClienteEntidad cliente) {
		this.cliente = cliente == null ? ClienteEntidad.builder().build() : cliente;
	}

	public EmpleadoEntidad getEmpleado() {
		return empleado;
	}

	private void setEmpleado(final EmpleadoEntidad empleado) {
		this.empleado = empleado == null ? EmpleadoEntidad.builder().build() : empleado;
	}

	public List<DetallePedidoEntidad> getDetalles() {
		return detalles;
	}

	private void setDetalles(final List<DetallePedidoEntidad> detalles) {
		this.detalles = detalles == null ? new ArrayList<>() : detalles;
	}

	public PagoEntidad getPago() {
		return pago;
	}

	private void setPago(final PagoEntidad pago) {
		this.pago = pago == null ? PagoEntidad.builder().build() : pago;
	}

	public static class Builder {

		private String codigoPedido;
		private LocalDate fechaRegistro;
		private LocalTime horaRegistro;
		private String tipoAtencion;
		private String estado;
		private BigDecimal totalPedido;
		private MesaEntidad mesa;
		private ClienteEntidad cliente;
		private EmpleadoEntidad empleado;
		private List<DetallePedidoEntidad> detalles;
		private PagoEntidad pago;

		private Builder() {
			super();
		}

		public Builder codigoPedido(final String codigoPedido) {
			this.codigoPedido = aplicarTrim(codigoPedido);
			return this;
		}

		public Builder fechaRegistro(final LocalDate fechaRegistro) {
			this.fechaRegistro = fechaRegistro;
			return this;
		}

		public Builder horaRegistro(final LocalTime horaRegistro) {
			this.horaRegistro = horaRegistro;
			return this;
		}

		public Builder tipoAtencion(final String tipoAtencion) {
			this.tipoAtencion = aplicarTrim(tipoAtencion);
			return this;
		}

		public Builder estado(final String estado) {
			this.estado = aplicarTrim(estado);
			return this;
		}

		public Builder totalPedido(final BigDecimal totalPedido) {
			this.totalPedido = totalPedido;
			return this;
		}

		public Builder mesa(final MesaEntidad mesa) {
			this.mesa = mesa == null ? MesaEntidad.builder().build() : mesa;
			return this;
		}

		public Builder cliente(final ClienteEntidad cliente) {
			this.cliente = cliente == null ? ClienteEntidad.builder().build() : cliente;
			return this;
		}

		public Builder empleado(final EmpleadoEntidad empleado) {
			this.empleado = empleado == null ? EmpleadoEntidad.builder().build() : empleado;
			return this;
		}

		public Builder detalles(final List<DetallePedidoEntidad> detalles) {
			this.detalles = detalles == null ? new ArrayList<>() : detalles;
			return this;
		}

		public Builder pago(final PagoEntidad pago) {
			this.pago = pago == null ? PagoEntidad.builder().build() : pago;
			return this;
		}

		public PedidoEntidad build() {
			return new PedidoEntidad(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}
