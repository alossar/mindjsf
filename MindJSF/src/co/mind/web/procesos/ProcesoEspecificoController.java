package co.mind.web.procesos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
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
import co.mind.management.shared.persistencia.GestionEvaluacion;
import co.mind.management.shared.persistencia.GestionEvaluados;
import co.mind.management.shared.persistencia.GestionProcesos;
import co.mind.management.shared.persistencia.GestionPruebas;
import co.mind.management.shared.persistencia.GestionUsos;
import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;
import co.mind.management.shared.recursos.SMTPSender;

public class ProcesoEspecificoController {

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

	private UIForm tableFormEvaluados;
	private HtmlDataTable dataTableEvaluados;
	private UIForm tableFormPruebas;
	private HtmlDataTable dataTablePruebas;
	private UIForm tableFormResultados;
	private HtmlDataTable dataTableResultados;

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

	@PostConstruct
	public void init() {
		verificarLogin();
		if (continuar) {
			setEnEvaluados(false);
			setEnPruebas(true);
			setEnResultados(false);

			setNombreUsuario(usuario.getNombres() + " "
					+ usuario.getApellidos());
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
						gPruebas.listarPruebasUsuarioAdministrador(usuario
								.getIdentificador()),
						getPruebasProcesoEspecifico())));
				setEvaluadosRestantes(listaEvaluadosAArreglo(obtenerEvaluadosNoEnProceso(
						gEvaluados.listarUsuariosBasicos(usuario
								.getIdentificador()),
						obtenerEvaluadosDeParticipacion(getParticipacionesProcesoEspecifico()))));
				setEditar(false);
				setAgregarPrueba(false);
				setCrearPrueba(false);
				setAgregarEvaluado(false);
				setCrearEvaluado(false);
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

	public String verParticipaciones() {
		setEnEvaluados(true);
		setEnPruebas(false);
		setEnResultados(false);
		return null;
	}

	public String verPruebas() {
		setEnEvaluados(false);
		setEnPruebas(true);
		setEnResultados(false);
		return null;
	}

	public String verResultados() {
		setEnEvaluados(false);
		setEnPruebas(false);
		setEnResultados(true);
		return null;
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

	public String editarProceso() {
		setEditar(true);
		return null;
	}

	public String guardarEditarProceso() {
		GestionProcesos gProcesos = new GestionProcesos();
		int result = gProcesos.editarProcesoUsuarioAdministrador(
				usuario.getIdentificador(), proceso);
		if (result == Convencion.CORRECTO) {
			proceso = gProcesos.consultarProcesoUsuarioAdministrador(
					usuario.getIdentificador(), proceso.getIdentificador());

		}
		setEditar(false);
		return null;
	}

	public String cancelarEditarProceso() {
		setEditar(false);
		return null;
	}

	public String irAReportePDF() {
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
		return null;
	}

	public String enviarNotificacion() {
		ParticipacionEnProcesoBO par = (ParticipacionEnProcesoBO) dataTableEvaluados
				.getRowData();
		int result = SMTPSender.enviarCorreoParticipacionAProceso(par,
				usuario.getEmpresa());
		if (result == Convencion.CORRECTO) {
			GestionEvaluacion gEvaluacion = new GestionEvaluacion();
			par.setEstaNotificado(Convencion.ESTADO_NOTIFICACION_ENVIADA);
			gEvaluacion.editarParticipacionEnProceso(
					usuario.getIdentificador(), par.getUsuarioBasico()
							.getIdentificador(), par.getProcesoID(), par);
			setParticipacionesProcesoEspecifico(gEvaluacion
					.listarParticipacionesEnProceso(usuario.getIdentificador(),
							proceso.getIdentificador()));
		}
		return null;
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

	public void crearEvaluadoEnProceso(AjaxBehaviorEvent event) {
		System.out.println("Creando evaluado");
		EvaluadoBO eva = new EvaluadoBO();
		eva.setApellidos(apellidoEvaluadoCrear);
		eva.setCedula(cedulaEvaluadoCrear);
		eva.setNombres(nombreEvaluadoCrear);
		eva.setCorreoElectronico(correoEvaluadoCrear);
		eva.setIdentificadorUsuarioAdministrador(usuario.getIdentificador());

		ParticipacionEnProcesoBO p = new ParticipacionEnProcesoBO();
		p.setUsuarioBasico(eva);
		p.setFechaFinalizacion(proceso.getFechaFinalizacion());
		p.setFechaInicio(proceso.getFechaInicio());
		p.setProcesoID(proceso.getIdentificador());
		p.setEstaNotificado(Convencion.ESTADO_NOTIFICACION_NO_ENVIADA);

		GestionEvaluacion gEvaluacion = new GestionEvaluacion();
		GestionUsos gUsos = new GestionUsos();
		if (gUsos.consultarCapacidadUsos(usuario.getIdentificador(), 1)) {
			int result = gEvaluacion.agregarParticipacionEnProceso(usuario
					.getIdentificador(), p.getUsuarioBasico()
					.getIdentificador(), proceso.getIdentificador(), p);
			if (result == Convencion.CORRECTO) {
				setParticipacionesProcesoEspecifico(gEvaluacion
						.listarParticipacionesEnProceso(
								usuario.getIdentificador(),
								proceso.getIdentificador()));
			}
		}
	}

	public void crearPruebaEnProceso(AjaxBehaviorEvent event) {
		System.out.println("Creando prueba");
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
		}
	}

	public String agregarEvaluadosAProceso() {
		GestionUsos gUsos = new GestionUsos();
		if (gUsos.consultarCapacidadUsos(usuario.getIdentificador(),
				selectItemsEvaluadosRestantes.length)) {
			GestionEvaluacion gEvaluacion = new GestionEvaluacion();
			for (Integer id : selectItemsEvaluadosRestantes) {
				EvaluadoBO eva = new EvaluadoBO();
				eva.setIdentificador(id);
				ParticipacionEnProcesoBO p = new ParticipacionEnProcesoBO();
				p.setUsuarioBasico(eva);
				p.setFechaFinalizacion(proceso.getFechaFinalizacion());
				p.setFechaInicio(proceso.getFechaInicio());
				p.setProcesoID(proceso.getIdentificador());
				p.setEstaNotificado(Convencion.ESTADO_NOTIFICACION_NO_ENVIADA);
				gEvaluacion.agregarParticipacionEnProceso(usuario
						.getIdentificador(), p.getUsuarioBasico()
						.getIdentificador(), proceso.getIdentificador(), p);
			}
			setParticipacionesProcesoEspecifico(gEvaluacion
					.listarParticipacionesEnProceso(usuario.getIdentificador(),
							proceso.getIdentificador()));
		}
		return null;
	}

	public String agregarPruebasAProceso() {
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
		return null;
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
		System.out.println(selectItemsPruebasRestantes);
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

}
