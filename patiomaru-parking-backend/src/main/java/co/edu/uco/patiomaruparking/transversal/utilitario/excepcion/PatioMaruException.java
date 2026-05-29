package co.edu.uco.patiomaruparking.transversal.utilitario.excepcion;

public class PatioMaruException extends Exception {

	private static final long serialVersionUID = 1L;

	private final String mensajeUsuario;
	private final String mensajeTecnico;
	private final Throwable excepcionRaiz;

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
	
	public static patioMaruException crear(final string mensajeUsuario, final String mensajeTecnico) {
		return new patioMaruException(mensajeUsuario, mensajeTecnico, null);
	}
	
	public static patioMaruExcepton crear(final string )
	
	
}
