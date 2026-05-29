package co.edu.uco.patiomaruparking.negocio.casouso.plato.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.DetallePedidoEntidad;
import co.edu.uco.patiomaruparking.entidad.PlatoEntidad;
import co.edu.uco.patiomaruparking.negocio.casouso.plato.EliminarPlatoCasoUso;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.NegocioPatioMaruExcepcion;
import co.edu.uco.patiomaruparking.transversal.utilitario.excepcion.TransversalPatioMaruExcepcion;

public final class EliminarPlatoCasoUsoImpl implements EliminarPlatoCasoUso {

	private static final Logger logger = LoggerFactory.getLogger(EliminarPlatoCasoUsoImpl.class);

	private static final String PREFIJO_PLATO = "PLT";
	private static final int LONGITUD_CODIGO_PLATO = 6;
	private static final int POSICION_INICIO_DIGITOS_PLATO = 3;

	private final DAOFactory daoFactory;

	public EliminarPlatoCasoUsoImpl(final DAOFactory daoFactory) {
		if (UtilObjeto.esNulo(daoFactory)) {
			throw TransversalPatioMaruExcepcion.crear(
					"No fue posible crear el caso de uso para eliminar plato porque la fábrica de datos es obligatoria.");
		}

		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final String codigoPlato) {
		logger.info("Iniciando la eliminación de un plato.");

		var codigoPlatoNormalizado = validarYNormalizarCodigoPlato(codigoPlato);

		validarExistePlato(codigoPlatoNormalizado);
		validarPlatoNoEstaAsociadoADetallesPedido(codigoPlatoNormalizado);

		daoFactory.obtenerPlatoDAO()
				.eliminar(codigoPlatoNormalizado);

		logger.info("Plato eliminado satisfactoriamente.");
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

	private void validarExistePlato(final String codigoPlato) {
		var platoEntidad = daoFactory.obtenerPlatoDAO()
				.consultarPorId(codigoPlato);

		if (UtilObjeto.esNulo(platoEntidad)
				|| !UtilTexto.tieneTexto(platoEntidad.getCodigoPlato())) {

			throw NegocioPatioMaruExcepcion.crear(
					"No existe un plato registrado con el código indicado.");
		}
	}

	private void validarPlatoNoEstaAsociadoADetallesPedido(final String codigoPlato) {
		var filtro = DetallePedidoEntidad.builder()
				.plato(PlatoEntidad.builder()
						.codigoPlato(codigoPlato)
						.build())
				.build();

		var detallesAsociados = UtilObjeto.obtenerValorDefecto(
				daoFactory.obtenerDetallePedidoDAO().consultar(filtro),
				List.<DetallePedidoEntidad>of());

		if (!detallesAsociados.isEmpty()) {
			throw NegocioPatioMaruExcepcion.crear(
					"No es posible eliminar el plato porque ya se encuentra asociado a uno o más detalles de pedido.");
		}
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