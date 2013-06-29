package co.mind.management.shared.persistencia.interfaces;

import java.util.List;

import co.mind.management.shared.dto.ProcesoUsuarioBO;
import co.mind.management.shared.dto.ProcesoUsuarioHasPruebaUsuarioBO;

public interface IGestionProcesos {
	public int agregarProcesoUsuarioAdministrador(int usuarioAdministradorID,
			ProcesoUsuarioBO proceso);
	
	public int editarProcesoUsuarioAdministrador(int usuarioAdministradorID,
			ProcesoUsuarioBO proceso);

	public ProcesoUsuarioBO consultarProcesoUsuarioAdministrador(
			int usuarioAdministradorID, int procesoID);

	public int eliminarProcesoUsuarioAdministrador(int usuarioAdministradorID,
			int procesoID);

	public List<ProcesoUsuarioBO> listarProcesoUsuarioAdministrador(
			int usuarioAdministradorID);

	public int agregarProcesoConPruebas(
			int identificador,
			ProcesoUsuarioBO proceso,
			List<ProcesoUsuarioHasPruebaUsuarioBO> procesoUsuarioHasPruebaUsuario);

	public Object listarProcesosPorNombreParcial(int identificador,
			String keyword);

	public Object listarProcesosParaRevisar();

}