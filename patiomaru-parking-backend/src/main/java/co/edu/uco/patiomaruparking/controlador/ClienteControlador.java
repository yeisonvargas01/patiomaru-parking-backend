package co.edu.uco.patiomaruparking.controlador;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.patiomaruparking.controlador.respuesta.RespuestaExito;
import co.edu.uco.patiomaruparking.dto.ClienteDTO;
import co.edu.uco.patiomaruparking.negocio.fachada.cliente.ActualizarClienteFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.cliente.ActualizarEstadoClienteFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.cliente.ConsultarClientePorIdFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.cliente.ConsultarClientesFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.cliente.RegistrarClienteFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.cliente.impl.ActualizarClienteFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.cliente.impl.ActualizarEstadoClienteFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.cliente.impl.ConsultarClientePorIdFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.cliente.impl.ConsultarClientesFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.cliente.impl.RegistrarClienteFachadaImpl;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteControlador {

	private static final Logger logger = LoggerFactory.getLogger(ClienteControlador.class);

	@GetMapping("/dummy")
	public ClienteDTO obtenerClienteDummy() {
		logger.debug("Iniciando obtención del cliente dummy.");

		var cliente = ClienteDTO.builder().build();

		logger.debug("Finalizó la obtención del cliente dummy.");

		return cliente;
	}

	@PostMapping
	public ResponseEntity<RespuestaExito<String>> registrarCliente(@RequestBody ClienteDTO cliente) {
		logger.info("Iniciando registro de cliente.");

		RegistrarClienteFachada fachada = new RegistrarClienteFachadaImpl();

		fachada.ejecutar(cliente);

		logger.info("Finalizó exitosamente el registro del cliente.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Se ha registrado de forma exitosa el cliente.", UtilTexto.TEXTO_VACIO),
				HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<RespuestaExito<String>> actualizarCliente(@RequestBody ClienteDTO cliente) {
		logger.info("Iniciando actualización de cliente.");

		ActualizarClienteFachada fachada = new ActualizarClienteFachadaImpl();

		fachada.ejecutar(cliente);

		logger.info("Finalizó exitosamente la actualización del cliente.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Se ha actualizado de forma exitosa la información del cliente.",
						UtilTexto.TEXTO_VACIO),
				HttpStatus.OK);
	}

	@PatchMapping("/estado")
	public ResponseEntity<RespuestaExito<String>> actualizarEstadoCliente(@RequestBody ClienteDTO cliente) {
		logger.info("Iniciando actualización de estado del cliente.");

		ActualizarEstadoClienteFachada fachada = new ActualizarEstadoClienteFachadaImpl();

		fachada.ejecutar(cliente);

		logger.info("Finalizó exitosamente la actualización del estado del cliente.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Se ha actualizado de forma exitosa el estado del cliente.",
						UtilTexto.TEXTO_VACIO),
				HttpStatus.OK);
	}

	@GetMapping("/{codigoCliente}")
	public ResponseEntity<RespuestaExito<ClienteDTO>> consultarClientePorId(
			@PathVariable("codigoCliente") String codigoCliente) {

		logger.info("Iniciando consulta de cliente por identificador.");

		ConsultarClientePorIdFachada fachada = new ConsultarClientePorIdFachadaImpl();

		var cliente = fachada.ejecutar(codigoCliente);

		logger.info("Finalizó exitosamente la consulta de cliente por identificador.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Cliente consultado exitosamente.", cliente),
				HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<RespuestaExito<List<ClienteDTO>>> consultarClientes(
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String codigoCliente,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String nombre,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String telefono,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String correoElectronico,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String estado) {

		logger.info("Iniciando consulta de clientes por filtro.");

		var clienteFiltro = construirFiltroCliente(
				codigoCliente,
				nombre,
				telefono,
				correoElectronico,
				estado);

		ConsultarClientesFachada fachada = new ConsultarClientesFachadaImpl();

		var clientes = fachada.ejecutar(clienteFiltro);

		logger.info("Finalizó exitosamente la consulta de clientes por filtro.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Clientes consultados exitosamente.", clientes),
				HttpStatus.OK);
	}

	private ClienteDTO construirFiltroCliente(
			final String codigoCliente,
			final String nombre,
			final String telefono,
			final String correoElectronico,
			final String estado) {

		var clienteBuilder = ClienteDTO.builder()
				.codigoCliente(UtilTexto.aplicarTrim(codigoCliente))
				.nombre(UtilTexto.aplicarTrim(nombre))
				.telefono(UtilTexto.aplicarTrim(telefono))
				.correoElectronico(UtilTexto.aplicarTrim(correoElectronico));

		if (UtilTexto.tieneTexto(estado)) {
			clienteBuilder.estado(convertirEstado(estado));
		}

		return clienteBuilder.build();
	}

	private Boolean convertirEstado(final String estado) {
		var estadoNormalizado = UtilTexto.aplicarTrimConvertirMayusculas(estado);

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "TRUE")
				|| UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "ACTIVO")) {
			return Boolean.TRUE;
		}

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "FALSE")
				|| UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "INACTIVO")) {
			return Boolean.FALSE;
		}

		throw NegocioPatioMaruExcepcion.crear(
				"El estado del cliente no es válido. Use ACTIVO, INACTIVO, true o false.");
	}
}
