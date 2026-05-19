package co.edu.uco.patiomaruparking.negocio.casouso.pedido.impl;

import java.util.Objects;
import java.util.Set;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.pedido.ActualizarEstadoPedidoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.PedidoDominio;

public final class ActualizarEstadoPedidoCasoUsoImpl implements ActualizarEstadoPedidoCasoUso {

	private static final int LONGITUD_CODIGO_PEDIDO = 7;

	private static final String ESTADO_CANCELADO = "CANCELADO";

	private static final Set<String> ESTADOS_PERMITIDOS = Set.of(
			"REGISTRADO",
			"EN PREPARACION",
			"PREPARADO",
			"ENTREGADO"
	);

	private final DAOFactory daoFactory;

	public ActualizarEstadoPedidoCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final PedidoDominio datos) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango.
		validarDatosConsistentes(datos);

		var codigoPedido = datos.getCodigoPedido().trim();
		var nuevoEstado = normalizarEstado(datos.getEstado());

		// 2. Debe existir un pedido registrado con el código indicado.
		var pedidoEntidad = daoFactory.obtenerPedidoDAO().consultarPorId(codigoPedido);

		if (Objects.isNull(pedidoEntidad)) {
			throw new RuntimeException("No existe un pedido registrado con el código indicado.");
		}

		var pedidoActual = PedidoEntidadAssembler.getInstance().ensamblarDominio(pedidoEntidad);
		var estadoActual = normalizarEstado(pedidoActual.getEstado());

		// 3. No se debe actualizar el estado de un pedido cancelado.
		if (ESTADO_CANCELADO.equalsIgnoreCase(estadoActual)) {
			throw new RuntimeException("No es posible actualizar el estado de un pedido cancelado.");
		}

		// 4. La responsabilidad de cancelar pedido se maneja en otro caso de uso.
		if (ESTADO_CANCELADO.equalsIgnoreCase(nuevoEstado)) {
			throw new RuntimeException("Para cancelar un pedido debe usar la operación Cancelar Pedido.");
		}

		// 5. El nuevo estado no debe ser igual al estado actual.
		if (estadoActual.equalsIgnoreCase(nuevoEstado)) {
			throw new RuntimeException("El pedido ya se encuentra en el estado indicado.");
		}

		// 6. Actualizar el estado del pedido.
		daoFactory.obtenerPedidoDAO().actualizarEstado(codigoPedido, nuevoEstado);
	}

	private void validarDatosConsistentes(final PedidoDominio datos) {
		if (Objects.isNull(datos)) {
			throw new RuntimeException("Los datos para actualizar el estado del pedido son obligatorios.");
		}

		if (!tieneTexto(datos.getCodigoPedido())) {
			throw new RuntimeException("El código del pedido es obligatorio.");
		}

		if (datos.getCodigoPedido().trim().length() != LONGITUD_CODIGO_PEDIDO) {
			throw new RuntimeException("El código del pedido debe tener exactamente "
					+ LONGITUD_CODIGO_PEDIDO + " caracteres.");
		}

		if (!tieneTexto(datos.getEstado())) {
			throw new RuntimeException("El nuevo estado del pedido es obligatorio.");
		}

		validarLongitud(datos.getEstado(), 6, 14, "El estado del pedido");

		var estadoNormalizado = normalizarEstado(datos.getEstado());

		if (!ESTADOS_PERMITIDOS.contains(estadoNormalizado) && !ESTADO_CANCELADO.equals(estadoNormalizado)) {
			throw new RuntimeException(
					"El estado del pedido no es válido. Estados permitidos: REGISTRADO, EN PREPARACION, PREPARADO, ENTREGADO.");
		}
	}

	private void validarLongitud(final String valor, final int longitudMinima, final int longitudMaxima,
			final String nombreCampo) {

		if (valor.trim().length() < longitudMinima || valor.trim().length() > longitudMaxima) {
			throw new RuntimeException(nombreCampo + " debe tener entre " + longitudMinima + " y "
					+ longitudMaxima + " caracteres.");
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
