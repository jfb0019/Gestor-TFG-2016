package ubu.digit.ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import ubu.digit.ui.views.InformationView;

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

	private static final long serialVersionUID = -4568743602891945769L;
	private Navigator navigator;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		Page.getCurrent().setTitle("Sistemas Informáticos");

		navigator = new Navigator(this, this);
		View informationView = new InformationView();

		navigator.addView("", informationView);
		navigator.addView(InformationView.VIEW_NAME, informationView);
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
