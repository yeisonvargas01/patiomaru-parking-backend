package co.edu.uco.patiomaruparking.datos;

public interface ActualizarDisponibilidadDAO<ID> {

	void actualizarDisponibilidad(ID id, boolean disponible);
}
