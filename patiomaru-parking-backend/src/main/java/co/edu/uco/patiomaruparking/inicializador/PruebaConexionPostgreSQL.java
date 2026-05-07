package co.edu.uco.patiomaruparking.inicializador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PruebaConexionPostgreSQL {

	public static void main(String[] args) {

		String url = "jdbc:postgresql://localhost:5432/patiomaru_db?currentSchema=restaurante_yeison";
		String usuario = "postgres";
		String clave = "Ajstyles4#";

		String sql = "SELECT current_database() AS base_datos, current_schema() AS esquema";

		try (
				Connection conexion = DriverManager.getConnection(url, usuario, clave);
				PreparedStatement sentencia = conexion.prepareStatement(sql);
				ResultSet resultado = sentencia.executeQuery()
		) {

			System.out.println("Conexión exitosa a PostgreSQL");

			if (resultado.next()) {
				System.out.println("Base de datos: " + resultado.getString("base_datos"));
				System.out.println("Esquema: " + resultado.getString("esquema"));
			}

		} catch (Exception excepcion) {
			excepcion.printStackTrace();
		}
	}
}
