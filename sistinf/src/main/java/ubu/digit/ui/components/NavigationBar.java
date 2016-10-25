package ubu.digit.ui.components;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

public class NavigationBar extends CustomComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3164191122994765469L;

	private Button botonInfo;
	private Button botonActive;
	private Button botonHistory;
	private Button botonMetrics;

	public NavigationBar() {
		botonInfo = new Button("Información");
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

}
