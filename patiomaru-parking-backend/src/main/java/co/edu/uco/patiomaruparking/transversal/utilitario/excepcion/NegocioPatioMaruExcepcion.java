package co.edu.uco.patiomaruparking.transversal.utilitario.excepcion;

public final class NegocioPatioMaruExcepcion extends PatioMaruException {

	private static final long serialVersionUID = 1L;

	private NegocioPatioMaruExcepcion(final String mensajeUsuario, final String mensajeTecnico,
			final Throwable excepcionRaiz) {
		super(mensajeUsuario, mensajeTecnico, excepcionRaiz);
	}

	public static NegocioPatioMaruExcepcion crear(final String mensajeUsuario, final String mensajeTecnico) {
		return new NegocioPatioMaruExcepcion(mensajeUsuario, mensajeTecnico, null);
	}

	public static NegocioPatioMaruExcepcion crear(final String mensajeUsuario, final String mensajeTecnico,
			final Throwable excepcionRaiz) {
		return new NegocioPatioMaruExcepcion(mensajeUsuario, mensajeTecnico, excepcionRaiz);
	}
}