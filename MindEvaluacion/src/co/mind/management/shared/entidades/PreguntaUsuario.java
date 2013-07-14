package co.mind.management.shared.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the preguntas_usuarios database table.
 * 
 */
@Entity
@Table(name="preguntas_usuarios")
public class PreguntaUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int identificador;

	@Column(name="CaracteresMaximo")
	private int caracteresMaximo;

	@Column(name="Orden")
	private int orden;

	@Column(name="posicion_pregunta_x")
	private int posicionPreguntaX;

	@Column(name="posicion_pregunta_y")
	private int posicionPreguntaY;

	private String pregunta;

	@Column(name="TiempoMaximo")
	private int tiempoMaximo;

	//bi-directional many-to-one association to ImagenUsuario
	@ManyToOne
	@JoinColumn(name="imagenes_usuarios_identificador")
	private ImagenUsuario imagenesUsuario;

	//bi-directional many-to-one association to PruebaUsuario
	@ManyToOne
	@JoinColumn(name="pruebas_usuarios_identificador")
	private PruebaUsuario pruebasUsuario;

	//bi-directional many-to-one association to Resultado
	@OneToMany(mappedBy="preguntasUsuario")
	private List<Resultado> resultados;

	public PreguntaUsuario() {
	}

	public int getIdentificador() {
		return this.identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	public int getCaracteresMaximo() {
		return this.caracteresMaximo;
	}

	public void setCaracteresMaximo(int caracteresMaximo) {
		this.caracteresMaximo = caracteresMaximo;
	}

	public int getOrden() {
		return this.orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public int getPosicionPreguntaX() {
		return this.posicionPreguntaX;
	}

	public void setPosicionPreguntaX(int posicionPreguntaX) {
		this.posicionPreguntaX = posicionPreguntaX;
	}

	public int getPosicionPreguntaY() {
		return this.posicionPreguntaY;
	}

	public void setPosicionPreguntaY(int posicionPreguntaY) {
		this.posicionPreguntaY = posicionPreguntaY;
	}

	public String getPregunta() {
		return this.pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

	public int getTiempoMaximo() {
		return this.tiempoMaximo;
	}

	public void setTiempoMaximo(int tiempoMaximo) {
		this.tiempoMaximo = tiempoMaximo;
	}

	public ImagenUsuario getImagenesUsuario() {
		return this.imagenesUsuario;
	}

	public void setImagenesUsuario(ImagenUsuario imagenesUsuario) {
		this.imagenesUsuario = imagenesUsuario;
	}

	public PruebaUsuario getPruebasUsuario() {
		return this.pruebasUsuario;
	}

	public void setPruebasUsuario(PruebaUsuario pruebasUsuario) {
		this.pruebasUsuario = pruebasUsuario;
	}

	public List<Resultado> getResultados() {
		return this.resultados;
	}

	public void setResultados(List<Resultado> resultados) {
		this.resultados = resultados;
	}

	public Resultado addResultado(Resultado resultado) {
		getResultados().add(resultado);
		resultado.setPreguntasUsuario(this);

		return resultado;
	}

	public Resultado removeResultado(Resultado resultado) {
		getResultados().remove(resultado);
		resultado.setPreguntasUsuario(null);

		return resultado;
	}

}