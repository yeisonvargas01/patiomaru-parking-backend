package co.edu.uco.patiomaruparking.transversal.utilitario.excepcion;

public final class DatosPatioMaruExcepcion extends PatioMaruExcepcion {

	private static final long serialVersionUID = 6986927882221236582L;

	private DatosPatioMaruExcepcion(final String mensaje) {
		super(mensaje);
	}

	private DatosPatioMaruExcepcion(final String mensaje, final Throwable excepcionRaiz) {
		super(mensaje, excepcionRaiz);
	}

	private DatosPatioMaruExcepcion(final String mensajeUsuario, final String mensajeTecnico) {
		super(mensajeUsuario, mensajeTecnico);
	}

	private DatosPatioMaruExcepcion(
			final String mensajeUsuario,
			final String mensajeTecnico,
			final Throwable excepcionRaiz) {

		super(mensajeUsuario, mensajeTecnico, excepcionRaiz);
	}

	public static DatosPatioMaruExcepcion crear(final String mensaje) {
		return new DatosPatioMaruExcepcion(mensaje);
	}

	public static DatosPatioMaruExcepcion crear(final String mensaje, final Throwable excepcionRaiz) {
		return new DatosPatioMaruExcepcion(mensaje, excepcionRaiz);
	}

	public static DatosPatioMaruExcepcion crear(
			final String mensajeUsuario,
			final String mensajeTecnico) {

		return new DatosPatioMaruExcepcion(mensajeUsuario, mensajeTecnico);
	}

	public static DatosPatioMaruExcepcion crear(
			final String mensajeUsuario,
			final String mensajeTecnico,
			final Throwable excepcionRaiz) {

		return new DatosPatioMaruExcepcion(mensajeUsuario, mensajeTecnico, excepcionRaiz);
	}
}