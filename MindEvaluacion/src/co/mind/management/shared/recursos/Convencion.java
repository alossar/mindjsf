package co.mind.management.shared.recursos;

public class Convencion {

	public static final String JTA_PU_NAME = "Mind";
	public static final int ID_USUARIO_ADMINISTRADOR_BASE = 123456789;

	public static final String MENSAJE_CREACION_CUENTA_USUARIO_ADMINISTRADOR = "Bienvenido a Tarmin. Hemos creado su cuenta.";
	public static final int CORRECTO = 0;
	public static final int INCORRECTO = 1;
	public static final int INCORRECTO_USUARIO_CEDULA_EXISTENTE = 2;
	public static final int INCORRECTO_USUARIO_CORREO_EXISTENTE = 3;

	public static final String CLAVE_USUARIO = "usuario_sesion";
	public static final String CLAVE_EVALUACION = "evaluacion_sesion";
	public static final String CLAVE_LOGIN_CORREO_USUARIO = "usuario";
	public static final String CLAVE_LOGIN_CONTRASENA = "password";
	public static final String CLAVE_PERMISOS_USUARIO_ADMINISTRADOR = "usuario_administrador";
	public static final String CLAVE_PERMISOS_USUARIO_MAESTRO = "usuario_maestro";
	public static final String CLAVE_PERMISOS_USUARIO_PROGRAMADOR = "usuario_programador";
	public static final String CLAVE_PERMISOS_USUARIO_MAESTRO_PRINCIPAL = "usuario_maestro_principal";
	public static final String CLAVE_PERMISOS_USUARIO_BASICO = "usuario_basico";

	public static final String CLAVE_REGISTRO_CORREO = "usuario";
	public static final String CLAVE_REGISTRO_CONTRASENA = "password";
	public static final String CLAVE_REGISTRO_NOMBRE = "nombre";
	public static final String CLAVE_REGISTRO_APELLIDOS = "apellidos";
	public static final String CLAVE_REGISTRO_TELEFONO = "telefono";
	public static final String CLAVE_REGISTRO_TELEFONO_CELULAR = "telefono_celular";
	public static final String CLAVE_REGISTRO_IDENTIFICADOR = "identificador";
	public static final String CLAVE_REGISTRO_EMPRESA = "empresa";
	public static final String CLAVE_REGISTRO_CARGO = "cargo";
	public static final String CLAVE_REGISTRO_PAIS = "pais";
	public static final String CLAVE_REGISTRO_CIUDAD = "ciudad";
	public static final String CLAVE_REGISTRO_DIRECCION_EMPRESA = "direccion_empresa";
	public static final String CLAVE_REGISTRO_DIRECCION_RESIDENCIA = "direccion_residencia";

	public static final String VALOR_LOGIN_CORREO_USUARIO = "usuario";
	public static final String VALOR_LOGIN_CONTRASENA = "password";
	public static final String VALOR_PERMISOS_USUARIO_ADMINISTRADOR = "usuario_administrador";
	public static final String VALOR_PERMISOS_USUARIO_MAESTRO = "usuario_maestro";
	public static final String VALOR_PERMISOS_USUARIO_PROGRAMADOR = "usuario_programador";
	public static final String VALOR_PERMISOS_USUARIO_MAESTRO_PRINCIPAL = "usuario_maestro_principal";
	public static final String VALOR_PERMISOS_USUARIO_BASICO = "usuario_basico";
	public static final String VALOR_REGISTRO_CORREO = "usuario";
	public static final String VALOR_REGISTRO_CONTRASENA = "password";
	public static final String VALOR_REGISTRO_NOMBRE = "nombre";
	public static final String VALOR_REGISTRO_APELLIDOS = "apellidos";
	public static final String VALOR_REGISTRO_TELEFONO = "telefono";
	public static final String VALOR_REGISTRO_TELEFONO_CELULAR = "telefono_celular";
	public static final String VALOR_REGISTRO_IDENTIFICADOR = "identificador";
	public static final String VALOR_REGISTRO_EMPRESA = "empresa";
	public static final String VALOR_REGISTRO_CARGO = "cargo";
	public static final String VALOR_REGISTRO_PAIS = "pais";
	public static final String VALOR_REGISTRO_CIUDAD = "ciudad";
	public static final String VALOR_REGISTRO_DIRECCION_EMPRESA = "direccion_empresa";
	public static final String VALOR_REGISTRO_DIRECCION_RESIDENCIA = "direccion_residencia";

	public static final String TIPO_USUARIO_ADMINISTRADOR = "usuario_administrador";
	public static final String TIPO_USUARIO_MAESTRO = "usuario_maestro";
	public static final String TIPO_USUARIO_PROGRAMADOR = "usuario_programador";
	public static final String TIPO_USUARIO_MAESTRO_PRINCIPAL = "usuario_maestro_principal";

	public static final String ESTADO_CUENTA_ACTIVA = "cuenta_activa";
	public static final String ESTADO_CUENTA_INACTIVA = "cuenta_inactiva";

	public static final String ESTADO_NOTIFICACION_ENVIADA = "correo_si";
	public static final String ESTADO_NOTIFICACION_NO_ENVIADA = "correo_no";

	public static final String ESTADO_PARTICIPACION_EN_PROCESO_INACTIVA = "participacion_inactiva";
	public static final String ESTADO_PARTICIPACION_EN_PROCESO_EN_EJECUCION = "participacion_ejecucion";
	public static final String ESTADO_PARTICIPACION_EN_PROCESO_EN_ESPERA = "participacion_espera";

	public static final int VERIFICACION_USUARIO_BASICO_CORRECTA = 0;
	public static final int VERIFICACION_USUARIO_BASICO_NO_EXISTE = 1;
	public static final int VERIFICACION_USUARIO_BASICO_CODIGO_ACCESO_NO_VALIDO = 2;
	public static final int VERIFICACION_USUARIO_BASICO_PARTICIPACION_NO_EXISTE = 3;
	public static final int VERIFICACION_USUARIO_BASICO_SIN_TERMINAR_PRUEBA = 4;

	public static final String SOLICITUD_INCREMENTO_USOS = "incremento_usos";
	public static final String SOLICITUD_ELIMINACION_CUENTA = "eliminacion_cuenta";
	public static final String SOLICITUD_CAMBIO_PLAN = "cambio_plan";
	public static final String SOLICITUD_PLAN = "nuevo_plan";
	public static final String SOLICITUD_VERIFICACION = "verificacion_proceso";

	public static final String ESTADO_SOLICITUD_NO_REALIZADA = "no_realizada";
	public static final String ESTADO_SOLICITUD_PENDIENTE = "pendiente";
	public static final String ESTADO_SOLICITUD_ACEPTADA = "aceptada";
	public static final String ESTADO_SOLICITUD_REALIZADA = "realizada";
	public static final String ESTADO_SOLICITUD_RECHAZADA = "rechazada";

	public static final int MAXIMA_LONGITUD_RESPUESTA = 2000;
	public static final int MAXIMA_LONGITUD_CEDULA = 10;
	public static final int MAXIMA_LONGITUD_DESCRIPCION_PROCESO = 250;
	public static final int MAXIMA_LONGITUD_DESCRIPCION_PRUEBA = 900;
	public static final int MAXIMA_LONGITUD_NOMBRE = 20;
	public static final int MAXIMA_LONGITUD_NOMBRE_USUARIO = 19;
	public static final int MAXIMA_LONGITUD_CIUDAD = 40;
	public static final int MAXIMA_LONGITUD_EMPRESA = 19;
	public static final int MAXIMA_LONGITUD_CARGO = 40;
	public static final String DIRECCION_WEB = "http://www.mindmanagement.co";
	public static final String CONEXION_URL = "jdbc:mysql://localhost:3306/mind_measurement";
	public static final String CONEXION_USER = "root";
	public static final String CONEXION_PASS = "2mmysqlserver";
	// Mensajes
	public static final String MENSAJE_FALLO_LOGIN = "El correo o la contraseña ingresada es incorrecta. Por favor intente de nuevo.";
	public static final int MAXIMA_LONGITUD_PREGUNTA = 2000;
	public static final int MAXIMO_EVALUADOS_PROCESO = 100;

	public static String obtenerTiempoMinutoSegundos(double tiempoLamina) {
		int minutos = (int) tiempoLamina / 60;
		int segundos = (int) (tiempoLamina - (minutos * 60));
		if (minutos > 0) {
			if (segundos != 0) {
				minutos++;
			}
			return minutos + " Min";
		} else {
			return segundos + " Seg";
		}
	}

	public static String obtenerTiempoHoraMinutoSegundos(double tiempoLamina) {
		int horas = (int) tiempoLamina / 3600;
		int minutos = (int) ((tiempoLamina - (horas * 3600)) / 60);
		int segundos = (int) (tiempoLamina - (minutos * 60));
		return horas + " Horas " + minutos + " Minutos " + segundos
				+ " Segundos";
	}

	public static String rgbToString(int tiempoLamina, int tiempoTotal) {
		// extract r, g, b information
		// A and B is a ARGB-packed int so we use bit operation to extract
		int Ar = 230;
		int Ag = 230;
		int Ab = 230;
		int Br = 255;
		int Bg = 0;
		int Bb = 0;
		float cociente = ((float) tiempoLamina) / ((float) tiempoTotal);
		int Yr = (int) (Ar + ((Br - Ar) * cociente));
		int Yg = (int) (Ag + ((Bg - Ag) * cociente));
		int Yb = (int) (Ab + ((Bb - Ab) * cociente));
		String hexYr = Integer.toHexString(Yr);
		if (hexYr.length() == 1) {
			hexYr = "0" + hexYr;
		}
		String hexYg = Integer.toHexString(Yg);
		if (hexYg.length() == 1) {
			hexYg = "0" + hexYg;
		}
		String hexYb = Integer.toHexString(Yb);
		if (hexYb.length() == 1) {
			hexYb = "0" + hexYb;
		}

		String retorno = "#" + hexYr + hexYg + hexYb;
		// pack ARGB with hardcoded alpha
		return retorno;

	}

}
