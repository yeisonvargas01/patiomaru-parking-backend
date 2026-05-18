package co.edu.uco.patiomaruparking.datos;

public interface ActualizarCantidadDisponibleDAO<ID, CANTIDAD> {

	void actualizarCantidadDisponible(ID id, CANTIDAD cantidad);
}
