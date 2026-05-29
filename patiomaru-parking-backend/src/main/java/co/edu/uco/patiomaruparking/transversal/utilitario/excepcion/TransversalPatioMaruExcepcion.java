package co.edu.uco.patiomaruparking.transversal.utilitario.excepcion;

public final class TransversalPatioMaruExcepcion extends PatioMaruExcepcion {

	private static final long serialVersionUID = 6986927882221236582L;

	private TransversalPatioMaruExcepcion(final String mensaje) {
		super(mensaje);
	}

	private TransversalPatioMaruExcepcion(final String mensaje, final Throwable excepcionRaiz) {
		super(mensaje, excepcionRaiz);
	}

	private TransversalPatioMaruExcepcion(final String mensajeUsuario, final String mensajeTecnico) {
		super(mensajeUsuario, mensajeTecnico);
	}

	private TransversalPatioMaruExcepcion(
			final String mensajeUsuario,
			final String mensajeTecnico,
			final Throwable excepcionRaiz) {

		super(mensajeUsuario, mensajeTecnico, excepcionRaiz);
	}

	public static TransversalPatioMaruExcepcion crear(final String mensaje) {
		return new TransversalPatioMaruExcepcion(mensaje);
	}

	public static TransversalPatioMaruExcepcion crear(final String mensaje, final Throwable excepcionRaiz) {
		return new TransversalPatioMaruExcepcion(mensaje, excepcionRaiz);
	}

	public static TransversalPatioMaruExcepcion crear(
			final String mensajeUsuario,
			final String mensajeTecnico) {

		return new TransversalPatioMaruExcepcion(mensajeUsuario, mensajeTecnico);
	}

	public static TransversalPatioMaruExcepcion crear(
			final String mensajeUsuario,
			final String mensajeTecnico,
			final Throwable excepcionRaiz) {

		return new TransversalPatioMaruExcepcion(mensajeUsuario, mensajeTecnico, excepcionRaiz);
	}
}