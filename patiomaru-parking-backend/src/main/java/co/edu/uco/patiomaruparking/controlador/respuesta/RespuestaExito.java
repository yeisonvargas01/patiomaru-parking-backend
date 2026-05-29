package co.edu.uco.patiomaruparking.controlador.respuesta;

import java.time.LocalDateTime;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public record RespuestaExito<T>(String mensaje, LocalDateTime fecha, T datos) {

	public RespuestaExito {
		mensaje = UtilTexto.aplicarTrim(mensaje);
		fecha = UtilObjeto.obtenerValorDefecto(fecha, LocalDateTime.now());
	}

	public static <T> RespuestaExito<T> crear(final String mensaje, final T datos) {
		return new RespuestaExito<>(
				UtilTexto.aplicarTrim(mensaje),
				LocalDateTime.now(),
				datos);
	}

	public static RespuestaExito<String> crear(final String mensaje) {
		return new RespuestaExito<>(
				UtilTexto.aplicarTrim(mensaje),
				LocalDateTime.now(),
				"");
	}
}
