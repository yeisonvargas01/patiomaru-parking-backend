package co.edu.uco.patiomaruparking.controlador.respuesta;

import java.time.LocalDateTime;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public record RespuestaError(String mensaje, LocalDateTime fecha) {

	public RespuestaError {
		mensaje = UtilTexto.aplicarTrim(mensaje);
		fecha = UtilObjeto.obtenerValorDefecto(fecha, LocalDateTime.now());
	}
	
	public static RespuestaError crear(final String mensaje) {
		return new RespuestaError(
				UtilTexto.aplicarTrim(mensaje),
				LocalDateTime.now());
	}
}
