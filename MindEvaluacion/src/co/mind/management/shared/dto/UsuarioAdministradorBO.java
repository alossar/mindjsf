package co.mind.management.shared.dto;

import java.io.Serializable;
import java.util.List;

public class UsuarioAdministradorBO extends UsuarioBO implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean editar;

	public boolean getEditar() {
		return editar;
	}

	public void setEditar(boolean b) {
		this.editar = b;

	}

}
