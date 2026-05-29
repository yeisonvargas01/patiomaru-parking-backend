package co.edu.uco.patiomaruparking.transversal.utilitario;

public final class UtilCodigo {

    private static final int CANTIDAD_DIGITOS_DEFECTO = 3;
    private static final int NUMERO_MINIMO = 1;

    private UtilCodigo() {
        super();
    }

    public static String generarCodigo(final String prefijo, final int numero) {
        return generarCodigo(prefijo, numero, CANTIDAD_DIGITOS_DEFECTO);
    }

    public static String generarCodigo(final String prefijo, final int numero, final int cantidadDigitos) {
        var prefijoSeguro = UtilTexto.aplicarTrimConvertirMayusculas(prefijo);
        var numeroSeguro = obtenerNumeroSeguro(numero);
        var cantidadDigitosSegura = obtenerCantidadDigitosSegura(cantidadDigitos);

        return prefijoSeguro + String.format("%0" + cantidadDigitosSegura + "d", numeroSeguro);
    }

    private static int obtenerNumeroSeguro(final int numero) {
        return numero < NUMERO_MINIMO ? NUMERO_MINIMO : numero;
    }

    private static int obtenerCantidadDigitosSegura(final int cantidadDigitos) {
        return cantidadDigitos < NUMERO_MINIMO ? CANTIDAD_DIGITOS_DEFECTO : cantidadDigitos;
    }
}
