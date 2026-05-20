package co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.impl;

import java.math.BigDecimal;

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

public final class ActualizarDetallePedidoCasoUsoImpl implements ActualizarDetallePedidoCasoUso {

	private static final int LONGITUD_CODIGO_DETALLE_PEDIDO = 5;
	private static final int LONGITUD_CODIGO_PEDIDO = 7;
	private static final int LONGITUD_CODIGO_PLATO = 6;

	private static final String ESTADO_CANCELADO = "CANCELADO";
	private static final String ESTADO_ENTREGADO = "ENTREGADO";

	private final DAOFactory daoFactory;

	public ActualizarDetallePedidoCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final DetallePedidoDominio datos) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango.
		validarDatosConsistentes(datos);

		var codigoDetallePedido = UtilTexto.aplicarTrim(datos.getCodigoDetallePedido());
		var codigoPedido = UtilTexto.aplicarTrim(datos.getCodigoPedido());

		// 2. Debe existir el detalle del pedido que se desea actualizar.
		var detalleActualEntidad = daoFactory.obtenerDetallePedidoDAO().consultarPorId(codigoDetallePedido);

		if (UtilObjeto.esNulo(detalleActualEntidad)) {
			throw new RuntimeException("No existe un detalle de pedido registrado con el código indicado.");
		}

		// 3. Debe existir el pedido al que pertenece el detalle.
		var pedido = validarYObtenerPedido(codigoPedido);

		// 4. No se debe actualizar un detalle de un pedido cancelado o entregado.
		validarPedidoPermiteActualizarDetalle(pedido);

		// 5. Debe existir el plato asociado al detalle y debe estar disponible.
		var plato = validarYObtenerPlato(datos);

		// 6. No debe existir otro detalle con la misma combinación única documentada:
		// pedido + plato.
		validarNoExisteOtroDetalleConMismoPedidoYPlato(codigoDetallePedido, codigoPedido, plato.getCodigoPlato());

		// 7. Cálculo de valores:
		// subtotal = cantidad * precioVenta del plato.
		var subtotal = calcularSubtotal(datos.getCantidad(), plato.getPrecioVenta());

		var detalleActualizado = DetallePedidoDominio.builder()
				.codigoDetallePedido(codigoDetallePedido)
				.codigoPedido(codigoPedido)
				.cantidad(datos.getCantidad())
				.plato(plato)
				.subtotal(subtotal)
				.build();

		actualizar(detalleActualizado);
	}

	private void validarDatosConsistentes(final DetallePedidoDominio datos) {
		if (UtilObjeto.esNulo(datos)) {
			throw new RuntimeException("Los datos del detalle del pedido son obligatorios.");
		}

		if (!UtilTexto.tieneTexto(datos.getCodigoDetallePedido())) {
			throw new RuntimeException("El código del detalle del pedido es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(datos.getCodigoDetallePedido()).length() != LONGITUD_CODIGO_DETALLE_PEDIDO) {
			throw new RuntimeException("El código del detalle del pedido debe tener exactamente "
					+ LONGITUD_CODIGO_DETALLE_PEDIDO + " caracteres.");
		}

		if (!UtilTexto.tieneTexto(datos.getCodigoPedido())) {
			throw new RuntimeException("El código del pedido es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(datos.getCodigoPedido()).length() != LONGITUD_CODIGO_PEDIDO) {
			throw new RuntimeException("El código del pedido debe tener exactamente "
					+ LONGITUD_CODIGO_PEDIDO + " caracteres.");
		}

		if (UtilObjeto.esNulo(datos.getCantidad())) {
			throw new RuntimeException("La cantidad del detalle del pedido es obligatoria.");
		}

		if (datos.getCantidad() <= 0) {
			throw new RuntimeException("La cantidad del detalle del pedido debe ser mayor que cero.");
		}

		if (UtilObjeto.esNulo(datos.getPlato())
				|| !UtilTexto.tieneTexto(datos.getPlato().getCodigoPlato())) {
			throw new RuntimeException("El plato del detalle del pedido es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(datos.getPlato().getCodigoPlato()).length() != LONGITUD_CODIGO_PLATO) {
			throw new RuntimeException("El código del plato debe tener exactamente "
					+ LONGITUD_CODIGO_PLATO + " caracteres.");
		}
	}

	private PedidoDominio validarYObtenerPedido(final String codigoPedido) {
		var pedidoEntidad = daoFactory.obtenerPedidoDAO().consultarPorId(codigoPedido);

		if (UtilObjeto.esNulo(pedidoEntidad)) {
			throw new RuntimeException("No existe un pedido registrado con el código indicado.");
		}

		return PedidoEntidadAssembler.getInstance().ensamblarDominio(pedidoEntidad);
	}

	private void validarPedidoPermiteActualizarDetalle(final PedidoDominio pedido) {
		var estadoPedido = UtilTexto.aplicarTrimConvertirMayusculas(pedido.getEstado());

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoPedido, ESTADO_CANCELADO)) {
			throw new RuntimeException("No es posible actualizar detalles de un pedido cancelado.");
		}

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoPedido, ESTADO_ENTREGADO)) {
			throw new RuntimeException("No es posible actualizar detalles de un pedido entregado.");
		}
	}

	private PlatoDominio validarYObtenerPlato(final DetallePedidoDominio datos) {
		var codigoPlato = UtilTexto.aplicarTrim(datos.getPlato().getCodigoPlato());

		var platoEntidad = daoFactory.obtenerPlatoDAO().consultarPorId(codigoPlato);

		if (UtilObjeto.esNulo(platoEntidad)) {
			throw new RuntimeException("No existe un plato registrado con el código indicado.");
		}

		var plato = PlatoEntidadAssembler.getInstance().ensamblarDominio(platoEntidad);

		if (!plato.estaDisponible()) {
			throw new RuntimeException("El plato " + plato.getNombre() + " no se encuentra disponible.");
		}

		if (!plato.tienePrecioVentaValido()) {
			throw new RuntimeException("El plato " + plato.getNombre() + " no tiene un precio de venta válido.");
		}

		return plato;
	}

	private void validarNoExisteOtroDetalleConMismoPedidoYPlato(final String codigoDetallePedido,
			final String codigoPedido, final String codigoPlato) {

		var filtro = DetallePedidoEntidad.builder()
				.codigoPedido(codigoPedido)
				.plato(PlatoEntidad.builder()
						.codigoPlato(codigoPlato)
						.build())
				.build();

		var resultados = daoFactory.obtenerDetallePedidoDAO().consultar(filtro);

		if (UtilObjeto.esNulo(resultados) || resultados.isEmpty()) {
			return;
		}

		for (DetallePedidoEntidad detalle : resultados) {
			if (!UtilTexto.sonIgualesIgnorandoMayusculas(
					detalle.getCodigoDetallePedido(), codigoDetallePedido)) {
				throw new RuntimeException("Ya existe otro detalle registrado para el mismo pedido y el mismo plato.");
			}
		}
	}

	private BigDecimal calcularSubtotal(final Integer cantidad, final BigDecimal precioVenta) {
		return precioVenta.multiply(BigDecimal.valueOf(cantidad));
	}

	private void actualizar(final DetallePedidoDominio detalle) {
		var detalleEntidad = DetallePedidoEntidadAssembler.getInstance().ensamblarEntidad(detalle);
		daoFactory.obtenerDetallePedidoDAO().actualizar(detalleEntidad);
	}
}
