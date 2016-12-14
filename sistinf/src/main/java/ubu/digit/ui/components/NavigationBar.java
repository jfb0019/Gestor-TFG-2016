package ubu.digit.ui.components;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

import ubu.digit.ui.views.ActiveProjectsView;
import ubu.digit.ui.views.InformationView;
import ubu.digit.ui.views.MetricsView;
import ubu.digit.ui.views.HistoricProjectsView;

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

	private static final String BUTTON_HEIGHT = "64px";

	public NavigationBar() {
		HorizontalLayout content = new HorizontalLayout();
		content.setMargin(false);
		content.setSpacing(false);
		content.setWidth("100%");

		inicializarComponentes();
		content.addComponents(botonInfo, botonActive, botonHistory, botonMetrics);
		setCompositionRoot(content);
	}

	private void inicializarComponentes() {
		navigator = UI.getCurrent().getNavigator();

		botonInfo = new Button("Información");
		botonInfo.addClickListener(new ButtonClick());
		botonActive = new Button("Proyectos activos");
		botonActive.addClickListener(new ButtonClick());
		botonHistory = new Button("Histórico");
		botonHistory.addClickListener(new ButtonClick());
		botonMetrics = new Button("Métricas");
		botonMetrics.addClickListener(new ButtonClick());

		botonInfo.setHeight(BUTTON_HEIGHT);
		botonActive.setHeight(BUTTON_HEIGHT);
		botonHistory.setHeight(BUTTON_HEIGHT);
		botonMetrics.setHeight(BUTTON_HEIGHT);

		botonInfo.setWidth("100%");
		botonActive.setWidth("100%");
		botonHistory.setWidth("100%");
		botonMetrics.setWidth("100%");
	}

	private class ButtonClick implements Button.ClickListener {
		private static final long serialVersionUID = -2703551968601700023L;

		@Override
		public void buttonClick(ClickEvent event) {
			if (event.getButton() == botonInfo) {
				navigator.navigateTo(InformationView.VIEW_NAME);
			} else if (event.getButton() == botonActive) {
				navigator.navigateTo(ActiveProjectsView.VIEW_NAME);
			} else if (event.getButton() == botonHistory) {
				navigator.navigateTo(HistoricProjectsView.VIEW_NAME);
			} else if (event.getButton() == botonMetrics) {
				navigator.navigateTo(MetricsView.VIEW_NAME);
			}

		}
	}
}
