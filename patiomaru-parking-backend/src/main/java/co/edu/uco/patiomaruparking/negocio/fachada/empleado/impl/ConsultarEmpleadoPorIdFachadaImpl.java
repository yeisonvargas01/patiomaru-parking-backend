package co.edu.uco.patiomaruparking.negocio.fachada.empleado.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.EmpleadoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.EmpleadoDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.empleado.impl.ConsultarEmpleadoPorIdCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.empleado.ConsultarEmpleadoPorIdFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class ConsultarEmpleadoPorIdFachadaImpl implements ConsultarEmpleadoPorIdFachada {

	private static final Logger logger = LoggerFactory.getLogger(ConsultarEmpleadoPorIdFachadaImpl.class);

	@Override
	public EmpleadoDTO ejecutar(final String codigoEmpleado) {
		logger.info("Iniciando fachada para consultar empleado por identificador.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			var casoUso = new ConsultarEmpleadoPorIdCasoUsoImpl(daoFactory);

			var empleadoDominio = casoUso.ejecutar(codigoEmpleado);

			var empleadoDTO = EmpleadoDTOAssembler.getInstance()
					.ensamblarDTO(empleadoDominio);

			logger.info("Fachada para consultar empleado por identificador finalizada satisfactoriamente.");

			return empleadoDTO;

		} catch (final PatioMaruExcepcion excepcion) {
			throw excepcion;

		} catch (final Exception excepcion) {
			logger.error("Se presentó un error inesperado consultando el empleado por identificador.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible consultar el empleado. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en ConsultarEmpleadoPorIdFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}
}
