package co.mind.web.demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import co.mind.management.shared.dto.PreguntaUsuarioBO;
import co.mind.management.shared.dto.PruebaUsuarioBO;
import co.mind.management.shared.dto.ResultadoBO;
import co.mind.management.shared.persistencia.GestionPruebas;
import co.mind.management.shared.recursos.SMTPSender;

@ManagedBean
@ViewScoped
public class DemoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String demo;
	private boolean enPregunta;
	private String respuesta;
	private String pregunta;
	private int caracteresMaximo;
	private int tiempoMaximo;
	private int tiempoActual;
	private String tiempoRepresentado;
	private String unidadRepresentada;
	private String imagenInstruccion;
	private List<PreguntaUsuarioBO> listaPregunta;
	private PreguntaUsuarioBO preguntaActual;
	static List<ResultadoBO> resultados;

	@PostConstruct
	public void init() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		demo = (String) facesContext.getExternalContext()
				.getRequestParameterMap().get("demo");
		System.out.println(demo);

		GestionPruebas gPruebas = new GestionPruebas();
		if (demo == null) {
			demo = "demoa";
		}
		List<PreguntaUsuarioBO> result = gPruebas.listarPreguntasDemo(demo);
		if (result != null) {
			listaPregunta = result;
			mostrarInstruccion();
		} else {
			NavigationHandler navigationHandler = facesContext.getApplication()
					.getNavigationHandler();
			navigationHandler.handleNavigation(facesContext, null, "end");
		}
		setImagenInstruccion("instrucciones.png");
		resultados = new ArrayList<ResultadoBO>();
	}

	public void guardarResultado() {

		System.out.print("Guardando resultado...");
		ResultadoBO resultado = new ResultadoBO();
		resultado.setRespuesta(respuesta);
		resultado.setPreguntasUsuario(preguntaActual);
		resultados.add(resultado);

		mostrarPregunta();
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
				setTiempoRepresentado(tiempoActual / 60 + ":" + tiempoActual
						% 60);
				setUnidadRepresentada("min");
			} else {
				// Representar Segundos
				setTiempoRepresentado(tiempoActual + "");
				setUnidadRepresentada("seg");
			}
			setRespuesta("");
			setEnPregunta(true);
		} catch (IndexOutOfBoundsException e) {
			terminarPrueba();
		}
	}

	private void mostrarInstruccion() {
		setEnPregunta(false);
	}

	public void actualizarTiempo() {
		tiempoActual--;
		if (tiempoActual / 60 > 0) {
			// Representar Minutos
			setTiempoRepresentado(tiempoActual / 60 + ":" + tiempoActual % 60);
			setUnidadRepresentada("min");
		} else {
			// Representar Segundos
			setTiempoRepresentado(tiempoActual + "");
			setUnidadRepresentada("seg");
		}
	}

	public void cerrarInstruccion(ActionEvent event) {
		mostrarPregunta();
	}

	public void terminarPrueba() {
		SMTPSender.enviarResultados(resultados);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		NavigationHandler navigationHandler = facesContext.getApplication()
				.getNavigationHandler();
		navigationHandler.handleNavigation(facesContext, null, "end");
	}

	public boolean isEnPregunta() {
		return enPregunta;
	}

	public void setEnPregunta(boolean enPregunta) {
		this.enPregunta = enPregunta;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public int getTiempoMaximo() {
		return tiempoMaximo;
	}

	public void setTiempoMaximo(int tiempoMaximo) {
		this.tiempoMaximo = tiempoMaximo;
	}

	public int getTiempoActual() {
		return tiempoActual;
	}

	public void setTiempoActual(int tiempoActual) {
		this.tiempoActual = tiempoActual;
	}

	public String getTiempoRepresentado() {
		return tiempoRepresentado;
	}

	public void setTiempoRepresentado(String tiempoRepresentado) {
		this.tiempoRepresentado = tiempoRepresentado;
	}

	public String getUnidadRepresentada() {
		return unidadRepresentada;
	}

	public void setUnidadRepresentada(String unidadRepresentada) {
		this.unidadRepresentada = unidadRepresentada;
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

	public String getImagenInstruccion() {
		return imagenInstruccion;
	}

	public void setImagenInstruccion(String imagenInstruccion) {
		this.imagenInstruccion = imagenInstruccion;
	}

	public String getDemo() {
		return demo;
	}

	public void setDemo(String demo) {
		this.demo = demo;
	}

	public PreguntaUsuarioBO getPreguntaActual() {
		return preguntaActual;
	}

	public void setPreguntaActual(PreguntaUsuarioBO preguntaActual) {
		this.preguntaActual = preguntaActual;
	}

}
