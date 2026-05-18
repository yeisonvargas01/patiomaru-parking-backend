package co.edu.uco.patiomaruparking.datos.dao.sql.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import co.edu.uco.patiomaruparking.datos.dao.ClienteDAO;
import co.edu.uco.patiomaruparking.datos.dao.sql.SQLDAO;
import co.edu.uco.patiomaruparking.entidad.ClienteEntidad;

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

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
			sentencia.setString(1, entidad.getCodigoCliente());
			sentencia.setString(2, entidad.getNombre());
			sentencia.setString(3, entidad.getTelefono());
			sentencia.setString(4, entidad.getCorreoElectronico());
			sentencia.setObject(5, entidad.getEstado());

			sentencia.executeUpdate();

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible registrar la información del cliente.", excepcion);
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
				WHERE codigo_cliente = ?
				""";

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
			sentencia.setString(1, codigoCliente);

			try (ResultSet resultado = sentencia.executeQuery()) {
				if (resultado.next()) {
					return ensamblarCliente(resultado);
				}
			}

			return null;

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible consultar la información del cliente por identificador.",
					excepcion);
		}
	}

	@Override
	public List<ClienteEntidad> consultar(final ClienteEntidad filtro) {
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

		if (Objects.nonNull(filtro)) {

			if (tieneTexto(filtro.getCodigoCliente())) {
				sentenciaSql.append("AND LOWER(codigo_cliente) = LOWER(?) ");
				parametros.add(filtro.getCodigoCliente());
			}

			if (tieneTexto(filtro.getNombre())) {
				sentenciaSql.append("AND LOWER(nombre) = LOWER(?) ");
				parametros.add(filtro.getNombre());
			}

			if (tieneTexto(filtro.getTelefono())) {
				sentenciaSql.append("AND telefono = ? ");
				parametros.add(filtro.getTelefono());
			}

			if (tieneTexto(filtro.getCorreoElectronico())) {
				sentenciaSql.append("AND LOWER(correo_electronico) = LOWER(?) ");
				parametros.add(filtro.getCorreoElectronico());
			}

			if (Objects.nonNull(filtro.getEstado())) {
				sentenciaSql.append("AND estado = ? ");
				parametros.add(filtro.getEstado());
			}
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
			throw new RuntimeException("No fue posible consultar la información de los clientes.", excepcion);
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

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
			sentencia.setString(1, entidad.getNombre());
			sentencia.setString(2, entidad.getTelefono());
			sentencia.setString(3, entidad.getCorreoElectronico());
			sentencia.setObject(4, entidad.getEstado());
			sentencia.setString(5, entidad.getCodigoCliente());

			sentencia.executeUpdate();

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible actualizar la información del cliente.", excepcion);
		}
	}

	@Override
	public void actualizarEstado(final String codigoCliente, final Boolean estado) {
		final String sentenciaSql = """
				UPDATE cliente
				SET estado = ?
				WHERE codigo_cliente = ?
				""";

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
			sentencia.setObject(1, estado);
			sentencia.setString(2, codigoCliente);

			sentencia.executeUpdate();

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible actualizar el estado del cliente.", excepcion);
		}
	}

	private ClienteEntidad ensamblarCliente(final ResultSet resultado) throws SQLException {
		return ClienteEntidad.builder()
				.codigoCliente(resultado.getString("codigo_cliente"))
				.nombre(resultado.getString("nombre"))
				.telefono(resultado.getString("telefono"))
				.correoElectronico(resultado.getString("correo_electronico"))
				.estado(resultado.getObject("estado", Boolean.class))
				.build();
	}

	private boolean tieneTexto(final String texto) {
		return Objects.nonNull(texto) && !texto.trim().isEmpty();
	}
}
