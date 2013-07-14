package co.mind.management.shared.dto;

import java.io.Serializable;
import java.util.Date;

public class UsoBO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int identificador;
	public int getIdentificador() {
		return identificador;
	}
	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}
	public Date getFechaAsignacion() {
		return fechaAsignacion;
	}
	public void setFechaAsignacion(Date fechaAsignacion) {
		this.fechaAsignacion = fechaAsignacion;
	}
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	public int getUsosAsignados() {
		return usosAsignados;
	}
	public void setUsosAsignados(int usosAsignados) {
		this.usosAsignados = usosAsignados;
	}
	public int getUsosRedimidos() {
		return usosRedimidos;
	}
	public void setUsosRedimidos(int usosRedimidos) {
		this.usosRedimidos = usosRedimidos;
	}
	private Date fechaAsignacion;
	private Date fechaVencimiento;
	private int usosAsignados;
	private int usosRedimidos;
}
