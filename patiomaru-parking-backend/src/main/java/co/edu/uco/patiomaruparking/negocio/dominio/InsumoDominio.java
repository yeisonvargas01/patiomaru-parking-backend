package co.edu.uco.patiomaruparking.negocio.dominio;

import java.util.UUID;

import co.edu.uco.patiomaruparking.transversal.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.UtilUUID;

public class InsumoDominio {

	private UUID id;
	private String nombre;
	private String unidadMedida;
	private int cantidadDisponible;
	private InventarioDominio inventario;

	private InsumoDominio(final Builder builder) {
		setId(builder.id);
		setNombre(builder.nombre);
		setUnidadMedida(builder.unidadMedida);
		setCantidadDisponible(builder.cantidadDisponible);
		setInventario(builder.inventario);
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

	public String getUnidadMedida() {
		return unidadMedida;
	}

	private void setUnidadMedida(final String unidadMedida) {
		this.unidadMedida = UtilTexto.aplicarTrim(unidadMedida);
	}

	public int getCantidadDisponible() {
		return cantidadDisponible;
	}

	private void setCantidadDisponible(final int cantidadDisponible) {
		this.cantidadDisponible = Math.max(0, cantidadDisponible);
	}

	public InventarioDominio getInventario() {
		return inventario;
	}

	private void setInventario(final InventarioDominio inventario) {
		this.inventario = UtilObjeto.obtenerValorDefecto(inventario, new InventarioDominio.Builder().build());
	}

	public static class Builder {

		private UUID id;
		private String nombre;
		private String unidadMedida;
		private int cantidadDisponible;
		private InventarioDominio inventario;

		public Builder id(final UUID id) {
			this.id = id;
			return this;
		}

		public Builder nombre(final String nombre) {
			this.nombre = UtilTexto.aplicarTrim(nombre);
			return this;
		}

		public Builder unidadMedida(final String unidadMedida) {
			this.unidadMedida = UtilTexto.aplicarTrim(unidadMedida);
			return this;
		}

		public Builder cantidadDisponible(final int cantidadDisponible) {
			this.cantidadDisponible = Math.max(0, cantidadDisponible);
			return this;
		}

		public Builder inventario(final InventarioDominio inventario) {
			this.inventario = inventario;
			return this;
		}

		public InsumoDominio build() {
			return new InsumoDominio(this);
		}
	}
}
