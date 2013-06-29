package co.mind.management.shared.entidades;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the resultados database table.
 * 
 */
@Entity
@Table(name="resultados")
public class Resultado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int identificador;

	private String respuesta;

	//bi-directional many-to-one association to ParticipacionEnProceso
	@ManyToOne
	@JoinColumn(name="participacion_en_proceso_identificador")
	private ParticipacionEnProceso participacionEnProceso;

	//bi-directional many-to-one association to PreguntaUsuario
	@ManyToOne
	@JoinColumn(name="preguntas_identificador")
	private PreguntaUsuario preguntasUsuario;

	public Resultado() {
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

	public ParticipacionEnProceso getParticipacionEnProceso() {
		return this.participacionEnProceso;
	}

	public void setParticipacionEnProceso(ParticipacionEnProceso participacionEnProceso) {
		this.participacionEnProceso = participacionEnProceso;
	}

	public PreguntaUsuario getPreguntasUsuario() {
		return this.preguntasUsuario;
	}

	public void setPreguntasUsuario(PreguntaUsuario preguntasUsuario) {
		this.preguntasUsuario = preguntasUsuario;
	}

}