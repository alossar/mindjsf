package co.mind.management.shared.persistencia.interfaces;

import co.mind.management.shared.dto.EvaluadoBO;
import co.mind.management.shared.dto.ParticipacionEnProcesoBO;
import co.mind.management.shared.dto.UsuarioBO;

public interface IGestionAccesos {
	
	public int verificarUsuarioBasico(EvaluadoBO usuarioBasico, ParticipacionEnProcesoBO participacion);
	
	public UsuarioBO verificarTipoUsuario(String correoUsuario, String contrasenaUsuario);
	
}
