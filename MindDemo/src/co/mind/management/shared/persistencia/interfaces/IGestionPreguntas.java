package co.mind.management.shared.persistencia.interfaces;

import java.util.List;

import co.mind.management.shared.dto.PreguntaUsuarioBO;
import co.mind.management.shared.dto.PruebaUsuarioBO;

public interface IGestionPreguntas {

	public int editarPreguntaUsuarioAdministrador(int usuarioAdministradorID,
			PreguntaUsuarioBO preguntaUsuario);

	public PreguntaUsuarioBO consultarPreguntaUsuarioAdministrador(
			int usuarioAdministradorID, int preguntaUsuarioID);

	public int eliminarPreguntaUsuarioAdministrador(int usuarioAdministradorID,
			int preguntaUsuarioID);

	public List<PreguntaUsuarioBO> listarPreguntasUsuarioAdministrador(
			int usuarioAdministrador);

	int agregarPreguntaUsuarioAdministrador(int usuarioAdministradorID,
			PreguntaUsuarioBO preguntaUsuario, PruebaUsuarioBO categoria);

}
