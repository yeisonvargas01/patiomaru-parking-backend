package co.edu.uco.patiomaruparking.negocio.casouso.cliente.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.ClienteEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.ClienteEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.cliente.RegistrarClienteCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.ClienteDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilCodigo;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class RegistrarClienteCasoUsoImpl implements RegistrarClienteCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(RegistrarClienteCasoUsoImpl.class);

	private static final String PREFIJO_CLIENTE = "CLI";
	private static final int CANTIDAD_DIGITOS_CLIENTE = 3;

	private static final boolean ESTADO_CLIENTE_ACTIVO = true;

	private static final int LONGITUD_MINIMA_NOMBRE = 2;
	private static final int LONGITUD_MAXIMA_NOMBRE = 80;

	private static final int LONGITUD_MINIMA_TELEFONO = 10;
	private static final int LONGITUD_MAXIMA_TELEFONO = 15;

	private static final int LONGITUD_MINIMA_CORREO = 5;
	private static final int LONGITUD_MAXIMA_CORREO = 100;

	private final DAOFactory daoFactory;

	public RegistrarClienteCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para registrar cliente porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public ClienteDominio ejecutar(final ClienteDominio datos) {
		logger.info("Iniciando el registro de un cliente.");

		var cliente = UtilObjeto.obtenerValorDefecto(
				datos,
				ClienteDominio.builder().build());

		validarDatosConsistentes(cliente);

		validarNoExisteClienteConMismoTelefono(cliente.getTelefono());
		validarNoExisteClienteConMismoCorreoElectronico(cliente.getCorreoElectronico());

		var codigoCliente = generarCodigoUnicoCliente();

		var clientePreparado = ClienteDominio.builder()
				.codigoCliente(codigoCliente)
				.nombre(UtilTexto.aplicarTrim(cliente.getNombre()))
				.telefono(UtilTexto.aplicarTrim(cliente.getTelefono()))
				.correoElectronico(UtilTexto.aplicarTrim(cliente.getCorreoElectronico()))
				.estado(ESTADO_CLIENTE_ACTIVO)
				.build();

		guardarCliente(clientePreparado);

		logger.info("Cliente registrado satisfactoriamente.");

		return clientePreparado;
	}

	private void validarDatosConsistentes(final ClienteDominio cliente) {
		validarNombre(cliente.getNombre());
		validarTelefono(cliente.getTelefono());
		validarCorreoElectronicoSiFueInformado(cliente.getCorreoElectronico());
	}

	private void validarNombre(final String nombre) {
		if (!UtilTexto.tieneTexto(nombre)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El nombre del cliente es obligatorio.");
		}

		validarLongitud(
				nombre,
				LONGITUD_MINIMA_NOMBRE,
				LONGITUD_MAXIMA_NOMBRE,
				"El nombre del cliente");
	}

	private void validarTelefono(final String telefono) {
		if (!UtilTexto.tieneTexto(telefono)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El número de teléfono del cliente es obligatorio.");
		}

		validarLongitud(
				telefono,
				LONGITUD_MINIMA_TELEFONO,
				LONGITUD_MAXIMA_TELEFONO,
				"El número de teléfono del cliente");

		if (!contieneSoloDigitos(telefono)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El número de teléfono del cliente solo debe contener dígitos.");
		}
	}

	private void validarCorreoElectronicoSiFueInformado(final String correoElectronico) {
		if (!UtilTexto.tieneTexto(correoElectronico)) {
			return;
		}

		validarLongitud(
				correoElectronico,
				LONGITUD_MINIMA_CORREO,
				LONGITUD_MAXIMA_CORREO,
				"El correo electrónico del cliente");

		if (!tieneFormatoBasicoCorreoElectronico(correoElectronico)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El correo electrónico del cliente no tiene un formato válido.");
		}
	}

	private void validarLongitud(
			final String valor,
			final int longitudMinima,
			final int longitudMaxima,
			final String nombreCampo) {

		var valorSeguro = UtilTexto.aplicarTrim(valor);

		if (valorSeguro.length() < longitudMinima || valorSeguro.length() > longitudMaxima) {
			throw NegocioPatioMaruExcepcion.crear(
					nombreCampo + " debe tener entre " + longitudMinima + " y "
							+ longitudMaxima + " caracteres.");
		}
	}

	private boolean contieneSoloDigitos(final String valor) {
		var valorSeguro = UtilTexto.aplicarTrim(valor);

		for (int indice = 0; indice < valorSeguro.length(); indice++) {
			if (!Character.isDigit(valorSeguro.charAt(indice))) {
				return false;
			}
		}

		return true;
	}

	private boolean tieneFormatoBasicoCorreoElectronico(final String correoElectronico) {
		var correoSeguro = UtilTexto.aplicarTrim(correoElectronico);

		return correoSeguro.contains("@")
				&& correoSeguro.contains(".")
				&& correoSeguro.indexOf("@") > 0
				&& correoSeguro.lastIndexOf(".") > correoSeguro.indexOf("@") + 1
				&& correoSeguro.lastIndexOf(".") < correoSeguro.length() - 1;
	}

	private void validarNoExisteClienteConMismoTelefono(final String telefono) {
		var filtro = ClienteEntidad.builder()
				.telefono(UtilTexto.aplicarTrim(telefono))
				.build();

		var resultados = UtilObjeto.obtenerValorDefecto(
				daoFactory.obtenerClienteDAO().consultar(filtro),
				List.<ClienteEntidad>of());

		if (!resultados.isEmpty()) {
			throw NegocioPatioMaruExcepcion.crear(
					"Ya existe otro cliente registrado con el mismo número de teléfono.");
		}
	}

	private void validarNoExisteClienteConMismoCorreoElectronico(final String correoElectronico) {
		if (!UtilTexto.tieneTexto(correoElectronico)) {
			return;
		}

		var filtro = ClienteEntidad.builder()
				.correoElectronico(UtilTexto.aplicarTrim(correoElectronico))
				.build();

		var resultados = UtilObjeto.obtenerValorDefecto(
				daoFactory.obtenerClienteDAO().consultar(filtro),
				List.<ClienteEntidad>of());

		if (!resultados.isEmpty()) {
			throw NegocioPatioMaruExcepcion.crear(
					"Ya existe otro cliente registrado con el mismo correo electrónico.");
		}
	}

	private String generarCodigoUnicoCliente() {
		String codigoCliente;
		ClienteEntidad clienteExistente;

		do {
			codigoCliente = UtilCodigo.generarCodigo(
					PREFIJO_CLIENTE,
					CANTIDAD_DIGITOS_CLIENTE);

			clienteExistente = daoFactory.obtenerClienteDAO()
					.consultarPorId(codigoCliente);

		} while (UtilObjeto.noEsNulo(clienteExistente)
				&& UtilTexto.tieneTexto(clienteExistente.getCodigoCliente()));

		return codigoCliente;
	}

	private void guardarCliente(final ClienteDominio cliente) {
		var clienteEntidad = ClienteEntidadAssembler.getInstance()
				.ensamblarEntidad(cliente);

		daoFactory.obtenerClienteDAO()
				.registrar(clienteEntidad);
	}
}
