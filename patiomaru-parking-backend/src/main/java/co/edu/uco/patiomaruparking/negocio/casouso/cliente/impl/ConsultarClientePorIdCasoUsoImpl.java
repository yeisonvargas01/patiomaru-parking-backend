package co.edu.uco.patiomaruparking.negocio.casouso.cliente.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.ClienteEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.cliente.ConsultarClientePorIdCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.ClienteDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class ConsultarClientePorIdCasoUsoImpl implements ConsultarClientePorIdCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(ConsultarClientePorIdCasoUsoImpl.class);

	private static final String PREFIJO_CLIENTE = "CLI";
	private static final int LONGITUD_CODIGO_CLIENTE = 6;
	private static final int POSICION_INICIO_DIGITOS_CLIENTE = 3;

	private final DAOFactory daoFactory;

	public ConsultarClientePorIdCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para consultar cliente por identificador porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public ClienteDominio ejecutar(final String codigoCliente) {
		logger.info("Iniciando la consulta de un cliente por identificador.");

		var codigoClienteNormalizado = validarYNormalizarCodigoCliente(codigoCliente);

		var clienteEntidad = daoFactory.obtenerClienteDAO()
				.consultarPorId(codigoClienteNormalizado);

		if (UtilObjeto.esNulo(clienteEntidad)
				|| UtilTexto.esVacio(clienteEntidad.getCodigoCliente())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe un cliente registrado con el código indicado.");
		}

		var cliente = ClienteEntidadAssembler.getInstance()
				.ensamblarDominio(clienteEntidad);

		logger.info("Cliente consultado satisfactoriamente.");

		return cliente;
	}

	private String validarYNormalizarCodigoCliente(final String codigoCliente) {
		if (!UtilTexto.tieneTexto(codigoCliente)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del cliente es obligatorio.");
		}

		var codigoClienteNormalizado = UtilTexto.aplicarTrim(codigoCliente);

		if (codigoClienteNormalizado.length() != LONGITUD_CODIGO_CLIENTE) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del cliente debe tener exactamente "
							+ LONGITUD_CODIGO_CLIENTE + " caracteres.");
		}

		if (!iniciaConPrefijoCliente(codigoClienteNormalizado)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del cliente debe iniciar con " + PREFIJO_CLIENTE + ".");
		}

		if (!contieneSoloDigitos(
				codigoClienteNormalizado.substring(POSICION_INICIO_DIGITOS_CLIENTE))) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del cliente debe tener el formato CLI seguido de tres dígitos numéricos.");
		}

		return codigoClienteNormalizado;
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
}