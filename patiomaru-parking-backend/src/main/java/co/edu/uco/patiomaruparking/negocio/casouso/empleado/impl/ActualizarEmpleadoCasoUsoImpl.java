package co.edu.uco.patiomaruparking.negocio.casouso.empleado.impl;

import java.time.LocalDate;
import java.time.Period;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.EmpleadoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.EmpleadoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.empleado.ActualizarEmpleadoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.EmpleadoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public final class ActualizarEmpleadoCasoUsoImpl implements ActualizarEmpleadoCasoUso {

	private static final int LONGITUD_CODIGO_EMPLEADO = 6;
	private static final int LONGITUD_MAXIMA_NUMERO_IDENTIFICACION = 15;
	private static final int LONGITUD_MAXIMA_NOMBRE = 40;
	private static final int LONGITUD_MAXIMA_APELLIDO = 40;
	private static final int LONGITUD_MAXIMA_TELEFONO = 15;
	private static final int LONGITUD_MAXIMA_CORREO_ELECTRONICO = 100;
	private static final int LONGITUD_MAXIMA_DIRECCION_RESIDENCIA = 120;
	private static final int LONGITUD_CODIGO_TIPO_DOCUMENTO_IDENTIFICACION = 7;
	private static final int LONGITUD_CODIGO_CARGO = 6;
	private static final int LONGITUD_CODIGO_CIUDAD_RESIDENCIA = 6;

	private final DAOFactory daoFactory;

	public ActualizarEmpleadoCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final EmpleadoDominio datos) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango.
		validarDatosConsistentes(datos);

		var codigoEmpleado = UtilTexto.aplicarTrimConvertirMayusculas(datos.getCodigoEmpleado());

		// 2. Debe existir el empleado que se desea actualizar.
		var empleadoActualEntidad = daoFactory.obtenerEmpleadoDAO().consultarPorId(codigoEmpleado);

		if (UtilObjeto.esNulo(empleadoActualEntidad)) {
			throw new RuntimeException("No existe un empleado registrado con el código indicado.");
		}

		var empleadoActual = EmpleadoEntidadAssembler.getInstance().ensamblarDominio(empleadoActualEntidad);

		// 3. No debe existir otro empleado con la misma combinación única documentada:
		// tipoDocumentoIdentificacion + numeroIdentificacion.
		validarNoExisteOtroEmpleadoConMismoTipoDocumentoYNumeroIdentificacion(datos);

		// 4. No debe existir otro empleado con el mismo número de teléfono.
		validarNoExisteOtroEmpleadoConMismoNumeroTelefono(datos);

		// 5. No debe existir otro empleado con el mismo correo electrónico.
		validarNoExisteOtroEmpleadoConMismoCorreoElectronico(datos);

		// 6. Actualizar información del empleado.
		var empleadoActualizado = EmpleadoDominio.builder()
				.codigoEmpleado(codigoEmpleado)
				.numeroIdentificacion(UtilTexto.aplicarTrim(datos.getNumeroIdentificacion()))
				.primerNombre(UtilTexto.aplicarTrim(datos.getPrimerNombre()))
				.segundoNombre(UtilTexto.aplicarTrim(datos.getSegundoNombre()))
				.primerApellido(UtilTexto.aplicarTrim(datos.getPrimerApellido()))
				.segundoApellido(UtilTexto.aplicarTrim(datos.getSegundoApellido()))
				.fechaNacimiento(datos.getFechaNacimiento())
				.edad(calcularEdad(datos.getFechaNacimiento()))
				.estado(empleadoActual.getEstado())
				.numeroTelefono(UtilTexto.aplicarTrim(datos.getNumeroTelefono()))
				.correoElectronico(UtilTexto.aplicarTrim(datos.getCorreoElectronico()))
				.direccionResidencia(UtilTexto.aplicarTrim(datos.getDireccionResidencia()))
				.tipoDocumentoIdentificacion(datos.getTipoDocumentoIdentificacion())
				.cargo(datos.getCargo())
				.ciudadResidencia(datos.getCiudadResidencia())
				.build();

		actualizar(empleadoActualizado);
	}

	private void validarDatosConsistentes(final EmpleadoDominio datos) {
		if (UtilObjeto.esNulo(datos)) {
			throw new RuntimeException("Los datos del empleado son obligatorios.");
		}

		if (!UtilTexto.tieneTexto(datos.getCodigoEmpleado())) {
			throw new RuntimeException("El código del empleado es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(datos.getCodigoEmpleado()).length() != LONGITUD_CODIGO_EMPLEADO) {
			throw new RuntimeException("El código del empleado debe tener exactamente "
					+ LONGITUD_CODIGO_EMPLEADO + " caracteres.");
		}

		validarTextoObligatorio(datos.getNumeroIdentificacion(), "El número de identificación",
				LONGITUD_MAXIMA_NUMERO_IDENTIFICACION);

		validarTextoObligatorio(datos.getPrimerNombre(), "El primer nombre", LONGITUD_MAXIMA_NOMBRE);
		validarTextoObligatorio(datos.getSegundoNombre(), "El segundo nombre", LONGITUD_MAXIMA_NOMBRE);
		validarTextoObligatorio(datos.getPrimerApellido(), "El primer apellido", LONGITUD_MAXIMA_APELLIDO);
		validarTextoObligatorio(datos.getSegundoApellido(), "El segundo apellido", LONGITUD_MAXIMA_APELLIDO);

		if (UtilObjeto.esNulo(datos.getFechaNacimiento())) {
			throw new RuntimeException("La fecha de nacimiento del empleado es obligatoria.");
		}

		if (datos.getFechaNacimiento().isAfter(LocalDate.now())) {
			throw new RuntimeException("La fecha de nacimiento del empleado no puede ser futura.");
		}

		if (calcularEdad(datos.getFechaNacimiento()) < 18) {
			throw new RuntimeException("El empleado debe ser mayor de edad.");
		}

		validarTextoObligatorio(datos.getNumeroTelefono(), "El número de teléfono", LONGITUD_MAXIMA_TELEFONO);

		validarTextoObligatorio(datos.getCorreoElectronico(), "El correo electrónico",
				LONGITUD_MAXIMA_CORREO_ELECTRONICO);
		validarFormatoCorreoElectronico(datos.getCorreoElectronico());

		validarTextoObligatorio(datos.getDireccionResidencia(), "La dirección de residencia",
				LONGITUD_MAXIMA_DIRECCION_RESIDENCIA);

		validarTipoDocumentoIdentificacion(datos);
		validarCargo(datos);
		validarCiudadResidencia(datos);
	}

	private void validarTextoObligatorio(final String valor, final String nombreCampo, final int longitudMaxima) {
		if (!UtilTexto.tieneTexto(valor)) {
			throw new RuntimeException(nombreCampo + " del empleado es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(valor).length() > longitudMaxima) {
			throw new RuntimeException(nombreCampo + " del empleado no puede superar "
					+ longitudMaxima + " caracteres.");
		}
	}

	private void validarTipoDocumentoIdentificacion(final EmpleadoDominio datos) {
		if (UtilObjeto.esNulo(datos.getTipoDocumentoIdentificacion())
				|| !UtilTexto.tieneTexto(datos.getTipoDocumentoIdentificacion()
						.getCodigoTipoDocumentoIdentificacion())) {
			throw new RuntimeException("El tipo de documento de identificación del empleado es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(datos.getTipoDocumentoIdentificacion()
				.getCodigoTipoDocumentoIdentificacion()).length() != LONGITUD_CODIGO_TIPO_DOCUMENTO_IDENTIFICACION) {
			throw new RuntimeException("El código del tipo de documento de identificación debe tener exactamente "
					+ LONGITUD_CODIGO_TIPO_DOCUMENTO_IDENTIFICACION + " caracteres.");
		}
	}

	private void validarCargo(final EmpleadoDominio datos) {
		if (UtilObjeto.esNulo(datos.getCargo())
				|| !UtilTexto.tieneTexto(datos.getCargo().getCodigoCargo())) {
			throw new RuntimeException("El cargo del empleado es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(datos.getCargo().getCodigoCargo()).length() != LONGITUD_CODIGO_CARGO) {
			throw new RuntimeException("El código del cargo debe tener exactamente "
					+ LONGITUD_CODIGO_CARGO + " caracteres.");
		}
	}

	private void validarCiudadResidencia(final EmpleadoDominio datos) {
		if (UtilObjeto.esNulo(datos.getCiudadResidencia())
				|| !UtilTexto.tieneTexto(datos.getCiudadResidencia().getCodigoCiudadResidencia())) {
			throw new RuntimeException("La ciudad de residencia del empleado es obligatoria.");
		}

		if (UtilTexto.aplicarTrim(datos.getCiudadResidencia()
				.getCodigoCiudadResidencia()).length() != LONGITUD_CODIGO_CIUDAD_RESIDENCIA) {
			throw new RuntimeException("El código de la ciudad de residencia debe tener exactamente "
					+ LONGITUD_CODIGO_CIUDAD_RESIDENCIA + " caracteres.");
		}
	}

	private void validarFormatoCorreoElectronico(final String correoElectronico) {
		var correo = UtilTexto.aplicarTrim(correoElectronico);

		if (!correo.contains("@") || !correo.contains(".")) {
			throw new RuntimeException("El correo electrónico del empleado no tiene un formato válido.");
		}
	}

	private void validarNoExisteOtroEmpleadoConMismoTipoDocumentoYNumeroIdentificacion(final EmpleadoDominio datos) {
		var empleados = daoFactory.obtenerEmpleadoDAO().consultar(EmpleadoEntidad.builder().build());

		if (UtilObjeto.esNulo(empleados) || empleados.isEmpty()) {
			return;
		}

		for (EmpleadoEntidad empleado : empleados) {
			var mismoEmpleado = UtilTexto.sonIgualesIgnorandoMayusculas(
					empleado.getCodigoEmpleado(), datos.getCodigoEmpleado());

			var mismoNumeroIdentificacion = UtilTexto.sonIgualesIgnorandoMayusculas(
					empleado.getNumeroIdentificacion(), datos.getNumeroIdentificacion());

			var mismoTipoDocumento = UtilTexto.sonIgualesIgnorandoMayusculas(
					empleado.getTipoDocumentoIdentificacion().getCodigoTipoDocumentoIdentificacion(),
					datos.getTipoDocumentoIdentificacion().getCodigoTipoDocumentoIdentificacion());

			if (!mismoEmpleado && mismoNumeroIdentificacion && mismoTipoDocumento) {
				throw new RuntimeException(
						"Ya existe otro empleado registrado con el mismo tipo de documento y número de identificación.");
			}
		}
	}

	private void validarNoExisteOtroEmpleadoConMismoNumeroTelefono(final EmpleadoDominio datos) {
		var empleados = daoFactory.obtenerEmpleadoDAO().consultar(EmpleadoEntidad.builder().build());

		if (UtilObjeto.esNulo(empleados) || empleados.isEmpty()) {
			return;
		}

		for (EmpleadoEntidad empleado : empleados) {
			var mismoEmpleado = UtilTexto.sonIgualesIgnorandoMayusculas(
					empleado.getCodigoEmpleado(), datos.getCodigoEmpleado());

			var mismoTelefono = UtilTexto.sonIgualesIgnorandoMayusculas(
					empleado.getNumeroTelefono(), datos.getNumeroTelefono());

			if (!mismoEmpleado && mismoTelefono) {
				throw new RuntimeException("Ya existe otro empleado registrado con el mismo número de teléfono.");
			}
		}
	}

	private void validarNoExisteOtroEmpleadoConMismoCorreoElectronico(final EmpleadoDominio datos) {
		var empleados = daoFactory.obtenerEmpleadoDAO().consultar(EmpleadoEntidad.builder().build());

		if (UtilObjeto.esNulo(empleados) || empleados.isEmpty()) {
			return;
		}

		for (EmpleadoEntidad empleado : empleados) {
			var mismoEmpleado = UtilTexto.sonIgualesIgnorandoMayusculas(
					empleado.getCodigoEmpleado(), datos.getCodigoEmpleado());

			var mismoCorreo = UtilTexto.sonIgualesIgnorandoMayusculas(
					empleado.getCorreoElectronico(), datos.getCorreoElectronico());

			if (!mismoEmpleado && mismoCorreo) {
				throw new RuntimeException("Ya existe otro empleado registrado con el mismo correo electrónico.");
			}
		}
	}

	private Integer calcularEdad(final LocalDate fechaNacimiento) {
		return Period.between(fechaNacimiento, LocalDate.now()).getYears();
	}

	private void actualizar(final EmpleadoDominio empleado) {
		var empleadoEntidad = EmpleadoEntidadAssembler.getInstance().ensamblarEntidad(empleado);
		daoFactory.obtenerEmpleadoDAO().actualizar(empleadoEntidad);
	}
}
