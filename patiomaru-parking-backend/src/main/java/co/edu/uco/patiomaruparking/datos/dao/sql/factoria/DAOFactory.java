package co.edu.uco.patiomaruparking.datos.dao.sql.factoria;

import java.sql.Connection;

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
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;

public abstract class DAOFactory {

	protected Connection conexion;

	private static final TipoFactoriaEnum FACTORIA_ACTUAL = TipoFactoriaEnum.POSTGRESQL;

	public static DAOFactory getFactory() {
		switch (FACTORIA_ACTUAL) {
			case POSTGRESQL:
				return new PostgreSQLDAOFactory();

			default:
				throw new IllegalArgumentException("Tipo de factoría no soportado: " + FACTORIA_ACTUAL);
		}
	}

	public abstract void abrirConexion();

	public abstract void cerrarConexion();

	public abstract void iniciarTransaccion();

	public abstract void confirmarTransaccion();

	public abstract void cancelarTransaccion();

	public abstract ClienteDAO obtenerClienteDAO();

	public abstract EmpleadoDAO obtenerEmpleadoDAO();

	public abstract MesaDAO obtenerMesaDAO();

	public abstract PedidoDAO obtenerPedidoDAO();

	public abstract DetallePedidoDAO obtenerDetallePedidoDAO();

	public abstract PlatoDAO obtenerPlatoDAO();

	public abstract CategoriaDAO obtenerCategoriaDAO();

	public abstract InventarioDAO obtenerInventarioDAO();

	public abstract InsumoDAO obtenerInsumoDAO();

	public abstract PagoDAO obtenerPagoDAO();

	public abstract MetodoPagoDAO obtenerMetodoPagoDAO();

	public abstract CajaDAO obtenerCajaDAO();

	public abstract UbicacionDAO obtenerUbicacionDAO();

	public abstract CiudadDAO obtenerCiudadDAO();

	public abstract CargoDAO obtenerCargoDAO();

	public abstract TipoDocumentoIdentificacionDAO obtenerTipoDocumentoIdentificacionDAO();
}