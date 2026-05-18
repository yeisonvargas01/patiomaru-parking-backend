package co.edu.uco.patiomaruparking.datos.dao;

import co.edu.uco.patiomaruparking.datos.ConsultarPorFiltroDAO;
import co.edu.uco.patiomaruparking.datos.ConsultarPorIdDAO;
import co.edu.uco.patiomaruparking.datos.ConsultarTodosDAO;
import co.edu.uco.patiomaruparking.entidad.MesaEntidad;

public interface MesaDAO extends ConsultarTodosDAO<MesaEntidad>,
		ConsultarPorIdDAO<MesaEntidad, String>,
		ConsultarPorFiltroDAO<MesaEntidad> {

}
