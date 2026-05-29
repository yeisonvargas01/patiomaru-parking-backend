package co.edu.uco.patiomaruparking.datos.dao.sql.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uco.patiomaruparking.datos.dao.MesaDAO;
import co.edu.uco.patiomaruparking.datos.dao.sql.SQLDAO;
import co.edu.uco.patiomaruparking.entidad.MesaEntidad;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class MesaPostgreSQLDAO extends SQLDAO implements MesaDAO {

    public MesaPostgreSQLDAO(final Connection conexion) {
        super(conexion);
    }

    @Override
    public List<MesaEntidad> consultar() {
        return consultar(MesaEntidad.builder().build());
    }

    @Override
    public MesaEntidad consultarPorId(final String codigoMesa) {
        final String sentenciaSql = """
                SELECT
                    codigo_mesa,
                    nombre
                FROM mesa
                WHERE LOWER(codigo_mesa) = LOWER(?)
                """;

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setString(1, UtilTexto.aplicarTrim(codigoMesa));

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return ensamblarMesa(resultado);
                }
            }

            return MesaEntidad.builder().build();

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible consultar la información de la mesa por identificador.",
                    excepcion);
        }
    }

    @Override
    public List<MesaEntidad> consultar(final MesaEntidad filtro) {
        var mesaFiltro = UtilObjeto.obtenerValorDefecto(
                filtro,
                MesaEntidad.builder().build());

        var parametros = new ArrayList<Object>();
        var sentenciaSql = new StringBuilder();

        sentenciaSql.append("SELECT ");
        sentenciaSql.append("codigo_mesa, ");
        sentenciaSql.append("nombre ");
        sentenciaSql.append("FROM mesa ");
        sentenciaSql.append("WHERE 1 = 1 ");

        if (UtilTexto.tieneTexto(mesaFiltro.getCodigoMesa())) {
            sentenciaSql.append("AND LOWER(codigo_mesa) = LOWER(?) ");
            parametros.add(mesaFiltro.getCodigoMesa());
        }

        if (UtilTexto.tieneTexto(mesaFiltro.getNombre())) {
            sentenciaSql.append("AND LOWER(nombre) = LOWER(?) ");
            parametros.add(mesaFiltro.getNombre());
        }

        sentenciaSql.append("ORDER BY nombre ASC");

        try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql.toString())) {

            for (int indice = 0; indice < parametros.size(); indice++) {
                sentencia.setObject(indice + 1, parametros.get(indice));
            }

            try (ResultSet resultado = sentencia.executeQuery()) {
                var mesas = new ArrayList<MesaEntidad>();

                while (resultado.next()) {
                    mesas.add(ensamblarMesa(resultado));
                }

                return mesas;
            }

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible consultar la información de las mesas.",
                    excepcion);
        }
    }

    private MesaEntidad ensamblarMesa(final ResultSet resultado) throws SQLException {
        return MesaEntidad.builder()
                .codigoMesa(resultado.getString("codigo_mesa"))
                .nombre(resultado.getString("nombre"))
                .build();
    }
}