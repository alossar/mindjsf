package co.mind.web.index;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;

public class IndexController {

	public String cerrarSesion() {
		HttpServletRequest httpServletRequest = MindHelper.obtenerRequest();
		HttpSession session = httpServletRequest.getSession();
		session.removeAttribute(Convencion.CLAVE_USUARIO);
		session.removeAttribute("permiso");
		eliminarAtributos();
		return "index";
	}

	public String irAIndex() {
		eliminarAtributos();
		return "index";
	}

	public String irACuenta() {
		eliminarAtributos();
		return "cuenta";
	}

	public String irAProcesos() {
		eliminarAtributos();
		return "procesos";
	}

	public String irAPruebas() {
		eliminarAtributos();
		return "pruebas";
	}

	public String irAEvaluados() {
		eliminarAtributos();
		return "evaluados";
	}

	public String irAProgramadores() {
		eliminarAtributos();
		return "programadores";
	}

	public String irAClientes() {
		eliminarAtributos();
		return "clientes";
	}

	private void eliminarAtributos() {
		MindHelper.obtenerSesion().removeAttribute("proceso");
		MindHelper.obtenerSesion().removeAttribute("prueba");
		MindHelper.obtenerSesion().removeAttribute("programador");
		MindHelper.obtenerSesion().removeAttribute("evaluado");
		MindHelper.obtenerSesion().removeAttribute("cliente");
	}

}
