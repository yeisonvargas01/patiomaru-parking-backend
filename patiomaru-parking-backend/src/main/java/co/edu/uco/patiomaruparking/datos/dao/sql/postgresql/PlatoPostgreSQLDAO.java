package co.edu.uco.patiomaruparking.datos.dao.sql.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uco.patiomaruparking.datos.dao.PlatoDAO;
import co.edu.uco.patiomaruparking.datos.dao.sql.SQLDAO;
import co.edu.uco.patiomaruparking.entidad.CategoriaEntidad;
import co.edu.uco.patiomaruparking.entidad.PlatoEntidad;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class PlatoPostgreSQLDAO extends SQLDAO implements PlatoDAO {

    public PlatoPostgreSQLDAO(final Connection conexion) {
        super(conexion);
    }

    @Override
    public void registrar(final PlatoEntidad entidad) {
        final String sentenciaSql = """
                INSERT INTO plato (
                    codigo_plato,
                    nombre,
                    codigo_categoria,
                    precio_venta,
                    estado
                ) VALUES (?, ?, ?, ?, ?)
                """;

        var plato = UtilObjeto.obtenerValorDefecto(
                entidad,
                PlatoEntidad.builder().build());

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setString(1, plato.getCodigoPlato());
            sentencia.setString(2, plato.getNombre());
            sentencia.setString(3, plato.getCategoria().getCodigoCategoria());
            sentencia.setBigDecimal(4, plato.getPrecioVenta());
            sentencia.setBoolean(5, plato.getEstado());

            sentencia.executeUpdate();

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible registrar la información del plato.",
                    excepcion);
        }
    }

    @Override
    public List<PlatoEntidad> consultar() {
        return consultar(PlatoEntidad.builder().build());
    }

    @Override
    public PlatoEntidad consultarPorId(final String codigoPlato) {
        final String sentenciaSql = """
                SELECT
                    p.codigo_plato,
                    p.nombre,
                    p.codigo_categoria,
                    c.nombre AS nombre_categoria,
                    p.precio_venta,
                    p.estado
                FROM plato p
                INNER JOIN categoria c ON p.codigo_categoria = c.codigo_categoria
                WHERE LOWER(p.codigo_plato) = LOWER(?)
                """;

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setString(1, UtilTexto.aplicarTrim(codigoPlato));

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return ensamblarPlato(resultado);
                }
            }

            return PlatoEntidad.builder().build();

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible consultar la información del plato por identificador.",
                    excepcion);
        }
    }

    @Override
    public List<PlatoEntidad> consultar(final PlatoEntidad filtro) {
        var platoFiltro = UtilObjeto.obtenerValorDefecto(
                filtro,
                PlatoEntidad.builder().build());

        var parametros = new ArrayList<Object>();
        var sentenciaSql = new StringBuilder();

        sentenciaSql.append("SELECT ");
        sentenciaSql.append("p.codigo_plato, ");
        sentenciaSql.append("p.nombre, ");
        sentenciaSql.append("p.codigo_categoria, ");
        sentenciaSql.append("c.nombre AS nombre_categoria, ");
        sentenciaSql.append("p.precio_venta, ");
        sentenciaSql.append("p.estado ");
        sentenciaSql.append("FROM plato p ");
        sentenciaSql.append("INNER JOIN categoria c ON p.codigo_categoria = c.codigo_categoria ");
        sentenciaSql.append("WHERE 1 = 1 ");

        if (UtilTexto.tieneTexto(platoFiltro.getCodigoPlato())) {
            sentenciaSql.append("AND LOWER(p.codigo_plato) = LOWER(?) ");
            parametros.add(platoFiltro.getCodigoPlato());
        }

        if (UtilTexto.tieneTexto(platoFiltro.getNombre())) {
            sentenciaSql.append("AND LOWER(p.nombre) = LOWER(?) ");
            parametros.add(platoFiltro.getNombre());
        }

        if (UtilTexto.tieneTexto(platoFiltro.getCategoria().getCodigoCategoria())) {
            sentenciaSql.append("AND LOWER(p.codigo_categoria) = LOWER(?) ");
            parametros.add(platoFiltro.getCategoria().getCodigoCategoria());
        }

        sentenciaSql.append("ORDER BY p.nombre ASC");

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql.toString())) {

            for (int indice = 0; indice < parametros.size(); indice++) {
                sentencia.setObject(indice + 1, parametros.get(indice));
            }

            try (ResultSet resultado = sentencia.executeQuery()) {
                var platos = new ArrayList<PlatoEntidad>();

                while (resultado.next()) {
                    platos.add(ensamblarPlato(resultado));
                }

                return platos;
            }

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible consultar la información de los platos.",
                    excepcion);
        }
    }

    @Override
    public void actualizar(final PlatoEntidad entidad) {
        final String sentenciaSql = """
                UPDATE plato
                SET nombre = ?,
                    codigo_categoria = ?,
                    precio_venta = ?,
                    estado = ?
                WHERE codigo_plato = ?
                """;

        var plato = UtilObjeto.obtenerValorDefecto(
                entidad,
                PlatoEntidad.builder().build());

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setString(1, plato.getNombre());
            sentencia.setString(2, plato.getCategoria().getCodigoCategoria());
            sentencia.setBigDecimal(3, plato.getPrecioVenta());
            sentencia.setBoolean(4, plato.getEstado());
            sentencia.setString(5, plato.getCodigoPlato());

            sentencia.executeUpdate();

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible actualizar la información del plato.",
                    excepcion);
        }
    }

    @Override
    public void actualizarDisponibilidad(final String codigoPlato, final boolean disponible) {
        final String sentenciaSql = """
                UPDATE plato
                SET estado = ?
                WHERE codigo_plato = ?
                """;

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setBoolean(1, disponible);
            sentencia.setString(2, UtilTexto.aplicarTrim(codigoPlato));

            sentencia.executeUpdate();

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible actualizar la disponibilidad del plato.",
                    excepcion);
        }
    }

    @Override
    public void eliminar(final String codigoPlato) {
        final String sentenciaSql = """
                DELETE FROM plato
                WHERE codigo_plato = ?
                """;

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setString(1, UtilTexto.aplicarTrim(codigoPlato));

            sentencia.executeUpdate();

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible eliminar la información del plato.",
                    excepcion);
        }
    }

    private PlatoEntidad ensamblarPlato(final ResultSet resultado) throws SQLException {
        var categoria = CategoriaEntidad.builder()
                .codigoCategoria(resultado.getString("codigo_categoria"))
                .nombre(resultado.getString("nombre_categoria"))
                .build();

        return PlatoEntidad.builder()
                .codigoPlato(resultado.getString("codigo_plato"))
                .nombre(resultado.getString("nombre"))
                .categoria(categoria)
                .precioVenta(resultado.getBigDecimal("precio_venta"))
                .estado(resultado.getBoolean("estado"))
                .build();
    }
}