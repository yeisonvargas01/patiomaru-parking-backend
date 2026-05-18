package co.edu.uco.patiomaruparking.datos.dao;

import co.edu.uco.patiomaruparking.datos.ActualizarDAO;
import co.edu.uco.patiomaruparking.datos.ActualizarDisponibilidadDAO;
import co.edu.uco.patiomaruparking.datos.ConsultarPorFiltroDAO;
import co.edu.uco.patiomaruparking.datos.ConsultarPorIdDAO;
import co.edu.uco.patiomaruparking.datos.ConsultarTodosDAO;
import co.edu.uco.patiomaruparking.datos.EliminarDAO;
import co.edu.uco.patiomaruparking.datos.RegistrarDAO;
import co.edu.uco.patiomaruparking.entidad.PlatoEntidad;

public interface PlatoDAO extends RegistrarDAO<PlatoEntidad>,
		ConsultarTodosDAO<PlatoEntidad>,
		ConsultarPorIdDAO<PlatoEntidad, String>,
		ConsultarPorFiltroDAO<PlatoEntidad>,
		ActualizarDAO<PlatoEntidad>,
		ActualizarDisponibilidadDAO<String>,
		EliminarDAO<String> {

}
