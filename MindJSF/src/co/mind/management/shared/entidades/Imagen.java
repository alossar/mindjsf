package co.mind.management.shared.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the imagenes database table.
 * 
 */
@Entity
@Table(name="imagenes")
public class Imagen implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int identificador;

	@Column(name="ImagenURI")
	private String imagenURI;

	//bi-directional many-to-one association to ImagenUsuario
	@OneToMany(mappedBy="imagene")
	private List<ImagenUsuario> imagenesUsuarios;

	public Imagen() {
	}

	public int getIdentificador() {
		return this.identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	public String getImagenURI() {
		return this.imagenURI;
	}

	public void setImagenURI(String imagenURI) {
		this.imagenURI = imagenURI;
	}

	public List<ImagenUsuario> getImagenesUsuarios() {
		return this.imagenesUsuarios;
	}

	public void setImagenesUsuarios(List<ImagenUsuario> imagenesUsuarios) {
		this.imagenesUsuarios = imagenesUsuarios;
	}

	public ImagenUsuario addImagenesUsuario(ImagenUsuario imagenesUsuario) {
		getImagenesUsuarios().add(imagenesUsuario);
		imagenesUsuario.setImagene(this);

		return imagenesUsuario;
	}

	public ImagenUsuario removeImagenesUsuario(ImagenUsuario imagenesUsuario) {
		getImagenesUsuarios().remove(imagenesUsuario);
		imagenesUsuario.setImagene(null);

		return imagenesUsuario;
	}

}