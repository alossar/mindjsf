package co.mind.management.shared.dto;

import java.io.Serializable;

public class ImagenBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private int identificador;

	private String imagenURI;

	public ImagenBO() {
	}

	public int getIdentificador() {
		return this.identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	public String getImagenURI() {
		return this.imagenURI;
	}

	public void setImagenURI(String imagenURI) {
		this.imagenURI = imagenURI;
	}
}
