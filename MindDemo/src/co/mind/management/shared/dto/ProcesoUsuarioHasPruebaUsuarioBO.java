package co.mind.management.shared.dto;

import java.io.Serializable;

public class ProcesoUsuarioHasPruebaUsuarioBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private int identificador;
	private ProcesoUsuarioBO procesosUsuario;
	private PruebaUsuarioBO pruebasUsuario;

	public ProcesoUsuarioHasPruebaUsuarioBO() {
	}

	public int getIdentificador() {
		return this.identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	public ProcesoUsuarioBO getProcesosUsuario() {
		return this.procesosUsuario;
	}

	public void setProcesosUsuario(ProcesoUsuarioBO procesosUsuario) {
		this.procesosUsuario = procesosUsuario;
	}

	public PruebaUsuarioBO getPruebasUsuario() {
		return this.pruebasUsuario;
	}

	public void setPruebasUsuario(PruebaUsuarioBO pruebasUsuario) {
		this.pruebasUsuario = pruebasUsuario;
	}

}