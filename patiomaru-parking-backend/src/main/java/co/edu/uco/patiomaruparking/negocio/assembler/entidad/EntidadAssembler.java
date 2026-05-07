package co.edu.uco.patiomaruparking.negocio.assembler.entidad;

public interface EntidadAssembler<D, E> {

	D ensamblarDominio(E entidad);

	E ensamblarEntidad(D dominio);
}
