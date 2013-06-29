package co.mind.management.shared.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the usuarios database table.
 * 
 */
@Entity
@Table(name="usuarios")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int identificador;

	@Column(name="Apellidos")
	private String apellidos;

	@Column(name="Cargo")
	private String cargo;

	@Column(name="Ciudad")
	private String ciudad;

	@Column(name="Contrasena")
	private String contrasena;

	@Column(name="Correo_Electronico")
	private String correo_Electronico;

	@Column(name="Empresa")
	private String empresa;

	@Column(name="Estado_Cuenta")
	private String estado_Cuenta;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Fecha_Creacion")
	private Date fecha_Creacion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_vencimiento")
	private Date fechaVencimiento;

	@Column(name="Nombres")
	private String nombres;

	@Column(name="Pais")
	private String pais;

	@Column(name="Telefono")
	private String telefono;

	@Column(name="Telefono_Celular")
	private String telefono_Celular;

	@Column(name="Tipo")
	private String tipo;

	//bi-directional many-to-one association to Evaluado
	@OneToMany(mappedBy="usuario")
	private List<Evaluado> evaluados;

	//bi-directional many-to-one association to ImagenUsuario
	@OneToMany(mappedBy="usuario")
	private List<ImagenUsuario> imagenesUsuarios;

	//bi-directional many-to-one association to ProcesoUsuario
	@OneToMany(mappedBy="usuario")
	private List<ProcesoUsuario> procesosUsuarios;

	//bi-directional many-to-one association to PruebaUsuario
	@OneToMany(mappedBy="usuario")
	private List<PruebaUsuario> pruebasUsuarios;

	//bi-directional many-to-one association to UsoUsuario
	@OneToMany(mappedBy="usuario")
	private List<UsoUsuario> usosUsuarios;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="usuarios_identificador")
	private Usuario usuario;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="usuario")
	private List<Usuario> usuarios;

	public Usuario() {
	}

	public int getIdentificador() {
		return this.identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCargo() {
		return this.cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getCiudad() {
		return this.ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getContrasena() {
		return this.contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getCorreo_Electronico() {
		return this.correo_Electronico;
	}

	public void setCorreo_Electronico(String correo_Electronico) {
		this.correo_Electronico = correo_Electronico;
	}

	public String getEmpresa() {
		return this.empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getEstado_Cuenta() {
		return this.estado_Cuenta;
	}

	public void setEstado_Cuenta(String estado_Cuenta) {
		this.estado_Cuenta = estado_Cuenta;
	}

	public Date getFecha_Creacion() {
		return this.fecha_Creacion;
	}

	public void setFecha_Creacion(Date fecha_Creacion) {
		this.fecha_Creacion = fecha_Creacion;
	}

	public Date getFechaVencimiento() {
		return this.fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getPais() {
		return this.pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTelefono_Celular() {
		return this.telefono_Celular;
	}

	public void setTelefono_Celular(String telefono_Celular) {
		this.telefono_Celular = telefono_Celular;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<Evaluado> getEvaluados() {
		return this.evaluados;
	}

	public void setEvaluados(List<Evaluado> evaluados) {
		this.evaluados = evaluados;
	}

	public Evaluado addEvaluado(Evaluado evaluado) {
		getEvaluados().add(evaluado);
		evaluado.setUsuario(this);

		return evaluado;
	}

	public Evaluado removeEvaluado(Evaluado evaluado) {
		getEvaluados().remove(evaluado);
		evaluado.setUsuario(null);

		return evaluado;
	}

	public List<ImagenUsuario> getImagenesUsuarios() {
		return this.imagenesUsuarios;
	}

	public void setImagenesUsuarios(List<ImagenUsuario> imagenesUsuarios) {
		this.imagenesUsuarios = imagenesUsuarios;
	}

	public ImagenUsuario addImagenesUsuario(ImagenUsuario imagenesUsuario) {
		getImagenesUsuarios().add(imagenesUsuario);
		imagenesUsuario.setUsuario(this);

		return imagenesUsuario;
	}

	public ImagenUsuario removeImagenesUsuario(ImagenUsuario imagenesUsuario) {
		getImagenesUsuarios().remove(imagenesUsuario);
		imagenesUsuario.setUsuario(null);

		return imagenesUsuario;
	}

	public List<ProcesoUsuario> getProcesosUsuarios() {
		return this.procesosUsuarios;
	}

	public void setProcesosUsuarios(List<ProcesoUsuario> procesosUsuarios) {
		this.procesosUsuarios = procesosUsuarios;
	}

	public ProcesoUsuario addProcesosUsuario(ProcesoUsuario procesosUsuario) {
		getProcesosUsuarios().add(procesosUsuario);
		procesosUsuario.setUsuario(this);

		return procesosUsuario;
	}

	public ProcesoUsuario removeProcesosUsuario(ProcesoUsuario procesosUsuario) {
		getProcesosUsuarios().remove(procesosUsuario);
		procesosUsuario.setUsuario(null);

		return procesosUsuario;
	}

	public List<PruebaUsuario> getPruebasUsuarios() {
		return this.pruebasUsuarios;
	}

	public void setPruebasUsuarios(List<PruebaUsuario> pruebasUsuarios) {
		this.pruebasUsuarios = pruebasUsuarios;
	}

	public PruebaUsuario addPruebasUsuario(PruebaUsuario pruebasUsuario) {
		getPruebasUsuarios().add(pruebasUsuario);
		pruebasUsuario.setUsuario(this);

		return pruebasUsuario;
	}

	public PruebaUsuario removePruebasUsuario(PruebaUsuario pruebasUsuario) {
		getPruebasUsuarios().remove(pruebasUsuario);
		pruebasUsuario.setUsuario(null);

		return pruebasUsuario;
	}

	public List<UsoUsuario> getUsosUsuarios() {
		return this.usosUsuarios;
	}

	public void setUsosUsuarios(List<UsoUsuario> usosUsuarios) {
		this.usosUsuarios = usosUsuarios;
	}

	public UsoUsuario addUsosUsuario(UsoUsuario usosUsuario) {
		getUsosUsuarios().add(usosUsuario);
		usosUsuario.setUsuario(this);

		return usosUsuario;
	}

	public UsoUsuario removeUsosUsuario(UsoUsuario usosUsuario) {
		getUsosUsuarios().remove(usosUsuario);
		usosUsuario.setUsuario(null);

		return usosUsuario;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setUsuario(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setUsuario(null);

		return usuario;
	}

}