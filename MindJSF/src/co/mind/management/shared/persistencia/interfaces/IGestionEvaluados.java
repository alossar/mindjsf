package co.mind.management.shared.persistencia.interfaces;

import java.util.List;

import co.mind.management.shared.dto.EvaluadoBO;

public interface IGestionEvaluados {
	/**
	 * <b> CUOO1 Agregar Usuario Basico (Definicion)</b>
	 * <p>
	 * Agrega un Usuario Basico a la Base de Datos, dada la cuenta de Usuario
	 * Administrador a la que pertenece.
	 * <p>
	 * Retorna un valor entero denotando si se pudo agregar exitosamente a la
	 * base de datos o no.
	 * 
	 * @param usuarioAdministradorID
	 *            Identificador del Usuario Administrador.
	 * @param usuarioBasico
	 *            Datos del Usuario Basico contenidos en un Business Object.
	 * @return 0 si el objeto se ingreso correctamente.
	 */
	public int agregarUsuarioBasico(int usuarioAdministradorID,
			EvaluadoBO usuarioBasico);

	/**
	 * <b> CUOO2 Editar Usuario Basico (Definicion)</b>
	 * <p>
	 * Edita un Usuario Basico ya contenido en la Base de Datos, dada la cuenta
	 * de Usuario Administrador a la que pertenece.
	 * <p>
	 * Retorna un valor entero denotando si se pudo editar exitosamente a la
	 * base de datos o no.
	 * 
	 * @param usuarioAdministradorID
	 *            Identificador del Usuario Administrador.
	 * @param usuarioBasico
	 *            Datos del Usuario Basico contenidos en un Business Object.
	 * @return 0 si el objeto se edit� correctamente.
	 */
	public int editarUsuarioBasico(int usuarioAdministradorID,
			EvaluadoBO usuarioBasico);

	/**
	 * <b> CUOO3 Consultar Usuario Basico (Definicion)</b>
	 * <p>
	 * Consulta el Usuario Basico de la Base de Datos, dada la cuenta de Usuario
	 * Administrador a la que pertenece y el identificador del usuario Basico.
	 * <p>
	 * Retorna los datos del Usuario Basico.
	 * 
	 * @param usuarioAdministradorID
	 *            Identificador del Usuario Administrador.
	 * @param usuarioBasicoID
	 *            Identificador del Usuario Basico.
	 * @return Un Business Object si el objeto se encontr�. null en caso
	 *         contrario.
	 */
	public EvaluadoBO consultarUsuarioBasico(int usuarioAdministradorID,
			int usuarioBasicoID);

	/**
	 * <b> CUOO4 Eliminar Usuario Basico (Definicion)</b>
	 * <p>
	 * Elimina el Usuario Basico de la Base de Datos, dada la cuenta de Usuario
	 * Administrador a la que pertenece y el identificador del usuario basico.
	 * <p>
	 * Retorna un valor entero denotando si se pudo eliminar exitosamente a la
	 * base de datos o no.
	 * 
	 * @param usuarioAdministradorID
	 *            Identificador del Usuario Administrador.
	 * @param usuarioBasicoID
	 *            Identificador del Usuario Basico.
	 * @return 0 si el objeto se elimin� correctamente.
	 */
	public int eliminarUsuarioBasico(int usuarioAdministradorID,
			int usuarioBasicoID);

	/**
	 * <b> CUOO5 Listar Usuarios Basicos (Definicion)</b>
	 * <p>
	 * Consulta los Usuarios Basicos de la Base de Datos dada la cuenta de
	 * Usuario Administrador.
	 * <p>
	 * Retorna una lista de Usuarios Basicos encontrados.
	 * 
	 * @param usuarioAdministradorID
	 *            Identificador del Usuario Administrador.
	 * @return Una lista de Business Object. Puede estar vacia.
	 */
	public List<EvaluadoBO> listarUsuariosBasicos(
			int usuarioAdministradorID);

	public Object listarEvaluadosPorCedula(int identificador, int valor);

	public Object listarEvaluadosPorCorreo(int identificador, String keyword);
}
