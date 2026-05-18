package co.edu.uco.patiomaruparking.datos.dao;

import java.util.UUID;

import co.edu.uco.patiomaruparking.datos.ActualizarEstadoDAO;
import co.edu.uco.patiomaruparking.datos.CancelarDAO;
import co.edu.uco.patiomaruparking.datos.ConsultarPorFiltroDAO;
import co.edu.uco.patiomaruparking.datos.ConsultarPorIdDAO;
import co.edu.uco.patiomaruparking.datos.ConsultarTodosDAO;
import co.edu.uco.patiomaruparking.datos.RegistrarDAO;
import co.edu.uco.patiomaruparking.entidad.PedidoEntidad;

public interface PedidoDAO extends RegistrarDAO<PedidoEntidad>,
		ConsultarTodosDAO<PedidoEntidad>,
		ConsultarPorIdDAO<PedidoEntidad, UUID>,
		ConsultarPorFiltroDAO<PedidoEntidad>,
		ActualizarEstadoDAO<UUID, String>,
		CancelarDAO<UUID> {

}
