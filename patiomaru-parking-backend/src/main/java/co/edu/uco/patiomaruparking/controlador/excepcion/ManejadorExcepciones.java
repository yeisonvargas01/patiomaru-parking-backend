package co.edu.uco.patiomaruparking.controlador.excepcion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.edu.uco.patiomaruparking.controlador.respuesta.RespuestaError;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.PatioMaruExcepcion;

@RestControllerAdvice
public class ManejadorExcepciones {

	private static final Logger logger = LoggerFactory.getLogger(ManejadorExcepciones.class);

	@ExceptionHandler(PatioMaruExcepcion.class)
	public ResponseEntity<RespuestaError> gestionarPatioMaruExcepcion(final PatioMaruExcepcion excepcion) {

		logger.error(excepcion.getMensajeTecnico(), excepcion);

		return new ResponseEntity<>(
				RespuestaError.crear(excepcion.getMensajeUsuario()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<RespuestaError> gestionarExcepcionGenerica(final Exception excepcion) {

		var mensajeUsuario = "Se ha presentado un problema no esperado. Por favor, intente nuevamente y si el problema persiste, contacte al administrador de la aplicación.";
		var mensajeTecnico = "Se ha presentado una excepción no controlada. Por favor verificar el log para más detalles.";

		logger.error(mensajeTecnico, excepcion);

		return new ResponseEntity<>(
				RespuestaError.crear(mensajeUsuario),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
