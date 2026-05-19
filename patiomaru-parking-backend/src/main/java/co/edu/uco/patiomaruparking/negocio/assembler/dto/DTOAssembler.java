package co.edu.uco.patiomaruparking.negocio.assembler.dto;

public interface DTOAssembler<D, T> {

	T ensamblarDTO(D dominio);

	D ensamblarDominio(T dto);
}
