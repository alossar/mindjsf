package co.mind.management.shared.persistencia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import co.mind.management.shared.dto.UsuarioProgramadorBO;
import co.mind.management.shared.entidades.Evaluado;
import co.mind.management.shared.entidades.ProcesoUsuario;
import co.mind.management.shared.entidades.Usuario;
import co.mind.management.shared.persistencia.interfaces.IGestionUsuariosProgramadores;
import co.mind.management.shared.recursos.Convencion;
import co.mind.management.shared.recursos.Generador;
import co.mind.management.shared.recursos.SMTPSender;

public class GestionUsuariosProgramadores implements
		IGestionUsuariosProgramadores {

	public EntityManagerFactory emf = null;
	public EntityManager entityManager = null;
	public static final String JTA_PU_NAME = Convencion.JTA_PU_NAME;

	public GestionUsuariosProgramadores() {
		emf = Persistence.createEntityManagerFactory(JTA_PU_NAME);
		entityManager = emf.createEntityManager();
	}

	@Override
	public int agregarUsuarioProgramador(int usuarioAdministradorID,
			UsuarioProgramadorBO usuarioAdministrador) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			String contrasena = Generador.GenerarCodigo(Generador.CARACTERES,
					12);
			Usuario usuario = entityManager.find(Usuario.class,
					usuarioAdministradorID);
			userTransaction.begin();
			Usuario u = new Usuario();
			u.setApellidos(usuarioAdministrador.getApellidos());
			u.setCorreo_Electronico(usuarioAdministrador
					.getCorreo_Electronico());
			u.setIdentificador(usuarioAdministrador.getIdentificador());
			u.setCedula(usuarioAdministrador.getCedula());
			u.setNombres(usuarioAdministrador.getNombres());
			u.setCargo(usuarioAdministrador.getCargo());
			u.setCiudad(usuarioAdministrador.getCiudad());
			u.setContrasena(Generador.convertirStringmd5(contrasena));
			u.setEmpresa(usuarioAdministrador.getEmpresa());
			u.setEstado_Cuenta(Convencion.ESTADO_CUENTA_ACTIVA);
			u.setFecha_Creacion(new Date());
			u.setIdentificador(usuarioAdministrador.getIdentificador());
			u.setNombres(usuarioAdministrador.getNombres());
			u.setPais(usuarioAdministrador.getPais());
			u.setTelefono(usuarioAdministrador.getTelefono());
			u.setTelefono_Celular(usuarioAdministrador.getTelefono_Celular());
			u.setTipo(Convencion.TIPO_USUARIO_PROGRAMADOR);
			u.setUsuario(usuario);
			if (!entityManager.contains(u)) {
				entityManager.persist(u);
				entityManager.flush();
				userTransaction.commit();
				SMTPSender.enviarCorreoCreacionCuenta(u.getNombres(),
						u.getApellidos(), u.getCorreo_Electronico(),
						contrasena, 0, false);
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
	public int editarUsuarioProgramador(int usuarioAdministradorID,
			UsuarioProgramadorBO usuarioAdministrador) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			Usuario u = entityManager.find(Usuario.class,
					usuarioAdministrador.getIdentificador());
			u.setApellidos(usuarioAdministrador.getApellidos());
			u.setCorreo_Electronico(usuarioAdministrador
					.getCorreo_Electronico());
			u.setIdentificador(usuarioAdministrador.getIdentificador());
			u.setCedula(usuarioAdministrador.getCedula());
			u.setNombres(usuarioAdministrador.getNombres());
			u.setCargo(usuarioAdministrador.getCargo());
			u.setCiudad(usuarioAdministrador.getCiudad());
			u.setEmpresa(usuarioAdministrador.getEmpresa());
			u.setEstado_Cuenta(usuarioAdministrador.getEstado_Cuenta());
			u.setFecha_Creacion(usuarioAdministrador.getFecha_Creacion());
			u.setIdentificador(usuarioAdministrador.getIdentificador());
			u.setNombres(usuarioAdministrador.getNombres());
			u.setPais(usuarioAdministrador.getPais());
			u.setTelefono(usuarioAdministrador.getTelefono());
			u.setTelefono_Celular(usuarioAdministrador.getTelefono_Celular());
			u.setTipo(usuarioAdministrador.getTipo());
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
	public UsuarioProgramadorBO consultarUsuarioProgramador(
			int usuarioAdministradorID, int usuarioProgramadorID) {
		Usuario usuario = entityManager.find(Usuario.class,
				usuarioProgramadorID);
		if (usuario == null) {
			return null;
		} else {
			entityManager.refresh(usuario);
			UsuarioProgramadorBO resultado = new UsuarioProgramadorBO();
			resultado.setApellidos(usuario.getApellidos());
			resultado.setCorreo_Electronico(usuario.getCorreo_Electronico());
			resultado.setIdentificador(usuario.getIdentificador());
			resultado.setCedula(usuario.getCedula());
			resultado.setNombres(usuario.getNombres());
			resultado.setCargo(usuario.getCargo());
			resultado.setCiudad(usuario.getCiudad());
			resultado.setEmpresa(usuario.getEmpresa());
			resultado.setEstado_Cuenta(usuario.getEstado_Cuenta());
			resultado.setFecha_Creacion(usuario.getFecha_Creacion());
			resultado.setIdentificador(usuario.getIdentificador());
			resultado.setNombres(usuario.getNombres());
			resultado.setPais(usuario.getPais());
			resultado.setTelefono(usuario.getTelefono());
			resultado.setTelefono_Celular(usuario.getTelefono_Celular());
			resultado.setTipo(usuario.getTipo());

			return resultado;
		}
	}

	@Override
	public int eliminarUsuarioProgramador(int usuarioAdministradorID,
			int usuarioProgramadorID) {
		Usuario usuario = entityManager.find(Usuario.class,
				usuarioProgramadorID);
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			List<Evaluado> evaluados = usuario.getEvaluados();
			GestionEvaluados geval = new GestionEvaluados();
			for (Evaluado evaluado : evaluados) {
				geval.eliminarUsuarioBasico(usuario.getIdentificador(),
						evaluado.getIdentificador());
			}
			List<ProcesoUsuario> procesos = usuario.getProcesosUsuarios();
			GestionProcesos gproc = new GestionProcesos();
			for (ProcesoUsuario procesoUsuario : procesos) {
				gproc.eliminarProcesoUsuarioAdministrador(
						usuario.getIdentificador(),
						procesoUsuario.getIdentificador());
			}
			entityManager.remove(usuario);
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
	public List<UsuarioProgramadorBO> listarUsuariosProgramadores(
			int usuarioAdministradorID) {
		Usuario user = entityManager
				.find(Usuario.class, usuarioAdministradorID);
		if (user != null) {
			entityManager.refresh(user);
			List<Usuario> usuarios = user.getUsuarios();
			// Valida que se encuentre un usuario.
			if (usuarios != null) {
				if (usuarios.size() > 0) {
					List<UsuarioProgramadorBO> lista = new ArrayList<UsuarioProgramadorBO>();
					for (int i = 0; i < usuarios.size(); i++) {
						UsuarioProgramadorBO resultado = new UsuarioProgramadorBO();
						Usuario l = usuarios.get(i);
						resultado.setApellidos(l.getApellidos());
						resultado.setCorreo_Electronico(l
								.getCorreo_Electronico());
						resultado.setIdentificador(l.getIdentificador());
						resultado.setCedula(l.getCedula());
						resultado.setNombres(l.getNombres());
						resultado.setCargo(l.getCargo());
						resultado.setCiudad(l.getCiudad());
						resultado.setEmpresa(l.getEmpresa());
						resultado.setEstado_Cuenta(l.getEstado_Cuenta());
						resultado.setFecha_Creacion(l.getFecha_Creacion());
						resultado.setIdentificador(l.getIdentificador());
						resultado.setNombres(l.getNombres());
						resultado.setPais(l.getPais());
						resultado.setTelefono(l.getTelefono());
						resultado.setTelefono_Celular(l.getTelefono_Celular());
						resultado.setTipo(l.getTipo());
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

	@Override
	public int cambiarEstadoCuenta(UsuarioProgramadorBO usuario,
			String estadoCuentaActiva) {
		EntityTransaction userTransaction = entityManager.getTransaction();
		try {
			userTransaction.begin();
			Usuario u = entityManager.find(Usuario.class,
					usuario.getIdentificador());
			u.setEstado_Cuenta(estadoCuentaActiva);
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
}
