package co.edu.uco.patiomaruparking.dto;

import java.time.LocalDate;

public class EmpleadoDTO {

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
	private CiudadResidenciaDTO ciudadResidencia;
	private TipoDocumentoIdentificacionDTO tipoDocumentoIdentificacion;
	private CargoDTO cargo;

	private EmpleadoDTO(final Builder builder) {
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

	public CiudadResidenciaDTO getCiudadResidencia() {
		return ciudadResidencia;
	}

	private void setCiudadResidencia(final CiudadResidenciaDTO ciudadResidencia) {
		this.ciudadResidencia = ciudadResidencia == null
				? CiudadResidenciaDTO.builder().build()
				: ciudadResidencia;
	}

	public TipoDocumentoIdentificacionDTO getTipoDocumentoIdentificacion() {
		return tipoDocumentoIdentificacion;
	}

	private void setTipoDocumentoIdentificacion(final TipoDocumentoIdentificacionDTO tipoDocumentoIdentificacion) {
		this.tipoDocumentoIdentificacion = tipoDocumentoIdentificacion == null
				? TipoDocumentoIdentificacionDTO.builder().build()
				: tipoDocumentoIdentificacion;
	}

	public CargoDTO getCargo() {
		return cargo;
	}

	private void setCargo(final CargoDTO cargo) {
		this.cargo = cargo == null ? CargoDTO.builder().build() : cargo;
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
		private CiudadResidenciaDTO ciudadResidencia;
		private TipoDocumentoIdentificacionDTO tipoDocumentoIdentificacion;
		private CargoDTO cargo;

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

		public Builder ciudadResidencia(final CiudadResidenciaDTO ciudadResidencia) {
			this.ciudadResidencia = ciudadResidencia == null
					? CiudadResidenciaDTO.builder().build()
					: ciudadResidencia;
			return this;
		}

		public Builder tipoDocumentoIdentificacion(final TipoDocumentoIdentificacionDTO tipoDocumentoIdentificacion) {
			this.tipoDocumentoIdentificacion = tipoDocumentoIdentificacion == null
					? TipoDocumentoIdentificacionDTO.builder().build()
					: tipoDocumentoIdentificacion;
			return this;
		}

		public Builder cargo(final CargoDTO cargo) {
			this.cargo = cargo == null ? CargoDTO.builder().build() : cargo;
			return this;
		}

		public EmpleadoDTO build() {
			return new EmpleadoDTO(this);
		}
	}

	private static String aplicarTrim(final String valor) {
		return valor == null ? "" : valor.trim();
	}
}