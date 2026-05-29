package co.edu.uco.patiomaruparking.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class PedidoDTO {

    private static final String ESTADO_DEFECTO = "Registrado";
    private static final BigDecimal TOTAL_PEDIDO_DEFECTO = BigDecimal.ZERO;

    private String codigoPedido = UtilTexto.TEXTO_VACIO;
    private LocalDate fechaRegistro = LocalDate.now();
    private LocalTime horaRegistro = LocalTime.now();
    private String tipoAtencion = UtilTexto.TEXTO_VACIO;
    private String estado = ESTADO_DEFECTO;
    private BigDecimal totalPedido = TOTAL_PEDIDO_DEFECTO;
    private MesaDTO mesa = MesaDTO.builder().build();
    private ClienteDTO cliente = ClienteDTO.builder().build();
    private EmpleadoDTO empleado = EmpleadoDTO.builder().build();
    private List<DetallePedidoDTO> detalles = new ArrayList<>();

    public PedidoDTO() {
        super();
    }

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

    public void setCodigoPedido(final String codigoPedido) {
        this.codigoPedido = UtilTexto.aplicarTrim(codigoPedido);
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(final LocalDate fechaRegistro) {
        this.fechaRegistro = UtilObjeto.obtenerValorDefecto(
                fechaRegistro,
                LocalDate.now());
    }

    public LocalTime getHoraRegistro() {
        return horaRegistro;
    }

    public void setHoraRegistro(final LocalTime horaRegistro) {
        this.horaRegistro = UtilObjeto.obtenerValorDefecto(
                horaRegistro,
                LocalTime.now());
    }

    public String getTipoAtencion() {
        return tipoAtencion;
    }

    public void setTipoAtencion(final String tipoAtencion) {
        this.tipoAtencion = UtilTexto.aplicarTrim(tipoAtencion);
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(final String estado) {
        this.estado = UtilTexto.obtenerValorDefecto(
                UtilTexto.aplicarTrim(estado),
                ESTADO_DEFECTO);
    }

    public BigDecimal getTotalPedido() {
        return totalPedido;
    }

    public void setTotalPedido(final BigDecimal totalPedido) {
        this.totalPedido = UtilObjeto.obtenerValorDefecto(
                totalPedido,
                TOTAL_PEDIDO_DEFECTO);
    }

    public MesaDTO getMesa() {
        return mesa;
    }

    public void setMesa(final MesaDTO mesa) {
        this.mesa = UtilObjeto.obtenerValorDefecto(
                mesa,
                MesaDTO.builder().build());
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(final ClienteDTO cliente) {
        this.cliente = UtilObjeto.obtenerValorDefecto(
                cliente,
                ClienteDTO.builder().build());
    }

    public EmpleadoDTO getEmpleado() {
        return empleado;
    }

    public void setEmpleado(final EmpleadoDTO empleado) {
        this.empleado = UtilObjeto.obtenerValorDefecto(
                empleado,
                EmpleadoDTO.builder().build());
    }

    public List<DetallePedidoDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(final List<DetallePedidoDTO> detalles) {
        this.detalles = new ArrayList<>(
                UtilObjeto.obtenerValorDefecto(
                        detalles,
                        List.<DetallePedidoDTO>of()));
    }

    public static class Builder {

        private String codigoPedido = UtilTexto.TEXTO_VACIO;
        private LocalDate fechaRegistro = LocalDate.now();
        private LocalTime horaRegistro = LocalTime.now();
        private String tipoAtencion = UtilTexto.TEXTO_VACIO;
        private String estado = ESTADO_DEFECTO;
        private BigDecimal totalPedido = TOTAL_PEDIDO_DEFECTO;
        private MesaDTO mesa = MesaDTO.builder().build();
        private ClienteDTO cliente = ClienteDTO.builder().build();
        private EmpleadoDTO empleado = EmpleadoDTO.builder().build();
        private List<DetallePedidoDTO> detalles = new ArrayList<>();

        private Builder() {
            super();
        }

        public Builder codigoPedido(final String codigoPedido) {
            this.codigoPedido = UtilTexto.aplicarTrim(codigoPedido);
            return this;
        }

        public Builder fechaRegistro(final LocalDate fechaRegistro) {
            this.fechaRegistro = UtilObjeto.obtenerValorDefecto(
                    fechaRegistro,
                    LocalDate.now());
            return this;
        }

        public Builder horaRegistro(final LocalTime horaRegistro) {
            this.horaRegistro = UtilObjeto.obtenerValorDefecto(
                    horaRegistro,
                    LocalTime.now());
            return this;
        }

        public Builder tipoAtencion(final String tipoAtencion) {
            this.tipoAtencion = UtilTexto.aplicarTrim(tipoAtencion);
            return this;
        }

        public Builder estado(final String estado) {
            this.estado = UtilTexto.obtenerValorDefecto(
                    UtilTexto.aplicarTrim(estado),
                    ESTADO_DEFECTO);
            return this;
        }

        public Builder totalPedido(final BigDecimal totalPedido) {
            this.totalPedido = UtilObjeto.obtenerValorDefecto(
                    totalPedido,
                    TOTAL_PEDIDO_DEFECTO);
            return this;
        }

        public Builder mesa(final MesaDTO mesa) {
            this.mesa = UtilObjeto.obtenerValorDefecto(
                    mesa,
                    MesaDTO.builder().build());
            return this;
        }

        public Builder cliente(final ClienteDTO cliente) {
            this.cliente = UtilObjeto.obtenerValorDefecto(
                    cliente,
                    ClienteDTO.builder().build());
            return this;
        }

        public Builder empleado(final EmpleadoDTO empleado) {
            this.empleado = UtilObjeto.obtenerValorDefecto(
                    empleado,
                    EmpleadoDTO.builder().build());
            return this;
        }

        public Builder detalles(final List<DetallePedidoDTO> detalles) {
            this.detalles = new ArrayList<>(
                    UtilObjeto.obtenerValorDefecto(
                            detalles,
                            List.<DetallePedidoDTO>of()));
            return this;
        }

        public PedidoDTO build() {
            return new PedidoDTO(this);
        }
    }
}