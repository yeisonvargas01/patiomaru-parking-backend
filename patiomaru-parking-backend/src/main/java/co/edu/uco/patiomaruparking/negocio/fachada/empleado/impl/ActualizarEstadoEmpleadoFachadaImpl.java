package co.edu.uco.patiomaruparking.negocio.fachada.empleado.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.EmpleadoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.EmpleadoDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.empleado.impl.ActualizarEstadoEmpleadoCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.empleado.ActualizarEstadoEmpleadoFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class ActualizarEstadoEmpleadoFachadaImpl implements ActualizarEstadoEmpleadoFachada {

	private static final Logger logger = LoggerFactory.getLogger(ActualizarEstadoEmpleadoFachadaImpl.class);

	@Override
	public void ejecutar(final EmpleadoDTO datos) {
		logger.info("Iniciando fachada para actualizar estado del empleado.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			daoFactory.iniciarTransaccion();

			var empleadoDominio = EmpleadoDTOAssembler.getInstance()
					.ensamblarDominio(datos);

			var casoUso = new ActualizarEstadoEmpleadoCasoUsoImpl(daoFactory);

			casoUso.ejecutar(empleadoDominio);

			daoFactory.confirmarTransaccion();

			logger.info("Fachada para actualizar estado del empleado finalizada satisfactoriamente.");

		} catch (final PatioMaruExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (final Exception excepcion) {
			daoFactory.cancelarTransaccion();

			logger.error("Se presentó un error inesperado actualizando el estado del empleado.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible actualizar el estado del empleado. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en ActualizarEstadoEmpleadoFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}
}
