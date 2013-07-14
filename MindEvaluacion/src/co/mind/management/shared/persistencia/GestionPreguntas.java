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
import co.mind.management.shared.dto.PruebaUsuarioBO;
import co.mind.management.shared.entidades.ImagenUsuario;
import co.mind.management.shared.entidades.PreguntaUsuario;
import co.mind.management.shared.entidades.PruebaUsuario;
import co.mind.management.shared.entidades.Resultado;
import co.mind.management.shared.entidades.Usuario;
import co.mind.management.shared.persistencia.interfaces.IGestionPreguntas;
import co.mind.management.shared.recursos.Convencion;

public class GestionPreguntas implements IGestionPreguntas {

	public EntityManagerFactory emf = null;
	public EntityManager entityManager = null;
	public static final String JTA_PU_NAME = Convencion.JTA_PU_NAME;

	public GestionPreguntas() {
		emf = Persistence.createEntityManagerFactory(JTA_PU_NAME);
		entityManager = emf.createEntityManager();
	}

	@Override
	public int agregarPreguntaUsuarioAdministrador(int usuarioAdministradorID,
			PreguntaUsuarioBO preguntaUsuario, PruebaUsuarioBO categoria) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			ImagenUsuario im = entityManager.find(ImagenUsuario.class,
					preguntaUsuario.getImagenesUsuario().getIdentificador());
			PruebaUsuario categ = entityManager.find(PruebaUsuario.class,
					categoria.getIdentificador());
			entityManager.refresh(categ);
			int orden = 1;
			if (categ.getPreguntasUsuarios() != null) {
				orden = categ.getPreguntasUsuarios().size();
				orden++;
			}
			PreguntaUsuario pregunta = new PreguntaUsuario();
			pregunta.setOrden(orden);
			pregunta.setCaracteresMaximo(preguntaUsuario.getCaracteresMaximo());
			pregunta.setImagenesUsuario(im);
			pregunta.setPregunta(preguntaUsuario.getPregunta());
			pregunta.setTiempoMaximo(preguntaUsuario.getTiempoMaximo());
			pregunta.setPosicionPreguntaX(preguntaUsuario
					.getPosicionPreguntaX());
			pregunta.setPosicionPreguntaY(preguntaUsuario
					.getPosicionPreguntaY());
			pregunta.setPruebasUsuario(categ);
			if (!entityManager.contains(pregunta)) {
				entityManager.persist(pregunta);
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
	public int editarPreguntaUsuarioAdministrador(int usuarioAdministradorID,
			PreguntaUsuarioBO preguntaUsuario) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			PreguntaUsuario u = entityManager.find(PreguntaUsuario.class,
					preguntaUsuario.getIdentificador());
			ImagenUsuario im = entityManager.find(ImagenUsuario.class,
					preguntaUsuario.getImagenesUsuario().getIdentificador());
			u.setCaracteresMaximo(preguntaUsuario.getCaracteresMaximo());
			u.setImagenesUsuario(im);
			u.setPregunta(preguntaUsuario.getPregunta());
			u.setTiempoMaximo(preguntaUsuario.getTiempoMaximo());
			u.setPosicionPreguntaX(preguntaUsuario.getPosicionPreguntaX());
			u.setPosicionPreguntaY(preguntaUsuario.getPosicionPreguntaY());
			entityManager.merge(u);
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
	public PreguntaUsuarioBO consultarPreguntaUsuarioAdministrador(
			int usuarioAdministradorID, int preguntaUsuarioID) {
		PreguntaUsuario im = entityManager.find(PreguntaUsuario.class,
				preguntaUsuarioID);
		if (im == null) {
			return null;
		} else {
			PreguntaUsuarioBO resultado = new PreguntaUsuarioBO();
			resultado.setCaracteresMaximo(im.getCaracteresMaximo());
			resultado.setIdentificador(im.getIdentificador());
			resultado.setPregunta(im.getPregunta());
			resultado.setTiempoMaximo(im.getTiempoMaximo());
			resultado.setPosicionPreguntaX(im.getPosicionPreguntaX());
			resultado.setPosicionPreguntaY(im.getPosicionPreguntaY());
			resultado.setOrden(im.getOrden());
			ImagenUsuarioBO imagen = new ImagenUsuarioBO();
			imagen.setIdentificador(im.getImagenesUsuario().getIdentificador());
			imagen.setUsuario(im.getImagenesUsuario().getUsuario()
					.getIdentificador());
			ImagenBO ima = new ImagenBO();
			ima.setIdentificador(im.getImagenesUsuario().getImagene()
					.getIdentificador());
			ima.setImagenURI(im.getImagenesUsuario().getImagene()
					.getImagenURI());
			imagen.setImagene(ima);
			resultado.setImagenesUsuarioID(imagen);
			PruebaUsuarioBO cat = new PruebaUsuarioBO();
			cat.setDescripcion(im.getPruebasUsuario().getDescripcion());
			cat.setIdentificador(im.getPruebasUsuario().getIdentificador());
			cat.setNombre(im.getPruebasUsuario().getNombre());
			cat.setUsuarioAdministradorID(im.getPruebasUsuario().getUsuario()
					.getIdentificador());
			resultado.setPruebaUsuario(cat);
			return resultado;
		}
	}

	@Override
	public int eliminarPreguntaUsuarioAdministrador(int usuarioAdministradorID,
			int preguntaUsuarioID) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			PreguntaUsuario im = entityManager.find(PreguntaUsuario.class,
					preguntaUsuarioID);
			int idPrueba = im.getPruebasUsuario().getIdentificador();
			if (im != null) {
				List<Resultado> resultados = im.getResultados();
				if (resultados != null) {
					for (Resultado resultado : resultados) {
						userTransaction.begin();
						entityManager.remove(resultado);
						entityManager.flush();
						userTransaction.commit();
					}
				}
				userTransaction.begin();
				entityManager.remove(im);
				entityManager.flush();
				userTransaction.commit();
				if (alterarOrdenEliminarPregunta(im.getOrden(), idPrueba)) {
					return Convencion.CORRECTO;
				} else {
					return Convencion.INCORRECTO;
				}

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

	private boolean alterarOrdenEliminarPregunta(int ordenEliminada,
			int idPrueba) {
		EntityTransaction userTransaction = null;

		try {
			PruebaUsuario prueba = entityManager.find(PruebaUsuario.class,
					idPrueba);
			if (prueba.getPreguntasUsuarios() != null) {
				for (PreguntaUsuario resultado : prueba.getPreguntasUsuarios()) {
					if (resultado.getOrden() > ordenEliminada) {
						userTransaction = entityManager.getTransaction();
						userTransaction.begin();
						resultado.setOrden(resultado.getOrden() - 1);
						entityManager.merge(resultado);
						entityManager.flush();
						userTransaction.commit();
					}
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<PreguntaUsuarioBO> listarPreguntasUsuarioAdministrador(
			int usuarioAdministrador) {
		Usuario user = entityManager.find(Usuario.class, usuarioAdministrador);
		if (user != null) {
			entityManager.refresh(user);
			String query = "SELECT DISTINCT(u) FROM PreguntaUsuario u, ImagenUsuario i WHERE i.usuario =:user AND u.imagenesUsuario = i";
			Query q = entityManager.createQuery(query);
			q.setParameter("user", user);
			List<PreguntaUsuario> usuarios = q.getResultList();
			// Valida que se encuentre un usuario.
			if (usuarios != null) {
				if (usuarios.size() > 0) {
					List<PreguntaUsuarioBO> lista = new ArrayList<PreguntaUsuarioBO>();
					for (int i = 0; i < usuarios.size(); i++) {
						PreguntaUsuario im = usuarios.get(i);
						PreguntaUsuarioBO resultado = new PreguntaUsuarioBO();
						resultado.setCaracteresMaximo(im.getCaracteresMaximo());
						resultado.setIdentificador(im.getIdentificador());
						resultado.setPregunta(im.getPregunta());
						resultado.setTiempoMaximo(im.getTiempoMaximo());
						resultado.setPosicionPreguntaX(im
								.getPosicionPreguntaX());
						resultado.setPosicionPreguntaY(im
								.getPosicionPreguntaY());
						resultado.setOrden(im.getOrden());
						ImagenUsuarioBO imagen = new ImagenUsuarioBO();
						imagen.setIdentificador(im.getImagenesUsuario()
								.getIdentificador());
						imagen.setUsuario(im.getImagenesUsuario().getUsuario()
								.getIdentificador());
						ImagenBO ima = new ImagenBO();
						ima.setIdentificador(im.getImagenesUsuario()
								.getImagene().getIdentificador());
						ima.setImagenURI(im.getImagenesUsuario().getImagene()
								.getImagenURI());
						imagen.setImagene(ima);
						resultado.setImagenesUsuarioID(imagen);
						PruebaUsuarioBO cat = new PruebaUsuarioBO();
						cat.setDescripcion(im.getPruebasUsuario()
								.getDescripcion());
						cat.setIdentificador(im.getPruebasUsuario()
								.getIdentificador());
						cat.setNombre(im.getPruebasUsuario().getNombre());
						cat.setUsuarioAdministradorID(im.getPruebasUsuario()
								.getUsuario().getIdentificador());
						resultado.setPruebaUsuario(cat);
						lista.add(resultado);
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

}
