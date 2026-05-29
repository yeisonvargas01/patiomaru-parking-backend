package co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.DetallePedidoEntidad;
import co.edu.uco.patiomaruparking.entidad.PlatoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.DetallePedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PlatoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.ActualizarDetallePedidoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio;
import co.edu.uco.patiomaruparking.negocio.dominio.PedidoDominio;
import co.edu.uco.patiomaruparking.negocio.dominio.PlatoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class ActualizarDetallePedidoCasoUsoImpl implements ActualizarDetallePedidoCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(ActualizarDetallePedidoCasoUsoImpl.class);

	private static final String PREFIJO_DETALLE_PEDIDO = "DP";
	private static final int LONGITUD_CODIGO_DETALLE_PEDIDO = 5;
	private static final int POSICION_INICIO_DIGITOS_DETALLE_PEDIDO = 2;

	private static final String PREFIJO_PEDIDO = "PEDD";
	private static final int LONGITUD_CODIGO_PEDIDO = 7;
	private static final int POSICION_INICIO_DIGITOS_PEDIDO = 4;

	private static final String PREFIJO_PLATO = "PLT";
	private static final int LONGITUD_CODIGO_PLATO = 6;
	private static final int POSICION_INICIO_DIGITOS_PLATO = 3;

	private static final int CANTIDAD_MINIMA_PERMITIDA = 1;

	private static final String ESTADO_REGISTRADO = "Registrado";

	private final DAOFactory daoFactory;

	public ActualizarDetallePedidoCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para actualizar detalle de pedido porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final DetallePedidoDominio datos) {
		logger.info("Iniciando la actualización de un detalle de pedido.");

		var detalleActualizar = UtilObjeto.obtenerValorDefecto(
				datos,
				DetallePedidoDominio.builder().build());

		validarDatosConsistentes(detalleActualizar);

		var codigoDetallePedido = UtilTexto.aplicarTrim(
				detalleActualizar.getCodigoDetallePedido());

		var codigoPedido = UtilTexto.aplicarTrim(
				detalleActualizar.getCodigoPedido());

		var detalleActualEntidad = validarYObtenerDetallePedido(
				codigoDetallePedido);

		validarDetallePerteneceAlPedido(
				detalleActualEntidad,
				codigoPedido);

		var pedido = validarYObtenerPedido(codigoPedido);

		validarPedidoPermiteActualizarDetalle(pedido);

		var plato = validarYObtenerPlato(detalleActualizar);

		validarNoExisteOtroDetalleConMismoPedidoYPlato(
				codigoDetallePedido,
				codigoPedido,
				plato.getCodigoPlato());

		var cantidad = obtenerCantidad(detalleActualizar);

		var subtotal = calcularSubtotal(
				cantidad,
				plato.getPrecioVenta());

		var detalleActualizado = DetallePedidoDominio.builder()
				.codigoDetallePedido(codigoDetallePedido)
				.codigoPedido(codigoPedido)
				.cantidad(cantidad)
				.plato(plato)
				.subtotal(subtotal)
				.build();

		actualizar(detalleActualizado);

		logger.info("Detalle de pedido actualizado satisfactoriamente.");
	}

	private void validarDatosConsistentes(final DetallePedidoDominio detalle) {
		validarCodigoDetallePedido(detalle.getCodigoDetallePedido());
		validarCodigoPedido(detalle.getCodigoPedido());
		validarCantidad(detalle);
		validarCodigoPlato(detalle);
	}

	private void validarCodigoDetallePedido(final String codigoDetallePedido) {
		if (!UtilTexto.tieneTexto(codigoDetallePedido)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del detalle del pedido es obligatorio.");
		}

		var codigoDetallePedidoNormalizado = UtilTexto.aplicarTrim(codigoDetallePedido);

		if (codigoDetallePedidoNormalizado.length() != LONGITUD_CODIGO_DETALLE_PEDIDO) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del detalle del pedido debe tener exactamente "
							+ LONGITUD_CODIGO_DETALLE_PEDIDO + " caracteres.");
		}

		if (!iniciaConPrefijo(
				codigoDetallePedidoNormalizado,
				PREFIJO_DETALLE_PEDIDO,
				POSICION_INICIO_DIGITOS_DETALLE_PEDIDO)) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del detalle del pedido debe iniciar con " + PREFIJO_DETALLE_PEDIDO + ".");
		}

		if (!contieneSoloDigitos(
				codigoDetallePedidoNormalizado.substring(POSICION_INICIO_DIGITOS_DETALLE_PEDIDO))) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del detalle del pedido debe tener el formato DP seguido de tres dígitos numéricos.");
		}
	}

	private void validarCodigoPedido(final String codigoPedido) {
		if (!UtilTexto.tieneTexto(codigoPedido)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido es obligatorio.");
		}

		var codigoPedidoNormalizado = UtilTexto.aplicarTrim(codigoPedido);

		if (codigoPedidoNormalizado.length() != LONGITUD_CODIGO_PEDIDO) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido debe tener exactamente "
							+ LONGITUD_CODIGO_PEDIDO + " caracteres.");
		}

		if (!iniciaConPrefijo(
				codigoPedidoNormalizado,
				PREFIJO_PEDIDO,
				POSICION_INICIO_DIGITOS_PEDIDO)) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido debe iniciar con " + PREFIJO_PEDIDO + ".");
		}

		if (!contieneSoloDigitos(
				codigoPedidoNormalizado.substring(POSICION_INICIO_DIGITOS_PEDIDO))) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido debe tener el formato PEDD seguido de tres dígitos numéricos.");
		}
	}

	private void validarCantidad(final DetallePedidoDominio detalle) {
		var cantidad = obtenerCantidad(detalle);

		if (cantidad < CANTIDAD_MINIMA_PERMITIDA) {
			throw NegocioPatioMaruExcepcion.crear(
					"La cantidad del detalle del pedido debe ser mayor que cero.");
		}
	}

	private Integer obtenerCantidad(final DetallePedidoDominio detalle) {
		return UtilObjeto.obtenerValorDefecto(
				detalle.getCantidad(),
				0);
	}

	private void validarCodigoPlato(final DetallePedidoDominio detalle) {
		if (!UtilTexto.tieneTexto(detalle.getPlato().getCodigoPlato())) {
			throw NegocioPatioMaruExcepcion.crear(
					"El plato del detalle del pedido es obligatorio.");
		}

		var codigoPlato = UtilTexto.aplicarTrim(
				detalle.getPlato().getCodigoPlato());

		if (codigoPlato.length() != LONGITUD_CODIGO_PLATO) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del plato debe tener exactamente "
							+ LONGITUD_CODIGO_PLATO + " caracteres.");
		}

		if (!iniciaConPrefijo(
				codigoPlato,
				PREFIJO_PLATO,
				POSICION_INICIO_DIGITOS_PLATO)) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del plato debe iniciar con " + PREFIJO_PLATO + ".");
		}

		if (!contieneSoloDigitos(
				codigoPlato.substring(POSICION_INICIO_DIGITOS_PLATO))) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del plato debe tener el formato PLT seguido de tres dígitos numéricos.");
		}
	}

	private DetallePedidoEntidad validarYObtenerDetallePedido(final String codigoDetallePedido) {
		var detallePedidoEntidad = daoFactory.obtenerDetallePedidoDAO()
				.consultarPorId(codigoDetallePedido);

		if (UtilObjeto.esNulo(detallePedidoEntidad)
				|| UtilTexto.esVacio(detallePedidoEntidad.getCodigoDetallePedido())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe un detalle de pedido registrado con el código indicado.");
		}

		return detallePedidoEntidad;
	}

	private void validarDetallePerteneceAlPedido(
			final DetallePedidoEntidad detalleActual,
			final String codigoPedido) {

		if (!UtilTexto.sonIgualesIgnorandoMayusculas(
				detalleActual.getCodigoPedido(),
				codigoPedido)) {

			throw NegocioPatioMaruExcepcion.crear(
					"El detalle del pedido no pertenece al pedido indicado.");
		}
	}

	private PedidoDominio validarYObtenerPedido(final String codigoPedido) {
		var pedidoEntidad = daoFactory.obtenerPedidoDAO()
				.consultarPorId(codigoPedido);

		if (UtilObjeto.esNulo(pedidoEntidad)
				|| UtilTexto.esVacio(pedidoEntidad.getCodigoPedido())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe un pedido registrado con el código indicado.");
		}

		return PedidoEntidadAssembler.getInstance()
				.ensamblarDominio(pedidoEntidad);
	}

	private void validarPedidoPermiteActualizarDetalle(final PedidoDominio pedido) {
		if (!UtilTexto.sonIgualesIgnorandoMayusculas(
				pedido.getEstado(),
				ESTADO_REGISTRADO)) {

			throw NegocioPatioMaruExcepcion.crear(
					"El pedido no permite actualizar detalles porque su estado actual no es Registrado.");
		}
	}

	private PlatoDominio validarYObtenerPlato(final DetallePedidoDominio detalle) {
		var codigoPlato = UtilTexto.aplicarTrim(
				detalle.getPlato().getCodigoPlato());

		var platoEntidad = daoFactory.obtenerPlatoDAO()
				.consultarPorId(codigoPlato);

		if (UtilObjeto.esNulo(platoEntidad)
				|| UtilTexto.esVacio(platoEntidad.getCodigoPlato())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe un plato registrado con el código indicado.");
		}

		var plato = PlatoEntidadAssembler.getInstance()
				.ensamblarDominio(platoEntidad);

		if (!plato.estaDisponible()) {
			throw NegocioPatioMaruExcepcion.crear(
					"El plato " + plato.getNombre() + " no se encuentra disponible.");
		}

		if (!plato.tienePrecioVentaValido()) {
			throw NegocioPatioMaruExcepcion.crear(
					"El plato " + plato.getNombre() + " no tiene un precio de venta válido.");
		}

		return plato;
	}

	private void validarNoExisteOtroDetalleConMismoPedidoYPlato(
			final String codigoDetallePedido,
			final String codigoPedido,
			final String codigoPlato) {

		var filtro = DetallePedidoEntidad.builder()
				.codigoPedido(UtilTexto.aplicarTrim(codigoPedido))
				.plato(PlatoEntidad.builder()
						.codigoPlato(UtilTexto.aplicarTrim(codigoPlato))
						.build())
				.build();

		var resultados = UtilObjeto.obtenerValorDefecto(
				daoFactory.obtenerDetallePedidoDAO().consultar(filtro),
				List.<DetallePedidoEntidad>of());

		for (DetallePedidoEntidad detalle : resultados) {
			var detalleSeguro = UtilObjeto.obtenerValorDefecto(
					detalle,
					DetallePedidoEntidad.builder().build());

			if (!UtilTexto.sonIgualesIgnorandoMayusculas(
					detalleSeguro.getCodigoDetallePedido(),
					codigoDetallePedido)) {

				throw NegocioPatioMaruExcepcion.crear(
						"Ya existe otro detalle registrado para el mismo pedido y el mismo plato.");
			}
		}
	}

	private BigDecimal calcularSubtotal(
			final Integer cantidad,
			final BigDecimal precioVenta) {

		var precioVentaSeguro = UtilObjeto.obtenerValorDefecto(
				precioVenta,
				BigDecimal.ZERO);

		return precioVentaSeguro.multiply(
				BigDecimal.valueOf(cantidad));
	}

	private void actualizar(final DetallePedidoDominio detalle) {
		var detalleEntidad = DetallePedidoEntidadAssembler.getInstance()
				.ensamblarEntidad(detalle);

		daoFactory.obtenerDetallePedidoDAO()
				.actualizar(detalleEntidad);
	}

	private boolean iniciaConPrefijo(
			final String codigo,
			final String prefijoEsperado,
			final int posicionFinPrefijo) {

		var prefijo = UtilTexto.aplicarTrim(codigo)
				.substring(0, posicionFinPrefijo);

		return UtilTexto.sonIgualesIgnorandoMayusculas(
				prefijo,
				prefijoEsperado);
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