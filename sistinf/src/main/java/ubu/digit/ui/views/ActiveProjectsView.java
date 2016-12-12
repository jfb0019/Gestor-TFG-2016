package ubu.digit.ui.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
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

		Footer footer = new Footer("Proyecto.csv");
		addComponent(footer);
	}

	private void createDataModel() {
		beans = new BeanItemContainer<ActiveProjectBean>(ActiveProjectBean.class);
		try (ResultSet result = fachadaDatos.getResultSet("Proyecto", "Titulo")) {
			while (result.next()) {
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
			Number totalProjectsNumber = fachadaDatos.getTotalNumber("Titulo", "Proyecto");
			Label totalProjects = new Label("- Número total de proyectos: " + totalProjectsNumber.intValue());

			Number totalFreeProjectNumber = fachadaDatos.getTotalFreeProject();
			Label totalFreeProject = new Label("- Número total de proyectos sin asignar: " + totalFreeProjectNumber.intValue());

			Number totalStudentNumber = fachadaDatos.getTotalNumber("ApellidosNombre", "Alumno");
			Label totalStudent = new Label("- Número total de alumnos: " + totalStudentNumber.intValue());

			String[] tutorColumnNames = { "Tutor1", "Tutor2", "Tutor3" };
			Number totalTutorNumber = fachadaDatos.getTotalNumber(tutorColumnNames, "Proyecto");
			Label totalTutor = new Label("- Número total de tutores involucrados: " + totalTutorNumber.intValue());

			addComponents(totalProjects, totalFreeProject, totalStudent, totalTutor);
		} catch (SQLException e) {
			LOGGER.error("Error en estadísticas", e);
		}
	}
	
	private void createFilters() {
		HorizontalLayout filters = new HorizontalLayout();
		filters.setSpacing(true);
		filters.setMargin(false);
		filters.setWidth("100%");
		addComponent(filters);

		TextField projectFilter = new TextField("Filtrar por proyectos:");
		filters.addComponent(projectFilter);
		projectFilter.addTextChangeListener(new TextChangeListener() {
			private static final long serialVersionUID = 2951555260639865997L;
			SimpleStringFilter filter = null;

			@Override
			public void textChange(TextChangeEvent event) {
				Filterable f = (Filterable) table.getContainerDataSource();
				if (filter != null)
					f.removeContainerFilter(filter);
				filter = new SimpleStringFilter("title", (String) event.getText(), true, false);
				f.addContainerFilter(filter);
			}
		});

		TextField descriptionFilter = new TextField("Filtrar por descripción:");
		filters.addComponent(descriptionFilter);
		descriptionFilter.addTextChangeListener(new TextChangeListener() {
			private static final long serialVersionUID = -2576084801398434433L;
			SimpleStringFilter filter = null;

			@Override
			public void textChange(TextChangeEvent event) {
				Filterable f = (Filterable) table.getContainerDataSource();
				if (filter != null)
					f.removeContainerFilter(filter);
				filter = new SimpleStringFilter("description", event.getText(), true, false);
				f.addContainerFilter(filter);
			}
		});

		TextField tutorsFilter = new TextField("Filtrar por tutores:");
		filters.addComponent(tutorsFilter);
		tutorsFilter.addTextChangeListener(new TextChangeListener() {
			private static final long serialVersionUID = 2532040717015693102L;
			Or filter = null;

			@Override
			public void textChange(TextChangeEvent event) {
				Filterable f = (Filterable) table.getContainerDataSource();
				if (filter != null)
					f.removeContainerFilter(filter);
				filter = new Or(new SimpleStringFilter("tutor1", event.getText(), true, false),
						new SimpleStringFilter("tutor2", event.getText(), true, false),
						new SimpleStringFilter("tutor3", event.getText(), true, false));
				f.addContainerFilter(filter);
			}
		});

		TextField studentsFilter = new TextField("Filtrar por alumnos:");
		filters.addComponent(studentsFilter);
		studentsFilter.addTextChangeListener(new TextChangeListener() {
			private static final long serialVersionUID = -4947036744365165277L;
			Or filter = null;

			@Override
			public void textChange(TextChangeEvent event) {
				Filterable f = (Filterable) table.getContainerDataSource();
				if (filter != null)
					f.removeContainerFilter(filter);
				filter = new Or(new SimpleStringFilter("student1", event.getText(), true, false),
						new SimpleStringFilter("student2", event.getText(), true, false),
						new SimpleStringFilter("student3", event.getText(), true, false));
				f.addContainerFilter(filter);
			}
		});

		TextField courseFilter = new TextField("Filtrar por curso:");
		filters.addComponent(courseFilter);
		courseFilter.addTextChangeListener(new TextChangeListener() {
			private static final long serialVersionUID = 1706510943596355465L;
			SimpleStringFilter filter = null;

			@Override
			public void textChange(TextChangeEvent event) {
				Filterable f = (Filterable) table.getContainerDataSource();
				if (filter != null)
					f.removeContainerFilter(filter);
				filter = new SimpleStringFilter("courseAssignment", (String) event.getText(), true, false);
				f.addContainerFilter(filter);
			}
		});
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
		table.setVisibleColumns("title", "description", "tutor1", "tutor2", "tutor3", "student1", "student2",
				"student3", "courseAssignment");

		setTableColumnHeaders();
		setColumnExpandRatios();
		collapseOptionalFields();
	}

	private void setTableColumnHeaders() {
		table.setColumnHeader("title", "Título");
		table.setColumnHeader("description", "Descripción");
		table.setColumnHeader("tutor1", "Tutor 1");
		table.setColumnHeader("tutor2", "Tutor 2");
		table.setColumnHeader("tutor3", "Tutor 3");
		table.setColumnHeader("student1", "Alumno 1");
		table.setColumnHeader("student2", "Alumno 2");
		table.setColumnHeader("student3", "Alumno 3");
		table.setColumnHeader("courseAssignment", "Curso Asignación");
	}

	private void setColumnExpandRatios() {
		table.setColumnExpandRatio("title", 5);
		table.setColumnExpandRatio("description", 17);
		table.setColumnExpandRatio("tutor1", 3);
		table.setColumnExpandRatio("tutor2", 3);
		table.setColumnExpandRatio("tutor3", 3);
		table.setColumnExpandRatio("student1", 3);
		table.setColumnExpandRatio("student2", 3);
		table.setColumnExpandRatio("student3", 3);
		table.setColumnExpandRatio("courseAssignment", 3);
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
			table.setColumnCollapsed("student2", true);
		if (student3Flag)
			table.setColumnCollapsed("student3", true);
		if (tutor2Flag)
			table.setColumnCollapsed("tutor2", true);
		if (tutor3Flag)
			table.setColumnCollapsed("tutor3", true);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

}
