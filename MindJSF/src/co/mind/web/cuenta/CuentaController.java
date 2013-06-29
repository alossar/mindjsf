package co.mind.web.cuenta;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlDataTable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.mind.management.shared.dto.ProcesoUsuarioBO;
import co.mind.management.shared.dto.UsuarioAdministradorBO;
import co.mind.management.shared.dto.UsuarioBO;
import co.mind.management.shared.dto.UsuarioProgramadorBO;
import co.mind.management.shared.persistencia.GestionClientes;
import co.mind.management.shared.persistencia.GestionProcesos;
import co.mind.management.shared.persistencia.GestionUsuariosProgramadores;
import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;

public class CuentaController {

	private UsuarioBO usuario;
	private String nombreUsuario;
	private List<ProcesoUsuarioBO> procesosRevisar;

	private boolean continuar = true;

	private UIForm tableForm;
	private HtmlDataTable dataTable;

	@PostConstruct
	public void init() {
		verificarLogin();
		if (continuar) {
			setNombreUsuario(usuario.getNombres() + " "
					+ usuario.getApellidos());
			GestionProcesos gProcesos = new GestionProcesos();
			setProcesosRevisar(gProcesos.listarProcesosParaRevisar());

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

	public String guardarCuenta() {
		String permiso = (String) MindHelper.obtenerSesion().getAttribute(
				"permiso");
		if (permiso
				.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_ADMINISTRADOR)) {
			GestionClientes gClientes = new GestionClientes();
			gClientes
					.editarUsuarioAdministrador((UsuarioAdministradorBO) usuario);
		} else if (permiso
				.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_PROGRAMADOR)) {
			GestionUsuariosProgramadores gProgramadores = new GestionUsuariosProgramadores();
			gProgramadores.editarUsuarioProgramador(usuario.getIdentificador(),
					(UsuarioProgramadorBO) usuario);

		} else if (permiso
				.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_MAESTRO)) {
			GestionClientes gClientes = new GestionClientes();
			gClientes.editarUsuarioAdministrador(usuario);
		}
		return null;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
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

	public List<ProcesoUsuarioBO> getProcesosRevisar() {
		return procesosRevisar;
	}

	public void setProcesosRevisar(List<ProcesoUsuarioBO> procesosRevisar) {
		this.procesosRevisar = procesosRevisar;
	}

	public UsuarioBO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioBO usuario) {
		this.usuario = usuario;
	}

}
