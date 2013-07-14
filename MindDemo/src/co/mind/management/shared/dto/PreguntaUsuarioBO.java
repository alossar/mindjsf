package co.mind.management.shared.dto;

import java.io.Serializable;

public class PreguntaUsuarioBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private int identificador;
	private int posicionPreguntaX;
	private int posicionPreguntaY;
	private int caracteresMaximo;
	private int tiempoMaximo;
	private String pregunta;
	private ImagenUsuarioBO imagenesUsuario;
	private PruebaUsuarioBO categoria;

	private int orden;

	public PreguntaUsuarioBO() {
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

	public ImagenUsuarioBO getImagenesUsuario() {
		return this.imagenesUsuario;
	}

	public void setImagenesUsuarioID(ImagenUsuarioBO imagenesUsuario) {
		this.imagenesUsuario = imagenesUsuario;
	}

	public int getPosicionPreguntaX() {
		return posicionPreguntaX;
	}

	public void setPosicionPreguntaX(int posicionPreguntaX) {
		this.posicionPreguntaX = posicionPreguntaX;
	}

	public int getPosicionPreguntaY() {
		return posicionPreguntaY;
	}

	public void setPosicionPreguntaY(int posicionPreguntaY) {
		this.posicionPreguntaY = posicionPreguntaY;
	}

	public PruebaUsuarioBO getPruebaUsuario() {
		return categoria;
	}

	public void setPruebaUsuario(PruebaUsuarioBO categoria) {
		this.categoria = categoria;
	}

	public int getOrden() {
		return this.orden;

	}

	public void setOrden(int orden) {
		this.orden = orden;

	}

}
