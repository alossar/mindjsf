package co.mind.web.pruebas;

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

import co.mind.management.shared.dto.ProcesoUsuarioBO;
import co.mind.management.shared.dto.PruebaUsuarioBO;
import co.mind.management.shared.dto.UsuarioBO;
import co.mind.management.shared.dto.UsuarioProgramadorBO;
import co.mind.management.shared.persistencia.GestionPruebas;
import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;

@ManagedBean
@ViewScoped
public class PruebasController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UsuarioBO usuario;
	private String nombreUsuario;
	private boolean continuar = true;
	private List<PruebaUsuarioBO> pruebas;
	private List<PruebaUsuarioBO> pruebasTemp;
	private PruebaUsuarioBO prueba;
	private String parametroBusqueda;
	private boolean mostrarMensajeFeedBack;
	private String mensajeFeedBack;

	private String nombrePruebaCrear;
	private String descripcionPruebaCrear;

	// JavaServerFaces related variables
	private transient UIForm tableForm;
	private transient HtmlDataTable dataTable;

	private int cantidadPruebasAMostrar = 10;
	private List<PruebaUsuarioBO> pruebasAMostrar = new ArrayList<PruebaUsuarioBO>();
	private boolean primeraPagina = true;
	private boolean ultimaPagina = false;
	private int paginaActual;

	@PostConstruct
	public void init() {
		verificarLogin();
		if (continuar) {
			GestionPruebas gPruebas = new GestionPruebas();
			String permiso = (String) MindHelper.obtenerSesion().getAttribute(
					"permiso");
			if (permiso.equalsIgnoreCase(Convencion.TIPO_USUARIO_PROGRAMADOR)) {
				setPruebas(gPruebas
						.listarPruebasUsuarioAdministrador(((UsuarioProgramadorBO) usuario)
								.getUsuarioAdministradorID()));
			} else {
				setPruebas(gPruebas.listarPruebasUsuarioAdministrador(usuario
						.getIdentificador()));
			}
			setNombreUsuario(usuario.getNombres() + " "
					+ usuario.getApellidos());
			pruebasTemp = pruebas;
			setMensajeFeedBack("Proceso creado");
			paginaActual = 0;
			actualizarPruebas();
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
				if (permiso == null) {
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

	public void crearPrueba(ActionEvent event) {
		PruebaUsuarioBO pru = new PruebaUsuarioBO();
		pru.setDescripcion(descripcionPruebaCrear);
		pru.setNombre(nombrePruebaCrear);
		pru.setUsuarioAdministradorID(usuario.getIdentificador());
		GestionPruebas gPruebas = new GestionPruebas();
		int result = gPruebas.agregarPruebaUsuarioAdministrador(
				usuario.getIdentificador(), pru);
		if (result == Convencion.CORRECTO) {
			setPruebas(gPruebas.listarPruebasUsuarioAdministrador(usuario
					.getIdentificador()));
			pruebasTemp = pruebas;
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Prueba creada.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN, "La prueba no se pudo crear.",
					""));
		}
		setNombrePruebaCrear("");
		setDescripcionPruebaCrear("");
		actualizarPruebas();
	}

	public void editarPrueba() {
		prueba = (PruebaUsuarioBO) dataTable.getRowData();
		prueba.setEditar(true);
	}

	public void cancelarEditarPrueba() {
		prueba = (PruebaUsuarioBO) dataTable.getRowData();
		prueba.setEditar(false);
		GestionPruebas gPruebas = new GestionPruebas();
		setPruebas(gPruebas.listarPruebasUsuarioAdministrador(usuario
				.getIdentificador()));
	}

	public void guardarEditarPrueba() {
		prueba = (PruebaUsuarioBO) dataTable.getRowData();
		prueba.setEditar(false);
		GestionPruebas gPruebas = new GestionPruebas();
		int result = gPruebas.editarPruebaUsuarioAdministrador(
				usuario.getIdentificador(), prueba);
		if (result == Convencion.CORRECTO) {
			setPruebas(gPruebas.listarPruebasUsuarioAdministrador(usuario
					.getIdentificador()));
			pruebasTemp = pruebas;
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Prueba editada.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN, "La prueba no se pudo crear.",
					""));
		}
	}

	public void seleccionarPruebaEliminar(AjaxBehaviorEvent event) {
		System.out.println("Prueba seleccionada para eliminar.");
		HttpSession session = MindHelper.obtenerSesion();
		session.setAttribute("pruebaEliminar",
				(PruebaUsuarioBO) dataTable.getRowData());
	}

	public void eliminarPrueba(ActionEvent event) {
		GestionPruebas gPruebas = new GestionPruebas();
		HttpServletRequest request = MindHelper.obtenerRequest();
		int result = gPruebas.eliminarPruebaUsuarioAdministrador(usuario
				.getIdentificador(),
				((PruebaUsuarioBO) ((HttpServletRequest) request).getSession()
						.getAttribute("pruebaEliminar")).getIdentificador());
		if (result == Convencion.CORRECTO) {
			setPruebas(gPruebas.listarPruebasUsuarioAdministrador(usuario
					.getIdentificador()));
			pruebasTemp = pruebas;
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Prueba eliminada.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"La prueba no se pudo eliminar.", ""));
		}
		HttpSession session = request.getSession();
		session.removeAttribute("pruebaEliminar");
		actualizarPruebas();
	}

	private void guardarPruebaEnSesion(PruebaUsuarioBO proceso) {
		HttpSession session = MindHelper.obtenerSesion();
		session.setAttribute("prueba", proceso);
	}

	public void buscarPruebas() {
		pruebas = pruebasTemp;
		if (getParametroBusqueda() != null) {
			if (getParametroBusqueda() != "") {
				List<PruebaUsuarioBO> resultadoBusqueda = new ArrayList<PruebaUsuarioBO>();
				for (PruebaUsuarioBO prueba : getPruebas()) {
					String nombre = prueba.getNombre();
					if (nombre.toLowerCase().contains(
							getParametroBusqueda().toLowerCase())) {
						resultadoBusqueda.add(prueba);
					}
				}
				pruebasTemp = pruebas;
				pruebas = resultadoBusqueda;
			}
		}
		actualizarPruebas();
	}

	public void pruebasSiguientes() {
		if (!ultimaPagina) {
			paginaActual++;
			actualizarPruebas();
		}
	}

	public void pruebasAnteriores() {
		if (!primeraPagina) {
			paginaActual--;
			actualizarPruebas();
		}
	}

	private void actualizarPruebas() {
		pruebasAMostrar.clear();
		for (int i = 0; i < cantidadPruebasAMostrar; i++) {
			try {
				pruebasAMostrar.add(getPruebas().get(
						paginaActual * cantidadPruebasAMostrar + i));

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
			if (pruebasAMostrar.size() < cantidadPruebasAMostrar) {
				ultimaPagina = true;
			}
			primeraPagina = true;
		} else if (paginaActual >= pruebas.size() / cantidadPruebasAMostrar) {
			ultimaPagina = true;
			primeraPagina = false;
		} else {
			ultimaPagina = false;
			primeraPagina = false;
		}
	}

	public String irAPruebaEspecifica() {
		prueba = (PruebaUsuarioBO) dataTable.getRowData();
		guardarPruebaEnSesion(prueba);
		return "prueba";
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public List<PruebaUsuarioBO> getPruebas() {
		return pruebas;
	}

	public void setPruebas(List<PruebaUsuarioBO> pruebas) {
		this.pruebas = pruebas;
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

	public boolean isMostrarMensajeFeedBack() {
		return mostrarMensajeFeedBack;
	}

	public void setMostrarMensajeFeedBack(boolean mostrarMensajeFeedBack) {
		this.mostrarMensajeFeedBack = mostrarMensajeFeedBack;
	}

	public String getNombrePruebaCrear() {
		return nombrePruebaCrear;
	}

	public void setNombrePruebaCrear(String nombrePruebaCrear) {
		this.nombrePruebaCrear = nombrePruebaCrear;
	}

	public String getDescripcionPruebaCrear() {
		return descripcionPruebaCrear;
	}

	public void setDescripcionPruebaCrear(String descripcionPruebaCrear) {
		this.descripcionPruebaCrear = descripcionPruebaCrear;
	}

	public int getCantidadPruebasAMostrar() {
		return cantidadPruebasAMostrar;
	}

	public void setCantidadPruebasAMostrar(int cantidadPruebasAMostrar) {
		this.cantidadPruebasAMostrar = cantidadPruebasAMostrar;
	}

	public List<PruebaUsuarioBO> getPruebasAMostrar() {
		return pruebasAMostrar;
	}

	public void setPruebasAMostrar(List<PruebaUsuarioBO> pruebasAMostrar) {
		this.pruebasAMostrar = pruebasAMostrar;
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

}
