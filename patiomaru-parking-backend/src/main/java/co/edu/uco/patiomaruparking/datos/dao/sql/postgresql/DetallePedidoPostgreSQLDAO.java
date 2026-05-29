package co.edu.uco.patiomaruparking.datos.dao.sql.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uco.patiomaruparking.datos.dao.DetallePedidoDAO;
import co.edu.uco.patiomaruparking.datos.dao.sql.SQLDAO;
import co.edu.uco.patiomaruparking.entidad.CategoriaEntidad;
import co.edu.uco.patiomaruparking.entidad.DetallePedidoEntidad;
import co.edu.uco.patiomaruparking.entidad.PlatoEntidad;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class DetallePedidoPostgreSQLDAO extends SQLDAO implements DetallePedidoDAO {

    private static final int CANTIDAD_DEFECTO = 0;

    public DetallePedidoPostgreSQLDAO(final Connection conexion) {
        super(conexion);
    }

    @Override
    public void registrar(final DetallePedidoEntidad entidad) {
        final String sentenciaSql = """
                INSERT INTO detalle_pedido (
                    codigo_detalle_pedido,
                    cantidad,
                    subtotal,
                    codigo_pedido,
                    codigo_plato
                ) VALUES (?, ?, ?, ?, ?)
                """;

        var detalle = UtilObjeto.obtenerValorDefecto(
                entidad,
                DetallePedidoEntidad.builder().build());

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setString(1, detalle.getCodigoDetallePedido());
            sentencia.setInt(2, detalle.getCantidad());
            sentencia.setBigDecimal(3, detalle.getSubtotal());
            sentencia.setString(4, detalle.getCodigoPedido());
            sentencia.setString(5, detalle.getPlato().getCodigoPlato());

            sentencia.executeUpdate();

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible registrar la información del detalle del pedido.",
                    excepcion);
        }
    }

    @Override
    public List<DetallePedidoEntidad> consultar() {
        return consultar(DetallePedidoEntidad.builder().build());
    }

    @Override
    public DetallePedidoEntidad consultarPorId(final String codigoDetallePedido) {
        final String sentenciaSql = """
                SELECT
                    dp.codigo_detalle_pedido,
                    dp.cantidad,
                    dp.subtotal,
                    dp.codigo_pedido,

                    p.codigo_plato,
                    p.nombre AS nombre_plato,
                    p.codigo_categoria,
                    c.nombre AS nombre_categoria,
                    p.precio_venta,
                    p.estado AS estado_plato

                FROM detalle_pedido dp
                INNER JOIN plato p ON dp.codigo_plato = p.codigo_plato
                INNER JOIN categoria c ON p.codigo_categoria = c.codigo_categoria
                WHERE LOWER(dp.codigo_detalle_pedido) = LOWER(?)
                """;

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setString(1, UtilTexto.aplicarTrim(codigoDetallePedido));

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return ensamblarDetallePedido(resultado);
                }
            }

            return DetallePedidoEntidad.builder().build();

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible consultar la información del detalle del pedido por identificador.",
                    excepcion);
        }
    }

    @Override
    public List<DetallePedidoEntidad> consultar(final DetallePedidoEntidad filtro) {
        var detalleFiltro = UtilObjeto.obtenerValorDefecto(
                filtro,
                DetallePedidoEntidad.builder().build());

        var parametros = new ArrayList<Object>();
        var sentenciaSql = new StringBuilder();

        sentenciaSql.append("SELECT ");
        sentenciaSql.append("dp.codigo_detalle_pedido, ");
        sentenciaSql.append("dp.cantidad, ");
        sentenciaSql.append("dp.subtotal, ");
        sentenciaSql.append("dp.codigo_pedido, ");

        sentenciaSql.append("p.codigo_plato, ");
        sentenciaSql.append("p.nombre AS nombre_plato, ");
        sentenciaSql.append("p.codigo_categoria, ");
        sentenciaSql.append("c.nombre AS nombre_categoria, ");
        sentenciaSql.append("p.precio_venta, ");
        sentenciaSql.append("p.estado AS estado_plato ");

        sentenciaSql.append("FROM detalle_pedido dp ");
        sentenciaSql.append("INNER JOIN plato p ON dp.codigo_plato = p.codigo_plato ");
        sentenciaSql.append("INNER JOIN categoria c ON p.codigo_categoria = c.codigo_categoria ");
        sentenciaSql.append("WHERE 1 = 1 ");

        if (UtilTexto.tieneTexto(detalleFiltro.getCodigoDetallePedido())) {
            sentenciaSql.append("AND LOWER(dp.codigo_detalle_pedido) = LOWER(?) ");
            parametros.add(detalleFiltro.getCodigoDetallePedido());
        }

        if (UtilTexto.tieneTexto(detalleFiltro.getCodigoPedido())) {
            sentenciaSql.append("AND LOWER(dp.codigo_pedido) = LOWER(?) ");
            parametros.add(detalleFiltro.getCodigoPedido());
        }

        if (detalleFiltro.getCantidad() > CANTIDAD_DEFECTO) {
            sentenciaSql.append("AND dp.cantidad = ? ");
            parametros.add(detalleFiltro.getCantidad());
        }

        if (UtilTexto.tieneTexto(detalleFiltro.getPlato().getCodigoPlato())) {
            sentenciaSql.append("AND LOWER(p.codigo_plato) = LOWER(?) ");
            parametros.add(detalleFiltro.getPlato().getCodigoPlato());
        }

        sentenciaSql.append("ORDER BY dp.codigo_detalle_pedido ASC");

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql.toString())) {

            for (int indice = 0; indice < parametros.size(); indice++) {
                sentencia.setObject(indice + 1, parametros.get(indice));
            }

            try (ResultSet resultado = sentencia.executeQuery()) {
                var detalles = new ArrayList<DetallePedidoEntidad>();

                while (resultado.next()) {
                    detalles.add(ensamblarDetallePedido(resultado));
                }

                return detalles;
            }

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible consultar la información de los detalles del pedido.",
                    excepcion);
        }
    }

    @Override
    public void actualizar(final DetallePedidoEntidad entidad) {
        final String sentenciaSql = """
                UPDATE detalle_pedido
                SET cantidad = ?,
                    subtotal = ?,
                    codigo_pedido = ?,
                    codigo_plato = ?
                WHERE codigo_detalle_pedido = ?
                """;

        var detalle = UtilObjeto.obtenerValorDefecto(
                entidad,
                DetallePedidoEntidad.builder().build());

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setInt(1, detalle.getCantidad());
            sentencia.setBigDecimal(2, detalle.getSubtotal());
            sentencia.setString(3, detalle.getCodigoPedido());
            sentencia.setString(4, detalle.getPlato().getCodigoPlato());
            sentencia.setString(5, detalle.getCodigoDetallePedido());

            sentencia.executeUpdate();

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible actualizar la información del detalle del pedido.",
                    excepcion);
        }
    }

    @Override
    public void eliminar(final String codigoDetallePedido) {
        final String sentenciaSql = """
                DELETE FROM detalle_pedido
                WHERE codigo_detalle_pedido = ?
                """;

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setString(1, UtilTexto.aplicarTrim(codigoDetallePedido));

            sentencia.executeUpdate();

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible eliminar la información del detalle del pedido.",
                    excepcion);
        }
    }

    private DetallePedidoEntidad ensamblarDetallePedido(final ResultSet resultado) throws SQLException {
        var categoria = CategoriaEntidad.builder()
                .codigoCategoria(resultado.getString("codigo_categoria"))
                .nombre(resultado.getString("nombre_categoria"))
                .build();

        var plato = PlatoEntidad.builder()
                .codigoPlato(resultado.getString("codigo_plato"))
                .nombre(resultado.getString("nombre_plato"))
                .categoria(categoria)
                .precioVenta(resultado.getBigDecimal("precio_venta"))
                .estado(resultado.getBoolean("estado_plato"))
                .build();

        return DetallePedidoEntidad.builder()
                .codigoDetallePedido(resultado.getString("codigo_detalle_pedido"))
                .cantidad(resultado.getInt("cantidad"))
                .subtotal(resultado.getBigDecimal("subtotal"))
                .codigoPedido(resultado.getString("codigo_pedido"))
                .plato(plato)
                .build();
    }
}