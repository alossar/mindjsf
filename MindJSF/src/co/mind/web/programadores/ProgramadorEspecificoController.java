package co.mind.web.programadores;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlDataTable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.mind.management.shared.dto.ProcesoUsuarioBO;
import co.mind.management.shared.dto.UsuarioBO;
import co.mind.management.shared.dto.UsuarioProgramadorBO;
import co.mind.management.shared.persistencia.GestionProcesos;
import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;

public class ProgramadorEspecificoController {

	private UsuarioBO usuario;
	private String nombreUsuario;
	private UsuarioProgramadorBO programador;
	private String nombreProgramador;
	private List<ProcesoUsuarioBO> procesos;

	private boolean continuar = true;

	private UIForm tableForm;
	private HtmlDataTable dataTable;

	@PostConstruct
	public void init() {
		verificarLogin();
		if (continuar) {
			setNombreUsuario(usuario.getNombres() + " "
					+ usuario.getApellidos());
			programador = obtenerProgramadorDeSesion();
			setNombreProgramador(programador.getNombres() + " "
					+ programador.getApellidos());
			GestionProcesos gProcesos = new GestionProcesos();
			setProcesos(gProcesos.listarProcesoUsuarioAdministrador(programador
					.getIdentificador()));

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

	private UsuarioProgramadorBO obtenerProgramadorDeSesion() {
		HttpServletRequest request = MindHelper.obtenerRequest();
		return (UsuarioProgramadorBO) ((HttpServletRequest) request)
				.getSession().getAttribute("programador");

	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getNombreProgramador() {
		return nombreProgramador;
	}

	public void setNombreProgramador(String nombreProgramador) {
		this.nombreProgramador = nombreProgramador;
	}

	public List<ProcesoUsuarioBO> getProcesos() {
		return procesos;
	}

	public void setProcesos(List<ProcesoUsuarioBO> procesos) {
		this.procesos = procesos;
	}

	public UIForm getTableForm() {
		return tableForm;
	}

	public void setTableForm(UIForm tableForm) {
		this.tableForm = tableForm;
	}

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public UsuarioProgramadorBO getProgramador() {
		return programador;
	}

	public void setProgramador(UsuarioProgramadorBO programador) {
		this.programador = programador;
	}

}
