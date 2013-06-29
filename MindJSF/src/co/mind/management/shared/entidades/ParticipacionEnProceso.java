package co.mind.management.shared.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the participacion_en_proceso database table.
 * 
 */
@Entity
@Table(name="participacion_en_proceso")
public class ParticipacionEnProceso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int identificador;

	@Column(name="Codigo_Acceso")
	private String codigo_Acceso;

	@Column(name="esta_notificado")
	private String estaNotificado;

	@Column(name="Estado")
	private String estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_finalizacion")
	private Date fechaFinalizacion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_inicio")
	private Date fechaInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_realizacion")
	private Date fechaRealizacion;

	//bi-directional many-to-one association to Evaluado
	@ManyToOne
	@JoinColumn(name="usuarios_basicos_identificador")
	private Evaluado evaluado;

	//bi-directional many-to-one association to ProcesoUsuario
	@ManyToOne
	@JoinColumn(name="procesos_usuarios_identificador")
	private ProcesoUsuario procesosUsuario;

	//bi-directional many-to-one association to Resultado
	@OneToMany(mappedBy="participacionEnProceso")
	private List<Resultado> resultados;

	public ParticipacionEnProceso() {
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

	public String getEstaNotificado() {
		return this.estaNotificado;
	}

	public void setEstaNotificado(String estaNotificado) {
		this.estaNotificado = estaNotificado;
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

	public Date getFechaRealizacion() {
		return this.fechaRealizacion;
	}

	public void setFechaRealizacion(Date fechaRealizacion) {
		this.fechaRealizacion = fechaRealizacion;
	}

	public Evaluado getEvaluado() {
		return this.evaluado;
	}

	public void setEvaluado(Evaluado evaluado) {
		this.evaluado = evaluado;
	}

	public ProcesoUsuario getProcesosUsuario() {
		return this.procesosUsuario;
	}

	public void setProcesosUsuario(ProcesoUsuario procesosUsuario) {
		this.procesosUsuario = procesosUsuario;
	}

	public List<Resultado> getResultados() {
		return this.resultados;
	}

	public void setResultados(List<Resultado> resultados) {
		this.resultados = resultados;
	}

	public Resultado addResultado(Resultado resultado) {
		getResultados().add(resultado);
		resultado.setParticipacionEnProceso(this);

		return resultado;
	}

	public Resultado removeResultado(Resultado resultado) {
		getResultados().remove(resultado);
		resultado.setParticipacionEnProceso(null);

		return resultado;
	}

}