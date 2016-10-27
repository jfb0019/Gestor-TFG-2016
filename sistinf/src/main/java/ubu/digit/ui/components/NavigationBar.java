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

	private final String buttonHeight = "64px";

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
		botonInfo.addClickListener(new GoToInformation());
		botonActive = new Button("Proyectos activos");
		botonHistory = new Button("Histórico");
		botonMetrics = new Button("Métricas");
		
		botonInfo.setHeight(buttonHeight);
		botonActive.setHeight(buttonHeight);
		botonHistory.setHeight(buttonHeight);
		botonMetrics.setHeight(buttonHeight);

		botonInfo.setWidth("100%");
		botonActive.setWidth("100%");
		botonHistory.setWidth("100%");
		botonMetrics.setWidth("100%");
	}

	private class GoToInformation implements Button.ClickListener {
		private static final long serialVersionUID = -2703551968601700023L;

		@Override
		public void buttonClick(ClickEvent event) {
			navigator.navigateTo(InformationView.VIEW_NAME);

		}
	}
}
