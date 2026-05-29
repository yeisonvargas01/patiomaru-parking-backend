package co.edu.uco.patiomaruparking.negocio.casouso.empleado.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.EmpleadoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.empleado.ActualizarEstadoEmpleadoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.EmpleadoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class ActualizarEstadoEmpleadoCasoUsoImpl implements ActualizarEstadoEmpleadoCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(ActualizarEstadoEmpleadoCasoUsoImpl.class);

	private static final String PREFIJO_EMPLEADO = "EMP";
	private static final int LONGITUD_CODIGO_EMPLEADO = 6;
	private static final int POSICION_INICIO_DIGITOS_EMPLEADO = 3;

	private final DAOFactory daoFactory;

	public ActualizarEstadoEmpleadoCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para actualizar estado del empleado porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final EmpleadoDominio datos) {
		logger.info("Iniciando la actualización del estado de un empleado.");

		var empleadoActualizar = UtilObjeto.obtenerValorDefecto(
				datos,
				EmpleadoDominio.builder().build());

		var codigoEmpleado = validarYNormalizarCodigoEmpleado(
				empleadoActualizar.getCodigoEmpleado());

		var nuevoEstado = validarYObtenerNuevoEstado(
				empleadoActualizar.getEstado());

		var empleadoActual = validarYObtenerEmpleado(codigoEmpleado);

		validarEstadoDiferente(
				empleadoActual.getEstado(),
				nuevoEstado);

		daoFactory.obtenerEmpleadoDAO()
				.actualizarEstado(codigoEmpleado, nuevoEstado);

		logger.info("Estado del empleado actualizado satisfactoriamente.");
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

	private Boolean validarYObtenerNuevoEstado(final Boolean estado) {
		if (UtilObjeto.esNulo(estado)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El estado del empleado es obligatorio.");
		}

		return estado;
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

	private void validarEstadoDiferente(
			final Boolean estadoActual,
			final Boolean nuevoEstado) {

		if (esMismoEstado(estadoActual, nuevoEstado)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El empleado ya se encuentra con el estado indicado.");
		}
	}

	private boolean esMismoEstado(
			final Boolean estadoActual,
			final Boolean nuevoEstado) {

		return Boolean.TRUE.equals(estadoActual) && Boolean.TRUE.equals(nuevoEstado)
				|| Boolean.FALSE.equals(estadoActual) && Boolean.FALSE.equals(nuevoEstado);
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
}