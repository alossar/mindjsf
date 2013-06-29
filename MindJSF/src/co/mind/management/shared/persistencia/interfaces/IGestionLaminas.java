package co.mind.management.shared.persistencia.interfaces;

import java.util.List;

import co.mind.management.shared.dto.ImagenBO;
import co.mind.management.shared.dto.ImagenUsuarioBO;

public interface IGestionLaminas {

	public int agregarLamina(ImagenBO lamina);

	public int editarLamina(ImagenBO lamina);

	public ImagenBO consultarLamina(int laminaID);

	public int eliminarLamina(int laminaID);

	public List<ImagenBO> listarLaminas();

	public int agregarLaminaUsuarioAdministrador(int usuarioAdministradorID,
			ImagenUsuarioBO laminaUsuario);

	public int editarLaminaUsuarioAdministrador(int usuarioAdministradorID,
			ImagenUsuarioBO laminaUsuario);

	public ImagenUsuarioBO consultarLaminaUsuarioAdministrador(
			int usuarioAdministradorID, int laminaUsuarioID);

	/**
	 * Consulta el ID de la lamina de usuario que corresponde a la lamina dada y
	 * el id del usuario.
	 * 
	 * @param usuarioAdministradorID
	 *            Identifidor del  Usuario Administrador.
	 * @param laminaUsuarioID
	 *            Identifidor de la lamina.        
	 * @return 0 si el objeto se ingreso correctamente.
	 */
	public int consultarIDLamina(int usuarioAdministradorID, int laminaUsuarioID);

	public int eliminarLaminaUsuarioAdministrador(int usuarioAdministradorID,
			int laminaUsuarioID);

	public List<ImagenUsuarioBO> listarLaminasUsuarioAdministrador(
			int usuarioAdministradorID);
}
