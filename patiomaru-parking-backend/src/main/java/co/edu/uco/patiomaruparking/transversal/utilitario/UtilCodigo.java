package co.edu.uco.patiomaruparking.transversal.utilitario;

import java.util.Random;

public final class UtilCodigo {

	private static final Random RANDOM = new Random();
	private static final int LIMITE_NUMERICO = 10000;

	private UtilCodigo() {
		super();
	}

	public static String generarCodigo(final String prefijo) {
		var prefijoSeguro = UtilTexto.aplicarTrimConvertirMayusculas(prefijo);
		var numero = RANDOM.nextInt(LIMITE_NUMERICO);

		return prefijoSeguro + String.format("%04d", numero);
	}
}
