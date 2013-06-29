package co.mind.management.shared.dto;

import java.io.Serializable;
import java.util.List;

public class PruebaUsuarioBO implements Serializable {
	private static final long serialVersionUID = 1L;

	private int identificador;
	private String descripcion;
	private String nombre;
	private int usuarioAdministradorID;
	private List<PreguntaUsuarioBO> preguntas;
	private boolean editar;
	private int duracion;
	private int cantidad;

	public int getIdentificador() {
		return this.identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getUsuarioAdministradorID() {
		return this.usuarioAdministradorID;
	}

	public void setUsuarioAdministradorID(int usuarioAdministradorID) {
		this.usuarioAdministradorID = usuarioAdministradorID;
	}

	public List<PreguntaUsuarioBO> getPreguntas() {
		return preguntas;
	}

	public void setPreguntas(List<PreguntaUsuarioBO> preguntas) {
		this.preguntas = preguntas;
	}

	public int getCantidad() {
		cantidad = preguntas.size();
		return cantidad;
	}

	public int getDuracion() {
		duracion = 0;
		for (int i = 0; i < preguntas.size(); i++) {
			duracion += preguntas.get(i).getTiempoMaximo();
		}
		return duracion / 60;
	}

	@Override
	public boolean equals(Object o) {
		return this.getIdentificador() == ((PruebaUsuarioBO) o)
				.getIdentificador();
	}

	@Override
	public int hashCode() {
		return this.getIdentificador();
	}

	public boolean getEditar() {
		return this.editar;

	}

	public void setEditar(boolean b) {
		this.editar = b;

	}
}
