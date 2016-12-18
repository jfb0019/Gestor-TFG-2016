package ubu.digit.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import ubu.digit.ui.components.NavigationBar;

import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

public class UploadCsvView extends VerticalLayout implements View {

	private static final long serialVersionUID = 8460171059055033456L;

	public static final String VIEW_NAME = "upload";

	private Label text;

	private Button logout;

	public UploadCsvView() {
		setMargin(true);
		setSpacing(true);

		NavigationBar navBar = new NavigationBar();
		addComponent(navBar);
		
		text = new Label();
		logout = new Button("Logout", new Button.ClickListener() {

			private static final long serialVersionUID = -6910251607481142610L;

			@Override
			public void buttonClick(ClickEvent event) {
				getSession().setAttribute("user", null);
				getUI().getNavigator().navigateTo(InformationView.VIEW_NAME);
				Notification.show("Has cerrado sesión satisfactoriamente.");
			}
		});
		addComponents(text, logout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		String username = String.valueOf(getSession().getAttribute("user"));
		text.setValue("Hola " + username);
	}
}
