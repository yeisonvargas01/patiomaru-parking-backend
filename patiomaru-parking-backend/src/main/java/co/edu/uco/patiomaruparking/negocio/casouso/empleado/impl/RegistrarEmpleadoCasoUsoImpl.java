package co.edu.uco.patiomaruparking.negocio.casouso.empleado.impl;

import java.time.LocalDate;
import java.time.Period;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.EmpleadoEntidad;
import co.edu.uco.patiomaruparking.entidad.TipoDocumentoIdentificacionEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.EmpleadoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.empleado.RegistrarEmpleadoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.EmpleadoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilCodigo;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public final class RegistrarEmpleadoCasoUsoImpl implements RegistrarEmpleadoCasoUso {

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

	public RegistrarEmpleadoCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public EmpleadoDominio ejecutar(final EmpleadoDominio datos) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango.
		validarDatosConsistentes(datos);

		// 2. No debe existir un empleado con la misma combinación única documentada:
		// tipoDocumentoIdentificacion + numeroIdentificacion.
		validarNoExisteEmpleadoConMismoTipoDocumentoYNumeroIdentificacion(datos);

		// 3. No debe existir un empleado con el mismo número de teléfono.
		validarNoExisteEmpleadoConMismoNumeroTelefono(datos.getNumeroTelefono());

		// 4. No debe existir un empleado con el mismo correo electrónico.
		validarNoExisteEmpleadoConMismoCorreoElectronico(datos.getCorreoElectronico());

		// 5. El código del empleado debe ser único.
		var codigoEmpleado = generarCodigoUnicoEmpleado();

		var empleadoPreparado = EmpleadoDominio.builder()
				.codigoEmpleado(codigoEmpleado)
				.numeroIdentificacion(UtilTexto.aplicarTrim(datos.getNumeroIdentificacion()))
				.primerNombre(UtilTexto.aplicarTrim(datos.getPrimerNombre()))
				.segundoNombre(UtilTexto.aplicarTrim(datos.getSegundoNombre()))
				.primerApellido(UtilTexto.aplicarTrim(datos.getPrimerApellido()))
				.segundoApellido(UtilTexto.aplicarTrim(datos.getSegundoApellido()))
				.fechaNacimiento(datos.getFechaNacimiento())
				.edad(calcularEdad(datos.getFechaNacimiento()))
				.estado(datos.getEstado())
				.numeroTelefono(UtilTexto.aplicarTrim(datos.getNumeroTelefono()))
				.correoElectronico(UtilTexto.aplicarTrimConvertirMayusculas(datos.getCorreoElectronico()))
				.direccionResidencia(UtilTexto.aplicarTrim(datos.getDireccionResidencia()))
				.tipoDocumentoIdentificacion(datos.getTipoDocumentoIdentificacion())
				.cargo(datos.getCargo())
				.ciudadResidencia(datos.getCiudadResidencia())
				.build();

		guardar(empleadoPreparado);

		return empleadoPreparado;
	}

	private void validarDatosConsistentes(final EmpleadoDominio datos) {
		if (UtilObjeto.esNulo(datos)) {
			throw new RuntimeException("Los datos del empleado son obligatorios.");
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

		if (UtilObjeto.esNulo(datos.getEstado())) {
			throw new RuntimeException("El estado del empleado es obligatorio.");
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

	private void validarNoExisteEmpleadoConMismoTipoDocumentoYNumeroIdentificacion(final EmpleadoDominio datos) {
		var filtro = EmpleadoEntidad.builder()
				.numeroIdentificacion(UtilTexto.aplicarTrim(datos.getNumeroIdentificacion()))
				.tipoDocumentoIdentificacion(TipoDocumentoIdentificacionEntidad.builder()
						.codigoTipoDocumentoIdentificacion(UtilTexto.aplicarTrim(datos
								.getTipoDocumentoIdentificacion()
								.getCodigoTipoDocumentoIdentificacion()))
						.build())
				.build();

		var resultados = daoFactory.obtenerEmpleadoDAO().consultar(filtro);

		if (UtilObjeto.noEsNulo(resultados) && !resultados.isEmpty()) {
			throw new RuntimeException(
					"Ya existe un empleado registrado con el mismo tipo de documento y número de identificación.");
		}
	}

	private void validarNoExisteEmpleadoConMismoNumeroTelefono(final String numeroTelefono) {
		var filtro = EmpleadoEntidad.builder()
				.numeroTelefono(UtilTexto.aplicarTrim(numeroTelefono))
				.build();

		var resultados = daoFactory.obtenerEmpleadoDAO().consultar(filtro);

		if (UtilObjeto.noEsNulo(resultados) && !resultados.isEmpty()) {
			throw new RuntimeException("Ya existe un empleado registrado con el mismo número de teléfono.");
		}
	}

	private void validarNoExisteEmpleadoConMismoCorreoElectronico(final String correoElectronico) {
		var filtro = EmpleadoEntidad.builder()
				.correoElectronico(UtilTexto.aplicarTrimConvertirMayusculas(correoElectronico))
				.build();

		var resultados = daoFactory.obtenerEmpleadoDAO().consultar(filtro);

		if (UtilObjeto.noEsNulo(resultados) && !resultados.isEmpty()) {
			throw new RuntimeException("Ya existe un empleado registrado con el mismo correo electrónico.");
		}
	}

	private String generarCodigoUnicoEmpleado() {
		String codigoEmpleado;
		EmpleadoEntidad empleadoExistente;

		do {
			codigoEmpleado = UtilCodigo.generarCodigo("EMP", 3);

			if (UtilTexto.aplicarTrim(codigoEmpleado).length() != LONGITUD_CODIGO_EMPLEADO) {
				throw new RuntimeException("El código del empleado generado debe tener exactamente "
						+ LONGITUD_CODIGO_EMPLEADO + " caracteres.");
			}

			empleadoExistente = daoFactory.obtenerEmpleadoDAO().consultarPorId(codigoEmpleado);

		} while (UtilObjeto.noEsNulo(empleadoExistente));

		return codigoEmpleado;
	}

	private Integer calcularEdad(final LocalDate fechaNacimiento) {
		return Period.between(fechaNacimiento, LocalDate.now()).getYears();
	}

	private void guardar(final EmpleadoDominio empleado) {
		var empleadoEntidad = EmpleadoEntidadAssembler.getInstance().ensamblarEntidad(empleado);
		daoFactory.obtenerEmpleadoDAO().registrar(empleadoEntidad);
	}
}
