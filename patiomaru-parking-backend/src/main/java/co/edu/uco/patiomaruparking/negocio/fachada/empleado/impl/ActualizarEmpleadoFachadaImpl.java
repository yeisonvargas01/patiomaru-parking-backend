package co.edu.uco.patiomaruparking.negocio.fachada.empleado.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.EmpleadoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.EmpleadoDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.empleado.impl.ActualizarEmpleadoCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.empleado.ActualizarEmpleadoFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class ActualizarEmpleadoFachadaImpl implements ActualizarEmpleadoFachada {

	private static final Logger logger = LoggerFactory.getLogger(ActualizarEmpleadoFachadaImpl.class);

	@Override
	public void ejecutar(final EmpleadoDTO datos) {
		logger.info("Iniciando fachada para actualizar empleado.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			daoFactory.iniciarTransaccion();

			var empleadoDominio = EmpleadoDTOAssembler.getInstance()
					.ensamblarDominio(datos);

			var casoUso = new ActualizarEmpleadoCasoUsoImpl(daoFactory);

			casoUso.ejecutar(empleadoDominio);

			daoFactory.confirmarTransaccion();

			logger.info("Fachada para actualizar empleado finalizada satisfactoriamente.");

		} catch (final PatioMaruExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (final Exception excepcion) {
			daoFactory.cancelarTransaccion();

			logger.error("Se presentó un error inesperado actualizando el empleado.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible actualizar el empleado. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en ActualizarEmpleadoFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}
}
