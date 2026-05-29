package co.edu.uco.patiomaruparking.datos.dao.sql.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import co.edu.uco.patiomaruparking.datos.dao.PedidoDAO;
import co.edu.uco.patiomaruparking.datos.dao.sql.SQLDAO;
import co.edu.uco.patiomaruparking.entidad.ClienteEntidad;
import co.edu.uco.patiomaruparking.entidad.EmpleadoEntidad;
import co.edu.uco.patiomaruparking.entidad.MesaEntidad;
import co.edu.uco.patiomaruparking.entidad.PedidoEntidad;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class PedidoPostgreSQLDAO extends SQLDAO implements PedidoDAO {

    private static final String ESTADO_CANCELADO = "Cancelado";

    public PedidoPostgreSQLDAO(final Connection conexion) {
        super(conexion);
    }

    @Override
    public void registrar(final PedidoEntidad entidad) {
        final String sentenciaSql = """
                INSERT INTO pedido (
                    codigo_pedido,
                    fecha_registro,
                    hora_registro,
                    tipo_atencion,
                    estado,
                    total_pedido,
                    codigo_mesa,
                    codigo_cliente,
                    codigo_empleado
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        var pedido = UtilObjeto.obtenerValorDefecto(
                entidad,
                PedidoEntidad.builder().build());

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setString(1, pedido.getCodigoPedido());
            sentencia.setObject(2, pedido.getFechaRegistro());
            sentencia.setObject(3, pedido.getHoraRegistro());
            sentencia.setString(4, pedido.getTipoAtencion());
            sentencia.setString(5, pedido.getEstado());
            sentencia.setBigDecimal(6, pedido.getTotalPedido());

            asignarCodigoMesa(sentencia, 7, pedido);

            sentencia.setString(8, pedido.getCliente().getCodigoCliente());
            sentencia.setString(9, pedido.getEmpleado().getCodigoEmpleado());

            sentencia.executeUpdate();

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible registrar la información del pedido.",
                    excepcion);
        }
    }

    @Override
    public List<PedidoEntidad> consultar() {
        return consultar(PedidoEntidad.builder().build());
    }

    @Override
    public PedidoEntidad consultarPorId(final String codigoPedido) {
        final String sentenciaSql = """
                SELECT
                    p.codigo_pedido,
                    p.fecha_registro,
                    p.hora_registro,
                    p.tipo_atencion,
                    p.estado AS estado_pedido,
                    p.total_pedido,

                    m.codigo_mesa,
                    m.nombre AS nombre_mesa,

                    c.codigo_cliente,
                    c.nombre AS nombre_cliente,
                    c.telefono,
                    c.correo_electronico AS correo_cliente,
                    c.estado AS estado_cliente,

                    e.codigo_empleado,
                    e.numero_identificacion,
                    e.primer_nombre,
                    e.primer_apellido,
                    e.segundo_nombre,
                    e.segundo_apellido,
                    e.fecha_nacimiento,
                    e.edad,
                    e.estado AS estado_empleado,
                    e.numero_telefono,
                    e.correo_electronico AS correo_empleado,
                    e.direccion_residencia

                FROM pedido p
                LEFT JOIN mesa m ON p.codigo_mesa = m.codigo_mesa
                INNER JOIN cliente c ON p.codigo_cliente = c.codigo_cliente
                INNER JOIN empleado e ON p.codigo_empleado = e.codigo_empleado
                WHERE LOWER(p.codigo_pedido) = LOWER(?)
                """;

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setString(1, UtilTexto.aplicarTrim(codigoPedido));

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return ensamblarPedido(resultado);
                }
            }

            return PedidoEntidad.builder().build();

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible consultar la información del pedido por identificador.",
                    excepcion);
        }
    }

    @Override
    public List<PedidoEntidad> consultar(final PedidoEntidad filtro) {
        var pedidoFiltro = UtilObjeto.obtenerValorDefecto(
                filtro,
                PedidoEntidad.builder().build());

        var parametros = new ArrayList<Object>();
        var sentenciaSql = new StringBuilder();

        sentenciaSql.append("SELECT ");
        sentenciaSql.append("p.codigo_pedido, ");
        sentenciaSql.append("p.fecha_registro, ");
        sentenciaSql.append("p.hora_registro, ");
        sentenciaSql.append("p.tipo_atencion, ");
        sentenciaSql.append("p.estado AS estado_pedido, ");
        sentenciaSql.append("p.total_pedido, ");

        sentenciaSql.append("m.codigo_mesa, ");
        sentenciaSql.append("m.nombre AS nombre_mesa, ");

        sentenciaSql.append("c.codigo_cliente, ");
        sentenciaSql.append("c.nombre AS nombre_cliente, ");
        sentenciaSql.append("c.telefono, ");
        sentenciaSql.append("c.correo_electronico AS correo_cliente, ");
        sentenciaSql.append("c.estado AS estado_cliente, ");

        sentenciaSql.append("e.codigo_empleado, ");
        sentenciaSql.append("e.numero_identificacion, ");
        sentenciaSql.append("e.primer_nombre, ");
        sentenciaSql.append("e.primer_apellido, ");
        sentenciaSql.append("e.segundo_nombre, ");
        sentenciaSql.append("e.segundo_apellido, ");
        sentenciaSql.append("e.fecha_nacimiento, ");
        sentenciaSql.append("e.edad, ");
        sentenciaSql.append("e.estado AS estado_empleado, ");
        sentenciaSql.append("e.numero_telefono, ");
        sentenciaSql.append("e.correo_electronico AS correo_empleado, ");
        sentenciaSql.append("e.direccion_residencia ");

        sentenciaSql.append("FROM pedido p ");
        sentenciaSql.append("LEFT JOIN mesa m ON p.codigo_mesa = m.codigo_mesa ");
        sentenciaSql.append("INNER JOIN cliente c ON p.codigo_cliente = c.codigo_cliente ");
        sentenciaSql.append("INNER JOIN empleado e ON p.codigo_empleado = e.codigo_empleado ");
        sentenciaSql.append("WHERE 1 = 1 ");

        if (UtilTexto.tieneTexto(pedidoFiltro.getCodigoPedido())) {
            sentenciaSql.append("AND LOWER(p.codigo_pedido) = LOWER(?) ");
            parametros.add(pedidoFiltro.getCodigoPedido());
        }

        if (UtilTexto.tieneTexto(pedidoFiltro.getTipoAtencion())) {
            sentenciaSql.append("AND LOWER(p.tipo_atencion) = LOWER(?) ");
            parametros.add(pedidoFiltro.getTipoAtencion());
        }

        if (UtilTexto.tieneTexto(pedidoFiltro.getMesa().getCodigoMesa())) {
            sentenciaSql.append("AND LOWER(p.codigo_mesa) = LOWER(?) ");
            parametros.add(pedidoFiltro.getMesa().getCodigoMesa());
        }

        if (UtilTexto.tieneTexto(pedidoFiltro.getCliente().getCodigoCliente())) {
            sentenciaSql.append("AND LOWER(p.codigo_cliente) = LOWER(?) ");
            parametros.add(pedidoFiltro.getCliente().getCodigoCliente());
        }

        if (UtilTexto.tieneTexto(pedidoFiltro.getEmpleado().getCodigoEmpleado())) {
            sentenciaSql.append("AND LOWER(p.codigo_empleado) = LOWER(?) ");
            parametros.add(pedidoFiltro.getEmpleado().getCodigoEmpleado());
        }

        sentenciaSql.append("ORDER BY p.fecha_registro DESC, p.hora_registro DESC");

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql.toString())) {

            for (int indice = 0; indice < parametros.size(); indice++) {
                sentencia.setObject(indice + 1, parametros.get(indice));
            }

            try (ResultSet resultado = sentencia.executeQuery()) {
                var pedidos = new ArrayList<PedidoEntidad>();

                while (resultado.next()) {
                    pedidos.add(ensamblarPedido(resultado));
                }

                return pedidos;
            }

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible consultar la información de los pedidos.",
                    excepcion);
        }
    }

    @Override
    public void actualizarEstado(final String codigoPedido, final String estado) {
        final String sentenciaSql = """
                UPDATE pedido
                SET estado = ?
                WHERE codigo_pedido = ?
                """;

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setString(1, UtilTexto.aplicarTrim(estado));
            sentencia.setString(2, UtilTexto.aplicarTrim(codigoPedido));

            sentencia.executeUpdate();

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible actualizar el estado del pedido.",
                    excepcion);
        }
    }

    @Override
    public void cancelar(final String codigoPedido) {
        actualizarEstado(codigoPedido, ESTADO_CANCELADO);
    }

    private void asignarCodigoMesa(
            final PreparedStatement sentencia,
            final int posicion,
            final PedidoEntidad pedido) throws SQLException {

        var codigoMesa = pedido.getMesa().getCodigoMesa();

        if (UtilTexto.tieneTexto(codigoMesa)) {
            sentencia.setString(posicion, codigoMesa);
        } else {
            sentencia.setNull(posicion, Types.VARCHAR);
        }
    }

    private PedidoEntidad ensamblarPedido(final ResultSet resultado) throws SQLException {
        var mesa = MesaEntidad.builder()
                .codigoMesa(resultado.getString("codigo_mesa"))
                .nombre(resultado.getString("nombre_mesa"))
                .build();

        var cliente = ClienteEntidad.builder()
                .codigoCliente(resultado.getString("codigo_cliente"))
                .nombre(resultado.getString("nombre_cliente"))
                .telefono(resultado.getString("telefono"))
                .correoElectronico(resultado.getString("correo_cliente"))
                .estado(resultado.getBoolean("estado_cliente"))
                .build();

        var empleado = EmpleadoEntidad.builder()
                .codigoEmpleado(resultado.getString("codigo_empleado"))
                .numeroIdentificacion(resultado.getString("numero_identificacion"))
                .primerNombre(resultado.getString("primer_nombre"))
                .primerApellido(resultado.getString("primer_apellido"))
                .segundoNombre(resultado.getString("segundo_nombre"))
                .segundoApellido(resultado.getString("segundo_apellido"))
                .fechaNacimiento(resultado.getObject("fecha_nacimiento", LocalDate.class))
                .edad(resultado.getInt("edad"))
                .estado(resultado.getBoolean("estado_empleado"))
                .numeroTelefono(resultado.getString("numero_telefono"))
                .correoElectronico(resultado.getString("correo_empleado"))
                .direccionResidencia(resultado.getString("direccion_residencia"))
                .build();

        return PedidoEntidad.builder()
                .codigoPedido(resultado.getString("codigo_pedido"))
                .fechaRegistro(resultado.getObject("fecha_registro", LocalDate.class))
                .horaRegistro(resultado.getObject("hora_registro", LocalTime.class))
                .tipoAtencion(resultado.getString("tipo_atencion"))
                .estado(resultado.getString("estado_pedido"))
                .totalPedido(resultado.getBigDecimal("total_pedido"))
                .mesa(mesa)
                .cliente(cliente)
                .empleado(empleado)
                .build();
    }
}