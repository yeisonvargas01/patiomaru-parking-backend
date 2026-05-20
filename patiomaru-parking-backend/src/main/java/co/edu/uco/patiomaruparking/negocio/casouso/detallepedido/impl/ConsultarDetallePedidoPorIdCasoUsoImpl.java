package co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.impl;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.DetallePedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.ConsultarDetallePedidoPorIdCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public final class ConsultarDetallePedidoPorIdCasoUsoImpl implements ConsultarDetallePedidoPorIdCasoUso {

	private static final int LONGITUD_CODIGO_DETALLE_PEDIDO = 5;

	private final DAOFactory daoFactory;

	public ConsultarDetallePedidoPorIdCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public DetallePedidoDominio ejecutar(final String codigoDetallePedido) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango.
		validarCodigoDetallePedido(codigoDetallePedido);

		var codigoDetallePedidoNormalizado = UtilTexto.aplicarTrim(codigoDetallePedido);

		// 2. Debe existir un detalle de pedido registrado con el código indicado.
		var detallePedidoEntidad = daoFactory.obtenerDetallePedidoDAO()
				.consultarPorId(codigoDetallePedidoNormalizado);

		if (UtilObjeto.esNulo(detallePedidoEntidad)) {
			throw new RuntimeException("No existe un detalle de pedido registrado con el código indicado.");
		}

		// 3. Ensamblar el detalle del pedido de Entidad a Dominio.
		return DetallePedidoEntidadAssembler.getInstance()
				.ensamblarDominio(detallePedidoEntidad);
	}

	private void validarCodigoDetallePedido(final String codigoDetallePedido) {
		if (!UtilTexto.tieneTexto(codigoDetallePedido)) {
			throw new RuntimeException("El código del detalle del pedido es obligatorio.");
		}

		if (UtilTexto.aplicarTrim(codigoDetallePedido).length() != LONGITUD_CODIGO_DETALLE_PEDIDO) {
			throw new RuntimeException("El código del detalle del pedido debe tener exactamente "
					+ LONGITUD_CODIGO_DETALLE_PEDIDO + " caracteres.");
		}
	}
}
