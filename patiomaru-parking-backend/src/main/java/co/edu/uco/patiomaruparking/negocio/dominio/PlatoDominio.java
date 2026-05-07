package co.edu.uco.patiomaruparking.negocio.dominio;

import java.math.BigDecimal;
import java.util.UUID;

import co.edu.uco.patiomaruparking.transversal.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.UtilUUID;

public class PlatoDominio {

	private static final BigDecimal PRECIO_VENTA_DEFECTO = BigDecimal.ZERO;

	private UUID id;
	private String nombre;
	private BigDecimal precioVenta;
	private boolean estado;
	private CategoriaDominio categoria;

	private PlatoDominio(final Builder builder) {
		setId(builder.id);
		setNombre(builder.nombre);
		setPrecioVenta(builder.precioVenta);
		setEstado(builder.estado);
		setCategoria(builder.categoria);
	}

	public UUID getId() {
		return id;
	}

	private void setId(final UUID id) {
		this.id = UtilUUID.obtenerValorDefecto(id);
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(final String nombre) {
		this.nombre = UtilTexto.aplicarTrim(nombre);
	}

	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	private void setPrecioVenta(final BigDecimal precioVenta) {
		BigDecimal precioVentaSeguro = UtilObjeto.obtenerValorDefecto(precioVenta, PRECIO_VENTA_DEFECTO);
		this.precioVenta = precioVentaSeguro.compareTo(BigDecimal.ZERO) < 0 ? PRECIO_VENTA_DEFECTO : precioVentaSeguro;
	}

	public boolean isEstado() {
		return estado;
	}

	private void setEstado(final boolean estado) {
		this.estado = estado;
	}

	public CategoriaDominio getCategoria() {
		return categoria;
	}

	private void setCategoria(final CategoriaDominio categoria) {
		this.categoria = UtilObjeto.obtenerValorDefecto(categoria, new CategoriaDominio.Builder().build());
	}

	public static class Builder {

		private UUID id;
		private String nombre;
		private BigDecimal precioVenta;
		private boolean estado;
		private CategoriaDominio categoria;

		public Builder id(final UUID id) {
			this.id = id;
			return this;
		}

		public Builder nombre(final String nombre) {
			this.nombre = UtilTexto.aplicarTrim(nombre);
			return this;
		}

		public Builder precioVenta(final BigDecimal precioVenta) {
			BigDecimal precioVentaSeguro = UtilObjeto.obtenerValorDefecto(precioVenta, PRECIO_VENTA_DEFECTO);
			this.precioVenta = precioVentaSeguro.compareTo(BigDecimal.ZERO) < 0 ? PRECIO_VENTA_DEFECTO : precioVentaSeguro;
			return this;
		}

		public Builder estado(final boolean estado) {
			this.estado = estado;
			return this;
		}

		public Builder categoria(final CategoriaDominio categoria) {
			this.categoria = categoria;
			return this;
		}

		public PlatoDominio build() {
			return new PlatoDominio(this);
		}
	}
}