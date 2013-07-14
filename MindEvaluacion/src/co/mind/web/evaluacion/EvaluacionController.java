package co.mind.web.evaluacion;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import co.mind.management.shared.dto.EvaluadoBO;
import co.mind.management.shared.dto.ParticipacionEnProcesoBO;
import co.mind.management.shared.dto.PreguntaUsuarioBO;
import co.mind.management.shared.dto.ProcesoUsuarioBO;
import co.mind.management.shared.dto.PruebaUsuarioBO;
import co.mind.management.shared.dto.ResultadoBO;
import co.mind.management.shared.persistencia.GestionEvaluacion;
import co.mind.management.shared.persistencia.GestionPruebas;
import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;

@ManagedBean
@ViewScoped
public class EvaluacionController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String respuesta;
	private String pregunta;
	private int caracteresMaximo;
	private int tiempoMaximo;
	private int tiempoActual;
	private int tiempoRepresentado;
	private String unidadRepresentada;
	private String imagenInstruccion;

	private boolean enPregunta;

	private List<PreguntaUsuarioBO> listaPregunta;
	private PreguntaUsuarioBO preguntaActual;

	private ParticipacionEnProcesoBO participacionEnProceso;
	private boolean nuevo = true;
	private boolean continuar = true;
	private static HashMap<PruebaUsuarioBO, List<PreguntaUsuarioBO>> preguntasPorPrueba;
	private static Entry<PruebaUsuarioBO, List<PreguntaUsuarioBO>> categoriaActual;
	private static Iterator<Entry<PruebaUsuarioBO, List<PreguntaUsuarioBO>>> iterador;

	@PostConstruct
	public void init() {
		verificarLogin();
		if (continuar) {

			comenzarPrueba();
		}
	}

	private void verificarLogin() {
		HttpServletRequest request = MindHelper.obtenerRequest();
		HttpServletResponse response = MindHelper.obtenerResponse();

		participacionEnProceso = (ParticipacionEnProcesoBO) ((HttpServletRequest) request)
				.getSession().getAttribute(Convencion.CLAVE_EVALUACION);

		System.out.println("Verificando usuario...");
		if (participacionEnProceso == null
				|| !(participacionEnProceso instanceof ParticipacionEnProcesoBO)) {
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
			if (!sid.equalsIgnoreCase(participacionEnProceso.getSesionID())) {
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

	private void comenzarPrueba() {
		GestionEvaluacion gEvaluacion = new GestionEvaluacion();
		if (participacionEnProceso.getEstado().equalsIgnoreCase(
				Convencion.ESTADO_PARTICIPACION_EN_PROCESO_EN_EJECUCION)) {
			nuevo = false;
		}
		EvaluadoBO usuarioBasico = participacionEnProceso.getUsuarioBasico();
		int result = Convencion.CORRECTO;
		if (nuevo) {
			System.out.println("Es primera vez.");
			participacionEnProceso
					.setEstado(Convencion.ESTADO_PARTICIPACION_EN_PROCESO_EN_EJECUCION);
			gEvaluacion.editarParticipacionEnProceso(
					usuarioBasico.getIdentificadorUsuarioAdministrador(),
					usuarioBasico.getIdentificador(),
					participacionEnProceso.getProcesoID(),
					participacionEnProceso);
			result = gEvaluacion.decrementarCantidadDeUsosUsuarios(
					participacionEnProceso.getIdentificador(),
					participacionEnProceso.getProcesoID());
			setImagenInstruccion("instrucciones.png");
		} else {
			System.out.println("No es primera vez.");
			setImagenInstruccion("cambioTema.png");
		}
		if (result == Convencion.CORRECTO) {
			List<PreguntaUsuarioBO> preguntas = null;
			GestionPruebas gPruebas = new GestionPruebas();
			ProcesoUsuarioBO proceso = new ProcesoUsuarioBO();
			proceso.setIdentificador(participacionEnProceso.getProcesoID());
			if (nuevo) {
				preguntas = gPruebas.listarPreguntasPorProceso(
						usuarioBasico.getIdentificadorUsuarioAdministrador(),
						proceso.getIdentificador());
			} else {
				preguntas = gPruebas.listarPreguntasPorProcesoRestantes(
						usuarioBasico.getIdentificador(),
						usuarioBasico.getIdentificadorUsuarioAdministrador(),
						proceso.getIdentificador());
			}
			obtenerListaPreguntasPorCategoria(preguntas);
			mostrarInstruccion();
		} else {
			terminarPrueba();
		}

	}

	private void mostrarPregunta() {
		try {
			setImagenInstruccion("cambioTema.png");
			preguntaActual = listaPregunta.remove(0);
			setPregunta(preguntaActual.getPregunta());
			setCaracteresMaximo(preguntaActual.getCaracteresMaximo());
			setTiempoMaximo(preguntaActual.getTiempoMaximo());
			setTiempoActual(preguntaActual.getTiempoMaximo());
			if (tiempoActual / 60 > 0) {
				// Representar Minutos
				setTiempoRepresentado(tiempoActual / 60);
				setUnidadRepresentada("min");
			} else {
				// Representar Segundos
				setTiempoRepresentado(tiempoActual);
				setUnidadRepresentada("seg");
			}
			setRespuesta("");
			setEnPregunta(true);
		} catch (IndexOutOfBoundsException e) {
			terminarPrueba();
		}
	}

	public void guardarResultado() {
		System.out.print("Guardando resultado...");
		ResultadoBO resultado = new ResultadoBO();
		resultado.setRespuesta(respuesta);
		resultado.setPreguntasUsuario(preguntaActual);
		resultado.setParticipacionEnProceso(participacionEnProceso);
		GestionEvaluacion gEvaluacion = new GestionEvaluacion();
		EvaluadoBO usuarioBasico = participacionEnProceso.getUsuarioBasico();
		int r = gEvaluacion.agregarResultadoParticipacionEnProceso(
				usuarioBasico.getIdentificadorUsuarioAdministrador(),
				usuarioBasico.getIdentificador(), resultado
						.getParticipacionEnProceso().getProcesoID(), resultado
						.getParticipacionEnProceso().getIdentificador(),
				resultado);
		if (r == Convencion.CORRECTO) {
			System.out.println("OK");
		} else {
			System.out.println("FAIL");
		}
		mostrarPregunta();

	}

	public void actualizarTiempo() {
		tiempoActual--;
		if (tiempoActual / 60 > 0) {
			// Representar Minutos
			setTiempoRepresentado(tiempoActual / 60);
			setUnidadRepresentada("min");
		} else {
			// Representar Segundos
			setTiempoRepresentado(tiempoActual);
			setUnidadRepresentada("seg");
		}
	}

	public void cerrarInstruccion(ActionEvent event) {
		listaPregunta = categoriaActual.getValue();
		mostrarPregunta();
	}

	private void terminarPrueba() {
		try {
			categoriaActual = iterador.next();
			mostrarInstruccion();

		} catch (NoSuchElementException exc) {

			GestionEvaluacion gEvaluacion = new GestionEvaluacion();
			participacionEnProceso
					.setEstado(Convencion.ESTADO_PARTICIPACION_EN_PROCESO_INACTIVA);
			EvaluadoBO usuarioBasico = participacionEnProceso
					.getUsuarioBasico();
			int result = gEvaluacion.editarParticipacionEnProceso(
					usuarioBasico.getIdentificadorUsuarioAdministrador(),
					usuarioBasico.getIdentificador(),
					participacionEnProceso.getProcesoID(),
					participacionEnProceso);
			cerrarSesion();
		} catch (Exception ex) {
			cerrarSesion();
		}
	}

	private void cerrarSesion() {
		HttpServletRequest httpServletRequest = MindHelper.obtenerRequest();
		HttpSession session = httpServletRequest.getSession();
		session.removeAttribute(Convencion.CLAVE_EVALUACION);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		NavigationHandler navigationHandler = facesContext.getApplication()
				.getNavigationHandler();
		navigationHandler.handleNavigation(facesContext, null, "end");
	}

	private void mostrarInstruccion() {
		setEnPregunta(false);
	}

	private void obtenerListaPreguntasPorCategoria(
			List<PreguntaUsuarioBO> listaPreguntas) {
		preguntasPorPrueba = new HashMap<PruebaUsuarioBO, List<PreguntaUsuarioBO>>();
		List<PreguntaUsuarioBO> preguntasTemporales = new ArrayList<PreguntaUsuarioBO>();
		for (PreguntaUsuarioBO pregunta : listaPreguntas) {
			if (preguntasPorPrueba.containsKey(pregunta.getPruebaUsuario())) {
				preguntasPorPrueba.get(pregunta.getPruebaUsuario()).add(
						pregunta);
			} else {
				preguntasTemporales = new ArrayList<PreguntaUsuarioBO>();
				preguntasTemporales.add(pregunta);
				preguntasPorPrueba.put(pregunta.getPruebaUsuario(),
						preguntasTemporales);
			}

		}
		iterador = preguntasPorPrueba.entrySet().iterator();
		categoriaActual = iterador.next();
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public String getPregunta() {
		return pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

	public int getCaracteresMaximo() {
		return caracteresMaximo;
	}

	public void setCaracteresMaximo(int caracteresMaximo) {
		this.caracteresMaximo = caracteresMaximo;
	}

	public int getTiempoMaximo() {
		return tiempoMaximo;
	}

	public void setTiempoMaximo(int tiempoMaximo) {
		this.tiempoMaximo = tiempoMaximo;
	}

	public boolean isEnPregunta() {
		return enPregunta;
	}

	public void setEnPregunta(boolean enPregunta) {
		this.enPregunta = enPregunta;
	}

	public String getImagenInstruccion() {
		return imagenInstruccion;
	}

	public void setImagenInstruccion(String imagenInstruccion) {
		this.imagenInstruccion = imagenInstruccion;
	}

	public PreguntaUsuarioBO getPreguntaActual() {
		return preguntaActual;
	}

	public void setPreguntaActual(PreguntaUsuarioBO preguntaActual) {
		this.preguntaActual = preguntaActual;
	}

	public int getTiempoActual() {
		return tiempoActual;
	}

	public void setTiempoActual(int tiempoActual) {
		this.tiempoActual = tiempoActual;
	}

	public int getTiempoRepresentado() {
		return tiempoRepresentado;
	}

	public void setTiempoRepresentado(int tiempoRepresentado) {
		this.tiempoRepresentado = tiempoRepresentado;
	}

	public String getUnidadRepresentada() {
		return unidadRepresentada;
	}

	public void setUnidadRepresentada(String unidadRepresentada) {
		this.unidadRepresentada = unidadRepresentada;
	}

}
