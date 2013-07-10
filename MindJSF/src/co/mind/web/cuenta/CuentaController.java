package co.mind.web.cuenta;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import co.mind.management.shared.dto.ParticipacionEnProcesoBO;
import co.mind.management.shared.dto.ProcesoUsuarioBO;
import co.mind.management.shared.dto.UsoBO;
import co.mind.management.shared.dto.UsuarioAdministradorBO;
import co.mind.management.shared.dto.UsuarioBO;
import co.mind.management.shared.dto.UsuarioProgramadorBO;
import co.mind.management.shared.persistencia.GestionAccesos;
import co.mind.management.shared.persistencia.GestionClientes;
import co.mind.management.shared.persistencia.GestionEvaluacion;
import co.mind.management.shared.persistencia.GestionProcesos;
import co.mind.management.shared.persistencia.GestionUsos;
import co.mind.management.shared.persistencia.GestionUsuariosProgramadores;
import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;
import co.mind.management.shared.recursos.SMTPSender;

@ManagedBean
@ViewScoped
public class CuentaController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UsuarioBO usuario;
	private String nombreUsuario;
	private List<ProcesoUsuarioBO> procesosRevisar;

	private boolean continuar = true;

	private transient UIForm tableForm;
	private transient HtmlDataTable dataTable;

	private List<UsoBO> usos;

	private transient UIForm tableFormUsos;
	private transient HtmlDataTable dataTableUsos;
	private String permiso;

	private String passActual;
	private String passNuevaRepetir;
	private String passNueva;

	private String mensajeCorreo;

	@PostConstruct
	public void init() {
		verificarLogin();
		if (continuar) {
			setNombreUsuario(usuario.getNombres() + " "
					+ usuario.getApellidos());
			permiso = (String) MindHelper.obtenerSesion().getAttribute(
					"permiso");
			if (permiso != null) {
				if (permiso
						.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_MAESTRO)) {
					GestionProcesos gProcesos = new GestionProcesos();
					setProcesosRevisar(gProcesos.listarProcesosParaRevisar());
				}
			}
			if (permiso != null) {
				if (permiso
						.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_ADMINISTRADOR)) {
					GestionUsos gUsos = new GestionUsos();
					setUsos(gUsos.listarUsos(usuario.getIdentificador()));
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

	public String guardarCuenta() {
		String permiso = (String) MindHelper.obtenerSesion().getAttribute(
				"permiso");
		if (permiso
				.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_ADMINISTRADOR)) {
			GestionClientes gClientes = new GestionClientes();
			int result = gClientes
					.editarUsuarioAdministrador((UsuarioAdministradorBO) usuario);
			if (result == Convencion.CORRECTO) {
				// Mensaje para el feedback
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_INFO,
						"Información modificada correctamente.", ""));
			} else {
				// Mensaje para el feedback
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN,
						"La información no se pudo modificar.", ""));
			}
		} else if (permiso
				.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_PROGRAMADOR)) {
			GestionUsuariosProgramadores gProgramadores = new GestionUsuariosProgramadores();
			int result = gProgramadores.editarUsuarioProgramador(
					usuario.getIdentificador(), (UsuarioProgramadorBO) usuario);
			if (result == Convencion.CORRECTO) {
				// Mensaje para el feedback
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_INFO,
						"Información modificada correctamente.", ""));
			} else {
				// Mensaje para el feedback
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN,
						"La información no se pudo modificar.", ""));
			}

		} else if (permiso
				.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_MAESTRO)) {
			GestionClientes gClientes = new GestionClientes();
			int result = gClientes.editarUsuarioAdministrador(usuario);
			if (result == Convencion.CORRECTO) {
				// Mensaje para el feedback
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_INFO,
						"Información modificada correctamente.", ""));
			} else {
				// Mensaje para el feedback
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN,
						"La información no se pudo modificar.", ""));
			}
		}
		return null;
	}

	public String cambiarContrasena() {
		GestionAccesos gAccesos = new GestionAccesos();
		Object user = gAccesos.verificarTipoUsuario(
				usuario.getCorreo_Electronico(), passActual);
		if (user != null && passNueva.equalsIgnoreCase(passNuevaRepetir)) {
			GestionClientes gClientes = new GestionClientes();
			int result = gClientes.cambiarContrasena(usuario, passNueva);
			// Mensaje para el feedback
			if (result == Convencion.CORRECTO) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_INFO,
						"Contraseña modificada correctamente.", ""));
			} else {
				// Mensaje para el feedback
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN,
						"La contraseña no se pudo modificar.", ""));
			}
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"La contraseña no se pudo modificar.", ""));
		}
		return null;
	}

	public void enviarCorreo(ActionEvent event) {
		int result = SMTPSender.enviarMensajeAMaestro(usuario, mensajeCorreo);
		if (result == Convencion.CORRECTO) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Mensaje enviado.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"El mensaje no pudo ser enviado.", ""));
		}
	}

	public void generarReporteProceso(ActionEvent event) {

		ProcesoUsuarioBO proceso = (ProcesoUsuarioBO) dataTable.getRowData();
		GestionEvaluacion gEvaluacion = new GestionEvaluacion();
		List<ParticipacionEnProcesoBO> participaciones = gEvaluacion
				.listarParticipacionesEnProceso(usuario.getIdentificador(),
						proceso.getIdentificador());
		List<Integer> identificadoresParticipaciones = new ArrayList<Integer>();
		for (ParticipacionEnProcesoBO participacionEnProcesoBO : participaciones) {
			identificadoresParticipaciones.add(participacionEnProcesoBO
					.getIdentificador());
		}
		String url = "ExcelReportServlet";
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ctx = context.getExternalContext();
		HttpSession sess = (HttpSession) ctx.getSession(true);
		sess.setAttribute("participaciones", identificadoresParticipaciones);
		try {
			context.getExternalContext().dispatch(url);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			context.responseComplete();
		}

	}

	public void reporteRevisado(ActionEvent event) {
		ProcesoUsuarioBO proceso = (ProcesoUsuarioBO) dataTable.getRowData();
		proceso.setEstadoValoracion(Convencion.ESTADO_SOLICITUD_REALIZADA);
		GestionProcesos gProcesos = new GestionProcesos();
		int result = gProcesos.editarProcesoUsuarioAdministrador(proceso
				.getUsuario().getIdentificador(), proceso);
		if (result == Convencion.CORRECTO) {
			setProcesosRevisar(gProcesos.listarProcesosParaRevisar());
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO,
					"Revisión del proceso confirmada.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"La revisión del proceso no pudo ser confirmada.", ""));
		}
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

	public List<UsoBO> getUsos() {
		return usos;
	}

	public void setUsos(List<UsoBO> usos) {
		this.usos = usos;
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

	public String getPassActual() {
		return passActual;
	}

	public void setPassActual(String passActual) {
		this.passActual = passActual;
	}

	public String getPassNuevaRepetir() {
		return passNuevaRepetir;
	}

	public void setPassNuevaRepetir(String passNuevaRepetir) {
		this.passNuevaRepetir = passNuevaRepetir;
	}

	public String getPassNueva() {
		return passNueva;
	}

	public void setPassNueva(String passNueva) {
		this.passNueva = passNueva;
	}

	public String getMensajeCorreo() {
		return mensajeCorreo;
	}

	public void setMensajeCorreo(String mensajeCorreo) {
		this.mensajeCorreo = mensajeCorreo;
	}

}
