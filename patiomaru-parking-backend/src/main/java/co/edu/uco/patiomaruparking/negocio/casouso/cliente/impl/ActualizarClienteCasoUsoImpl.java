package co.edu.uco.patiomaruparking.negocio.casouso.cliente.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.ClienteEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.ClienteEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.cliente.ActualizarClienteCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.ClienteDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class ActualizarClienteCasoUsoImpl implements ActualizarClienteCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(ActualizarClienteCasoUsoImpl.class);

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

	public ActualizarClienteCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para actualizar cliente porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final ClienteDominio datos) {
		logger.info("Iniciando la actualización de un cliente.");

		var clienteActualizar = UtilObjeto.obtenerValorDefecto(
				datos,
				ClienteDominio.builder().build());

		validarDatosConsistentes(clienteActualizar);

		var codigoCliente = UtilTexto.aplicarTrim(
				clienteActualizar.getCodigoCliente());

		var clienteActual = validarYObtenerCliente(codigoCliente);

		validarNoExisteOtroClienteConMismoTelefono(
				codigoCliente,
				clienteActualizar.getTelefono());

		validarNoExisteOtroClienteConMismoCorreoElectronico(
				codigoCliente,
				clienteActualizar.getCorreoElectronico());

		var clienteActualizado = ClienteDominio.builder()
				.codigoCliente(codigoCliente)
				.nombre(UtilTexto.aplicarTrim(clienteActualizar.getNombre()))
				.telefono(UtilTexto.aplicarTrim(clienteActualizar.getTelefono()))
				.correoElectronico(UtilTexto.aplicarTrim(clienteActualizar.getCorreoElectronico()))
				.estado(clienteActual.getEstado())
				.build();

		actualizar(clienteActualizado);

		logger.info("Cliente actualizado satisfactoriamente.");
	}

	private void validarDatosConsistentes(final ClienteDominio cliente) {
		validarCodigoCliente(cliente.getCodigoCliente());
		validarNombre(cliente.getNombre());
		validarTelefono(cliente.getTelefono());
		validarCorreoElectronicoSiFueInformado(cliente.getCorreoElectronico());
	}

	private void validarCodigoCliente(final String codigoCliente) {
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
				LONGITUD_MINIMA_CORREO_ELECTRONICO,
				LONGITUD_MAXIMA_CORREO_ELECTRONICO,
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

	private ClienteDominio validarYObtenerCliente(final String codigoCliente) {
		var clienteEntidad = daoFactory.obtenerClienteDAO()
				.consultarPorId(codigoCliente);

		if (UtilObjeto.esNulo(clienteEntidad)
				|| !UtilTexto.tieneTexto(clienteEntidad.getCodigoCliente())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe un cliente registrado con el código indicado.");
		}

		return ClienteEntidadAssembler.getInstance()
				.ensamblarDominio(clienteEntidad);
	}

	private void validarNoExisteOtroClienteConMismoTelefono(
			final String codigoCliente,
			final String telefono) {

		var filtro = ClienteEntidad.builder()
				.telefono(UtilTexto.aplicarTrim(telefono))
				.build();

		var resultados = UtilObjeto.obtenerValorDefecto(
				daoFactory.obtenerClienteDAO().consultar(filtro),
				List.<ClienteEntidad>of());

		for (ClienteEntidad cliente : resultados) {
			var clienteSeguro = UtilObjeto.obtenerValorDefecto(
					cliente,
					ClienteEntidad.builder().build());

			if (!UtilTexto.sonIgualesIgnorandoMayusculas(
					clienteSeguro.getCodigoCliente(),
					codigoCliente)) {

				throw NegocioPatioMaruExcepcion.crear(
						"Ya existe otro cliente registrado con el mismo número de teléfono.");
			}
		}
	}

	private void validarNoExisteOtroClienteConMismoCorreoElectronico(
			final String codigoCliente,
			final String correoElectronico) {

		if (!UtilTexto.tieneTexto(correoElectronico)) {
			return;
		}

		var filtro = ClienteEntidad.builder()
				.correoElectronico(UtilTexto.aplicarTrim(correoElectronico))
				.build();

		var resultados = UtilObjeto.obtenerValorDefecto(
				daoFactory.obtenerClienteDAO().consultar(filtro),
				List.<ClienteEntidad>of());

		for (ClienteEntidad cliente : resultados) {
			var clienteSeguro = UtilObjeto.obtenerValorDefecto(
					cliente,
					ClienteEntidad.builder().build());

			if (!UtilTexto.sonIgualesIgnorandoMayusculas(
					clienteSeguro.getCodigoCliente(),
					codigoCliente)) {

				throw NegocioPatioMaruExcepcion.crear(
						"Ya existe otro cliente registrado con el mismo correo electrónico.");
			}
		}
	}

	private void actualizar(final ClienteDominio cliente) {
		var clienteEntidad = ClienteEntidadAssembler.getInstance()
				.ensamblarEntidad(cliente);

		daoFactory.obtenerClienteDAO()
				.actualizar(clienteEntidad);
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