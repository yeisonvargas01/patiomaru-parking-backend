package co.edu.uco.patiomaruparking.negocio.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class PedidoDominio {

    private static final String ESTADO_DEFECTO = "Registrado";
    private static final BigDecimal TOTAL_PEDIDO_DEFECTO = BigDecimal.ZERO;

    private String codigoPedido = UtilTexto.TEXTO_VACIO;
    private LocalDate fechaRegistro = obtenerFechaRegistroDefecto();
    private LocalTime horaRegistro = obtenerHoraRegistroDefecto();
    private String tipoAtencion = UtilTexto.TEXTO_VACIO;
    private String estado = ESTADO_DEFECTO;
    private BigDecimal totalPedido = TOTAL_PEDIDO_DEFECTO;
    private MesaDominio mesa = MesaDominio.builder().build();
    private ClienteDominio cliente = ClienteDominio.builder().build();
    private EmpleadoDominio empleado = EmpleadoDominio.builder().build();
    private List<DetallePedidoDominio> detalles = new ArrayList<>();

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
        this.codigoPedido = UtilTexto.aplicarTrim(codigoPedido);
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    private void setFechaRegistro(final LocalDate fechaRegistro) {
        this.fechaRegistro = UtilObjeto.obtenerValorDefecto(
                fechaRegistro,
                obtenerFechaRegistroDefecto());
    }

    public LocalTime getHoraRegistro() {
        return horaRegistro;
    }

    private void setHoraRegistro(final LocalTime horaRegistro) {
        this.horaRegistro = UtilObjeto.obtenerValorDefecto(
                horaRegistro,
                obtenerHoraRegistroDefecto());
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

    public MesaDominio getMesa() {
        return mesa;
    }

    private void setMesa(final MesaDominio mesa) {
        this.mesa = UtilObjeto.obtenerValorDefecto(
                mesa,
                MesaDominio.builder().build());
    }

    public ClienteDominio getCliente() {
        return cliente;
    }

    private void setCliente(final ClienteDominio cliente) {
        this.cliente = UtilObjeto.obtenerValorDefecto(
                cliente,
                ClienteDominio.builder().build());
    }

    public EmpleadoDominio getEmpleado() {
        return empleado;
    }

    private void setEmpleado(final EmpleadoDominio empleado) {
        this.empleado = UtilObjeto.obtenerValorDefecto(
                empleado,
                EmpleadoDominio.builder().build());
    }

    public List<DetallePedidoDominio> getDetalles() {
        return detalles;
    }

    private void setDetalles(final List<DetallePedidoDominio> detalles) {
        this.detalles = new ArrayList<>(
                UtilObjeto.obtenerValorDefecto(
                        detalles,
                        List.<DetallePedidoDominio>of()));
    }

    public boolean tieneCodigo() {
        return UtilTexto.tieneTexto(codigoPedido);
    }

    public boolean tieneTipoAtencion() {
        return UtilTexto.tieneTexto(tipoAtencion);
    }

    public boolean tieneEstado() {
        return UtilTexto.tieneTexto(estado);
    }

    public boolean tieneCliente() {
        return cliente.tieneCodigo();
    }

    public boolean tieneEmpleado() {
        return empleado.tieneCodigo();
    }

    public boolean tieneMesa() {
        return mesa.tieneCodigo();
    }

    public boolean tieneDetalles() {
        return !detalles.isEmpty();
    }

    public boolean tieneTotalPedidoValido() {
        return totalPedido.compareTo(TOTAL_PEDIDO_DEFECTO) >= 0;
    }

    public boolean esAtencionEnMesa() {
        return "Mesa".equalsIgnoreCase(tipoAtencion);
    }

    public boolean esAtencionParaLlevar() {
        return "Para llevar".equalsIgnoreCase(tipoAtencion);
    }

    public boolean estaCancelado() {
        return "Cancelado".equalsIgnoreCase(estado);
    }

    public boolean estaRegistrado() {
        return "Registrado".equalsIgnoreCase(estado);
    }

    public static class Builder {

        private String codigoPedido = UtilTexto.TEXTO_VACIO;
        private LocalDate fechaRegistro = obtenerFechaRegistroDefecto();
        private LocalTime horaRegistro = obtenerHoraRegistroDefecto();
        private String tipoAtencion = UtilTexto.TEXTO_VACIO;
        private String estado = ESTADO_DEFECTO;
        private BigDecimal totalPedido = TOTAL_PEDIDO_DEFECTO;
        private MesaDominio mesa = MesaDominio.builder().build();
        private ClienteDominio cliente = ClienteDominio.builder().build();
        private EmpleadoDominio empleado = EmpleadoDominio.builder().build();
        private List<DetallePedidoDominio> detalles = new ArrayList<>();

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
                    obtenerFechaRegistroDefecto());
            return this;
        }

        public Builder horaRegistro(final LocalTime horaRegistro) {
            this.horaRegistro = UtilObjeto.obtenerValorDefecto(
                    horaRegistro,
                    obtenerHoraRegistroDefecto());
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

        public Builder mesa(final MesaDominio mesa) {
            this.mesa = UtilObjeto.obtenerValorDefecto(
                    mesa,
                    MesaDominio.builder().build());
            return this;
        }

        public Builder cliente(final ClienteDominio cliente) {
            this.cliente = UtilObjeto.obtenerValorDefecto(
                    cliente,
                    ClienteDominio.builder().build());
            return this;
        }

        public Builder empleado(final EmpleadoDominio empleado) {
            this.empleado = UtilObjeto.obtenerValorDefecto(
                    empleado,
                    EmpleadoDominio.builder().build());
            return this;
        }

        public Builder detalles(final List<DetallePedidoDominio> detalles) {
            this.detalles = new ArrayList<>(
                    UtilObjeto.obtenerValorDefecto(
                            detalles,
                            List.<DetallePedidoDominio>of()));
            return this;
        }

        public PedidoDominio build() {
            return new PedidoDominio(this);
        }
    }

    private static LocalDate obtenerFechaRegistroDefecto() {
        return LocalDate.now();
    }

    private static LocalTime obtenerHoraRegistroDefecto() {
        return LocalTime.now();
    }
}