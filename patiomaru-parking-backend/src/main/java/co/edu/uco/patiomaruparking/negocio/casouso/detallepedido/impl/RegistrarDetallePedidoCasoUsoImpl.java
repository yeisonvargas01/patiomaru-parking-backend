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
import co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.RegistrarDetallePedidoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio;
import co.edu.uco.patiomaruparking.negocio.dominio.PedidoDominio;
import co.edu.uco.patiomaruparking.negocio.dominio.PlatoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilCodigo;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class RegistrarDetallePedidoCasoUsoImpl implements RegistrarDetallePedidoCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(RegistrarDetallePedidoCasoUsoImpl.class);

	private static final String PREFIJO_PEDIDO = "PEDD";
	private static final int LONGITUD_CODIGO_PEDIDO = 7;
	private static final int POSICION_INICIO_DIGITOS_PEDIDO = 4;

	private static final String PREFIJO_DETALLE_PEDIDO = "DP";
	private static final int CANTIDAD_DIGITOS_DETALLE_PEDIDO = 3;

	private static final String PREFIJO_PLATO = "PLT";
	private static final int LONGITUD_CODIGO_PLATO = 6;
	private static final int POSICION_INICIO_DIGITOS_PLATO = 3;

	private static final int CANTIDAD_MINIMA_PERMITIDA = 1;

	private static final String ESTADO_REGISTRADO = "Registrado";

	private final DAOFactory daoFactory;

	public RegistrarDetallePedidoCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para registrar detalle de pedido porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public DetallePedidoDominio ejecutar(final DetallePedidoDominio datos) {
		logger.info("Iniciando el registro de un detalle de pedido.");

		var detalle = UtilObjeto.obtenerValorDefecto(
				datos,
				DetallePedidoDominio.builder().build());

		validarDatosConsistentes(detalle);

		var codigoPedido = UtilTexto.aplicarTrim(detalle.getCodigoPedido());

		var pedido = validarYObtenerPedido(codigoPedido);

		validarPedidoPermiteRegistrarDetalle(pedido);

		var plato = validarYObtenerPlato(detalle);

		validarNoExisteDetalleConMismoPedidoYPlato(
				codigoPedido,
				plato.getCodigoPlato());

		var codigoDetallePedido = generarCodigoUnicoDetallePedido();

		var cantidad = obtenerCantidad(detalle);

		var subtotal = calcularSubtotal(
				cantidad,
				plato.getPrecioVenta());

		var detallePreparado = DetallePedidoDominio.builder()
				.codigoDetallePedido(codigoDetallePedido)
				.codigoPedido(codigoPedido)
				.cantidad(cantidad)
				.plato(plato)
				.subtotal(subtotal)
				.build();

		guardar(detallePreparado);

		logger.info("Detalle de pedido registrado satisfactoriamente.");

		return detallePreparado;
	}

	private void validarDatosConsistentes(final DetallePedidoDominio detalle) {
		validarCodigoPedido(detalle.getCodigoPedido());
		validarCantidad(detalle);
		validarPlato(detalle);
	}

	private void validarCodigoPedido(final String codigoPedido) {
		if (!UtilTexto.tieneTexto(codigoPedido)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido es obligatorio para registrar el detalle.");
		}

		var codigoPedidoNormalizado = UtilTexto.aplicarTrim(codigoPedido);

		if (codigoPedidoNormalizado.length() != LONGITUD_CODIGO_PEDIDO) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido debe tener exactamente "
							+ LONGITUD_CODIGO_PEDIDO + " caracteres.");
		}

		if (!codigoPedidoNormalizado.startsWith(PREFIJO_PEDIDO)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del pedido debe iniciar con " + PREFIJO_PEDIDO + ".");
		}

		if (!contieneSoloDigitos(codigoPedidoNormalizado.substring(POSICION_INICIO_DIGITOS_PEDIDO))) {
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

	private void validarPlato(final DetallePedidoDominio detalle) {
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

		if (!codigoPlato.startsWith(PREFIJO_PLATO)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del plato debe iniciar con " + PREFIJO_PLATO + ".");
		}

		if (!contieneSoloDigitos(codigoPlato.substring(POSICION_INICIO_DIGITOS_PLATO))) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del plato debe tener el formato PLT seguido de tres dígitos numéricos.");
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

	private void validarPedidoPermiteRegistrarDetalle(final PedidoDominio pedido) {
		if (!UtilTexto.sonIgualesIgnorandoMayusculas(pedido.getEstado(), ESTADO_REGISTRADO)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El pedido no permite registrar nuevos detalles porque su estado actual no es Registrado.");
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

	private void validarNoExisteDetalleConMismoPedidoYPlato(
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

		if (!resultados.isEmpty()) {
			throw NegocioPatioMaruExcepcion.crear(
					"Ya existe un detalle registrado para el mismo pedido y el mismo plato.");
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

	private void guardar(final DetallePedidoDominio detalle) {
		var detalleEntidad = DetallePedidoEntidadAssembler.getInstance()
				.ensamblarEntidad(detalle);

		daoFactory.obtenerDetallePedidoDAO()
				.registrar(detalleEntidad);
	}

	private String generarCodigoUnicoDetallePedido() {
		String codigoDetallePedido;
		DetallePedidoEntidad detalleExistente;

		do {
			codigoDetallePedido = UtilCodigo.generarCodigo(
					PREFIJO_DETALLE_PEDIDO,
					CANTIDAD_DIGITOS_DETALLE_PEDIDO);

			detalleExistente = daoFactory.obtenerDetallePedidoDAO()
					.consultarPorId(codigoDetallePedido);

		} while (UtilObjeto.noEsNulo(detalleExistente)
				&& UtilTexto.tieneTexto(detalleExistente.getCodigoDetallePedido()));

		return codigoDetallePedido;
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