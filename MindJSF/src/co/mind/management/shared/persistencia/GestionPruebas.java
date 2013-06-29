package co.mind.management.shared.persistencia;

import java.util.ArrayList;
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
import co.mind.management.shared.dto.PruebaUsuarioBO;
import co.mind.management.shared.entidades.Evaluado;
import co.mind.management.shared.entidades.PreguntaUsuario;
import co.mind.management.shared.entidades.ProcesoUsuario;
import co.mind.management.shared.entidades.ProcesoUsuarioHasPruebaUsuario;
import co.mind.management.shared.entidades.PruebaUsuario;
import co.mind.management.shared.entidades.Usuario;
import co.mind.management.shared.persistencia.interfaces.IGestionPruebas;
import co.mind.management.shared.recursos.Convencion;

public class GestionPruebas implements IGestionPruebas {

	public EntityManagerFactory emf = null;
	public EntityManager entityManager = null;
	public static final String JTA_PU_NAME = Convencion.JTA_PU_NAME;

	public GestionPruebas() {
		emf = Persistence.createEntityManagerFactory(JTA_PU_NAME);
		entityManager = emf.createEntityManager();
	}

	@Override
	public int agregarPruebaUsuarioAdministrador(int usuarioAdministradorID,
			PruebaUsuarioBO prueba) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			Usuario usuario = entityManager.find(Usuario.class,
					usuarioAdministradorID);
			PruebaUsuario c = new PruebaUsuario();
			c.setUsuario(usuario);
			c.setDescripcion(prueba.getDescripcion());
			c.setNombre(prueba.getNombre());
			if (!entityManager.contains(c)) {
				entityManager.persist(c);
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
	public int agregarPruebaUsuarioAdministrador(int usuarioAdministradorID,
			PruebaUsuarioBO prueba, List<PreguntaUsuarioBO> preguntas) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			Usuario usuario = entityManager.find(Usuario.class,
					usuarioAdministradorID);
			PruebaUsuario c = new PruebaUsuario();
			c.setUsuario(usuario);
			c.setDescripcion(prueba.getDescripcion());
			c.setNombre(prueba.getNombre());
			if (!entityManager.contains(c)) {
				entityManager.persist(c);
				entityManager.flush();
				userTransaction.commit();
				entityManager.refresh(c);
				GestionPreguntas gPreguntas = new GestionPreguntas();
				for (PreguntaUsuarioBO preguntaBO : preguntas) {
					prueba.setIdentificador(c.getIdentificador());
					gPreguntas.agregarPreguntaUsuarioAdministrador(
							usuarioAdministradorID, preguntaBO, prueba);
				}
				return Convencion.CORRECTO;
			} else {
				return Convencion.INCORRECTO;
			}
		} catch (Exception exception) {
			// Exception has occurred, roll-back the transaction.
			userTransaction.rollback();
			return Convencion.INCORRECTO;
		}
	}

	@Override
	public int editarPruebaUsuarioAdministrador(int usuarioAdministradorID,
			PruebaUsuarioBO prueba) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			Usuario usuario = entityManager.find(Usuario.class,
					usuarioAdministradorID);
			PruebaUsuario c = entityManager.find(PruebaUsuario.class,
					prueba.getIdentificador());
			c.setUsuario(usuario);
			c.setDescripcion(prueba.getDescripcion());
			c.setNombre(prueba.getNombre());
			entityManager.merge(c);
			entityManager.flush();
			userTransaction.commit();
			return Convencion.CORRECTO;
		} catch (Exception e) {
			userTransaction.rollback();
			return Convencion.INCORRECTO;
		}
	}

	@Override
	public PruebaUsuarioBO consultarPruebaUsuarioAdministrador(
			int usuarioAdministradorID, int pruebaID) {
		PruebaUsuario prueba = entityManager
				.find(PruebaUsuario.class, pruebaID);
		if (prueba == null) {
			return null;
		} else {
			PruebaUsuarioBO resultado = new PruebaUsuarioBO();
			resultado.setDescripcion(prueba.getDescripcion());
			resultado.setIdentificador(prueba.getIdentificador());
			resultado.setNombre(prueba.getNombre());
			resultado.setUsuarioAdministradorID(usuarioAdministradorID);
			List<PreguntaUsuario> preguntas = prueba.getPreguntasUsuarios();
			List<PreguntaUsuarioBO> preguntasBO = new ArrayList<>();
			for (PreguntaUsuario pre : preguntas) {
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
			resultado.setPreguntas(preguntasBO);
			return resultado;
		}
	}

	@Override
	public int eliminarPruebaUsuarioAdministrador(int usuarioAdministradorID,
			int pruebaID) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			PruebaUsuario cat = entityManager.find(PruebaUsuario.class,
					pruebaID);
			if (cat != null) {
				List<ProcesoUsuarioHasPruebaUsuario> p = cat
						.getProcesosUsuariosHasPruebasUsuarios();
				List<PreguntaUsuario> preguntas = cat.getPreguntasUsuarios();
				if (p != null) {
					for (ProcesoUsuarioHasPruebaUsuario procesoUsuarioHasPruebaUsuario : p) {
						userTransaction.begin();
						entityManager.remove(procesoUsuarioHasPruebaUsuario);
						entityManager.flush();
						userTransaction.commit();
					}
				}
				if (cat.getPreguntasUsuarios() != null) {
					for (PreguntaUsuario preguntaUsuario : preguntas) {
						userTransaction.begin();
						entityManager.remove(preguntaUsuario);
						entityManager.flush();
						userTransaction.commit();
					}
				}
				userTransaction.begin();
				entityManager.remove(cat);
				entityManager.flush();
				userTransaction.commit();
				return Convencion.CORRECTO;
			} else {
				return Convencion.INCORRECTO;
			}
		} catch (Exception e) {
			userTransaction.rollback();
			return Convencion.INCORRECTO;
		}
	}

	public List<PruebaUsuarioBO> listarPruebasPorNombreParcial(int usuarioID,
			String keyword) {
		Usuario user = entityManager.find(Usuario.class, usuarioID);
		if (user != null) {
			entityManager.refresh(user);
			String query = "SELECT DISTINCT(u) FROM PruebaUsuario u WHERE u.usuario =:user AND u.nombre LIKE :keyword";
			Query q = entityManager.createQuery(query);
			q.setParameter("user", user);
			q.setParameter("keyword", "%" + keyword + "%");
			List<PruebaUsuario> usuarios = q.getResultList();
			if (usuarios != null) {
				List<PruebaUsuarioBO> lista = new ArrayList<PruebaUsuarioBO>();
				for (int i = 0; i < usuarios.size(); i++) {
					PruebaUsuario prueba = usuarios.get(i);
					PruebaUsuarioBO resultado = new PruebaUsuarioBO();
					resultado.setDescripcion(prueba.getDescripcion());
					resultado.setIdentificador(prueba.getIdentificador());
					resultado.setNombre(prueba.getNombre());
					resultado.setUsuarioAdministradorID(usuarioID);
					List<PreguntaUsuario> preguntas = prueba
							.getPreguntasUsuarios();
					List<PreguntaUsuarioBO> preguntasBO = new ArrayList<>();
					for (PreguntaUsuario pre : preguntas) {
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
					resultado.setPreguntas(preguntasBO);
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

	@Override
	public List<PruebaUsuarioBO> listarPruebasUsuarioAdministrador(
			int usuarioAdministradorID) {
		Usuario user = entityManager
				.find(Usuario.class, usuarioAdministradorID);
		if (user != null) {
			entityManager.refresh(user);
			List<PruebaUsuarioBO> lista = new ArrayList<PruebaUsuarioBO>();
			for (int i = 0; i < user.getPruebasUsuarios().size(); i++) {
				PruebaUsuario prueba = user.getPruebasUsuarios().get(i);
				PruebaUsuarioBO resultado = new PruebaUsuarioBO();
				resultado.setDescripcion(prueba.getDescripcion());
				resultado.setIdentificador(prueba.getIdentificador());
				resultado.setNombre(prueba.getNombre());
				resultado.setUsuarioAdministradorID(usuarioAdministradorID);
				List<PreguntaUsuario> preguntas = prueba.getPreguntasUsuarios();
				List<PreguntaUsuarioBO> preguntasBO = new ArrayList<>();
				for (PreguntaUsuario pre : preguntas) {
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
				resultado.setPreguntas(preguntasBO);
				lista.add(resultado);
			}
			return lista;
		} else {
			return null;
		}
	}

	@Override
	public int eliminarPreguntaDePruebaUsuarioAdministrador(
			int usuarioAdministradorID, int preguntaCategoriaEnPruebaID) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			PreguntaUsuario p = entityManager.find(PreguntaUsuario.class,
					preguntaCategoriaEnPruebaID);
			entityManager.remove(p);
			entityManager.flush();
			userTransaction.commit();
			return Convencion.CORRECTO;
		} catch (Exception e) {
			userTransaction.rollback();
			return Convencion.INCORRECTO;
		}
	}

	@Override
	public List<PreguntaUsuarioBO> listarPreguntasPorProceso(
			int usuarioAdministradorID, int procesoID) {
		ProcesoUsuario proceso = entityManager.find(ProcesoUsuario.class,
				procesoID);
		if (proceso != null) {
			entityManager.refresh(proceso);
			List<ProcesoUsuarioHasPruebaUsuario> procesoUsuarioHasPruebaUsuario = proceso
					.getProcesosUsuariosHasPruebasUsuarios();
			List<PruebaUsuario> pruebasProceso = new ArrayList<PruebaUsuario>();
			for (ProcesoUsuarioHasPruebaUsuario procesoUsuarioHasPruebaUsuario2 : procesoUsuarioHasPruebaUsuario) {
				entityManager.refresh(procesoUsuarioHasPruebaUsuario2);
				pruebasProceso.add(procesoUsuarioHasPruebaUsuario2
						.getPruebasUsuario());
			}
			List<PreguntaUsuario> preguntas = new ArrayList<>();
			for (PruebaUsuario pruebaUsuario : pruebasProceso) {
				preguntas.addAll(pruebaUsuario.getPreguntasUsuarios());
			}
			if (preguntas.size() > 0) {
				List<PreguntaUsuarioBO> lista = new ArrayList<PreguntaUsuarioBO>();
				for (int i = 0; i < preguntas.size(); i++) {
					PreguntaUsuario pregunta = preguntas.get(i);
					entityManager.refresh(pregunta);

					PruebaUsuarioBO prueba = new PruebaUsuarioBO();
					prueba.setDescripcion(pregunta.getPruebasUsuario()
							.getDescripcion());
					prueba.setIdentificador(pregunta.getPruebasUsuario()
							.getIdentificador());
					prueba.setNombre(pregunta.getPruebasUsuario().getNombre());
					prueba.setUsuarioAdministradorID(pregunta
							.getPruebasUsuario().getUsuario()
							.getIdentificador());

					PreguntaUsuarioBO preguntaUsuario = new PreguntaUsuarioBO();
					preguntaUsuario.setCaracteresMaximo(pregunta
							.getCaracteresMaximo());
					preguntaUsuario.setIdentificador(pregunta
							.getIdentificador());
					preguntaUsuario.setPosicionPreguntaX(pregunta
							.getPosicionPreguntaX());
					preguntaUsuario.setPosicionPreguntaY(pregunta
							.getPosicionPreguntaY());
					preguntaUsuario.setOrden(pregunta.getOrden());
					preguntaUsuario.setPregunta(pregunta.getPregunta());
					preguntaUsuario.setTiempoMaximo(pregunta.getTiempoMaximo());
					ImagenUsuarioBO imagen = new ImagenUsuarioBO();
					imagen.setIdentificador(pregunta.getImagenesUsuario()
							.getIdentificador());
					ImagenBO img = new ImagenBO();
					img.setIdentificador(pregunta.getImagenesUsuario()
							.getImagene().getIdentificador());
					img.setImagenURI(pregunta.getImagenesUsuario().getImagene()
							.getImagenURI());
					imagen.setImagene(img);
					imagen.setUsuario(usuarioAdministradorID);
					preguntaUsuario.setImagenesUsuarioID(imagen);
					preguntaUsuario.setPruebaUsuario(prueba);
					lista.add(preguntaUsuario);
				}
				return lista;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public List<PreguntaUsuarioBO> listarPreguntasRestantesPrueba(
			int usuarioAdministradorID, int procesoID, int usuarioBasicoID) {

		Evaluado eval = entityManager.find(Evaluado.class, usuarioBasicoID);
		ProcesoUsuario proce = entityManager.find(ProcesoUsuario.class,
				procesoID);
		String query = "select distinct (pre)"
				+ "from Evaluado e, PreguntaUsuario pre, ProcesoUsuario pro, ParticipacionEnProceso par, PruebaUsuario pru, ProcesoUsuarioHasPruebaUsuario prp, Resultado r "
				+ "where e =:eval and par.estado =:estado pro =:proce"
				+ "and pre.pruebasUsuario = pru and prp.pruebasUsuario = pru and prp.procesosUsuario = pro and r.preguntasUsuario = pre par.procesosUsuario = pro and par.evaluado = e "
				+ "and 0= (SELECT COUNT(re) FROM Evaluado ev, PreguntaUsuario preg, ProcesoUsuario proc, ParticipacionEnProceso part, PruebaUsuario prue, ProcesoUsuarioHasPruebaUsuario prpr, Resultado re "
				+ "WHERE e=ev and pre = preg and par = part and re.preguntasUsuario = preg and re.participacionEnProceso = part and part.evaluado = ev)";

		Query q = entityManager.createQuery(query);
		q.setParameter("eval", eval);
		q.setParameter("estado",
				Convencion.ESTADO_PARTICIPACION_EN_PROCESO_EN_EJECUCION);
		q.setParameter("eval", proce);
		List<PreguntaUsuario> preguntas = q.getResultList();
		if (preguntas.size() > 0) {
			List<PreguntaUsuarioBO> lista = new ArrayList<PreguntaUsuarioBO>();
			for (int i = 0; i < preguntas.size(); i++) {
				PreguntaUsuario pregunta = preguntas.get(i);
				entityManager.refresh(pregunta);

				PruebaUsuarioBO prueba = new PruebaUsuarioBO();
				prueba.setDescripcion(pregunta.getPruebasUsuario()
						.getDescripcion());
				prueba.setIdentificador(pregunta.getPruebasUsuario()
						.getIdentificador());
				prueba.setNombre(pregunta.getPruebasUsuario().getNombre());
				prueba.setUsuarioAdministradorID(pregunta.getPruebasUsuario()
						.getUsuario().getIdentificador());

				PreguntaUsuarioBO preguntaUsuario = new PreguntaUsuarioBO();
				preguntaUsuario.setCaracteresMaximo(pregunta
						.getCaracteresMaximo());
				preguntaUsuario.setIdentificador(pregunta.getIdentificador());
				preguntaUsuario.setPosicionPreguntaX(pregunta
						.getPosicionPreguntaX());
				preguntaUsuario.setPosicionPreguntaY(pregunta
						.getPosicionPreguntaY());
				preguntaUsuario.setPregunta(pregunta.getPregunta());
				preguntaUsuario.setOrden(pregunta.getOrden());
				preguntaUsuario.setTiempoMaximo(pregunta.getTiempoMaximo());
				ImagenUsuarioBO imagen = new ImagenUsuarioBO();
				imagen.setIdentificador(pregunta.getImagenesUsuario()
						.getIdentificador());
				ImagenBO img = new ImagenBO();
				img.setIdentificador(pregunta.getImagenesUsuario().getImagene()
						.getIdentificador());
				img.setImagenURI(pregunta.getImagenesUsuario().getImagene()
						.getImagenURI());
				imagen.setImagene(img);
				imagen.setUsuario(usuarioAdministradorID);
				preguntaUsuario.setImagenesUsuarioID(imagen);
				preguntaUsuario.setPruebaUsuario(prueba);
				lista.add(preguntaUsuario);
			}
			return lista;
		} else {
			return null;
		}
	}

	@Override
	public List<PreguntaUsuarioBO> listarPreguntasPrueba(
			int usuarioAdministradorID, int pruebaID) {
		Usuario user = entityManager
				.find(Usuario.class, usuarioAdministradorID);
		entityManager.refresh(user);
		if (user != null) {
			PruebaUsuario prueba = entityManager.find(PruebaUsuario.class,
					pruebaID);
			entityManager.refresh(prueba);
			List<PreguntaUsuario> preguntas = prueba.getPreguntasUsuarios();
			// Valida que se encuentre un usuario.
			if (preguntas != null) {
				if (preguntas.size() > 0) {
					List<PreguntaUsuarioBO> lista = new ArrayList<PreguntaUsuarioBO>();
					for (int i = 0; i < preguntas.size(); i++) {
						PreguntaUsuario pregunta = preguntas.get(i);
						entityManager.refresh(pregunta);

						PruebaUsuarioBO p = new PruebaUsuarioBO();
						p.setDescripcion(pregunta.getPruebasUsuario()
								.getDescripcion());
						p.setIdentificador(pregunta.getPruebasUsuario()
								.getIdentificador());
						p.setNombre(pregunta.getPruebasUsuario().getNombre());
						p.setUsuarioAdministradorID(pregunta
								.getPruebasUsuario().getUsuario()
								.getIdentificador());

						PreguntaUsuarioBO preguntaUsuario = new PreguntaUsuarioBO();
						preguntaUsuario.setCaracteresMaximo(pregunta
								.getCaracteresMaximo());
						preguntaUsuario.setIdentificador(pregunta
								.getIdentificador());
						preguntaUsuario.setPosicionPreguntaX(pregunta
								.getPosicionPreguntaX());
						preguntaUsuario.setPosicionPreguntaY(pregunta
								.getPosicionPreguntaY());
						preguntaUsuario.setPregunta(pregunta.getPregunta());
						preguntaUsuario.setOrden(pregunta.getOrden());
						preguntaUsuario.setTiempoMaximo(pregunta
								.getTiempoMaximo());
						ImagenUsuarioBO imagen = new ImagenUsuarioBO();
						imagen.setIdentificador(pregunta.getImagenesUsuario()
								.getIdentificador());
						ImagenBO img = new ImagenBO();
						img.setIdentificador(pregunta.getImagenesUsuario()
								.getImagene().getIdentificador());
						img.setImagenURI(pregunta.getImagenesUsuario()
								.getImagene().getImagenURI());
						imagen.setImagene(img);
						imagen.setUsuario(usuarioAdministradorID);
						preguntaUsuario.setImagenesUsuarioID(imagen);
						preguntaUsuario.setPruebaUsuario(p);
						lista.add(preguntaUsuario);
					}
					return lista;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public int agregarPruebaAProceso(int usuarioAdministradorID,
			PruebaUsuarioBO prueba, ProcesoUsuarioBO proceso) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			PruebaUsuario c = entityManager.find(PruebaUsuario.class,
					prueba.getIdentificador());
			ProcesoUsuario proc = entityManager.find(ProcesoUsuario.class,
					proceso.getIdentificador());
			if (c == null) {
				userTransaction.begin();
				c = new PruebaUsuario();
				Usuario usuario = entityManager.find(Usuario.class,
						usuarioAdministradorID);
				c.setUsuario(usuario);
				c.setDescripcion(prueba.getDescripcion());
				c.setNombre(prueba.getNombre());
				if (!entityManager.contains(c)) {
					entityManager.persist(c);
					entityManager.flush();
					userTransaction.commit();
				}
			}
			entityManager.refresh(c);
			return agregarAsociacionProcesoPrueba(c, proc);
		} catch (Exception exception) {
			// Exception has occurred, roll-back the transaction.
			exception.printStackTrace();
			userTransaction.rollback();
			return Convencion.INCORRECTO;
		}
	}

	private int agregarAsociacionProcesoPrueba(PruebaUsuario prueba,
			ProcesoUsuario proceso) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			ProcesoUsuarioHasPruebaUsuario proHas = new ProcesoUsuarioHasPruebaUsuario();

			proHas.setProcesosUsuario(proceso);
			proHas.setPruebasUsuario(prueba);
			if (!entityManager.contains(proHas)) {
				entityManager.persist(proHas);
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

	public List<PreguntaUsuarioBO> listarPreguntasPorProcesoRestantes(
			int evaluado, int usuarioAdministradorID, int procesoID) {
		Usuario user = entityManager
				.find(Usuario.class, usuarioAdministradorID);
		if (user != null) {
			ProcesoUsuario proceso = entityManager.find(ProcesoUsuario.class,
					procesoID);
			Evaluado eval = entityManager.find(Evaluado.class, evaluado);
			entityManager.refresh(proceso);
			entityManager.refresh(eval);
			List<ProcesoUsuarioHasPruebaUsuario> procesoUsuarioHasPruebaUsuario = proceso
					.getProcesosUsuariosHasPruebasUsuarios();
			List<PruebaUsuario> pruebasProceso = new ArrayList<PruebaUsuario>();
			for (ProcesoUsuarioHasPruebaUsuario procesoUsuarioHasPruebaUsuario2 : procesoUsuarioHasPruebaUsuario) {
				entityManager.refresh(procesoUsuarioHasPruebaUsuario2);
				pruebasProceso.add(procesoUsuarioHasPruebaUsuario2
						.getPruebasUsuario());
			}
			List<PreguntaUsuario> preguntas = new ArrayList<>();
			for (PruebaUsuario pruebaUsuario : pruebasProceso) {
				entityManager.refresh(pruebaUsuario);
				preguntas.addAll(pruebaUsuario.getPreguntasUsuarios());
			}
			if (preguntas.size() > 0) {
				List<PreguntaUsuarioBO> lista = new ArrayList<PreguntaUsuarioBO>();
				for (int i = 0; i < preguntas.size(); i++) {
					PreguntaUsuario pregunta = preguntas.get(i);
					entityManager.refresh(pregunta);

					PruebaUsuarioBO prueba = new PruebaUsuarioBO();
					prueba.setDescripcion(pregunta.getPruebasUsuario()
							.getDescripcion());
					prueba.setIdentificador(pregunta.getPruebasUsuario()
							.getIdentificador());
					prueba.setNombre(pregunta.getPruebasUsuario().getNombre());
					prueba.setUsuarioAdministradorID(pregunta
							.getPruebasUsuario().getUsuario()
							.getIdentificador());

					PreguntaUsuarioBO preguntaUsuario = new PreguntaUsuarioBO();
					preguntaUsuario.setCaracteresMaximo(pregunta
							.getCaracteresMaximo());
					preguntaUsuario.setIdentificador(pregunta
							.getIdentificador());
					preguntaUsuario.setPosicionPreguntaX(pregunta
							.getPosicionPreguntaX());
					preguntaUsuario.setOrden(pregunta.getOrden());
					preguntaUsuario.setPosicionPreguntaY(pregunta
							.getPosicionPreguntaY());
					preguntaUsuario.setPregunta(pregunta.getPregunta());
					preguntaUsuario.setTiempoMaximo(pregunta.getTiempoMaximo());
					ImagenUsuarioBO imagen = new ImagenUsuarioBO();
					imagen.setIdentificador(pregunta.getImagenesUsuario()
							.getIdentificador());
					ImagenBO img = new ImagenBO();
					img.setIdentificador(pregunta.getImagenesUsuario()
							.getImagene().getIdentificador());
					img.setImagenURI(pregunta.getImagenesUsuario().getImagene()
							.getImagenURI());
					imagen.setImagene(img);
					imagen.setUsuario(usuarioAdministradorID);
					preguntaUsuario.setImagenesUsuarioID(imagen);
					preguntaUsuario.setPruebaUsuario(prueba);
					lista.add(preguntaUsuario);
				}
				return lista;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public List<PruebaUsuarioBO> listarPruebasRestantesProceso(
			int identificador, ProcesoUsuarioBO proceso) {
		// TODO Auto-generated method stub
		return null;
	}
}
