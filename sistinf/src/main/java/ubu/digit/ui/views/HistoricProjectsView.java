package ubu.digit.ui.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
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

	private String estiloTitulo;

	private Table table;

	public HistoricProjectsView() {
		fachadaDatos = SistInfData.getInstance();
		estiloTitulo = "lbl-title";
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
		Label projectsTitle = new Label("Descripción de proyectos");
		projectsTitle.setStyleName(estiloTitulo);
		addComponent(projectsTitle);

		table = new Table();
		addComponent(table);
		table.setWidth("100%");
		table.setPageLength(10);
		table.setColumnCollapsingAllowed(true);
		table.setContainerDataSource(beans);
		table.setVisibleColumns("title", "tutor1", "tutor2", "tutor3", "numStudents", "assignmentDate", "presentationDate", "score");
		setTableColumnHeaders();
		setColumnExpandRatios();
		collapseOptionalFields();

	}
	
	private void setTableColumnHeaders() {
		table.setColumnHeader("title", "Título");
		table.setColumnHeader("tutor1", "Tutor 1");
		table.setColumnHeader("tutor2", "Tutor 2");
		table.setColumnHeader("tutor3", "Tutor 3");
		table.setColumnHeader("numStudents", "Nº Alumnos");
		table.setColumnHeader("assignmentDate", "Fecha Asignación");
		table.setColumnHeader("presentationDate", "Fecha Presentación");
		table.setColumnHeader("score", "Nota");
	}
	
	private void setColumnExpandRatios() {
		table.setColumnExpandRatio("title", 35);
		table.setColumnExpandRatio("tutor1", 10);
		table.setColumnExpandRatio("tutor2", 10);
		table.setColumnExpandRatio("tutor3", 10);
		table.setColumnExpandRatio("numStudents", 5);
		table.setColumnExpandRatio("assignmentDate", 7);
		table.setColumnExpandRatio("presentationDate", 8);
		table.setColumnExpandRatio("score", 3);
	}
	
	private void collapseOptionalFields() {
		List<HistoricProjectBean> itemIds = beans.getItemIds();
		Iterator<HistoricProjectBean> iterator = itemIds.iterator();
		boolean tutor2Flag = true;
		boolean tutor3Flag = true;
		
		while (iterator.hasNext()) {
			HistoricProjectBean next = iterator.next();
			HistoricProjectBean bean = beans.getItem(next).getBean();
			if (!"".equals(bean.getTutor2()))
				tutor2Flag = false;
			if (!"".equals(bean.getTutor3()))
				tutor3Flag = false;
		}

		if (tutor2Flag)
			table.setColumnCollapsed("tutor2", true);
		if (tutor3Flag)
			table.setColumnCollapsed("tutor3", true);		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
	}

}
