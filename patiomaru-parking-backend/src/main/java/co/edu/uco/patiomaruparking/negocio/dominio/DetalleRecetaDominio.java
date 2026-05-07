package co.edu.uco.patiomaruparking.negocio.dominio;

import java.util.UUID;

import co.edu.uco.patiomaruparking.transversal.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.UtilUUID;

public class DetalleRecetaDominio {

	private UUID id;
	private RecetaDominio receta;
	private InsumoDominio insumo;
	private int cantidadRequerida;
	private String unidadMedida;

	private DetalleRecetaDominio(final Builder builder) {
		setId(builder.id);
		setReceta(builder.receta);
		setInsumo(builder.insumo);
		setCantidadRequerida(builder.cantidadRequerida);
		setUnidadMedida(builder.unidadMedida);
	}

	public UUID getId() {
		return id;
	}

	private void setId(final UUID id) {
		this.id = UtilUUID.obtenerValorDefecto(id);
	}

	public RecetaDominio getReceta() {
		return receta;
	}

	private void setReceta(final RecetaDominio receta) {
		this.receta = UtilObjeto.obtenerValorDefecto(receta, new RecetaDominio.Builder().build());
	}

	public InsumoDominio getInsumo() {
		return insumo;
	}

	private void setInsumo(final InsumoDominio insumo) {
		this.insumo = UtilObjeto.obtenerValorDefecto(insumo, new InsumoDominio.Builder().build());
	}

	public int getCantidadRequerida() {
		return cantidadRequerida;
	}

	private void setCantidadRequerida(final int cantidadRequerida) {
		this.cantidadRequerida = Math.max(0, cantidadRequerida);
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	private void setUnidadMedida(final String unidadMedida) {
		this.unidadMedida = UtilTexto.aplicarTrim(unidadMedida);
	}

	public static class Builder {

		private UUID id;
		private RecetaDominio receta;
		private InsumoDominio insumo;
		private int cantidadRequerida;
		private String unidadMedida;

		public Builder id(final UUID id) {
			this.id = id;
			return this;
		}

		public Builder receta(final RecetaDominio receta) {
			this.receta = receta;
			return this;
		}

		public Builder insumo(final InsumoDominio insumo) {
			this.insumo = insumo;
			return this;
		}

		public Builder cantidadRequerida(final int cantidadRequerida) {
			this.cantidadRequerida = Math.max(0, cantidadRequerida);
			return this;
		}

		public Builder unidadMedida(final String unidadMedida) {
			this.unidadMedida = UtilTexto.aplicarTrim(unidadMedida);
			return this;
		}

		public DetalleRecetaDominio build() {
			return new DetalleRecetaDominio(this);
		}
	}
}
