package ubu.digit.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ubu.digit.ui.components.Footer;
import ubu.digit.ui.components.NavigationBar;

public class MetricsView extends VerticalLayout implements View {

	private static final long serialVersionUID = 1110300353177565418L;

	public static String VIEW_NAME = "metrics";

	public MetricsView() {
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
