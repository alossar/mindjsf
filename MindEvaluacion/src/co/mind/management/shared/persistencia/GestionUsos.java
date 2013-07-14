package co.mind.management.shared.persistencia;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import co.mind.management.shared.dto.UsoBO;
import co.mind.management.shared.entidades.UsoUsuario;
import co.mind.management.shared.entidades.Usuario;
import co.mind.management.shared.persistencia.interfaces.IGestionUsos;
import co.mind.management.shared.recursos.Convencion;

public class GestionUsos implements IGestionUsos {

	public EntityManagerFactory emf = null;
	public EntityManager entityManager = null;
	public static final String JTA_PU_NAME = Convencion.JTA_PU_NAME;

	public GestionUsos() {
		crearEntityManager();
	}

	private void crearEntityManager() {

		emf = Persistence.createEntityManagerFactory(JTA_PU_NAME);
		entityManager = emf.createEntityManager();
	}

	@Override
	public int agregarUso(int usuarioID, UsoBO uso) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			Usuario usuario = entityManager.find(Usuario.class, usuarioID);
			UsoUsuario u = new UsoUsuario();
			u.setFechaAsignacion(uso.getFechaAsignacion());
			u.setFechaVencimiento(uso.getFechaVencimiento());
			u.setUsosAsignados(uso.getUsosAsignados());
			u.setUsosRedimidos(uso.getUsosRedimidos());
			u.setUsuario(usuario);

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
	public int editarPermiso(UsoBO uso) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			UsoUsuario u = entityManager.find(UsoUsuario.class,
					uso.getIdentificador());
			u.setFechaAsignacion(uso.getFechaAsignacion());
			u.setFechaVencimiento(uso.getFechaVencimiento());
			u.setUsosAsignados(uso.getUsosAsignados());
			u.setUsosRedimidos(uso.getUsosRedimidos());
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
	public UsoBO consultarUso(int usoID) {
		UsoUsuario u = entityManager.find(UsoUsuario.class, usoID);

		if (u == null) {
			return null;
		} else {
			entityManager.refresh(u);
			UsoBO resultado = new UsoBO();
			resultado.setFechaAsignacion(u.getFechaAsignacion());
			resultado.setFechaVencimiento(u.getFechaVencimiento());
			resultado.setIdentificador(u.getIdentificador());
			resultado.setUsosAsignados(u.getUsosAsignados());
			resultado.setUsosRedimidos(u.getUsosRedimidos());
			return resultado;
		}
	}

	@Override
	public int eliminarPermiso(int usoID) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			UsoUsuario usuario = entityManager.find(UsoUsuario.class, usoID);
			if (usuario != null) {
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
	public List<UsoBO> listarUsos(int usuarioID) {
		Usuario user = entityManager.find(Usuario.class, usuarioID);
		if (user != null) {
			entityManager.refresh(user);
			List<UsoBO> lista = new ArrayList<UsoBO>();
			for (int i = 0; i < user.getUsosUsuarios().size(); i++) {
				UsoBO resultado = new UsoBO();
				UsoUsuario u = user.getUsosUsuarios().get(i);
				resultado.setFechaAsignacion(u.getFechaAsignacion());
				resultado.setFechaVencimiento(u.getFechaVencimiento());
				resultado.setIdentificador(u.getIdentificador());
				resultado.setUsosAsignados(u.getUsosAsignados());
				resultado.setUsosRedimidos(u.getUsosRedimidos());
				lista.add(resultado);
			}
			return lista;
		} else {
			return null;
		}
	}

	public boolean consultarCapacidadUsos(int identificador, int size) {
		Usuario user = entityManager.find(Usuario.class, identificador);
		if (user != null) {
			entityManager.refresh(user);
			if (user.getTipo()
					.equalsIgnoreCase(Convencion.TIPO_USUARIO_MAESTRO)
					|| user.getTipo().equalsIgnoreCase(
							Convencion.TIPO_USUARIO_MAESTRO_PRINCIPAL)) {
				return true;
			}
			List<UsoUsuario> usos = user.getUsosUsuarios();
			boolean continuar = true;
			for (int i = 0; i < usos.size() && continuar; i++) {
				UsoUsuario usoUsuario = usos.get(i);
				int restantes = usoUsuario.getUsosAsignados()
						- usoUsuario.getUsosRedimidos();
				if (restantes != 0) {
					if (size <= restantes) {
						return true;
					} else {
						size -= restantes;
					}
				}
			}
			if (size > 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
}
