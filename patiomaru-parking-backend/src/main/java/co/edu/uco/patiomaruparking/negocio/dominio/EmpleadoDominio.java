package co.edu.uco.patiomaruparking.negocio.dominio;

import java.time.LocalDate;
import java.util.UUID;

import co.edu.uco.patiomaruparking.transversal.UtilObjeto;
import co.edu.uco.patiomaruparking.transversal.UtilTexto;
import co.edu.uco.patiomaruparking.transversal.UtilUUID;

public class EmpleadoDominio {

	private static final LocalDate FECHA_DEFECTO = LocalDate.of(1900, 1, 1);

	private UUID id;
	private TipoDocumentoIdentificacionDominio tipoDocumentoIdentificacion;
	private String numeroIdentificacion;
	private String primerNombre;
	private String segundoNombre;
	private String primerApellido;
	private String segundoApellido;
	private LocalDate fechaNacimiento;
	private int edad;
	private boolean estado;
	private String numeroTelefono;
	private String correoElectronico;
	private String direccionResidencia;
	private CargoDominio cargo;
	private CiudadDominio ciudad;

	private EmpleadoDominio(final Builder builder) {
		setId(builder.id);
		setTipoDocumentoIdentificacion(builder.tipoDocumentoIdentificacion);
		setNumeroIdentificacion(builder.numeroIdentificacion);
		setPrimerNombre(builder.primerNombre);
		setSegundoNombre(builder.segundoNombre);
		setPrimerApellido(builder.primerApellido);
		setSegundoApellido(builder.segundoApellido);
		setFechaNacimiento(builder.fechaNacimiento);
		setEdad(builder.edad);
		setEstado(builder.estado);
		setNumeroTelefono(builder.numeroTelefono);
		setCorreoElectronico(builder.correoElectronico);
		setDireccionResidencia(builder.direccionResidencia);
		setCargo(builder.cargo);
		setCiudad(builder.ciudad);
	}

	public UUID getId() {
		return id;
	}

	private void setId(final UUID id) {
		this.id = UtilUUID.obtenerValorDefecto(id);
	}

	public TipoDocumentoIdentificacionDominio getTipoDocumentoIdentificacion() {
		return tipoDocumentoIdentificacion;
	}

	private void setTipoDocumentoIdentificacion(
			final TipoDocumentoIdentificacionDominio tipoDocumentoIdentificacion) {
		this.tipoDocumentoIdentificacion = UtilObjeto.obtenerValorDefecto(
				tipoDocumentoIdentificacion, new TipoDocumentoIdentificacionDominio.Builder().build());
	}

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	private void setNumeroIdentificacion(final String numeroIdentificacion) {
		this.numeroIdentificacion = UtilTexto.aplicarTrim(numeroIdentificacion);
	}

	public String getPrimerNombre() {
		return primerNombre;
	}

	private void setPrimerNombre(final String primerNombre) {
		this.primerNombre = UtilTexto.aplicarTrim(primerNombre);
	}

	public String getSegundoNombre() {
		return segundoNombre;
	}

	private void setSegundoNombre(final String segundoNombre) {
		this.segundoNombre = UtilTexto.aplicarTrim(segundoNombre);
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	private void setPrimerApellido(final String primerApellido) {
		this.primerApellido = UtilTexto.aplicarTrim(primerApellido);
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	private void setSegundoApellido(final String segundoApellido) {
		this.segundoApellido = UtilTexto.aplicarTrim(segundoApellido);
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	private void setFechaNacimiento(final LocalDate fechaNacimiento) {
		this.fechaNacimiento = UtilObjeto.obtenerValorDefecto(fechaNacimiento, FECHA_DEFECTO);
	}

	public int getEdad() {
		return edad;
	}

	private void setEdad(final int edad) {
		this.edad = edad;
	}

	public boolean isEstado() {
		return estado;
	}

	private void setEstado(final boolean estado) {
		this.estado = estado;
	}

	public String getNumeroTelefono() {
		return numeroTelefono;
	}

	private void setNumeroTelefono(final String numeroTelefono) {
		this.numeroTelefono = UtilTexto.aplicarTrim(numeroTelefono);
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	private void setCorreoElectronico(final String correoElectronico) {
		this.correoElectronico = UtilTexto.aplicarTrim(correoElectronico);
	}

	public String getDireccionResidencia() {
		return direccionResidencia;
	}

	private void setDireccionResidencia(final String direccionResidencia) {
		this.direccionResidencia = UtilTexto.aplicarTrim(direccionResidencia);
	}

	public CargoDominio getCargo() {
		return cargo;
	}

	private void setCargo(final CargoDominio cargo) {
		this.cargo = UtilObjeto.obtenerValorDefecto(cargo, new CargoDominio.Builder().build());
	}

	public CiudadDominio getCiudad() {
		return ciudad;
	}

	private void setCiudad(final CiudadDominio ciudad) {
		this.ciudad = UtilObjeto.obtenerValorDefecto(ciudad, new CiudadDominio.Builder().build());
	}

	public static class Builder {

		private UUID id;
		private TipoDocumentoIdentificacionDominio tipoDocumentoIdentificacion;
		private String numeroIdentificacion;
		private String primerNombre;
		private String segundoNombre;
		private String primerApellido;
		private String segundoApellido;
		private LocalDate fechaNacimiento;
		private int edad;
		private boolean estado;
		private String numeroTelefono;
		private String correoElectronico;
		private String direccionResidencia;
		private CargoDominio cargo;
		private CiudadDominio ciudad;

		public Builder id(final UUID id) {
			this.id = id;
			return this;
		}

		public Builder tipoDocumentoIdentificacion(
				final TipoDocumentoIdentificacionDominio tipoDocumentoIdentificacion) {
			this.tipoDocumentoIdentificacion = tipoDocumentoIdentificacion;
			return this;
		}

		public Builder numeroIdentificacion(final String numeroIdentificacion) {
			this.numeroIdentificacion = UtilTexto.aplicarTrim(numeroIdentificacion);
			return this;
		}

		public Builder primerNombre(final String primerNombre) {
			this.primerNombre = UtilTexto.aplicarTrim(primerNombre);
			return this;
		}

		public Builder segundoNombre(final String segundoNombre) {
			this.segundoNombre = UtilTexto.aplicarTrim(segundoNombre);
			return this;
		}

		public Builder primerApellido(final String primerApellido) {
			this.primerApellido = UtilTexto.aplicarTrim(primerApellido);
			return this;
		}

		public Builder segundoApellido(final String segundoApellido) {
			this.segundoApellido = UtilTexto.aplicarTrim(segundoApellido);
			return this;
		}

		public Builder fechaNacimiento(final LocalDate fechaNacimiento) {
			this.fechaNacimiento = fechaNacimiento;
			return this;
		}

		public Builder edad(final int edad) {
			this.edad = edad;
			return this;
		}

		public Builder estado(final boolean estado) {
			this.estado = estado;
			return this;
		}

		public Builder numeroTelefono(final String numeroTelefono) {
			this.numeroTelefono = UtilTexto.aplicarTrim(numeroTelefono);
			return this;
		}

		public Builder correoElectronico(final String correoElectronico) {
			this.correoElectronico = UtilTexto.aplicarTrim(correoElectronico);
			return this;
		}

		public Builder direccionResidencia(final String direccionResidencia) {
			this.direccionResidencia = UtilTexto.aplicarTrim(direccionResidencia);
			return this;
		}

		public Builder cargo(final CargoDominio cargo) {
			this.cargo = cargo;
			return this;
		}

		public Builder ciudad(final CiudadDominio ciudad) {
			this.ciudad = ciudad;
			return this;
		}

		public EmpleadoDominio build() {
			return new EmpleadoDominio(this);
		}
	}
}
