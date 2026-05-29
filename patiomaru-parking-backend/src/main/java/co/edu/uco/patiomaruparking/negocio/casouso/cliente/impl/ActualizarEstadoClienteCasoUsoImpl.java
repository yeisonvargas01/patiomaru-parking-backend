package co.edu.uco.patiomaruparking.negocio.casouso.cliente.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.ClienteEntidad;
import co.edu.uco.patiomaruparking.entidad.PedidoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.ClienteEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.cliente.ActualizarEstadoClienteCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.ClienteDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class ActualizarEstadoClienteCasoUsoImpl implements ActualizarEstadoClienteCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(ActualizarEstadoClienteCasoUsoImpl.class);

	private static final String PREFIJO_CLIENTE = "CLI";
	private static final int LONGITUD_CODIGO_CLIENTE = 6;
	private static final int POSICION_INICIO_DIGITOS_CLIENTE = 3;

	private static final String ESTADO_PEDIDO_ENTREGADO = "Entregado";
	private static final String ESTADO_PEDIDO_CANCELADO = "Cancelado";

	private final DAOFactory daoFactory;

	public ActualizarEstadoClienteCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para actualizar estado del cliente porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final ClienteDominio datos) {
		logger.info("Iniciando la actualización del estado de un cliente.");

		var clienteActualizar = UtilObjeto.obtenerValorDefecto(
				datos,
				ClienteDominio.builder().build());

		var codigoCliente = validarYNormalizarCodigoCliente(
				clienteActualizar.getCodigoCliente());

		var nuevoEstado = validarYObtenerNuevoEstado(
				clienteActualizar.getEstado());

		var clienteActual = validarYObtenerCliente(codigoCliente);

		validarEstadoDiferente(
				clienteActual.getEstado(),
				nuevoEstado);

		validarReglasParaInactivarCliente(
				codigoCliente,
				nuevoEstado);

		daoFactory.obtenerClienteDAO()
				.actualizarEstado(codigoCliente, nuevoEstado);

		logger.info("Estado del cliente actualizado satisfactoriamente.");
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

	private Boolean validarYObtenerNuevoEstado(final Boolean estado) {
		if (UtilObjeto.esNulo(estado)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El estado del cliente es obligatorio.");
		}

		return estado;
	}

	private ClienteDominio validarYObtenerCliente(final String codigoCliente) {
		var clienteEntidad = daoFactory.obtenerClienteDAO()
				.consultarPorId(codigoCliente);

		if (UtilObjeto.esNulo(clienteEntidad)
				|| UtilTexto.esVacio(clienteEntidad.getCodigoCliente())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe un cliente registrado con el código indicado.");
		}

		return ClienteEntidadAssembler.getInstance()
				.ensamblarDominio(clienteEntidad);
	}

	private void validarEstadoDiferente(
			final Boolean estadoActual,
			final Boolean nuevoEstado) {

		if (esMismoEstado(estadoActual, nuevoEstado)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El cliente ya se encuentra con el estado indicado.");
		}
	}

	private boolean esMismoEstado(
			final Boolean estadoActual,
			final Boolean nuevoEstado) {

		return Boolean.TRUE.equals(estadoActual) && Boolean.TRUE.equals(nuevoEstado)
				|| Boolean.FALSE.equals(estadoActual) && Boolean.FALSE.equals(nuevoEstado);
	}

	private void validarReglasParaInactivarCliente(
			final String codigoCliente,
			final Boolean nuevoEstado) {

		if (Boolean.TRUE.equals(nuevoEstado)) {
			return;
		}

		var filtro = PedidoEntidad.builder()
				.cliente(ClienteEntidad.builder()
						.codigoCliente(codigoCliente)
						.build())
				.build();

		var pedidos = UtilObjeto.obtenerValorDefecto(
				daoFactory.obtenerPedidoDAO().consultar(filtro),
				List.<PedidoEntidad>of());

		for (PedidoEntidad pedido : pedidos) {
			var pedidoSeguro = UtilObjeto.obtenerValorDefecto(
					pedido,
					PedidoEntidad.builder().build());

			if (pedidoImpideInactivarCliente(pedidoSeguro)) {
				throw NegocioPatioMaruExcepcion.crear(
						"No es posible inactivar el cliente porque tiene pedidos activos o pendientes asociados.");
			}
		}
	}

	private boolean pedidoImpideInactivarCliente(final PedidoEntidad pedido) {
		return !pedidoEstaFinalizado(pedido.getEstado());
	}

	private boolean pedidoEstaFinalizado(final String estadoPedido) {
		return UtilTexto.sonIgualesIgnorandoMayusculas(
				estadoPedido,
				ESTADO_PEDIDO_ENTREGADO)
				|| UtilTexto.sonIgualesIgnorandoMayusculas(
						estadoPedido,
						ESTADO_PEDIDO_CANCELADO);
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
