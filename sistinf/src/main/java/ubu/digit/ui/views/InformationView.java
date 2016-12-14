package ubu.digit.ui.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

import ubu.digit.pesistence.SistInfData;
import ubu.digit.ui.components.Footer;
import ubu.digit.ui.components.NavigationBar;
import ubu.digit.util.ExternalProperties;

public class InformationView extends VerticalLayout implements View {

	private static final long serialVersionUID = 7820866989198327219L;

	/**
	 * Logger de la clase.
	 */
	private static final Logger LOGGER = Logger.getLogger(SistInfData.class);

	private ExternalProperties config;

	private SistInfData fachadaDatos;

	private String estiloTitulo;

	private String lineaBlanco;
	
	public static final String VIEW_NAME = "information";

	public InformationView() {
		fachadaDatos = SistInfData.getInstance();
		config = ExternalProperties.getInstance("/WEB-INF/classes/config.properties", false);
		estiloTitulo = "lbl-title";
		lineaBlanco = "&nbsp;";

		setMargin(true);
		setSpacing(true);

		NavigationBar navBar = new NavigationBar();
		addComponent(navBar);

		createTribunal();
		createNormas();
		createCalendar();
		createDocumentos();
		
		Footer footer = new Footer("Tribunal.csv");
		addComponent(footer);
	}
	
	private void createTribunal() {
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
		addComponents(tribunalTitle, horizontalTribunal, curso);
	}

	private void createNormas() {
		addComponent(new Label(lineaBlanco, ContentMode.HTML));
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
		addComponents(normasTitle, normas);
	}

	private void createCalendar() {
		addComponent(new Label(lineaBlanco, ContentMode.HTML));
		Label fechasTitle = new Label("Fechas de entrega");
		fechasTitle.setStyleName(estiloTitulo);

		String urlCalendario = config.getSetting("urlCalendario");
		BrowserFrame calendar = new BrowserFrame("", new ExternalResource("https://" + urlCalendario));
		calendar.setWidth(85, Unit.PERCENTAGE);
		calendar.setHeight(500, Unit.PIXELS);
		addComponents(fechasTitle, calendar);
	}

	private void createDocumentos() {
		addComponent(new Label(lineaBlanco, ContentMode.HTML));
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
		addComponents(documentosTitle, documentos);
		addComponent(new Label(lineaBlanco, ContentMode.HTML));
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// Se inicializa el contenido de la vista en el constructor
	}
}
