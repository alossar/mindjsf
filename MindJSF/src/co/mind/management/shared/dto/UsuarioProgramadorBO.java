package co.mind.management.shared.dto;

import java.io.Serializable;

public class UsuarioProgramadorBO extends UsuarioBO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int usuarioAdministradorID;

	private boolean editar;

	public int getUsuarioAdministradorID() {
		return usuarioAdministradorID;
	}

	public void setUsuarioAdministradorID(int usuarioAdministradorID) {
		this.usuarioAdministradorID = usuarioAdministradorID;
	}
	
	public boolean getEditar(){
		return editar;
	}

	public void setEditar(boolean b) {
		this.editar = b;
	}

}
