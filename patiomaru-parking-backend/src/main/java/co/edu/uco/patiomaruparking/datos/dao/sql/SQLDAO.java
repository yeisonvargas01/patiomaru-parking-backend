package co.edu.uco.patiomaruparking.datos.dao.sql;

import java.sql.Connection;

public abstract class SQLDAO {

	private Connection conexion;

	protected SQLDAO(final Connection conexion) {
		super();
		this.conexion = conexion;
	}

	protected Connection getConexion() {
		return conexion;
	}
}
