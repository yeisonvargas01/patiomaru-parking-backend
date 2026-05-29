package co.edu.uco.patiomaruparking.transversal.utilitario.excepcion;

import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public class PatioMaruExcepcion extends RuntimeException {

	private static final long serialVersionUID = 6986927882221236582L;

	private final String mensajeUsuario;
	private final String mensajeTecnico;

	protected PatioMaruExcepcion(final String mensajeUsuario) {
		this(
				mensajeUsuario,
				mensajeUsuario,
				new RuntimeException(UtilTexto.aplicarTrim(mensajeUsuario))
		);
	}

	protected PatioMaruExcepcion(final String mensajeUsuario, final Throwable excepcionRaiz) {
		this(
				mensajeUsuario,
				mensajeUsuario,
				excepcionRaiz
		);
	}

	protected PatioMaruExcepcion(final String mensajeUsuario, final String mensajeTecnico) {
		this(
				mensajeUsuario,
				mensajeTecnico,
				new RuntimeException(UtilTexto.aplicarTrim(mensajeTecnico))
		);
	}

	protected PatioMaruExcepcion(
			final String mensajeUsuario,
			final String mensajeTecnico,
			final Throwable excepcionRaiz) {

		super(
				UtilTexto.aplicarTrim(mensajeTecnico),
				UtilObjeto.obtenerValorDefecto(
						excepcionRaiz,
						new RuntimeException(UtilTexto.aplicarTrim(mensajeTecnico))
				)
		);

		this.mensajeUsuario = UtilTexto.aplicarTrim(mensajeUsuario);
		this.mensajeTecnico = UtilTexto.aplicarTrim(mensajeTecnico);
	}

	public String getMensajeUsuario() {
		return mensajeUsuario;
	}

	public String getMensajeTecnico() {
		return mensajeTecnico;
	}

	public Throwable getExcepcionRaiz() {
		return getCause();
	}
}