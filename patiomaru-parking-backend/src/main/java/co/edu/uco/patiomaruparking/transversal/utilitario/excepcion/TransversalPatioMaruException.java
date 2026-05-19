package co.edu.uco.patiomaruparking.transversal.utilitario.excepcion;

public final class TransversalPatioMaruException extends PatioMaruException {

	private static final long serialVersionUID = 1L;

	private TransversalPatioMaruException(final String mensajeUsuario, final String mensajeTecnico,
			final Throwable excepcionRaiz) {
		super(mensajeUsuario, mensajeTecnico, excepcionRaiz);
	}

	public static TransversalPatioMaruException crear(final String mensajeUsuario, final String mensajeTecnico) {
		return new TransversalPatioMaruException(mensajeUsuario, mensajeTecnico, null);
	}

	public static TransversalPatioMaruException crear(final String mensajeUsuario, final String mensajeTecnico,
			final Throwable excepcionRaiz) {
		return new TransversalPatioMaruException(mensajeUsuario, mensajeTecnico, excepcionRaiz);
	}
}