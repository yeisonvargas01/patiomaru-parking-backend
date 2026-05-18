package co.edu.uco.patiomaruparking.negocio.dominio;

import java.time.LocalDate;

public class EmpleadoDominio {

	private String codigoEmpleado;
	private String numeroIdentificacion;
	private String primerNombre;
	private String primerApellido;
	private String segundoNombre;
	private String segundoApellido;
	private LocalDate fechaNacimiento;
	private Integer edad;
	private Boolean estado;
	private String numeroTelefono;
	private String correoElectronico;
	private String direccionResidencia;
	private CiudadResidenciaDominio ciudadResidencia;
	private TipoDocumentoIdentificacionDominio tipoDocumentoIdentificacion;
	private CargoDominio cargo;

	private EmpleadoDominio(final Builder builder) {
		setCodigoEmpleado(builder.codigoEmpleado);
		setNumeroIdentificacion(builder.numeroIdentificacion);
		setPrimerNombre(builder.primerNombre);
		setPrimerApellido(builder.primerApellido);
		setSegundoNombre(builder.segundoNombre);
		setSegundoApellido(builder.segundoApellido);
		setFechaNacimiento(builder.fechaNacimiento);
		setEdad(builder.edad);
		setEstado(builder.estado);
		setNumeroTelefono(builder.numeroTelefono);
		setCorreoElectronico(builder.correoElectronico);
		setDireccionResidencia(builder.direccionResidencia);
		setCiudadResidencia(builder.ciudadResidencia);
		setTipoDocumentoIdentificacion(builder.tipoDocumentoIdentificacion);
		setCargo(builder.cargo);
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getCodigoEmpleado() {
		return codigoEmpleado;
	}

	private void setCodigoEmpleado(final String codigoEmpleado) {
		this.codigoEmpleado = aplicarTrim(codigoEmpleado);
	}

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	private void setNumeroIdentificacion(final String numeroIdentificacion) {
		this.numeroIdentificacion = aplicarTrim(numeroIdentificacion);
	}

	public String getPrimerNombre() {
		return primerNombre;
	}

	private void setPrimerNombre(final String primerNombre) {
		this.primerNombre = aplicarTrim(primerNombre);
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	private void setPrimerApellido(final String primerApellido) {
		this.primerApellido = aplicarTrim(primerApellido);
	}

	public String getSegundoNombre() {
		return segundoNombre;
	}

	private void setSegundoNombre(final String segundoNombre) {
		this.segundoNombre = aplicarTrim(segundoNombre);
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	private void setSegundoApellido(final String segundoApellido) {
		this.segundoApellido = aplicarTrim(segundoApellido);
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	private void setFechaNacimiento(final LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Integer getEdad() {
		return edad;
	}

	private void setEdad(final Integer edad) {
		this.edad = edad;
	}

	public Boolean getEstado() {
		return estado;
	}

	private void setEstado(final Boolean estado) {
		this.estado = estado;
	}

	public String getNumeroTelefono() {
		return numeroTelefono;
	}

	private void setNumeroTelefono(final String numeroTelefono) {
		this.numeroTelefono = aplicarTrim(numeroTelefono);
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	private void setCorreoElectronico(final String correoElectronico) {
		this.correoElectronico = aplicarTrim(correoElectronico);
	}

	public String getDireccionResidencia() {
		return direccionResidencia;
	}

	private void setDireccionResidencia(final String direccionResidencia) {
		this.direccionResidencia = aplicarTrim(direccionResidencia);
	}

	public CiudadResidenciaDominio getCiudadResidencia() {
		return ciudadResidencia;
	}

	private void setCiudadResidencia(final CiudadResidenciaDominio ciudadResidencia) {
		this.ciudadResidencia = ciudadResidencia == null
				? CiudadResidenciaDominio.builder().build()
				: ciudadResidencia;
	}

	public TipoDocumentoIdentificacionDominio getTipoDocumentoIdentificacion() {
		return tipoDocumentoIdentificacion;
	}

	private void setTipoDocumentoIdentificacion(final TipoDocumentoIdentificacionDominio tipoDocumentoIdentificacion) {
		this.tipoDocumentoIdentificacion = tipoDocumentoIdentificacion == null
				? TipoDocumentoIdentificacionDominio.builder().build()
				: tipoDocumentoIdentificacion;
	}

	public CargoDominio getCargo() {
		return cargo;
	}

	private void setCargo(final CargoDominio cargo) {
		this.cargo = cargo == null ? CargoDominio.builder().build() : cargo;
	}

	public boolean tieneCodigo() {
		return !codigoEmpleado.isBlank();
	}

	public boolean tieneNumeroIdentificacion() {
		return !numeroIdentificacion.isBlank();
	}

	public boolean tienePrimerNombre() {
		return !primerNombre.isBlank();
	}

	public boolean tienePrimerApellido() {
		return !primerApellido.isBlank();
	}

	public boolean tieneCiudadResidencia() {
		return ciudadResidencia != null && ciudadResidencia.tieneCodigo();
	}

	public boolean tieneTipoDocumentoIdentificacion() {
		return tipoDocumentoIdentificacion != null && tipoDocumentoIdentificacion.tieneCodigo();
	}

	public boolean tieneCargo() {
		return cargo != null && cargo.tieneCodigo();
	}

	public boolean estaActivo() {
		return Boolean.TRUE.equals(estado);
	}

	public String obtenerNombreCompleto() {
		return (primerNombre + " " + segundoNombre + " " + primerApellido + " " + segundoApellido)
				.replaceAll("\\s+", " ")
				.trim();
	}

	public static class Builder {

		private String codigoEmpleado;
		private String numeroIdentificacion;
		private String primerNombre;
		private String primerApellido;
		private String segundoNombre;
		private String segundoApellido;
		private LocalDate fechaNacimiento;
		private Integer edad;
		private Boolean estado;
		private String numeroTelefono;
		private String correoElectronico;
		private String direccionResidencia;
		private CiudadResidenciaDominio ciudadResidencia;
		private TipoDocumentoIdentificacionDominio tipoDocumentoIdentificacion;
		private CargoDominio cargo;

		private Builder() {
			super();
		}

		public Builder codigoEmpleado(final String codigoEmpleado) {
			this.codigoEmpleado = aplicarTrim(codigoEmpleado);
			return this;
		}

		public Builder numeroIdentificacion(final String numeroIdentificacion) {
			this.numeroIdentificacion = aplicarTrim(numeroIdentificacion);
			return this;
		}

		public Builder primerNombre(final String primerNombre) {
			this.primerNombre = aplicarTrim(primerNombre);
			return this;
		}

		public Builder primerApellido(final String primerApellido) {
			this.primerApellido = aplicarTrim(primerApellido);
			return this;
		}

		public Builder segundoNombre(final String segundoNombre) {
			this.segundoNombre = aplicarTrim(segundoNombre);
			return this;
		}

		public Builder segundoApellido(final String segundoApellido) {
			this.segundoApellido = aplicarTrim(segundoApellido);
			return this;
		}

		public Builder fechaNacimiento(final LocalDate fechaNacimiento) {
			this.fechaNacimiento = fechaNacimiento;
			return this;
		}

		public Builder edad(final Integer edad) {
			this.edad = edad;
			return this;
		}

		public Builder estado(final Boolean estado) {
			this.estado = estado;
			return this;
		}

		public Builder numeroTelefono(final String numeroTelefono) {
			this.numeroTelefono = aplicarTrim(numeroTelefono);
			return this;
		}

		public Builder correoElectronico(final String correoElectronico) {
			this.correoElectronico = aplicarTrim(correoElectronico);
			return this;
		}

		public Builder direccionResidencia(final String direccionResidencia) {
			this.direccionResidencia = aplicarTrim(direccionResidencia);
			return this;
		}

		public Builder ciudadResidencia(final CiudadResidenciaDominio ciudadResidencia) {
			this.ciudadResidencia = ciudadResidencia == null
					? CiudadResidenciaDominio.builder().build()
					: ciudadResidencia;
			return this;
		}

		public Builder tipoDocumentoIdentificacion(final TipoDocumentoIdentificacionDominio tipoDocumentoIdentificacion) {
			this.tipoDocumentoIdentificacion = tipoDocumentoIdentificacion == null
					? TipoDocumentoIdentificacionDominio.builder().build()
					: tipoDocumentoIdentificacion;
			return this;
		}

		public Builder cargo(final CargoDominio cargo) {
			this.cargo = cargo == null ? CargoDominio.builder().build() : cargo;
			return this;
		}

		public EmpleadoDominio build() {
			return new EmpleadoDominio(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}