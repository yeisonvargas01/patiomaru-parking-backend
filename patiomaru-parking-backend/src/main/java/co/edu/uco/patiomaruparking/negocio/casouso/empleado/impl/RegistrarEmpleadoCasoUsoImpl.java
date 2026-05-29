package co.edu.uco.patiomaruparking.negocio.casouso.empleado.impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.EmpleadoEntidad;
import co.edu.uco.patiomaruparking.entidad.TipoDocumentoIdentificacionEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.EmpleadoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.empleado.RegistrarEmpleadoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.EmpleadoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilCodigo;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class RegistrarEmpleadoCasoUsoImpl implements RegistrarEmpleadoCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(RegistrarEmpleadoCasoUsoImpl.class);

	private static final String PREFIJO_EMPLEADO = "EMP";
	private static final int CANTIDAD_DIGITOS_EMPLEADO = 3;
	private static final int LONGITUD_CODIGO_EMPLEADO = 6;

	private static final int LONGITUD_MINIMA_NUMERO_IDENTIFICACION = 6;
	private static final int LONGITUD_MAXIMA_NUMERO_IDENTIFICACION = 15;

	private static final int LONGITUD_MINIMA_NOMBRE = 2;
	private static final int LONGITUD_MAXIMA_NOMBRE = 40;

	private static final int LONGITUD_MINIMA_APELLIDO = 2;
	private static final int LONGITUD_MAXIMA_APELLIDO = 40;

	private static final int LONGITUD_MINIMA_TELEFONO = 10;
	private static final int LONGITUD_MAXIMA_TELEFONO = 15;

	private static final int LONGITUD_MINIMA_CORREO_ELECTRONICO = 5;
	private static final int LONGITUD_MAXIMA_CORREO_ELECTRONICO = 100;

	private static final int LONGITUD_MINIMA_DIRECCION_RESIDENCIA = 5;
	private static final int LONGITUD_MAXIMA_DIRECCION_RESIDENCIA = 120;

	private static final int LONGITUD_CODIGO_TIPO_DOCUMENTO_IDENTIFICACION = 7;
	private static final int LONGITUD_CODIGO_CARGO = 6;
	private static final int LONGITUD_CODIGO_CIUDAD_RESIDENCIA = 6;

	private static final int EDAD_MINIMA_EMPLEADO = 18;

	private static final boolean ESTADO_EMPLEADO_ACTIVO = true;

	private final DAOFactory daoFactory;

	public RegistrarEmpleadoCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para registrar empleado porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public EmpleadoDominio ejecutar(final EmpleadoDominio datos) {
		logger.info("Iniciando el registro de un empleado.");

		var empleado = UtilObjeto.obtenerValorDefecto(
				datos,
				EmpleadoDominio.builder().build());

		validarDatosConsistentes(empleado);

		validarNoExisteEmpleadoConMismoTipoDocumentoYNumeroIdentificacion(empleado);
		validarNoExisteEmpleadoConMismoNumeroTelefono(empleado.getNumeroTelefono());
		validarNoExisteEmpleadoConMismoCorreoElectronico(empleado.getCorreoElectronico());

		var codigoEmpleado = generarCodigoUnicoEmpleado();

		var empleadoPreparado = EmpleadoDominio.builder()
				.codigoEmpleado(codigoEmpleado)
				.numeroIdentificacion(UtilTexto.aplicarTrim(empleado.getNumeroIdentificacion()))
				.primerNombre(UtilTexto.aplicarTrim(empleado.getPrimerNombre()))
				.segundoNombre(UtilTexto.aplicarTrim(empleado.getSegundoNombre()))
				.primerApellido(UtilTexto.aplicarTrim(empleado.getPrimerApellido()))
				.segundoApellido(UtilTexto.aplicarTrim(empleado.getSegundoApellido()))
				.fechaNacimiento(empleado.getFechaNacimiento())
				.edad(calcularEdad(empleado.getFechaNacimiento()))
				.estado(ESTADO_EMPLEADO_ACTIVO)
				.numeroTelefono(UtilTexto.aplicarTrim(empleado.getNumeroTelefono()))
				.correoElectronico(UtilTexto.aplicarTrim(empleado.getCorreoElectronico()))
				.direccionResidencia(UtilTexto.aplicarTrim(empleado.getDireccionResidencia()))
				.tipoDocumentoIdentificacion(empleado.getTipoDocumentoIdentificacion())
				.cargo(empleado.getCargo())
				.ciudadResidencia(empleado.getCiudadResidencia())
				.build();

		guardar(empleadoPreparado);

		logger.info("Empleado registrado satisfactoriamente.");

		return empleadoPreparado;
	}

	private void validarDatosConsistentes(final EmpleadoDominio empleado) {
		validarNumeroIdentificacion(empleado.getNumeroIdentificacion());

		validarTextoObligatorio(
				empleado.getPrimerNombre(),
				"El primer nombre",
				LONGITUD_MINIMA_NOMBRE,
				LONGITUD_MAXIMA_NOMBRE);

		validarTextoOpcional(
				empleado.getSegundoNombre(),
				"El segundo nombre",
				LONGITUD_MINIMA_NOMBRE,
				LONGITUD_MAXIMA_NOMBRE);

		validarTextoObligatorio(
				empleado.getPrimerApellido(),
				"El primer apellido",
				LONGITUD_MINIMA_APELLIDO,
				LONGITUD_MAXIMA_APELLIDO);

		validarTextoOpcional(
				empleado.getSegundoApellido(),
				"El segundo apellido",
				LONGITUD_MINIMA_APELLIDO,
				LONGITUD_MAXIMA_APELLIDO);

		validarFechaNacimiento(empleado.getFechaNacimiento());
		validarNumeroTelefono(empleado.getNumeroTelefono());
		validarCorreoElectronico(empleado.getCorreoElectronico());

		validarTextoObligatorio(
				empleado.getDireccionResidencia(),
				"La dirección de residencia",
				LONGITUD_MINIMA_DIRECCION_RESIDENCIA,
				LONGITUD_MAXIMA_DIRECCION_RESIDENCIA);

		validarTipoDocumentoIdentificacion(empleado);
		validarCargo(empleado);
		validarCiudadResidencia(empleado);
	}

	private void validarNumeroIdentificacion(final String numeroIdentificacion) {
		validarTextoObligatorio(
				numeroIdentificacion,
				"El número de identificación",
				LONGITUD_MINIMA_NUMERO_IDENTIFICACION,
				LONGITUD_MAXIMA_NUMERO_IDENTIFICACION);

		if (!contieneSoloDigitos(numeroIdentificacion)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El número de identificación del empleado solo debe contener dígitos.");
		}
	}

	private void validarNumeroTelefono(final String numeroTelefono) {
		validarTextoObligatorio(
				numeroTelefono,
				"El número de teléfono",
				LONGITUD_MINIMA_TELEFONO,
				LONGITUD_MAXIMA_TELEFONO);

		if (!contieneSoloDigitos(numeroTelefono)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El número de teléfono del empleado solo debe contener dígitos.");
		}
	}

	private void validarCorreoElectronico(final String correoElectronico) {
		validarTextoObligatorio(
				correoElectronico,
				"El correo electrónico",
				LONGITUD_MINIMA_CORREO_ELECTRONICO,
				LONGITUD_MAXIMA_CORREO_ELECTRONICO);

		if (!tieneFormatoBasicoCorreoElectronico(correoElectronico)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El correo electrónico del empleado no tiene un formato válido.");
		}
	}

	private void validarFechaNacimiento(final LocalDate fechaNacimiento) {
		if (UtilObjeto.esNulo(fechaNacimiento)) {
			throw NegocioPatioMaruExcepcion.crear(
					"La fecha de nacimiento del empleado es obligatoria.");
		}

		if (fechaNacimiento.isAfter(LocalDate.now())) {
			throw NegocioPatioMaruExcepcion.crear(
					"La fecha de nacimiento del empleado no puede ser futura.");
		}

		if (calcularEdad(fechaNacimiento) < EDAD_MINIMA_EMPLEADO) {
			throw NegocioPatioMaruExcepcion.crear(
					"El empleado debe ser mayor de edad.");
		}
	}

	private void validarTextoObligatorio(
			final String valor,
			final String nombreCampo,
			final int longitudMinima,
			final int longitudMaxima) {

		if (!UtilTexto.tieneTexto(valor)) {
			throw NegocioPatioMaruExcepcion.crear(
					nombreCampo + " del empleado es obligatorio.");
		}

		validarLongitud(valor, nombreCampo, longitudMinima, longitudMaxima);
	}

	private void validarTextoOpcional(
			final String valor,
			final String nombreCampo,
			final int longitudMinima,
			final int longitudMaxima) {

		if (!UtilTexto.tieneTexto(valor)) {
			return;
		}

		validarLongitud(valor, nombreCampo, longitudMinima, longitudMaxima);
	}

	private void validarLongitud(
			final String valor,
			final String nombreCampo,
			final int longitudMinima,
			final int longitudMaxima) {

		var valorSeguro = UtilTexto.aplicarTrim(valor);

		if (valorSeguro.length() < longitudMinima || valorSeguro.length() > longitudMaxima) {
			throw NegocioPatioMaruExcepcion.crear(
					nombreCampo + " del empleado debe tener entre "
							+ longitudMinima + " y " + longitudMaxima + " caracteres.");
		}
	}

	private void validarTipoDocumentoIdentificacion(final EmpleadoDominio empleado) {
		if (!UtilTexto.tieneTexto(
				empleado.getTipoDocumentoIdentificacion().getCodigoTipoDocumentoIdentificacion())) {

			throw NegocioPatioMaruExcepcion.crear(
					"El tipo de documento de identificación del empleado es obligatorio.");
		}

		validarLongitudExacta(
				empleado.getTipoDocumentoIdentificacion().getCodigoTipoDocumentoIdentificacion(),
				LONGITUD_CODIGO_TIPO_DOCUMENTO_IDENTIFICACION,
				"El código del tipo de documento de identificación");
	}

	private void validarCargo(final EmpleadoDominio empleado) {
		if (!UtilTexto.tieneTexto(empleado.getCargo().getCodigoCargo())) {
			throw NegocioPatioMaruExcepcion.crear(
					"El cargo del empleado es obligatorio.");
		}

		validarLongitudExacta(
				empleado.getCargo().getCodigoCargo(),
				LONGITUD_CODIGO_CARGO,
				"El código del cargo");
	}

	private void validarCiudadResidencia(final EmpleadoDominio empleado) {
		if (!UtilTexto.tieneTexto(empleado.getCiudadResidencia().getCodigoCiudadResidencia())) {
			throw NegocioPatioMaruExcepcion.crear(
					"La ciudad de residencia del empleado es obligatoria.");
		}

		validarLongitudExacta(
				empleado.getCiudadResidencia().getCodigoCiudadResidencia(),
				LONGITUD_CODIGO_CIUDAD_RESIDENCIA,
				"El código de la ciudad de residencia");
	}

	private void validarLongitudExacta(
			final String valor,
			final int longitudEsperada,
			final String nombreCampo) {

		if (UtilTexto.aplicarTrim(valor).length() != longitudEsperada) {
			throw NegocioPatioMaruExcepcion.crear(
					nombreCampo + " debe tener exactamente "
							+ longitudEsperada + " caracteres.");
		}
	}

	private void validarNoExisteEmpleadoConMismoTipoDocumentoYNumeroIdentificacion(final EmpleadoDominio empleado) {
		var filtro = EmpleadoEntidad.builder()
				.numeroIdentificacion(UtilTexto.aplicarTrim(empleado.getNumeroIdentificacion()))
				.tipoDocumentoIdentificacion(TipoDocumentoIdentificacionEntidad.builder()
						.codigoTipoDocumentoIdentificacion(UtilTexto.aplicarTrim(
								empleado.getTipoDocumentoIdentificacion()
										.getCodigoTipoDocumentoIdentificacion()))
						.build())
				.build();

		var resultados = UtilObjeto.obtenerValorDefecto(
				daoFactory.obtenerEmpleadoDAO().consultar(filtro),
				List.<EmpleadoEntidad>of());

		if (!resultados.isEmpty()) {
			throw NegocioPatioMaruExcepcion.crear(
					"Ya existe un empleado registrado con el mismo tipo de documento y número de identificación.");
		}
	}

	private void validarNoExisteEmpleadoConMismoNumeroTelefono(final String numeroTelefono) {
		var empleados = consultarTodosLosEmpleados();

		for (EmpleadoEntidad empleado : empleados) {
			var empleadoSeguro = UtilObjeto.obtenerValorDefecto(
					empleado,
					EmpleadoEntidad.builder().build());

			if (UtilTexto.sonIgualesIgnorandoMayusculas(
					empleadoSeguro.getNumeroTelefono(),
					numeroTelefono)) {

				throw NegocioPatioMaruExcepcion.crear(
						"Ya existe un empleado registrado con el mismo número de teléfono.");
			}
		}
	}

	private void validarNoExisteEmpleadoConMismoCorreoElectronico(final String correoElectronico) {
		var empleados = consultarTodosLosEmpleados();

		for (EmpleadoEntidad empleado : empleados) {
			var empleadoSeguro = UtilObjeto.obtenerValorDefecto(
					empleado,
					EmpleadoEntidad.builder().build());

			if (UtilTexto.sonIgualesIgnorandoMayusculas(
					empleadoSeguro.getCorreoElectronico(),
					correoElectronico)) {

				throw NegocioPatioMaruExcepcion.crear(
						"Ya existe un empleado registrado con el mismo correo electrónico.");
			}
		}
	}

	private List<EmpleadoEntidad> consultarTodosLosEmpleados() {
		return UtilObjeto.obtenerValorDefecto(
				daoFactory.obtenerEmpleadoDAO().consultar(EmpleadoEntidad.builder().build()),
				List.<EmpleadoEntidad>of());
	}

	private String generarCodigoUnicoEmpleado() {
		String codigoEmpleado;
		EmpleadoEntidad empleadoExistente;

		do {
			codigoEmpleado = UtilCodigo.generarCodigo(
					PREFIJO_EMPLEADO,
					CANTIDAD_DIGITOS_EMPLEADO);

			validarLongitudExacta(
					codigoEmpleado,
					LONGITUD_CODIGO_EMPLEADO,
					"El código del empleado generado");

			empleadoExistente = daoFactory.obtenerEmpleadoDAO()
					.consultarPorId(codigoEmpleado);

		} while (UtilObjeto.noEsNulo(empleadoExistente)
				&& UtilTexto.tieneTexto(empleadoExistente.getCodigoEmpleado()));

		return codigoEmpleado;
	}

	private Integer calcularEdad(final LocalDate fechaNacimiento) {
		return Period.between(fechaNacimiento, LocalDate.now()).getYears();
	}

	private void guardar(final EmpleadoDominio empleado) {
		var empleadoEntidad = EmpleadoEntidadAssembler.getInstance()
				.ensamblarEntidad(empleado);

		daoFactory.obtenerEmpleadoDAO()
				.registrar(empleadoEntidad);
	}

	private boolean contieneSoloDigitos(final String valor) {
		var valorSeguro = UtilTexto.aplicarTrim(valor);

		for (int indice = 0; indice < valorSeguro.length(); indice++) {
			if (!Character.isDigit(valorSeguro.charAt(indice))) {
				return false;
			}
		}

		return true;
	}

	private boolean tieneFormatoBasicoCorreoElectronico(final String correoElectronico) {
		var correoSeguro = UtilTexto.aplicarTrim(correoElectronico);

		return correoSeguro.contains("@")
				&& correoSeguro.contains(".")
				&& correoSeguro.indexOf("@") > 0
				&& correoSeguro.lastIndexOf(".") > correoSeguro.indexOf("@") + 1
				&& correoSeguro.lastIndexOf(".") < correoSeguro.length() - 1;
	}
}