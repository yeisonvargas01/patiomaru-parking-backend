package co.edu.uco.patiomaruparking.negocio.casouso.empleado.impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.EmpleadoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.EmpleadoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.empleado.ActualizarEmpleadoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.EmpleadoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class ActualizarEmpleadoCasoUsoImpl implements ActualizarEmpleadoCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(ActualizarEmpleadoCasoUsoImpl.class);

	private static final String PREFIJO_EMPLEADO = "EMP";
	private static final int LONGITUD_CODIGO_EMPLEADO = 6;
	private static final int POSICION_INICIO_DIGITOS_EMPLEADO = 3;

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

	private final DAOFactory daoFactory;

	public ActualizarEmpleadoCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para actualizar empleado porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final EmpleadoDominio datos) {
		logger.info("Iniciando la actualización de un empleado.");

		var empleadoActualizar = UtilObjeto.obtenerValorDefecto(
				datos,
				EmpleadoDominio.builder().build());

		var codigoEmpleado = validarYNormalizarCodigoEmpleado(
				empleadoActualizar.getCodigoEmpleado());

		validarDatosConsistentes(empleadoActualizar);

		var empleadoActual = validarYObtenerEmpleado(codigoEmpleado);

		validarNoExisteOtroEmpleadoConMismoTipoDocumentoYNumeroIdentificacion(
				codigoEmpleado,
				empleadoActualizar);

		validarNoExisteOtroEmpleadoConMismoNumeroTelefono(
				codigoEmpleado,
				empleadoActualizar.getNumeroTelefono());

		validarNoExisteOtroEmpleadoConMismoCorreoElectronico(
				codigoEmpleado,
				empleadoActualizar.getCorreoElectronico());

		var empleadoActualizado = EmpleadoDominio.builder()
				.codigoEmpleado(codigoEmpleado)
				.numeroIdentificacion(UtilTexto.aplicarTrim(empleadoActualizar.getNumeroIdentificacion()))
				.primerNombre(UtilTexto.aplicarTrim(empleadoActualizar.getPrimerNombre()))
				.segundoNombre(UtilTexto.aplicarTrim(empleadoActualizar.getSegundoNombre()))
				.primerApellido(UtilTexto.aplicarTrim(empleadoActualizar.getPrimerApellido()))
				.segundoApellido(UtilTexto.aplicarTrim(empleadoActualizar.getSegundoApellido()))
				.fechaNacimiento(empleadoActualizar.getFechaNacimiento())
				.edad(calcularEdad(empleadoActualizar.getFechaNacimiento()))
				.estado(empleadoActual.getEstado())
				.numeroTelefono(UtilTexto.aplicarTrim(empleadoActualizar.getNumeroTelefono()))
				.correoElectronico(UtilTexto.aplicarTrim(empleadoActualizar.getCorreoElectronico()))
				.direccionResidencia(UtilTexto.aplicarTrim(empleadoActualizar.getDireccionResidencia()))
				.tipoDocumentoIdentificacion(empleadoActualizar.getTipoDocumentoIdentificacion())
				.cargo(empleadoActualizar.getCargo())
				.ciudadResidencia(empleadoActualizar.getCiudadResidencia())
				.build();

		actualizar(empleadoActualizado);

		logger.info("Empleado actualizado satisfactoriamente.");
	}

	private String validarYNormalizarCodigoEmpleado(final String codigoEmpleado) {
		if (!UtilTexto.tieneTexto(codigoEmpleado)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del empleado es obligatorio.");
		}

		var codigoEmpleadoNormalizado = UtilTexto.aplicarTrimConvertirMayusculas(codigoEmpleado);

		if (codigoEmpleadoNormalizado.length() != LONGITUD_CODIGO_EMPLEADO) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del empleado debe tener exactamente "
							+ LONGITUD_CODIGO_EMPLEADO + " caracteres.");
		}

		if (!iniciaConPrefijoEmpleado(codigoEmpleadoNormalizado)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del empleado debe iniciar con " + PREFIJO_EMPLEADO + ".");
		}

		if (!contieneSoloDigitos(
				codigoEmpleadoNormalizado.substring(POSICION_INICIO_DIGITOS_EMPLEADO))) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del empleado debe tener el formato EMP seguido de tres dígitos numéricos.");
		}

		return codigoEmpleadoNormalizado;
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

	private EmpleadoDominio validarYObtenerEmpleado(final String codigoEmpleado) {
		var empleadoEntidad = daoFactory.obtenerEmpleadoDAO()
				.consultarPorId(codigoEmpleado);

		if (UtilObjeto.esNulo(empleadoEntidad)
				|| !UtilTexto.tieneTexto(empleadoEntidad.getCodigoEmpleado())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe un empleado registrado con el código indicado.");
		}

		return EmpleadoEntidadAssembler.getInstance()
				.ensamblarDominio(empleadoEntidad);
	}

	private void validarNoExisteOtroEmpleadoConMismoTipoDocumentoYNumeroIdentificacion(
			final String codigoEmpleado,
			final EmpleadoDominio empleadoActualizar) {

		var empleados = consultarTodosLosEmpleados();

		for (EmpleadoEntidad empleadoEntidad : empleados) {
			var empleadoSeguro = UtilObjeto.obtenerValorDefecto(
					empleadoEntidad,
					EmpleadoEntidad.builder().build());

			var esMismoEmpleado = UtilTexto.sonIgualesIgnorandoMayusculas(
					empleadoSeguro.getCodigoEmpleado(),
					codigoEmpleado);

			var mismoNumeroIdentificacion = UtilTexto.sonIgualesIgnorandoMayusculas(
					empleadoSeguro.getNumeroIdentificacion(),
					empleadoActualizar.getNumeroIdentificacion());

			var mismoTipoDocumento = UtilTexto.sonIgualesIgnorandoMayusculas(
					empleadoSeguro.getTipoDocumentoIdentificacion().getCodigoTipoDocumentoIdentificacion(),
					empleadoActualizar.getTipoDocumentoIdentificacion().getCodigoTipoDocumentoIdentificacion());

			if (!esMismoEmpleado && mismoNumeroIdentificacion && mismoTipoDocumento) {
				throw NegocioPatioMaruExcepcion.crear(
						"Ya existe otro empleado registrado con el mismo tipo de documento y número de identificación.");
			}
		}
	}

	private void validarNoExisteOtroEmpleadoConMismoNumeroTelefono(
			final String codigoEmpleado,
			final String numeroTelefono) {

		var empleados = consultarTodosLosEmpleados();

		for (EmpleadoEntidad empleadoEntidad : empleados) {
			var empleadoSeguro = UtilObjeto.obtenerValorDefecto(
					empleadoEntidad,
					EmpleadoEntidad.builder().build());

			var esMismoEmpleado = UtilTexto.sonIgualesIgnorandoMayusculas(
					empleadoSeguro.getCodigoEmpleado(),
					codigoEmpleado);

			var mismoTelefono = UtilTexto.sonIgualesIgnorandoMayusculas(
					empleadoSeguro.getNumeroTelefono(),
					numeroTelefono);

			if (!esMismoEmpleado && mismoTelefono) {
				throw NegocioPatioMaruExcepcion.crear(
						"Ya existe otro empleado registrado con el mismo número de teléfono.");
			}
		}
	}

	private void validarNoExisteOtroEmpleadoConMismoCorreoElectronico(
			final String codigoEmpleado,
			final String correoElectronico) {

		var empleados = consultarTodosLosEmpleados();

		for (EmpleadoEntidad empleadoEntidad : empleados) {
			var empleadoSeguro = UtilObjeto.obtenerValorDefecto(
					empleadoEntidad,
					EmpleadoEntidad.builder().build());

			var esMismoEmpleado = UtilTexto.sonIgualesIgnorandoMayusculas(
					empleadoSeguro.getCodigoEmpleado(),
					codigoEmpleado);

			var mismoCorreoElectronico = UtilTexto.sonIgualesIgnorandoMayusculas(
					empleadoSeguro.getCorreoElectronico(),
					correoElectronico);

			if (!esMismoEmpleado && mismoCorreoElectronico) {
				throw NegocioPatioMaruExcepcion.crear(
						"Ya existe otro empleado registrado con el mismo correo electrónico.");
			}
		}
	}

	private List<EmpleadoEntidad> consultarTodosLosEmpleados() {
		return UtilObjeto.obtenerValorDefecto(
				daoFactory.obtenerEmpleadoDAO().consultar(EmpleadoEntidad.builder().build()),
				List.<EmpleadoEntidad>of());
	}

	private Integer calcularEdad(final LocalDate fechaNacimiento) {
		return Period.between(fechaNacimiento, LocalDate.now()).getYears();
	}

	private void actualizar(final EmpleadoDominio empleado) {
		var empleadoEntidad = EmpleadoEntidadAssembler.getInstance()
				.ensamblarEntidad(empleado);

		daoFactory.obtenerEmpleadoDAO()
				.actualizar(empleadoEntidad);
	}

	private boolean iniciaConPrefijoEmpleado(final String codigoEmpleado) {
		var prefijo = codigoEmpleado.substring(0, POSICION_INICIO_DIGITOS_EMPLEADO);

		return UtilTexto.sonIgualesIgnorandoMayusculas(
				prefijo,
				PREFIJO_EMPLEADO);
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
