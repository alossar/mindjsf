package co.mind.web.login;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import co.mind.management.shared.dto.UsuarioAdministradorBO;
import co.mind.management.shared.dto.UsuarioBO;
import co.mind.management.shared.dto.UsuarioMaestroBO;
import co.mind.management.shared.dto.UsuarioProgramadorBO;
import co.mind.management.shared.persistencia.GestionAccesos;
import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;
import co.mind.management.shared.recursos.SMTPSender;

public class LoginController {

	private String correo;
	private String pass;
	private String correoRecuperar;

	private GestionAccesos gestionAccesos = new GestionAccesos();

	public String autenticar() {
		Object user = gestionAccesos.verificarTipoUsuario(correo, pass);
		if (user != null) {

			String sid = MindHelper.obtenerSesion().getId();
			((UsuarioBO) user).setSesionID(sid);
			guardarUsuarioEnSesion((UsuarioBO) user);

			return "index";
		} else {
			return null;
		}
	}

	private void guardarUsuarioEnSesion(UsuarioBO user) {
		HttpSession session = MindHelper.obtenerSesion();
		if (user instanceof UsuarioAdministradorBO) {
			session.setAttribute("permiso",
					Convencion.VALOR_PERMISOS_USUARIO_ADMINISTRADOR);
		} else if (user instanceof UsuarioProgramadorBO) {
			session.setAttribute("permiso",
					Convencion.VALOR_PERMISOS_USUARIO_PROGRAMADOR);
		} else if (user instanceof UsuarioMaestroBO) {
			session.setAttribute("permiso",
					Convencion.VALOR_PERMISOS_USUARIO_MAESTRO);
		}
		session.setAttribute(Convencion.CLAVE_USUARIO, user);
	}

	public String recuperarContrasena() {
		String con = gestionAccesos.cambiarContrasenaCorreo(correo);
		if (pass != null) {
			SMTPSender.enviarContrasenaAlCorreo(correo, con);
		}
		return "login";
	}

	@PostConstruct
	public void verificarLogin() {

		HttpServletRequest request = MindHelper.obtenerRequest();
		HttpServletResponse response = MindHelper.obtenerResponse();

		UsuarioBO user = (UsuarioBO) ((HttpServletRequest) request)
				.getSession().getAttribute(Convencion.CLAVE_USUARIO);

		System.out.println("Verificando usuario...");
		if (user != null && user instanceof UsuarioBO) {
			System.out.println("Verificando sesion...");
			String sid = MindHelper.obtenerSesion().getId();
			if (sid.equalsIgnoreCase(user.getSesionID())) {
				try {
					System.out.println("Redirigiendo a index...");
					response.sendRedirect("index.do");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public String irALogin() {
		return "login";
	}

	public String irARecuperarContrasena() {
		return "recuperar";
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getCorreoRecuperar() {
		return correoRecuperar;
	}

	public void setCorreoRecuperar(String correoRecuperar) {
		this.correoRecuperar = correoRecuperar;
	}

}
