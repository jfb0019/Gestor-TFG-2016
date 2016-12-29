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
import static ubu.digit.util.Constants.*;

import com.vaadin.ui.Button.ClickEvent;

public class NavigationBar extends CustomComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3164191122994765469L;

	private Button buttonInfo;
	private Button buttonActive;
	private Button buttonHistory;
	private Button buttonMetrics;

	private Navigator navigator;

	public NavigationBar() {
		HorizontalLayout content = new HorizontalLayout();
		content.setMargin(false);
		content.setSpacing(false);
		content.setWidth("100%");

		inicializarComponentes();
		content.addComponents(buttonInfo, buttonActive, buttonHistory, buttonMetrics);
		setCompositionRoot(content);
	}

	private void inicializarComponentes() {
		navigator = UI.getCurrent().getNavigator();

		buttonInfo = new Button(INFORMACION);
		buttonInfo.addClickListener(new ButtonClick());
		buttonActive = new Button(PROYECTOS_ACTIVOS);
		buttonActive.addClickListener(new ButtonClick());
		buttonHistory = new Button(PROYECTOS_HISTORICOS);
		buttonHistory.addClickListener(new ButtonClick());
		buttonMetrics = new Button(METRICAS);
		buttonMetrics.addClickListener(new ButtonClick());

		buttonInfo.setHeight(BUTTON_HEIGHT);
		buttonActive.setHeight(BUTTON_HEIGHT);
		buttonHistory.setHeight(BUTTON_HEIGHT);
		buttonMetrics.setHeight(BUTTON_HEIGHT);

		buttonInfo.setWidth("100%");
		buttonActive.setWidth("100%");
		buttonHistory.setWidth("100%");
		buttonMetrics.setWidth("100%");
	}

	private class ButtonClick implements Button.ClickListener {
		private static final long serialVersionUID = -2703551968601700023L;

		@Override
		public void buttonClick(ClickEvent event) {
			if (event.getButton() == buttonInfo) {
				navigator.navigateTo(InformationView.VIEW_NAME);
			} else if (event.getButton() == buttonActive) {
				navigator.navigateTo(ActiveProjectsView.VIEW_NAME);
			} else if (event.getButton() == buttonHistory) {
				navigator.navigateTo(HistoricProjectsView.VIEW_NAME);
			} else if (event.getButton() == buttonMetrics) {
				navigator.navigateTo(MetricsView.VIEW_NAME);
			}
		}
	}
}
