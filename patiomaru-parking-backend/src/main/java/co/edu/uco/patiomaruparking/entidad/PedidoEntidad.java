package co.edu.uco.patiomaruparking.entidad;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class PedidoEntidad {

    private static final String ESTADO_DEFECTO = "Registrado";
    private static final BigDecimal TOTAL_PEDIDO_DEFECTO = BigDecimal.ZERO;

    private String codigoPedido = UtilTexto.TEXTO_VACIO;
    private LocalDate fechaRegistro = obtenerFechaDefecto();
    private LocalTime horaRegistro = obtenerHoraDefecto();
    private String tipoAtencion = UtilTexto.TEXTO_VACIO;
    private String estado = ESTADO_DEFECTO;
    private BigDecimal totalPedido = TOTAL_PEDIDO_DEFECTO;
    private MesaEntidad mesa = MesaEntidad.builder().build();
    private ClienteEntidad cliente = ClienteEntidad.builder().build();
    private EmpleadoEntidad empleado = EmpleadoEntidad.builder().build();
    private List<DetallePedidoEntidad> detalles = new ArrayList<>();

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
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    private void setCodigoPedido(final String codigoPedido) {
        this.codigoPedido = UtilTexto.aplicarTrim(codigoPedido);
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    private void setFechaRegistro(final LocalDate fechaRegistro) {
        this.fechaRegistro = UtilObjeto.obtenerValorDefecto(
                fechaRegistro,
                obtenerFechaDefecto());
    }

    public LocalTime getHoraRegistro() {
        return horaRegistro;
    }

    private void setHoraRegistro(final LocalTime horaRegistro) {
        this.horaRegistro = UtilObjeto.obtenerValorDefecto(
                horaRegistro,
                obtenerHoraDefecto());
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
        var estadoSeguro = UtilTexto.aplicarTrim(estado);

        this.estado = UtilTexto.esVacio(estadoSeguro)
                ? ESTADO_DEFECTO
                : estadoSeguro;
    }

    public BigDecimal getTotalPedido() {
        return totalPedido;
    }

    private void setTotalPedido(final BigDecimal totalPedido) {
        this.totalPedido = UtilObjeto.obtenerValorDefecto(
                totalPedido,
                TOTAL_PEDIDO_DEFECTO);
    }

    public MesaEntidad getMesa() {
        return mesa;
    }

    private void setMesa(final MesaEntidad mesa) {
        this.mesa = UtilObjeto.obtenerValorDefecto(
                mesa,
                MesaEntidad.builder().build());
    }

    public ClienteEntidad getCliente() {
        return cliente;
    }

    private void setCliente(final ClienteEntidad cliente) {
        this.cliente = UtilObjeto.obtenerValorDefecto(
                cliente,
                ClienteEntidad.builder().build());
    }

    public EmpleadoEntidad getEmpleado() {
        return empleado;
    }

    private void setEmpleado(final EmpleadoEntidad empleado) {
        this.empleado = UtilObjeto.obtenerValorDefecto(
                empleado,
                EmpleadoEntidad.builder().build());
    }

    public List<DetallePedidoEntidad> getDetalles() {
        return detalles;
    }

    private void setDetalles(final List<DetallePedidoEntidad> detalles) {
        this.detalles = new ArrayList<>(
                UtilObjeto.obtenerValorDefecto(
                        detalles,
                        List.<DetallePedidoEntidad>of()));
    }

    public static class Builder {

        private String codigoPedido = UtilTexto.TEXTO_VACIO;
        private LocalDate fechaRegistro = obtenerFechaDefecto();
        private LocalTime horaRegistro = obtenerHoraDefecto();
        private String tipoAtencion = UtilTexto.TEXTO_VACIO;
        private String estado = ESTADO_DEFECTO;
        private BigDecimal totalPedido = TOTAL_PEDIDO_DEFECTO;
        private MesaEntidad mesa = MesaEntidad.builder().build();
        private ClienteEntidad cliente = ClienteEntidad.builder().build();
        private EmpleadoEntidad empleado = EmpleadoEntidad.builder().build();
        private List<DetallePedidoEntidad> detalles = new ArrayList<>();

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
                    obtenerFechaDefecto());
            return this;
        }

        public Builder horaRegistro(final LocalTime horaRegistro) {
            this.horaRegistro = UtilObjeto.obtenerValorDefecto(
                    horaRegistro,
                    obtenerHoraDefecto());
            return this;
        }

        public Builder tipoAtencion(final String tipoAtencion) {
            this.tipoAtencion = UtilTexto.aplicarTrim(tipoAtencion);
            return this;
        }

        public Builder estado(final String estado) {
            var estadoSeguro = UtilTexto.aplicarTrim(estado);

            this.estado = UtilTexto.esVacio(estadoSeguro)
                    ? ESTADO_DEFECTO
                    : estadoSeguro;

            return this;
        }

        public Builder totalPedido(final BigDecimal totalPedido) {
            this.totalPedido = UtilObjeto.obtenerValorDefecto(
                    totalPedido,
                    TOTAL_PEDIDO_DEFECTO);
            return this;
        }

        public Builder mesa(final MesaEntidad mesa) {
            this.mesa = UtilObjeto.obtenerValorDefecto(
                    mesa,
                    MesaEntidad.builder().build());
            return this;
        }

        public Builder cliente(final ClienteEntidad cliente) {
            this.cliente = UtilObjeto.obtenerValorDefecto(
                    cliente,
                    ClienteEntidad.builder().build());
            return this;
        }

        public Builder empleado(final EmpleadoEntidad empleado) {
            this.empleado = UtilObjeto.obtenerValorDefecto(
                    empleado,
                    EmpleadoEntidad.builder().build());
            return this;
        }

        public Builder detalles(final List<DetallePedidoEntidad> detalles) {
            this.detalles = new ArrayList<>(
                    UtilObjeto.obtenerValorDefecto(
                            detalles,
                            List.<DetallePedidoEntidad>of()));
            return this;
        }

        public PedidoEntidad build() {
            return new PedidoEntidad(this);
        }
    }

    private static LocalDate obtenerFechaDefecto() {
        return LocalDate.now();
    }

    private static LocalTime obtenerHoraDefecto() {
        return LocalTime.now();
    }
}