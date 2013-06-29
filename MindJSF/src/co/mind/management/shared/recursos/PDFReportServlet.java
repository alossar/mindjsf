package co.mind.management.shared.recursos;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import co.mind.management.shared.dto.ParticipacionEnProcesoBO;

public class PDFReportServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ServletOutputStream servletOutputStream = response.getOutputStream();
		byte[] bytes = null;
		try {
			HttpSession session = request.getSession();
			List<ParticipacionEnProcesoBO> participaciones = (List<ParticipacionEnProcesoBO>) session
					.getAttribute("participaciones");
			if (participaciones != null) {
				Map<String, Object> parametros = new HashMap<>();
				Integer participacion = participaciones.get(0)
						.getIdentificador();
				String direccionSubreporteCategorias = getServletConfig()
						.getServletContext().getRealPath(
								"/reports/subreporte_categorias.jasper");
				String direccionSubreportePreguntas = getServletConfig()
						.getServletContext().getRealPath(
								"/reports/subreporte_preguntas.jasper");
				parametros.put("participacionID", participacion);
				parametros.put("SUBREPORT_DIR", direccionSubreporteCategorias);
				parametros.put("SUBREPORT_DIR_PRE",
						direccionSubreportePreguntas);
				Connection jdbcConnection = getConnection();
				JasperReport jasperReport = JasperCompileManager
						.compileReport(getServletConfig().getServletContext()
								.getRealPath("/reports/reporte.jrxml"));
				bytes = JasperRunManager.runReportToPdf(jasperReport,
						parametros, jdbcConnection);
				session.removeAttribute("participaciones");

				response.setContentType("application/pdf");
				response.setContentLength(bytes.length);
				servletOutputStream.write(bytes, 0, bytes.length);
				servletOutputStream.flush();
				servletOutputStream.close();
			} else {
				response.sendRedirect(Convencion.DIRECCION_WEB);
			}
		} catch (JRException e) {
			// display stack trace in the browser
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			response.setContentType("text/plain");
			response.getOutputStream().print(stringWriter.toString());
		} catch (NullPointerException nul) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			nul.printStackTrace(pw);
			response.setContentType("text/plain");
			response.getOutputStream().print(sw.toString());

		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	public static Connection getConnection() {
		Connection jdbcConnection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			jdbcConnection = DriverManager.getConnection(
					Convencion.CONEXION_URL, Convencion.CONEXION_USER,
					Convencion.CONEXION_PASS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jdbcConnection;
	}
}