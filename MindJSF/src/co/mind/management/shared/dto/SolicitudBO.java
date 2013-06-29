package co.mind.management.shared.dto;

import java.io.Serializable;
import java.util.Date;

public class SolicitudBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private int identificador;

	private String apellidos;

	private String cargo;

	private String ciudad;

	private String correo_Electronico;

	private String descripcion;

	private String direccion_Empresa;

	private String direccion_Residencia;

	private String empresa;

	private String estado_Solicitud;

	private Date fecha_Solicitud;

	private String nombres;

	private String pais;

	private String telefono;

	private String telefono_Celular;

	private String tipo;

	public SolicitudBO() {
	}

	public int getIdentificador() {
		return this.identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCargo() {
		return this.cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getCiudad() {
		return this.ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getCorreo_Electronico() {
		return this.correo_Electronico;
	}

	public void setCorreo_Electronico(String correo_Electronico) {
		this.correo_Electronico = correo_Electronico;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDireccion_Empresa() {
		return this.direccion_Empresa;
	}

	public void setDireccion_Empresa(String direccion_Empresa) {
		this.direccion_Empresa = direccion_Empresa;
	}

	public String getDireccion_Residencia() {
		return this.direccion_Residencia;
	}

	public void setDireccion_Residencia(String direccion_Residencia) {
		this.direccion_Residencia = direccion_Residencia;
	}

	public String getEmpresa() {
		return this.empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getEstado_Solicitud() {
		return this.estado_Solicitud;
	}

	public void setEstado_Solicitud(String estado_Solicitud) {
		this.estado_Solicitud = estado_Solicitud;
	}

	public Date getFecha_Solicitud() {
		return this.fecha_Solicitud;
	}

	public void setFecha_Solicitud(Date fecha_Solicitud) {
		this.fecha_Solicitud = fecha_Solicitud;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getPais() {
		return this.pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTelefono_Celular() {
		return this.telefono_Celular;
	}

	public void setTelefono_Celular(String telefono_Celular) {
		this.telefono_Celular = telefono_Celular;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
