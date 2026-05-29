package co.edu.uco.patiomaruparking.controlador;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import co.edu.uco.patiomaruparking.dto.CategoriaDTO;
import co.edu.uco.patiomaruparking.dto.PlatoDTO;
import co.edu.uco.patiomaruparking.negocio.fachada.plato.ActualizarDisponibilidadPlatoFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.plato.ActualizarPlatoFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.plato.ConsultarPlatoPorIdFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.plato.ConsultarPlatosFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.plato.EliminarPlatoFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.plato.RegistrarPlatoFachada;
import co.edu.uco.patiomaruparking.negocio.fachada.plato.impl.ActualizarDisponibilidadPlatoFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.plato.impl.ActualizarPlatoFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.plato.impl.ConsultarPlatoPorIdFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.plato.impl.ConsultarPlatosFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.plato.impl.EliminarPlatoFachadaImpl;
import co.edu.uco.patiomaruparking.negocio.fachada.plato.impl.RegistrarPlatoFachadaImpl;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;

@RestController
@RequestMapping("/api/v1/platos")
public class PlatoControlador {

	private static final Logger logger = LoggerFactory.getLogger(PlatoControlador.class);

	@GetMapping("/dummy")
	public PlatoDTO obtenerPlatoDummy() {
		logger.debug("Iniciando obtención del plato dummy.");

		var plato = PlatoDTO.builder().build();

		logger.debug("Finalizó la obtención del plato dummy.");

		return plato;
	}

	@PostMapping
	public ResponseEntity<RespuestaExito<String>> registrarPlato(@RequestBody PlatoDTO plato) {
		logger.info("Iniciando registro de plato.");

		RegistrarPlatoFachada fachada = new RegistrarPlatoFachadaImpl();

		fachada.ejecutar(plato);

		logger.info("Finalizó exitosamente el registro del plato.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Se ha registrado de forma exitosa el plato.", UtilTexto.TEXTO_VACIO),
				HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<RespuestaExito<String>> actualizarPlato(@RequestBody PlatoDTO plato) {
		logger.info("Iniciando actualización de plato.");

		ActualizarPlatoFachada fachada = new ActualizarPlatoFachadaImpl();

		fachada.ejecutar(plato);

		logger.info("Finalizó exitosamente la actualización del plato.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Se ha actualizado de forma exitosa la información del plato.",
						UtilTexto.TEXTO_VACIO),
				HttpStatus.OK);
	}

	@PatchMapping("/disponibilidad")
	public ResponseEntity<RespuestaExito<String>> actualizarDisponibilidadPlato(@RequestBody PlatoDTO plato) {
		logger.info("Iniciando actualización de disponibilidad del plato.");

		ActualizarDisponibilidadPlatoFachada fachada = new ActualizarDisponibilidadPlatoFachadaImpl();

		fachada.ejecutar(plato);

		logger.info("Finalizó exitosamente la actualización de disponibilidad del plato.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Se ha actualizado de forma exitosa la disponibilidad del plato.",
						UtilTexto.TEXTO_VACIO),
				HttpStatus.OK);
	}

	@DeleteMapping("/{codigoPlato}")
	public ResponseEntity<RespuestaExito<String>> eliminarPlato(
			@PathVariable("codigoPlato") String codigoPlato) {

		logger.info("Iniciando eliminación de plato.");

		EliminarPlatoFachada fachada = new EliminarPlatoFachadaImpl();

		fachada.ejecutar(codigoPlato);

		logger.info("Finalizó exitosamente la eliminación del plato.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Se ha eliminado de forma exitosa el plato.", UtilTexto.TEXTO_VACIO),
				HttpStatus.OK);
	}

	@GetMapping("/{codigoPlato}")
	public ResponseEntity<RespuestaExito<PlatoDTO>> consultarPlatoPorId(
			@PathVariable("codigoPlato") String codigoPlato) {

		logger.info("Iniciando consulta de plato por identificador.");

		ConsultarPlatoPorIdFachada fachada = new ConsultarPlatoPorIdFachadaImpl();

		var plato = fachada.ejecutar(codigoPlato);

		logger.info("Finalizó exitosamente la consulta de plato por identificador.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Plato consultado exitosamente.", plato),
				HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<RespuestaExito<List<PlatoDTO>>> consultarPlatos(
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String codigoPlato,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String nombre,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String codigoCategoria,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String precioVenta,
			@RequestParam(required = false, defaultValue = UtilTexto.TEXTO_VACIO) String estado) {

		logger.info("Iniciando consulta de platos por filtro.");

		var platoFiltro = construirFiltroPlato(
				codigoPlato,
				nombre,
				codigoCategoria,
				precioVenta,
				estado);

		ConsultarPlatosFachada fachada = new ConsultarPlatosFachadaImpl();

		var platos = fachada.ejecutar(platoFiltro);

		logger.info("Finalizó exitosamente la consulta de platos por filtro.");

		return new ResponseEntity<>(
				RespuestaExito.crear("Platos consultados exitosamente.", platos),
				HttpStatus.OK);
	}

	private PlatoDTO construirFiltroPlato(
			final String codigoPlato,
			final String nombre,
			final String codigoCategoria,
			final String precioVenta,
			final String estado) {

		var platoBuilder = PlatoDTO.builder()
				.codigoPlato(UtilTexto.aplicarTrim(codigoPlato))
				.nombre(UtilTexto.aplicarTrim(nombre))
				.categoria(CategoriaDTO.builder()
						.codigoCategoria(UtilTexto.aplicarTrim(codigoCategoria))
						.build());

		if (UtilTexto.tieneTexto(precioVenta)) {
			platoBuilder.precioVenta(convertirPrecioVenta(precioVenta));
		}

		if (UtilTexto.tieneTexto(estado)) {
			platoBuilder.estado(convertirEstado(estado));
		}

		return platoBuilder.build();
	}

	private BigDecimal convertirPrecioVenta(final String precioVenta) {
		try {
			return new BigDecimal(UtilTexto.aplicarTrim(precioVenta));
		} catch (NumberFormatException excepcion) {
			throw NegocioPatioMaruExcepcion.crear(
					"El precio de venta del plato debe ser un número válido.",
					"Se recibió un precio de venta con formato inválido en PlatoControlador.",
					excepcion);
		}
	}

	private Boolean convertirEstado(final String estado) {
		var estadoNormalizado = UtilTexto.aplicarTrimConvertirMayusculas(estado);

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "TRUE")
				|| UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "ACTIVO")
				|| UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "DISPONIBLE")) {
			return Boolean.TRUE;
		}

		if (UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "FALSE")
				|| UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "INACTIVO")
				|| UtilTexto.sonIgualesIgnorandoMayusculas(estadoNormalizado, "NO DISPONIBLE")) {
			return Boolean.FALSE;
		}

		throw NegocioPatioMaruExcepcion.crear(
				"El estado del plato no es válido. Use DISPONIBLE, NO DISPONIBLE, ACTIVO, INACTIVO, true o false.");
	}
}
