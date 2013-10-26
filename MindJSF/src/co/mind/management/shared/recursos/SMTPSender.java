package co.mind.management.shared.recursos;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import co.mind.management.shared.dto.ParticipacionEnProcesoBO;
import co.mind.management.shared.dto.ProcesoUsuarioBO;
import co.mind.management.shared.dto.ResultadoBO;
import co.mind.management.shared.dto.UsuarioBO;

public class SMTPSender {

	private static String urlEvaluacion = "http://www.mindmanagement.co/MindEvaluacion";
	private static String urlMind = "http://www.mindmanagement.co/MindJSF";

	public static int enviarCorreoParticipacionAProceso(
			ParticipacionEnProcesoBO participacion, String empresa) {

		if (empresa == null) {
			empresa = "Se";
		}
		String i = "<!DOCTYPE HTML><html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'><style>#nombre p{	float:left;	margin-left: 10px;} #mail{ clear:both;	}#mail p{ float:left;	margin-left: 10px;	}#mailLink p{	padding-top: 30px;}	</style></head>	<body style='margin:0px; padding:0px;'>	<center> <div id='contenedorTotalMail' style='position: relative; width: 960px margin: 0px auto;'>		<div id='cuerpoMail' style='width: 408px; height: 643px; margin:auto;'>		<div id='mailContenedorDatos' style='	width:408px; height:380px; background-color: white; box-shadow: 0px 0px 10px rgba(0,0,0,0.3);'>		<div id='MailTitle' style='width:137px; height:62px; margin:25px auto; padding-top:20px;'><img src='https://dl.dropbox.com/u/32952272/logo2M.png'></div>	<div id='mailTitleGracias' style='color:rgb(91,91,91); font-family:Arial; font-size:14px; text-align:center; margin-bottom:50px;'><h1>"
				+ empresa
				+ " ha programado una evaluación para usted</h1></div> <div class='registrateInfo' style='width:337px;font-family:Arial; font-size:12px; margin-top:10px; margin-left:35px;'> "
				+ "<p>Para acceder, ingrese su cédula de ciudadanía, su correo electrónico y el código de acceso.</p>	"
				+ "<div id='nombre'><p>Correo Electrónico:</p>	<p style=' color:rgb(17,170,209); font-style:italic;' >"
				+ participacion.getUsuarioBasico().getCorreoElectronico()
				+ "</p></div>	<div id='mail'>	<p>Código de Acceso: </p><p id='pass' style=' color:rgb(17,170,209); font-style:italic;'>"
				+ participacion.getCodigo_Acceso()
				+ "</p>	</div></div><a style='color:rgb(17,170,209);' href='"
				+ urlEvaluacion
				+ "'><div id='mailLink'><p style='font-family:Arial; font-size:14px; clear:both; text-align:center; color:rgb(17,170,209); cursor:pointer;'>Click Aqui para Ingresar</p></div></a><div id='mailFooter'><p style='height:37px; width:100%; margin-top:25px;  padding-top:15px; color:grey; text-align:center;  font-family:Arial; font-size:12px;'>www.mindmanagement.co</p></div></div></div><!--<div id='botonEntrarServicios'><p>Registrate</p></div>--></div><!-- Cierro Cont Total---></center></body></html> ";

		return enviarCorreo(participacion.getUsuarioBasico()
				.getCorreoElectronico(), "Bienvenido a Mind Management", i);

	}

	public static int enviarCorreoCreacionCuenta(String nombre,
			String apellido, String correo, String contrasena, int usos,
			boolean administrador) {

		String tipoCuenta = "administración";
		String mostrarUsos = "</p></div>	<div id='mail'>	<p>Cantidad de Usos: </p><p id='pass' style=' color:rgb(17,170,209); font-style:italic;'>"
				+ usos;
		if (!administrador) {
			tipoCuenta = "de programador";
			mostrarUsos = "";
		}
		String i = "<!DOCTYPE HTML><html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'><style>#nombre p{	float:left;	margin-left: 10px;} #mail{ clear:both;	}#mail p{ float:left;	margin-left: 10px;	}#mailLink p{	padding-top: 30px;}	</style></head>	<center> <body style='margin:0px; padding:0px;'>	<div id='contenedorTotalMail' style='position: relative; width: 960px margin: 0px auto;'>		<div id='cuerpoMail' style='width: 408px; height: 643px; margin:auto;'>		<div id='mailContenedorDatos' style='	width:408px; height:380px; background-color: white; box-shadow: 0px 0px 10px rgba(0,0,0,0.3);'>		<div id='MailTitle' style='width:137px; height:62px; margin:25px auto; padding-top:20px;'><img src='https://dl.dropbox.com/u/32952272/logo2M.png'></div>	<div id='mailTitleGracias' style='color:rgb(91,91,91); font-family:Arial; font-size:14px; text-align:center; margin-bottom:50px;'><h1>Bienvenido a Mind Management</h1>"
				+ "<h3>Se ha creado una cuenta de "
				+ tipoCuenta
				+ ".</h3>"
				+ "</div> <div class='registrateInfo' style='width:337px;font-family:Arial; font-size:12px; margin-top:10px; margin-left:35px;'>	<div id='nombre'><p>Correo Electrónico:</p>	<p style=' color:rgb(17,170,209); font-style:italic;' >"
				+ correo
				+ "</p></div>	<div id='mail'>	<p>Contraseña: </p><p id='pass' style=' color:rgb(17,170,209); font-style:italic;'>"
				+ contrasena
				+ mostrarUsos
				+ "</p>	</div></div><a style='color:rgb(17,170,209);' href='"
				+ urlMind
				+ "'><div id='mailLink'><p style='font-family:Arial; font-size:14px; clear:both; text-align:center; color:rgb(17,170,209); cursor:pointer;'>Click Aqui para Ingresar</p></div></a><div id='mailFooter'><p style='height:37px; width:100%; margin-top:25px;  padding-top:15px; color:grey; text-align:center;  font-family:Arial; font-size:12px;'>www.mindmanagement.co</p></div></div></div><!--<div id='botonEntrarServicios'><p>Registrate</p></div>--></div><!-- Cierro Cont Total---></center></body></html> ";

		return enviarCorreo(correo, "Bienvenido a Mind Management", i);

	}

	public static int enviarContrasenaAlCorreo(String correo, String pass) {

		String i = "<!DOCTYPE HTML><html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'><style>#nombre p{	float:left;	margin-left: 10px;} #mail{ clear:both;	}#mail p{ float:left;	margin-left: 10px;	}#mailLink p{	padding-top: 30px;}	</style></head> <center> <body style='margin:0px; padding:0px;'>	<div id='contenedorTotalMail' style='position: relative; width: 960px margin: 0px auto;'>		<div id='cuerpoMail' style='width: 408px; height: 643px; margin:auto;'>		<div id='mailContenedorDatos' style='	width:408px; height:380px; background-color: white; box-shadow: 0px 0px 10px rgba(0,0,0,0.3);'>		<div id='MailTitle' style='width:137px; height:62px; margin:25px auto; padding-top:20px;'><img src='https://dl.dropbox.com/u/32952272/logo2M.png'></div>	<div id='mailTitleGracias' style='color:rgb(91,91,91); font-family:Arial; font-size:14px; text-align:center; margin-bottom:50px;'><h1>Mind Management</h1><h3>Se ha creado una nueva contraseña.</h3></div> <div class='registrateInfo' style='width:337px;font-family:Arial; font-size:12px; margin-top:10px; margin-left:35px;'>"
				+ "<div id='mail'>	<p>Contraseña: </p><p id='pass' style=' color:rgb(17,170,209); font-style:italic;'>"
				+ pass
				+ "</p></div>"
				+ "</div><a style='color:rgb(17,170,209);' href='"
				+ urlMind
				+ "'><div id='mailLink'><p style='font-family:Arial; font-size:14px; clear:both; text-align:center; color:rgb(17,170,209); cursor:pointer;'>Click Aqui para Ingresar</p></div></a><div id='mailFooter'><p style='height:37px; width:100%; margin-top:25px;  padding-top:15px; color:grey; text-align:center;  font-family:Arial; font-size:12px;'>www.mindmanagement.co</p></div></div></div><!--<div id='botonEntrarServicios'><p>Registrate</p></div>--></div><!-- Cierro Cont Total---> </center> </body></html> ";

		return enviarCorreo(correo, "Recuperacion de Contraseña", i);

	}

	public static int enviarCorreoProcesoRevision(UsuarioBO usuarioMaestro,
			ProcesoUsuarioBO proceso) {

		String empresa = usuarioMaestro.getEmpresa();
		if (empresa == null) {
			empresa = ".";
		} else {
			empresa = " de " + empresa;
		}
		String i = "<!DOCTYPE HTML><html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'><style>#nombre p{	float:left;	margin-left: 10px;} #mail{ clear:both;	}#mail p{ float:left;	margin-left: 10px;	}#mailLink p{	padding-top: 30px;}	</style></head>	<center> <body style='margin:0px; padding:0px;'>	<div id='contenedorTotalMail' style='position: relative; width: 960px margin: 0px auto;'>		<div id='cuerpoMail' style='width: 408px; height: 643px; margin:auto;'>		<div id='mailContenedorDatos' style='	width:408px; height:380px; background-color: white; box-shadow: 0px 0px 10px rgba(0,0,0,0.3);'>		<div id='MailTitle' style='width:137px; height:62px; margin:25px auto; padding-top:20px;'><img src='https://dl.dropbox.com/u/32952272/logo2M.png'></div>	<div id='mailTitleGracias' style='color:rgb(91,91,91); font-family:Arial; font-size:14px; text-align:center; margin-bottom:50px;'><h1>Bienvenido a Mind Management</h1>"
				+ "<h3>Se ha solicitado la revisión de un proceso.</h3>"
				+ "</div> <div class='registrateInfo' style='width:337px;font-family:Arial; font-size:12px; margin-top:10px; margin-left:35px;'>	<div id='nombre'><p>Cliente Solicitante:</p>	<p style=' color:rgb(17,170,209); font-style:italic;' >"
				+ usuarioMaestro.getNombres()
				+ " "
				+ usuarioMaestro.getApellidos()
				+ empresa
				+ "</p></div>	<div id='mail'>	<p>Proceso: </p><p id='pass' style=' color:rgb(17,170,209); font-style:italic;'>"
				+ proceso.getNombre()
				+ "</p>	</div></div><a style='color:rgb(17,170,209);' href='"
				+ urlMind
				+ "'><div id='mailLink'><p style='font-family:Arial; font-size:14px; clear:both; text-align:center; color:rgb(17,170,209); cursor:pointer;'>Click Aqui para Ingresar</p></div></a><div id='mailFooter'><p style='height:37px; width:100%; margin-top:25px;  padding-top:15px; color:grey; text-align:center;  font-family:Arial; font-size:12px;'>www.mindmanagement.co</p></div></div></div><!--<div id='botonEntrarServicios'><p>Registrate</p></div>--></div><!-- Cierro Cont Total---></center></body></html> ";

		return enviarCorreo("raymond.chaux@gmail.com", "Proceso de Revisión", i);

	}

	public static int enviarMensajeAMaestro(UsuarioBO usuario,
			String mensajeCorreo, String correoMaestro) {

		String empresa = usuario.getEmpresa();
		if (empresa == null) {
			empresa = "";
		} else {
			empresa = " de " + empresa;
		}

		String i = usuario.getNombres() + " " + usuario.getApellidos() + " "
				+ empresa + " le envía el siguiente comentario. <p>"
				+ mensajeCorreo + "</p><p>Su correo es: "
				+ usuario.getCorreo_Electronico() + ".</p>";

		return enviarCorreo(correoMaestro,
				"Tiene un comentario de " + usuario.getNombres() + " "
						+ usuario.getApellidos() + empresa, i);

	}

	public static int enviarResultados(List<ResultadoBO> resultados) {
		String respuesta = "<html><head></head><body><center><table>";
		for (ResultadoBO resultadoBO : resultados) {
			respuesta += "<tr><td>"
					+ resultadoBO.getPreguntasUsuario().getPregunta()
					+ "</td><td>" + resultadoBO.getRespuesta() + "</td></tr>";
		}
		respuesta += "</table></center></body></html>";

		return enviarCorreo("raymond.chaux@gmail.com", "Resultados del Demo.",
				respuesta);
	}

	private static int enviarCorreo(String to, String title, String textToSend) {

		String host = "localhost";
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		Session session = Session.getDefaultInstance(properties);

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("info@mindmanagement.co",
					"Mind Management"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			message.setSubject(title);
			message.setContent(textToSend, "text/html");
			Transport.send(message);
			System.out.println("Sent message successfully....");
			return Convencion.CORRECTO;
		} catch (MessagingException mex) {
			mex.printStackTrace();
			return Convencion.INCORRECTO;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Convencion.INCORRECTO;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Convencion.INCORRECTO;
		}

	}

}
