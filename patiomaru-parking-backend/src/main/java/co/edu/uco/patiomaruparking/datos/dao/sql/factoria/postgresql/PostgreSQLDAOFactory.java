package co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql;

import java.sql.SQLException;

import co.edu.uco.patiomaruparking.datos.dao.CajaDAO;
import co.edu.uco.patiomaruparking.datos.dao.CargoDAO;
import co.edu.uco.patiomaruparking.datos.dao.CategoriaDAO;
import co.edu.uco.patiomaruparking.datos.dao.CiudadDAO;
import co.edu.uco.patiomaruparking.datos.dao.ClienteDAO;
import co.edu.uco.patiomaruparking.datos.dao.DetallePedidoDAO;
import co.edu.uco.patiomaruparking.datos.dao.EmpleadoDAO;
import co.edu.uco.patiomaruparking.datos.dao.InsumoDAO;
import co.edu.uco.patiomaruparking.datos.dao.InventarioDAO;
import co.edu.uco.patiomaruparking.datos.dao.MesaDAO;
import co.edu.uco.patiomaruparking.datos.dao.MetodoPagoDAO;
import co.edu.uco.patiomaruparking.datos.dao.PagoDAO;
import co.edu.uco.patiomaruparking.datos.dao.PedidoDAO;
import co.edu.uco.patiomaruparking.datos.dao.PlatoDAO;
import co.edu.uco.patiomaruparking.datos.dao.TipoDocumentoIdentificacionDAO;
import co.edu.uco.patiomaruparking.datos.dao.UbicacionDAO;
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

	@Override
	public CategoriaDAO obtenerCategoriaDAO() {
		return null;
	}

	@Override
	public InventarioDAO obtenerInventarioDAO() {
		return null;
	}

	@Override
	public InsumoDAO obtenerInsumoDAO() {
		return null;
	}

	@Override
	public PagoDAO obtenerPagoDAO() {
		return null;
	}

	@Override
	public MetodoPagoDAO obtenerMetodoPagoDAO() {
		return null;
	}

	@Override
	public CajaDAO obtenerCajaDAO() {
		return null;
	}

	@Override
	public UbicacionDAO obtenerUbicacionDAO() {
		return null;
	}

	@Override
	public CiudadDAO obtenerCiudadDAO() {
		return null;
	}

	@Override
	public CargoDAO obtenerCargoDAO() {
		return null;
	}

	@Override
	public TipoDocumentoIdentificacionDAO obtenerTipoDocumentoIdentificacionDAO() {
		return null;
	}
}
