package co.mind.management.shared.dto;

import java.io.Serializable;
import java.util.Date;

import co.mind.management.shared.recursos.Convencion;

public class ParticipacionEnProcesoBO implements Serializable {

	private static final long serialVersionUID = 1L;
	private int identificador;
	private String codigo_Acceso;
	private String estado;
	private Date fechaFinalizacion;
	private Date fechaInicio;
	private int procesoID;
	private EvaluadoBO usuarioBasico;

	private String sesionID;
	private ProcesoUsuarioBO proceso;
	private String estaNotificado;
	private Date fechaRealizacion;

	private boolean notificado;

	public ParticipacionEnProcesoBO() {
	}

	public int getIdentificador() {
		return this.identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	public String getCodigo_Acceso() {
		return this.codigo_Acceso;
	}

	public void setCodigo_Acceso(String codigo_Acceso) {
		this.codigo_Acceso = codigo_Acceso;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaFinalizacion() {
		return this.fechaFinalizacion;
	}

	public void setFechaFinalizacion(Date fechaFinalizacion) {
		this.fechaFinalizacion = fechaFinalizacion;
	}

	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public int getProcesoID() {
		return procesoID;
	}

	public void setProcesoID(int procesoID) {
		this.procesoID = procesoID;
	}

	public EvaluadoBO getUsuarioBasico() {
		return usuarioBasico;
	}

	public void setUsuarioBasico(EvaluadoBO usuarioBasico) {
		this.usuarioBasico = usuarioBasico;
	}

	public void setSesionID(String id) {
		this.sesionID = id;

	}

	public String getSesionID() {
		return sesionID;
	}

	public ProcesoUsuarioBO getProceso() {
		return proceso;
	}

	public void setProceso(ProcesoUsuarioBO proceso) {
		this.proceso = proceso;

	}

	public String getEstaNotificado() {
		return this.estaNotificado;
	}

	public void setEstaNotificado(String estaNotificado) {
		this.estaNotificado = estaNotificado;
	}

	public Date getFechaRealizacion() {
		return this.fechaRealizacion;
	}

	public void setFechaRealizacion(Date fechaRealizacion) {
		this.fechaRealizacion = fechaRealizacion;
	}

	public boolean isNotificado() {
		if (estaNotificado == null) {
			return false;
		} else {
			if (estaNotificado
					.equalsIgnoreCase(Convencion.ESTADO_NOTIFICACION_ENVIADA)) {
				return true;
			} else {
				if (estaNotificado
						.equalsIgnoreCase(Convencion.ESTADO_NOTIFICACION_NO_ENVIADA)) {
					return false;
				}
				return false;
			}
		}
	}

	public void setNotificado(boolean notificado) {
		this.notificado = notificado;
	}
}
