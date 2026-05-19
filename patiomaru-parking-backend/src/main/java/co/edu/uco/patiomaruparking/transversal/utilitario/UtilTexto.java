package co.edu.uco.patiomaruparking.transversal.utilitario;

public final class UtilTexto {

	public static final String TEXTO_VACIO = "";

	private UtilTexto() {
		super();
	}

	public static boolean esNulo(final String texto) {
		return UtilObjeto.esNulo(texto);
	}

	public static String obtenerValorDefecto(final String texto) {
		return obtenerValorDefecto(texto, TEXTO_VACIO);
	}

	public static String obtenerValorDefecto(final String texto, final String valorDefecto) {
		return UtilObjeto.obtenerValorDefecto(texto, valorDefecto);
	}

	public static String aplicarTrim(final String texto) {
		return obtenerValorDefecto(texto).trim();
	}

	public static String aplicarTrimConvertirMayusculas(final String texto) {
		return aplicarTrim(texto).toUpperCase();
	}
}