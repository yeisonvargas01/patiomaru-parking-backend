package co.edu.uco.patiomaruparking.negocio.casouso.empleado.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.EmpleadoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.empleado.ConsultarEmpleadoPorIdCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.EmpleadoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class ConsultarEmpleadoPorIdCasoUsoImpl implements ConsultarEmpleadoPorIdCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(ConsultarEmpleadoPorIdCasoUsoImpl.class);

	private static final String PREFIJO_EMPLEADO = "EMP";
	private static final int LONGITUD_CODIGO_EMPLEADO = 6;
	private static final int POSICION_INICIO_DIGITOS_EMPLEADO = 3;

	private final DAOFactory daoFactory;

	public ConsultarEmpleadoPorIdCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para consultar empleado por identificador porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public EmpleadoDominio ejecutar(final String codigoEmpleado) {
		logger.info("Iniciando la consulta de un empleado por identificador.");

		var codigoEmpleadoNormalizado = validarYNormalizarCodigoEmpleado(codigoEmpleado);

		var empleadoEntidad = daoFactory.obtenerEmpleadoDAO()
				.consultarPorId(codigoEmpleadoNormalizado);

		if (UtilObjeto.esNulo(empleadoEntidad)
				|| UtilTexto.esVacio(empleadoEntidad.getCodigoEmpleado())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe un empleado registrado con el código indicado.");
		}

		var empleado = EmpleadoEntidadAssembler.getInstance()
				.ensamblarDominio(empleadoEntidad);

		logger.info("Empleado consultado satisfactoriamente.");

		return empleado;
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