package ubu.digit.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

import ubu.digit.ui.components.Footer;
import ubu.digit.ui.components.NavigationBar;
import ubu.digit.util.ExternalProperties;

public class MetricsView extends VerticalLayout implements View {

	private static final long serialVersionUID = 1110300353177565418L;

	public static final String VIEW_NAME = "metrics";

	private ExternalProperties config;

	public MetricsView() {
		config = ExternalProperties.getInstance("/WEB-INF/classes/config.properties", false);
		setMargin(true);
		setSpacing(true);
		NavigationBar navBar = new NavigationBar();
		addComponent(navBar);

		addSonarImageLink();
		
		Footer footer = new Footer(null);
		addComponent(footer);
	}

	private void addSonarImageLink(){
		String urlSonar = config.getSetting("urlSonar");
		Link sonarLink = new Link(null, new ExternalResource("https://" + urlSonar));
		sonarLink.setIcon(new ThemeResource("img/tfgsonar.png"));
		sonarLink.setTargetName("_blank");
		addComponent(sonarLink);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
	}

}
