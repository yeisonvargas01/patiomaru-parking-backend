package co.edu.uco.patiomaruparking.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDTO {

	private String codigoPedido;
	private LocalDate fechaRegistro;
	private LocalTime horaRegistro;
	private String tipoAtencion;
	private String estado;
	private BigDecimal totalPedido;
	private MesaDTO mesa;
	private ClienteDTO cliente;
	private EmpleadoDTO empleado;
	private List<DetallePedidoDTO> detalles;

	private PedidoDTO(final Builder builder) {
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

	public MesaDTO getMesa() {
		return mesa;
	}

	private void setMesa(final MesaDTO mesa) {
		this.mesa = mesa;
	}

	public ClienteDTO getCliente() {
		return cliente;
	}

	private void setCliente(final ClienteDTO cliente) {
		this.cliente = cliente;
	}

	public EmpleadoDTO getEmpleado() {
		return empleado;
	}

	private void setEmpleado(final EmpleadoDTO empleado) {
		this.empleado = empleado;
	}

	public List<DetallePedidoDTO> getDetalles() {
		return detalles;
	}

	private void setDetalles(final List<DetallePedidoDTO> detalles) {
		this.detalles = detalles == null ? new ArrayList<>() : detalles;
	}


	public static class Builder {

		private String codigoPedido;
		private LocalDate fechaRegistro;
		private LocalTime horaRegistro;
		private String tipoAtencion;
		private String estado;
		private BigDecimal totalPedido;
		private MesaDTO mesa;
		private ClienteDTO cliente;
		private EmpleadoDTO empleado;
		private List<DetallePedidoDTO> detalles;


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

		public Builder mesa(final MesaDTO mesa) {
			this.mesa = mesa;
			return this;
		}

		public Builder cliente(final ClienteDTO cliente) {
			this.cliente = cliente;
			return this;
		}

		public Builder empleado(final EmpleadoDTO empleado) {
			this.empleado = empleado;
			return this;
		}

		public Builder detalles(final List<DetallePedidoDTO> detalles) {
			this.detalles = detalles;
			return this;
		}


		public PedidoDTO build() {
			return new PedidoDTO(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}
