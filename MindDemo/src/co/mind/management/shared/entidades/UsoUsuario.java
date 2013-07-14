package co.mind.management.shared.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the usos_usuarios database table.
 * 
 */
@Entity
@Table(name="usos_usuarios")
public class UsoUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int identificador;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_asignacion")
	private Date fechaAsignacion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_vencimiento")
	private Date fechaVencimiento;

	@Column(name="usos_asignados")
	private int usosAsignados;

	@Column(name="usos_redimidos")
	private int usosRedimidos;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="usuarios_identificador")
	private Usuario usuario;

	public UsoUsuario() {
	}

	public int getIdentificador() {
		return this.identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	public Date getFechaAsignacion() {
		return this.fechaAsignacion;
	}

	public void setFechaAsignacion(Date fechaAsignacion) {
		this.fechaAsignacion = fechaAsignacion;
	}

	public Date getFechaVencimiento() {
		return this.fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public int getUsosAsignados() {
		return this.usosAsignados;
	}

	public void setUsosAsignados(int usosAsignados) {
		this.usosAsignados = usosAsignados;
	}

	public int getUsosRedimidos() {
		return this.usosRedimidos;
	}

	public void setUsosRedimidos(int usosRedimidos) {
		this.usosRedimidos = usosRedimidos;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}