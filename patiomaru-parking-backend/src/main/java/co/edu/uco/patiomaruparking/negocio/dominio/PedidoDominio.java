package co.edu.uco.patiomaruparking.negocio.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDominio {

	private String codigoPedido;
	private LocalDate fechaRegistro;
	private LocalTime horaRegistro;
	private String tipoAtencion;
	private String estado;
	private BigDecimal totalPedido;
	private MesaDominio mesa;
	private ClienteDominio cliente;
	private EmpleadoDominio empleado;
	private List<DetallePedidoDominio> detalles;
	

	private PedidoDominio(final Builder builder) {
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

	public MesaDominio getMesa() {
		return mesa;
	}

	private void setMesa(final MesaDominio mesa) {
		this.mesa = mesa == null ? MesaDominio.builder().build() : mesa;
	}

	public ClienteDominio getCliente() {
		return cliente;
	}

	private void setCliente(final ClienteDominio cliente) {
		this.cliente = cliente == null ? ClienteDominio.builder().build() : cliente;
	}

	public EmpleadoDominio getEmpleado() {
		return empleado;
	}

	private void setEmpleado(final EmpleadoDominio empleado) {
		this.empleado = empleado == null ? EmpleadoDominio.builder().build() : empleado;
	}

	public List<DetallePedidoDominio> getDetalles() {
		return detalles;
	}

	private void setDetalles(final List<DetallePedidoDominio> detalles) {
		this.detalles = detalles == null ? new ArrayList<>() : detalles;
	}


	public boolean tieneCodigo() {
		return !codigoPedido.isBlank();
	}

	public boolean tieneTipoAtencion() {
		return !tipoAtencion.isBlank();
	}

	public boolean tieneEstado() {
		return !estado.isBlank();
	}

	public boolean tieneCliente() {
		return cliente != null && cliente.getCodigoCliente() != null && !cliente.getCodigoCliente().isBlank();
	}

	public boolean tieneEmpleado() {
		return empleado != null && empleado.tieneCodigo();
	}

	public boolean tieneMesa() {
		return mesa != null && mesa.tieneCodigo();
	}

	public boolean tieneDetalles() {
		return detalles != null && !detalles.isEmpty();
	}


	public boolean estaCancelado() {
		return "CANCELADO".equalsIgnoreCase(estado);
	}

	public boolean estaRegistrado() {
		return "REGISTRADO".equalsIgnoreCase(estado);
	}

	public BigDecimal calcularTotalPedido() {
		if (!tieneDetalles()) {
			return BigDecimal.ZERO;
		}

		BigDecimal total = BigDecimal.ZERO;

		for (DetallePedidoDominio detalle : detalles) {
			if (detalle != null) {
				total = total.add(detalle.calcularSubtotal());
			}
		}

		return total;
	}

	public PedidoDominio actualizarTotalCalculado() {
		return PedidoDominio.builder()
				.codigoPedido(getCodigoPedido())
				.fechaRegistro(getFechaRegistro())
				.horaRegistro(getHoraRegistro())
				.tipoAtencion(getTipoAtencion())
				.estado(getEstado())
				.totalPedido(calcularTotalPedido())
				.mesa(getMesa())
				.cliente(getCliente())
				.empleado(getEmpleado())
				.detalles(getDetalles())
				.build();
	}

	public static class Builder {

		private String codigoPedido;
		private LocalDate fechaRegistro;
		private LocalTime horaRegistro;
		private String tipoAtencion;
		private String estado;
		private BigDecimal totalPedido;
		private MesaDominio mesa;
		private ClienteDominio cliente;
		private EmpleadoDominio empleado;
		private List<DetallePedidoDominio> detalles;
	

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

		public Builder mesa(final MesaDominio mesa) {
			this.mesa = mesa == null ? MesaDominio.builder().build() : mesa;
			return this;
		}

		public Builder cliente(final ClienteDominio cliente) {
			this.cliente = cliente == null ? ClienteDominio.builder().build() : cliente;
			return this;
		}

		public Builder empleado(final EmpleadoDominio empleado) {
			this.empleado = empleado == null ? EmpleadoDominio.builder().build() : empleado;
			return this;
		}

		public Builder detalles(final List<DetallePedidoDominio> detalles) {
			this.detalles = detalles == null ? new ArrayList<>() : detalles;
			return this;
		}


		public PedidoDominio build() {
			return new PedidoDominio(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}