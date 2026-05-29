package co.edu.uco.patiomaruparking.transversal.utilitario.excepcion;

public final class DatosPatioMaruException extends PatioMaruException {

	private static final long serialVersionUID = 1L;

	private DatosPatioMaruException(final String mensajeUsuario, final String mensajeTecnico,
			final Throwable excepcionRaiz) {
		super(mensajeUsuario, mensajeTecnico, excepcionRaiz);
	}

	public static DatosPatioMaruException crear(final String mensajeUsuario, final String mensajeTecnico) {
		return new DatosPatioMaruException(mensajeUsuario, mensajeTecnico, null);
	}

	public static DatosPatioMaruException crear(final String mensajeUsuario, final String mensajeTecnico,
			final Throwable excepcionRaiz) {
		return new DatosPatioMaruException(mensajeUsuario, mensajeTecnico, excepcionRaiz);
	}
	
	public static DatosPatioMaruException(final String mensaje);
	
	
}