package co.mind.web.procesos;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import co.mind.management.shared.dto.ParticipacionEnProcesoBO;
import co.mind.management.shared.dto.ProcesoUsuarioBO;
import co.mind.management.shared.dto.PruebaUsuarioBO;
import co.mind.management.shared.dto.UsuarioBO;
import co.mind.management.shared.persistencia.GestionProcesos;
import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;

@ManagedBean
@ViewScoped
public class ProcesosController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UsuarioBO usuario;
	private boolean continuar = true;
	private String nombreUsuario;
	private List<ProcesoUsuarioBO> procesos;
	private List<ProcesoUsuarioBO> procesosTemp;
	private ProcesoUsuarioBO proceso;
	private String parametroBusqueda;
	private boolean mostrarMensajeFeedBack;
	private String mensajeFeedBack;

	// JavaServerFaces related variables
	private transient UIForm tableForm;
	private transient HtmlDataTable dataTable;

	private String nombreProcesoCrear;
	private String descripcionProcesoCrear;

	private String nombreProcesoDuplicar;
	private String descripcionProcesoDuplicar;

	@PostConstruct
	public void init() {
		verificarLogin();
		if (continuar) {
			GestionProcesos gProcesos = new GestionProcesos();
			setProcesos(gProcesos.listarProcesoUsuarioAdministrador(usuario
					.getIdentificador()));
			setNombreUsuario(usuario.getNombres() + " "
					+ usuario.getApellidos());
			procesosTemp = procesos;
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

	public void crearProceso(ActionEvent event) {
		ProcesoUsuarioBO proc = new ProcesoUsuarioBO();
		proc.setNombre(nombreProcesoCrear);
		proc.setDescripcion(descripcionProcesoCrear);
		proc.setFechaCreacion(new Date());
		GestionProcesos gProcesos = new GestionProcesos();
		int result = gProcesos.agregarProcesoUsuarioAdministrador(
				usuario.getIdentificador(), proc);
		if (result == Convencion.CORRECTO) {
			setNombreProcesoCrear("");
			setDescripcionProcesoCrear("");
			setProcesos(gProcesos.listarProcesoUsuarioAdministrador(usuario
					.getIdentificador()));
			procesosTemp = procesos;
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Proceso creado.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN, "El proceso no se pudo crear.",
					""));
		}
	}

	public void duplicarProceso(ActionEvent event) {
		GestionProcesos gProcesos = new GestionProcesos();
		HttpServletRequest request = MindHelper.obtenerRequest();
		ProcesoUsuarioBO procesoADuplicar = gProcesos
				.consultarProcesoUsuarioAdministrador(usuario
						.getIdentificador(),
						((ProcesoUsuarioBO) ((HttpServletRequest) request)
								.getSession().getAttribute("procesoDuplicar"))
								.getIdentificador());
		procesoADuplicar.setNombre(nombreProcesoDuplicar);
		procesoADuplicar.setDescripcion(descripcionProcesoDuplicar);
		int result = gProcesos.agregarProcesoConPruebas(
				usuario.getIdentificador(), procesoADuplicar,
				procesoADuplicar.getProcesoUsuarioHasPruebaUsuario());
		if (result == Convencion.CORRECTO) {
			setProcesos(gProcesos.listarProcesoUsuarioAdministrador(usuario
					.getIdentificador()));
			procesosTemp = procesos;
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Proceso duplicado.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"El proceso no se pudo duplicar.", ""));
		}
		HttpSession session = request.getSession();
		session.removeAttribute("procesoDuplicar");
	}

	public String editarProceso() {
		proceso = (ProcesoUsuarioBO) dataTable.getRowData();
		proceso.setEditar(true);
		return nombreUsuario;
	}

	public String cancelarEditarProceso() {
		proceso.setEditar(false);
		return null;
	}

	public String guardarEditarProceso() {
		proceso = (ProcesoUsuarioBO) dataTable.getRowData();
		proceso.setEditar(true);
		GestionProcesos gProcesos = new GestionProcesos();
		int result = gProcesos.editarProcesoUsuarioAdministrador(
				usuario.getIdentificador(), proceso);
		if (result == Convencion.CORRECTO) {
			setProcesos(gProcesos.listarProcesoUsuarioAdministrador(usuario
					.getIdentificador()));
			procesosTemp = procesos;
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Proceso editado.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"El proceso no se pudo editar.", ""));
		}
		return null;
	}

	public void eliminarProceso(ActionEvent event) {

		HttpServletRequest request = MindHelper.obtenerRequest();
		GestionProcesos gProcesos = new GestionProcesos();
		int result = gProcesos.eliminarProcesoUsuarioAdministrador(usuario
				.getIdentificador(),
				((ProcesoUsuarioBO) ((HttpServletRequest) request).getSession()
						.getAttribute("procesoEliminar")).getIdentificador());
		if (result == Convencion.CORRECTO) {
			setProcesos(gProcesos.listarProcesoUsuarioAdministrador(usuario
					.getIdentificador()));
			procesosTemp = procesos;
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Proceso eliminado.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"El proceso no se pudo eliminar.", ""));
		}
		HttpSession session = request.getSession();
		session.removeAttribute("procesoEliminar");
	}

	public String irAProcesoEspecifico() {
		proceso = (ProcesoUsuarioBO) dataTable.getRowData();
		guardarProcesoEnSesion(proceso);
		return "proceso";
	}

	private void guardarProcesoEnSesion(ProcesoUsuarioBO proceso) {
		HttpSession session = MindHelper.obtenerSesion();
		session.setAttribute("proceso", proceso);
	}

	public String buscarProcesos() {
		procesos = procesosTemp;
		if (getParametroBusqueda() != null) {
			if (getParametroBusqueda() != "") {
				List<ProcesoUsuarioBO> resultadoBusqueda = new ArrayList<ProcesoUsuarioBO>();
				for (ProcesoUsuarioBO proceso : getProcesos()) {
					String nombre = proceso.getNombre();
					if (nombre.toLowerCase().contains(
							getParametroBusqueda().toLowerCase())) {
						resultadoBusqueda.add(proceso);
					}
				}
				procesosTemp = procesos;
				procesos = resultadoBusqueda;
			}
		}
		return null;
	}

	public void seleccionarProcesoEliminar(AjaxBehaviorEvent event) {
		HttpSession session = MindHelper.obtenerSesion();
		session.setAttribute("procesoEliminar",
				(ProcesoUsuarioBO) dataTable.getRowData());
	}

	public void seleccionarProcesoDuplicar(AjaxBehaviorEvent event) {
		HttpSession session = MindHelper.obtenerSesion();
		session.setAttribute("procesoDuplicar",
				(ProcesoUsuarioBO) dataTable.getRowData());
	}

	public List<ProcesoUsuarioBO> getProcesos() {
		return procesos;
	}

	public void setProcesos(List<ProcesoUsuarioBO> procesos) {
		this.procesos = procesos;
	}

	public ProcesoUsuarioBO getProceso() {
		return proceso;
	}

	public void setProceso(ProcesoUsuarioBO proceso) {
		this.proceso = proceso;
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

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getParametroBusqueda() {
		return parametroBusqueda;
	}

	public void setParametroBusqueda(String parametroBusqueda) {
		this.parametroBusqueda = parametroBusqueda;
	}

	public String getMensajeFeedBack() {
		return mensajeFeedBack;
	}

	public void setMensajeFeedBack(String mensajeFeedBack) {
		this.mensajeFeedBack = mensajeFeedBack;
	}

	public boolean isMostrarMensajeFeedBack() {
		return mostrarMensajeFeedBack;
	}

	public void setMostrarMensajeFeedBack(boolean mostrarMensajeFeedBack) {
		this.mostrarMensajeFeedBack = mostrarMensajeFeedBack;
	}

	public String getNombreProcesoCrear() {
		return nombreProcesoCrear;
	}

	public void setNombreProcesoCrear(String nombreProcesoCrear) {
		this.nombreProcesoCrear = nombreProcesoCrear;
	}

	public String getDescripcionProcesoCrear() {
		return descripcionProcesoCrear;
	}

	public void setDescripcionProcesoCrear(String descripcionProcesoCrear) {
		this.descripcionProcesoCrear = descripcionProcesoCrear;
	}

	public String getDescripcionProcesoDuplicar() {
		return descripcionProcesoDuplicar;
	}

	public void setDescripcionProcesoDuplicar(String descripcionProcesoDuplicar) {
		this.descripcionProcesoDuplicar = descripcionProcesoDuplicar;
	}

	public String getNombreProcesoDuplicar() {
		return nombreProcesoDuplicar;
	}

	public void setNombreProcesoDuplicar(String nombreProcesoDuplicar) {
		this.nombreProcesoDuplicar = nombreProcesoDuplicar;
	}

}
