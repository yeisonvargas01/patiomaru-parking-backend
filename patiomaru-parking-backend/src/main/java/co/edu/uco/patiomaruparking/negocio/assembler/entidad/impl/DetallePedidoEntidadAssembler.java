package co.edu.uco.patiomaruparking.negocio.assembler.entidad.impl;

import co.edu.uco.patiomaruparking.entidad.DetallePedidoEntidad;
import co.edu.uco.patiomaruparking.negocio.assembler.entidad.EntidadAssembler;
import co.edu.uco.patiomaruparking.negocio.dominio.DetallePedidoDominio;

public final class DetallePedidoEntidadAssembler
		implements EntidadAssembler<DetallePedidoDominio, DetallePedidoEntidad> {

	private static DetallePedidoEntidadAssembler INSTANCE = null;

	private DetallePedidoEntidadAssembler() {
		super();
	}

	public static synchronized DetallePedidoEntidadAssembler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DetallePedidoEntidadAssembler();
		}

		return INSTANCE;
	}

	@Override
	public DetallePedidoEntidad ensamblarEntidad(final DetallePedidoDominio dominio) {
		var detallePedidoEnsamblar = dominio == null
				? DetallePedidoDominio.builder().build()
				: dominio;

		return DetallePedidoEntidad.builder()
				.codigoDetallePedido(detallePedidoEnsamblar.getCodigoDetallePedido())
				.cantidad(detallePedidoEnsamblar.getCantidad())
				.subtotal(detallePedidoEnsamblar.getSubtotal())
				.codigoPedido(detallePedidoEnsamblar.getCodigoPedido())
				.plato(PlatoEntidadAssembler.getInstance()
						.ensamblarEntidad(detallePedidoEnsamblar.getPlato()))
				.build();
	}

	@Override
	public DetallePedidoDominio ensamblarDominio(final DetallePedidoEntidad entidad) {
		var detallePedidoEnsamblar = entidad == null
				? DetallePedidoEntidad.builder().build()
				: entidad;

		return DetallePedidoDominio.builder()
				.codigoDetallePedido(detallePedidoEnsamblar.getCodigoDetallePedido())
				.cantidad(detallePedidoEnsamblar.getCantidad())
				.subtotal(detallePedidoEnsamblar.getSubtotal())
				.codigoPedido(detallePedidoEnsamblar.getCodigoPedido())
				.plato(PlatoEntidadAssembler.getInstance()
						.ensamblarDominio(detallePedidoEnsamblar.getPlato()))
				.build();
	}
}
