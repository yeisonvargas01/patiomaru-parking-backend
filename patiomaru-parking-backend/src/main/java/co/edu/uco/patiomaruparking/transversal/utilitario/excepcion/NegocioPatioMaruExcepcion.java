package co.edu.uco.patiomaruparking.transversal.utilitario.excepcion;

public final class NegocioPatioMaruExcepcion extends PatioMaruExcepcion {

	private static final long serialVersionUID = 6986927882221236582L;

	private NegocioPatioMaruExcepcion(final String mensaje) {
		super(mensaje);
	}

	private NegocioPatioMaruExcepcion(final String mensaje, final Throwable excepcionRaiz) {
		super(mensaje, excepcionRaiz);
	}

	private NegocioPatioMaruExcepcion(final String mensajeUsuario, final String mensajeTecnico) {
		super(mensajeUsuario, mensajeTecnico);
	}

	private NegocioPatioMaruExcepcion(
			final String mensajeUsuario,
			final String mensajeTecnico,
			final Throwable excepcionRaiz) {

		super(mensajeUsuario, mensajeTecnico, excepcionRaiz);
	}

	public static NegocioPatioMaruExcepcion crear(final String mensaje) {
		return new NegocioPatioMaruExcepcion(mensaje);
	}

	public static NegocioPatioMaruExcepcion crear(final String mensaje, final Throwable excepcionRaiz) {
		return new NegocioPatioMaruExcepcion(mensaje, excepcionRaiz);
	}

	public static NegocioPatioMaruExcepcion crear(
			final String mensajeUsuario,
			final String mensajeTecnico) {

		return new NegocioPatioMaruExcepcion(mensajeUsuario, mensajeTecnico);
	}

	public static NegocioPatioMaruExcepcion crear(
			final String mensajeUsuario,
			final String mensajeTecnico,
			final Throwable excepcionRaiz) {

		return new NegocioPatioMaruExcepcion(mensajeUsuario, mensajeTecnico, excepcionRaiz);
	}
}