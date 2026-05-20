package co.edu.uco.patiomaruparking.negocio.casouso.cliente.impl;

import java.util.List;
import java.util.stream.Collectors;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.ClienteEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.cliente.ConsultarClientesCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.ClienteDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public final class ConsultarClientesCasoUsoImpl implements ConsultarClientesCasoUso {

	private static final int LONGITUD_CODIGO_CLIENTE = 6;
	private static final int LONGITUD_MAXIMA_NOMBRE = 80;
	private static final int LONGITUD_MAXIMA_TELEFONO = 15;
	private static final int LONGITUD_MAXIMA_CORREO_ELECTRONICO = 100;

	private final DAOFactory daoFactory;

	public ConsultarClientesCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public List<ClienteDominio> ejecutar(final ClienteDominio filtro) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango cuando se envían filtros.
		validarFiltro(filtro);

		// 2. Consultar información de los clientes según los filtros enviados.
		var filtroSeguro = UtilObjeto.esNulo(filtro)
				? ClienteDominio.builder().build()
				: filtro;

		var filtroEntidad = ClienteEntidadAssembler.getInstance().ensamblarEntidad(filtroSeguro);

		var clientesEntidad = daoFactory.obtenerClienteDAO().consultar(filtroEntidad);

		if (UtilObjeto.esNulo(clientesEntidad) || clientesEntidad.isEmpty()) {
			return List.of();
		}

		// 3. Ensamblar los resultados de Entidad a Dominio.
		return clientesEntidad.stream()
				.map(ClienteEntidadAssembler.getInstance()::ensamblarDominio)
				.collect(Collectors.toList());
	}

	private void validarFiltro(final ClienteDominio filtro) {
		if (UtilObjeto.esNulo(filtro)) {
			return;
		}

		if (UtilTexto.tieneTexto(filtro.getCodigoCliente())
				&& UtilTexto.aplicarTrim(filtro.getCodigoCliente()).length() != LONGITUD_CODIGO_CLIENTE) {
			throw new RuntimeException("El código del cliente debe tener exactamente "
					+ LONGITUD_CODIGO_CLIENTE + " caracteres.");
		}

		if (UtilTexto.tieneTexto(filtro.getNombre())
				&& UtilTexto.aplicarTrim(filtro.getNombre()).length() > LONGITUD_MAXIMA_NOMBRE) {
			throw new RuntimeException("El nombre del cliente no puede superar "
					+ LONGITUD_MAXIMA_NOMBRE + " caracteres.");
		}

		if (UtilTexto.tieneTexto(filtro.getTelefono())
				&& UtilTexto.aplicarTrim(filtro.getTelefono()).length() > LONGITUD_MAXIMA_TELEFONO) {
			throw new RuntimeException("El teléfono del cliente no puede superar "
					+ LONGITUD_MAXIMA_TELEFONO + " caracteres.");
		}

		if (UtilTexto.tieneTexto(filtro.getCorreoElectronico())) {
			if (UtilTexto.aplicarTrim(filtro.getCorreoElectronico()).length() > LONGITUD_MAXIMA_CORREO_ELECTRONICO) {
				throw new RuntimeException("El correo electrónico del cliente no puede superar "
						+ LONGITUD_MAXIMA_CORREO_ELECTRONICO + " caracteres.");
			}

			validarFormatoCorreoElectronico(filtro.getCorreoElectronico());
		}
	}

	private void validarFormatoCorreoElectronico(final String correoElectronico) {
		var correo = UtilTexto.aplicarTrim(correoElectronico);

		if (!correo.contains("@") || !correo.contains(".")) {
			throw new RuntimeException("El correo electrónico del cliente no tiene un formato válido.");
		}
	}
}
