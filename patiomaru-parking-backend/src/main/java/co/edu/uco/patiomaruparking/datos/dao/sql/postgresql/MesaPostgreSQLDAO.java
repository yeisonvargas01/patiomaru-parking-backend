package co.edu.uco.patiomaruparking.datos.dao.sql.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import co.edu.uco.patiomaruparking.datos.dao.MesaDAO;
import co.edu.uco.patiomaruparking.datos.dao.sql.SQLDAO;
import co.edu.uco.patiomaruparking.entidad.MesaEntidad;

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
				WHERE codigo_mesa = ?
				""";

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
			sentencia.setString(1, codigoMesa);

			try (ResultSet resultado = sentencia.executeQuery()) {
				if (resultado.next()) {
					return ensamblarMesa(resultado);
				}
			}

			return null;

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible consultar la información de la mesa por identificador.",
					excepcion);
		}
	}

	@Override
	public List<MesaEntidad> consultar(final MesaEntidad filtro) {
		var parametros = new ArrayList<Object>();
		var sentenciaSql = new StringBuilder();

		sentenciaSql.append("SELECT ");
		sentenciaSql.append("codigo_mesa, ");
		sentenciaSql.append("nombre ");
		sentenciaSql.append("FROM mesa ");
		sentenciaSql.append("WHERE 1 = 1 ");

		if (Objects.nonNull(filtro)) {

			if (tieneTexto(filtro.getCodigoMesa())) {
				sentenciaSql.append("AND LOWER(codigo_mesa) = LOWER(?) ");
				parametros.add(filtro.getCodigoMesa());
			}

			if (tieneTexto(filtro.getNombre())) {
				sentenciaSql.append("AND LOWER(nombre) = LOWER(?) ");
				parametros.add(filtro.getNombre());
			}
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
			throw new RuntimeException("No fue posible consultar la información de las mesas.", excepcion);
		}
	}

	private MesaEntidad ensamblarMesa(final ResultSet resultado) throws SQLException {
		return MesaEntidad.builder()
				.codigoMesa(resultado.getString("codigo_mesa"))
				.nombre(resultado.getString("nombre"))
				.build();
	}

	private boolean tieneTexto(final String texto) {
		return Objects.nonNull(texto) && !texto.trim().isEmpty();
	}
}