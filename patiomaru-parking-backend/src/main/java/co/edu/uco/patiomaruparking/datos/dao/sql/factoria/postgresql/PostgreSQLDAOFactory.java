package co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql;

import java.sql.SQLException;

import co.edu.uco.patiomaruparking.datos.dao.ClienteDAO;
import co.edu.uco.patiomaruparking.datos.dao.DetallePedidoDAO;
import co.edu.uco.patiomaruparking.datos.dao.EmpleadoDAO;
import co.edu.uco.patiomaruparking.datos.dao.MesaDAO;
import co.edu.uco.patiomaruparking.datos.dao.PedidoDAO;
import co.edu.uco.patiomaruparking.datos.dao.PlatoDAO;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.postgresql.ClientePostgreSQLDAO;
import co.edu.uco.patiomaruparking.datos.dao.sql.postgresql.DetallePedidoPostgreSQLDAO;
import co.edu.uco.patiomaruparking.datos.dao.sql.postgresql.EmpleadoPostgreSQLDAO;
import co.edu.uco.patiomaruparking.datos.dao.sql.postgresql.MesaPostgreSQLDAO;
import co.edu.uco.patiomaruparking.datos.dao.sql.postgresql.PedidoPostgreSQLDAO;
import co.edu.uco.patiomaruparking.datos.dao.sql.postgresql.PlatoPostgreSQLDAO;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;

public final class PostgreSQLDAOFactory extends DAOFactory {

    public PostgreSQLDAOFactory() {
        abrirConexion();
    }

    @Override
    public void abrirConexion() {
        try {
            if (UtilObjeto.esNulo(conexion) || conexion.isClosed()) {
                conexion = PostgreSQLConexion.obtenerConexion();
            }

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible validar la conexión con PostgreSQL.",
                    excepcion);
        }
    }

    @Override
    public void cerrarConexion() {
        try {
            if (UtilObjeto.noEsNulo(conexion) && !conexion.isClosed()) {
                conexion.close();
            }

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible cerrar la conexión con PostgreSQL.",
                    excepcion);
        }
    }

    @Override
    public void iniciarTransaccion() {
        try {
            abrirConexion();
            conexion.setAutoCommit(false);

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible iniciar la transacción en PostgreSQL.",
                    excepcion);
        }
    }

    @Override
    public void confirmarTransaccion() {
        try {
            abrirConexion();
            conexion.commit();
            conexion.setAutoCommit(true);

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible confirmar la transacción en PostgreSQL.",
                    excepcion);
        }
    }

    @Override
    public void cancelarTransaccion() {
        try {
            abrirConexion();
            conexion.rollback();
            conexion.setAutoCommit(true);

        } catch (SQLException excepcion) {
            throw new RuntimeException(
                    "No fue posible cancelar la transacción en PostgreSQL.",
                    excepcion);
        }
    }

    @Override
    public ClienteDAO obtenerClienteDAO() {
        abrirConexion();
        return new ClientePostgreSQLDAO(conexion);
    }

    @Override
    public EmpleadoDAO obtenerEmpleadoDAO() {
        abrirConexion();
        return new EmpleadoPostgreSQLDAO(conexion);
    }

    @Override
    public MesaDAO obtenerMesaDAO() {
        abrirConexion();
        return new MesaPostgreSQLDAO(conexion);
    }

    @Override
    public PedidoDAO obtenerPedidoDAO() {
        abrirConexion();
        return new PedidoPostgreSQLDAO(conexion);
    }

    @Override
    public DetallePedidoDAO obtenerDetallePedidoDAO() {
        abrirConexion();
        return new DetallePedidoPostgreSQLDAO(conexion);
    }

    @Override
    public PlatoDAO obtenerPlatoDAO() {
        abrirConexion();
        return new PlatoPostgreSQLDAO(conexion);
    }
}
