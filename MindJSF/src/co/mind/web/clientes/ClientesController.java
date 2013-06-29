package co.mind.web.clientes;

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

import co.mind.management.shared.dto.UsoBO;
import co.mind.management.shared.dto.UsuarioAdministradorBO;
import co.mind.management.shared.dto.UsuarioBO;
import co.mind.management.shared.persistencia.GestionClientes;
import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;

public class ClientesController {

	private UsuarioBO usuario;
	private String nombreUsuario;

	private List<UsuarioAdministradorBO> clientes;
	private List<UsuarioAdministradorBO> clientesTemp;
	private UsuarioAdministradorBO cliente;
	private String parametroBusqueda;
	private boolean mostrarMensajeFeedBack;
	private String mensajeFeedBack;

	private boolean continuar = true;

	private UIForm tableForm;
	private HtmlDataTable dataTable;
	private UsuarioAdministradorBO clienteEliminar;

	private String nombreClienteCrear;
	private String apellidosClienteCrear;
	private String correoClienteCrear;
	private int idClienteCrear;
	private String telefonoClienteCrear;
	private String empresaClienteCrear;
	private String cargoClienteCrear;
	private int usosClienteCrear;

	@PostConstruct
	public void init() {
		verificarLogin();
		if (continuar) {
			setNombreUsuario(usuario.getNombres() + " "
					+ usuario.getApellidos());
			GestionClientes gCLientes = new GestionClientes();
			setClientes(gCLientes.listarUsuariosAdministradores());
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
					if (!permiso
							.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_MAESTRO)) {
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

	public String crearCliente() {
		UsuarioAdministradorBO cli = new UsuarioAdministradorBO();
		cli.setIdentificador(idClienteCrear);
		cli.setNombres(nombreClienteCrear);
		cli.setApellidos(apellidosClienteCrear);
		cli.setCargo(cargoClienteCrear);
		cli.setEmpresa(empresaClienteCrear);
		cli.setTelefono(telefonoClienteCrear);
		cli.setCorreo_Electronico(correoClienteCrear);
		UsoBO us = new UsoBO();
		us.setFechaAsignacion(new Date());
		us.setUsosAsignados(usosClienteCrear);
		GestionClientes gClientes = new GestionClientes();
		int result = gClientes.agregarUsuarioAdministrador(
				usuario.getIdentificador(), cli, us, null);
		if (result == Convencion.CORRECTO) {
			setClientes(gClientes.listarUsuariosAdministradores());
			clientesTemp = clientes;
		}
		return null;
	}

	public String editarCliente() {
		cliente = (UsuarioAdministradorBO) dataTable.getRowData();
		cliente.setEditar(true);
		return nombreUsuario;
	}

	public String cancelarEditarCliente() {
		cliente = (UsuarioAdministradorBO) dataTable.getRowData();
		cliente.setEditar(true);
		return null;
	}

	public String guardarEditarCliente() {
		cliente = (UsuarioAdministradorBO) dataTable.getRowData();
		cliente.setEditar(true);
		GestionClientes gCLientes = new GestionClientes();
		int result = gCLientes.editarUsuarioAdministrador(cliente);
		if (result == Convencion.CORRECTO) {
			setClientes(gCLientes.listarUsuariosAdministradores());
			clientesTemp = clientes;
		}
		return null;
	}

	public String desactivarCliente() {
		cliente = (UsuarioAdministradorBO) dataTable.getRowData();

		GestionClientes gClientes = new GestionClientes();
		cliente.setEstado_Cuenta(Convencion.ESTADO_CUENTA_INACTIVA);
		int result = gClientes.cambiarEstadoCuenta(
				(UsuarioAdministradorBO) cliente,
				Convencion.ESTADO_CUENTA_INACTIVA);
		if (result == Convencion.CORRECTO) {
			setClientes(gClientes.listarUsuariosAdministradores());
			clientesTemp = clientes;
		}
		return null;
	}

	public String activarCliente() {
		cliente = (UsuarioAdministradorBO) dataTable.getRowData();

		GestionClientes gClientes = new GestionClientes();
		cliente.setEstado_Cuenta(Convencion.ESTADO_CUENTA_ACTIVA);
		int result = gClientes.cambiarEstadoCuenta(
				(UsuarioAdministradorBO) cliente,
				Convencion.ESTADO_CUENTA_ACTIVA);
		if (result == Convencion.CORRECTO) {
			setClientes(gClientes.listarUsuariosAdministradores());
			clientesTemp = clientes;
		}
		return null;
	}

	public void eliminarCliente(ActionEvent event) {
		GestionClientes gCLientes = new GestionClientes();
		int result = gCLientes.eliminarUsuarioAdministrador(clienteEliminar
				.getIdentificador());
		if (result == Convencion.CORRECTO) {
			setClientes(gCLientes.listarUsuariosAdministradores());
			clientesTemp = clientes;
		}
	}

	public void seleccionarClienteEliminar(AjaxBehaviorEvent event) {
		clienteEliminar = (UsuarioAdministradorBO) dataTable.getRowData();
		System.out.println("Cliente seleccionada para eliminar");
	}

	public String irAClienteEspecifico() {
		cliente = (UsuarioAdministradorBO) dataTable.getRowData();
		guardarClienteEnSesion(cliente);
		return "cliente";
	}

	private void guardarClienteEnSesion(UsuarioAdministradorBO proceso) {
		HttpSession session = MindHelper.obtenerSesion();
		session.setAttribute("cliente", proceso);
	}

	public String buscarClientes() {
		clientes = clientesTemp;
		if (getParametroBusqueda() != null) {
			if (getParametroBusqueda() != "") {
				int cedula = 0;
				boolean esCedula = true;
				try {
					cedula = Integer.parseInt(getParametroBusqueda());
				} catch (NumberFormatException e) {
					esCedula = false;
				}
				List<UsuarioAdministradorBO> resultadoBusqueda = new ArrayList<UsuarioAdministradorBO>();
				if (esCedula) {
					for (UsuarioAdministradorBO cli : getClientes()) {
						int id = cli.getIdentificador();
						if (id == cedula) {
							resultadoBusqueda.add(cli);
						}
					}
				} else {
					for (UsuarioAdministradorBO cli : getClientes()) {
						if (getParametroBusqueda().toLowerCase().contains(
								cli.getEmpresa())
								|| getParametroBusqueda().equalsIgnoreCase(
										cli.getCorreo_Electronico())) {
							resultadoBusqueda.add(cli);
						}
					}
				}
				clientesTemp = clientes;
				clientes = resultadoBusqueda;
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

	public List<UsuarioAdministradorBO> getClientes() {
		return clientes;
	}

	public void setClientes(List<UsuarioAdministradorBO> clientes) {
		this.clientes = clientes;
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

	public String getNombreClienteCrear() {
		return nombreClienteCrear;
	}

	public void setNombreClienteCrear(String nombreClienteCrear) {
		this.nombreClienteCrear = nombreClienteCrear;
	}

	public String getApellidosClienteCrear() {
		return apellidosClienteCrear;
	}

	public void setApellidosClienteCrear(String apellidosClienteCrear) {
		this.apellidosClienteCrear = apellidosClienteCrear;
	}

	public String getCorreoClienteCrear() {
		return correoClienteCrear;
	}

	public void setCorreoClienteCrear(String correoClienteCrear) {
		this.correoClienteCrear = correoClienteCrear;
	}

	public int getIdClienteCrear() {
		return idClienteCrear;
	}

	public void setIdClienteCrear(int idClienteCrear) {
		this.idClienteCrear = idClienteCrear;
	}

	public String getTelefonoClienteCrear() {
		return telefonoClienteCrear;
	}

	public void setTelefonoClienteCrear(String telefonoClienteCrear) {
		this.telefonoClienteCrear = telefonoClienteCrear;
	}

	public String getEmpresaClienteCrear() {
		return empresaClienteCrear;
	}

	public void setEmpresaClienteCrear(String empresaClienteCrear) {
		this.empresaClienteCrear = empresaClienteCrear;
	}

	public String getCargoClienteCrear() {
		return cargoClienteCrear;
	}

	public void setCargoClienteCrear(String cargoClienteCrear) {
		this.cargoClienteCrear = cargoClienteCrear;
	}

	public int getUsosClienteCrear() {
		return usosClienteCrear;
	}

	public void setUsosClienteCrear(int usosClienteCrear) {
		this.usosClienteCrear = usosClienteCrear;
	}

}
