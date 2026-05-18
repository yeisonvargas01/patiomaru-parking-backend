package co.edu.uco.patiomaruparking.datos;

import java.util.List;

public interface ConsultarPorFiltroDAO<E> {

	List<E> consultar(E filtro);
}