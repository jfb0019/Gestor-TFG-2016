package ubu.digit.ui.components;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

public class Footer extends CustomComponent {

	private static final long serialVersionUID = 1285443082746553886L;

	private GridLayout content;

	public Footer() {
		content = new GridLayout(3, 1);
		content.setMargin(false);
		content.setSpacing(true);
		content.setWidth("100%");

		addInformation();
		addVaadinLogo();
		addLicense();

		content.setColumnExpandRatio(0, 4);
		content.setColumnExpandRatio(1, 1);
		content.setColumnExpandRatio(2, 4);

		setCompositionRoot(content);
	}

	private void addInformation() {
		VerticalLayout information = new VerticalLayout();
		information.setMargin(false);
		information.setSpacing(true);

		Label subtitle = new Label("Información");
		subtitle.setStyleName("lbl-info");

		Label version2 = new Label("Versión 2.0 creada por Javier de la Fuente Barrios");
		Link link2 = new Link("jfb0019@alu.ubu.es", new ExternalResource("mailto:jfb0019@alu.ubu.es"));
		link2.setIcon(FontAwesome.ENVELOPE);

		Label version1 = new Label("Versión 1.0 creada por Beatriz Zurera Martínez-Acitores");
		Link link1 = new Link("bzm0001@alu.ubu.es", new ExternalResource("mailto:bzm0001@alu.ubu.es"));
		link1.setIcon(FontAwesome.ENVELOPE);

		Label tutor = new Label("Tutorizado por Carlos López Nozal");
		Link linkT = new Link("clopezno@ubu.es", new ExternalResource("mailto:clopezno@alu.ubu.es"));
		linkT.setIcon(FontAwesome.ENVELOPE);

		Label copyright = new Label("Copyright @ LSI");

		information.addComponents(subtitle, version2, link2, version1, link1, tutor, linkT, copyright);
		content.addComponent(information);
	}

	private void addVaadinLogo() {
		Link vaadinImage = new Link(null, new ExternalResource("https://vaadin.com/home"));
		vaadinImage.setIcon(new ThemeResource("img/logo-vaadin.png"));
		content.addComponents(vaadinImage);
	}

	private void addLicense() {
		VerticalLayout license = new VerticalLayout();
		license.setMargin(false);
		license.setSpacing(true);

		Link ccImage = new Link(null, new ExternalResource("https://creativecommons.org/licenses/by/4.0/"));
		ccImage.setIcon(new ThemeResource("img/cc.png"));
		
		Label licenseText = new Label("This work is licensed under a: ");
		Link ccLink = new Link("Creative Commons Attribution 4.0 International License.",
				new ExternalResource("https://creativecommons.org/licenses/by/4.0/"));
		
		license.addComponents(ccImage, licenseText, ccLink);
		content.addComponent(license);
	}

}
