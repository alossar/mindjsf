package co.mind.management.shared.persistencia.interfaces;

import java.util.List;

import co.mind.management.shared.dto.UsoBO;

public interface IGestionUsos {

	public int agregarUso(int usuarioID, UsoBO uso);

	public int editarPermiso(UsoBO uso);

	public UsoBO consultarUso(int usoID);

	public int eliminarPermiso(int usoID);

	public List<UsoBO> listarUsos(int usuarioID);

	public boolean consultarCapacidadUsos(int usuarioConsulta, int size);

}
