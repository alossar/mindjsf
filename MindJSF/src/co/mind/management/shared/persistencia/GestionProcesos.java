package co.mind.management.shared.persistencia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import co.mind.management.shared.dto.ImagenBO;
import co.mind.management.shared.dto.ImagenUsuarioBO;
import co.mind.management.shared.dto.PreguntaUsuarioBO;
import co.mind.management.shared.dto.ProcesoUsuarioBO;
import co.mind.management.shared.dto.ProcesoUsuarioHasPruebaUsuarioBO;
import co.mind.management.shared.dto.PruebaUsuarioBO;
import co.mind.management.shared.dto.UsuarioBO;
import co.mind.management.shared.entidades.ParticipacionEnProceso;
import co.mind.management.shared.entidades.PreguntaUsuario;
import co.mind.management.shared.entidades.ProcesoUsuario;
import co.mind.management.shared.entidades.ProcesoUsuarioHasPruebaUsuario;
import co.mind.management.shared.entidades.PruebaUsuario;
import co.mind.management.shared.entidades.Usuario;
import co.mind.management.shared.persistencia.interfaces.IGestionProcesos;
import co.mind.management.shared.recursos.Convencion;

public class GestionProcesos implements IGestionProcesos {

	public EntityManagerFactory emf = null;
	public EntityManager entityManager = null;
	public static final String JTA_PU_NAME = Convencion.JTA_PU_NAME;

	public GestionProcesos() {
		emf = Persistence.createEntityManagerFactory(JTA_PU_NAME);
		entityManager = emf.createEntityManager();
	}

	@Override
	public int agregarProcesoUsuarioAdministrador(int usuarioAdministradorID,
			ProcesoUsuarioBO proceso) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			ProcesoUsuario proc = new ProcesoUsuario();
			proc.setDescripcion(proceso.getDescripcion());
			proc.setEstadoValoracion(proceso.getEstadoValoracion());
			proc.setFechaCreacion(proceso.getFechaCreacion());
			proc.setFechaFinalizacion(proceso.getFechaFinalizacion());
			proc.setFechaInicio(proceso.getFechaInicio());
			proc.setNombre(proceso.getNombre());
			proc.setNotificacionEnviada(proceso.getNotificacionEnviada());
			Usuario user = entityManager.find(Usuario.class,
					usuarioAdministradorID);
			proc.setUsuario(user);
			System.out.println("Agregando");
			if (!entityManager.contains(proc)) {
				entityManager.persist(proc);
				entityManager.flush();
				userTransaction.commit();
				entityManager.refresh(proc);
				return Convencion.CORRECTO;
			} else {
				return Convencion.INCORRECTO;
			}
		} catch (Exception exception) {
			// Exception has occurred, roll-back the transaction.
			userTransaction.rollback();
			exception.printStackTrace();
			return Convencion.INCORRECTO;
		}
	}

	@Override
	public int editarProcesoUsuarioAdministrador(int usuarioAdministradorID,
			ProcesoUsuarioBO proceso) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			ProcesoUsuario proc = entityManager.find(ProcesoUsuario.class,
					proceso.getIdentificador());
			proc.setDescripcion(proceso.getDescripcion());
			proc.setEstadoValoracion(proceso.getEstadoValoracion());
			proc.setFechaFinalizacion(proceso.getFechaFinalizacion());
			proc.setFechaInicio(proceso.getFechaInicio());
			proc.setNombre(proceso.getNombre());
			proc.setEstadoValoracion(proceso.getEstadoValoracion());
			proc.setNotificacionEnviada(proceso.getNotificacionEnviada());
			entityManager.merge(proc);
			entityManager.flush();
			userTransaction.commit();
			return Convencion.CORRECTO;
		} catch (Exception exception) {
			// Exception has occurred, roll-back the transaction.
			exception.printStackTrace();
			userTransaction.rollback();
			return Convencion.INCORRECTO;
		}
	}

	@Override
	public ProcesoUsuarioBO consultarProcesoUsuarioAdministrador(
			int usuarioAdministradorID, int procesoID) {
		ProcesoUsuario proceso = entityManager.find(ProcesoUsuario.class,
				procesoID);
		if (proceso == null) {
			return null;
		} else {
			entityManager.refresh(proceso);
			ProcesoUsuarioBO resultado = new ProcesoUsuarioBO();

			UsuarioBO usuario = new UsuarioBO();
			usuario.setIdentificador(proceso.getUsuario().getIdentificador());
			usuario.setNombres(proceso.getUsuario().getNombres());
			usuario.setApellidos(proceso.getUsuario().getApellidos());
			usuario.setEmpresa(proceso.getUsuario().getEmpresa());

			resultado.setUsuario(usuario);

			resultado.setDescripcion(proceso.getDescripcion());
			resultado.setEstadoValoracion(proceso.getEstadoValoracion());
			resultado.setFechaCreacion(proceso.getFechaCreacion());
			resultado.setFechaFinalizacion(proceso.getFechaFinalizacion());
			resultado.setFechaInicio(proceso.getFechaInicio());
			resultado.setIdentificador(proceso.getIdentificador());
			resultado.setNombre(proceso.getNombre());
			resultado.setNotificacionEnviada(proceso.getNotificacionEnviada());
			List<ProcesoUsuarioHasPruebaUsuario> pruebas = proceso
					.getProcesosUsuariosHasPruebasUsuarios();
			List<ProcesoUsuarioHasPruebaUsuarioBO> pruebasUsuarioBOs = new ArrayList<>();
			for (ProcesoUsuarioHasPruebaUsuario pruebaUsuario : pruebas) {
				entityManager.refresh(pruebaUsuario);
				ProcesoUsuarioHasPruebaUsuarioBO pHas = new ProcesoUsuarioHasPruebaUsuarioBO();
				pHas.setIdentificador(pruebaUsuario.getIdentificador());
				PruebaUsuarioBO prueba = new PruebaUsuarioBO();
				prueba.setDescripcion(pruebaUsuario.getPruebasUsuario()
						.getDescripcion());
				prueba.setIdentificador(pruebaUsuario.getPruebasUsuario()
						.getIdentificador());
				prueba.setNombre(pruebaUsuario.getPruebasUsuario().getNombre());
				prueba.setUsuarioAdministradorID(pruebaUsuario
						.getPruebasUsuario().getUsuario().getIdentificador());
				List<PreguntaUsuario> preguntas = pruebaUsuario
						.getPruebasUsuario().getPreguntasUsuarios();
				List<PreguntaUsuarioBO> preguntasBO = new ArrayList<>();
				for (PreguntaUsuario pre : preguntas) {
					entityManager.refresh(pre);
					PreguntaUsuarioBO pregunta = new PreguntaUsuarioBO();
					pregunta.setCaracteresMaximo(pre.getCaracteresMaximo());
					pregunta.setIdentificador(pre.getIdentificador());
					pregunta.setPregunta(pre.getPregunta());
					pregunta.setTiempoMaximo(pre.getTiempoMaximo());
					ImagenUsuarioBO imagen = new ImagenUsuarioBO();
					imagen.setIdentificador(pre.getImagenesUsuario()
							.getIdentificador());
					imagen.setUsuario(pre.getImagenesUsuario().getUsuario()
							.getIdentificador());
					ImagenBO ima = new ImagenBO();
					ima.setIdentificador(pre.getImagenesUsuario().getImagene()
							.getIdentificador());
					ima.setImagenURI(pre.getImagenesUsuario().getImagene()
							.getImagenURI());
					imagen.setImagene(ima);
					pregunta.setImagenesUsuarioID(imagen);
					preguntasBO.add(pregunta);
				}
				prueba.setPreguntas(preguntasBO);
				pHas.setPruebasUsuario(prueba);
				pruebasUsuarioBOs.add(pHas);
			}
			resultado.setPruebasUsuarioID(pruebasUsuarioBOs);
			return resultado;
		}
	}

	@Override
	public int eliminarProcesoUsuarioAdministrador(int usuarioAdministradorID,
			int procesoID) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			ProcesoUsuario im = entityManager.find(ProcesoUsuario.class,
					procesoID);
			if (im != null) {
				List<ProcesoUsuarioHasPruebaUsuario> p = im
						.getProcesosUsuariosHasPruebasUsuarios();
				List<ParticipacionEnProceso> participaciones = im
						.getParticipacionEnProcesos();
				if (p != null) {
					for (ProcesoUsuarioHasPruebaUsuario procesoUsuarioHasPruebaUsuario : p) {
						userTransaction.begin();
						entityManager.remove(procesoUsuarioHasPruebaUsuario);
						entityManager.flush();
						userTransaction.commit();
					}
				}
				if (participaciones != null) {
					for (ParticipacionEnProceso participacionEnProceso : participaciones) {
						userTransaction.begin();
						entityManager.remove(participacionEnProceso);
						entityManager.flush();
						userTransaction.commit();
					}
				}
				userTransaction.begin();
				entityManager.remove(im);
				entityManager.flush();
				userTransaction.commit();
				return Convencion.CORRECTO;
			} else {
				return Convencion.INCORRECTO;
			}
		} catch (Exception exception) {
			// Exception has occurred, roll-back the transaction.
			exception.printStackTrace();
			userTransaction.rollback();
			return Convencion.INCORRECTO;
		}
	}

	@Override
	public List<ProcesoUsuarioBO> listarProcesoUsuarioAdministrador(
			int usuarioAdministradorID) {
		Usuario user = entityManager
				.find(Usuario.class, usuarioAdministradorID);
		if (user != null) {
			entityManager.refresh(user);
			List<ProcesoUsuarioBO> lista = new ArrayList<ProcesoUsuarioBO>();
			for (int i = 0; i < user.getProcesosUsuarios().size(); i++) {
				ProcesoUsuario im = user.getProcesosUsuarios().get(i);
				entityManager.refresh(im);
				ProcesoUsuarioBO resultado = new ProcesoUsuarioBO();

				UsuarioBO usuario = new UsuarioBO();
				usuario.setIdentificador(im.getUsuario().getIdentificador());
				usuario.setNombres(im.getUsuario().getNombres());
				usuario.setApellidos(im.getUsuario().getApellidos());
				usuario.setEmpresa(im.getUsuario().getEmpresa());

				resultado.setUsuario(usuario);

				resultado.setDescripcion(im.getDescripcion());
				resultado.setEstadoValoracion(im.getEstadoValoracion());
				resultado.setFechaCreacion(im.getFechaCreacion());
				resultado.setFechaFinalizacion(im.getFechaFinalizacion());
				resultado.setFechaInicio(im.getFechaInicio());
				resultado.setIdentificador(im.getIdentificador());
				resultado.setNombre(im.getNombre());
				resultado.setNotificacionEnviada(im.getNotificacionEnviada());
				List<ProcesoUsuarioHasPruebaUsuario> pruebas = im
						.getProcesosUsuariosHasPruebasUsuarios();
				List<ProcesoUsuarioHasPruebaUsuarioBO> pruebasUsuarioBOs = new ArrayList<>();
				for (int j = 0; j < pruebas.size(); j++) {
					ProcesoUsuarioHasPruebaUsuario pHas = pruebas.get(j);
					ProcesoUsuarioHasPruebaUsuarioBO pHasBO = new ProcesoUsuarioHasPruebaUsuarioBO();
					pHasBO.setIdentificador(pHas.getIdentificador());
					PruebaUsuario pruebaUsuario = pHas.getPruebasUsuario();
					entityManager.refresh(pruebaUsuario);
					PruebaUsuarioBO pruebaBO = new PruebaUsuarioBO();
					entityManager.refresh(im);

					pruebaBO.setDescripcion(pruebaUsuario.getDescripcion());
					pruebaBO.setIdentificador(pruebaUsuario.getIdentificador());
					pruebaBO.setNombre(pruebaUsuario.getNombre());
					pruebaBO.setUsuarioAdministradorID(pruebaUsuario
							.getUsuario().getIdentificador());
					List<PreguntaUsuario> preguntas = pruebaUsuario
							.getPreguntasUsuarios();
					List<PreguntaUsuarioBO> preguntasBO = new ArrayList<>();
					for (int k = 0; k < preguntas.size(); k++) {
						PreguntaUsuario pre = preguntas.get(k);
						entityManager.refresh(pre);
						PreguntaUsuarioBO pregunta = new PreguntaUsuarioBO();
						pregunta.setCaracteresMaximo(pre.getCaracteresMaximo());
						pregunta.setIdentificador(pre.getIdentificador());
						pregunta.setPregunta(pre.getPregunta());
						pregunta.setTiempoMaximo(pre.getTiempoMaximo());
						ImagenUsuarioBO imagen = new ImagenUsuarioBO();
						imagen.setIdentificador(pre.getImagenesUsuario()
								.getIdentificador());
						imagen.setUsuario(pre.getImagenesUsuario().getUsuario()
								.getIdentificador());
						ImagenBO ima = new ImagenBO();
						ima.setIdentificador(pre.getImagenesUsuario()
								.getImagene().getIdentificador());
						ima.setImagenURI(pre.getImagenesUsuario().getImagene()
								.getImagenURI());
						imagen.setImagene(ima);
						pregunta.setImagenesUsuarioID(imagen);
						preguntasBO.add(pregunta);

					}
					pruebaBO.setPreguntas(preguntasBO);
					pHasBO.setPruebasUsuario(pruebaBO);
					pruebasUsuarioBOs.add(pHasBO);
				}
				resultado.setPruebasUsuarioID(pruebasUsuarioBOs);
				lista.add(resultado);
			}
			return lista;
		} else {
			return null;
		}
	}

	public int agregarProcesoConPruebas(
			int identificador,
			ProcesoUsuarioBO proceso,
			List<ProcesoUsuarioHasPruebaUsuarioBO> procesoUsuarioHasPruebaUsuario) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			ProcesoUsuario proc = new ProcesoUsuario();
			proc.setDescripcion(proceso.getDescripcion());
			proc.setEstadoValoracion(proceso.getEstadoValoracion());
			proc.setFechaCreacion(new Date());
			proc.setFechaFinalizacion(proceso.getFechaFinalizacion());
			proc.setFechaInicio(proceso.getFechaInicio());
			proc.setNombre(proceso.getNombre());
			proc.setNotificacionEnviada(proceso.getNotificacionEnviada());
			Usuario user = entityManager.find(Usuario.class, identificador);
			proc.setUsuario(user);
			System.out.println("Agregando");
			if (!entityManager.contains(proc)) {
				entityManager.persist(proc);
				entityManager.flush();
				userTransaction.commit();
				entityManager.refresh(proc);
				proceso.setIdentificador(proc.getIdentificador());
				GestionPruebas gPruebas = new GestionPruebas();
				for (ProcesoUsuarioHasPruebaUsuarioBO procesoUsuarioHasPruebaUsuarioBO : procesoUsuarioHasPruebaUsuario) {
					gPruebas.agregarPruebaAProceso(identificador,
							procesoUsuarioHasPruebaUsuarioBO
									.getPruebasUsuario(), proceso);
				}
				return Convencion.CORRECTO;
			} else {
				return Convencion.INCORRECTO;
			}
		} catch (Exception exception) {
			// Exception has occurred, roll-back the transaction.
			userTransaction.rollback();
			exception.printStackTrace();
			return Convencion.INCORRECTO;
		}
	}

	public List<ProcesoUsuarioBO> listarProcesosParaRevisar() {
		String query = "SELECT DISTINCT(u) FROM ProcesoUsuario u WHERE (u.estadoValoracion = :pendiente OR u.estadoValoracion = :aceptada OR u.estadoValoracion = :realizada) ";
		Query q = entityManager.createQuery(query);
		q.setParameter("pendiente", Convencion.ESTADO_SOLICITUD_PENDIENTE);
		q.setParameter("aceptada", Convencion.ESTADO_SOLICITUD_ACEPTADA);
		q.setParameter("realizada", Convencion.ESTADO_SOLICITUD_REALIZADA);
		List<ProcesoUsuario> usuarios = q.getResultList();
		if (usuarios != null) {
			if (usuarios.size() > 0) {
				List<ProcesoUsuarioBO> lista = new ArrayList<ProcesoUsuarioBO>();
				for (int i = 0; i < usuarios.size(); i++) {
					ProcesoUsuario im = usuarios.get(i);
					entityManager.refresh(im);
					ProcesoUsuarioBO resultado = new ProcesoUsuarioBO();

					UsuarioBO usuario = new UsuarioBO();
					usuario.setIdentificador(im.getUsuario().getIdentificador());
					usuario.setNombres(im.getUsuario().getNombres());
					usuario.setApellidos(im.getUsuario().getApellidos());
					usuario.setEmpresa(im.getUsuario().getEmpresa());

					resultado.setUsuario(usuario);

					resultado.setDescripcion(im.getDescripcion());
					resultado.setEstadoValoracion(im.getEstadoValoracion());
					resultado.setFechaCreacion(im.getFechaCreacion());
					resultado.setFechaFinalizacion(im.getFechaFinalizacion());
					resultado.setFechaInicio(im.getFechaInicio());
					resultado.setIdentificador(im.getIdentificador());
					resultado.setNombre(im.getNombre());
					resultado.setNotificacionEnviada(im
							.getNotificacionEnviada());
					List<ProcesoUsuarioHasPruebaUsuario> pruebas = im
							.getProcesosUsuariosHasPruebasUsuarios();
					List<ProcesoUsuarioHasPruebaUsuarioBO> pruebasUsuarioBOs = new ArrayList<>();
					for (int j = 0; j < pruebas.size(); j++) {
						ProcesoUsuarioHasPruebaUsuario pruebaUsuario = pruebas
								.get(j);
						ProcesoUsuarioHasPruebaUsuarioBO pHas = new ProcesoUsuarioHasPruebaUsuarioBO();
						pHas.setIdentificador(pruebaUsuario.getIdentificador());
						PruebaUsuarioBO prueba = new PruebaUsuarioBO();
						prueba.setDescripcion(pruebaUsuario.getPruebasUsuario()
								.getDescripcion());
						prueba.setIdentificador(pruebaUsuario
								.getPruebasUsuario().getIdentificador());
						prueba.setNombre(pruebaUsuario.getPruebasUsuario()
								.getNombre());
						prueba.setUsuarioAdministradorID(pruebaUsuario
								.getPruebasUsuario().getUsuario()
								.getIdentificador());
						List<PreguntaUsuario> preguntas = pruebaUsuario
								.getPruebasUsuario().getPreguntasUsuarios();
						List<PreguntaUsuarioBO> preguntasBO = new ArrayList<>();
						for (int k = 0; k < preguntas.size(); k++) {
							PreguntaUsuario pre = preguntas.get(k);
							entityManager.refresh(pre);
							PreguntaUsuarioBO pregunta = new PreguntaUsuarioBO();
							pregunta.setCaracteresMaximo(pre
									.getCaracteresMaximo());
							pregunta.setIdentificador(pre.getIdentificador());
							pregunta.setPregunta(pre.getPregunta());
							pregunta.setTiempoMaximo(pre.getTiempoMaximo());
							ImagenUsuarioBO imagen = new ImagenUsuarioBO();
							imagen.setIdentificador(pre.getImagenesUsuario()
									.getIdentificador());
							imagen.setUsuario(pre.getImagenesUsuario()
									.getUsuario().getIdentificador());
							ImagenBO ima = new ImagenBO();
							ima.setIdentificador(pre.getImagenesUsuario()
									.getImagene().getIdentificador());
							ima.setImagenURI(pre.getImagenesUsuario()
									.getImagene().getImagenURI());
							imagen.setImagene(ima);
							pregunta.setImagenesUsuarioID(imagen);
							preguntasBO.add(pregunta);

						}
						prueba.setPreguntas(preguntasBO);
						pHas.setPruebasUsuario(prueba);
						pruebasUsuarioBOs.add(pHas);
					}
					resultado.setPruebasUsuarioID(pruebasUsuarioBOs);
					lista.add(resultado);
				}
				return lista;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public int eliminarPruebaDeProceso(int identificador,
			PruebaUsuarioBO pruebaEliminar, ProcesoUsuarioBO procesoBO) {
		ProcesoUsuario proceso = entityManager.find(ProcesoUsuario.class,
				procesoBO.getIdentificador());
		if (proceso == null) {
			return Convencion.INCORRECTO;
		} else {
			entityManager.refresh(proceso);

			List<ProcesoUsuarioHasPruebaUsuario> pruebas = proceso
					.getProcesosUsuariosHasPruebasUsuarios();
			List<ProcesoUsuarioHasPruebaUsuarioBO> pruebasUsuarioBOs = new ArrayList<>();
			for (ProcesoUsuarioHasPruebaUsuario pruebaUsuario : pruebas) {
				entityManager.refresh(pruebaUsuario);
				if (pruebaEliminar.getIdentificador() == pruebaUsuario
						.getPruebasUsuario().getIdentificador()) {
					EntityTransaction userTransaction = entityManager
							.getTransaction();
					try {
						userTransaction.begin();
						entityManager.remove(pruebaUsuario);
						entityManager.flush();
						userTransaction.commit();
						return Convencion.CORRECTO;
					} catch (Exception exception) {
						// Exception has occurred, roll-back the transaction.
						exception.printStackTrace();
						userTransaction.rollback();
						return Convencion.INCORRECTO;
					}
				}
			}
		}
		return Convencion.INCORRECTO;
	}
}
