package co.mind.web.pruebas;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.mind.management.shared.dto.ImagenUsuarioBO;
import co.mind.management.shared.dto.PreguntaUsuarioBO;
import co.mind.management.shared.dto.ProcesoUsuarioBO;
import co.mind.management.shared.dto.PruebaUsuarioBO;
import co.mind.management.shared.dto.UsuarioBO;
import co.mind.management.shared.persistencia.GestionLaminas;
import co.mind.management.shared.persistencia.GestionPreguntas;
import co.mind.management.shared.persistencia.GestionPruebas;
import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.MindHelper;

@ViewScoped
public class PruebaEspecificaController {

	private UsuarioBO usuario;
	private String nombreUsuario;
	private boolean continuar = true;
	private PruebaUsuarioBO prueba;
	private ProcesoUsuarioBO proceso;
	private String nombrePrueba;
	private String nombreProceso;
	private boolean mostrarBreadcrumbProceso;

	private List<PreguntaUsuarioBO> preguntas;
	private PreguntaUsuarioBO pregunta;

	private List<ImagenUsuarioBO> imagenes;
	private ImagenUsuarioBO imagen;
	private String preguntaCrear;
	private int tiempoCrear;
	private int caracteresCrear;

	@PostConstruct
	public void init() {
		verificarLogin();
		if (continuar) {
			setNombreUsuario(usuario.getNombres() + " "
					+ usuario.getApellidos());
			prueba = obtenerPruebaDeSesion();
			if (prueba == null) {
				HttpServletResponse response = MindHelper.obtenerResponse();
				try {
					response.sendRedirect("pruebas.do");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				setNombrePrueba(prueba.getNombre());
				proceso = obtenerProcesoDeSesion();
				if (proceso != null) {
					setNombreProceso(proceso.getNombre());
					setMostrarBreadcrumbProceso(true);
				} else {
					setMostrarBreadcrumbProceso(false);
				}
				GestionPruebas gPreguntas = new GestionPruebas();
				setPreguntas(gPreguntas.listarPreguntasPrueba(
						usuario.getIdentificador(), prueba.getIdentificador()));
				GestionLaminas gImagen = new GestionLaminas();
				setImagenes(gImagen.listarLaminasUsuarioAdministrador(usuario
						.getIdentificador()));
			}
		}
	}

	public void preRenderPruebaEspecifica() {
		prueba = obtenerPruebaDeSesion();
		if (prueba == null) {
			HttpServletResponse response = MindHelper.obtenerResponse();
			try {
				response.sendRedirect("pruebas.do");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			setNombrePrueba(prueba.getNombre());
			proceso = obtenerProcesoDeSesion();
			if (proceso != null) {
				setNombreProceso(proceso.getNombre());
				setMostrarBreadcrumbProceso(true);
			} else {
				setMostrarBreadcrumbProceso(false);
			}
			GestionPruebas gPreguntas = new GestionPruebas();
			setPreguntas(gPreguntas.listarPreguntasPrueba(
					usuario.getIdentificador(), prueba.getIdentificador()));
			GestionLaminas gImagen = new GestionLaminas();
			setImagenes(gImagen.listarLaminasUsuarioAdministrador(usuario
					.getIdentificador()));
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

	private PruebaUsuarioBO obtenerPruebaDeSesion() {
		HttpServletRequest request = MindHelper.obtenerRequest();
		return (PruebaUsuarioBO) ((HttpServletRequest) request).getSession()
				.getAttribute("prueba");

	}

	private ProcesoUsuarioBO obtenerProcesoDeSesion() {
		HttpServletRequest request = MindHelper.obtenerRequest();
		return (ProcesoUsuarioBO) ((HttpServletRequest) request).getSession()
				.getAttribute("proceso");

	}

	public String irAPruebaEspecifica() {
		return "prueba";
	}

	public String irAProcesoEspecifico() {
		return "proceso";
	}

	public void mostrarInformacionPregunta(
			javax.faces.event.AjaxBehaviorEvent event) {
		pregunta = (PreguntaUsuarioBO) event.getComponent().getAttributes()
				.get("pregunta");
	}

	public void actualizarImagenModal(javax.faces.event.AjaxBehaviorEvent event) {
		setImagen((ImagenUsuarioBO) event.getComponent().getAttributes()
				.get("imagen"));
	}

	public String crearPregunta() {
		PreguntaUsuarioBO pr = new PreguntaUsuarioBO();
		pr.setCaracteresMaximo(caracteresCrear);
		pr.setTiempoMaximo(tiempoCrear);
		pr.setPregunta(preguntaCrear);
		pr.setImagenesUsuarioID(imagen);
		pr.setPruebaUsuario(prueba);
		GestionPreguntas gPreguntas = new GestionPreguntas();
		int result = gPreguntas.agregarPreguntaUsuarioAdministrador(
				usuario.getIdentificador(), pr, prueba);
		if (result == Convencion.CORRECTO) {
			GestionPruebas gPruebas = new GestionPruebas();
			setPreguntas(gPruebas.listarPreguntasPrueba(
					usuario.getIdentificador(), prueba.getIdentificador()));
		}
		return null;
	}

	public void eliminarPregunta(ActionEvent event) {
		GestionPreguntas gPreguntas = new GestionPreguntas();
		int result = gPreguntas.eliminarPreguntaUsuarioAdministrador(
				usuario.getIdentificador(), pregunta.getIdentificador());
		if (result == Convencion.CORRECTO) {
			GestionPruebas gPruebas = new GestionPruebas();
			setPreguntas(gPruebas.listarPreguntasPrueba(
					usuario.getIdentificador(), prueba.getIdentificador()));
			pregunta = null;
		}
	}

	public void editarPregunta(ActionEvent event) {
		GestionPreguntas gPreguntas = new GestionPreguntas();
		int result = gPreguntas.editarPreguntaUsuarioAdministrador(
				usuario.getIdentificador(), pregunta);
		if (result == Convencion.CORRECTO) {
			GestionPruebas gPruebas = new GestionPruebas();
			setPreguntas(gPruebas.listarPreguntasPrueba(
					usuario.getIdentificador(), prueba.getIdentificador()));
		}
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getNombrePrueba() {
		return nombrePrueba;
	}

	public void setNombrePrueba(String nombrePrueba) {
		this.nombrePrueba = nombrePrueba;
	}

	public String getNombreProceso() {
		return nombreProceso;
	}

	public void setNombreProceso(String nombreProceso) {
		this.nombreProceso = nombreProceso;
	}

	public boolean isMostrarBreadcrumbProceso() {
		return mostrarBreadcrumbProceso;
	}

	public void setMostrarBreadcrumbProceso(boolean mostrarBreadcrumbProceso) {
		this.mostrarBreadcrumbProceso = mostrarBreadcrumbProceso;
	}

	public List<PreguntaUsuarioBO> getPreguntas() {
		return preguntas;
	}

	public void setPreguntas(List<PreguntaUsuarioBO> preguntas) {
		this.preguntas = preguntas;
	}

	public PreguntaUsuarioBO getPregunta() {
		return pregunta;
	}

	public void setPregunta(PreguntaUsuarioBO pregunta) {
		this.pregunta = pregunta;
	}

	public List<ImagenUsuarioBO> getImagenes() {
		return imagenes;
	}

	public void setImagenes(List<ImagenUsuarioBO> imagenes) {
		this.imagenes = imagenes;
	}

	public ImagenUsuarioBO getImagen() {
		return imagen;
	}

	public void setImagen(ImagenUsuarioBO imagen) {
		this.imagen = imagen;
	}

	public String getPreguntaCrear() {
		return preguntaCrear;
	}

	public void setPreguntaCrear(String preguntaCrear) {
		this.preguntaCrear = preguntaCrear;
	}

	public int getTiempoCrear() {
		return tiempoCrear;
	}

	public void setTiempoCrear(int tiempoCrear) {
		this.tiempoCrear = tiempoCrear;
	}

	public int getCaracteresCrear() {
		return caracteresCrear;
	}

	public void setCaracteresCrear(int caracteresCrear) {
		this.caracteresCrear = caracteresCrear;
	}

}
