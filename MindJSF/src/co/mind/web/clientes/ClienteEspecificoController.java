package co.mind.web.clientes;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import co.mind.management.shared.dto.UsoBO;
import co.mind.management.shared.dto.UsuarioAdministradorBO;
import co.mind.management.shared.dto.UsuarioBO;
import co.mind.management.shared.persistencia.GestionUsos;
import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;

public class ClienteEspecificoController {

	private UsuarioBO usuario;
	private String nombreUsuario;
	private UsuarioAdministradorBO cliente;
	private String nombreCliente;
	private List<UsoBO> usos;

	private boolean continuar = true;

	private UIForm tableFormUsos;
	private HtmlDataTable dataTableUsos;
	private Date fechaInicial;
	private Date fechaFinal;
	private String parametroFechaInicial;
	private String parametroFechaFinal;

	private int cantidadUsos;

	@PostConstruct
	public void init() {
		verificarLogin();
		if (continuar) {
			setNombreUsuario(usuario.getNombres() + " "
					+ usuario.getApellidos());
			cliente = obtenerClienteDeSesion();
			setNombreCliente(cliente.getNombres() + " "
					+ cliente.getApellidos());
			GestionUsos gUsos = new GestionUsos();
			setUsos(gUsos.listarUsos(cliente.getIdentificador()));
			Date fecha = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(fecha);
			setParametroFechaFinal((cal.get(Calendar.MONTH) + 1) + "/"
					+ cal.get(Calendar.DAY_OF_MONTH) + "/"
					+ cal.get(Calendar.YEAR));
			setParametroFechaInicial((cal.get(Calendar.MONTH) + 1) + "/" + 1
					+ "/" + cal.get(Calendar.YEAR));
			fechaFinal = fecha;
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
			fechaInicial = cal.getTime();
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

	private UsuarioAdministradorBO obtenerClienteDeSesion() {
		HttpServletRequest request = MindHelper.obtenerRequest();
		return (UsuarioAdministradorBO) ((HttpServletRequest) request)
				.getSession().getAttribute("cliente");

	}

	public String agregarUsos() {
		System.out.print("agregando usos...");
		UsoBO uso = new UsoBO();
		uso.setFechaAsignacion(new Date());
		uso.setUsosAsignados(cantidadUsos);
		uso.setUsosRedimidos(0);
		GestionUsos gUsos = new GestionUsos();
		int result = gUsos.agregarUso(usuario.getIdentificador(), uso);
		if (result == Convencion.CORRECTO) {
			System.out.println("EXITO");
			setUsos(gUsos.listarUsos(cliente.getIdentificador()));
		} else {
			System.out.println("FRACASo");
		}
		return null;
	}

	public String generarReporteUsos() {

		if (fechaFinal == null || fechaInicial == null) {
			return null;
		} else {
			String url = "UsosReportServlet";
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext ctx = context.getExternalContext();
			HttpSession sess = (HttpSession) ctx.getSession(true);

			sess.setAttribute("usuarioUsos", usuario);
			sess.setAttribute("inicioUsos", fechaInicial);
			sess.setAttribute("finalUsos", fechaFinal);
			try {
				context.getExternalContext().dispatch(url);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				context.responseComplete();
			}
		}
		return null;
	}

	public void formatoFechaInicial(javax.faces.event.ValueChangeEvent e) {
		try {
			SimpleDateFormat formatoDelTexto = new SimpleDateFormat(
					"MM/dd/yyyy");
			fechaInicial = formatoDelTexto.parse((String) e.getNewValue());
		} catch (ParseException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void formatoFechaFinal(javax.faces.event.ValueChangeEvent e) {
		try {
			SimpleDateFormat formatoDelTexto = new SimpleDateFormat(
					"MM/dd/yyyy");
			fechaFinal = formatoDelTexto.parse((String) e.getNewValue());
		} catch (ParseException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
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

	public List<UsoBO> getUsos() {
		return usos;
	}

	public void setUsos(List<UsoBO> usos) {
		this.usos = usos;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public UsuarioAdministradorBO getCliente() {
		return cliente;
	}

	public void setCliente(UsuarioAdministradorBO cliente) {
		this.cliente = cliente;
	}

	public int getCantidadUsos() {
		return cantidadUsos;
	}

	public void setCantidadUsos(int cantidadUsos) {
		this.cantidadUsos = cantidadUsos;
	}

	public String getParametroFechaInicial() {
		return parametroFechaInicial;
	}

	public void setParametroFechaInicial(String parametroFechaInicial) {
		this.parametroFechaInicial = parametroFechaInicial;
	}

	public String getParametroFechaFinal() {
		return parametroFechaFinal;
	}

	public void setParametroFechaFinal(String parametroFechaFinal) {
		this.parametroFechaFinal = parametroFechaFinal;
	}

}
