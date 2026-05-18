package co.edu.uco.patiomaruparking.datos.dao;

import co.edu.uco.patiomaruparking.datos.ActualizarDAO;
import co.edu.uco.patiomaruparking.datos.ActualizarEstadoDAO;
import co.edu.uco.patiomaruparking.datos.ConsultarPorFiltroDAO;
import co.edu.uco.patiomaruparking.datos.ConsultarPorIdDAO;
import co.edu.uco.patiomaruparking.datos.ConsultarTodosDAO;
import co.edu.uco.patiomaruparking.datos.RegistrarDAO;
import co.edu.uco.patiomaruparking.entidad.EmpleadoEntidad;

public interface EmpleadoDAO extends RegistrarDAO<EmpleadoEntidad>,
		ConsultarTodosDAO<EmpleadoEntidad>,
		ConsultarPorIdDAO<EmpleadoEntidad, String>,
		ConsultarPorFiltroDAO<EmpleadoEntidad>,
		ActualizarDAO<EmpleadoEntidad>,
		ActualizarEstadoDAO<String, Boolean> {

}
