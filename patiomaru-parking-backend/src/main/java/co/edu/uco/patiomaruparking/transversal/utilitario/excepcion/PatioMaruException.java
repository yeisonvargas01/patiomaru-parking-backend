package co.edu.uco.patiomaruparking.transversal.utilitario.excepcion;

public abstract class PatioMaruException extends Exception {

	private static final long serialVersionUID = 1L;

	private final String mensajeUsuario;
	private final String mensajeTecnico;

	protected PatioMaruException(final String mensajeUsuario, final String mensajeTecnico,
			final Throwable excepcionRaiz) {
		super(mensajeTecnico, excepcionRaiz);
		this.mensajeUsuario = mensajeUsuario;
		this.mensajeTecnico = mensajeTecnico;
	}

	public String getMensajeUsuario() {
		return mensajeUsuario;
	}

	public String getMensajeTecnico() {
		return mensajeTecnico;
	}
}
