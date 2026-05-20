package co.edu.uco.patiomaruparking.negocio.casouso.cliente.impl;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.ClienteEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.ClienteEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.cliente.ActualizarClienteCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.ClienteDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public final class ActualizarClienteCasoUsoImpl implements ActualizarClienteCasoUso {

	private static final int LONGITUD_CODIGO_CLIENTE = 6;
	private static final int LONGITUD_MAXIMA_NOMBRE = 80;
	private static final int LONGITUD_MAXIMA_TELEFONO = 15;
	private static final int LONGITUD_MAXIMA_CORREO_ELECTRONICO = 100;

	private final DAOFactory daoFactory;

	public ActualizarClienteCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final ClienteDominio datos) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango.
		validarDatosConsistentes(datos);

		var codigoCliente = UtilTexto.aplicarTrim(datos.getCodigoCliente());

		// 2. Debe existir el cliente que se desea actualizar.
		var clienteActualEntidad = daoFactory.obtenerClienteDAO().consultarPorId(codigoCliente);

		if (UtilObjeto.esNulo(clienteActualEntidad)) {
			throw new RuntimeException("No existe un cliente registrado con el código indicado.");
		}

		var clienteActual = ClienteEntidadAssembler.getInstance().ensamblarDominio(clienteActualEntidad);

		// 3. No debe existir otro cliente con la misma combinación única documentada:
		// nombre + teléfono.
		validarNoExisteOtroClienteConMismoNombreYTelefono(datos);

		// 4. Actualizar información del cliente.
		var clienteActualizado = ClienteDominio.builder()
				.codigoCliente(codigoCliente)
				.nombre(UtilTexto.aplicarTrim(datos.getNombre()))
				.telefono(UtilTexto.aplicarTrim(datos.getTelefono()))
				.correoElectronico(UtilTexto.aplicarTrim(datos.getCorreoElectronico()))
				.estado(clienteActual.getEstado())
				.build();

		actualizar(clienteActualizado);
	}

	private void validarDatosConsistentes(final ClienteDominio datos) {
		if (UtilObjeto.esNulo(datos)) {
			throw new RuntimeException("Los datos del cliente son obligatorios.");
		}

		if (!UtilTexto.tieneTexto(datos.getCodigoCliente())) {
			throw new RuntimeException("El código del cliente es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(datos.getCodigoCliente()).length() != LONGITUD_CODIGO_CLIENTE) {
			throw new RuntimeException("El código del cliente debe tener exactamente "
					+ LONGITUD_CODIGO_CLIENTE + " caracteres.");
		}

		if (!UtilTexto.tieneTexto(datos.getNombre())) {
			throw new RuntimeException("El nombre del cliente es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(datos.getNombre()).length() > LONGITUD_MAXIMA_NOMBRE) {
			throw new RuntimeException("El nombre del cliente no puede superar "
					+ LONGITUD_MAXIMA_NOMBRE + " caracteres.");
		}

		if (!UtilTexto.tieneTexto(datos.getTelefono())) {
			throw new RuntimeException("El teléfono del cliente es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(datos.getTelefono()).length() > LONGITUD_MAXIMA_TELEFONO) {
			throw new RuntimeException("El teléfono del cliente no puede superar "
					+ LONGITUD_MAXIMA_TELEFONO + " caracteres.");
		}

		if (!UtilTexto.tieneTexto(datos.getCorreoElectronico())) {
			throw new RuntimeException("El correo electrónico del cliente es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(datos.getCorreoElectronico()).length() > LONGITUD_MAXIMA_CORREO_ELECTRONICO) {
			throw new RuntimeException("El correo electrónico del cliente no puede superar "
					+ LONGITUD_MAXIMA_CORREO_ELECTRONICO + " caracteres.");
		}

		validarFormatoCorreoElectronico(datos.getCorreoElectronico());
	}

	private void validarFormatoCorreoElectronico(final String correoElectronico) {
		var correo = UtilTexto.aplicarTrim(correoElectronico);

		if (!correo.contains("@") || !correo.contains(".")) {
			throw new RuntimeException("El correo electrónico del cliente no tiene un formato válido.");
		}
	}

	private void validarNoExisteOtroClienteConMismoNombreYTelefono(final ClienteDominio datos) {
		var filtro = ClienteEntidad.builder()
				.nombre(UtilTexto.aplicarTrim(datos.getNombre()))
				.telefono(UtilTexto.aplicarTrim(datos.getTelefono()))
				.build();

		var resultados = daoFactory.obtenerClienteDAO().consultar(filtro);

		if (UtilObjeto.esNulo(resultados) || resultados.isEmpty()) {
			return;
		}

		for (ClienteEntidad cliente : resultados) {
			if (!UtilTexto.sonIgualesIgnorandoMayusculas(
					cliente.getCodigoCliente(), datos.getCodigoCliente())) {
				throw new RuntimeException("Ya existe otro cliente registrado con el mismo nombre y teléfono.");
			}
		}
	}

	private void actualizar(final ClienteDominio cliente) {
		var clienteEntidad = ClienteEntidadAssembler.getInstance().ensamblarEntidad(cliente);
		daoFactory.obtenerClienteDAO().actualizar(clienteEntidad);
	}
}
