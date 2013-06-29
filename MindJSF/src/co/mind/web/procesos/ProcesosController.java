package co.mind.web.procesos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import co.mind.management.shared.dto.ProcesoUsuarioBO;
import co.mind.management.shared.dto.UsuarioBO;
import co.mind.management.shared.persistencia.GestionProcesos;
import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;

public class ProcesosController {

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
	private UIForm tableForm;
	private HtmlDataTable dataTable;
	private ProcesoUsuarioBO procesoEliminar;

	private String nombreProcesoCrear;
	private String descripcionProcesoCrear;

	private int idProcesoDuplicar;
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
			setMensajeFeedBack("Proceso creado");
			System.out.println("Cantidad de procesos" + procesos.size());
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
		}
	}

	public void duplicarProceso(ActionEvent event) {
		System.out.println("Duplicando...");
		GestionProcesos gProcesos = new GestionProcesos();
		ProcesoUsuarioBO procesoADuplicar = gProcesos
				.consultarProcesoUsuarioAdministrador(
						usuario.getIdentificador(), getIdProcesoDuplicar());
		procesoADuplicar.setNombre(nombreProcesoDuplicar);
		procesoADuplicar.setDescripcion(descripcionProcesoDuplicar);
		int result = gProcesos.agregarProcesoConPruebas(
				usuario.getIdentificador(), procesoADuplicar,
				procesoADuplicar.getProcesoUsuarioHasPruebaUsuario());
		if (result == Convencion.CORRECTO) {
			setProcesos(gProcesos.listarProcesoUsuarioAdministrador(usuario
					.getIdentificador()));
		}
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
		}
		return null;
	}

	public void eliminarProceso(ActionEvent event) {
		GestionProcesos gProcesos = new GestionProcesos();
		int result = gProcesos.eliminarProcesoUsuarioAdministrador(
				usuario.getIdentificador(), procesoEliminar.getIdentificador());
		if (result == Convencion.CORRECTO) {
			setProcesos(gProcesos.listarProcesoUsuarioAdministrador(usuario
					.getIdentificador()));
		}
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
		procesoEliminar = (ProcesoUsuarioBO) dataTable.getRowData();
		System.out.println("Proceso seleccionado para eliminar");
	}

	public void seleccionarProcesoDuplicar(AjaxBehaviorEvent event) {
		ProcesoUsuarioBO p = (ProcesoUsuarioBO) dataTable.getRowData();
		setNombreProcesoDuplicar(p.getNombre());
		setDescripcionProcesoDuplicar(p.getDescripcion());
		setIdProcesoDuplicar(p.getIdentificador());
		System.out.println("Proceso seleccionado para duplicar: "
				+ getNombreProcesoDuplicar());
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

	public int getIdProcesoDuplicar() {
		return idProcesoDuplicar;
	}

	public void setIdProcesoDuplicar(int idProcesoDuplicar) {
		this.idProcesoDuplicar = idProcesoDuplicar;
	}

}
