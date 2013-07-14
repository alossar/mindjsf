package co.mind.management.shared.dto;

import java.io.Serializable;

public class ImagenUsuarioBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private int identificador;
	private ImagenBO imagen;
	private int usuarioAdministradorID;

	public ImagenUsuarioBO() {
	}

	public int getIdentificador() {
		return this.identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	public ImagenBO getImagen() {
		return this.imagen;
	}

	public void setImagene(ImagenBO imagen) {
		this.imagen = imagen;
	}

	public int getUsuario() {
		return this.usuarioAdministradorID;
	}

	public void setUsuario(int usuarioAdministradorID) {
		this.usuarioAdministradorID = usuarioAdministradorID;
	}

}
