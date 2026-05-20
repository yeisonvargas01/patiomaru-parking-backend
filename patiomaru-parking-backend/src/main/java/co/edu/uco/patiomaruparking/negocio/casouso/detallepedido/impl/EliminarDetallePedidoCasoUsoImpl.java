package co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.impl;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.EliminarDetallePedidoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.PedidoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public final class EliminarDetallePedidoCasoUsoImpl implements EliminarDetallePedidoCasoUso {

	private static final int LONGITUD_CODIGO_DETALLE_PEDIDO = 5;

	private static final String ESTADO_CANCELADO = "CANCELADO";
	private static final String ESTADO_ENTREGADO = "ENTREGADO";

	private final DAOFactory daoFactory;

	public EliminarDetallePedidoCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final String codigoDetallePedido) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango.
		validarCodigoDetallePedido(codigoDetallePedido);

		var codigoDetallePedidoNormalizado = UtilTexto.aplicarTrim(codigoDetallePedido);

		// 2. Debe existir el detalle del pedido que se desea eliminar.
		var detallePedidoEntidad = daoFactory.obtenerDetallePedidoDAO()
				.consultarPorId(codigoDetallePedidoNormalizado);

		if (UtilObjeto.esNulo(detallePedidoEntidad)) {
			throw new RuntimeException("No existe un detalle de pedido registrado con el código indicado.");
		}

		// 3. Debe existir el pedido al que pertenece el detalle.
		var pedido = validarYObtenerPedido(detallePedidoEntidad.getCodigoPedido());

		// 4. No se debe eliminar un detalle de un pedido cancelado o entregado.
		validarPedidoPermiteEliminarDetalle(pedido);

		// 5. Eliminar el detalle del pedido.
		daoFactory.obtenerDetallePedidoDAO().eliminar(codigoDetallePedidoNormalizado);
	}

	private void validarCodigoDetallePedido(final String codigoDetallePedido) {
		if (!UtilTexto.tieneTexto(codigoDetallePedido)) {
			throw new RuntimeException("El código del detalle del pedido es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(codigoDetallePedido).length() != LONGITUD_CODIGO_DETALLE_PEDIDO) {
			throw new RuntimeException("El código del detalle del pedido debe tener exactamente "
					+ LONGITUD_CODIGO_DETALLE_PEDIDO + " caracteres.");
		}
	}

	private PedidoDominio validarYObtenerPedido(final String codigoPedido) {
		if (!UtilTexto.tieneTexto(codigoPedido)) {
			throw new RuntimeException("El detalle del pedido no tiene un pedido asociado.");
		}

		var pedidoEntidad = daoFactory.obtenerPedidoDAO()
				.consultarPorId(UtilTexto.aplicarTrim(codigoPedido));

		if (UtilObjeto.esNulo(pedidoEntidad)) {
			throw new RuntimeException("No existe el pedido asociado al detalle indicado.");
		}

		return PedidoEntidadAssembler.getInstance().ensamblarDominio(pedidoEntidad);
	}

	private void validarPedidoPermiteEliminarDetalle(final PedidoDominio pedido) {
		var estadoPedido = UtilTexto.aplicarTrimConvertirMayusculas(pedido.getEstado());

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoPedido, ESTADO_CANCELADO)) {
			throw new RuntimeException("No es posible eliminar detalles de un pedido cancelado.");
		}

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoPedido, ESTADO_ENTREGADO)) {
			throw new RuntimeException("No es posible eliminar detalles de un pedido entregado.");
		}
	}
}
