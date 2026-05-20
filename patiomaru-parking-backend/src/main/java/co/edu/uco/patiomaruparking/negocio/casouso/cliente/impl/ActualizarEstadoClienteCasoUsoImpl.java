package co.edu.uco.patiomaruparking.negocio.casouso.cliente.impl;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.ClienteEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.cliente.ActualizarEstadoClienteCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.ClienteDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public final class ActualizarEstadoClienteCasoUsoImpl implements ActualizarEstadoClienteCasoUso {

	private static final int LONGITUD_CODIGO_CLIENTE = 6;

	private final DAOFactory daoFactory;

	public ActualizarEstadoClienteCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final ClienteDominio datos) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango.
		validarDatosConsistentes(datos);

		var codigoCliente = UtilTexto.aplicarTrim(datos.getCodigoCliente());
		var nuevoEstado = datos.getEstado();

		// 2. Debe existir el cliente al que se le desea actualizar el estado.
		var clienteEntidad = daoFactory.obtenerClienteDAO().consultarPorId(codigoCliente);

		if (UtilObjeto.esNulo(clienteEntidad)) {
			throw new RuntimeException("No existe un cliente registrado con el código indicado.");
		}

		var clienteActual = ClienteEntidadAssembler.getInstance().ensamblarDominio(clienteEntidad);

		// 3. El nuevo estado no debe ser igual al estado actual.
		if (clienteActual.getEstado().equals(nuevoEstado)) {
			throw new RuntimeException("El cliente ya se encuentra con el estado indicado.");
		}

		// 4. Actualizar estado del cliente.
		daoFactory.obtenerClienteDAO().actualizarEstado(codigoCliente, nuevoEstado);
	}

	private void validarDatosConsistentes(final ClienteDominio datos) {
		if (UtilObjeto.esNulo(datos)) {
			throw new RuntimeException("Los datos para actualizar el estado del cliente son obligatorios.");
		}

		if (!UtilTexto.tieneTexto(datos.getCodigoCliente())) {
			throw new RuntimeException("El código del cliente es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(datos.getCodigoCliente()).length() != LONGITUD_CODIGO_CLIENTE) {
			throw new RuntimeException("El código del cliente debe tener exactamente "
					+ LONGITUD_CODIGO_CLIENTE + " caracteres.");
		}

		if (UtilObjeto.esNulo(datos.getEstado())) {
			throw new RuntimeException("El estado del cliente es obligatorio.");
		}
	}
}
