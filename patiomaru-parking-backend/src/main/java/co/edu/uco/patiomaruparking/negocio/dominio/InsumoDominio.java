package co.edu.uco.patiomaruparking.negocio.dominio;

public class InsumoDominio {

	private String codigoInsumo;
	private String nombre;
	private String unidadMedida;
	private Integer cantidadDisponible;
	private InventarioDominio inventario;

	private InsumoDominio(final Builder builder) {
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

	public InventarioDominio getInventario() {
		return inventario;
	}

	private void setInventario(final InventarioDominio inventario) {
		this.inventario = inventario == null ? InventarioDominio.builder().build() : inventario;
	}

	public boolean tieneCodigo() {
		return !codigoInsumo.isBlank();
	}

	public boolean tieneNombre() {
		return !nombre.isBlank();
	}

	public boolean tieneUnidadMedida() {
		return !unidadMedida.isBlank();
	}

	public boolean tieneInventario() {
		return inventario != null && inventario.tieneCodigo();
	}

	public boolean tieneCantidadDisponibleValida() {
		return cantidadDisponible != null && cantidadDisponible >= 0;
	}

	public boolean hayCantidadSuficiente(final Integer cantidadRequerida) {
		return cantidadRequerida != null
				&& cantidadRequerida > 0
				&& cantidadDisponible != null
				&& cantidadDisponible >= cantidadRequerida;
	}

	public InsumoDominio descontarCantidad(final Integer cantidadADescontar) {
		if (!hayCantidadSuficiente(cantidadADescontar)) {
			return this;
		}

		return InsumoDominio.builder()
				.codigoInsumo(getCodigoInsumo())
				.nombre(getNombre())
				.unidadMedida(getUnidadMedida())
				.cantidadDisponible(getCantidadDisponible() - cantidadADescontar)
				.inventario(getInventario())
				.build();
	}

	public static class Builder {

		private String codigoInsumo;
		private String nombre;
		private String unidadMedida;
		private Integer cantidadDisponible;
		private InventarioDominio inventario;

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

		public Builder inventario(final InventarioDominio inventario) {
			this.inventario = inventario == null ? InventarioDominio.builder().build() : inventario;
			return this;
		}

		public InsumoDominio build() {
			return new InsumoDominio(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}