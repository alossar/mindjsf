package co.mind.management.shared.persistencia.interfaces;

import java.util.List;

import co.mind.management.shared.dto.UsuarioMaestroBO;

public interface IGestionUsuariosMaestros {
	/**
	 * <b> CUO16 Agregar Usuario Maestro (Definicion)</b>
	 * <p>
	 * Agrega un Usuario Maestro a la Base de Datos.
	 * <p>
	 * Retorna un valor entero denotando si se pudo agregar exitosamente a la
	 * base de datos o no.
	 * 
	 * @param usuarioMaestro
	 *            Datos del Usuario Maestro contenidos en un Business Object.
	 * @return 0 si el objeto se ingreso correctamente.
	 */
	public int agregarUsuarioMaestro(UsuarioMaestroBO usuarioMaestro);

	/**
	 * <b> CUO17 Editar Usuario Maestro (Definicion)</b>
	 * <p>
	 * Edita un Usuario Maestro ya contenido en la Base de Datos.
	 * <p>
	 * Retorna un valor entero denotando si se pudo editar exitosamente a la
	 * base de datos o no.
	 * 
	 * @param usuarioMaestro
	 *            Datos del Usuario Maestro contenidos en un Business
	 *            Object.
	 * @return 0 si el objeto se edit� correctamente.
	 */
	public int editarUsuarioMaestro(
			UsuarioMaestroBO usuarioMaestro);

	/**
	 * <b> CUO18 Consultar Usuario Maestro (Definicion)</b>
	 * <p>
	 * Consulta el Usuario Maestro de la Base de Datos dado el identificador del usuario
	 * Maestro.
	 * <p>
	 * Retorna los datos del Usuario Maestro.
	 * 
	 * @param usuarioMaestroID
	 *            Identificador del Usuario Maestro.
	 * @return Un Business Object si el objeto se encontr�. null en caso
	 *         contrario.
	 */
	public UsuarioMaestroBO consultarUsuarioMaestro(
			int usuarioMaestroID);

	/**
	 * <b> CUO19 Eliminar Usuario Maestro (Definicion)</b>
	 * <p>
	 * Elimina el Usuario Maestro de la Base de Datos, dado el identificador del usuario
	 * maestro.
	 * <p>
	 * Retorna un valor entero denotando si se pudo eliminar exitosamente a la
	 * base de datos o no.
	 * 
	 * @param usuarioMaestroID
	 *            Identificador del Usuario Maestro.
	 * @return 0 si el objeto se elimin� correctamente.
	 */
	public int eliminarUsuarioMaestro(int usuarioMaestroID);

	/**
	 * <b> CUO20 Listar Usuarios Maestros (Definicion)</b>
	 * <p>
	 * Consulta los Usuarios Maestros de la Base de Datos.
	 * <p>
	 * Retorna una lista de Usuarios Maestroes encontrados.
	 * 
	 * @return Una lista de Business Object. Puede estar vacia.
	 */
	public List<UsuarioMaestroBO> listarUsuariosMaestros();
}
