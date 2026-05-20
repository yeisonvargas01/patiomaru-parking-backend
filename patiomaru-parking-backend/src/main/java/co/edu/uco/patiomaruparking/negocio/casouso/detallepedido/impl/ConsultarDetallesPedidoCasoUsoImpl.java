package co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.impl;

import java.util.List;
import java.util.stream.Collectors;

import co.edu.uco.patiomaruparking.datos.dao.sql.factoria.DAOFactory;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl.DetallePedidoEntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.casouso.detallepedido.ConsultarDetallesPedidoCasoUso;
import co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.utilitario.UtilTexto;

public final class ConsultarDetallesPedidoCasoUsoImpl implements ConsultarDetallesPedidoCasoUso {

	private static final int LONGITUD_CODIGO_PEDIDO = 7;
	private static final int LONGITUD_CODIGO_DETALLE_PEDIDO = 5;

	private final DAOFactory daoFactory;

	public ConsultarDetallesPedidoCasoUsoImpl(final DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public List<DetallePedidoDominio> ejecutar(final DetallePedidoDominio filtro) {

		// 1. Validación de datos consistentes:
		// tipo de dato, longitud, obligatoriedad, formato y rango cuando se envían filtros.
		validarFiltro(filtro);

		// 2. Consultar información de los detalles del pedido según los filtros enviados.
		var filtroSeguro = UtilObjeto.esNulo(filtro)
				? DetallePedidoDominio.builder().build()
				: filtro;

		var filtroEntidad = DetallePedidoEntidadAssembler.getInstance().ensamblarEntidad(filtroSeguro);

		var detallesEntidad = daoFactory.obtenerDetallePedidoDAO().consultar(filtroEntidad);

		if (UtilObjeto.esNulo(detallesEntidad) || detallesEntidad.isEmpty()) {
			return List.of();
		}

		// 3. Ensamblar los resultados de Entidad a Dominio.
		return detallesEntidad.stream()
				.map(DetallePedidoEntidadAssembler.getInstance()::ensamblarDominio)
				.collect(Collectors.toList());
	}

	private void validarFiltro(final DetallePedidoDominio filtro) {
		if (UtilObjeto.esNulo(filtro)) {
			return;
		}

		if (UtilTexto.tieneTexto(filtro.getCodigoDetallePedido())
				&& UtilTexto.aplicarTrim(filtro.getCodigoDetallePedido()).length() != LONGITUD_CODIGO_DETALLE_PEDIDO) {
			throw new RuntimeException("El código del detalle del pedido debe tener exactamente "
					+ LONGITUD_CODIGO_DETALLE_PEDIDO + " caracteres.");
		}

		if (UtilTexto.tieneTexto(filtro.getCodigoPedido())
				&& UtilTexto.aplicarTrim(filtro.getCodigoPedido()).length() != LONGITUD_CODIGO_PEDIDO) {
			throw new RuntimeException("El código del pedido debe tener exactamente "
					+ LONGITUD_CODIGO_PEDIDO + " caracteres.");
		}

		if (UtilObjeto.noEsNulo(filtro.getCantidad()) && filtro.getCantidad() <= 0) {
			throw new RuntimeException("La cantidad del detalle del pedido debe ser mayor que cero.");
		}

		if (UtilObjeto.noEsNulo(filtro.getPlato())
				&& UtilTexto.tieneTexto(filtro.getPlato().getCodigoPlato())) {
			validarLongitud(filtro.getPlato().getCodigoPlato(), 4, 6, "El código del plato");
		}
	}

	private void validarLongitud(final String valor, final int longitudMinima, final int longitudMaxima,
			final String nombreCampo) {

		var valorSeguro = UtilTexto.aplicarTrim(valor);

		if (valorSeguro.length() < longitudMinima || valorSeguro.length() > longitudMaxima) {
			throw new RuntimeException(nombreCampo + " debe tener entre " + longitudMinima + " y "
					+ longitudMaxima + " caracteres.");
		}
	}
}
