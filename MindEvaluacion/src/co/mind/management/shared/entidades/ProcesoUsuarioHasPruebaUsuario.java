package co.mind.management.shared.entidades;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the procesos_usuarios_has_pruebas_usuarios database table.
 * 
 */
@Entity
@Table(name="procesos_usuarios_has_pruebas_usuarios")
public class ProcesoUsuarioHasPruebaUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int identificador;

	//bi-directional many-to-one association to ProcesoUsuario
	@ManyToOne
	@JoinColumn(name="procesos_usuarios_identificador")
	private ProcesoUsuario procesosUsuario;

	//bi-directional many-to-one association to PruebaUsuario
	@ManyToOne
	@JoinColumn(name="pruebas_usuarios_identificador")
	private PruebaUsuario pruebasUsuario;

	public ProcesoUsuarioHasPruebaUsuario() {
	}

	public int getIdentificador() {
		return this.identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	public ProcesoUsuario getProcesosUsuario() {
		return this.procesosUsuario;
	}

	public void setProcesosUsuario(ProcesoUsuario procesosUsuario) {
		this.procesosUsuario = procesosUsuario;
	}

	public PruebaUsuario getPruebasUsuario() {
		return this.pruebasUsuario;
	}

	public void setPruebasUsuario(PruebaUsuario pruebasUsuario) {
		this.pruebasUsuario = pruebasUsuario;
	}

}