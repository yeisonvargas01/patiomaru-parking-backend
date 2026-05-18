package co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql;

import java.sql.SQLException;

import co.edu.uco.patiomaruparking.datos.dao.ClienteDAO;
import co.edu.uco.patiomaruparking.datos.dao.DetallePedidoDAO;
import co.edu.uco.patiomaruparking.datos.dao.EmpleadoDAO;
import co.edu.uco.patiomaruparking.datos.dao.MesaDAO;
import co.edu.uco.patiomaruparking.datos.dao.PedidoDAO;
import co.edu.uco.patiomaruparking.datos.dao.PlatoDAO;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;

public final class PostgreSQLDAOFactory extends DAOFactory {

	public PostgreSQLDAOFactory() {
		abrirConexion();
	}

	@Override
	public void abrirConexion() {
		if (conexion == null) {
			conexion = PostgreSQLConexion.obtenerConexion();
		}
	}

	@Override
	public void cerrarConexion() {
		try {
			if (conexion != null && !conexion.isClosed()) {
				conexion.close();
			}
		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible cerrar la conexión con PostgreSQL.", excepcion);
		}
	}

	@Override
	public void iniciarTransaccion() {
		try {
			if (conexion != null && !conexion.isClosed()) {
				conexion.setAutoCommit(false);
			}
		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible iniciar la transacción en PostgreSQL.", excepcion);
		}
	}

	@Override
	public void confirmarTransaccion() {
		try {
			if (conexion != null && !conexion.isClosed()) {
				conexion.commit();
			}
		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible confirmar la transacción en PostgreSQL.", excepcion);
		}
	}

	@Override
	public void cancelarTransaccion() {
		try {
			if (conexion != null && !conexion.isClosed()) {
				conexion.rollback();
			}
		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible cancelar la transacción en PostgreSQL.", excepcion);
		}
	}

	@Override
	public ClienteDAO obtenerClienteDAO() {
		return null;
	}

	@Override
	public EmpleadoDAO obtenerEmpleadoDAO() {
		return null;
	}

	@Override
	public MesaDAO obtenerMesaDAO() {
		return null;
	}

	@Override
	public PedidoDAO obtenerPedidoDAO() {
		return null;
	}

	@Override
	public DetallePedidoDAO obtenerDetallePedidoDAO() {
		return null;
	}

	@Override
	public PlatoDAO obtenerPlatoDAO() {
		return null;
	}

}
