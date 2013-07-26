package co.mind.web.procesos;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import co.mind.management.shared.dto.EvaluadoBO;
import co.mind.management.shared.dto.ParticipacionEnProcesoBO;
import co.mind.management.shared.dto.ProcesoUsuarioBO;
import co.mind.management.shared.dto.ProcesoUsuarioHasPruebaUsuarioBO;
import co.mind.management.shared.dto.PruebaUsuarioBO;
import co.mind.management.shared.dto.UsuarioBO;
import co.mind.management.shared.dto.UsuarioProgramadorBO;
import co.mind.management.shared.persistencia.GestionEvaluacion;
import co.mind.management.shared.persistencia.GestionEvaluados;
import co.mind.management.shared.persistencia.GestionProcesos;
import co.mind.management.shared.persistencia.GestionPruebas;
import co.mind.management.shared.persistencia.GestionUsos;
import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;
import co.mind.management.shared.recursos.SMTPSender;

@ManagedBean
@ViewScoped
public class ProcesoEspecificoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	UsuarioBO usuario;
	private String nombreUsuario;
	ProcesoUsuarioBO proceso;
	private String nombreProceso;
	private String descripcionProceso;
	List<PruebaUsuarioBO> pruebasProcesoEspecifico;
	List<ParticipacionEnProcesoBO> participacionesProcesoEspecifico;
	List<ParticipacionEnProcesoBO> resultadosProcesoEspecifico;

	private boolean enEvaluados;
	private boolean enPruebas;
	private boolean enResultados;

	private transient UIForm tableFormEvaluados;
	private transient HtmlDataTable dataTableEvaluados;
	private transient UIForm tableFormPruebas;
	private transient HtmlDataTable dataTablePruebas;
	private transient UIForm tableFormResultados;
	private transient HtmlDataTable dataTableResultados;

	private String nombreEvaluadoCrear;
	private String apellidoEvaluadoCrear;
	private int cedulaEvaluadoCrear;
	private String correoEvaluadoCrear;

	private String nombrePruebaCrear;
	private String descripcionPruebaCrear;

	private PruebaUsuarioBO[] pruebasRestantes;
	private EvaluadoBO[] evaluadosRestantes;

	private Integer[] selectItemsPruebasRestantes;
	private Integer[] selectItemsEvaluadosRestantes;

	boolean continuar = true;

	private boolean editar;

	private boolean agregarPrueba;
	private boolean crearPrueba;
	private boolean agregarEvaluado;
	private boolean crearEvaluado;

	private Date fechaInicial;
	private Date fechaFinal;
	private String parametroFechaInicial;
	private String parametroFechaFinal;
	private int idUsuario;

	private int cantidadPruebasAMostrar = 10;
	private List<PruebaUsuarioBO> pruebasAMostrar = new ArrayList<PruebaUsuarioBO>();
	private boolean primeraPaginaPruebas = true;
	private boolean ultimaPaginaPruebas = false;
	private int paginaActualPruebas;

	private int cantidadEvaluadosAMostrar = 10;
	private List<ParticipacionEnProcesoBO> evaluadosAMostrar = new ArrayList<ParticipacionEnProcesoBO>();
	private boolean primeraPaginaEvaluados = true;
	private boolean ultimaPaginaEvaluados = false;
	private int paginaActualEvaluados;

	private int cantidadResultadosAMostrar = 10;
	private List<ParticipacionEnProcesoBO> resultadosAMostrar = new ArrayList<ParticipacionEnProcesoBO>();
	private boolean primeraPaginaResultados = true;
	private boolean ultimaPaginaResultados = false;
	private int paginaActualResultados;

	private Map<Integer, Boolean> resultadosReporte = new HashMap<Integer, Boolean>();

	private String parametroBusquedaPrueba;
	private String parametroBusquedaEvaluado;
	private PruebaUsuarioBO[] pruebasTemp;
	private EvaluadoBO[] evaluadosTemp;

	@PostConstruct
	public void init() {
		verificarLogin();
		if (continuar) {
			proceso = obtenerProcesoDeSesion();
			if (proceso == null) {
				HttpServletResponse response = MindHelper.obtenerResponse();
				try {
					response.sendRedirect("procesos.do");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				setEnEvaluados(false);
				setEnPruebas(true);
				setEnResultados(false);

				setNombreUsuario(usuario.getNombres() + " "
						+ usuario.getApellidos());
				setNombreProceso(proceso.getNombre());
				setDescripcionProceso(proceso.getDescripcion());
				GestionProcesos gProcesos = new GestionProcesos();
				GestionEvaluacion gEvaluacion = new GestionEvaluacion();
				GestionPruebas gPruebas = new GestionPruebas();
				GestionEvaluados gEvaluados = new GestionEvaluados();

				ProcesoUsuarioBO result = gProcesos
						.consultarProcesoUsuarioAdministrador(
								usuario.getIdentificador(),
								proceso.getIdentificador());
				List<PruebaUsuarioBO> pruebas = new ArrayList<PruebaUsuarioBO>();
				for (ProcesoUsuarioHasPruebaUsuarioBO pHas : result
						.getProcesoUsuarioHasPruebaUsuario()) {
					pruebas.add(pHas.getPruebasUsuario());
				}

				String permiso = (String) MindHelper.obtenerSesion()
						.getAttribute("permiso");
				idUsuario = usuario.getIdentificador();

				if (permiso
						.equalsIgnoreCase(Convencion.VALOR_PERMISOS_USUARIO_PROGRAMADOR)) {
					idUsuario = ((UsuarioProgramadorBO) usuario)
							.getUsuarioAdministradorID();
				}

				setPruebasProcesoEspecifico(pruebas);
				setParticipacionesProcesoEspecifico(gEvaluacion
						.listarParticipacionesEnProceso(
								usuario.getIdentificador(),
								proceso.getIdentificador()));
				setResultadosProcesoEspecifico(gEvaluacion
						.listarParticipacionesEnProcesoCompletas(
								usuario.getIdentificador(),
								proceso.getIdentificador()));
				setPruebasRestantes(listaPruebasAArreglo(obtenerPruebasNoEnProceso(
						gPruebas.listarPruebasUsuarioAdministrador(idUsuario),
						getPruebasProcesoEspecifico())));
				setEvaluadosRestantes(listaEvaluadosAArreglo(obtenerEvaluadosNoEnProceso(
						gEvaluados.listarUsuariosBasicos(usuario
								.getIdentificador()),
						obtenerEvaluadosDeParticipacion(getParticipacionesProcesoEspecifico()))));
				evaluadosTemp = getEvaluadosRestantes();
				pruebasTemp = getPruebasRestantes();
				setEditar(false);
				setAgregarPrueba(false);
				setCrearPrueba(false);
				setAgregarEvaluado(false);
				setCrearEvaluado(false);
				try {
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(proceso.getFechaFinalizacion());
					setParametroFechaFinal((cal1.get(Calendar.MONTH)) + "/"
							+ cal1.get(Calendar.DAY_OF_MONTH) + "/"
							+ cal1.get(Calendar.YEAR));
				} catch (Exception e) {
					setParametroFechaFinal(null);
				}
				try {
					Calendar cal2 = Calendar.getInstance();
					cal2.setTime(proceso.getFechaInicio());
					setParametroFechaInicial((cal2.get(Calendar.MONTH) + 1)
							+ "/" + 1 + "/" + cal2.get(Calendar.YEAR));
				} catch (Exception e) {
					setParametroFechaInicial(null);
				}
				fechaFinal = proceso.getFechaFinalizacion();
				fechaInicial = proceso.getFechaInicio();
				resultadosReporte.clear();
				paginaActualEvaluados = 0;
				paginaActualPruebas = 0;
				paginaActualResultados = 0;
				actualizarPruebas();
				actualizarEvaluados();
				actualizarResultados();
			}
		}
	}

	private ProcesoUsuarioBO obtenerProcesoDeSesion() {
		HttpServletRequest request = MindHelper.obtenerRequest();
		return (ProcesoUsuarioBO) ((HttpServletRequest) request).getSession()
				.getAttribute("proceso");

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

	public void verParticipaciones() {
		setEnEvaluados(true);
		setEnPruebas(false);
		setEnResultados(false);
	}

	public void verPruebas() {
		setEnEvaluados(false);
		setEnPruebas(true);
		setEnResultados(false);
	}

	public void verResultados() {
		setEnEvaluados(false);
		setEnPruebas(false);
		setEnResultados(true);
	}

	public String irAPrueba() {
		PruebaUsuarioBO prueba = (PruebaUsuarioBO) dataTablePruebas
				.getRowData();
		guardarPruebaEnSesion(prueba);
		return "prueba";
	}

	private void guardarPruebaEnSesion(PruebaUsuarioBO prueba) {
		HttpSession session = MindHelper.obtenerSesion();
		session.setAttribute("prueba", prueba);
	}

	public void seleccionarPruebaEliminar(AjaxBehaviorEvent event) {
		System.out.println("Prueba seleccionada para eliminar.");
		HttpSession session = MindHelper.obtenerSesion();
		session.setAttribute("pruebaEliminar",
				(PruebaUsuarioBO) dataTablePruebas.getRowData());
	}

	public void seleccionarParticipacionEliminar(AjaxBehaviorEvent event) {
		System.out.println("Participacion seleccionado para eliminar.");
		HttpSession session = MindHelper.obtenerSesion();
		session.setAttribute("participacionEliminar",
				(ParticipacionEnProcesoBO) dataTableEvaluados.getRowData());
	}

	public void eliminarPrueba(ActionEvent event) {
		GestionProcesos gProcesos = new GestionProcesos();
		HttpServletRequest request = MindHelper.obtenerRequest();
		int result = gProcesos.eliminarPruebaDeProceso(usuario
				.getIdentificador(),
				(PruebaUsuarioBO) ((HttpServletRequest) request).getSession()
						.getAttribute("pruebaEliminar"), proceso);

		if (result == Convencion.CORRECTO) {
			GestionPruebas gPruebas = new GestionPruebas();
			ProcesoUsuarioBO pro = gProcesos
					.consultarProcesoUsuarioAdministrador(
							usuario.getIdentificador(),
							proceso.getIdentificador());
			List<PruebaUsuarioBO> pruebas = new ArrayList<PruebaUsuarioBO>();
			for (ProcesoUsuarioHasPruebaUsuarioBO pHas : pro
					.getProcesoUsuarioHasPruebaUsuario()) {
				pruebas.add(pHas.getPruebasUsuario());
			}
			setPruebasProcesoEspecifico(pruebas);

			setPruebasRestantes(listaPruebasAArreglo(obtenerPruebasNoEnProceso(
					gPruebas.listarPruebasUsuarioAdministrador(usuario
							.getIdentificador()), getPruebasProcesoEspecifico())));

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

	public void eliminarParticipacion(ActionEvent event) {
		HttpServletRequest request = MindHelper.obtenerRequest();
		GestionEvaluacion gEvaluacion = new GestionEvaluacion();
		int result = gEvaluacion.eliminarParticipacionEnProceso(usuario
				.getIdentificador(),
				((ParticipacionEnProcesoBO) ((HttpServletRequest) request)
						.getSession().getAttribute("participacionEliminar"))
						.getUsuarioBasico().getIdentificador(), proceso
						.getIdentificador(),
				((ParticipacionEnProcesoBO) ((HttpServletRequest) request)
						.getSession().getAttribute("participacionEliminar"))
						.getIdentificador());
		if (result == Convencion.CORRECTO) {
			setParticipacionesProcesoEspecifico(gEvaluacion
					.listarParticipacionesEnProceso(usuario.getIdentificador(),
							proceso.getIdentificador()));
			GestionEvaluados gEvaluados = new GestionEvaluados();
			setEvaluadosRestantes(listaEvaluadosAArreglo(obtenerEvaluadosNoEnProceso(
					gEvaluados
							.listarUsuariosBasicos(usuario.getIdentificador()),
					obtenerEvaluadosDeParticipacion(getParticipacionesProcesoEspecifico()))));
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO,
					"Participación del evaluado eliminada.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"La participación del evaluado no se pudo eliminar.", ""));
		}
		HttpSession session = request.getSession();
		session.removeAttribute("participacionEliminar");
		actualizarEvaluados();
	}

	public void guardarEditarProceso() {
		GestionProcesos gProcesos = new GestionProcesos();
		if (fechaFinal != null) {
			if (fechaInicial != null) {
				if (!fechaFinal.before(fechaInicial)) {
					proceso.setFechaFinalizacion(fechaFinal);
					proceso.setFechaInicio(fechaInicial);
				}
			}
		} else {
			if (fechaInicial != null) {
				proceso.setFechaInicio(fechaInicial);
			}
		}
		int result = gProcesos.editarProcesoUsuarioAdministrador(
				usuario.getIdentificador(), proceso);
		if (result == Convencion.CORRECTO) {
			proceso = gProcesos.consultarProcesoUsuarioAdministrador(
					usuario.getIdentificador(), proceso.getIdentificador());

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
		setEditar(false);
	}

	public void irAReportePDF() {
		System.out.println("Generando Reporte");
		ParticipacionEnProcesoBO par = (ParticipacionEnProcesoBO) dataTableResultados
				.getRowData();
		String url = "PDFReportServlet";
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ctx = context.getExternalContext();
		HttpSession sess = (HttpSession) ctx.getSession(true);
		List<ParticipacionEnProcesoBO> participaciones = new ArrayList<ParticipacionEnProcesoBO>();
		participaciones.add(par);
		sess.setAttribute("participaciones", participaciones);
		try {
			context.getExternalContext().dispatch(url);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			context.responseComplete();
		}
	}

	public void enviarNotificacion() {
		GestionUsos gUsos = new GestionUsos();
		if (gUsos.consultarCapacidadUsos(idUsuario, 1)) {
			ParticipacionEnProcesoBO par = (ParticipacionEnProcesoBO) dataTableEvaluados
					.getRowData();
			int result = SMTPSender.enviarCorreoParticipacionAProceso(par,
					usuario.getEmpresa());
			if (result == Convencion.CORRECTO) {
				GestionEvaluacion gEvaluacion = new GestionEvaluacion();
				par.setEstaNotificado(Convencion.ESTADO_NOTIFICACION_ENVIADA);
				gEvaluacion.editarParticipacionEnProceso(usuario
						.getIdentificador(), par.getUsuarioBasico()
						.getIdentificador(), par.getProcesoID(), par);
				setParticipacionesProcesoEspecifico(gEvaluacion
						.listarParticipacionesEnProceso(
								usuario.getIdentificador(),
								proceso.getIdentificador()));
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Notificación enviada.", ""));
			} else {
				// Mensaje para el feedback
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN,
						"La notificación no se pudo enviar.", ""));
			}
		} else {
			System.out.print("Revision de usos negativa.");
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"La notificacion no puede ser enviada.",
					"No dispone de los usos suficientes."));
		}
	}

	public void mostrarAgregarPrueba(AjaxBehaviorEvent event) {
		setAgregarPrueba(true);
		setCrearPrueba(false);
	}

	public void mostrarCrearPrueba(AjaxBehaviorEvent event) {
		setAgregarPrueba(false);
		setCrearPrueba(true);
	}

	public void mostrarAgregarEvaluado(AjaxBehaviorEvent event) {
		setAgregarEvaluado(true);
		setCrearEvaluado(false);
	}

	public void volverEvaluado() {
		setAgregarEvaluado(false);
		setCrearEvaluado(false);
	}

	public void volverPrueba() {
		setAgregarPrueba(false);
		setCrearPrueba(false);

	}

	public void mostrarCrearEvaluado(AjaxBehaviorEvent event) {
		setAgregarEvaluado(false);
		setCrearEvaluado(true);
	}

	public void crearEvaluadoEnProceso() {
		System.out.print("Creando evaluado...");

		GestionEvaluacion gEvaluacion = new GestionEvaluacion();
		GestionUsos gUsos = new GestionUsos();
		if (gUsos.consultarCapacidadUsos(idUsuario, 1)) {
			System.out.print("Revision de usos positiva...");

			EvaluadoBO eva = new EvaluadoBO();
			eva.setApellidos(apellidoEvaluadoCrear);
			eva.setCedula(cedulaEvaluadoCrear);
			eva.setNombres(nombreEvaluadoCrear);
			eva.setCorreoElectronico(correoEvaluadoCrear.toLowerCase());
			eva.setIdentificadorUsuarioAdministrador(usuario.getIdentificador());

			ParticipacionEnProcesoBO p = new ParticipacionEnProcesoBO();
			p.setUsuarioBasico(eva);
			p.setFechaFinalizacion(proceso.getFechaFinalizacion());
			p.setFechaInicio(new Date());
			p.setProcesoID(proceso.getIdentificador());
			p.setEstaNotificado(Convencion.ESTADO_NOTIFICACION_NO_ENVIADA);

			int result = gEvaluacion.agregarParticipacionEnProceso(usuario
					.getIdentificador(), p.getUsuarioBasico()
					.getIdentificador(), proceso.getIdentificador(), p);
			if (result == Convencion.CORRECTO) {
				System.out.println("Usuario creado...");

				setParticipacionesProcesoEspecifico(gEvaluacion
						.listarParticipacionesEnProceso(
								usuario.getIdentificador(),
								proceso.getIdentificador()));
				GestionEvaluados gEvaluados = new GestionEvaluados();
				setEvaluadosRestantes(listaEvaluadosAArreglo(obtenerEvaluadosNoEnProceso(
						gEvaluados.listarUsuariosBasicos(usuario
								.getIdentificador()),
						obtenerEvaluadosDeParticipacion(getParticipacionesProcesoEspecifico()))));
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_INFO, "Evaluado creado.", ""));
			} else {
				System.out.println("Usuario no creado...");
				// Mensaje para el feedback
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN,
						"El evaluado no se pudo crear.", ""));
			}
		} else {
			System.out.print("Revision de usos negativa.");
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"El evaluado no se pudo crear.",
					"No dispone de los usos suficientes."));
		}
		setApellidoEvaluadoCrear("");
		setCedulaEvaluadoCrear(0);
		setNombreEvaluadoCrear("");
		setCorreoEvaluadoCrear("");
		actualizarEvaluados();
	}

	public void crearPruebaEnProceso() {
		System.out.println("Creando prueba...");
		PruebaUsuarioBO prueba = new PruebaUsuarioBO();
		prueba.setNombre(nombrePruebaCrear);
		prueba.setDescripcion(descripcionPruebaCrear);
		GestionPruebas gPruebas = new GestionPruebas();
		int result = gPruebas.agregarPruebaAProceso(usuario.getIdentificador(),
				prueba, proceso);
		if (result == Convencion.CORRECTO) {
			GestionProcesos gProcesos = new GestionProcesos();
			ProcesoUsuarioBO proc = gProcesos
					.consultarProcesoUsuarioAdministrador(
							usuario.getIdentificador(),
							proceso.getIdentificador());
			List<PruebaUsuarioBO> pruebas = new ArrayList<PruebaUsuarioBO>();
			for (ProcesoUsuarioHasPruebaUsuarioBO pHas : proc
					.getProcesoUsuarioHasPruebaUsuario()) {
				pruebas.add(pHas.getPruebasUsuario());
			}
			setPruebasProcesoEspecifico(pruebas);
			setPruebasRestantes(listaPruebasAArreglo(obtenerPruebasNoEnProceso(
					gPruebas.listarPruebasUsuarioAdministrador(usuario
							.getIdentificador()), getPruebasProcesoEspecifico())));

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

	public void agregarEvaluadosAProceso() {
		GestionUsos gUsos = new GestionUsos();
		if (gUsos.consultarCapacidadUsos(idUsuario,
				selectItemsEvaluadosRestantes.length)) {
			GestionEvaluacion gEvaluacion = new GestionEvaluacion();
			int result = 0;
			for (Integer id : selectItemsEvaluadosRestantes) {
				EvaluadoBO eva = new EvaluadoBO();
				eva.setIdentificador(id);
				ParticipacionEnProcesoBO p = new ParticipacionEnProcesoBO();
				p.setUsuarioBasico(eva);
				p.setFechaFinalizacion(proceso.getFechaFinalizacion());
				p.setFechaInicio(new Date());
				p.setProcesoID(proceso.getIdentificador());
				p.setEstaNotificado(Convencion.ESTADO_NOTIFICACION_NO_ENVIADA);
				result = gEvaluacion.agregarParticipacionEnProceso(usuario
						.getIdentificador(), p.getUsuarioBasico()
						.getIdentificador(), proceso.getIdentificador(), p);
			}
			setParticipacionesProcesoEspecifico(gEvaluacion
					.listarParticipacionesEnProceso(usuario.getIdentificador(),
							proceso.getIdentificador()));
			GestionEvaluados gEvaluados = new GestionEvaluados();
			setEvaluadosRestantes(listaEvaluadosAArreglo(obtenerEvaluadosNoEnProceso(
					gEvaluados
							.listarUsuariosBasicos(usuario.getIdentificador()),
					obtenerEvaluadosDeParticipacion(getParticipacionesProcesoEspecifico()))));
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Evaluado agregado.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"El evaluado no se pudo agregar.",
					"No dispone de los usos suficientes."));
			actualizarEvaluados();
		}
	}

	public void agregarPruebasAProceso() {
		GestionPruebas gPruebas = new GestionPruebas();

		for (Integer id : selectItemsPruebasRestantes) {
			PruebaUsuarioBO prueba = new PruebaUsuarioBO();
			prueba.setIdentificador(id);
			gPruebas.agregarPruebaAProceso(usuario.getIdentificador(), prueba,
					proceso);
		}

		GestionProcesos gProcesos = new GestionProcesos();
		ProcesoUsuarioBO proc = gProcesos.consultarProcesoUsuarioAdministrador(
				usuario.getIdentificador(), proceso.getIdentificador());
		List<PruebaUsuarioBO> pruebas = new ArrayList<PruebaUsuarioBO>();
		for (ProcesoUsuarioHasPruebaUsuarioBO pHas : proc
				.getProcesoUsuarioHasPruebaUsuario()) {
			pruebas.add(pHas.getPruebasUsuario());
		}
		setPruebasProcesoEspecifico(pruebas);
		setPruebasRestantes(listaPruebasAArreglo(obtenerPruebasNoEnProceso(
				gPruebas.listarPruebasUsuarioAdministrador(idUsuario),
				getPruebasProcesoEspecifico())));
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Prueba agregada.", ""));
		actualizarPruebas();
	}

	private List<PruebaUsuarioBO> obtenerPruebasNoEnProceso(
			List<PruebaUsuarioBO> pruebas,
			List<PruebaUsuarioBO> listaPruebasDeProceso) {
		List<PruebaUsuarioBO> resultado = new ArrayList<PruebaUsuarioBO>();
		boolean continuar = true;
		if (pruebas != null) {
			for (int i = 0; i < pruebas.size(); i++) {
				for (int j = 0; j < listaPruebasDeProceso.size() && continuar; j++) {
					if (listaPruebasDeProceso.get(j).getIdentificador() == pruebas
							.get(i).getIdentificador()) {
						continuar = false;
					}
				}
				if (continuar) {
					resultado.add(pruebas.get(i));
				}
				continuar = true;
			}
		}
		return resultado;
	}

	private List<EvaluadoBO> obtenerEvaluadosNoEnProceso(
			List<EvaluadoBO> usuarios, List<EvaluadoBO> listaEvaluadosDeProceso) {
		List<EvaluadoBO> resultado = new ArrayList<EvaluadoBO>();
		boolean continuar = true;
		if (usuarios != null) {
			for (int i = 0; i < usuarios.size(); i++) {
				for (int j = 0; j < listaEvaluadosDeProceso.size() && continuar; j++) {
					if (listaEvaluadosDeProceso.get(j).getIdentificador() == usuarios
							.get(i).getIdentificador()) {
						continuar = false;
					}
				}
				if (continuar) {
					resultado.add(usuarios.get(i));
				}
				continuar = true;
			}
		}
		return resultado;
	}

	private List<EvaluadoBO> obtenerEvaluadosDeParticipacion(
			List<ParticipacionEnProcesoBO> result) {
		List<EvaluadoBO> resultado = new ArrayList<EvaluadoBO>();
		if (result != null) {
			for (ParticipacionEnProcesoBO participacionEnProcesoBO : result) {
				resultado.add(participacionEnProcesoBO.getUsuarioBasico());
			}
		}
		return resultado;
	}

	private PruebaUsuarioBO[] listaPruebasAArreglo(
			List<PruebaUsuarioBO> pruebasRestantes) {
		PruebaUsuarioBO[] pruebas = new PruebaUsuarioBO[pruebasRestantes.size()];
		for (int i = 0; i < pruebasRestantes.size(); i++) {
			pruebas[i] = pruebasRestantes.get(i);
		}
		return pruebas;
	}

	private EvaluadoBO[] listaEvaluadosAArreglo(
			List<EvaluadoBO> evaluadosRestantes) {
		EvaluadoBO[] evaluados = new EvaluadoBO[evaluadosRestantes.size()];
		for (int i = 0; i < evaluadosRestantes.size(); i++) {
			evaluados[i] = evaluadosRestantes.get(i);
		}
		return evaluados;
	}

	public void selectedPruebasChanged(ValueChangeEvent event) {
		Integer[] oldValue = (Integer[]) event.getOldValue();
		Integer[] newValue = (Integer[]) event.getNewValue();
		System.out.println(oldValue);
		System.out.println(newValue);
	}

	public void formatoFechaInicial(ValueChangeEvent e) {
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

	public void formatoFechaFinal(ValueChangeEvent e) {
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

	public void solicitarRevision() {
		proceso.setEstadoValoracion(Convencion.ESTADO_SOLICITUD_PENDIENTE);
		GestionProcesos gProcesos = new GestionProcesos();
		gProcesos.editarProcesoUsuarioAdministrador(usuario.getIdentificador(),
				proceso);
		int result = SMTPSender.enviarCorreoProcesoRevision(usuario, proceso);
		if (result == Convencion.CORRECTO) {

			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO,
					"Solicitud de revisión enviada.", ""));
		} else {
			// Mensaje para el feedback
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"La solicitud de revisión no pudo ser enviada.", ""));
		}
	}

	public void reporteExcel(ActionEvent event) {
		if (resultadosReporte != null) {
			if (resultadosReporte.size() > 0) {
				List<Integer> participaciones = new ArrayList<Integer>();

				for (ParticipacionEnProcesoBO item : resultadosProcesoEspecifico) {
					if (resultadosReporte.get(item.getIdentificador()) != null) {

						if (resultadosReporte.get(item.getIdentificador())) {
							participaciones.add(item.getIdentificador());
						}
					}
				}

				resultadosReporte.clear(); // If necessary.

				if (participaciones.isEmpty()) {
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_WARN,
									"Debe seleccionar los resultados para exportar",
									""));
				} else {

					String url = "ExcelReportServlet";
					FacesContext context = FacesContext.getCurrentInstance();
					ExternalContext ctx = context.getExternalContext();
					HttpSession sess = (HttpSession) ctx.getSession(true);
					sess.setAttribute("participaciones", participaciones);
					try {
						context.getExternalContext().dispatch(url);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						context.responseComplete();
					}
				}

			} else {
				// Mensaje para el feedback
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN,
						"Debe seleccionar los resultados para exportar", ""));

			}
		}
	}

	public void pruebasSiguientes() {
		if (!ultimaPaginaPruebas) {
			paginaActualPruebas++;
			actualizarPruebas();
		}
	}

	public void pruebasAnteriores() {
		if (!primeraPaginaPruebas) {
			paginaActualPruebas--;
			actualizarPruebas();
		}
	}

	private void actualizarPruebas() {
		pruebasAMostrar.clear();
		for (int i = 0; i < cantidadPruebasAMostrar; i++) {
			try {
				pruebasAMostrar.add(getPruebasProcesoEspecifico().get(
						paginaActualPruebas * cantidadPruebasAMostrar + i));

			} catch (IndexOutOfBoundsException e) {
				ultimaPaginaPruebas = true;
				primeraPaginaPruebas = false;
				break;
			} catch (NullPointerException e) {
				break;
			}
		}
		if (paginaActualPruebas <= 0) {
			ultimaPaginaPruebas = false;
			if (pruebasAMostrar.size() < cantidadPruebasAMostrar
					|| pruebasAMostrar.size() == pruebasProcesoEspecifico
							.size()) {
				ultimaPaginaPruebas = true;
			}
			primeraPaginaPruebas = true;
		} else if (paginaActualPruebas >= pruebasProcesoEspecifico.size()
				/ cantidadPruebasAMostrar) {
			ultimaPaginaPruebas = true;
			primeraPaginaPruebas = false;
		} else {
			ultimaPaginaPruebas = false;
			primeraPaginaPruebas = false;
		}
	}

	public void evaluadosSiguientes() {
		if (!ultimaPaginaEvaluados) {
			paginaActualEvaluados++;
			actualizarEvaluados();
		}
	}

	public void evaluadosAnteriores() {
		if (!primeraPaginaEvaluados) {
			paginaActualEvaluados--;
			actualizarEvaluados();
		}
	}

	private void actualizarEvaluados() {
		evaluadosAMostrar.clear();
		for (int i = 0; i < cantidadEvaluadosAMostrar; i++) {
			try {
				evaluadosAMostrar.add(getParticipacionesProcesoEspecifico()
						.get(paginaActualEvaluados * cantidadEvaluadosAMostrar
								+ i));

			} catch (IndexOutOfBoundsException e) {
				ultimaPaginaEvaluados = true;
				primeraPaginaEvaluados = false;
				break;
			} catch (NullPointerException e) {
				break;
			}
		}
		if (paginaActualEvaluados <= 0) {
			ultimaPaginaEvaluados = false;
			if (evaluadosAMostrar.size() < cantidadEvaluadosAMostrar
					|| evaluadosAMostrar.size() == participacionesProcesoEspecifico
							.size()) {
				ultimaPaginaEvaluados = true;
			}
			primeraPaginaEvaluados = true;
		} else if (paginaActualEvaluados >= participacionesProcesoEspecifico
				.size() / cantidadEvaluadosAMostrar) {
			ultimaPaginaEvaluados = true;
			primeraPaginaEvaluados = false;
		} else {
			ultimaPaginaEvaluados = false;
			primeraPaginaEvaluados = false;
		}
	}

	public void resultadosSiguientes() {
		if (!ultimaPaginaResultados) {
			paginaActualResultados++;
			actualizarResultados();
		}
	}

	public void resultadosAnteriores() {
		if (!primeraPaginaResultados) {
			paginaActualResultados--;
			actualizarResultados();
		}
	}

	private void actualizarResultados() {
		resultadosAMostrar.clear();
		for (int i = 0; i < cantidadEvaluadosAMostrar; i++) {
			try {
				resultadosAMostrar.add(getResultadosProcesoEspecifico()
						.get(paginaActualResultados
								* cantidadResultadosAMostrar + i));

			} catch (IndexOutOfBoundsException e) {
				ultimaPaginaResultados = true;
				primeraPaginaResultados = false;
				break;
			} catch (NullPointerException e) {
				break;
			}
		}
		if (paginaActualResultados <= 0) {
			ultimaPaginaResultados = false;
			if (resultadosAMostrar.size() < cantidadResultadosAMostrar
					|| resultadosAMostrar.size() == resultadosProcesoEspecifico
							.size()) {
				ultimaPaginaResultados = true;
			}
			primeraPaginaResultados = true;
		} else if (paginaActualResultados >= resultadosProcesoEspecifico.size()
				/ cantidadResultadosAMostrar) {
			ultimaPaginaResultados = true;
			primeraPaginaResultados = false;
		} else {
			ultimaPaginaResultados = false;
			primeraPaginaResultados = false;
		}
	}

	public void buscarPruebas() {
		pruebasRestantes = pruebasTemp;
		if (getParametroBusquedaPrueba() != null) {
			if (getParametroBusquedaPrueba() != "") {
				List<PruebaUsuarioBO> resultadoBusqueda = new ArrayList<PruebaUsuarioBO>();
				for (PruebaUsuarioBO prueba : getPruebasRestantes()) {
					String nombre = prueba.getNombre();
					if (nombre.toLowerCase().contains(
							getParametroBusquedaPrueba().toLowerCase())) {
						resultadoBusqueda.add(prueba);
					}
				}
				pruebasTemp = pruebasRestantes;
				PruebaUsuarioBO[] res = new PruebaUsuarioBO[resultadoBusqueda
						.size()];
				for (int i = 0; i < resultadoBusqueda.size(); i++) {
					res[i] = resultadoBusqueda.get(i);
				}
				pruebasRestantes = res;
			}
		}
	}

	public void buscarEvaluados() {
		evaluadosRestantes = evaluadosTemp;
		if (getParametroBusquedaEvaluado() != null) {
			if (getParametroBusquedaEvaluado() != "") {
				int cedula = 0;
				boolean esCedula = true;
				try {
					cedula = Integer.parseInt(getParametroBusquedaEvaluado());
				} catch (NumberFormatException e) {
					esCedula = false;
				}
				List<EvaluadoBO> resultadoBusqueda = new ArrayList<EvaluadoBO>();
				if (esCedula) {
					for (EvaluadoBO eval : getEvaluadosRestantes()) {
						int id = eval.getIdentificador();
						if (id == cedula) {
							resultadoBusqueda.add(eval);
						}
					}
				} else {
					for (EvaluadoBO eval : getEvaluadosRestantes()) {
						if (getParametroBusquedaEvaluado().equalsIgnoreCase(
								eval.getCorreoElectronico())
								|| eval.getNombres()
										.toLowerCase()
										.contains(
												getParametroBusquedaEvaluado()
														.toLowerCase())
								|| eval.getApellidos()
										.toLowerCase()
										.contains(
												getParametroBusquedaEvaluado()
														.toLowerCase())) {
							resultadoBusqueda.add(eval);
						}
					}
				}
				evaluadosTemp = evaluadosRestantes;
				EvaluadoBO[] res = new EvaluadoBO[resultadoBusqueda.size()];
				for (int i = 0; i < resultadoBusqueda.size(); i++) {
					res[i] = resultadoBusqueda.get(i);
				}
				evaluadosRestantes = res;
			}
		}
	}

	public List<PruebaUsuarioBO> getPruebasProcesoEspecifico() {
		return pruebasProcesoEspecifico;
	}

	public void setPruebasProcesoEspecifico(
			List<PruebaUsuarioBO> pruebasProcesoEspecifico) {
		this.pruebasProcesoEspecifico = pruebasProcesoEspecifico;
	}

	public List<ParticipacionEnProcesoBO> getParticipacionesProcesoEspecifico() {
		return participacionesProcesoEspecifico;
	}

	public void setParticipacionesProcesoEspecifico(
			List<ParticipacionEnProcesoBO> participacionesProcesoEspecifico) {
		this.participacionesProcesoEspecifico = participacionesProcesoEspecifico;
	}

	public List<ParticipacionEnProcesoBO> getResultadosProcesoEspecifico() {
		return resultadosProcesoEspecifico;
	}

	public void setResultadosProcesoEspecifico(
			List<ParticipacionEnProcesoBO> resultadosProcesoEspecifico) {
		this.resultadosProcesoEspecifico = resultadosProcesoEspecifico;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public boolean isEnEvaluados() {
		return enEvaluados;
	}

	public void setEnEvaluados(boolean enEvaluados) {
		this.enEvaluados = enEvaluados;
	}

	public boolean isEnPruebas() {
		return enPruebas;
	}

	public void setEnPruebas(boolean enPruebas) {
		this.enPruebas = enPruebas;
	}

	public boolean isEnResultados() {
		return enResultados;
	}

	public void setEnResultados(boolean enResultados) {
		this.enResultados = enResultados;
	}

	public UIForm getTableFormEvaluados() {
		return tableFormEvaluados;
	}

	public void setTableFormEvaluados(UIForm tableFormEvaluados) {
		this.tableFormEvaluados = tableFormEvaluados;
	}

	public HtmlDataTable getDataTableEvaluados() {
		return dataTableEvaluados;
	}

	public void setDataTableEvaluados(HtmlDataTable dataTableEvaluados) {
		this.dataTableEvaluados = dataTableEvaluados;
	}

	public UIForm getTableFormPruebas() {
		return tableFormPruebas;
	}

	public void setTableFormPruebas(UIForm tableFormPruebas) {
		this.tableFormPruebas = tableFormPruebas;
	}

	public HtmlDataTable getDataTablePruebas() {
		return dataTablePruebas;
	}

	public void setDataTablePruebas(HtmlDataTable dataTablePruebas) {
		this.dataTablePruebas = dataTablePruebas;
	}

	public UIForm getTableFormResultados() {
		return tableFormResultados;
	}

	public void setTableFormResultados(UIForm tableFormResultados) {
		this.tableFormResultados = tableFormResultados;
	}

	public HtmlDataTable getDataTableResultados() {
		return dataTableResultados;
	}

	public void setDataTableResultados(HtmlDataTable dataTableResultados) {
		this.dataTableResultados = dataTableResultados;
	}

	public String getNombreProceso() {
		return nombreProceso;
	}

	public void setNombreProceso(String nombreProceso) {
		this.nombreProceso = nombreProceso;
	}

	public String getDescripcionProceso() {
		return descripcionProceso;
	}

	public void setDescripcionProceso(String descripcionProceso) {
		this.descripcionProceso = descripcionProceso;
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}

	public ProcesoUsuarioBO getProceso() {
		return proceso;
	}

	public void setProceso(ProcesoUsuarioBO proceso) {
		this.proceso = proceso;
	}

	public boolean isAgregarPrueba() {
		return agregarPrueba;
	}

	public void setAgregarPrueba(boolean agregarPrueba) {
		this.agregarPrueba = agregarPrueba;
	}

	public boolean isCrearPrueba() {
		return crearPrueba;
	}

	public void setCrearPrueba(boolean crearPrueba) {
		this.crearPrueba = crearPrueba;
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

	public PruebaUsuarioBO[] getPruebasRestantes() {
		return pruebasRestantes;
	}

	public void setPruebasRestantes(PruebaUsuarioBO[] pruebasRestantes) {
		this.pruebasRestantes = pruebasRestantes;
	}

	public EvaluadoBO[] getEvaluadosRestantes() {
		return evaluadosRestantes;
	}

	public void setEvaluadosRestantes(EvaluadoBO[] evaluadosRestantes) {
		this.evaluadosRestantes = evaluadosRestantes;
	}

	public boolean isAgregarEvaluado() {
		return agregarEvaluado;
	}

	public void setAgregarEvaluado(boolean agregarEvaluado) {
		this.agregarEvaluado = agregarEvaluado;
	}

	public boolean isCrearEvaluado() {
		return crearEvaluado;
	}

	public void setCrearEvaluado(boolean crearEvaluado) {
		this.crearEvaluado = crearEvaluado;
	}

	public Integer[] getSelectItemsPruebasRestantes() {
		return selectItemsPruebasRestantes;
	}

	public void setSelectItemsPruebasRestantes(
			Integer[] selectItemsPruebasRestantes) {
		this.selectItemsPruebasRestantes = selectItemsPruebasRestantes;
	}

	public Integer[] getSelectItemsEvaluadosRestantes() {
		return selectItemsEvaluadosRestantes;
	}

	public void setSelectItemsEvaluadosRestantes(
			Integer[] selectItemsEvaluadosRestantes) {
		System.out.println(selectItemsEvaluadosRestantes);
		this.selectItemsEvaluadosRestantes = selectItemsEvaluadosRestantes;
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

	public Map<Integer, Boolean> getResultadosReporte() {
		return resultadosReporte;
	}

	public void setResultadosReporte(Map<Integer, Boolean> resultadosReporte) {
		this.resultadosReporte = resultadosReporte;
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

	public boolean isPrimeraPaginaPruebas() {
		return primeraPaginaPruebas;
	}

	public void setPrimeraPaginaPruebas(boolean primeraPaginaPruebas) {
		this.primeraPaginaPruebas = primeraPaginaPruebas;
	}

	public boolean isUltimaPaginaPruebas() {
		return ultimaPaginaPruebas;
	}

	public void setUltimaPaginaPruebas(boolean ultimaPaginaPruebas) {
		this.ultimaPaginaPruebas = ultimaPaginaPruebas;
	}

	public int getCantidadEvaluadosAMostrar() {
		return cantidadEvaluadosAMostrar;
	}

	public void setCantidadEvaluadosAMostrar(int cantidadEvaluadosAMostrar) {
		this.cantidadEvaluadosAMostrar = cantidadEvaluadosAMostrar;
	}

	public List<ParticipacionEnProcesoBO> getEvaluadosAMostrar() {
		return evaluadosAMostrar;
	}

	public void setEvaluadosAMostrar(
			List<ParticipacionEnProcesoBO> evaluadosAMostrar) {
		this.evaluadosAMostrar = evaluadosAMostrar;
	}

	public boolean isPrimeraPaginaEvaluados() {
		return primeraPaginaEvaluados;
	}

	public void setPrimeraPaginaEvaluados(boolean primeraPaginaEvaluados) {
		this.primeraPaginaEvaluados = primeraPaginaEvaluados;
	}

	public boolean isUltimaPaginaEvaluados() {
		return ultimaPaginaEvaluados;
	}

	public void setUltimaPaginaEvaluados(boolean ultimaPaginaEvaluados) {
		this.ultimaPaginaEvaluados = ultimaPaginaEvaluados;
	}

	public int getCantidadResultadosAMostrar() {
		return cantidadResultadosAMostrar;
	}

	public void setCantidadResultadosAMostrar(int cantidadResultadosAMostrar) {
		this.cantidadResultadosAMostrar = cantidadResultadosAMostrar;
	}

	public List<ParticipacionEnProcesoBO> getResultadosAMostrar() {
		return resultadosAMostrar;
	}

	public void setResultadosAMostrar(
			List<ParticipacionEnProcesoBO> resultadosAMostrar) {
		this.resultadosAMostrar = resultadosAMostrar;
	}

	public boolean isPrimeraPaginaResultados() {
		return primeraPaginaResultados;
	}

	public void setPrimeraPaginaResultados(boolean primeraPaginaResultados) {
		this.primeraPaginaResultados = primeraPaginaResultados;
	}

	public boolean isUltimaPaginaResultados() {
		return ultimaPaginaResultados;
	}

	public void setUltimaPaginaResultados(boolean ultimaPaginaResultados) {
		this.ultimaPaginaResultados = ultimaPaginaResultados;
	}

	public String getParametroBusquedaPrueba() {
		return parametroBusquedaPrueba;
	}

	public void setParametroBusquedaPrueba(String parametrBusquedaPrueba) {
		this.parametroBusquedaPrueba = parametrBusquedaPrueba;
	}

	public String getParametroBusquedaEvaluado() {
		return parametroBusquedaEvaluado;
	}

	public void setParametroBusquedaEvaluado(String parametroBusquedaEvaluado) {
		this.parametroBusquedaEvaluado = parametroBusquedaEvaluado;
	}

}
