package co.edu.uco.patiomaruparking.negocio.casouso.empleado.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.EmpleadoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.EmpleadoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.empleado.ConsultarEmpleadosCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.EmpleadoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class ConsultarEmpleadosCasoUsoImpl implements ConsultarEmpleadosCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(ConsultarEmpleadosCasoUsoImpl.class);

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

	private final DAOFactory daoFactory;

	public ConsultarEmpleadosCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para consultar empleados porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public List<EmpleadoDominio> ejecutar(final EmpleadoDominio filtro) {
		logger.info("Iniciando la consulta de empleados.");

		var filtroSeguro = UtilObjeto.obtenerValorDefecto(
				filtro,
				EmpleadoDominio.builder().build());

		validarFiltro(filtroSeguro);

		var filtroEntidad = EmpleadoEntidadAssembler.getInstance()
				.ensamblarEntidad(filtroSeguro);

		var empleadosEntidad = UtilObjeto.obtenerValorDefecto(
				daoFactory.obtenerEmpleadoDAO().consultar(filtroEntidad),
				List.<EmpleadoEntidad>of());

		var empleados = new ArrayList<EmpleadoDominio>();

		for (EmpleadoEntidad empleadoEntidad : empleadosEntidad) {
			var empleado = EmpleadoEntidadAssembler.getInstance()
					.ensamblarDominio(empleadoEntidad);

			empleados.add(empleado);
		}

		logger.info("Consulta de empleados finalizada satisfactoriamente.");

		return empleados;
	}

	private void validarFiltro(final EmpleadoDominio filtro) {
		validarCodigoEmpleadoSiFueInformado(filtro);
		validarNumeroIdentificacionSiFueInformado(filtro);
		validarNombresSiFueronInformados(filtro);
		validarApellidosSiFueronInformados(filtro);
		validarFechaNacimientoSiFueInformada(filtro);
		validarEdadSiFueInformada(filtro);
		validarTelefonoSiFueInformado(filtro);
		validarCorreoElectronicoSiFueInformado(filtro);
		validarDireccionResidenciaSiFueInformada(filtro);
		validarTipoDocumentoIdentificacionSiFueInformado(filtro);
		validarCargoSiFueInformado(filtro);
		validarCiudadResidenciaSiFueInformada(filtro);
	}

	private void validarCodigoEmpleadoSiFueInformado(final EmpleadoDominio filtro) {
		if (!UtilTexto.tieneTexto(filtro.getCodigoEmpleado())) {
			return;
		}

		var codigoEmpleado = UtilTexto.aplicarTrimConvertirMayusculas(filtro.getCodigoEmpleado());

		if (codigoEmpleado.length() != LONGITUD_CODIGO_EMPLEADO) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del empleado debe tener exactamente "
							+ LONGITUD_CODIGO_EMPLEADO + " caracteres.");
		}

		if (!iniciaConPrefijoEmpleado(codigoEmpleado)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del empleado debe iniciar con " + PREFIJO_EMPLEADO + ".");
		}

		if (!contieneSoloDigitos(
				codigoEmpleado.substring(POSICION_INICIO_DIGITOS_EMPLEADO))) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del empleado debe tener el formato EMP seguido de tres dígitos numéricos.");
		}
	}

	private void validarNumeroIdentificacionSiFueInformado(final EmpleadoDominio filtro) {
		if (!UtilTexto.tieneTexto(filtro.getNumeroIdentificacion())) {
			return;
		}

		validarLongitud(
				filtro.getNumeroIdentificacion(),
				"El número de identificación",
				LONGITUD_MINIMA_NUMERO_IDENTIFICACION,
				LONGITUD_MAXIMA_NUMERO_IDENTIFICACION);

		if (!contieneSoloDigitos(filtro.getNumeroIdentificacion())) {
			throw NegocioPatioMaruExcepcion.crear(
					"El número de identificación del empleado solo debe contener dígitos.");
		}
	}

	private void validarNombresSiFueronInformados(final EmpleadoDominio filtro) {
		validarTextoSiFueInformado(
				filtro.getPrimerNombre(),
				"El primer nombre",
				LONGITUD_MINIMA_NOMBRE,
				LONGITUD_MAXIMA_NOMBRE);

		validarTextoSiFueInformado(
				filtro.getSegundoNombre(),
				"El segundo nombre",
				LONGITUD_MINIMA_NOMBRE,
				LONGITUD_MAXIMA_NOMBRE);
	}

	private void validarApellidosSiFueronInformados(final EmpleadoDominio filtro) {
		validarTextoSiFueInformado(
				filtro.getPrimerApellido(),
				"El primer apellido",
				LONGITUD_MINIMA_APELLIDO,
				LONGITUD_MAXIMA_APELLIDO);

		validarTextoSiFueInformado(
				filtro.getSegundoApellido(),
				"El segundo apellido",
				LONGITUD_MINIMA_APELLIDO,
				LONGITUD_MAXIMA_APELLIDO);
	}

	private void validarFechaNacimientoSiFueInformada(final EmpleadoDominio filtro) {
		if (UtilObjeto.esNulo(filtro.getFechaNacimiento())) {
			return;
		}

		if (filtro.getFechaNacimiento().isAfter(LocalDate.now())) {
			throw NegocioPatioMaruExcepcion.crear(
					"La fecha de nacimiento del empleado no puede ser futura.");
		}
	}

	private void validarEdadSiFueInformada(final EmpleadoDominio filtro) {
		if (UtilObjeto.esNulo(filtro.getEdad())) {
			return;
		}

		if (filtro.getEdad() < 0) {
			throw NegocioPatioMaruExcepcion.crear(
					"La edad del empleado no puede ser negativa.");
		}
	}

	private void validarTelefonoSiFueInformado(final EmpleadoDominio filtro) {
		if (!UtilTexto.tieneTexto(filtro.getNumeroTelefono())) {
			return;
		}

		validarLongitud(
				filtro.getNumeroTelefono(),
				"El número de teléfono",
				LONGITUD_MINIMA_TELEFONO,
				LONGITUD_MAXIMA_TELEFONO);

		if (!contieneSoloDigitos(filtro.getNumeroTelefono())) {
			throw NegocioPatioMaruExcepcion.crear(
					"El número de teléfono del empleado solo debe contener dígitos.");
		}
	}

	private void validarCorreoElectronicoSiFueInformado(final EmpleadoDominio filtro) {
		if (!UtilTexto.tieneTexto(filtro.getCorreoElectronico())) {
			return;
		}

		validarLongitud(
				filtro.getCorreoElectronico(),
				"El correo electrónico",
				LONGITUD_MINIMA_CORREO_ELECTRONICO,
				LONGITUD_MAXIMA_CORREO_ELECTRONICO);

		if (!tieneFormatoBasicoCorreoElectronico(filtro.getCorreoElectronico())) {
			throw NegocioPatioMaruExcepcion.crear(
					"El correo electrónico del empleado no tiene un formato válido.");
		}
	}

	private void validarDireccionResidenciaSiFueInformada(final EmpleadoDominio filtro) {
		validarTextoSiFueInformado(
				filtro.getDireccionResidencia(),
				"La dirección de residencia",
				LONGITUD_MINIMA_DIRECCION_RESIDENCIA,
				LONGITUD_MAXIMA_DIRECCION_RESIDENCIA);
	}

	private void validarTipoDocumentoIdentificacionSiFueInformado(final EmpleadoDominio filtro) {
		if (!UtilTexto.tieneTexto(
				filtro.getTipoDocumentoIdentificacion().getCodigoTipoDocumentoIdentificacion())) {
			return;
		}

		validarLongitudExacta(
				filtro.getTipoDocumentoIdentificacion().getCodigoTipoDocumentoIdentificacion(),
				LONGITUD_CODIGO_TIPO_DOCUMENTO_IDENTIFICACION,
				"El código del tipo de documento de identificación");
	}

	private void validarCargoSiFueInformado(final EmpleadoDominio filtro) {
		if (!UtilTexto.tieneTexto(filtro.getCargo().getCodigoCargo())) {
			return;
		}

		validarLongitudExacta(
				filtro.getCargo().getCodigoCargo(),
				LONGITUD_CODIGO_CARGO,
				"El código del cargo");
	}

	private void validarCiudadResidenciaSiFueInformada(final EmpleadoDominio filtro) {
		if (!UtilTexto.tieneTexto(filtro.getCiudadResidencia().getCodigoCiudadResidencia())) {
			return;
		}

		validarLongitudExacta(
				filtro.getCiudadResidencia().getCodigoCiudadResidencia(),
				LONGITUD_CODIGO_CIUDAD_RESIDENCIA,
				"El código de la ciudad de residencia");
	}

	private void validarTextoSiFueInformado(
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