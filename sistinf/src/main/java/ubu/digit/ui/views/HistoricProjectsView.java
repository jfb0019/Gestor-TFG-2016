package ubu.digit.ui.views;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ubu.digit.pesistence.SistInfData;
import ubu.digit.ui.beans.HistoricProjectBean;
import ubu.digit.ui.components.Footer;
import ubu.digit.ui.components.NavigationBar;

public class HistoricProjectsView extends VerticalLayout implements View {

	private static final long serialVersionUID = 8431807779365780674L;

	/**
	 * Logger de la clase.
	 */
	private static final Logger LOGGER = Logger.getLogger(HistoricProjectsView.class);

	public static final String VIEW_NAME = "historic-projects";

	private BeanItemContainer<HistoricProjectBean> beans;

	private SistInfData fachadaDatos;

	public HistoricProjectsView() {
		fachadaDatos = SistInfData.getInstance();
		setMargin(true);
		setSpacing(true);
		NavigationBar navBar = new NavigationBar();
		addComponent(navBar);

		createDataModel();
		createMetrics();
		createHistoricProjectsTable();

		Footer footer = new Footer();
		addComponent(footer);
	}

	private void createDataModel() {
		beans = new BeanItemContainer<HistoricProjectBean>(HistoricProjectBean.class);
		try (ResultSet result = fachadaDatos.getResultSet("Historico", "Titulo")) {
			while (result.next()) {
				int numStudents = 1;
				String title = result.getString("Titulo");
				String description = result.getString("Descripcion");
				String tutor1 = result.getString("Tutor1");
				String tutor2 = result.getString("Tutor2");
				if (tutor2 == null)
					tutor2 = "";
				String tutor3 = result.getString("Tutor3");
				if (tutor3 == null)
					tutor3 = "";
				String student1 = result.getString("Alumno1");
				String student2 = result.getString("Alumno2");
				if (student2 == null)
					student2 = "";
				else
					numStudents++;
				String student3 = result.getString("Alumno3");
				if (student3 == null)
					student3 = "";
				else
					numStudents++;
				String assignmentDate = result.getString("FechaAsignacion");
				String presentationDate = result.getString("FechaPresentacion");
				String score = result.getString("Nota");
				int totalDays = result.getShort("TotalDias");

				HistoricProjectBean bean = new HistoricProjectBean(title, description, tutor1, tutor2, tutor3, student1,
						student2, student3, numStudents, assignmentDate, presentationDate, score, totalDays);
				beans.addBean(bean);
			}
		} catch (SQLException e) {
			LOGGER.error("Error en históricos", e);
		}

	}

	private void createMetrics() {

	}

	private void createHistoricProjectsTable() {

	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

}
