package co.edu.uco.patiomaruparking.transversal.utilitario.excepcion;

public final class TransversalPatioMaruExcepcion extends PatioMaruException {

	private static final long serialVersionUID = 1L;

	private TransversalPatioMaruExcepcion(final String mensajeUsuario, final String mensajeTecnico,
			final Throwable excepcionRaiz) {
		super(mensajeUsuario, mensajeTecnico, excepcionRaiz);
	}

	public static TransversalPatioMaruExcepcion crear(final String mensajeUsuario, final String mensajeTecnico) {
		return new TransversalPatioMaruExcepcion(mensajeUsuario, mensajeTecnico, null);
	}

	public static TransversalPatioMaruExcepcion crear(final String mensajeUsuario, final String mensajeTecnico,
			final Throwable excepcionRaiz) {
		return new TransversalPatioMaruExcepcion(mensajeUsuario, mensajeTecnico, excepcionRaiz);
	}
}