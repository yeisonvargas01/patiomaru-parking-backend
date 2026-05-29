package co.edu.uco.patiomaruparking.controlador;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.patiomaruparking.controlador.respuesta.RespuestaExito;
import co.edu.uco.patiomaruparking.dto.ClienteDTO;
import co.edu.uco.patiomaruparking.dto.EmpleadoDTO;
import co.edu.uco.patiomaruparking.dto.MesaDTO;
import co.edu.uco.patiomaruparking.dto.PedidoDTO;
import co.edu.uco.patiomaruparking.negocio.fachada.pedido.ActualizarEstadoPedidoFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.pedido.CancelarPedidoFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.pedido.ConsultarPedidoPorIdFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.pedido.ConsultarPedidosFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.pedido.RegistrarPedidoFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.pedido.impl.ActualizarEstadoPedidoFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.pedido.impl.CancelarPedidoFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.pedido.impl.ConsultarPedidoPorIdFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.pedido.impl.ConsultarPedidosFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.pedido.impl.RegistrarPedidoFachadaImpl;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoControlador {

	private static final Logger logger = LoggerFactory.getLogger(PedidoControlador.class);

	@GetMapping("/dummy")
	public PedidoDTO obtenerPedidoDummy() {
		logger.debug("Iniciando obtención del pedido dummy.");

		var pedido = PedidoDTO.builder()
				.codigoPedido(UtilTexto.TEXTO_VACIO)
				.fechaRegistro(LocalDate.now())
				.horaRegistro(LocalTime.now().withNano(0))
				.tipoAtencion(UtilTexto.TEXTO_VACIO)
				.estado("Registrado")
				.totalPedido(BigDecimal.ZERO)
				.mesa(MesaDTO.builder().build())
				.cliente(ClienteDTO.builder().build())
				.empleado(EmpleadoDTO.builder()
						.fechaNacimiento(LocalDate.now())
						.build())
				.detalles(List.of())
				.build();

		logger.debug("Finalizó la obtención del pedido dummy.");

		return pedido;
	}

	@PostMapping
	public ResponseEntity<RespuestaExito<PedidoDTO>> registrarPedido(@RequestBody PedidoDTO pedido) {
		logger.info("Iniciando registro de pedido.");

		RegistrarPedidoFachada fachada = new RegistrarPedidoFachadaImpl();

		var pedidoRegistrado = fachada.ejecutar(pedido);

		logger.info("Finalizó exitosamente el registro del pedido.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Se ha registrado de forma exitosa el pedido.", pedidoRegistrado),
				HttpStatus.OK);
	}

	@PatchMapping("/estado")
	public ResponseEntity<RespuestaExito<String>> actualizarEstadoPedido(@RequestBody PedidoDTO pedido) {
		logger.info("Iniciando actualización de estado del pedido.");

		var pedidoActualizar = PedidoDTO.builder()
				.codigoPedido(UtilTexto.aplicarTrim(pedido.getCodigoPedido()))
				.estado(normalizarEstadoPedido(pedido.getEstado()))
				.build();

		ActualizarEstadoPedidoFachada fachada = new ActualizarEstadoPedidoFachadaImpl();

		fachada.ejecutar(pedidoActualizar);

		logger.info("Finalizó exitosamente la actualización del estado del pedido.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Se ha actualizado de forma exitosa el estado del pedido.",
						UtilTexto.TEXTO_VACIO),
				HttpStatus.OK);
	}

	@PatchMapping("/{codigoPedido}/cancelar")
	public ResponseEntity<RespuestaExito<String>> cancelarPedido(
			@PathVariable("codigoPedido") String codigoPedido) {

		logger.info("Iniciando cancelación de pedido.");

		CancelarPedidoFachada fachada = new CancelarPedidoFachadaImpl();

		fachada.ejecutar(codigoPedido);

		logger.info("Finalizó exitosamente la cancelación del pedido.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Se ha cancelado de forma exitosa el pedido.", UtilTexto.TEXTO_VACIO),
				HttpStatus.OK);
	}

	@GetMapping("/{codigoPedido}")
	public ResponseEntity<RespuestaExito<PedidoDTO>> consultarPedidoPorId(
			@PathVariable("codigoPedido") String codigoPedido) {

		logger.info("Iniciando consulta de pedido por identificador.");

		ConsultarPedidoPorIdFachada fachada = new ConsultarPedidoPorIdFachadaImpl();

		var pedido = fachada.ejecutar(codigoPedido);

		logger.info("Finalizó exitosamente la consulta de pedido por identificador.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Pedido consultado exitosamente.", pedido),
				HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<RespuestaExito<List<PedidoDTO>>> consultarPedidos(
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String codigoPedido,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String fechaRegistro,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String horaRegistro,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String tipoAtencion,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String estado,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String totalPedido,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String codigoMesa,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String codigoCliente,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String codigoEmpleado) {

		logger.info("Iniciando consulta de pedidos por filtro.");

		var pedidoFiltro = construirFiltroPedido(
				codigoPedido,
				fechaRegistro,
				horaRegistro,
				tipoAtencion,
				estado,
				totalPedido,
				codigoMesa,
				codigoCliente,
				codigoEmpleado);

		ConsultarPedidosFachada fachada = new ConsultarPedidosFachadaImpl();

		var pedidos = fachada.ejecutar(pedidoFiltro);

		logger.info("Finalizó exitosamente la consulta de pedidos por filtro.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Pedidos consultados exitosamente.", pedidos),
				HttpStatus.OK);
	}

	private PedidoDTO construirFiltroPedido(
			final String codigoPedido,
			final String fechaRegistro,
			final String horaRegistro,
			final String tipoAtencion,
			final String estado,
			final String totalPedido,
			final String codigoMesa,
			final String codigoCliente,
			final String codigoEmpleado) {

		var pedidoBuilder = PedidoDTO.builder()
				.codigoPedido(UtilTexto.aplicarTrim(codigoPedido))
				.tipoAtencion(UtilTexto.aplicarTrim(tipoAtencion))
				.estado(normalizarEstadoPedidoSiFueInformado(estado))
				.mesa(MesaDTO.builder()
						.codigoMesa(UtilTexto.aplicarTrim(codigoMesa))
						.build())
				.cliente(ClienteDTO.builder()
						.codigoCliente(UtilTexto.aplicarTrim(codigoCliente))
						.build())
				.empleado(EmpleadoDTO.builder()
						.codigoEmpleado(UtilTexto.aplicarTrim(codigoEmpleado))
						.build());

		if (UtilTexto.tieneTexto(fechaRegistro)) {
			pedidoBuilder.fechaRegistro(convertirFechaRegistro(fechaRegistro));
		}

		if (UtilTexto.tieneTexto(horaRegistro)) {
			pedidoBuilder.horaRegistro(convertirHoraRegistro(horaRegistro));
		}

		if (UtilTexto.tieneTexto(totalPedido)) {
			pedidoBuilder.totalPedido(convertirTotalPedido(totalPedido));
		}

		return pedidoBuilder.build();
	}

	private LocalDate convertirFechaRegistro(final String fechaRegistro) {
		try {
			return LocalDate.parse(UtilTexto.aplicarTrim(fechaRegistro));
		} catch (DateTimeParseException excepcion) {
			throw NegocioPatioMaruExcepcion.crear(
					"La fecha de registro del pedido no tiene un formato válido. Use el formato AAAA-MM-DD.",
					"Se recibió una fecha de registro con formato inválido en PedidoControlador.",
					excepcion);
		}
	}

	private LocalTime convertirHoraRegistro(final String horaRegistro) {
		try {
			return LocalTime.parse(UtilTexto.aplicarTrim(horaRegistro));
		} catch (DateTimeParseException excepcion) {
			throw NegocioPatioMaruExcepcion.crear(
					"La hora de registro del pedido no tiene un formato válido. Use el formato HH:mm:ss.",
					"Se recibió una hora de registro con formato inválido en PedidoControlador.",
					excepcion);
		}
	}

	private BigDecimal convertirTotalPedido(final String totalPedido) {
		try {
			return new BigDecimal(UtilTexto.aplicarTrim(totalPedido));
		} catch (NumberFormatException excepcion) {
			throw NegocioPatioMaruExcepcion.crear(
					"El total del pedido debe ser un número válido.",
					"Se recibió un total de pedido con formato inválido en PedidoControlador.",
					excepcion);
		}
	}

	private String normalizarEstadoPedidoSiFueInformado(final String estado) {
		if (!UtilTexto.tieneTexto(estado)) {
			return UtilTexto.TEXTO_VACIO;
		}

		return normalizarEstadoPedido(estado);
	}

	private String normalizarEstadoPedido(final String estado) {
		var estadoNormalizado = UtilTexto.aplicarTrimConvertirMayusculas(estado);

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "REGISTRADO")) {
			return "REGISTRADO";
		}

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "PAGADO")) {
			return "PAGADO";
		}

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "ENTREGADO")) {
			return "ENTREGADO";
		}

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "PREPARADO")) {
			return "PREPARADO";
		}

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "EN PREPARACION")
				|| UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "EN PREPARACIÓN")) {
			return "EN PREPARACION";
		}

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "CANCELADO")) {
			return "CANCELADO";
		}

		throw NegocioPatioMaruExcepcion.crear(
				"El estado del pedido no es válido. Use REGISTRADO, PAGADO, EN PREPARACION, PREPARADO, ENTREGADO o CANCELADO.");
	}
}
