package co.edu.uco.patiomaruparking.dto;

public class InsumoDTO {

	private String codigoInsumo;
	private String nombre;
	private String unidadMedida;
	private Integer cantidadDisponible;
	private InventarioDTO inventario;

	private InsumoDTO(final Builder builder) {
		setCodigoInsumo(builder.codigoInsumo);
		setNombre(builder.nombre);
		setUnidadMedida(builder.unidadMedida);
		setCantidadDisponible(builder.cantidadDisponible);
		setInventario(builder.inventario);
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getCodigoInsumo() {
		return codigoInsumo;
	}

	private void setCodigoInsumo(final String codigoInsumo) {
		this.codigoInsumo = aplicarTrim(codigoInsumo);
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(final String nombre) {
		this.nombre = aplicarTrim(nombre);
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	private void setUnidadMedida(final String unidadMedida) {
		this.unidadMedida = aplicarTrim(unidadMedida);
	}

	public Integer getCantidadDisponible() {
		return cantidadDisponible;
	}

	private void setCantidadDisponible(final Integer cantidadDisponible) {
		this.cantidadDisponible = cantidadDisponible;
	}

	public InventarioDTO getInventario() {
		return inventario;
	}

	private void setInventario(final InventarioDTO inventario) {
		this.inventario = inventario == null ? InventarioDTO.builder().build() : inventario;
	}

	public static class Builder {

		private String codigoInsumo;
		private String nombre;
		private String unidadMedida;
		private Integer cantidadDisponible;
		private InventarioDTO inventario;

		private Builder() {
			super();
		}

		public Builder codigoInsumo(final String codigoInsumo) {
			this.codigoInsumo = aplicarTrim(codigoInsumo);
			return this;
		}

		public Builder nombre(final String nombre) {
			this.nombre = aplicarTrim(nombre);
			return this;
		}

		public Builder unidadMedida(final String unidadMedida) {
			this.unidadMedida = aplicarTrim(unidadMedida);
			return this;
		}

		public Builder cantidadDisponible(final Integer cantidadDisponible) {
			this.cantidadDisponible = cantidadDisponible;
			return this;
		}

		public Builder inventario(final InventarioDTO inventario) {
			this.inventario = inventario == null ? InventarioDTO.builder().build() : inventario;
			return this;
		}

		public InsumoDTO build() {
			return new InsumoDTO(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}
