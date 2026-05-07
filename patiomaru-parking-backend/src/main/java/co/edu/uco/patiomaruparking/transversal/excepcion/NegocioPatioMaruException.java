package co.edu.uco.patiomaruparking.transversal.excepcion;

public final class NegocioPatioMaruException extends PatioMaruException {

	private static final long serialVersionUID = 1L;

	private NegocioPatioMaruException(final String mensajeUsuario, final String mensajeTecnico,
			final Throwable excepcionRaiz) {
		super(mensajeUsuario, mensajeTecnico, excepcionRaiz);
	}

	public static NegocioPatioMaruException crear(final String mensajeUsuario, final String mensajeTecnico) {
		return new NegocioPatioMaruException(mensajeUsuario, mensajeTecnico, null);
	}

	public static NegocioPatioMaruException crear(final String mensajeUsuario, final String mensajeTecnico,
			final Throwable excepcionRaiz) {
		return new NegocioPatioMaruException(mensajeUsuario, mensajeTecnico, excepcionRaiz);
	}
}