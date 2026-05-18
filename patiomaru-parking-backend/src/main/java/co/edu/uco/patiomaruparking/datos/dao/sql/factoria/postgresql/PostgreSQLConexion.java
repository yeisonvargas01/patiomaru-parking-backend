package co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class PostgreSQLConexion {

	private static final String URL = "jdbc:postgresql://localhost:5433/patiomaru_parking";
	private static final String USUARIO = "postgres";
	private static final String CLAVE = "postgres";

	private PostgreSQLConexion() {
		super();
	}

	public static Connection obtenerConexion() {
		try {
			Class.forName("org.postgresql.Driver");
			return DriverManager.getConnection(URL, USUARIO, CLAVE);

		} catch (ClassNotFoundException excepcion) {
			throw new RuntimeException("No fue posible cargar el driver de PostgreSQL.", excepcion);

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible abrir la conexión con PostgreSQL.", excepcion);
		}
	}
}
