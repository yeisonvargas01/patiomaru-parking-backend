package co.edu.uco.patiomaruparking.negocio.fachada.empleado.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.postgresql.PostgreSQLDAOFactory;
import co.edu.uco.patiomaruparking.dto.EmpleadoDTO;
import co.edu.uco.patiomaruparking.negocio.assembler.dto.impl.EmpleadoDTOAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.empleado.impl.ConsultarEmpleadosCasoUsoImpl;
import co.edu.uco.patiomaruparking.negocio.dominio.EmpleadoDominio;
import co.edu.uco.patiomaruparking.negocio.fachada.empleado.ConsultarEmpleadosFachada;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

public final class ConsultarEmpleadosFachadaImpl implements ConsultarEmpleadosFachada {

	private static final Logger logger = LoggerFactory.getLogger(ConsultarEmpleadosFachadaImpl.class);

	@Override
	public List<EmpleadoDTO> ejecutar(final EmpleadoDTO filtro) {
		logger.info("Iniciando fachada para consultar empleados.");

		DAOFactory daoFactory = new PostgreSQLDAOFactory();

		try {
			var filtroDominio = EmpleadoDTOAssembler.getInstance()
					.ensamblarDominio(filtro);

			var casoUso = new ConsultarEmpleadosCasoUsoImpl(daoFactory);

			var empleadosDominio = UtilObjeto.obtenerValorDefecto(
					casoUso.ejecutar(filtroDominio),
					List.<EmpleadoDominio>of());

			var empleadosDTO = ensamblarRespuesta(empleadosDominio);

			logger.info("Fachada para consultar empleados finalizada satisfactoriamente.");

			return empleadosDTO;

		} catch (final PatioMaruExcepcion excepcion) {
			throw excepcion;

		} catch (final Exception excepcion) {
			logger.error("Se presentó un error inesperado consultando los empleados.", excepcion);

			throw NegocioPatioMaruExcepcion.crear(
					"No fue posible consultar los empleados. Por favor intente nuevamente.",
					"Se presentó una excepción inesperada en ConsultarEmpleadosFachadaImpl.",
					excepcion);

		} finally {
			daoFactory.cerrarConexion();
		}
	}

	private List<EmpleadoDTO> ensamblarRespuesta(final List<EmpleadoDominio> empleadosDominio) {
		var empleadosDTO = new ArrayList<EmpleadoDTO>();

		for (EmpleadoDominio empleadoDominio : empleadosDominio) {
			var empleadoDTO = EmpleadoDTOAssembler.getInstance()
					.ensamblarDTO(empleadoDominio);

			empleadosDTO.add(empleadoDTO);
		}

		return empleadosDTO;
	}
}
