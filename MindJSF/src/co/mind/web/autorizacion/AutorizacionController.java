package co.mind.web.autorizacion;

import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;

public class AutorizacionController {

	private boolean maestro;
	private boolean onlyMaestro;
	private boolean cliente;
	private boolean onlyCliente;
	private boolean programador;
	private boolean onlyProgramador;

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

	public boolean isOnlyMaestro() {
		String permiso = (String) MindHelper.obtenerSesion().getAttribute(
				"permiso");
		if (permiso != null) {
			if (permiso
					.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_ADMINISTRADOR)) {
				onlyMaestro = false;
			} else if (permiso
					.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_MAESTRO)) {
				onlyMaestro = true;
			} else if (permiso
					.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_PROGRAMADOR)) {
				onlyMaestro = false;
			}
		}
		return onlyMaestro;
	}

	public void setOnlyMaestro(boolean onlyMaestro) {
		this.onlyMaestro = onlyMaestro;
	}

	public boolean isOnlyCliente() {
		String permiso = (String) MindHelper.obtenerSesion().getAttribute(
				"permiso");
		if (permiso != null) {
			if (permiso
					.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_ADMINISTRADOR)) {
				onlyCliente = true;
			} else if (permiso
					.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_MAESTRO)) {
				onlyCliente = false;
			} else if (permiso
					.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_PROGRAMADOR)) {
				onlyCliente = false;
			}
		}
		return onlyCliente;
	}

	public void setOnlyCliente(boolean onlyCliente) {
		this.onlyCliente = onlyCliente;
	}

	public boolean isOnlyProgramador() {
		String permiso = (String) MindHelper.obtenerSesion().getAttribute(
				"permiso");
		if (permiso != null) {
			if (permiso
					.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_ADMINISTRADOR)) {
				onlyProgramador = false;
			} else if (permiso
					.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_MAESTRO)) {
				onlyProgramador = false;
			} else if (permiso
					.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_PROGRAMADOR)) {
				onlyProgramador = true;
			}
		}
		return onlyProgramador;
	}

	public void setOnlyProgramador(boolean onlyProgramador) {
		this.onlyProgramador = onlyProgramador;
	}

}
