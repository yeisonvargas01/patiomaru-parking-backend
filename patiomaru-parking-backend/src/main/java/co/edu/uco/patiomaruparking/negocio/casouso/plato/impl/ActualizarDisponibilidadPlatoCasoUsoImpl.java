package co.edu.uco.patiomaruparking.negocio.casouso.plato.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.PlatoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.plato.ActualizarDisponibilidadPlatoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.PlatoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class ActualizarDisponibilidadPlatoCasoUsoImpl implements ActualizarDisponibilidadPlatoCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(ActualizarDisponibilidadPlatoCasoUsoImpl.class);

	private static final String PREFIJO_PLATO = "PLT";
	private static final int LONGITUD_CODIGO_PLATO = 6;
	private static final int POSICION_INICIO_DIGITOS_PLATO = 3;

	private final DAOFactory daoFactory;

	public ActualizarDisponibilidadPlatoCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para actualizar disponibilidad del plato porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final PlatoDominio datos) {
		logger.info("Iniciando la actualización de disponibilidad de un plato.");

		var platoActualizar = UtilObjeto.obtenerValorDefecto(
				datos,
				PlatoDominio.builder().build());

		var codigoPlato = validarYNormalizarCodigoPlato(
				platoActualizar.getCodigoPlato());

		var nuevaDisponibilidad = validarYObtenerNuevaDisponibilidad(
				platoActualizar.getEstado());

		var platoActual = validarYObtenerPlato(codigoPlato);

		validarDisponibilidadDiferente(
				platoActual.getEstado(),
				nuevaDisponibilidad);

		daoFactory.obtenerPlatoDAO()
				.actualizarDisponibilidad(codigoPlato, nuevaDisponibilidad);

		logger.info("Disponibilidad del plato actualizada satisfactoriamente.");
	}

	private String validarYNormalizarCodigoPlato(final String codigoPlato) {
		if (!UtilTexto.tieneTexto(codigoPlato)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del plato es obligatorio.");
		}

		var codigoPlatoNormalizado = UtilTexto.aplicarTrimConvertirMayusculas(codigoPlato);

		if (codigoPlatoNormalizado.length() != LONGITUD_CODIGO_PLATO) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del plato debe tener exactamente "
							+ LONGITUD_CODIGO_PLATO + " caracteres.");
		}

		if (!iniciaConPrefijoPlato(codigoPlatoNormalizado)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El código del plato debe iniciar con " + PREFIJO_PLATO + ".");
		}

		if (!contieneSoloDigitos(
				codigoPlatoNormalizado.substring(POSICION_INICIO_DIGITOS_PLATO))) {

			throw NegocioPatioMaruExcepcion.crear(
					"El código del plato debe tener el formato PLT seguido de tres dígitos numéricos.");
		}

		return codigoPlatoNormalizado;
	}

	private Boolean validarYObtenerNuevaDisponibilidad(final Boolean disponibilidad) {
		if (UtilObjeto.esNulo(disponibilidad)) {
			throw NegocioPatioMaruExcepcion.crear(
					"La nueva disponibilidad del plato es obligatoria.");
		}

		return disponibilidad;
	}

	private PlatoDominio validarYObtenerPlato(final String codigoPlato) {
		var platoEntidad = daoFactory.obtenerPlatoDAO()
				.consultarPorId(codigoPlato);

		if (UtilObjeto.esNulo(platoEntidad)
				|| !UtilTexto.tieneTexto(platoEntidad.getCodigoPlato())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe un plato registrado con el código indicado.");
		}

		return PlatoEntidadAssembler.getInstance()
				.ensamblarDominio(platoEntidad);
	}

	private void validarDisponibilidadDiferente(
			final Boolean disponibilidadActual,
			final Boolean nuevaDisponibilidad) {

		if (esMismaDisponibilidad(disponibilidadActual, nuevaDisponibilidad)) {
			throw NegocioPatioMaruExcepcion.crear(
					"El plato ya se encuentra con la disponibilidad indicada.");
		}
	}

	private boolean esMismaDisponibilidad(
			final Boolean disponibilidadActual,
			final Boolean nuevaDisponibilidad) {

		return Boolean.TRUE.equals(disponibilidadActual) && Boolean.TRUE.equals(nuevaDisponibilidad)
				|| Boolean.FALSE.equals(disponibilidadActual) && Boolean.FALSE.equals(nuevaDisponibilidad);
	}

	private boolean iniciaConPrefijoPlato(final String codigoPlato) {
		var prefijo = codigoPlato.substring(0, POSICION_INICIO_DIGITOS_PLATO);

		return UtilTexto.sonIgualesIgnorandoMayusculas(
				prefijo,
				PREFIJO_PLATO);
	}

	private boolean contieneSoloDigitos(final String valor) {
		var valorSeguro = UtilTexto.aplicarTrim(valor);

		for (int indice = 0; indice < valorSeguro.length(); indice++) {
			if (!Character.isDigit(valorSeguro.charAt(indice))) {
				return false;
			}
		}

		return true;
	}
}