package co.edu.uco.patiomaruparking.datos.dao.sql.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import co.edu.uco.patiomaruparking.datos.dao.EmpleadoDAO;
import co.edu.uco.patiomaruparking.datos.dao.sql.SQLDAO;
import co.edu.uco.patiomaruparking.entidad.CargoEntidad;
import co.edu.uco.patiomaruparking.entidad.CiudadResidenciaEntidad;
import co.edu.uco.patiomaruparking.entidad.EmpleadoEntidad;
import co.edu.uco.patiomaruparking.entidad.TipoDocumentoIdentificacionEntidad;

public class EmpleadoPostgreSQLDAO extends SQLDAO implements EmpleadoDAO {

	public EmpleadoPostgreSQLDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void registrar(final EmpleadoEntidad entidad) {
		final String sentenciaSql = """
				INSERT INTO empleado (
					codigo_empleado,
					numero_identificacion,
					primer_nombre,
					primer_apellido,
					segundo_nombre,
					segundo_apellido,
					fecha_nacimiento,
					edad,
					estado,
					numero_telefono,
					correo_electronico,
					direccion_residencia,
					codigo_ciudad_residencia,
					codigo_tipo_documento_identificacion,
					codigo_cargo
				) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
			sentencia.setString(1, entidad.getCodigoEmpleado());
			sentencia.setString(2, entidad.getNumeroIdentificacion());
			sentencia.setString(3, entidad.getPrimerNombre());
			sentencia.setString(4, entidad.getPrimerApellido());
			sentencia.setString(5, entidad.getSegundoNombre());
			sentencia.setString(6, entidad.getSegundoApellido());
			sentencia.setObject(7, entidad.getFechaNacimiento());
			sentencia.setObject(8, entidad.getEdad());
			sentencia.setObject(9, entidad.getEstado());
			sentencia.setString(10, entidad.getNumeroTelefono());
			sentencia.setString(11, entidad.getCorreoElectronico());
			sentencia.setString(12, entidad.getDireccionResidencia());
			sentencia.setString(13, entidad.getCiudadResidencia().getCodigoCiudadResidencia());
			sentencia.setString(14, entidad.getTipoDocumentoIdentificacion().getCodigoTipoDocumentoIdentificacion());
			sentencia.setString(15, entidad.getCargo().getCodigoCargo());

			sentencia.executeUpdate();

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible registrar la información del empleado.", excepcion);
		}
	}

	@Override
	public List<EmpleadoEntidad> consultar() {
		return consultar(EmpleadoEntidad.builder().build());
	}

	@Override
	public EmpleadoEntidad consultarPorId(final String codigoEmpleado) {
		final String sentenciaSql = """
				SELECT
					e.codigo_empleado,
					e.numero_identificacion,
					e.primer_nombre,
					e.primer_apellido,
					e.segundo_nombre,
					e.segundo_apellido,
					e.fecha_nacimiento,
					e.edad,
					e.estado AS estado_empleado,
					e.numero_telefono,
					e.correo_electronico,
					e.direccion_residencia,

					cr.codigo_ciudad_residencia,
					cr.nombre AS nombre_ciudad_residencia,

					tdi.codigo_tipo_documento_identificacion,
					tdi.nombre AS nombre_tipo_documento_identificacion,

					c.codigo_cargo,
					c.nombre AS nombre_cargo,
					c.estado AS estado_cargo

				FROM empleado e
				INNER JOIN ciudad_residencia cr
					ON e.codigo_ciudad_residencia = cr.codigo_ciudad_residencia
				INNER JOIN tipo_documento_identificacion tdi
					ON e.codigo_tipo_documento_identificacion = tdi.codigo_tipo_documento_identificacion
				INNER JOIN cargo c
					ON e.codigo_cargo = c.codigo_cargo
				WHERE e.codigo_empleado = ?
				""";

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
			sentencia.setString(1, codigoEmpleado);

			try (ResultSet resultado = sentencia.executeQuery()) {
				if (resultado.next()) {
					return ensamblarEmpleado(resultado);
				}
			}

			return null;

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible consultar la información del empleado por identificador.",
					excepcion);
		}
	}

	@Override
	public List<EmpleadoEntidad> consultar(final EmpleadoEntidad filtro) {
		var parametros = new ArrayList<Object>();
		var sentenciaSql = new StringBuilder();

		sentenciaSql.append("SELECT ");
		sentenciaSql.append("e.codigo_empleado, ");
		sentenciaSql.append("e.numero_identificacion, ");
		sentenciaSql.append("e.primer_nombre, ");
		sentenciaSql.append("e.primer_apellido, ");
		sentenciaSql.append("e.segundo_nombre, ");
		sentenciaSql.append("e.segundo_apellido, ");
		sentenciaSql.append("e.fecha_nacimiento, ");
		sentenciaSql.append("e.edad, ");
		sentenciaSql.append("e.estado AS estado_empleado, ");
		sentenciaSql.append("e.numero_telefono, ");
		sentenciaSql.append("e.correo_electronico, ");
		sentenciaSql.append("e.direccion_residencia, ");

		sentenciaSql.append("cr.codigo_ciudad_residencia, ");
		sentenciaSql.append("cr.nombre AS nombre_ciudad_residencia, ");

		sentenciaSql.append("tdi.codigo_tipo_documento_identificacion, ");
		sentenciaSql.append("tdi.nombre AS nombre_tipo_documento_identificacion, ");

		sentenciaSql.append("c.codigo_cargo, ");
		sentenciaSql.append("c.nombre AS nombre_cargo, ");
		sentenciaSql.append("c.estado AS estado_cargo ");

		sentenciaSql.append("FROM empleado e ");
		sentenciaSql.append("INNER JOIN ciudad_residencia cr ");
		sentenciaSql.append("ON e.codigo_ciudad_residencia = cr.codigo_ciudad_residencia ");
		sentenciaSql.append("INNER JOIN tipo_documento_identificacion tdi ");
		sentenciaSql.append("ON e.codigo_tipo_documento_identificacion = tdi.codigo_tipo_documento_identificacion ");
		sentenciaSql.append("INNER JOIN cargo c ");
		sentenciaSql.append("ON e.codigo_cargo = c.codigo_cargo ");
		sentenciaSql.append("WHERE 1 = 1 ");

		if (Objects.nonNull(filtro)) {

			if (tieneTexto(filtro.getCodigoEmpleado())) {
				sentenciaSql.append("AND LOWER(e.codigo_empleado) = LOWER(?) ");
				parametros.add(filtro.getCodigoEmpleado());
			}

			if (tieneTexto(filtro.getNumeroIdentificacion())) {
				sentenciaSql.append("AND LOWER(e.numero_identificacion) = LOWER(?) ");
				parametros.add(filtro.getNumeroIdentificacion());
			}

			if (tieneTexto(filtro.getPrimerNombre())) {
				sentenciaSql.append("AND LOWER(e.primer_nombre) = LOWER(?) ");
				parametros.add(filtro.getPrimerNombre());
			}

			if (tieneTexto(filtro.getPrimerApellido())) {
				sentenciaSql.append("AND LOWER(e.primer_apellido) = LOWER(?) ");
				parametros.add(filtro.getPrimerApellido());
			}

			if (Objects.nonNull(filtro.getEstado())) {
				sentenciaSql.append("AND e.estado = ? ");
				parametros.add(filtro.getEstado());
			}

			if (Objects.nonNull(filtro.getCiudadResidencia())
					&& tieneTexto(filtro.getCiudadResidencia().getCodigoCiudadResidencia())) {
				sentenciaSql.append("AND LOWER(e.codigo_ciudad_residencia) = LOWER(?) ");
				parametros.add(filtro.getCiudadResidencia().getCodigoCiudadResidencia());
			}

			if (Objects.nonNull(filtro.getTipoDocumentoIdentificacion())
					&& tieneTexto(filtro.getTipoDocumentoIdentificacion().getCodigoTipoDocumentoIdentificacion())) {
				sentenciaSql.append("AND LOWER(e.codigo_tipo_documento_identificacion) = LOWER(?) ");
				parametros.add(filtro.getTipoDocumentoIdentificacion().getCodigoTipoDocumentoIdentificacion());
			}

			if (Objects.nonNull(filtro.getCargo())
					&& tieneTexto(filtro.getCargo().getCodigoCargo())) {
				sentenciaSql.append("AND LOWER(e.codigo_cargo) = LOWER(?) ");
				parametros.add(filtro.getCargo().getCodigoCargo());
			}
		}

		sentenciaSql.append("ORDER BY e.primer_nombre ASC, e.primer_apellido ASC");

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql.toString())) {

			for (int indice = 0; indice < parametros.size(); indice++) {
				sentencia.setObject(indice + 1, parametros.get(indice));
			}

			try (ResultSet resultado = sentencia.executeQuery()) {
				var empleados = new ArrayList<EmpleadoEntidad>();

				while (resultado.next()) {
					empleados.add(ensamblarEmpleado(resultado));
				}

				return empleados;
			}

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible consultar la información de los empleados.", excepcion);
		}
	}

	@Override
	public void actualizar(final EmpleadoEntidad entidad) {
		final String sentenciaSql = """
				UPDATE empleado
				SET numero_identificacion = ?,
					primer_nombre = ?,
					primer_apellido = ?,
					segundo_nombre = ?,
					segundo_apellido = ?,
					fecha_nacimiento = ?,
					edad = ?,
					estado = ?,
					numero_telefono = ?,
					correo_electronico = ?,
					direccion_residencia = ?,
					codigo_ciudad_residencia = ?,
					codigo_tipo_documento_identificacion = ?,
					codigo_cargo = ?
				WHERE codigo_empleado = ?
				""";

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
			sentencia.setString(1, entidad.getNumeroIdentificacion());
			sentencia.setString(2, entidad.getPrimerNombre());
			sentencia.setString(3, entidad.getPrimerApellido());
			sentencia.setString(4, entidad.getSegundoNombre());
			sentencia.setString(5, entidad.getSegundoApellido());
			sentencia.setObject(6, entidad.getFechaNacimiento());
			sentencia.setObject(7, entidad.getEdad());
			sentencia.setObject(8, entidad.getEstado());
			sentencia.setString(9, entidad.getNumeroTelefono());
			sentencia.setString(10, entidad.getCorreoElectronico());
			sentencia.setString(11, entidad.getDireccionResidencia());
			sentencia.setString(12, entidad.getCiudadResidencia().getCodigoCiudadResidencia());
			sentencia.setString(13, entidad.getTipoDocumentoIdentificacion().getCodigoTipoDocumentoIdentificacion());
			sentencia.setString(14, entidad.getCargo().getCodigoCargo());
			sentencia.setString(15, entidad.getCodigoEmpleado());

			sentencia.executeUpdate();

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible actualizar la información del empleado.", excepcion);
		}
	}

	@Override
	public void actualizarEstado(final String codigoEmpleado, final Boolean estado) {
		final String sentenciaSql = """
				UPDATE empleado
				SET estado = ?
				WHERE codigo_empleado = ?
				""";

		try (PreparedStatement sentencia = getConexion().prepareStatement(sentenciaSql)) {
			sentencia.setObject(1, estado);
			sentencia.setString(2, codigoEmpleado);

			sentencia.executeUpdate();

		} catch (SQLException excepcion) {
			throw new RuntimeException("No fue posible actualizar el estado del empleado.", excepcion);
		}
	}

	private EmpleadoEntidad ensamblarEmpleado(final ResultSet resultado) throws SQLException {
		var ciudadResidencia = CiudadResidenciaEntidad.builder()
				.codigoCiudadResidencia(resultado.getString("codigo_ciudad_residencia"))
				.nombre(resultado.getString("nombre_ciudad_residencia"))
				.build();

		var tipoDocumentoIdentificacion = TipoDocumentoIdentificacionEntidad.builder()
				.codigoTipoDocumentoIdentificacion(resultado.getString("codigo_tipo_documento_identificacion"))
				.nombre(resultado.getString("nombre_tipo_documento_identificacion"))
				.build();

		var cargo = CargoEntidad.builder()
				.codigoCargo(resultado.getString("codigo_cargo"))
				.nombre(resultado.getString("nombre_cargo"))
				.estado(resultado.getObject("estado_cargo", Boolean.class))
				.build();

		return EmpleadoEntidad.builder()
				.codigoEmpleado(resultado.getString("codigo_empleado"))
				.numeroIdentificacion(resultado.getString("numero_identificacion"))
				.primerNombre(resultado.getString("primer_nombre"))
				.primerApellido(resultado.getString("primer_apellido"))
				.segundoNombre(resultado.getString("segundo_nombre"))
				.segundoApellido(resultado.getString("segundo_apellido"))
				.fechaNacimiento(resultado.getObject("fecha_nacimiento", LocalDate.class))
				.edad(resultado.getObject("edad", Integer.class))
				.estado(resultado.getObject("estado_empleado", Boolean.class))
				.numeroTelefono(resultado.getString("numero_telefono"))
				.correoElectronico(resultado.getString("correo_electronico"))
				.direccionResidencia(resultado.getString("direccion_residencia"))
				.ciudadResidencia(ciudadResidencia)
				.tipoDocumentoIdentificacion(tipoDocumentoIdentificacion)
				.cargo(cargo)
				.build();
	}

	private boolean tieneTexto(final String texto) {
		return Objects.nonNull(texto) && !texto.trim().isEmpty();
	}
}
