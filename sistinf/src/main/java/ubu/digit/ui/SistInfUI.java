package ubu.digit.ui;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;

import org.apache.log4j.Logger;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Label;
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
			LOGGER.error(e);
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
