package co.mind.web.programadores;

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
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import co.mind.management.shared.dto.UsuarioBO;
import co.mind.management.shared.dto.UsuarioProgramadorBO;
import co.mind.management.shared.persistencia.GestionUsuariosProgramadores;
import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;

@ManagedBean
@ViewScoped
public class ProgramadoresController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UsuarioBO usuario;
	private String nombreUsuario;

	private List<UsuarioProgramadorBO> programadores;
	private List<UsuarioProgramadorBO> programadoresTemp;
	private UsuarioProgramadorBO programador;
	private String parametroBusqueda;

	private boolean continuar = true;

	private transient UIForm tableForm;
	private transient HtmlDataTable dataTable;

	private String nombreProgramadorCrear;
	private String apellidosProgramadorCrear;
	private String correoProgramadorCrear;
	private int idProgramadorCrear;
	private String telefonoProgramadorCrear;

	@PostConstruct
	public void init() {
		verificarLogin();
		if (continuar) {
			setNombreUsuario(usuario.getNombres() + " "
					+ usuario.getApellidos());
			GestionUsuariosProgramadores gProgramadores = new GestionUsuariosProgramadores();
			setProgramadores(gProgramadores.listarUsuariosProgramadores(usuario
					.getIdentificador()));
			programadoresTemp = programadores;
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
			} else {
				String permiso = (String) MindHelper.obtenerSesion()
						.getAttribute("permiso");
				if (permiso != null) {
					if (permiso
							.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_PROGRAMADOR)) {
						System.out.println("Redirigiendo a login...");
						try {
							continuar = false;
							response.sendRedirect("login.do");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				} else {
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
	}

	public void crearProgramador() {
		UsuarioProgramadorBO programador = new UsuarioProgramadorBO();
		programador.setCedula(idProgramadorCrear);
		programador.setNombres(nombreProgramadorCrear);
		programador.setApellidos(apellidosProgramadorCrear);
		programador.setEmpresa(usuario.getEmpresa());
		programador.setCorreo_Electronico(correoProgramadorCrear.toLowerCase());
		programador.setTelefono(telefonoProgramadorCrear);
		GestionUsuariosProgramadores gProgramadores = new GestionUsuariosProgramadores();
		int result = gProgramadores.agregarUsuarioProgramador(
				usuario.getIdentificador(), programador);
		if (result == Convencion.CORRECTO) {
			setProgramadores(gProgramadores.listarUsuariosProgramadores(usuario
					.getIdentificador()));
			programadoresTemp = programadores;
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Programador creado.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"El programador no se pudo crear.", ""));
		}
		setNombreProgramadorCrear("");
		setApellidosProgramadorCrear("");
		setCorreoProgramadorCrear("");
		setIdProgramadorCrear(0);
		setTelefonoProgramadorCrear("");
	}

	public void editarProgramador() {
		programador = (UsuarioProgramadorBO) dataTable.getRowData();
		programador.setEditar(true);
	}

	public void cancelarEditarProgramador() {
		programador = (UsuarioProgramadorBO) dataTable.getRowData();
		programador.setEditar(true);
	}

	public void guardarEditarProgramador() {
		programador = (UsuarioProgramadorBO) dataTable.getRowData();
		programador.setEditar(true);
		GestionUsuariosProgramadores gProgramadores = new GestionUsuariosProgramadores();
		int result = gProgramadores.editarUsuarioProgramador(
				usuario.getIdentificador(), programador);
		if (result == Convencion.CORRECTO) {
			setProgramadores(gProgramadores.listarUsuariosProgramadores(usuario
					.getIdentificador()));
			programadoresTemp = programadores;
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Programador editado.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"El programador no se pudo editar.", ""));
		}
	}

	public void eliminarProgramador(ActionEvent event) {
		GestionUsuariosProgramadores gProgramadores = new GestionUsuariosProgramadores();
		HttpServletRequest request = MindHelper.obtenerRequest();
		int result = gProgramadores.eliminarUsuarioProgramador(usuario
				.getIdentificador(),
				((UsuarioProgramadorBO) ((HttpServletRequest) request)
						.getSession().getAttribute("programadorEliminar"))
						.getIdentificador());
		if (result == Convencion.CORRECTO) {
			setProgramadores(gProgramadores.listarUsuariosProgramadores(usuario
					.getIdentificador()));
			programadoresTemp = programadores;
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Programador eliminado.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"El programador no se pudo eliminar.", ""));
		}
		HttpSession session = request.getSession();
		session.removeAttribute("programadorEliminar");
	}

	public void seleccionarProgramadorEliminar(AjaxBehaviorEvent event) {
		System.out.println("Programador seleccionado para eliminar.");
		HttpSession session = MindHelper.obtenerSesion();
		session.setAttribute("programadorEliminar",
				(UsuarioProgramadorBO) dataTable.getRowData());
	}

	public void activarProgramador() {
		programador = (UsuarioProgramadorBO) dataTable.getRowData();

		GestionUsuariosProgramadores gProgramadores = new GestionUsuariosProgramadores();
		programador.setEstado_Cuenta(Convencion.ESTADO_CUENTA_ACTIVA);
		int result = gProgramadores.cambiarEstadoCuenta(
				(UsuarioProgramadorBO) programador,
				Convencion.ESTADO_CUENTA_ACTIVA);
		if (result == Convencion.CORRECTO) {
			setProgramadores(gProgramadores.listarUsuariosProgramadores(usuario
					.getIdentificador()));
			programadoresTemp = programadores;
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Programador activado.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"El programador no se pudo activar.", ""));
		}
	}

	public void desactivarProgramador() {
		programador = (UsuarioProgramadorBO) dataTable.getRowData();

		GestionUsuariosProgramadores gProgramadores = new GestionUsuariosProgramadores();
		programador.setEstado_Cuenta(Convencion.ESTADO_CUENTA_INACTIVA);
		int result = gProgramadores.cambiarEstadoCuenta(
				(UsuarioProgramadorBO) programador,
				Convencion.ESTADO_CUENTA_INACTIVA);
		if (result == Convencion.CORRECTO) {
			setProgramadores(gProgramadores.listarUsuariosProgramadores(usuario
					.getIdentificador()));
			programadoresTemp = programadores;
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Programador desactivado.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"El programador no se pudo desactivar.", ""));
		}
	}

	public String irAProgramadorEspecifico() {
		programador = (UsuarioProgramadorBO) dataTable.getRowData();
		guardarProgramadorEnSesion(programador);
		return "programador";
	}

	private void guardarProgramadorEnSesion(UsuarioProgramadorBO proceso) {
		HttpSession session = MindHelper.obtenerSesion();
		session.setAttribute("programador", proceso);
	}

	public void buscarProgramadores() {
		programadores = programadoresTemp;
		if (getParametroBusqueda() != null) {
			if (getParametroBusqueda() != "") {
				int cedula = 0;
				boolean esCedula = true;
				try {
					cedula = Integer.parseInt(getParametroBusqueda());
				} catch (NumberFormatException e) {
					esCedula = false;
				}
				List<UsuarioProgramadorBO> resultadoBusqueda = new ArrayList<UsuarioProgramadorBO>();
				if (esCedula) {
					for (UsuarioProgramadorBO pro : getProgramadores()) {
						int id = pro.getIdentificador();
						if (id == cedula) {
							resultadoBusqueda.add(pro);
						}
					}
				} else {
					for (UsuarioProgramadorBO pro : getProgramadores()) {
						if (getParametroBusqueda().equalsIgnoreCase(
								pro.getCorreo_Electronico())
								|| pro.getNombres()
										.toLowerCase()
										.contains(
												getParametroBusqueda()
														.toLowerCase())
								|| pro.getApellidos()
										.toLowerCase()
										.contains(
												getParametroBusqueda()
														.toLowerCase())) {
							resultadoBusqueda.add(pro);
						}
					}
				}
				programadoresTemp = programadores;
				programadores = resultadoBusqueda;
			}
		}
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public List<UsuarioProgramadorBO> getProgramadores() {
		return programadores;
	}

	public void setProgramadores(List<UsuarioProgramadorBO> programadores) {
		this.programadores = programadores;
	}

	public String getParametroBusqueda() {
		return parametroBusqueda;
	}

	public void setParametroBusqueda(String parametroBusqueda) {
		this.parametroBusqueda = parametroBusqueda;
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

	public String getNombreProgramadorCrear() {
		return nombreProgramadorCrear;
	}

	public void setNombreProgramadorCrear(String nombreProgramadorCrear) {
		this.nombreProgramadorCrear = nombreProgramadorCrear;
	}

	public String getApellidosProgramadorCrear() {
		return apellidosProgramadorCrear;
	}

	public void setApellidosProgramadorCrear(String apellidosProgramadorCrear) {
		this.apellidosProgramadorCrear = apellidosProgramadorCrear;
	}

	public String getCorreoProgramadorCrear() {
		return correoProgramadorCrear;
	}

	public void setCorreoProgramadorCrear(String correoProgramadorCrear) {
		this.correoProgramadorCrear = correoProgramadorCrear;
	}

	public int getIdProgramadorCrear() {
		return idProgramadorCrear;
	}

	public void setIdProgramadorCrear(int idProgramadorCrear) {
		this.idProgramadorCrear = idProgramadorCrear;
	}

	public String getTelefonoProgramadorCrear() {
		return telefonoProgramadorCrear;
	}

	public void setTelefonoProgramadorCrear(String telefonoProgramadorCrear) {
		this.telefonoProgramadorCrear = telefonoProgramadorCrear;
	}
}
