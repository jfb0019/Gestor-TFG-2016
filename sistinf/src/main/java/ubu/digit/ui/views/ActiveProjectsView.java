package ubu.digit.ui.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ubu.digit.pesistence.SistInfData;
import ubu.digit.ui.beans.ActiveProjectBean;
import ubu.digit.ui.components.Footer;
import ubu.digit.ui.components.NavigationBar;
import ubu.digit.ui.listeners.OrSimpleStringFilterListener;
import ubu.digit.ui.listeners.SimpleStringFilterListener;

public class ActiveProjectsView extends VerticalLayout implements View {

	private static final long serialVersionUID = 8857805864102975132L;

	/**
	 * Logger de la clase.
	 */
	private static final Logger LOGGER = Logger.getLogger(ActiveProjectsView.class);

	public static final String VIEW_NAME = "active-projects";

	private SistInfData fachadaDatos;

	private BeanItemContainer<ActiveProjectBean> beans;

	private Table table;

	private String estiloTitulo;

	private TextField projectFilter;

	private TextField descriptionFilter;

	private TextField tutorsFilter;

	private TextField studentsFilter;

	private TextField courseFilter;

	private static final String TITULO = "Titulo";

	private static final String PROYECTO = "Proyecto";

	private static final String TITLE = "title";

	private static final String DESCRIPTION = "description";

	private static final String TUTOR1 = "tutor1";

	private static final String TUTOR2 = "tutor2";

	private static final String TUTOR3 = "tutor3";

	private static final String STUDENT1 = "student1";

	private static final String STUDENT2 = "student2";

	private static final String STUDENT3 = "student3";

	private static final String COURSE_ASSIGNMENT = "courseAssignment";

	public ActiveProjectsView() {
		fachadaDatos = SistInfData.getInstance();
		estiloTitulo = "lbl-title";
		setMargin(true);
		setSpacing(true);

		NavigationBar navBar = new NavigationBar();
		addComponent(navBar);

		createDataModel();
		createMetrics();
		createFilters();
		createCurrentProjectsTable();
		addFiltersListeners();

		Footer footer = new Footer("Proyecto.csv");
		addComponent(footer);
	}

	private void createDataModel() {
		beans = new BeanItemContainer<>(ActiveProjectBean.class);
		try (ResultSet result = fachadaDatos.getResultSet(PROYECTO, TITULO)) {
			while (result.next()) {
				String title = result.getString(TITULO);
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
				String student3 = result.getString("Alumno3");
				if (student3 == null)
					student3 = "";
				String courseAssignment = result.getString("CursoAsignacion");

				ActiveProjectBean bean = new ActiveProjectBean(title, description, tutor1, tutor2, tutor3, student1,
						student2, student3, courseAssignment);
				beans.addBean(bean);
			}
		} catch (SQLException e) {
			LOGGER.error("Error en actuales", e);
		}
	}

	private void createMetrics() {
		Label metricsTitle = new Label("Información estadística");
		metricsTitle.setStyleName("lbl-title");
		addComponent(metricsTitle);

		try {
			Number totalProjectsNumber = fachadaDatos.getTotalNumber(TITULO, PROYECTO);
			Label totalProjects = new Label("- Número total de proyectos: " + totalProjectsNumber.intValue());

			Number totalFreeProjectNumber = fachadaDatos.getTotalFreeProject();
			Label totalFreeProject = new Label(
					"- Número total de proyectos sin asignar: " + totalFreeProjectNumber.intValue());

			Number totalStudentNumber = fachadaDatos.getTotalNumber("ApellidosNombre", "Alumno");
			Label totalStudent = new Label("- Número total de alumnos: " + totalStudentNumber.intValue());

			String[] tutorColumnNames = { "Tutor1", "Tutor2", "Tutor3" };
			Number totalTutorNumber = fachadaDatos.getTotalNumber(tutorColumnNames, PROYECTO);
			Label totalTutor = new Label("- Número total de tutores involucrados: " + totalTutorNumber.intValue());

			addComponents(totalProjects, totalFreeProject, totalStudent, totalTutor);
		} catch (SQLException e) {
			LOGGER.error("Error en estadísticas", e);
		}
	}

	private void createFilters() {
		Label filtersTitle = new Label("Filtros");
		filtersTitle.setStyleName(estiloTitulo);
		addComponent(filtersTitle);

		HorizontalLayout filters = new HorizontalLayout();
		filters.setSpacing(true);
		filters.setMargin(false);
		filters.setWidth("100%");
		addComponent(filters);

		projectFilter = new TextField("Filtrar por proyectos:");
		filters.addComponent(projectFilter);

		descriptionFilter = new TextField("Filtrar por descripción:");
		filters.addComponent(descriptionFilter);

		tutorsFilter = new TextField("Filtrar por tutores:");
		filters.addComponent(tutorsFilter);

		studentsFilter = new TextField("Filtrar por alumnos:");
		filters.addComponent(studentsFilter);

		courseFilter = new TextField("Filtrar por curso:");
		filters.addComponent(courseFilter);
	}

	private void createCurrentProjectsTable() {
		Label proyectosTitle = new Label("Descripción de proyectos");
		proyectosTitle.setStyleName(estiloTitulo);
		addComponent(proyectosTitle);

		table = new Table();
		addComponent(table);
		table.setWidth("100%");
		table.setPageLength(9);
		table.setColumnCollapsingAllowed(true);
		table.setContainerDataSource(beans);
		table.setVisibleColumns(TITLE, DESCRIPTION, TUTOR1, TUTOR2, TUTOR3, STUDENT1, STUDENT2, STUDENT3,
				COURSE_ASSIGNMENT);

		setTableColumnHeaders();
		setColumnExpandRatios();
		collapseOptionalFields();
	}

	private void setTableColumnHeaders() {
		table.setColumnHeader(TITLE, "Título");
		table.setColumnHeader(DESCRIPTION, "Descripción");
		table.setColumnHeader(TUTOR1, "Tutor 1");
		table.setColumnHeader(TUTOR2, "Tutor 2");
		table.setColumnHeader(TUTOR3, "Tutor 3");
		table.setColumnHeader(STUDENT1, "Alumno 1");
		table.setColumnHeader(STUDENT2, "Alumno 2");
		table.setColumnHeader(STUDENT3, "Alumno 3");
		table.setColumnHeader(COURSE_ASSIGNMENT, "Curso Asignación");
	}

	private void setColumnExpandRatios() {
		table.setColumnExpandRatio(TITLE, 5);
		table.setColumnExpandRatio(DESCRIPTION, 17);
		table.setColumnExpandRatio(TUTOR1, 3);
		table.setColumnExpandRatio(TUTOR2, 3);
		table.setColumnExpandRatio(TUTOR3, 3);
		table.setColumnExpandRatio(STUDENT1, 3);
		table.setColumnExpandRatio(STUDENT2, 3);
		table.setColumnExpandRatio(STUDENT3, 3);
		table.setColumnExpandRatio(COURSE_ASSIGNMENT, 3);
	}

	private void collapseOptionalFields() {
		List<ActiveProjectBean> itemIds = beans.getItemIds();
		Iterator<ActiveProjectBean> iterator = itemIds.iterator();
		boolean student2Flag = true;
		boolean student3Flag = true;
		boolean tutor2Flag = true;
		boolean tutor3Flag = true;

		while (iterator.hasNext()) {
			ActiveProjectBean next = iterator.next();
			ActiveProjectBean bean = beans.getItem(next).getBean();
			if (!"".equals(bean.getStudent2()))
				student2Flag = false;
			if (!"".equals(bean.getStudent3()))
				student3Flag = false;
			if (!"".equals(bean.getTutor2()))
				tutor2Flag = false;
			if (!"".equals(bean.getTutor3()))
				tutor3Flag = false;
		}

		if (student2Flag)
			table.setColumnCollapsed(STUDENT2, true);
		if (student3Flag)
			table.setColumnCollapsed(STUDENT3, true);
		if (tutor2Flag)
			table.setColumnCollapsed(TUTOR2, true);
		if (tutor3Flag)
			table.setColumnCollapsed(TUTOR3, true);
	}

	private void addFiltersListeners() {
		projectFilter.addTextChangeListener(new SimpleStringFilterListener(table, TITLE));
		descriptionFilter.addTextChangeListener(new SimpleStringFilterListener(table, DESCRIPTION));
		tutorsFilter.addTextChangeListener(new OrSimpleStringFilterListener(table, TUTOR1, TUTOR2, TUTOR3));
		studentsFilter.addTextChangeListener(new OrSimpleStringFilterListener(table, STUDENT1, STUDENT2, STUDENT3));
		courseFilter.addTextChangeListener(new SimpleStringFilterListener(table, COURSE_ASSIGNMENT));
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// Se inicializa el contenido de la vista en el constructor
	}
}
