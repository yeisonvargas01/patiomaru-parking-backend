package co.edu.uco.patiomaruparking.negocio.casouso.empleado.impl;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.EmpleadoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.empleado.ConsultarEmpleadoPorIdCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.EmpleadoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public final class ConsultarEmpleadoPorIdCasoUsoImpl implements ConsultarEmpleadoPorIdCasoUso {

	private static final int LONGITUD_CODIGO_EMPLEADO = 6;

	private final DAOFactory daoFactory;

	public ConsultarEmpleadoPorIdCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public EmpleadoDominio ejecutar(final String codigoEmpleado) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango.
		validarCodigoEmpleado(codigoEmpleado);

		var codigoEmpleadoNormalizado = UtilTexto.aplicarTrimConvertirMayusculas(codigoEmpleado);

		// 2. Debe existir un empleado registrado con el código indicado.
		var empleadoEntidad = daoFactory.obtenerEmpleadoDAO().consultarPorId(codigoEmpleadoNormalizado);

		if (UtilObjeto.esNulo(empleadoEntidad)) {
			throw new RuntimeException("No existe un empleado registrado con el código indicado.");
		}

		// 3. Ensamblar el empleado de Entidad a Dominio.
		return EmpleadoEntidadAssembler.getInstance().ensamblarDominio(empleadoEntidad);
	}

	private void validarCodigoEmpleado(final String codigoEmpleado) {
		if (!UtilTexto.tieneTexto(codigoEmpleado)) {
			throw new RuntimeException("El código del empleado es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(codigoEmpleado).length() != LONGITUD_CODIGO_EMPLEADO) {
			throw new RuntimeException("El código del empleado debe tener exactamente "
					+ LONGITUD_CODIGO_EMPLEADO + " caracteres.");
		}
	}
}
