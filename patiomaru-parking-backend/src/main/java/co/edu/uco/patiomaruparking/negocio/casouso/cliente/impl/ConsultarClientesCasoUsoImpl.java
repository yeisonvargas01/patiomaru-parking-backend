package co.edu.uco.patiomaruparking.negocio.casouso.cliente.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.ClienteEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.ClienteEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.cliente.ConsultarClientesCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.ClienteDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class ConsultarClientesCasoUsoImpl implements ConsultarClientesCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(ConsultarClientesCasoUsoImpl.class);

	private static final String PREFIJO_CLIENTE = "CLI";
	private static final int LONGITUD_CODIGO_CLIENTE = 6;
	private static final int POSICION_INICIO_DIGITOS_CLIENTE = 3;

	private static final int LONGITUD_MINIMA_NOMBRE = 2;
	private static final int LONGITUD_MAXIMA_NOMBRE = 80;

	private static final int LONGITUD_MINIMA_TELEFONO = 10;
	private static final int LONGITUD_MAXIMA_TELEFONO = 15;

	private static final int LONGITUD_MINIMA_CORREO_ELECTRONICO = 5;
	private static final int LONGITUD_MAXIMA_CORREO_ELECTRONICO = 100;

	private final DAOFactory daoFactory;

	public ConsultarClientesCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para consultar clientes porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public List<ClienteDominio> ejecutar(final ClienteDominio filtro) {
		logger.info("Iniciando la consulta de clientes.");

		var filtroSeguro = UtilObjeto.obtenerValorDefecto(
				filtro,
				ClienteDominio.builder().build());

		validarFiltro(filtroSeguro);

		var filtroEntidad = ClienteEntidadAssembler.getInstance()
				.ensamblarEntidad(filtroSeguro);

		var clientesEntidad = UtilObjeto.obtenerValorDefecto(
				daoFactory.obtenerClienteDAO().consultar(filtroEntidad),
				List.<ClienteEntidad>of());

		var clientes = new ArrayList<ClienteDominio>();

		for (ClienteEntidad clienteEntidad : clientesEntidad) {
			var cliente = ClienteEntidadAssembler.getInstance()
					.ensamblarDominio(clienteEntidad);

			clientes.add(cliente);
		}

		logger.info("Consulta de clientes finalizada satisfactoriamente.");

		return clientes;
	}

	private void validarFiltro(final ClienteDominio filtro) {
		validarCodigoClienteSiFueInformado(filtro);
		validarNombreSiFueInformado(filtro);
		validarTelefonoSiFueInformado(filtro);
		validarCorreoElectronicoSiFueInformado(filtro);
	}

	private void validarCodigoClienteSiFueInformado(final ClienteDominio filtro) {
		if (!UtilTexto.tieneTexto(filtro.getCodigoCliente())) {
			return;
		}

		var codigoCliente = UtilTexto.aplicarTrim(filtro.getCodigoCliente());

		if (codigoCliente.length() != LONGITUD_CODIGO_CLIENTE) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del cliente debe tener exactamente "
							+ LONGITUD_CODIGO_CLIENTE + " caracteres.");
		}

		if (!iniciaConPrefijoCliente(codigoCliente)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del cliente debe iniciar con " + PREFIJO_CLIENTE + ".");
		}

		if (!contieneSoloDigitos(
				codigoCliente.substring(POSICION_INICIO_DIGITOS_CLIENTE))) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del cliente debe tener el formato CLI seguido de tres dígitos numéricos.");
		}
	}

	private void validarNombreSiFueInformado(final ClienteDominio filtro) {
		if (!UtilTexto.tieneTexto(filtro.getNombre())) {
			return;
		}

		validarLongitud(
				filtro.getNombre(),
				LONGITUD_MINIMA_NOMBRE,
				LONGITUD_MAXIMA_NOMBRE,
				"El nombre del cliente");
	}

	private void validarTelefonoSiFueInformado(final ClienteDominio filtro) {
		if (!UtilTexto.tieneTexto(filtro.getTelefono())) {
			return;
		}

		validarLongitud(
				filtro.getTelefono(),
				LONGITUD_MINIMA_TELEFONO,
				LONGITUD_MAXIMA_TELEFONO,
				"El número de teléfono del cliente");

		if (!contieneSoloDigitos(filtro.getTelefono())) {
			throw NegocioPatioMaruExcepcion.crear(
					"El número de teléfono del cliente solo debe contener dígitos.");
		}
	}

	private void validarCorreoElectronicoSiFueInformado(final ClienteDominio filtro) {
		if (!UtilTexto.tieneTexto(filtro.getCorreoElectronico())) {
			return;
		}

		validarLongitud(
				filtro.getCorreoElectronico(),
				LONGITUD_MINIMA_CORREO_ELECTRONICO,
				LONGITUD_MAXIMA_CORREO_ELECTRONICO,
				"El correo electrónico del cliente");

		if (!tieneFormatoBasicoCorreoElectronico(filtro.getCorreoElectronico())) {
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

	private boolean iniciaConPrefijoCliente(final String codigoCliente) {
		var prefijo = codigoCliente.substring(0, POSICION_INICIO_DIGITOS_CLIENTE);

		return UtilTexto.sonIgualesIgnorandoMayusculas(
				prefijo,
				PREFIJO_CLIENTE);
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
}