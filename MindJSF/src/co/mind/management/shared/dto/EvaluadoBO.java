package co.mind.management.shared.dto;

import java.io.Serializable;

public class EvaluadoBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private int identificador;

	private int identificadorUsuarioAdministrador;

	private String apellidos;

	private String correoElectronico;

	private int edad;

	private String nombres;

	private int cedula;

	private boolean editar;

	public EvaluadoBO() {
	}

	public int getIdentificador() {
		return this.identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	public int getIdentificadorUsuarioAdministrador() {
		return this.identificadorUsuarioAdministrador;
	}

	public void setIdentificadorUsuarioAdministrador(
			int identificadorUsuarioAdministrador) {
		this.identificadorUsuarioAdministrador = identificadorUsuarioAdministrador;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCorreoElectronico() {
		return this.correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public int getEdad() {
		return this.edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public int getCedula() {
		return this.cedula;
	}

	public void setCedula(int cedula) {
		this.cedula = cedula;
	}

	public boolean getEditar() {
		return this.editar;
	}

	public void setEditar(boolean b) {
		this.editar = b;

	}

}
