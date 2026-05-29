package co.edu.uco.patiomaruparking.controlador;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.patiomaruparking.controlador.respuesta.RespuestaExito;
import co.edu.uco.patiomaruparking.dto.DetallePedidoDTO;
import co.edu.uco.patiomaruparking.dto.PlatoDTO;
import co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.ActualizarDetallePedidoFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.ConsultarDetallePedidoPorIdFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.ConsultarDetallesPedidoFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.EliminarDetallePedidoFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.RegistrarDetallePedidoFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.impl.ActualizarDetallePedidoFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.impl.ConsultarDetallePedidoPorIdFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.impl.ConsultarDetallesPedidoFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.impl.EliminarDetallePedidoFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.detallepedido.impl.RegistrarDetallePedidoFachadaImpl;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;

@RestController
@RequestMapping("/api/v1/detalles-pedido")
public class DetallePedidoControlador {

	private static final Logger logger = LoggerFactory.getLogger(DetallePedidoControlador.class);

	@GetMapping("/dummy")
	public DetallePedidoDTO obtenerDetallePedidoDummy() {
		logger.debug("Iniciando obtención del detalle de pedido dummy.");

		var detallePedido = DetallePedidoDTO.builder().build();

		logger.debug("Finalizó la obtención del detalle de pedido dummy.");

		return detallePedido;
	}

	@PostMapping
	public ResponseEntity<RespuestaExito<DetallePedidoDTO>> registrarDetallePedido(
			@RequestBody DetallePedidoDTO detallePedido) {

		logger.info("Iniciando registro de detalle de pedido.");

		RegistrarDetallePedidoFachada fachada = new RegistrarDetallePedidoFachadaImpl();

		var detallePedidoRegistrado = fachada.ejecutar(detallePedido);

		logger.info("Finalizó exitosamente el registro del detalle de pedido.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Se ha registrado de forma exitosa el detalle del pedido.",
						detallePedidoRegistrado),
				HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<RespuestaExito<String>> actualizarDetallePedido(
			@RequestBody DetallePedidoDTO detallePedido) {

		logger.info("Iniciando actualización de detalle de pedido.");

		ActualizarDetallePedidoFachada fachada = new ActualizarDetallePedidoFachadaImpl();

		fachada.ejecutar(detallePedido);

		logger.info("Finalizó exitosamente la actualización del detalle de pedido.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Se ha actualizado de forma exitosa la información del detalle del pedido.",
						UtilTexto.TEXTO_VACIO),
				HttpStatus.OK);
	}

	@DeleteMapping("/{codigoDetallePedido}")
	public ResponseEntity<RespuestaExito<String>> eliminarDetallePedido(
			@PathVariable("codigoDetallePedido") String codigoDetallePedido) {

		logger.info("Iniciando eliminación de detalle de pedido.");

		EliminarDetallePedidoFachada fachada = new EliminarDetallePedidoFachadaImpl();

		fachada.ejecutar(codigoDetallePedido);

		logger.info("Finalizó exitosamente la eliminación del detalle de pedido.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Se ha eliminado de forma exitosa el detalle del pedido.",
						UtilTexto.TEXTO_VACIO),
				HttpStatus.OK);
	}

	@GetMapping("/{codigoDetallePedido}")
	public ResponseEntity<RespuestaExito<DetallePedidoDTO>> consultarDetallePedidoPorId(
			@PathVariable("codigoDetallePedido") String codigoDetallePedido) {

		logger.info("Iniciando consulta de detalle de pedido por identificador.");

		ConsultarDetallePedidoPorIdFachada fachada = new ConsultarDetallePedidoPorIdFachadaImpl();

		var detallePedido = fachada.ejecutar(codigoDetallePedido);

		logger.info("Finalizó exitosamente la consulta de detalle de pedido por identificador.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Detalle de pedido consultado exitosamente.", detallePedido),
				HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<RespuestaExito<List<DetallePedidoDTO>>> consultarDetallesPedido(
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String codigoDetallePedido,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String codigoPedido,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String cantidad,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String subtotal,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String codigoPlato) {

		logger.info("Iniciando consulta de detalles de pedido por filtro.");

		var detallePedidoFiltro = construirFiltroDetallePedido(
				codigoDetallePedido,
				codigoPedido,
				cantidad,
				subtotal,
				codigoPlato);

		ConsultarDetallesPedidoFachada fachada = new ConsultarDetallesPedidoFachadaImpl();

		var detallesPedido = fachada.ejecutar(detallePedidoFiltro);

		logger.info("Finalizó exitosamente la consulta de detalles de pedido por filtro.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Detalles de pedido consultados exitosamente.", detallesPedido),
				HttpStatus.OK);
	}

	private DetallePedidoDTO construirFiltroDetallePedido(
			final String codigoDetallePedido,
			final String codigoPedido,
			final String cantidad,
			final String subtotal,
			final String codigoPlato) {

		var detallePedidoBuilder = DetallePedidoDTO.builder()
				.codigoDetallePedido(UtilTexto.aplicarTrim(codigoDetallePedido))
				.codigoPedido(UtilTexto.aplicarTrim(codigoPedido))
				.plato(PlatoDTO.builder()
						.codigoPlato(UtilTexto.aplicarTrim(codigoPlato))
						.build());

		if (UtilTexto.tieneTexto(cantidad)) {
			detallePedidoBuilder.cantidad(convertirCantidad(cantidad));
		}

		if (UtilTexto.tieneTexto(subtotal)) {
			detallePedidoBuilder.subtotal(convertirSubtotal(subtotal));
		}

		return detallePedidoBuilder.build();
	}

	private Integer convertirCantidad(final String cantidad) {
		try {
			return Integer.valueOf(UtilTexto.aplicarTrim(cantidad));
		} catch (NumberFormatException excepcion) {
			throw NegocioPatioMaruExcepcion.crear(
					"La cantidad del detalle del pedido debe ser un número entero válido.",
					"Se recibió una cantidad con formato inválido en DetallePedidoControlador.",
					excepcion);
		}
	}

	private BigDecimal convertirSubtotal(final String subtotal) {
		try {
			return new BigDecimal(UtilTexto.aplicarTrim(subtotal));
		} catch (NumberFormatException excepcion) {
			throw NegocioPatioMaruExcepcion.crear(
					"El subtotal del detalle del pedido debe ser un número válido.",
					"Se recibió un subtotal con formato inválido en DetallePedidoControlador.",
					excepcion);
		}
	}
}