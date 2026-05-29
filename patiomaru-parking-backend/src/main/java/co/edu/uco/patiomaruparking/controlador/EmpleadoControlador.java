package co.edu.uco.patiomaruparking.controlador;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.patiomaruparking.controlador.respuesta.RespuestaExito;
import co.edu.uco.patiomaruparking.dto.CargoDTO;
import co.edu.uco.patiomaruparking.dto.CiudadResidenciaDTO;
import co.edu.uco.patiomaruparking.dto.EmpleadoDTO;
import co.edu.uco.patiomaruparking.dto.TipoDocumentoIdentificacionDTO;
import co.edu.uco.patiomaruparking.negocio.fachada.empleado.ActualizarEmpleadoFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.empleado.ActualizarEstadoEmpleadoFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.empleado.ConsultarEmpleadoPorIdFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.empleado.ConsultarEmpleadosFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.empleado.RegistrarEmpleadoFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.empleado.impl.ActualizarEmpleadoFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.empleado.impl.ActualizarEstadoEmpleadoFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.empleado.impl.ConsultarEmpleadoPorIdFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.empleado.impl.ConsultarEmpleadosFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.empleado.impl.RegistrarEmpleadoFachadaImpl;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;

@RestController
@RequestMapping("/api/v1/empleados")
public class EmpleadoControlador {

	private static final Logger logger = LoggerFactory.getLogger(EmpleadoControlador.class);

	@GetMapping("/dummy")
	public EmpleadoDTO obtenerEmpleadoDummy() {
		logger.debug("Iniciando obtención del empleado dummy.");

		var empleado = EmpleadoDTO.builder().build();

		logger.debug("Finalizó la obtención del empleado dummy.");

		return empleado;
	}

	@PostMapping
	public ResponseEntity<RespuestaExito<String>> registrarEmpleado(@RequestBody EmpleadoDTO empleado) {
		logger.info("Iniciando registro de empleado.");

		RegistrarEmpleadoFachada fachada = new RegistrarEmpleadoFachadaImpl();

		fachada.ejecutar(empleado);

		logger.info("Finalizó exitosamente el registro del empleado.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Se ha registrado de forma exitosa el empleado.", UtilTexto.TEXTO_VACIO),
				HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<RespuestaExito<String>> actualizarEmpleado(@RequestBody EmpleadoDTO empleado) {
		logger.info("Iniciando actualización de empleado.");

		ActualizarEmpleadoFachada fachada = new ActualizarEmpleadoFachadaImpl();

		fachada.ejecutar(empleado);

		logger.info("Finalizó exitosamente la actualización del empleado.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Se ha actualizado de forma exitosa la información del empleado.",
						UtilTexto.TEXTO_VACIO),
				HttpStatus.OK);
	}

	@PatchMapping("/estado")
	public ResponseEntity<RespuestaExito<String>> actualizarEstadoEmpleado(@RequestBody EmpleadoDTO empleado) {
		logger.info("Iniciando actualización de estado del empleado.");

		ActualizarEstadoEmpleadoFachada fachada = new ActualizarEstadoEmpleadoFachadaImpl();

		fachada.ejecutar(empleado);

		logger.info("Finalizó exitosamente la actualización del estado del empleado.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Se ha actualizado de forma exitosa el estado del empleado.",
						UtilTexto.TEXTO_VACIO),
				HttpStatus.OK);
	}

	@GetMapping("/{codigoEmpleado}")
	public ResponseEntity<RespuestaExito<EmpleadoDTO>> consultarEmpleadoPorId(
			@PathVariable("codigoEmpleado") String codigoEmpleado) {

		logger.info("Iniciando consulta de empleado por identificador.");

		ConsultarEmpleadoPorIdFachada fachada = new ConsultarEmpleadoPorIdFachadaImpl();

		var empleado = fachada.ejecutar(codigoEmpleado);

		logger.info("Finalizó exitosamente la consulta de empleado por identificador.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Empleado consultado exitosamente.", empleado),
				HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<RespuestaExito<List<EmpleadoDTO>>> consultarEmpleados(
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String codigoEmpleado,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String numeroIdentificacion,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String primerNombre,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String segundoNombre,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String primerApellido,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String segundoApellido,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String fechaNacimiento,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String edad,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String estado,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String numeroTelefono,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String correoElectronico,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String direccionResidencia,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String codigoTipoDocumentoIdentificacion,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String codigoCargo,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String codigoCiudadResidencia) {

		logger.info("Iniciando consulta de empleados por filtro.");

		var empleadoFiltro = construirFiltroEmpleado(
				codigoEmpleado,
				numeroIdentificacion,
				primerNombre,
				segundoNombre,
				primerApellido,
				segundoApellido,
				fechaNacimiento,
				edad,
				estado,
				numeroTelefono,
				correoElectronico,
				direccionResidencia,
				codigoTipoDocumentoIdentificacion,
				codigoCargo,
				codigoCiudadResidencia);

		ConsultarEmpleadosFachada fachada = new ConsultarEmpleadosFachadaImpl();

		var empleados = fachada.ejecutar(empleadoFiltro);

		logger.info("Finalizó exitosamente la consulta de empleados por filtro.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Empleados consultados exitosamente.", empleados),
				HttpStatus.OK);
	}

	private EmpleadoDTO construirFiltroEmpleado(
			final String codigoEmpleado,
			final String numeroIdentificacion,
			final String primerNombre,
			final String segundoNombre,
			final String primerApellido,
			final String segundoApellido,
			final String fechaNacimiento,
			final String edad,
			final String estado,
			final String numeroTelefono,
			final String correoElectronico,
			final String direccionResidencia,
			final String codigoTipoDocumentoIdentificacion,
			final String codigoCargo,
			final String codigoCiudadResidencia) {

		var empleadoBuilder = EmpleadoDTO.builder()
				.codigoEmpleado(UtilTexto.aplicarTrim(codigoEmpleado))
				.numeroIdentificacion(UtilTexto.aplicarTrim(numeroIdentificacion))
				.primerNombre(UtilTexto.aplicarTrim(primerNombre))
				.segundoNombre(UtilTexto.aplicarTrim(segundoNombre))
				.primerApellido(UtilTexto.aplicarTrim(primerApellido))
				.segundoApellido(UtilTexto.aplicarTrim(segundoApellido))
				.numeroTelefono(UtilTexto.aplicarTrim(numeroTelefono))
				.correoElectronico(UtilTexto.aplicarTrim(correoElectronico))
				.direccionResidencia(UtilTexto.aplicarTrim(direccionResidencia))
				.tipoDocumentoIdentificacion(TipoDocumentoIdentificacionDTO.builder()
						.codigoTipoDocumentoIdentificacion(UtilTexto.aplicarTrim(codigoTipoDocumentoIdentificacion))
						.build())
				.cargo(CargoDTO.builder()
						.codigoCargo(UtilTexto.aplicarTrim(codigoCargo))
						.build())
				.ciudadResidencia(CiudadResidenciaDTO.builder()
						.codigoCiudadResidencia(UtilTexto.aplicarTrim(codigoCiudadResidencia))
						.build());

		if (UtilTexto.tieneTexto(fechaNacimiento)) {
			empleadoBuilder.fechaNacimiento(convertirFechaNacimiento(fechaNacimiento));
		}

		if (UtilTexto.tieneTexto(edad)) {
			empleadoBuilder.edad(convertirEdad(edad));
		}

		if (UtilTexto.tieneTexto(estado)) {
			empleadoBuilder.estado(convertirEstado(estado));
		}

		return empleadoBuilder.build();
	}

	private LocalDate convertirFechaNacimiento(final String fechaNacimiento) {
		try {
			return LocalDate.parse(UtilTexto.aplicarTrim(fechaNacimiento));
		} catch (DateTimeParseException excepcion) {
			throw NegocioPatioMaruExcepcion.crear(
					"La fecha de nacimiento del empleado no tiene un formato válido. Use el formato AAAA-MM-DD.",
					"Se recibió una fecha de nacimiento con formato inválido en EmpleadoControlador.",
					excepcion);
		}
	}

	private Integer convertirEdad(final String edad) {
		try {
			return Integer.valueOf(UtilTexto.aplicarTrim(edad));
		} catch (NumberFormatException excepcion) {
			throw NegocioPatioMaruExcepcion.crear(
					"La edad del empleado debe ser un número entero válido.",
					"Se recibió una edad con formato inválido en EmpleadoControlador.",
					excepcion);
		}
	}

	private Boolean convertirEstado(final String estado) {
		var estadoNormalizado = UtilTexto.aplicarTrimConvertirMayusculas(estado);

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "TRUE")
				|| UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "ACTIVO")) {
			return Boolean.TRUE;
		}

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "FALSE")
				|| UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "INACTIVO")) {
			return Boolean.FALSE;
		}

		throw NegocioPatioMaruExcepcion.crear(
				"El estado del empleado no es válido. Use ACTIVO, INACTIVO, true o false.");
	}
}
