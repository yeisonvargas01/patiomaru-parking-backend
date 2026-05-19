package co.edu.uco.patiomaruparking.negocio.assembler.entidad;

public interface EntidadAssembler<D, E> {

	E ensamblarEntidad(D dominio);

	D ensamblarDominio(E entidad);
}