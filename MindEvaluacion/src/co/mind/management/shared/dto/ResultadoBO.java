package co.mind.management.shared.dto;

import java.io.Serializable;

public class ResultadoBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private int identificador;

	private String respuesta;
	
	private ParticipacionEnProcesoBO participacionEnProceso;

	private PreguntaUsuarioBO preguntasUsuario;

	public ResultadoBO() {
	}

	public int getIdentificador() {
		return this.identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	public String getRespuesta() {
		return this.respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public ParticipacionEnProcesoBO getParticipacionEnProceso() {
		return this.participacionEnProceso;
	}

	public void setParticipacionEnProceso(ParticipacionEnProcesoBO participacionEnProceso) {
		this.participacionEnProceso = participacionEnProceso;
	}

	public PreguntaUsuarioBO getPreguntasUsuario() {
		return this.preguntasUsuario;
	}

	public void setPreguntasUsuario(PreguntaUsuarioBO preguntasUsuario) {
		this.preguntasUsuario = preguntasUsuario;
	}

}
