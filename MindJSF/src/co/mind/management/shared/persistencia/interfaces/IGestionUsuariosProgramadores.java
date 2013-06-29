package co.mind.management.shared.persistencia.interfaces;

import java.util.List;

import co.mind.management.shared.dto.UsuarioAdministradorBO;
import co.mind.management.shared.dto.UsuarioProgramadorBO;

public interface IGestionUsuariosProgramadores {

	/**
	 * <b> CUO11 Agregar Usuario Programador (Definicion)</b>
	 * <p>
	 * Agrega un Usuario Programador a la Base de Datos, dada la cuenta de
	 * Usuario Administrador a la que pertenece.
	 * <p>
	 * Retorna un valor entero denotando si se pudo agregar exitosamente a la
	 * base de datos o no.
	 * 
	 * @param usuarioAdministradorID
	 *            Identificador del Usuario Administrador.
	 * @param usuarioProgramador
	 *            Datos del Usuario Programador contenidos en un Business
	 *            Object.
	 * @return 0 si el objeto se ingreso correctamente.
	 */
	public int agregarUsuarioProgramador(int usuarioAdministradorID,
			UsuarioProgramadorBO usuarioProgramador);

	/**
	 * <b> CUO12 Editar Usuario Programador (Definicion)</b>
	 * <p>
	 * Edita un Usuario Programador ya contenido en la Base de Datos, dada la
	 * cuenta de Usuario Administrador a la que pertenece.
	 * <p>
	 * Retorna un valor entero denotando si se pudo editar exitosamente a la
	 * base de datos o no.
	 * 
	 * @param usuarioAdministradorID
	 *            Identificador del Usuario Administrador.
	 * @param usuarioProgramador
	 *            Datos del Usuario Programador contenidos en un Business
	 *            Object.
	 * @return 0 si el objeto se edit� correctamente.
	 */
	public int editarUsuarioProgramador(int usuarioAdministradorID,
			UsuarioProgramadorBO usuarioProgramador);

	/**
	 * <b> CUO13 Consultar Usuario Programador (Definicion)</b>
	 * <p>
	 * Consulta el Usuario Programador de la Base de Datos, dada la cuenta de
	 * Usuario Administrador a la que pertenece y el identificador del usuario
	 * programador.
	 * <p>
	 * Retorna los datos del Usuario Programador.
	 * 
	 * @param usuarioAdministradorID
	 *            Identificador del Usuario Administrador.
	 * @param usuarioProgramadorID
	 *            Identificador del Usuario Programador.
	 * @return Un Business Object si el objeto se encontr�. null en caso
	 *         contrario.
	 */
	public UsuarioProgramadorBO consultarUsuarioProgramador(
			int usuarioAdministradorID, int usuarioProgramadorID);

	/**
	 * <b> CUO14 Eliminar Usuario Programador (Definicion)</b>
	 * <p>
	 * Elimina el Usuario Programador de la Base de Datos, dada la cuenta de
	 * Usuario Administrador a la que pertenece y el identificador del usuario
	 * programador.
	 * <p>
	 * Retorna un valor entero denotando si se pudo eliminar exitosamente a la
	 * base de datos o no.
	 * 
	 * @param usuarioAdministradorID
	 *            Identificador del Usuario Administrador.
	 * @param usuarioProgramadorID
	 *            Identificador del Usuario Programador.
	 * @return 0 si el objeto se elimin� correctamente.
	 */
	public int eliminarUsuarioProgramador(int usuarioAdministradorID,
			int usuarioProgramadorID);

	/**
	 * <b> CUO15 Listar Usuarios Programadores (Definicion)</b>
	 * <p>
	 * Consulta los Usuarios Programadores de la Base de Datos dada la cuenta de
	 * Usuario Administrador.
	 * <p>
	 * Retorna una lista de Usuarios Programadores encontrados.
	 * 
	 * @param usuarioAdministradorID
	 *            Identificador del Usuario Administrador.
	 * @return Una lista de Business Object. Puede estar vacia.
	 */
	public List<UsuarioProgramadorBO> listarUsuariosProgramadores(
			int usuarioAdministradorID);

	public int cambiarEstadoCuenta(UsuarioProgramadorBO usuario,
			String estadoCuentaActiva);
}
