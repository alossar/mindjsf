package co.mind.web.evaluados;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import co.mind.management.shared.dto.EvaluadoBO;
import co.mind.management.shared.dto.PruebaUsuarioBO;
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
	private boolean mostrarMensajeFeedBack;
	private String mensajeFeedBack;

	// JavaServerFaces related variables
	private transient UIForm tableForm;
	private transient HtmlDataTable dataTable;

	private String nombreEvaluadoCrear;
	private String apellidoEvaluadoCrear;
	private int cedulaEvaluadoCrear;
	private String correoEvaluadoCrear;

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

	public String crearEvaluado() {
		EvaluadoBO eva = new EvaluadoBO();
		eva.setApellidos(apellidoEvaluadoCrear);
		eva.setCedula(cedulaEvaluadoCrear);
		eva.setNombres(nombreEvaluadoCrear);
		eva.setCorreoElectronico(correoEvaluadoCrear);
		eva.setIdentificadorUsuarioAdministrador(usuario.getIdentificador());
		GestionEvaluados gEvaluados = new GestionEvaluados();
		int result = gEvaluados.agregarUsuarioBasico(
				usuario.getIdentificador(), eva);
		if (result == Convencion.CORRECTO) {
			setEvaluados(gEvaluados.listarUsuariosBasicos(usuario
					.getIdentificador()));
			evaluadosTemp = evaluados;
		}
		return null;
	}

	public String editarEvaluado() {
		evaluado = (EvaluadoBO) dataTable.getRowData();
		evaluado.setEditar(true);
		return nombreUsuario;
	}

	public String cancelarEditarEvaluado() {
		evaluado.setEditar(false);
		GestionEvaluados gEvaluados = new GestionEvaluados();
		setEvaluados(gEvaluados.listarUsuariosBasicos(usuario
				.getIdentificador()));
		evaluadosTemp = evaluados;
		return null;
	}

	public String guardarEditarEvaluado() {
		evaluado = (EvaluadoBO) dataTable.getRowData();
		evaluado.setEditar(false);
		GestionEvaluados gEvaluados = new GestionEvaluados();
		int result = gEvaluados.editarUsuarioBasico(usuario.getIdentificador(),
				evaluado);
		if (result == Convencion.CORRECTO) {
			setEvaluados(gEvaluados.listarUsuariosBasicos(usuario
					.getIdentificador()));
			evaluadosTemp = evaluados;
		}
		return null;
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
		}
		HttpSession session = request.getSession();
		session.removeAttribute("evaluadoEliminar");
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

	public String buscarEvaluados() {
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
		return null;
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

}
