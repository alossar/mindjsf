package co.mind.web.evaluados;

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
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import co.mind.management.shared.dto.EvaluadoBO;
import co.mind.management.shared.dto.UsuarioBO;
import co.mind.management.shared.persistencia.GestionEvaluados;
import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;

@ManagedBean
@ViewScoped
public class EvaluadosController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UsuarioBO usuario;
	private String nombreUsuario;

	private List<EvaluadoBO> evaluados;
	private List<EvaluadoBO> evaluadosTemp;
	private EvaluadoBO evaluado;
	private String parametroBusqueda;

	// JavaServerFaces related variables
	private transient UIForm tableForm;
	private transient HtmlDataTable dataTable;

	private String nombreEvaluadoCrear;
	private String apellidoEvaluadoCrear;
	private int cedulaEvaluadoCrear;
	private String correoEvaluadoCrear;

	private int cantidadEvaluadosAMostrar = 10;
	private List<EvaluadoBO> evaluadosAMostrar = new ArrayList<EvaluadoBO>();
	private boolean primeraPagina = true;
	private boolean ultimaPagina = false;
	private int paginaActual;

	private boolean continuar = true;

	@PostConstruct
	public void init() {
		verificarLogin();
		if (continuar) {
			setNombreUsuario(usuario.getNombres() + " "
					+ usuario.getApellidos());
			GestionEvaluados gEvaluados = new GestionEvaluados();
			setEvaluados(gEvaluados.listarUsuariosBasicos(usuario
					.getIdentificador()));
			evaluadosTemp = evaluados;
			paginaActual = 0;
			actualizarEvaluados();
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

	public void crearEvaluado() {
		EvaluadoBO eva = new EvaluadoBO();
		eva.setApellidos(apellidoEvaluadoCrear);
		eva.setCedula(cedulaEvaluadoCrear);
		eva.setNombres(nombreEvaluadoCrear);
		eva.setCorreoElectronico(correoEvaluadoCrear.toLowerCase());
		eva.setIdentificadorUsuarioAdministrador(usuario.getIdentificador());
		GestionEvaluados gEvaluados = new GestionEvaluados();
		int result = gEvaluados.agregarUsuarioBasico(
				usuario.getIdentificador(), eva);
		if (result == Convencion.CORRECTO) {
			setEvaluados(gEvaluados.listarUsuariosBasicos(usuario
					.getIdentificador()));
			evaluadosTemp = evaluados;

			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Evaluado creado.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"El evaluado no se pudo crear.", ""));
		}
		setNombreEvaluadoCrear("");
		setCedulaEvaluadoCrear(0);
		setCorreoEvaluadoCrear("");
		setApellidoEvaluadoCrear("");
		actualizarEvaluados();
	}

	public void editarEvaluado() {
		evaluado = (EvaluadoBO) dataTable.getRowData();
		evaluado.setEditar(true);
	}

	public void cancelarEditarEvaluado() {
		evaluado.setEditar(false);
		GestionEvaluados gEvaluados = new GestionEvaluados();
		setEvaluados(gEvaluados.listarUsuariosBasicos(usuario
				.getIdentificador()));
		evaluadosTemp = evaluados;
	}

	public void guardarEditarEvaluado() {
		evaluado = (EvaluadoBO) dataTable.getRowData();
		evaluado.setEditar(false);
		GestionEvaluados gEvaluados = new GestionEvaluados();
		int result = gEvaluados.editarUsuarioBasico(usuario.getIdentificador(),
				evaluado);
		if (result == Convencion.CORRECTO) {
			setEvaluados(gEvaluados.listarUsuariosBasicos(usuario
					.getIdentificador()));
			evaluadosTemp = evaluados;

			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Evaluado editado.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"El evaluado no se pudo editar.", ""));
		}
	}

	public void seleccionarEvaluadoEliminar(AjaxBehaviorEvent event) {
		System.out.println("Evaluado seleccionado para eliminar.");
		HttpSession session = MindHelper.obtenerSesion();
		session.setAttribute("evaluadoEliminar",
				(EvaluadoBO) dataTable.getRowData());
	}

	public void eliminarEvaluado() {
		System.out.println("Eliminando evaluado.");
		HttpServletRequest request = MindHelper.obtenerRequest();
		GestionEvaluados gEvaluados = new GestionEvaluados();
		int result = gEvaluados.eliminarUsuarioBasico(usuario
				.getIdentificador(),
				((EvaluadoBO) ((HttpServletRequest) request).getSession()
						.getAttribute("evaluadoEliminar")).getIdentificador());
		if (result == Convencion.CORRECTO) {
			setEvaluados(gEvaluados.listarUsuariosBasicos(usuario
					.getIdentificador()));
			evaluadosTemp = evaluados;

			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Evaluado eliminado.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"El evaluado no se pudo eliminar.", ""));
		}
		HttpSession session = request.getSession();
		session.removeAttribute("evaluadoEliminar");
		actualizarEvaluados();
	}

	public String irAEvaluadoEspecifico() {
		evaluado = (EvaluadoBO) dataTable.getRowData();
		guardarEvaluadoEnSesion(evaluado);
		return "evaluado";
	}

	private void guardarEvaluadoEnSesion(EvaluadoBO proceso) {
		HttpSession session = MindHelper.obtenerSesion();
		session.setAttribute("evaluado", proceso);
	}

	public void buscarEvaluados() {
		evaluados = evaluadosTemp;
		if (getParametroBusqueda() != null) {
			if (getParametroBusqueda() != "") {
				int cedula = 0;
				boolean esCedula = true;
				try {
					cedula = Integer.parseInt(getParametroBusqueda());
				} catch (NumberFormatException e) {
					esCedula = false;
				}
				List<EvaluadoBO> resultadoBusqueda = new ArrayList<EvaluadoBO>();
				if (esCedula) {
					for (EvaluadoBO eval : getEvaluados()) {
						int id = eval.getIdentificador();
						if (id == cedula) {
							resultadoBusqueda.add(eval);
						}
					}
				} else {
					for (EvaluadoBO eval : getEvaluados()) {
						if (getParametroBusqueda().equalsIgnoreCase(
								eval.getCorreoElectronico())
								|| eval.getNombres()
										.toLowerCase()
										.contains(
												getParametroBusqueda()
														.toLowerCase())
								|| eval.getApellidos()
										.toLowerCase()
										.contains(
												getParametroBusqueda()
														.toLowerCase())) {
							resultadoBusqueda.add(eval);
						}
					}
				}
				evaluadosTemp = evaluados;
				evaluados = resultadoBusqueda;
			}
		}
		actualizarEvaluados();
	}

	public void evaluadosSiguientes() {
		if (!ultimaPagina) {
			paginaActual++;
			actualizarEvaluados();
		}
	}

	public void evaluadosAnteriores() {
		if (!primeraPagina) {
			paginaActual--;
			actualizarEvaluados();
		}
	}

	private void actualizarEvaluados() {
		evaluadosAMostrar.clear();
		for (int i = 0; i < cantidadEvaluadosAMostrar; i++) {
			try {
				evaluadosAMostrar.add(getEvaluados().get(
						paginaActual * cantidadEvaluadosAMostrar + i));

			} catch (IndexOutOfBoundsException e) {
				ultimaPagina = true;
				primeraPagina = false;
				break;
			} catch (NullPointerException e) {
				break;
			}
		}
		if (paginaActual <= 0) {
			ultimaPagina = false;
			if (evaluadosAMostrar.size() < cantidadEvaluadosAMostrar) {
				ultimaPagina = true;
			}
			primeraPagina = true;
		} else if (paginaActual >= evaluados.size() / cantidadEvaluadosAMostrar) {
			ultimaPagina = true;
			primeraPagina = false;
		} else {
			ultimaPagina = false;
			primeraPagina = false;
		}
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public List<EvaluadoBO> getEvaluados() {
		return evaluados;
	}

	public void setEvaluados(List<EvaluadoBO> evaluados) {
		this.evaluados = evaluados;
	}

	public String getParametroBusqueda() {
		return parametroBusqueda;
	}

	public void setParametroBusqueda(String parametroBusqueda) {
		this.parametroBusqueda = parametroBusqueda;
	}

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public UIForm getTableForm() {
		return tableForm;
	}

	public void setTableForm(UIForm tableForm) {
		this.tableForm = tableForm;
	}

	public String getNombreEvaluadoCrear() {
		return nombreEvaluadoCrear;
	}

	public void setNombreEvaluadoCrear(String nombreEvaluadoCrear) {
		this.nombreEvaluadoCrear = nombreEvaluadoCrear;
	}

	public String getApellidoEvaluadoCrear() {
		return apellidoEvaluadoCrear;
	}

	public void setApellidoEvaluadoCrear(String apellidoEvaluadoCrear) {
		this.apellidoEvaluadoCrear = apellidoEvaluadoCrear;
	}

	public int getCedulaEvaluadoCrear() {
		return cedulaEvaluadoCrear;
	}

	public void setCedulaEvaluadoCrear(int cedulaEvaluadoCrear) {
		this.cedulaEvaluadoCrear = cedulaEvaluadoCrear;
	}

	public String getCorreoEvaluadoCrear() {
		return correoEvaluadoCrear;
	}

	public void setCorreoEvaluadoCrear(String correoEvaluadoCrear) {
		this.correoEvaluadoCrear = correoEvaluadoCrear;
	}

	public List<EvaluadoBO> getEvaluadosAMostrar() {
		return evaluadosAMostrar;
	}

	public void setEvaluadosAMostrar(List<EvaluadoBO> evaluadosAMostrar) {
		this.evaluadosAMostrar = evaluadosAMostrar;
	}

	public boolean isPrimeraPagina() {
		return primeraPagina;
	}

	public void setPrimeraPagina(boolean primeraPagina) {
		this.primeraPagina = primeraPagina;
	}

	public boolean isUltimaPagina() {
		return ultimaPagina;
	}

	public void setUltimaPagina(boolean ultimaPagina) {
		this.ultimaPagina = ultimaPagina;
	}

	public int getCantidadEvaluadosAMostrar() {
		return cantidadEvaluadosAMostrar;
	}

	public void setCantidadEvaluadosAMostrar(int cantidadEvaluadosAMostrar) {
		this.cantidadEvaluadosAMostrar = cantidadEvaluadosAMostrar;
	}

}
