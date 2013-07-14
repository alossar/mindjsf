package co.mind.management.shared.persistencia.interfaces;

import java.util.List;

import co.mind.management.shared.dto.PreguntaUsuarioBO;
import co.mind.management.shared.dto.ProcesoUsuarioBO;
import co.mind.management.shared.dto.PruebaUsuarioBO;

public interface IGestionPruebas {
	public int agregarPruebaUsuarioAdministrador(int usuarioAdministradorID,
			PruebaUsuarioBO prueba);

	public int editarPruebaUsuarioAdministrador(int usuarioAdministradorID,
			PruebaUsuarioBO prueba);

	public PruebaUsuarioBO consultarPruebaUsuarioAdministrador(
			int usuarioAdministradorID, int pruebaID);

	public int eliminarPruebaUsuarioAdministrador(int usuarioAdministradorID,
			int pruebaID);

	public List<PruebaUsuarioBO> listarPruebasUsuarioAdministrador(
			int usuarioAdministradorID);

	public int eliminarPreguntaDePruebaUsuarioAdministrador(
			int usuarioAdministradorID, int preguntaID);

	public List<PreguntaUsuarioBO> listarPreguntasPrueba(
			int usuarioAdministradorID, int pruebaID);

	public List<PreguntaUsuarioBO> listarPreguntasRestantesPrueba(
			int usuarioAdministradorID, int pruebaID, int usuarioBasicoID);

	List<PreguntaUsuarioBO> listarPreguntasPorProceso(
			int usuarioAdministradorID, int procesoID);

	int agregarPruebaUsuarioAdministrador(int usuarioAdministradorID,
			PruebaUsuarioBO prueba, List<PreguntaUsuarioBO> categorias);

	int agregarPruebaAProceso(int usuarioAdministradorID,
			PruebaUsuarioBO prueba, ProcesoUsuarioBO proceso);

	public Object listarPruebasPorNombreParcial(int identificador,
			String keyword);

}
