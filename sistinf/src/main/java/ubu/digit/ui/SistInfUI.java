package ubu.digit.ui;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;

import org.apache.log4j.Logger;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import ubu.digit.pesistence.SistInfData;
import ubu.digit.util.ExternalProperties;

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
		SistInfData fachadaDatos = SistInfData.getInstance();
		ExternalProperties config = ExternalProperties.getInstance("/WEB-INF/classes/config.properties", false);
		String estiloTitulo = "lbl-title";
		String lineaBlanco = "&nbsp;";
		Page.getCurrent().setTitle("Sistemas Informáticos - Información");

		final VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		content.setSpacing(true);

		// TRIBUNAL
		Label tribunalTitle = new Label("Tribunal");
		tribunalTitle.setStyleName(estiloTitulo);

		final HorizontalLayout horizontalTribunal = new HorizontalLayout();
		horizontalTribunal.setSpacing(true);
		horizontalTribunal.setMargin(new MarginInfo(false, true, false, true));

		Label iconoTribunal = new Label(FontAwesome.USERS.getHtml(), ContentMode.HTML);
		iconoTribunal.setStyleName("icon-big");
		iconoTribunal.setWidth(130, Unit.PIXELS);

		final VerticalLayout tribunal = new VerticalLayout();
		tribunal.setSpacing(true);
		tribunal.setWidth(350, Unit.PIXELS);

		try (ResultSet result = fachadaDatos.getResultSet("Tribunal", "NombreApellidos")) {
			while (result.next()) {
				String cargo = result.getString("Cargo");
				String nombre = result.getString("NombreApellidos");
				String filaTribunal = cargo + ": " + nombre;
				tribunal.addComponent(new Label(filaTribunal));
			}
		} catch (SQLException e) {
			LOGGER.error("Error en tribunal", e);
		}
		horizontalTribunal.addComponents(iconoTribunal, tribunal);

		String indexAño = config.getSetting("indexAño");
		int indexAñoSiguiente = Integer.parseInt(indexAño) + 1;
		Label curso = new Label("Programa en vigor a partir del Curso " + indexAño + "-" + indexAñoSiguiente + ".");
		content.addComponents(tribunalTitle, horizontalTribunal, curso);

		// NORMAS
		content.addComponent(new Label(lineaBlanco, ContentMode.HTML));
		Label normasTitle = new Label("Especificaciones de Entrega");
		normasTitle.setStyleName(estiloTitulo);

		final VerticalLayout normas = new VerticalLayout();
		normas.setSpacing(true);

		try (ResultSet result = fachadaDatos.getResultSet("Norma", "Descripcion")) {
			while (result.next()) {
				String descripcion = result.getString("Descripcion");
				normas.addComponent(new Label(" - " + descripcion));
			}
		} catch (SQLException e) {
			LOGGER.error("Error en normas", e);
		}
		content.addComponents(normasTitle, normas);

		// FECHAS
		content.addComponent(new Label(lineaBlanco, ContentMode.HTML));
		Label fechasTitle = new Label("Fechas de entrega");
		fechasTitle.setStyleName(estiloTitulo);

		String urlCalendario = config.getSetting("urlCalendario");
		BrowserFrame calendar = new BrowserFrame("", new ExternalResource("https://" + urlCalendario));
		calendar.setWidth(85, Unit.PERCENTAGE);
		calendar.setHeight(500, Unit.PIXELS);
		content.addComponents(fechasTitle, calendar);

		// DOCUMENTOS
		content.addComponent(new Label(lineaBlanco, ContentMode.HTML));
		Label documentosTitle = new Label("Documentos");
		documentosTitle.setStyleName(estiloTitulo);

		final VerticalLayout documentos = new VerticalLayout();
		documentos.setSpacing(true);

		try (ResultSet result = fachadaDatos.getResultSet("Documento", "Descripcion")) {
			while (result.next()) {
				String descripcion = result.getString("Descripcion");
				String url = result.getString("Url");
				Link link = new Link(descripcion, new ExternalResource(url));
				link.setIcon(FontAwesome.LINK);
				documentos.addComponent(link);
			}
		} catch (SQLException e) {
			LOGGER.error("Error en documentos", e);
		}
		content.addComponents(documentosTitle, documentos);

		setContent(content);
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
