package co.edu.uco.patiomaruparking.negocio.casouso.cliente.impl;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.ClienteEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.cliente.ConsultarClientePorIdCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.ClienteDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public final class ConsultarClientePorIdCasoUsoImpl implements ConsultarClientePorIdCasoUso {

	private static final int LONGITUD_CODIGO_CLIENTE = 6;

	private final DAOFactory daoFactory;

	public ConsultarClientePorIdCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public ClienteDominio ejecutar(final String codigoCliente) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango.
		validarCodigoCliente(codigoCliente);

		var codigoClienteNormalizado = UtilTexto.aplicarTrim(codigoCliente);

		// 2. Debe existir un cliente registrado con el código indicado.
		var clienteEntidad = daoFactory.obtenerClienteDAO().consultarPorId(codigoClienteNormalizado);

		if (UtilObjeto.esNulo(clienteEntidad)) {
			throw new RuntimeException("No existe un cliente registrado con el código indicado.");
		}

		// 3. Ensamblar el cliente de Entidad a Dominio.
		return ClienteEntidadAssembler.getInstance().ensamblarDominio(clienteEntidad);
	}

	private void validarCodigoCliente(final String codigoCliente) {
		if (!UtilTexto.tieneTexto(codigoCliente)) {
			throw new RuntimeException("El código del cliente es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(codigoCliente).length() != LONGITUD_CODIGO_CLIENTE) {
			throw new RuntimeException("El código del cliente debe tener exactamente "
					+ LONGITUD_CODIGO_CLIENTE + " caracteres.");
		}
	}
}
