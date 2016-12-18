package ubu.digit.ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

import ubu.digit.ui.views.ActiveProjectsView;
import ubu.digit.ui.views.HistoricProjectsView;
import ubu.digit.ui.views.InformationView;
import ubu.digit.ui.views.LoginView;
import ubu.digit.ui.views.MetricsView;
import ubu.digit.ui.views.UploadCsvView;

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
		View activeProjectsView = new ActiveProjectsView();
		View historicProjectsView = new HistoricProjectsView();
		View metricsView = new MetricsView();
		View loginView = new LoginView();
		View uploadCsvView = new UploadCsvView();

		navigator.addView("", informationView);
		navigator.addView(InformationView.VIEW_NAME, informationView);
		navigator.addView(ActiveProjectsView.VIEW_NAME, activeProjectsView);
		navigator.addView(HistoricProjectsView.VIEW_NAME, historicProjectsView);
		navigator.addView(MetricsView.VIEW_NAME, metricsView);
		navigator.addView(LoginView.VIEW_NAME, loginView);
		navigator.addView(UploadCsvView.VIEW_NAME, uploadCsvView);
		navigator.addViewChangeListener(new ViewChangeListener() {

			private static final long serialVersionUID = -650123765531182231L;

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				boolean isUserLoggedIn = getSession().getAttribute("user") != null;
				boolean isNextViewLoginView = event.getNewView() instanceof LoginView;
				boolean isNextViewUploadCsvView = event.getNewView() instanceof UploadCsvView;

				if (!isUserLoggedIn && isNextViewUploadCsvView) {
					Notification.show("Error", "Por favor inicie sesión para acceder a esta página.", Notification.Type.WARNING_MESSAGE);
					getNavigator().navigateTo(LoginView.VIEW_NAME);
					return false;
				} else if (isUserLoggedIn && isNextViewLoginView) {
					getNavigator().navigateTo(UploadCsvView.VIEW_NAME);
					return false;
				}
				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {

			}
		});
	}

	@WebServlet(urlPatterns = "/*", name = "SistInfUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = SistInfUI.class, productionMode = false)
	public static class SistInfUIServlet extends VaadinServlet {

		private static final long serialVersionUID = -8278292941976902830L;
	}
}
