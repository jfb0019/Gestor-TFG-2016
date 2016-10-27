package ubu.digit.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ubu.digit.ui.components.Footer;
import ubu.digit.ui.components.NavigationBar;

public class ActiveProjectsView extends VerticalLayout implements View {

	private static final long serialVersionUID = 8857805864102975132L;

	public static String VIEW_NAME = "active-projects";

	public ActiveProjectsView() {
		setMargin(true);
		setSpacing(true);
		NavigationBar navBar = new NavigationBar();
		addComponent(navBar);

		Footer footer = new Footer();
		addComponent(footer);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

}
