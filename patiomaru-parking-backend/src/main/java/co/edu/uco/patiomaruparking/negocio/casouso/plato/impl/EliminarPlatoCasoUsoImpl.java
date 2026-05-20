package co.edu.uco.patiomaruparking.negocio.casouso.plato.impl;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.entidad.DetallePedidoEntidad;
import co.edu.uco.patiomaruparking.entidad.PlatoEntidad;
import co.edu.uco.patiomaruparking.negocio.casouso.plato.EliminarPlatoCasoUso;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public final class EliminarPlatoCasoUsoImpl implements EliminarPlatoCasoUso {

	private static final int LONGITUD_CODIGO_PLATO = 6;

	private final DAOFactory daoFactory;

	public EliminarPlatoCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final String codigoPlato) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango.
		validarCodigoPlato(codigoPlato);

		var codigoPlatoNormalizado = UtilTexto.aplicarTrim(codigoPlato);

		// 2. Debe existir el plato que se desea eliminar.
		var platoEntidad = daoFactory.obtenerPlatoDAO().consultarPorId(codigoPlatoNormalizado);

		if (UtilObjeto.esNulo(platoEntidad)) {
			throw new RuntimeException("No existe un plato registrado con el código indicado.");
		}

		// 3. No se debe eliminar un plato que ya esté asociado a detalles de pedido.
		validarPlatoNoEstaAsociadoADetallesPedido(codigoPlatoNormalizado);

		// 4. Eliminar plato.
		daoFactory.obtenerPlatoDAO().eliminar(codigoPlatoNormalizado);
	}

	private void validarCodigoPlato(final String codigoPlato) {
		if (!UtilTexto.tieneTexto(codigoPlato)) {
			throw new RuntimeException("El código del plato es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(codigoPlato).length() != LONGITUD_CODIGO_PLATO) {
			throw new RuntimeException("El código del plato debe tener exactamente "
					+ LONGITUD_CODIGO_PLATO + " caracteres.");
		}
	}

	private void validarPlatoNoEstaAsociadoADetallesPedido(final String codigoPlato) {
		var filtro = DetallePedidoEntidad.builder()
				.plato(PlatoEntidad.builder()
						.codigoPlato(codigoPlato)
						.build())
				.build();

		var detallesAsociados = daoFactory.obtenerDetallePedidoDAO().consultar(filtro);

		if (UtilObjeto.noEsNulo(detallesAsociados) && !detallesAsociados.isEmpty()) {
			throw new RuntimeException(
					"No es posible eliminar el plato porque ya se encuentra asociado a uno o más detalles de pedido.");
		}
	}
}
