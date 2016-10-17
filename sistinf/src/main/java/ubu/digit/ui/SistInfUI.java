package ubu.digit.ui;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;

import org.apache.log4j.Logger;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import ubu.digit.pesistence.SistInfData;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("sistinftheme")
public class SistInfUI extends UI {

	/**
	 * Logger de la clase.
	 */
	private static final Logger LOGGER = Logger.getLogger(SistInfData.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -4568743602891945769L;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		final VerticalLayout layout = new VerticalLayout();
		SistInfData fachadaDatos = SistInfData.getInstance();

		layout.addComponent(new Label("Tribunal:"));
		try (ResultSet result = fachadaDatos.getResultSet("Tribunal", "NombreApellidos")){
			while (result.next()) {
				String cargo = result.getString("Cargo");
				String nombre = result.getString("NombreApellidos");
				String tribunal = cargo + ": " + nombre;
				layout.addComponent(new Label(tribunal));
			}		
		} catch (SQLException e) {
			LOGGER.error("Error en tribunal", e);
		}
		layout.addComponent(new Label("Programa en vigor a partir del Curso 2002-2003."));

		layout.addComponent(new Label("&nbsp;", ContentMode.HTML));
		layout.addComponent(new Label("Especificaciones de Entrega:"));
		try (ResultSet result = fachadaDatos.getResultSet("Norma", "Descripcion")){
			while (result.next()) {
				String descripcion = result.getString("Descripcion");
				layout.addComponent(new Label(descripcion));
			}		
		} catch (SQLException e) {
			LOGGER.error("Error en normas", e);
		}
		
		layout.addComponent(new Label("&nbsp;", ContentMode.HTML));
		layout.addComponent(new Label("Fechas de entrega:"));
		BrowserFrame calendar = new BrowserFrame("", new ExternalResource("https://goo.gl/PgEkF1"));
		calendar.setWidth(100, Unit.PERCENTAGE);
		calendar.setHeight(500, Unit.PIXELS);
		layout.addComponent(calendar);

		layout.addComponent(new Label("&nbsp;", ContentMode.HTML));
		layout.addComponent(new Label("Documentos:"));
		try (ResultSet result = fachadaDatos.getResultSet("Documento", "Descripcion")){
			while (result.next()) {
				String descripcion = result.getString("Descripcion");
				String url = result.getString("Url");
				layout.addComponent(new Link(descripcion, new ExternalResource(url)));
			}		
		} catch (SQLException e) {
			LOGGER.error("Error en documentos", e);
		}
		
		layout.setMargin(true);
		layout.setSpacing(true);
		setContent(layout);
	}

	@WebServlet(urlPatterns = "/*", name = "SistInfUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = SistInfUI.class, productionMode = false)
	public static class SistInfUIServlet extends VaadinServlet {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8278292941976902830L;
	}
}
