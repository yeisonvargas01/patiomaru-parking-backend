package co.edu.uco.patiomaruparking.datos;

public interface ActualizarEstadoDAO<ID, ESTADO> {

	void actualizarEstado(ID id, ESTADO estado);
}