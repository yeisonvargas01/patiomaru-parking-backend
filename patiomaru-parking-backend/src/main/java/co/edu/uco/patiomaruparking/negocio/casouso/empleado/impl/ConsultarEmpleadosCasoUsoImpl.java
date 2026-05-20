package co.edu.uco.patiomaruparking.negocio.casouso.empleado.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.EmpleadoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.empleado.ConsultarEmpleadosCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.EmpleadoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public final class ConsultarEmpleadosCasoUsoImpl implements ConsultarEmpleadosCasoUso {

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

	public ConsultarEmpleadosCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public List<EmpleadoDominio> ejecutar(final EmpleadoDominio filtro) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango cuando se envían filtros.
		validarFiltro(filtro);

		// 2. Consultar información de los empleados según los filtros enviados.
		var filtroSeguro = UtilObjeto.esNulo(filtro)
				? EmpleadoDominio.builder().build()
				: filtro;

		var filtroEntidad = EmpleadoEntidadAssembler.getInstance().ensamblarEntidad(filtroSeguro);

		var empleadosEntidad = daoFactory.obtenerEmpleadoDAO().consultar(filtroEntidad);

		if (UtilObjeto.esNulo(empleadosEntidad) || empleadosEntidad.isEmpty()) {
			return List.of();
		}

		// 3. Ensamblar los resultados de Entidad a Dominio.
		return empleadosEntidad.stream()
				.map(EmpleadoEntidadAssembler.getInstance()::ensamblarDominio)
				.collect(Collectors.toList());
	}

	private void validarFiltro(final EmpleadoDominio filtro) {
		if (UtilObjeto.esNulo(filtro)) {
			return;
		}

		if (UtilTexto.tieneTexto(filtro.getCodigoEmpleado())
				&& UtilTexto.aplicarTrim(filtro.getCodigoEmpleado()).length() != LONGITUD_CODIGO_EMPLEADO) {
			throw new RuntimeException("El código del empleado debe tener exactamente "
					+ LONGITUD_CODIGO_EMPLEADO + " caracteres.");
		}

		validarTextoOpcional(filtro.getNumeroIdentificacion(), "El número de identificación",
				LONGITUD_MAXIMA_NUMERO_IDENTIFICACION);

		validarTextoOpcional(filtro.getPrimerNombre(), "El primer nombre", LONGITUD_MAXIMA_NOMBRE);
		validarTextoOpcional(filtro.getSegundoNombre(), "El segundo nombre", LONGITUD_MAXIMA_NOMBRE);
		validarTextoOpcional(filtro.getPrimerApellido(), "El primer apellido", LONGITUD_MAXIMA_APELLIDO);
		validarTextoOpcional(filtro.getSegundoApellido(), "El segundo apellido", LONGITUD_MAXIMA_APELLIDO);

		if (UtilObjeto.noEsNulo(filtro.getFechaNacimiento())
				&& filtro.getFechaNacimiento().isAfter(LocalDate.now())) {
			throw new RuntimeException("La fecha de nacimiento del empleado no puede ser futura.");
		}

		if (UtilObjeto.noEsNulo(filtro.getEdad()) && filtro.getEdad() < 0) {
			throw new RuntimeException("La edad del empleado no puede ser negativa.");
		}

		validarTextoOpcional(filtro.getNumeroTelefono(), "El número de teléfono", LONGITUD_MAXIMA_TELEFONO);

		if (UtilTexto.tieneTexto(filtro.getCorreoElectronico())) {
			validarTextoOpcional(filtro.getCorreoElectronico(), "El correo electrónico",
					LONGITUD_MAXIMA_CORREO_ELECTRONICO);
			validarFormatoCorreoElectronico(filtro.getCorreoElectronico());
		}

		validarTextoOpcional(filtro.getDireccionResidencia(), "La dirección de residencia",
				LONGITUD_MAXIMA_DIRECCION_RESIDENCIA);

		validarTipoDocumentoIdentificacionSiAplica(filtro);
		validarCargoSiAplica(filtro);
		validarCiudadResidenciaSiAplica(filtro);
	}

	private void validarTextoOpcional(final String valor, final String nombreCampo, final int longitudMaxima) {
		if (UtilTexto.tieneTexto(valor) && UtilTexto.aplicarTrim(valor).length() > longitudMaxima) {
			throw new RuntimeException(nombreCampo + " del empleado no puede superar "
					+ longitudMaxima + " caracteres.");
		}
	}

	private void validarFormatoCorreoElectronico(final String correoElectronico) {
		var correo = UtilTexto.aplicarTrim(correoElectronico);

		if (!correo.contains("@") || !correo.contains(".")) {
			throw new RuntimeException("El correo electrónico del empleado no tiene un formato válido.");
		}
	}

	private void validarTipoDocumentoIdentificacionSiAplica(final EmpleadoDominio filtro) {
		if (UtilObjeto.noEsNulo(filtro.getTipoDocumentoIdentificacion())
				&& UtilTexto.tieneTexto(filtro.getTipoDocumentoIdentificacion()
						.getCodigoTipoDocumentoIdentificacion())
				&& UtilTexto.aplicarTrim(filtro.getTipoDocumentoIdentificacion()
						.getCodigoTipoDocumentoIdentificacion()).length() != LONGITUD_CODIGO_TIPO_DOCUMENTO_IDENTIFICACION) {

			throw new RuntimeException("El código del tipo de documento de identificación debe tener exactamente "
					+ LONGITUD_CODIGO_TIPO_DOCUMENTO_IDENTIFICACION + " caracteres.");
		}
	}

	private void validarCargoSiAplica(final EmpleadoDominio filtro) {
		if (UtilObjeto.noEsNulo(filtro.getCargo())
				&& UtilTexto.tieneTexto(filtro.getCargo().getCodigoCargo())
				&& UtilTexto.aplicarTrim(filtro.getCargo().getCodigoCargo()).length() != LONGITUD_CODIGO_CARGO) {

			throw new RuntimeException("El código del cargo debe tener exactamente "
					+ LONGITUD_CODIGO_CARGO + " caracteres.");
		}
	}

	private void validarCiudadResidenciaSiAplica(final EmpleadoDominio filtro) {
		if (UtilObjeto.noEsNulo(filtro.getCiudadResidencia())
				&& UtilTexto.tieneTexto(filtro.getCiudadResidencia().getCodigoCiudadResidencia())
				&& UtilTexto.aplicarTrim(filtro.getCiudadResidencia()
						.getCodigoCiudadResidencia()).length() != LONGITUD_CODIGO_CIUDAD_RESIDENCIA) {

			throw new RuntimeException("El código de la ciudad de residencia debe tener exactamente "
					+ LONGITUD_CODIGO_CIUDAD_RESIDENCIA + " caracteres.");
		}
	}
}
