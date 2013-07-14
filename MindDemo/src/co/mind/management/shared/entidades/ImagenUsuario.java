package co.mind.management.shared.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the imagenes_usuarios database table.
 * 
 */
@Entity
@Table(name="imagenes_usuarios")
public class ImagenUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int identificador;

	//bi-directional many-to-one association to Imagen
	@ManyToOne
	@JoinColumn(name="imagenes_identificador")
	private Imagen imagene;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="usuarios_identificador")
	private Usuario usuario;

	//bi-directional many-to-one association to PreguntaUsuario
	@OneToMany(mappedBy="imagenesUsuario")
	private List<PreguntaUsuario> preguntasUsuarios;

	public ImagenUsuario() {
	}

	public int getIdentificador() {
		return this.identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	public Imagen getImagene() {
		return this.imagene;
	}

	public void setImagene(Imagen imagene) {
		this.imagene = imagene;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<PreguntaUsuario> getPreguntasUsuarios() {
		return this.preguntasUsuarios;
	}

	public void setPreguntasUsuarios(List<PreguntaUsuario> preguntasUsuarios) {
		this.preguntasUsuarios = preguntasUsuarios;
	}

	public PreguntaUsuario addPreguntasUsuario(PreguntaUsuario preguntasUsuario) {
		getPreguntasUsuarios().add(preguntasUsuario);
		preguntasUsuario.setImagenesUsuario(this);

		return preguntasUsuario;
	}

	public PreguntaUsuario removePreguntasUsuario(PreguntaUsuario preguntasUsuario) {
		getPreguntasUsuarios().remove(preguntasUsuario);
		preguntasUsuario.setImagenesUsuario(null);

		return preguntasUsuario;
	}

}