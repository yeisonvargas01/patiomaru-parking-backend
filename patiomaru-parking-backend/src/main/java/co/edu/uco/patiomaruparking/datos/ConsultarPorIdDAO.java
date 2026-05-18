package co.edu.uco.patiomaruparking.datos;

public interface ConsultarPorIdDAO<E, ID> {

	E consultarPorId(ID id);
}
