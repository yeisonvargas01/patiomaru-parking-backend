package co.edu.uco.patiomaruparking.negocio.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import co.edu.uco.patiomaruparking.transversal.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.UtilUUID;

public class PedidoDominio {

	private static final LocalDate FECHA_DEFECTO = LocalDate.of(1900, 1, 1);
	private static final LocalTime HORA_DEFECTO = LocalTime.of(0, 0);
	private static final BigDecimal TOTAL_PEDIDO_DEFECTO = BigDecimal.ZERO;

	private UUID id;
	private LocalDate fechaRegistro;
	private LocalTime horaRegistro;
	private String tipoAtencion;
	private String estado;
	private BigDecimal totalPedido;
	private MesaDominio mesa;
	private ClienteDominio cliente;
	private EmpleadoDominio empleado;

	private PedidoDominio(final Builder builder) {
		setId(builder.id);
		setFechaRegistro(builder.fechaRegistro);
		setHoraRegistro(builder.horaRegistro);
		setTipoAtencion(builder.tipoAtencion);
		setEstado(builder.estado);
		setTotalPedido(builder.totalPedido);
		setMesa(builder.mesa);
		setCliente(builder.cliente);
		setEmpleado(builder.empleado);
	}

	public UUID getId() {
		return id;
	}

	private void setId(final UUID id) {
		this.id = UtilUUID.obtenerValorDefecto(id);
	}

	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}

	private void setFechaRegistro(final LocalDate fechaRegistro) {
		this.fechaRegistro = UtilObjeto.obtenerValorDefecto(fechaRegistro, FECHA_DEFECTO);
	}

	public LocalTime getHoraRegistro() {
		return horaRegistro;
	}

	private void setHoraRegistro(final LocalTime horaRegistro) {
		this.horaRegistro = UtilObjeto.obtenerValorDefecto(horaRegistro, HORA_DEFECTO);
	}

	public String getTipoAtencion() {
		return tipoAtencion;
	}

	private void setTipoAtencion(final String tipoAtencion) {
		this.tipoAtencion = UtilTexto.aplicarTrim(tipoAtencion);
	}

	public String getEstado() {
		return estado;
	}

	private void setEstado(final String estado) {
		this.estado = UtilTexto.aplicarTrim(estado);
	}

	public BigDecimal getTotalPedido() {
		return totalPedido;
	}

	private void setTotalPedido(final BigDecimal totalPedido) {
		BigDecimal totalPedidoSeguro = UtilObjeto.obtenerValorDefecto(totalPedido, TOTAL_PEDIDO_DEFECTO);
		this.totalPedido = totalPedidoSeguro.compareTo(BigDecimal.ZERO) < 0 ? TOTAL_PEDIDO_DEFECTO : totalPedidoSeguro;
	}

	public MesaDominio getMesa() {
		return mesa;
	}

	private void setMesa(final MesaDominio mesa) {
		this.mesa = UtilObjeto.obtenerValorDefecto(mesa, new MesaDominio.Builder().build());
	}

	public ClienteDominio getCliente() {
		return cliente;
	}

	private void setCliente(final ClienteDominio cliente) {
		this.cliente = UtilObjeto.obtenerValorDefecto(cliente, new ClienteDominio.Builder().build());
	}

	public EmpleadoDominio getEmpleado() {
		return empleado;
	}

	private void setEmpleado(final EmpleadoDominio empleado) {
		this.empleado = UtilObjeto.obtenerValorDefecto(empleado, new EmpleadoDominio.Builder().build());
	}

	public static class Builder {

		private UUID id;
		private LocalDate fechaRegistro;
		private LocalTime horaRegistro;
		private String tipoAtencion;
		private String estado;
		private BigDecimal totalPedido;
		private MesaDominio mesa;
		private ClienteDominio cliente;
		private EmpleadoDominio empleado;

		public Builder id(final UUID id) {
			this.id = id;
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
			this.tipoAtencion = UtilTexto.aplicarTrim(tipoAtencion);
			return this;
		}

		public Builder estado(final String estado) {
			this.estado = UtilTexto.aplicarTrim(estado);
			return this;
		}

		public Builder totalPedido(final BigDecimal totalPedido) {
			BigDecimal totalPedidoSeguro = UtilObjeto.obtenerValorDefecto(totalPedido, TOTAL_PEDIDO_DEFECTO);
			this.totalPedido = totalPedidoSeguro.compareTo(BigDecimal.ZERO) < 0 ? TOTAL_PEDIDO_DEFECTO : totalPedidoSeguro;
			return this;
		}

		public Builder mesa(final MesaDominio mesa) {
			this.mesa = mesa;
			return this;
		}

		public Builder cliente(final ClienteDominio cliente) {
			this.cliente = cliente;
			return this;
		}

		public Builder empleado(final EmpleadoDominio empleado) {
			this.empleado = empleado;
			return this;
		}

		public PedidoDominio build() {
			return new PedidoDominio(this);
		}
	}
}
