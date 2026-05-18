package co.edu.uco.patiomaruparking.transversal.utilitario.excepcion;

import java.util.UUID;

public final class UtilUUID {

	private static final UUID UUID_DEFECTO = new UUID(0L, 0L);

	private UtilUUID() {
		super();
	}

	public static UUID obtenerValorDefecto() {
		return UUID_DEFECTO;
	}

	public static UUID obtenerValorDefecto(final UUID valor) {
		return UtilObjeto.obtenerValorDefecto(valor, UUID_DEFECTO);
	}

	public static boolean esValorDefecto(final UUID valor) {
		return UUID_DEFECTO.equals(obtenerValorDefecto(valor));
	}
}