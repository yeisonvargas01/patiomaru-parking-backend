package co.edu.uco.patiomaruparking.datos.dao.sql.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import co.edu.uco.patiomaruparking.datos.dao.PlatoDAO;
import co.edu.uco.patiomaruparking.datos.dao.sql.SQLDAO;
import co.edu.uco.patiomaruparking.entidad.CategoriaEntidad;
import co.edu.uco.patiomaruparking.entidad.PlatoEntidad;

public class PlatoPostgreSQLDAO extends SQLDAO implements PlatoDAO {

	public PlatoPostgreSQLDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void registrar(final PlatoEntidad entidad) {
		final String sentenciaSql = """
				INSERT INTO plato (
					codigo_plato,
					nombre,
					codigo_categoria,
					precio_venta,
					estado
				) VALUES (?, ?, ?, ?, ?)
				""";

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
			sentencia.setString(1, entidad.getCodigoPlato());
			sentencia.setString(2, entidad.getNombre());
			sentencia.setString(3, entidad.getCategoria().getCodigoCategoria());
			sentencia.setBigDecimal(4, entidad.getPrecioVenta());
			sentencia.setObject(5, entidad.getEstado());

			sentencia.executeUpdate();

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible registrar la información del plato.", excepcion);
		}
	}

	@Override
	public List<PlatoEntidad> consultar() {
		return consultar(PlatoEntidad.builder().build());
	}

	@Override
	public PlatoEntidad consultarPorId(final String codigoPlato) {
		final String sentenciaSql = """
				SELECT
					p.codigo_plato,
					p.nombre,
					p.codigo_categoria,
					c.nombre AS nombre_categoria,
					p.precio_venta,
					p.estado
				FROM plato p
				INNER JOIN categoria c ON p.codigo_categoria = c.codigo_categoria
				WHERE p.codigo_plato = ?
				""";

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
			sentencia.setString(1, codigoPlato);

			try (ResultSet resultado = sentencia.executeQuery()) {
				if (resultado.next()) {
					return ensamblarPlato(resultado);
				}
			}

			return null;

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible consultar la información del plato por identificador.",
					excepcion);
		}
	}

	@Override
	public List<PlatoEntidad> consultar(final PlatoEntidad filtro) {
		var parametros = new ArrayList<Object>();
		var sentenciaSql = new StringBuilder();

		sentenciaSql.append("SELECT ");
		sentenciaSql.append("p.codigo_plato, ");
		sentenciaSql.append("p.nombre, ");
		sentenciaSql.append("p.codigo_categoria, ");
		sentenciaSql.append("c.nombre AS nombre_categoria, ");
		sentenciaSql.append("p.precio_venta, ");
		sentenciaSql.append("p.estado ");
		sentenciaSql.append("FROM plato p ");
		sentenciaSql.append("INNER JOIN categoria c ON p.codigo_categoria = c.codigo_categoria ");
		sentenciaSql.append("WHERE 1 = 1 ");

		if (Objects.nonNull(filtro)) {

			if (tieneTexto(filtro.getCodigoPlato())) {
				sentenciaSql.append("AND LOWER(p.codigo_plato) = LOWER(?) ");
				parametros.add(filtro.getCodigoPlato());
			}

			if (tieneTexto(filtro.getNombre())) {
				sentenciaSql.append("AND LOWER(p.nombre) = LOWER(?) ");
				parametros.add(filtro.getNombre());
			}

			if (Objects.nonNull(filtro.getCategoria())
					&& tieneTexto(filtro.getCategoria().getCodigoCategoria())) {
				sentenciaSql.append("AND LOWER(p.codigo_categoria) = LOWER(?) ");
				parametros.add(filtro.getCategoria().getCodigoCategoria());
			}

			if (Objects.nonNull(filtro.getEstado())) {
				sentenciaSql.append("AND p.estado = ? ");
				parametros.add(filtro.getEstado());
			}
		}

		sentenciaSql.append("ORDER BY p.nombre ASC");

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql.toString())) {

			for (int indice = 0; indice < parametros.size(); indice++) {
				sentencia.setObject(indice + 1, parametros.get(indice));
			}

			try (ResultSet resultado = sentencia.executeQuery()) {
				var platos = new ArrayList<PlatoEntidad>();

				while (resultado.next()) {
					platos.add(ensamblarPlato(resultado));
				}

				return platos;
			}

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible consultar la información de los platos.", excepcion);
		}
	}

	@Override
	public void actualizar(final PlatoEntidad entidad) {
		final String sentenciaSql = """
				UPDATE plato
				SET nombre = ?,
					codigo_categoria = ?,
					precio_venta = ?,
					estado = ?
				WHERE codigo_plato = ?
				""";

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
			sentencia.setString(1, entidad.getNombre());
			sentencia.setString(2, entidad.getCategoria().getCodigoCategoria());
			sentencia.setBigDecimal(3, entidad.getPrecioVenta());
			sentencia.setObject(4, entidad.getEstado());
			sentencia.setString(5, entidad.getCodigoPlato());

			sentencia.executeUpdate();

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible actualizar la información del plato.", excepcion);
		}
	}

	@Override
	public void actualizarDisponibilidad(final String codigoPlato, final boolean disponible) {
		final String sentenciaSql = """
				UPDATE plato
				SET estado = ?
				WHERE codigo_plato = ?
				""";

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
			sentencia.setBoolean(1, disponible);
			sentencia.setString(2, codigoPlato);

			sentencia.executeUpdate();

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible actualizar la disponibilidad del plato.", excepcion);
		}
	}

	@Override
	public void eliminar(final String codigoPlato) {
		final String sentenciaSql = """
				DELETE FROM plato
				WHERE codigo_plato = ?
				""";

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
			sentencia.setString(1, codigoPlato);

			sentencia.executeUpdate();

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible eliminar la información del plato.", excepcion);
		}
	}

	private PlatoEntidad ensamblarPlato(final ResultSet resultado) throws SQLException {
		var categoria = CategoriaEntidad.builder()
				.codigoCategoria(resultado.getString("codigo_categoria"))
				.nombre(resultado.getString("nombre_categoria"))
				.build();

		return PlatoEntidad.builder()
				.codigoPlato(resultado.getString("codigo_plato"))
				.nombre(resultado.getString("nombre"))
				.categoria(categoria)
				.precioVenta(resultado.getBigDecimal("precio_venta"))
				.estado(resultado.getObject("estado", Boolean.class))
				.build();
	}

	private boolean tieneTexto(final String texto) {
		return Objects.nonNull(texto) && !texto.trim().isEmpty();
	}
}
