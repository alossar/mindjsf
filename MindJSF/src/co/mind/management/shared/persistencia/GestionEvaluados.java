package co.mind.management.shared.persistencia;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import co.mind.management.shared.dto.EvaluadoBO;
import co.mind.management.shared.dto.ProcesoUsuarioBO;
import co.mind.management.shared.entidades.Evaluado;
import co.mind.management.shared.entidades.ParticipacionEnProceso;
import co.mind.management.shared.entidades.ProcesoUsuario;
import co.mind.management.shared.entidades.Usuario;
import co.mind.management.shared.persistencia.interfaces.IGestionEvaluados;
import co.mind.management.shared.recursos.Convencion;

public class GestionEvaluados implements IGestionEvaluados {

	public EntityManagerFactory emf = null;
	public EntityManager entityManager = null;
	public static final String JTA_PU_NAME = Convencion.JTA_PU_NAME;

	public GestionEvaluados() {
		crearEntityManager();
	}

	private void crearEntityManager() {

		emf = Persistence.createEntityManagerFactory(JTA_PU_NAME);
		entityManager = emf.createEntityManager();
	}

	@Override
	public int agregarUsuarioBasico(int usuarioAdministradorID,
			EvaluadoBO Evaluado) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			Usuario usuario = entityManager.find(Usuario.class,
					usuarioAdministradorID);
			Evaluado u = new Evaluado();
			u.setApellidos(Evaluado.getApellidos());
			u.setCorreoElectronico(Evaluado.getCorreoElectronico());
			u.setEdad(Evaluado.getEdad());
			u.setCedula(Evaluado.getCedula());
			u.setNombres(Evaluado.getNombres());
			u.setUsuario(usuario);

			if (!entityManager.contains(u)) {
				entityManager.persist(u);
				entityManager.flush();
				userTransaction.commit();
				entityManager.refresh(u);
				return Convencion.CORRECTO;
			} else {
				return Convencion.INCORRECTO;
			}
		} catch (Exception exception) {
			// Exception has occurred, roll-back the transaction.
			exception.printStackTrace();
			userTransaction.rollback();
			return 1;
		}
	}

	@Override
	public int editarUsuarioBasico(int usuarioAdministradorID,
			EvaluadoBO Evaluado) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			Usuario usuario = entityManager.find(Usuario.class,
					usuarioAdministradorID);
			Evaluado u = entityManager.find(Evaluado.class,
					Evaluado.getIdentificador());
			u.setApellidos(Evaluado.getApellidos());
			u.setCorreoElectronico(Evaluado.getCorreoElectronico());
			u.setEdad(Evaluado.getEdad());
			u.setIdentificador(Evaluado.getIdentificador());
			u.setNombres(Evaluado.getNombres());
			u.setUsuario(usuario);
			entityManager.merge(u);
			entityManager.flush();
			userTransaction.commit();
			return Convencion.CORRECTO;
		} catch (Exception e) {
			e.printStackTrace();
			userTransaction.rollback();
			return Convencion.INCORRECTO;
		}
	}

	@Override
	public EvaluadoBO consultarUsuarioBasico(int usuarioAdministradorID,
			int usuarioBasicoID) {
		Evaluado usuario = entityManager.find(Evaluado.class, usuarioBasicoID);

		if (usuario == null) {
			return null;
		} else {
			entityManager.refresh(usuario);
			EvaluadoBO resultado = new EvaluadoBO();
			resultado.setApellidos(usuario.getApellidos());
			resultado.setCorreoElectronico(usuario.getCorreoElectronico());
			resultado.setEdad(usuario.getEdad());
			resultado.setIdentificador(usuario.getIdentificador());
			resultado.setNombres(usuario.getNombres());
			resultado
					.setIdentificadorUsuarioAdministrador(usuarioAdministradorID);
			resultado.setCedula(usuario.getCedula());
			return resultado;
		}
	}

	@Override
	public int eliminarUsuarioBasico(int usuarioAdministradorID,
			int usuarioBasicoID) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			Evaluado usuario = entityManager.find(Evaluado.class,
					usuarioBasicoID);
			if (usuario != null) {
				List<ParticipacionEnProceso> p = usuario
						.getParticipacionEnProcesos();
				if (p != null) {
					for (ParticipacionEnProceso part : p) {
						userTransaction.begin();
						entityManager.remove(part);
						entityManager.flush();
						userTransaction.commit();
					}
				}
				userTransaction.begin();
				entityManager.remove(usuario);
				entityManager.flush();
				userTransaction.commit();
				return Convencion.CORRECTO;
			} else {
				return Convencion.INCORRECTO;
			}
		} catch (Exception e) {
			e.printStackTrace();
			userTransaction.rollback();
			return Convencion.INCORRECTO;
		}
	}

	@Override
	public List<EvaluadoBO> listarUsuariosBasicos(int usuarioAdministradorID) {
		Usuario user = entityManager
				.find(Usuario.class, usuarioAdministradorID);
		if (user != null) {
			entityManager.refresh(user);
			List<EvaluadoBO> lista = new ArrayList<EvaluadoBO>();
			for (int i = 0; i < user.getEvaluados().size(); i++) {
				EvaluadoBO resultado = new EvaluadoBO();
				Evaluado u = user.getEvaluados().get(i);
				resultado.setApellidos(u.getApellidos());
				resultado.setCorreoElectronico(u.getCorreoElectronico());
				resultado.setEdad(u.getEdad());
				resultado.setIdentificador(u.getIdentificador());
				resultado.setNombres(u.getNombres());
				resultado
						.setIdentificadorUsuarioAdministrador(usuarioAdministradorID);
				resultado.setCedula(u.getCedula());
				lista.add(resultado);
			}
			return lista;
		} else {
			return null;
		}
	}

	public List<EvaluadoBO> listarEvaluadosPorCorreo(
			int usuarioAdministradorID, String correo) {
		Usuario user = entityManager
				.find(Usuario.class, usuarioAdministradorID);
		if (user != null) {
			entityManager.refresh(user);
			String query = "SELECT DISTINCT(u) FROM Evaluado u WHERE u.usuario =:user AND u.correoElectronico =:correo";
			Query q = entityManager.createQuery(query);
			q.setParameter("user", user);
			q.setParameter("correo", correo);
			List<Evaluado> lista = q.getResultList();
			List<EvaluadoBO> listaResultado = new ArrayList<EvaluadoBO>();
			for (int i = 0; i < lista.size(); i++) {
				EvaluadoBO resultado = new EvaluadoBO();
				Evaluado u = lista.get(i);
				resultado.setApellidos(u.getApellidos());
				resultado.setCorreoElectronico(u.getCorreoElectronico());
				resultado.setEdad(u.getEdad());
				resultado.setIdentificador(u.getIdentificador());
				resultado.setNombres(u.getNombres());
				resultado
						.setIdentificadorUsuarioAdministrador(usuarioAdministradorID);
				resultado.setCedula(u.getCedula());
				listaResultado.add(resultado);
			}
			return listaResultado;
		} else {
			return null;
		}
	}

	public List<EvaluadoBO> listarEvaluadosPorCedula(
			int usuarioAdministradorID, int id) {
		Usuario user = entityManager
				.find(Usuario.class, usuarioAdministradorID);
		if (user != null) {
			entityManager.refresh(user);
			String query = "SELECT DISTINCT(u) FROM Evaluado u WHERE u.usuario =:user AND u.identificador =:correo";
			Query q = entityManager.createQuery(query);
			q.setParameter("user", user);
			q.setParameter("correo", id);
			List<Evaluado> lista = q.getResultList();
			List<EvaluadoBO> listaResultado = new ArrayList<EvaluadoBO>();
			for (int i = 0; i < lista.size(); i++) {
				EvaluadoBO resultado = new EvaluadoBO();
				Evaluado u = lista.get(i);
				resultado.setApellidos(u.getApellidos());
				resultado.setCorreoElectronico(u.getCorreoElectronico());
				resultado.setEdad(u.getEdad());
				resultado.setIdentificador(u.getIdentificador());
				resultado.setNombres(u.getNombres());
				resultado
						.setIdentificadorUsuarioAdministrador(usuarioAdministradorID);
				resultado.setCedula(u.getCedula());
				listaResultado.add(resultado);
			}
			return listaResultado;
		} else {
			return null;
		}
	}

	public List<EvaluadoBO> listarEvaluadosRestantesProceso(int identificador,
			ProcesoUsuarioBO proceso) {
		ProcesoUsuario proc = entityManager.find(ProcesoUsuario.class,
				proceso.getIdentificador());
		if (proc != null) {

			Usuario user = entityManager.find(Usuario.class, identificador);
			if (user != null) {
				entityManager.refresh(user);
				List<EvaluadoBO> lista = new ArrayList<EvaluadoBO>();
				for (int i = 0; i < user.getEvaluados().size(); i++) {
					EvaluadoBO resultado = new EvaluadoBO();
					Evaluado u = user.getEvaluados().get(i);
					resultado.setApellidos(u.getApellidos());
					resultado.setCorreoElectronico(u.getCorreoElectronico());
					resultado.setEdad(u.getEdad());
					resultado.setIdentificador(u.getIdentificador());
					resultado.setNombres(u.getNombres());
					resultado
							.setIdentificadorUsuarioAdministrador(identificador);
					resultado.setCedula(u.getCedula());
					lista.add(resultado);
				}
				return lista;
			} else {
				return null;
			}
		}
		return null;
	}
}
