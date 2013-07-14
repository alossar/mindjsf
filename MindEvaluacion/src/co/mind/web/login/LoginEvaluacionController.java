package co.mind.web.login;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import co.mind.management.shared.dto.EvaluadoBO;
import co.mind.management.shared.dto.ParticipacionEnProcesoBO;
import co.mind.management.shared.persistencia.GestionAccesos;
import co.mind.management.shared.persistencia.GestionEvaluacion;
import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;

public class LoginEvaluacionController {

	private String correo;
	private String codigoAcceso;
	private String correoRecuperar;
	private int cedula;

	static Logger logger = Logger.getLogger(LoginEvaluacionController.class.getName());

	public String autenticar() {
		EvaluadoBO u = new EvaluadoBO();
		u.setCorreoElectronico(correo);
		u.setCedula(cedula);
		ParticipacionEnProcesoBO p = new ParticipacionEnProcesoBO();
		p.setCodigo_Acceso(codigoAcceso);
		GestionAccesos g = new GestionAccesos();
		int result = g.verificarUsuarioBasico(u, p);
		if (result == Convencion.VERIFICACION_USUARIO_BASICO_CORRECTA) {
			obtenerParticipacionEnProceso();
			return "evaluacion";
		} else if (result == Convencion.VERIFICACION_USUARIO_BASICO_SIN_TERMINAR_PRUEBA) {
			obtenerParticipacionEnProceso();
			return "evaluacion";
		} else {
			if (result == Convencion.VERIFICACION_USUARIO_BASICO_CODIGO_ACCESO_NO_VALIDO) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN,
						"El código de acceso no es válido", ""));
			} else if (result == Convencion.VERIFICACION_USUARIO_BASICO_PARTICIPACION_NO_EXISTE) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN,
						"Identificación o código no válido.", ""));
			} else if (result == Convencion.VERIFICACION_USUARIO_BASICO_NO_EXISTE) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN,
						"Identificación o código no válido.", ""));
			} else {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN,
						"Identificación o código no válido.", ""));
			}
		}
		return null;
	}

	private void obtenerParticipacionEnProceso() {
		GestionEvaluacion gEvaluacion = new GestionEvaluacion();
		Object res = gEvaluacion.consultarParticipacionAProceso(cedula, correo,
				codigoAcceso);
		if (res != null) {
			((ParticipacionEnProcesoBO) res).setSesionID(MindHelper
					.obtenerSesion().getId());
			guardarParticipacionEnSesion((ParticipacionEnProcesoBO) res);
		}

	}

	private void guardarParticipacionEnSesion(ParticipacionEnProcesoBO user) {
		HttpSession session = MindHelper.obtenerSesion();
		session.setAttribute(Convencion.CLAVE_EVALUACION, user);
	}

	@PostConstruct
	public void verificarLogin() {

		HttpServletRequest request = MindHelper.obtenerRequest();

		ParticipacionEnProcesoBO user = (ParticipacionEnProcesoBO) ((HttpServletRequest) request)
				.getSession().getAttribute(Convencion.CLAVE_EVALUACION);

		System.out.println("Verificando participacion...");
		if (user != null && user instanceof ParticipacionEnProcesoBO) {
			System.out.println("Verificando sesion...");
			String sid = MindHelper.obtenerSesion().getId();
			if (sid.equalsIgnoreCase(user.getSesionID())) {

				FacesContext facesContext = FacesContext.getCurrentInstance();
				NavigationHandler navigationHandler = facesContext
						.getApplication().getNavigationHandler();
				navigationHandler.handleNavigation(facesContext, null,
						"evaluacion");
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

	public String getCorreoRecuperar() {
		return correoRecuperar;
	}

	public void setCorreoRecuperar(String correoRecuperar) {
		this.correoRecuperar = correoRecuperar;
	}

	public int getCedula() {
		return cedula;
	}

	public void setCedula(int cedula) {
		this.cedula = cedula;
	}

	public String getCodigoAcceso() {
		return codigoAcceso;
	}

	public void setCodigoAcceso(String codigoAcceso) {
		this.codigoAcceso = codigoAcceso;
	}

}
