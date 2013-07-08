package co.mind.web.evaluados;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlDataTable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.mind.management.shared.dto.EvaluadoBO;
import co.mind.management.shared.dto.ParticipacionEnProcesoBO;
import co.mind.management.shared.dto.UsuarioBO;
import co.mind.management.shared.persistencia.GestionEvaluacion;
import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;

public class EvaluadoEspecificoController {

	private UsuarioBO usuario;
	private String nombreUsuario;
	private EvaluadoBO evaluado;
	private String nombreEvaluado;
	private List<ParticipacionEnProcesoBO> participaciones;

	private boolean continuar = true;
	private boolean editar;

	private UIForm tableFormUsos;
	private HtmlDataTable dataTableUsos;

	@PostConstruct
	public void init() {
		verificarLogin();
		if (continuar) {
			setNombreUsuario(usuario.getNombres() + " "
					+ usuario.getApellidos());
			evaluado = obtenerEvaluadoDeSesion();
			if (evaluado != null) {
				setNombreEvaluado(evaluado.getNombres() + " "
						+ evaluado.getApellidos());
				GestionEvaluacion gProcesos = new GestionEvaluacion();
				setParticipaciones(gProcesos.listarParticipacionesEvaluado(
						usuario, evaluado));
				setEditar(false);
			} else {
				HttpServletResponse response = MindHelper.obtenerResponse();
				try {
					response.sendRedirect("evaluados.do");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void verificarLogin() {

		HttpServletRequest request = MindHelper.obtenerRequest();
		HttpServletResponse response = MindHelper.obtenerResponse();

		usuario = (UsuarioBO) ((HttpServletRequest) request).getSession()
				.getAttribute(Convencion.CLAVE_USUARIO);

		System.out.println("Verificando usuario...");
		if (usuario == null || !(usuario instanceof UsuarioBO)) {
			System.out.println("Redirigiendo a login...");
			try {
				continuar = false;
				response.sendRedirect("login.do");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			System.out.println("Verificando sesion...");
			String sid = MindHelper.obtenerSesion().getId();
			if (!sid.equalsIgnoreCase(usuario.getSesionID())) {
				System.out.println("Redirigiendo a login...");
				try {
					continuar = false;
					response.sendRedirect("login.do");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		}
	}

	private EvaluadoBO obtenerEvaluadoDeSesion() {
		HttpServletRequest request = MindHelper.obtenerRequest();
		return (EvaluadoBO) ((HttpServletRequest) request).getSession()
				.getAttribute("evaluado");

	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public UIForm getTableFormUsos() {
		return tableFormUsos;
	}

	public void setTableFormUsos(UIForm tableFormUsos) {
		this.tableFormUsos = tableFormUsos;
	}

	public HtmlDataTable getDataTableUsos() {
		return dataTableUsos;
	}

	public void setDataTableUsos(HtmlDataTable dataTableUsos) {
		this.dataTableUsos = dataTableUsos;
	}

	public String getNombreEvaluado() {
		return nombreEvaluado;
	}

	public void setNombreEvaluado(String nombreEvaluado) {
		this.nombreEvaluado = nombreEvaluado;
	}

	public List<ParticipacionEnProcesoBO> getParticipaciones() {
		return participaciones;
	}

	public void setParticipaciones(
			List<ParticipacionEnProcesoBO> participaciones) {
		this.participaciones = participaciones;
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}

	public EvaluadoBO getEvaluado() {
		return evaluado;
	}

	public void setEvaluado(EvaluadoBO evaluado) {
		this.evaluado = evaluado;
	}

}
