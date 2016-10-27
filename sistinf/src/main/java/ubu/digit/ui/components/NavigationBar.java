package ubu.digit.ui.components;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

import ubu.digit.ui.views.InformationView;

import com.vaadin.ui.Button.ClickEvent;

public class NavigationBar extends CustomComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3164191122994765469L;

	private Button botonInfo;
	private Button botonActive;
	private Button botonHistory;
	private Button botonMetrics;

	private Navigator navigator;

	public NavigationBar() {
		navigator = UI.getCurrent().getNavigator();
		botonInfo = new Button("Información");
		botonInfo.addClickListener(new GoToInformation());
		botonActive = new Button("Proyectos activos");
		botonHistory = new Button("Histórico");
		botonMetrics = new Button("Métricas");

		HorizontalLayout content = new HorizontalLayout();
		content.setMargin(false);
		content.setSpacing(false);
		content.setWidth("100%");

		botonInfo.setHeight("64px");
		botonActive.setHeight("64px");
		botonHistory.setHeight("64px");
		botonMetrics.setHeight("64px");

		botonInfo.setWidth("100%");
		botonActive.setWidth("100%");
		botonHistory.setWidth("100%");
		botonMetrics.setWidth("100%");

		content.addComponents(botonInfo, botonActive, botonHistory, botonMetrics);

		setCompositionRoot(content);

	}

	private class GoToInformation implements Button.ClickListener {
		private static final long serialVersionUID = -2703551968601700023L;

		@Override
		public void buttonClick(ClickEvent event) {
			navigator.navigateTo(InformationView.VIEW_NAME);

		}
	}
}
