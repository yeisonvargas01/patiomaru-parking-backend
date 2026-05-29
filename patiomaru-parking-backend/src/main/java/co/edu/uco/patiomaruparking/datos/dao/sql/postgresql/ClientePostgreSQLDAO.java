package co.edu.uco.patiomaruparking.datos.dao.sql.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uco.patiomaruparking.datos.dao.ClienteDAO;
import co.edu.uco.patiomaruparking.datos.dao.sql.SQLDAO;
import co.edu.uco.patiomaruparking.entidad.ClienteEntidad;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class ClientePostgreSQLDAO extends SQLDAO implements ClienteDAO {

    public ClientePostgreSQLDAO(final Connection conexion) {
        super(conexion);
    }

    @Override
    public void registrar(final ClienteEntidad entidad) {
        final String sentenciaSql = """
                INSERT INTO cliente (
                    codigo_cliente,
                    nombre,
                    telefono,
                    correo_electronico,
                    estado
                ) VALUES (?, ?, ?, ?, ?)
                """;

        var cliente = UtilObjeto.obtenerValorDefecto(
                entidad,
                ClienteEntidad.builder().build());

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setString(1, cliente.getCodigoCliente());
            sentencia.setString(2, cliente.getNombre());
            sentencia.setString(3, cliente.getTelefono());
            sentencia.setString(4, cliente.getCorreoElectronico());
            sentencia.setBoolean(5, cliente.getEstado());

            sentencia.executeUpdate();

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible registrar la información del cliente.",
                    excepcion);
        }
    }

    @Override
    public List<ClienteEntidad> consultar() {
        return consultar(ClienteEntidad.builder().build());
    }

    @Override
    public ClienteEntidad consultarPorId(final String codigoCliente) {
        final String sentenciaSql = """
                SELECT
                    codigo_cliente,
                    nombre,
                    telefono,
                    correo_electronico,
                    estado
                FROM cliente
                WHERE LOWER(codigo_cliente) = LOWER(?)
                """;

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setString(1, UtilTexto.aplicarTrim(codigoCliente));

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return ensamblarCliente(resultado);
                }
            }

            return ClienteEntidad.builder().build();

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible consultar la información del cliente por identificador.",
                    excepcion);
        }
    }

    @Override
    public List<ClienteEntidad> consultar(final ClienteEntidad filtro) {
        var clienteFiltro = UtilObjeto.obtenerValorDefecto(
                filtro,
                ClienteEntidad.builder().build());

        var parametros = new ArrayList<Object>();
        var sentenciaSql = new StringBuilder();

        sentenciaSql.append("SELECT ");
        sentenciaSql.append("codigo_cliente, ");
        sentenciaSql.append("nombre, ");
        sentenciaSql.append("telefono, ");
        sentenciaSql.append("correo_electronico, ");
        sentenciaSql.append("estado ");
        sentenciaSql.append("FROM cliente ");
        sentenciaSql.append("WHERE 1 = 1 ");

        if (UtilTexto.tieneTexto(clienteFiltro.getCodigoCliente())) {
            sentenciaSql.append("AND LOWER(codigo_cliente) = LOWER(?) ");
            parametros.add(clienteFiltro.getCodigoCliente());
        }

        if (UtilTexto.tieneTexto(clienteFiltro.getNombre())) {
            sentenciaSql.append("AND LOWER(nombre) = LOWER(?) ");
            parametros.add(clienteFiltro.getNombre());
        }

        if (UtilTexto.tieneTexto(clienteFiltro.getTelefono())) {
            sentenciaSql.append("AND telefono = ? ");
            parametros.add(clienteFiltro.getTelefono());
        }

        if (UtilTexto.tieneTexto(clienteFiltro.getCorreoElectronico())) {
            sentenciaSql.append("AND LOWER(correo_electronico) = LOWER(?) ");
            parametros.add(clienteFiltro.getCorreoElectronico());
        }

        sentenciaSql.append("ORDER BY nombre ASC");

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql.toString())) {

            for (int indice = 0; indice < parametros.size(); indice++) {
                sentencia.setObject(indice + 1, parametros.get(indice));
            }

            try (ResultSet resultado = sentencia.executeQuery()) {
                var clientes = new ArrayList<ClienteEntidad>();

                while (resultado.next()) {
                    clientes.add(ensamblarCliente(resultado));
                }

                return clientes;
            }

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible consultar la información de los clientes.",
                    excepcion);
        }
    }

    @Override
    public void actualizar(final ClienteEntidad entidad) {
        final String sentenciaSql = """
                UPDATE cliente
                SET nombre = ?,
                    telefono = ?,
                    correo_electronico = ?,
                    estado = ?
                WHERE codigo_cliente = ?
                """;

        var cliente = UtilObjeto.obtenerValorDefecto(
                entidad,
                ClienteEntidad.builder().build());

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setString(1, cliente.getNombre());
            sentencia.setString(2, cliente.getTelefono());
            sentencia.setString(3, cliente.getCorreoElectronico());
            sentencia.setBoolean(4, cliente.getEstado());
            sentencia.setString(5, cliente.getCodigoCliente());

            sentencia.executeUpdate();

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible actualizar la información del cliente.",
                    excepcion);
        }
    }

    @Override
    public void actualizarEstado(final String codigoCliente, final Boolean estado) {
        final String sentenciaSql = """
                UPDATE cliente
                SET estado = ?
                WHERE codigo_cliente = ?
                """;

        var estadoSeguro = UtilObjeto.obtenerValorDefecto(estado, Boolean.TRUE);

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setBoolean(1, estadoSeguro);
            sentencia.setString(2, UtilTexto.aplicarTrim(codigoCliente));

            sentencia.executeUpdate();

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible actualizar el estado del cliente.",
                    excepcion);
        }
    }

    private ClienteEntidad ensamblarCliente(final ResultSet resultado) throws SQLException {
        return ClienteEntidad.builder()
                .codigoCliente(resultado.getString("codigo_cliente"))
                .nombre(resultado.getString("nombre"))
                .telefono(resultado.getString("telefono"))
                .correoElectronico(resultado.getString("correo_electronico"))
                .estado(resultado.getBoolean("estado"))
                .build();
    }
}
