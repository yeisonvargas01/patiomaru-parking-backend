package co.edu.uco.patiomaruparking.datos.dao;

import co.edu.uco.patiomaruparking.datos.ActualizarDAO;
import co.edu.uco.patiomaruparking.datos.ConsultarPorFiltroDAO;
import co.edu.uco.patiomaruparking.datos.ConsultarPorIdDAO;
import co.edu.uco.patiomaruparking.datos.ConsultarTodosDAO;
import co.edu.uco.patiomaruparking.datos.EliminarDAO;
import co.edu.uco.patiomaruparking.datos.RegistrarDAO;
import co.edu.uco.patiomaruparking.entidad.DetallePedidoEntidad;

public interface DetallePedidoDAO extends RegistrarDAO<DetallePedidoEntidad>,
		ConsultarTodosDAO<DetallePedidoEntidad>,
		ConsultarPorIdDAO<DetallePedidoEntidad, String>,
		ConsultarPorFiltroDAO<DetallePedidoEntidad>,
		ActualizarDAO<DetallePedidoEntidad>,
		EliminarDAO<String> {

}
