package co.edu.uco.patiomaruparking.datos.dao;

import co.edu.uco.patiomaruparking.datos.ActualizarDAO;
import co.edu.uco.patiomaruparking.datos.ActualizarEstadoDAO;
import co.edu.uco.patiomaruparking.datos.ConsultarPorFiltroDAO;
import co.edu.uco.patiomaruparking.datos.ConsultarPorIdDAO;
import co.edu.uco.patiomaruparking.datos.ConsultarTodosDAO;
import co.edu.uco.patiomaruparking.datos.RegistrarDAO;
import co.edu.uco.patiomaruparking.entidad.ClienteEntidad;

public interface ClienteDAO extends RegistrarDAO<ClienteEntidad>,
		ConsultarTodosDAO<ClienteEntidad>,
		ConsultarPorIdDAO<ClienteEntidad, String>,
		ConsultarPorFiltroDAO<ClienteEntidad>,
		ActualizarDAO<ClienteEntidad>,
		ActualizarEstadoDAO<String, Boolean> {

}
