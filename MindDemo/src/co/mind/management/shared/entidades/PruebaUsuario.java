package co.mind.management.shared.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the pruebas_usuarios database table.
 * 
 */
@Entity
@Table(name="pruebas_usuarios")
public class PruebaUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int identificador;

	@Column(name="Descripcion")
	private String descripcion;

	@Column(name="Nombre")
	private String nombre;

	//bi-directional many-to-one association to PreguntaUsuario
	@OneToMany(mappedBy="pruebasUsuario")
	private List<PreguntaUsuario> preguntasUsuarios;

	//bi-directional many-to-one association to ProcesoUsuarioHasPruebaUsuario
	@OneToMany(mappedBy="pruebasUsuario")
	private List<ProcesoUsuarioHasPruebaUsuario> procesosUsuariosHasPruebasUsuarios;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="usuarios_identificador")
	private Usuario usuario;

	public PruebaUsuario() {
	}

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

	public List<PreguntaUsuario> getPreguntasUsuarios() {
		return this.preguntasUsuarios;
	}

	public void setPreguntasUsuarios(List<PreguntaUsuario> preguntasUsuarios) {
		this.preguntasUsuarios = preguntasUsuarios;
	}

	public PreguntaUsuario addPreguntasUsuario(PreguntaUsuario preguntasUsuario) {
		getPreguntasUsuarios().add(preguntasUsuario);
		preguntasUsuario.setPruebasUsuario(this);

		return preguntasUsuario;
	}

	public PreguntaUsuario removePreguntasUsuario(PreguntaUsuario preguntasUsuario) {
		getPreguntasUsuarios().remove(preguntasUsuario);
		preguntasUsuario.setPruebasUsuario(null);

		return preguntasUsuario;
	}

	public List<ProcesoUsuarioHasPruebaUsuario> getProcesosUsuariosHasPruebasUsuarios() {
		return this.procesosUsuariosHasPruebasUsuarios;
	}

	public void setProcesosUsuariosHasPruebasUsuarios(List<ProcesoUsuarioHasPruebaUsuario> procesosUsuariosHasPruebasUsuarios) {
		this.procesosUsuariosHasPruebasUsuarios = procesosUsuariosHasPruebasUsuarios;
	}

	public ProcesoUsuarioHasPruebaUsuario addProcesosUsuariosHasPruebasUsuario(ProcesoUsuarioHasPruebaUsuario procesosUsuariosHasPruebasUsuario) {
		getProcesosUsuariosHasPruebasUsuarios().add(procesosUsuariosHasPruebasUsuario);
		procesosUsuariosHasPruebasUsuario.setPruebasUsuario(this);

		return procesosUsuariosHasPruebasUsuario;
	}

	public ProcesoUsuarioHasPruebaUsuario removeProcesosUsuariosHasPruebasUsuario(ProcesoUsuarioHasPruebaUsuario procesosUsuariosHasPruebasUsuario) {
		getProcesosUsuariosHasPruebasUsuarios().remove(procesosUsuariosHasPruebasUsuario);
		procesosUsuariosHasPruebasUsuario.setPruebasUsuario(null);

		return procesosUsuariosHasPruebasUsuario;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}