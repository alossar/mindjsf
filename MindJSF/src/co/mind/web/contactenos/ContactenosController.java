package co.mind.web.contactenos;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import co.mind.management.shared.dto.UsuarioBO;
import co.mind.management.shared.recursos.MindHelper;
import co.mind.management.shared.recursos.SMTPSender;

public class ContactenosController {

	public void contactenos() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String nombre = (String) facesContext.getExternalContext()
				.getRequestParameterMap().get("name");
		String correo = (String) facesContext.getExternalContext()
				.getRequestParameterMap().get("email");
		String mensaje = (String) facesContext.getExternalContext()
				.getRequestParameterMap().get("message");
		HttpServletResponse response = MindHelper.obtenerResponse();

		if (nombre != null && correo != null) {
			UsuarioBO usuario = new UsuarioBO();
			usuario.setCorreo_Electronico(correo);
			usuario.setNombres(nombre);

			SMTPSender.enviarMensajeAMaestro(usuario, mensaje, "alejo.ossa91@gmail.com");
		}
		try {
			facesContext.getExternalContext().redirect(
					"http://www.mindmanagement.co");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
