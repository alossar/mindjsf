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
import co.mind.management.shared.entidades.Imagen;
import co.mind.management.shared.entidades.ImagenUsuario;
import co.mind.management.shared.entidades.Usuario;
import co.mind.management.shared.persistencia.interfaces.IGestionLaminas;
import co.mind.management.shared.recursos.Convencion;

public class GestionLaminas implements IGestionLaminas {

	public EntityManagerFactory emf = null;
	public EntityManager entityManager = null;
	public static final String JTA_PU_NAME = Convencion.JTA_PU_NAME;

	public GestionLaminas() {
		emf = Persistence.createEntityManagerFactory(JTA_PU_NAME);
		entityManager = emf.createEntityManager();
	}

	@Override
	public int agregarLamina(ImagenBO lamina) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			Imagen u = new Imagen();
			u.setImagenURI(lamina.getImagenURI());
			if (!entityManager.contains(u)) {
				entityManager.persist(u);
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
	public int editarLamina(ImagenBO lamina) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			Imagen u = entityManager.find(Imagen.class,
					lamina.getIdentificador());
			u.setImagenURI(lamina.getImagenURI());
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
	public ImagenBO consultarLamina(int laminaID) {
		Imagen im = entityManager.find(Imagen.class, laminaID);
		if (im == null) {
			return null;
		} else {
			ImagenBO resultado = new ImagenBO();

			resultado.setIdentificador(im.getIdentificador());
			resultado.setImagenURI(im.getImagenURI());
			return resultado;
		}
	}

	@Override
	public int eliminarLamina(int laminaID) {
		Imagen im = entityManager.find(Imagen.class, laminaID);
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			entityManager.remove(im);
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
	public int agregarLaminaUsuarioAdministrador(int usuarioAdministradorID,
			ImagenUsuarioBO laminaUsuario) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			Usuario usuario = entityManager.find(Usuario.class,
					usuarioAdministradorID);
			Imagen im = entityManager.find(Imagen.class, laminaUsuario
					.getImagen().getIdentificador());
			ImagenUsuario imagen = new ImagenUsuario();
			imagen.setUsuario(usuario);
			imagen.setImagene(im);
			if (!entityManager.contains(imagen)) {
				entityManager.persist(imagen);
				entityManager.flush();
				userTransaction.commit();
				return 0;
			} else {
				return 1;
			}
		} catch (Exception exception) {
			// Exception has occurred, roll-back the transaction.
			exception.printStackTrace();
			userTransaction.rollback();
			return 1;
		}
	}

	@Override
	public int editarLaminaUsuarioAdministrador(int usuarioAdministradorID,
			ImagenUsuarioBO laminaUsuario) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			ImagenUsuario u = entityManager.find(ImagenUsuario.class,
					laminaUsuario.getIdentificador());
			Imagen im = entityManager.find(Imagen.class, laminaUsuario
					.getImagen().getIdentificador());
			u.setImagene(im);
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
	public ImagenUsuarioBO consultarLaminaUsuarioAdministrador(
			int usuarioAdministradorID, int laminaUsuarioID) {
		ImagenUsuario im = entityManager.find(ImagenUsuario.class,
				laminaUsuarioID);
		if (im == null) {
			return null;
		} else {
			ImagenUsuarioBO resultado = new ImagenUsuarioBO();
			resultado.setIdentificador(im.getIdentificador());
			resultado.setUsuario(usuarioAdministradorID);
			ImagenBO ima = new ImagenBO();

			ima.setIdentificador(im.getImagene().getIdentificador());
			ima.setImagenURI(im.getImagene().getImagenURI());
			resultado.setImagene(ima);
			return resultado;
		}
	}

	@Override
	public int eliminarLaminaUsuarioAdministrador(int usuarioAdministradorID,
			int laminaUsuarioID) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			ImagenUsuario im = entityManager.find(ImagenUsuario.class,
					laminaUsuarioID);
			userTransaction.begin();
			entityManager.remove(im);
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
	public List<ImagenBO> listarLaminas() {
		String query = "SELECT DISTINCT(u) FROM Imagen u";
		Query q = entityManager.createQuery(query);
		List<Imagen> imagenes = q.getResultList();
		// Valida que se encuentre un usuario.
		if (imagenes != null) {
			if (imagenes.size() > 0) {
				List<ImagenBO> lista = new ArrayList<ImagenBO>();
				for (int i = 0; i < imagenes.size(); i++) {
					Imagen im = imagenes.get(i);
					ImagenBO resultado = new ImagenBO();

					resultado.setIdentificador(im.getIdentificador());
					resultado.setImagenURI(im.getImagenURI());
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
	public List<ImagenUsuarioBO> listarLaminasUsuarioAdministrador(
			int usuarioAdministradorID) {
		Usuario user = entityManager
				.find(Usuario.class, usuarioAdministradorID);
		if (user != null) {

			List<ImagenUsuario> usuarios = user.getImagenesUsuarios();
			if (usuarios.size() > 0) {
				List<ImagenUsuarioBO> lista = new ArrayList<ImagenUsuarioBO>();
				for (int i = 0; i < usuarios.size(); i++) {
					ImagenUsuarioBO resultado = new ImagenUsuarioBO();
					ImagenUsuario im = usuarios.get(i);
					resultado.setIdentificador(im.getIdentificador());
					resultado.setUsuario(usuarioAdministradorID);
					ImagenBO ima = new ImagenBO();

					ima.setIdentificador(im.getImagene().getIdentificador());
					ima.setImagenURI(im.getImagene().getImagenURI());
					resultado.setImagene(ima);
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
	public int consultarIDLamina(int usuarioAdministradorID, int laminaUsuarioID) {
		Usuario user = entityManager
				.find(Usuario.class, usuarioAdministradorID);

		if (user != null) {
			String query = "SELECT DISTINCT(u) FROM Imagen u, ImagenUsuario i WHERE i.usuario =: usuar AND u.imagen = u";
			Query q = entityManager.createQuery(query);
			q.setParameter("usuar", user);
			Imagen pregunta = (Imagen) q.getSingleResult();
			// Valida que se encuentre un usuario.
			if (pregunta != null) {
				return pregunta.getIdentificador();
			} else {
				return 0;
			}

		} else {
			return 0;
		}
	}

	public ImagenUsuarioBO consultarLaminaUsuarioAdministradorEnlace(
			int identificador, String imagenURI) {
		Usuario user = entityManager.find(Usuario.class, identificador);
		if (user != null) {
			entityManager.refresh(user);
			List<ImagenUsuario> imagenes = user.getImagenesUsuarios();
			for (ImagenUsuario imagenUsuario : imagenes) {
				if (imagenUsuario.getImagene().getImagenURI().equals(imagenURI)) {
					ImagenUsuarioBO imagen = new ImagenUsuarioBO();
					imagen.setIdentificador(imagenUsuario.getIdentificador());
					imagen.setUsuario(imagenUsuario.getUsuario()
							.getIdentificador());
					ImagenBO ima = new ImagenBO();
					ima.setIdentificador(imagenUsuario.getImagene()
							.getIdentificador());
					ima.setImagenURI(imagenUsuario.getImagene().getImagenURI());
					imagen.setImagene(ima);
					return imagen;
				}
			}
		}
		return null;

	}

}
