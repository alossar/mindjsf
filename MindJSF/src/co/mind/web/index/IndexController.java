package co.mind.web.index;

import javax.faces.context.FacesContext;
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
		return "index";
	}

	public String irAIndex() {
		MindHelper.obtenerSesion().removeAttribute("proceso");
		MindHelper.obtenerSesion().removeAttribute("prueba");
		MindHelper.obtenerSesion().removeAttribute("programador");
		MindHelper.obtenerSesion().removeAttribute("evaluado");
		MindHelper.obtenerSesion().removeAttribute("cliente");
		return "index";
	}

	public String irACuenta() {
		MindHelper.obtenerSesion().removeAttribute("proceso");
		MindHelper.obtenerSesion().removeAttribute("prueba");
		MindHelper.obtenerSesion().removeAttribute("programador");
		MindHelper.obtenerSesion().removeAttribute("evaluado");
		MindHelper.obtenerSesion().removeAttribute("cliente");
		return "cuenta";
	}

	public String irAProcesos() {
		MindHelper.obtenerSesion().removeAttribute("proceso");
		MindHelper.obtenerSesion().removeAttribute("prueba");
		MindHelper.obtenerSesion().removeAttribute("programador");
		MindHelper.obtenerSesion().removeAttribute("evaluado");
		MindHelper.obtenerSesion().removeAttribute("cliente");
		return "procesos";
	}

	public String irAPruebas() {
		MindHelper.obtenerSesion().removeAttribute("proceso");
		MindHelper.obtenerSesion().removeAttribute("prueba");
		MindHelper.obtenerSesion().removeAttribute("programador");
		MindHelper.obtenerSesion().removeAttribute("evaluado");
		MindHelper.obtenerSesion().removeAttribute("cliente");
		return "pruebas";
	}

	public String irAEvaluados() {
		MindHelper.obtenerSesion().removeAttribute("proceso");
		MindHelper.obtenerSesion().removeAttribute("prueba");
		MindHelper.obtenerSesion().removeAttribute("programador");
		MindHelper.obtenerSesion().removeAttribute("evaluado");
		MindHelper.obtenerSesion().removeAttribute("cliente");
		return "evaluados";
	}

	public String irAProgramadores() {
		MindHelper.obtenerSesion().removeAttribute("proceso");
		MindHelper.obtenerSesion().removeAttribute("prueba");
		MindHelper.obtenerSesion().removeAttribute("programador");
		MindHelper.obtenerSesion().removeAttribute("evaluado");
		MindHelper.obtenerSesion().removeAttribute("cliente");
		return "programadores";
	}

	public String irAClientes() {
		MindHelper.obtenerSesion().removeAttribute("proceso");
		MindHelper.obtenerSesion().removeAttribute("prueba");
		MindHelper.obtenerSesion().removeAttribute("programador");
		MindHelper.obtenerSesion().removeAttribute("evaluado");
		MindHelper.obtenerSesion().removeAttribute("cliente");
		return "clientes";
	}

}
