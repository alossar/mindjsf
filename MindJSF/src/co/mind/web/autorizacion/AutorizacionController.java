package co.mind.web.autorizacion;

import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;

public class AutorizacionController {

	private boolean maestro;
	private boolean cliente;
	private boolean programador;

	public boolean isMaestro() {
		String permiso = (String) MindHelper.obtenerSesion().getAttribute(
				"permiso");
		if (permiso != null) {
			if (permiso
					.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_ADMINISTRADOR)) {
				maestro = false;
			} else if (permiso
					.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_MAESTRO)) {
				maestro = true;
			} else if (permiso
					.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_PROGRAMADOR)) {
				maestro = false;
			}
		}
		return maestro;
	}

	public void setMaestro(boolean maestro) {
		this.maestro = maestro;
	}

	public boolean isCliente() {
		String permiso = (String) MindHelper.obtenerSesion().getAttribute(
				"permiso");
		if (permiso != null) {
			if (permiso
					.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_ADMINISTRADOR)) {
				cliente = true;
			} else if (permiso
					.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_MAESTRO)) {
				cliente = true;
			} else if (permiso
					.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_PROGRAMADOR)) {
				cliente = false;
			}
		}
		return cliente;
	}

	public void setCliente(boolean cliente) {
		this.cliente = cliente;
	}

	public boolean isProgramador() {
		String permiso = (String) MindHelper.obtenerSesion().getAttribute(
				"permiso");
		if (permiso != null) {
			if (permiso
					.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_ADMINISTRADOR)) {
				programador = true;
			} else if (permiso
					.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_MAESTRO)) {
				programador = true;
			} else if (permiso
					.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_PROGRAMADOR)) {
				programador = true;
			}
		}
		return programador;
	}

	public void setProgramador(boolean programador) {
		this.programador = programador;
	}

}
