package co.edu.uco.patiomaruparking.negocio.casouso.pedido.impl;

import java.util.Objects;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.pedido.CancelarPedidoCasoUso;

public final class CancelarPedidoCasoUsoImpl implements CancelarPedidoCasoUso {

	private static final int LONGITUD_CODIGO_PEDIDO = 7;

	private static final String ESTADO_CANCELADO = "CANCELADO";
	private static final String ESTADO_ENTREGADO = "ENTREGADO";

	private final DAOFactory daoFactory;

	public CancelarPedidoCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final String codigoPedido) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango.
		validarCodigoPedido(codigoPedido);

		var codigoPedidoNormalizado = codigoPedido.trim();

		// 2. Debe existir un pedido registrado con el código indicado.
		var pedidoEntidad = daoFactory.obtenerPedidoDAO().consultarPorId(codigoPedidoNormalizado);

		if (Objects.isNull(pedidoEntidad)) {
			throw new RuntimeException("No existe un pedido registrado con el código indicado.");
		}

		var pedido = PedidoEntidadAssembler.getInstance().ensamblarDominio(pedidoEntidad);
		var estadoActual = normalizarEstado(pedido.getEstado());

		// 3. No se debe cancelar un pedido que ya se encuentra cancelado.
		if (ESTADO_CANCELADO.equalsIgnoreCase(estadoActual)) {
			throw new RuntimeException("El pedido ya se encuentra cancelado.");
		}

		// 4. No se debe cancelar un pedido que ya fue entregado.
		if (ESTADO_ENTREGADO.equalsIgnoreCase(estadoActual)) {
			throw new RuntimeException("No es posible cancelar un pedido que ya fue entregado.");
		}

		// 5. Cancelar el pedido.
		daoFactory.obtenerPedidoDAO().cancelar(codigoPedidoNormalizado);
	}

	private void validarCodigoPedido(final String codigoPedido) {
		if (!tieneTexto(codigoPedido)) {
			throw new RuntimeException("El código del pedido es obligatorio.");
		}

		if (codigoPedido.trim().length() != LONGITUD_CODIGO_PEDIDO) {
			throw new RuntimeException("El código del pedido debe tener exactamente "
					+ LONGITUD_CODIGO_PEDIDO + " caracteres.");
		}
	}

	private String normalizarEstado(final String estado) {
		return Objects.toString(estado, "")
				.trim()
				.toUpperCase();
	}

	private boolean tieneTexto(final String texto) {
		return Objects.nonNull(texto) && !texto.trim().isEmpty();
	}
}
