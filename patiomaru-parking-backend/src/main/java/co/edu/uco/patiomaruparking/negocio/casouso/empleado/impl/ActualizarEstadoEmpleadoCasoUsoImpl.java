package co.edu.uco.patiomaruparking.negocio.casouso.empleado.impl;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.EmpleadoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.empleado.ActualizarEstadoEmpleadoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.EmpleadoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public final class ActualizarEstadoEmpleadoCasoUsoImpl implements ActualizarEstadoEmpleadoCasoUso {

	private static final int LONGITUD_CODIGO_EMPLEADO = 6;

	private final DAOFactory daoFactory;

	public ActualizarEstadoEmpleadoCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final EmpleadoDominio datos) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango.
		validarDatosConsistentes(datos);

		var codigoEmpleado = UtilTexto.aplicarTrimConvertirMayusculas(datos.getCodigoEmpleado());
		var nuevoEstado = datos.getEstado();

		// 2. Debe existir el empleado al que se le desea actualizar el estado.
		var empleadoEntidad = daoFactory.obtenerEmpleadoDAO().consultarPorId(codigoEmpleado);

		if (UtilObjeto.esNulo(empleadoEntidad)) {
			throw new RuntimeException("No existe un empleado registrado con el código indicado.");
		}

		var empleadoActual = EmpleadoEntidadAssembler.getInstance().ensamblarDominio(empleadoEntidad);

		// 3. El nuevo estado no debe ser igual al estado actual.
		if (empleadoActual.getEstado().equals(nuevoEstado)) {
			throw new RuntimeException("El empleado ya se encuentra con el estado indicado.");
		}

		// 4. Actualizar estado del empleado.
		daoFactory.obtenerEmpleadoDAO().actualizarEstado(codigoEmpleado, nuevoEstado);
	}

	private void validarDatosConsistentes(final EmpleadoDominio datos) {
		if (UtilObjeto.esNulo(datos)) {
			throw new RuntimeException("Los datos para actualizar el estado del empleado son obligatorios.");
		}

		if (!UtilTexto.tieneTexto(datos.getCodigoEmpleado())) {
			throw new RuntimeException("El código del empleado es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(datos.getCodigoEmpleado()).length() != LONGITUD_CODIGO_EMPLEADO) {
			throw new RuntimeException("El código del empleado debe tener exactamente "
					+ LONGITUD_CODIGO_EMPLEADO + " caracteres.");
		}

		if (UtilObjeto.esNulo(datos.getEstado())) {
			throw new RuntimeException("El estado del empleado es obligatorio.");
		}
	}
}
