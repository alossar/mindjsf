package co.mind.web.pruebas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import co.mind.management.shared.dto.PruebaUsuarioBO;
import co.mind.management.shared.dto.UsuarioBO;
import co.mind.management.shared.persistencia.GestionPruebas;
import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;

public class PruebasController {

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
	private UIForm tableForm;
	private HtmlDataTable dataTable;
	private PruebaUsuarioBO pruebaEliminar;

	@PostConstruct
	public void init() {
		verificarLogin();
		if (continuar) {
			GestionPruebas gPruebas = new GestionPruebas();
			setPruebas(gPruebas.listarPruebasUsuarioAdministrador(usuario
					.getIdentificador()));
			setNombreUsuario(usuario.getNombres() + " "
					+ usuario.getApellidos());
			pruebasTemp = pruebas;
			setMensajeFeedBack("Proceso creado");
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

	public String crearPrueba() {
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
		}
		return null;
	}

	public String editarPrueba() {
		prueba = (PruebaUsuarioBO) dataTable.getRowData();
		prueba.setEditar(true);
		return nombreUsuario;
	}

	public String cancelarEditarPrueba() {
		prueba = (PruebaUsuarioBO) dataTable.getRowData();
		prueba.setEditar(false);
		GestionPruebas gPruebas = new GestionPruebas();
		setPruebas(gPruebas.listarPruebasUsuarioAdministrador(usuario
				.getIdentificador()));
		return null;
	}

	public String guardarEditarPrueba() {
		prueba = (PruebaUsuarioBO) dataTable.getRowData();
		prueba.setEditar(false);
		GestionPruebas gPruebas = new GestionPruebas();
		int result = gPruebas.editarPruebaUsuarioAdministrador(
				usuario.getIdentificador(), prueba);
		if (result == Convencion.CORRECTO) {
			setPruebas(gPruebas.listarPruebasUsuarioAdministrador(usuario
					.getIdentificador()));
			pruebasTemp = pruebas;
		}
		return null;
	}

	public void seleccionarPruebaEliminar(AjaxBehaviorEvent event) {
		pruebaEliminar = (PruebaUsuarioBO) dataTable.getRowData();
		System.out.println("Prueba seleccionada para eliminar");
	}

	public void eliminarPrueba(ActionEvent event) {
		GestionPruebas gPruebas = new GestionPruebas();
		int result = gPruebas.eliminarPruebaUsuarioAdministrador(
				usuario.getIdentificador(), pruebaEliminar.getIdentificador());
		if (result == Convencion.CORRECTO) {
			setPruebas(gPruebas.listarPruebasUsuarioAdministrador(usuario
					.getIdentificador()));
			pruebasTemp = pruebas;
		}
	}

	private void guardarPruebaEnSesion(PruebaUsuarioBO proceso) {
		HttpSession session = MindHelper.obtenerSesion();
		session.setAttribute("prueba", proceso);
	}

	public String buscarPruebas() {
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
		return null;
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

}
