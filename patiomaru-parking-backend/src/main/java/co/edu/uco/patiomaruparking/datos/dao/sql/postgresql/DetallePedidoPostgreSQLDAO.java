package co.edu.uco.patiomaruparking.datos.dao.sql.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import co.edu.uco.patiomaruparking.datos.dao.DetallePedidoDAO;
import co.edu.uco.patiomaruparking.datos.dao.sql.SQLDAO;
import co.edu.uco.patiomaruparking.entidad.CategoriaEntidad;
import co.edu.uco.patiomaruparking.entidad.DetallePedidoEntidad;
import co.edu.uco.patiomaruparking.entidad.PlatoEntidad;

public class DetallePedidoPostgreSQLDAO extends SQLDAO implements DetallePedidoDAO {

	public DetallePedidoPostgreSQLDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void registrar(final DetallePedidoEntidad entidad) {
		final String sentenciaSql = """
				INSERT INTO detalle_pedido (
					codigo_detalle_pedido,
					cantidad,
					subtotal,
					codigo_pedido,
					codigo_plato
				) VALUES (?, ?, ?, ?, ?)
				""";

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
			sentencia.setString(1, entidad.getCodigoDetallePedido());
			sentencia.setObject(2, entidad.getCantidad());
			sentencia.setBigDecimal(3, entidad.getSubtotal());
			sentencia.setString(4, entidad.getCodigoPedido());
			sentencia.setString(5, entidad.getPlato().getCodigoPlato());

			sentencia.executeUpdate();

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible registrar la información del detalle del pedido.", excepcion);
		}
	}

	@Override
	public List<DetallePedidoEntidad> consultar() {
		return consultar(DetallePedidoEntidad.builder().build());
	}

	@Override
	public DetallePedidoEntidad consultarPorId(final String codigoDetallePedido) {
		final String sentenciaSql = """
				SELECT
					dp.codigo_detalle_pedido,
					dp.cantidad,
					dp.subtotal,
					dp.codigo_pedido,

					p.codigo_plato,
					p.nombre AS nombre_plato,
					p.codigo_categoria,
					c.nombre AS nombre_categoria,
					p.precio_venta,
					p.estado AS estado_plato

				FROM detalle_pedido dp
				INNER JOIN plato p ON dp.codigo_plato = p.codigo_plato
				INNER JOIN categoria c ON p.codigo_categoria = c.codigo_categoria
				WHERE dp.codigo_detalle_pedido = ?
				""";

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
			sentencia.setString(1, codigoDetallePedido);

			try (ResultSet resultado = sentencia.executeQuery()) {
				if (resultado.next()) {
					return ensamblarDetallePedido(resultado);
				}
			}

			return null;

		} catch (SQLException excepcion) {
			throw new RuntimeException(
					"No fue posible consultar la información del detalle del pedido por identificador.",
					excepcion);
		}
	}

	@Override
	public List<DetallePedidoEntidad> consultar(final DetallePedidoEntidad filtro) {
		var parametros = new ArrayList<Object>();
		var sentenciaSql = new StringBuilder();

		sentenciaSql.append("SELECT ");
		sentenciaSql.append("dp.codigo_detalle_pedido, ");
		sentenciaSql.append("dp.cantidad, ");
		sentenciaSql.append("dp.subtotal, ");
		sentenciaSql.append("dp.codigo_pedido, ");

		sentenciaSql.append("p.codigo_plato, ");
		sentenciaSql.append("p.nombre AS nombre_plato, ");
		sentenciaSql.append("p.codigo_categoria, ");
		sentenciaSql.append("c.nombre AS nombre_categoria, ");
		sentenciaSql.append("p.precio_venta, ");
		sentenciaSql.append("p.estado AS estado_plato ");

		sentenciaSql.append("FROM detalle_pedido dp ");
		sentenciaSql.append("INNER JOIN plato p ON dp.codigo_plato = p.codigo_plato ");
		sentenciaSql.append("INNER JOIN categoria c ON p.codigo_categoria = c.codigo_categoria ");
		sentenciaSql.append("WHERE 1 = 1 ");

		if (Objects.nonNull(filtro)) {

			if (tieneTexto(filtro.getCodigoDetallePedido())) {
				sentenciaSql.append("AND LOWER(dp.codigo_detalle_pedido) = LOWER(?) ");
				parametros.add(filtro.getCodigoDetallePedido());
			}

			if (tieneTexto(filtro.getCodigoPedido())) {
				sentenciaSql.append("AND LOWER(dp.codigo_pedido) = LOWER(?) ");
				parametros.add(filtro.getCodigoPedido());
			}

			if (Objects.nonNull(filtro.getCantidad())) {
				sentenciaSql.append("AND dp.cantidad = ? ");
				parametros.add(filtro.getCantidad());
			}

			if (Objects.nonNull(filtro.getPlato()) && tieneTexto(filtro.getPlato().getCodigoPlato())) {
				sentenciaSql.append("AND LOWER(p.codigo_plato) = LOWER(?) ");
				parametros.add(filtro.getPlato().getCodigoPlato());
			}
		}

		sentenciaSql.append("ORDER BY dp.codigo_detalle_pedido ASC");

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql.toString())) {

			for (int indice = 0; indice < parametros.size(); indice++) {
				sentencia.setObject(indice + 1, parametros.get(indice));
			}

			try (ResultSet resultado = sentencia.executeQuery()) {
				var detalles = new ArrayList<DetallePedidoEntidad>();

				while (resultado.next()) {
					detalles.add(ensamblarDetallePedido(resultado));
				}

				return detalles;
			}

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible consultar la información de los detalles del pedido.", excepcion);
		}
	}

	@Override
	public void actualizar(final DetallePedidoEntidad entidad) {
		final String sentenciaSql = """
				UPDATE detalle_pedido
				SET cantidad = ?,
					subtotal = ?,
					codigo_pedido = ?,
					codigo_plato = ?
				WHERE codigo_detalle_pedido = ?
				""";

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
			sentencia.setObject(1, entidad.getCantidad());
			sentencia.setBigDecimal(2, entidad.getSubtotal());
			sentencia.setString(3, entidad.getCodigoPedido());
			sentencia.setString(4, entidad.getPlato().getCodigoPlato());
			sentencia.setString(5, entidad.getCodigoDetallePedido());

			sentencia.executeUpdate();

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible actualizar la información del detalle del pedido.", excepcion);
		}
	}

	@Override
	public void eliminar(final String codigoDetallePedido) {
		final String sentenciaSql = """
				DELETE FROM detalle_pedido
				WHERE codigo_detalle_pedido = ?
				""";

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
			sentencia.setString(1, codigoDetallePedido);

			sentencia.executeUpdate();

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible eliminar la información del detalle del pedido.", excepcion);
		}
	}

	private DetallePedidoEntidad ensamblarDetallePedido(final ResultSet resultado) throws SQLException {
		var categoria = CategoriaEntidad.builder()
				.codigoCategoria(resultado.getString("codigo_categoria"))
				.nombre(resultado.getString("nombre_categoria"))
				.build();

		var plato = PlatoEntidad.builder()
				.codigoPlato(resultado.getString("codigo_plato"))
				.nombre(resultado.getString("nombre_plato"))
				.categoria(categoria)
				.precioVenta(resultado.getBigDecimal("precio_venta"))
				.estado(resultado.getObject("estado_plato", Boolean.class))
				.build();

		return DetallePedidoEntidad.builder()
				.codigoDetallePedido(resultado.getString("codigo_detalle_pedido"))
				.cantidad(resultado.getObject("cantidad", Integer.class))
				.subtotal(resultado.getBigDecimal("subtotal"))
				.codigoPedido(resultado.getString("codigo_pedido"))
				.plato(plato)
				.build();
	}

	private boolean tieneTexto(final String texto) {
		return Objects.nonNull(texto) && !texto.trim().isEmpty();
	}
}
