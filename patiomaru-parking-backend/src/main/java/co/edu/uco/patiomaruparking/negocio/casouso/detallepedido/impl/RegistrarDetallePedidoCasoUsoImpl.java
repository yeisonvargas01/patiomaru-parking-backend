package co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.impl;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Random;

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

public final class RegistrarDetallePedidoCasoUsoImpl implements RegistrarDetallePedidoCasoUso {

	private static final int LONGITUD_CODIGO_PEDIDO = 7;
	private static final String ESTADO_CANCELADO = "CANCELADO";
	private static final String ESTADO_ENTREGADO = "ENTREGADO";
	private static final Random RANDOM = new Random();

	private final DAOFactory daoFactory;

	public RegistrarDetallePedidoCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public DetallePedidoDominio ejecutar(final DetallePedidoDominio datos) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango.
		validarDatosConsistentes(datos);

		// 2. Debe existir el pedido al que pertenece el detalle.
		var pedido = validarYObtenerPedido(datos.getCodigoPedido());

		// 3. No se debe registrar un detalle en un pedido cancelado o entregado.
		validarPedidoPermiteRegistrarDetalle(pedido);

		// 4. Debe existir el plato asociado al detalle y debe estar disponible.
		var plato = validarYObtenerPlato(datos);

		// 5. No debe existir un detalle con la misma combinación única documentada:
		// pedido + plato.
		validarNoExisteDetalleConMismoPedidoYPlato(datos.getCodigoPedido(), plato.getCodigoPlato());

		// 6. El código del detalle del pedido debe ser único.
		var codigoDetallePedido = generarCodigoUnicoDetallePedido();

		// 7. Cálculo de valores:
		// subtotal = cantidad * precioVenta del plato.
		var subtotal = calcularSubtotal(datos.getCantidad(), plato.getPrecioVenta());

		var detallePreparado = DetallePedidoDominio.builder()
				.codigoDetallePedido(codigoDetallePedido)
				.codigoPedido(datos.getCodigoPedido().trim())
				.cantidad(datos.getCantidad())
				.plato(plato)
				.subtotal(subtotal)
				.build();

		guardar(detallePreparado);

		return detallePreparado;
	}

	private void validarDatosConsistentes(final DetallePedidoDominio datos) {
		if (Objects.isNull(datos)) {
			throw new RuntimeException("Los datos del detalle del pedido son obligatorios.");
		}

		if (!tieneTexto(datos.getCodigoPedido())) {
			throw new RuntimeException("El código del pedido es obligatorio para registrar el detalle.");
		}

		if (datos.getCodigoPedido().trim().length() != LONGITUD_CODIGO_PEDIDO) {
			throw new RuntimeException("El código del pedido debe tener exactamente "
					+ LONGITUD_CODIGO_PEDIDO + " caracteres.");
		}

		if (Objects.isNull(datos.getCantidad())) {
			throw new RuntimeException("La cantidad del detalle del pedido es obligatoria.");
		}

		if (datos.getCantidad() <= 0) {
			throw new RuntimeException("La cantidad del detalle del pedido debe ser mayor que cero.");
		}

		if (Objects.isNull(datos.getPlato()) || !tieneTexto(datos.getPlato().getCodigoPlato())) {
			throw new RuntimeException("El plato del detalle del pedido es obligatorio.");
		}
	}

	private PedidoDominio validarYObtenerPedido(final String codigoPedido) {
		var pedidoEntidad = daoFactory.obtenerPedidoDAO().consultarPorId(codigoPedido.trim());

		if (Objects.isNull(pedidoEntidad)) {
			throw new RuntimeException("No existe un pedido registrado con el código indicado.");
		}

		return PedidoEntidadAssembler.getInstance().ensamblarDominio(pedidoEntidad);
	}

	private void validarPedidoPermiteRegistrarDetalle(final PedidoDominio pedido) {
		var estadoPedido = normalizarTexto(pedido.getEstado());

		if (ESTADO_CANCELADO.equalsIgnoreCase(estadoPedido)) {
			throw new RuntimeException("No es posible registrar detalles en un pedido cancelado.");
		}

		if (ESTADO_ENTREGADO.equalsIgnoreCase(estadoPedido)) {
			throw new RuntimeException("No es posible registrar detalles en un pedido entregado.");
		}
	}

	private PlatoDominio validarYObtenerPlato(final DetallePedidoDominio datos) {
		var codigoPlato = datos.getPlato().getCodigoPlato().trim();

		var platoEntidad = daoFactory.obtenerPlatoDAO().consultarPorId(codigoPlato);

		if (Objects.isNull(platoEntidad)) {
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

	private void validarNoExisteDetalleConMismoPedidoYPlato(final String codigoPedido, final String codigoPlato) {
		var filtro = DetallePedidoEntidad.builder()
				.codigoPedido(codigoPedido.trim())
				.plato(PlatoEntidad.builder()
						.codigoPlato(codigoPlato.trim())
						.build())
				.build();

		var resultados = daoFactory.obtenerDetallePedidoDAO().consultar(filtro);

		if (Objects.nonNull(resultados) && !resultados.isEmpty()) {
			throw new RuntimeException("Ya existe un detalle registrado para el mismo pedido y el mismo plato.");
		}
	}

	private BigDecimal calcularSubtotal(final Integer cantidad, final BigDecimal precioVenta) {
		return precioVenta.multiply(BigDecimal.valueOf(cantidad));
	}

	private void guardar(final DetallePedidoDominio detalle) {
		var detalleEntidad = DetallePedidoEntidadAssembler.getInstance().ensamblarEntidad(detalle);
		daoFactory.obtenerDetallePedidoDAO().registrar(detalleEntidad);
	}

	private String generarCodigoUnicoDetallePedido() {
		String codigoDetallePedido;
		DetallePedidoEntidad detalleExistente;

		do {
			codigoDetallePedido = generarCodigo("DP");
			detalleExistente = daoFactory.obtenerDetallePedidoDAO().consultarPorId(codigoDetallePedido);
		} while (Objects.nonNull(detalleExistente));

		return codigoDetallePedido;
	}

	private String generarCodigo(final String prefijo) {
		var numero = RANDOM.nextInt(10000);
		return prefijo + String.format("%04d", numero);
	}

	private boolean tieneTexto(final String texto) {
		return Objects.nonNull(texto) && !texto.trim().isEmpty();
	}

	private String normalizarTexto(final String texto) {
		return Objects.toString(texto, "").trim().toUpperCase();
	}
}
