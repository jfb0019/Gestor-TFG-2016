package ubu.digit.ui.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ubu.digit.pesistence.SistInfData;
import ubu.digit.ui.beans.HistoricProjectBean;
import ubu.digit.ui.columngenerators.ProjectsColumnGenerator;
import ubu.digit.ui.columngenerators.TutorsColumnGenerator;
import ubu.digit.ui.components.Footer;
import ubu.digit.ui.components.NavigationBar;
import ubu.digit.ui.listeners.OrSimpleStringFilterListener;
import ubu.digit.ui.listeners.SimpleStringFilterListener;
import ubu.digit.util.ExternalProperties;
import static ubu.digit.util.Constants.*;
	
public class HistoricProjectsView extends VerticalLayout implements View {

	private static final long serialVersionUID = 8431807779365780674L;

	/**
	 * Logger de la clase.
	 */
	private static final Logger LOGGER = Logger.getLogger(HistoricProjectsView.class);

	public static final String VIEW_NAME = "historic-projects";

	private BeanItemContainer<HistoricProjectBean> beans;

	private SistInfData fachadaDatos;

	private ExternalProperties config;

	private Table table;

	private NumberFormat formatter;

	private transient Map<Integer, List<List<Object>>> yearOfProjects;

	private transient Map<Integer, List<List<Object>>> newProjects;

	private transient Map<Integer, List<List<Object>>> oldProjects;

	private transient Map<Integer, List<List<Object>>> presentedProjects;

	private int minYear;

	private int maxYear;

	private TextField projectFilter;

	private TextField tutorsFilter;

	private TextField assignmentDateFilter;

	private TextField presentationDateFilter;

	public HistoricProjectsView() {
		fachadaDatos = SistInfData.getInstance();
		config = ExternalProperties.getInstance("/WEB-INF/classes/config.properties", false);
		formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(2);
		setMargin(true);
		setSpacing(true);
		
		NavigationBar navBar = new NavigationBar();
		addComponent(navBar);

		createDataModel();
		createGlobalMetrics();
		createYearlyMetrics();
		createFilters();
		createHistoricProjectsTable();
		addFiltersListeners();

		Footer footer = new Footer("Historico.csv");
		addComponent(footer);
	}

	private void createDataModel() {
		beans = new BeanItemContainer<>(HistoricProjectBean.class);
		try (ResultSet result = fachadaDatos.getResultSet(HISTORICO, TITULO)) {
			while (result.next()) {
				int numStudents = 1;
				String title = result.getString(TITULO);
				String description = result.getString(DESCRIPCION);
				String tutor1 = result.getString(TUTOR1);
				String tutor2 = result.getString(TUTOR2);
				if (tutor2 == null)
					tutor2 = "";
				String tutor3 = result.getString(TUTOR3);
				if (tutor3 == null)
					tutor3 = "";
				String student1 = result.getString(ALUMNO1);
				String student2 = result.getString(ALUMNO2);
				if (student2 == null)
					student2 = "";
				else
					numStudents++;
				String student3 = result.getString(ALUMNO3);
				if (student3 == null)
					student3 = "";
				else
					numStudents++;
				String assignmentDate = transformDateToYMD(result.getString(FECHA_ASIGNACION));
				String presentationDate = transformDateToYMD(result.getString(FECHA_PRESENTACION));
				String score = result.getString(NOTA);
				int totalDays = result.getShort(TOTAL_DIAS);
				String repoLink = result.getString(ENLACE_REPOSITORIO);
				if(repoLink == null)
					repoLink = "";

				HistoricProjectBean bean = new HistoricProjectBean(title, description, tutor1, tutor2, tutor3, student1,
						student2, student3, numStudents, assignmentDate, presentationDate, score, totalDays, repoLink);
				beans.addBean(bean);
			}
		} catch (SQLException e) {
			LOGGER.error("Error en históricos", e);
		}
	}

	private String transformDateToYMD(String string) {
		SimpleDateFormat dmy = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat ymd = new SimpleDateFormat("yyyy/MM/dd");
		Date date = null;
		try {
			date = dmy.parse(string);
		}
		catch (ParseException e) {
			LOGGER.error("Error parsing dates", e);
		}
		return ymd.format(date);
	}

	private void createGlobalMetrics() {
		Label metricsTitle = new Label(INFO_ESTADISTICA);
		metricsTitle.setStyleName(TITLE_STYLE);
		addComponent(metricsTitle);
		try {
			Number totalProjectsNumber = fachadaDatos.getTotalNumber(TITULO, HISTORICO);
			Label totalProjects = new Label("Número total de proyectos: " + totalProjectsNumber.intValue());

			String[] studentColumnNames = { ALUMNO1, ALUMNO2, ALUMNO3 };
			Number totalStudentNumber = fachadaDatos.getTotalNumber(studentColumnNames, HISTORICO);
			Label totalStudents = new Label("Número total de alumnos: " + totalStudentNumber.intValue());

			Number avgScore = fachadaDatos.getAvgColumn(NOTA, HISTORICO);
			Number minScore = fachadaDatos.getMinColumn(NOTA, HISTORICO);
			Number maxScore = fachadaDatos.getMaxColumn(NOTA, HISTORICO);
			Number stdvScore = fachadaDatos.getStdvColumn(NOTA, HISTORICO);
			List<String> scores = new ArrayList<>();
			scores.add(formatter.format(avgScore));
			scores.add(formatter.format(minScore));
			scores.add(formatter.format(maxScore));
			scores.add(formatter.format(stdvScore));
			Label scoreStats = new Label("Calificación [media,min,max,stdv]: " + scores);

			Number avgDays = fachadaDatos.getAvgColumn(TOTAL_DIAS, HISTORICO);
			Number minDays = fachadaDatos.getMinColumn(TOTAL_DIAS, HISTORICO);
			Number maxDays = fachadaDatos.getMaxColumn(TOTAL_DIAS, HISTORICO);
			Number stdvDays = fachadaDatos.getStdvColumn(TOTAL_DIAS, HISTORICO);
			List<String> days = new ArrayList<>();
			days.add(formatter.format(avgDays));
			days.add(formatter.format(minDays));
			days.add(formatter.format(maxDays));
			days.add(formatter.format(stdvDays));
			Label daysStats = new Label("Tiempo/días [media,min,max,stdv]: " + days);

			addComponents(totalProjects, totalStudents, scoreStats, daysStats);
		} catch (SQLException e) {
			LOGGER.error("Error en históricos (metricas)", e);
		}
	}

	private void createYearlyMetrics() {
		initProjectsStructures();
		createYearlyAverageStats();
		createYearlyTotalStats();
	}

	private void initProjectsStructures() {
		minYear = getYearCourse(true).get(Calendar.YEAR);
		maxYear = getYearCourse(false).get(Calendar.YEAR);
		yearOfProjects = new HashMap<>();
		for (int year = minYear; year < maxYear + 1; year++) {
			try {
				yearOfProjects.put(year, fachadaDatos.getProjectsCurso(FECHA_ASIGNACION, FECHA_PRESENTACION,
						TOTAL_DIAS, NOTA, HISTORICO, year));
			} catch (SQLException e) {
				LOGGER.error("Error en historicos", e);
			}
		}
		organizeProjects();
	}

	private void organizeProjects() {
		newProjects = new HashMap<>();
		oldProjects = new HashMap<>();
		presentedProjects = new HashMap<>();
		for (int year = minYear; year <= maxYear; year++) {
			List<Object> currentProject;
			for (int index = 0; index < yearOfProjects.get(year).size(); index++) {
				currentProject = yearOfProjects.get(year).get(index);
				Calendar assignmentDate = Calendar.getInstance();
				assignmentDate.setTime((Date) currentProject.get(0));
				Calendar startDate = Calendar.getInstance();
				startDate.set(year, Integer.parseInt(config.getSetting("inicioCurso.mes")),
						Integer.parseInt(config.getSetting("inicioCurso.dia")));
				int totalDays = (int) currentProject.get(2);
				int totalYearNumber = totalDays / 360;

				if (assignmentDate.getTime().before(startDate.getTime())) {
					assignProjectCourses(year, currentProject, totalYearNumber, true);
				} else {
					assignProjectCourses(year, currentProject, totalYearNumber, false);
				}
				buildPresentedProjects(currentProject);
			}
		}
	}

	private void assignProjectCourses(int year, List<Object> project, int totalYearNumber, boolean currentCourse) {
		for (int yearCount = 0; yearCount <= totalYearNumber; yearCount++) {
			assignProject(year, yearCount, project, currentCourse);
		}
	}

	private void assignProject(int year, int yearNumber, List<Object> project, boolean isCurrentCourse) {
		int before = 0;
		if (!isCurrentCourse) {
			before = 1;
		}
		if (yearNumber == 0) {
			if (newProjects.containsKey(year + before)) {
				List<List<Object>> aux = newProjects.get(year + before);
				aux.add(project);
				newProjects.put(year + before, aux);
			} else {
				List<List<Object>> aux = new ArrayList<>();
				aux.add(project);
				newProjects.put(year + before, aux);
			}
		} else {
			if (oldProjects.containsKey(year + yearNumber + before)) {
				List<List<Object>> aux = oldProjects.get(year + yearNumber + before);
				aux.add(project);
				oldProjects.put(year + yearNumber + before, aux);
			} else {
				List<List<Object>> aux = new ArrayList<>();
				aux.add(project);
				oldProjects.put(year + yearNumber + before, aux);
			}
		}
	}

	private void buildPresentedProjects(List<Object> project) {
		Calendar presentedDate = Calendar.getInstance();
		presentedDate.setTime((Date) project.get(1));

		Calendar startDate = Calendar.getInstance();
		startDate.set(presentedDate.get(Calendar.YEAR), Integer.parseInt(config.getSetting("finPresentaciones.mes")),
				Integer.parseInt(config.getSetting("finPresentaciones.dia")));
		if (presentedDate.getTime().before(startDate.getTime())) {
			if (presentedProjects.containsKey(presentedDate.get(Calendar.YEAR))) {
				List<List<Object>> aux = presentedProjects.get(presentedDate.get(Calendar.YEAR));
				aux.add(project);
				presentedProjects.put(presentedDate.get(Calendar.YEAR), aux);
			} else {
				List<List<Object>> aux = new ArrayList<>();
				aux.add(project);
				presentedProjects.put(presentedDate.get(Calendar.YEAR), aux);
			}
		}
	}

	private Map<Integer, Number> getStudentsCount() {
		Map<Integer, Number> studentsCount = new HashMap<>();
		for (int year = minYear; year <= maxYear; year++) {
			List<Object> project;
			int numStudents = 0;
			if (newProjects.containsKey(year)) {
				for (int index = 0; index < newProjects.get(year).size(); index++) {
					project = newProjects.get(year).get(index);
					numStudents += getProjectNumberOfStudents(project);
				}
			}
			studentsCount.put(year, numStudents);
		}
		return studentsCount;
	}

	private int getProjectNumberOfStudents(List<Object> project) {
		int projectNumberOfStudents = 0;
		if (!"".equals(project.get(4)) && project.get(4) != null) {
			projectNumberOfStudents++;
		}
		if (!"".equals(project.get(5)) && project.get(5) != null) {
			projectNumberOfStudents++;
		}
		if (!"".equals(project.get(6)) && project.get(6) != null) {
			projectNumberOfStudents++;
		}
		return projectNumberOfStudents;
	}

	private Map<Integer, Number> getTutorsCount() {
		Map<Integer, Number> tutorsCount = new HashMap<>();
		for (int year = minYear; year <= maxYear; year++) {
			List<Object> current;
			int numTutors = 0;
			if (newProjects.containsKey(year)) {
				for (int index = 0; index < newProjects.get(year).size(); index++) {
					current = newProjects.get(year).get(index);
					numTutors += getProjectNumberOfTutors(current);
				}
			}
			tutorsCount.put(year, numTutors);
		}
		return tutorsCount;
	}

	private int getProjectNumberOfTutors(List<Object> project) {
		int projectNumberOfTutors = 0;
		if (!"".equals(project.get(7)) && project.get(7) != null) {
			projectNumberOfTutors++;
		}
		if (!"".equals(project.get(8)) && project.get(8) != null) {
			projectNumberOfTutors++;
		}
		if (!"".equals(project.get(9)) && project.get(9) != null) {
			projectNumberOfTutors++;
		}
		return projectNumberOfTutors;
	}

	private void createYearlyAverageStats() {
		Map<Integer, Number> averageScores = getAverageScores();
		Map<Integer, Number> averageTotalDays = getAverageTotalDays();
		Map<Integer, Number> averageMonths = new HashMap<>();

		for (int index = minYear; index <= maxYear; index++) {
			Number averageDays = averageTotalDays.get(index);
			averageMonths.put(index, averageDays.floatValue() / 31);
		}

		List<String> scores = new ArrayList<>();
		List<String> days = new ArrayList<>();
		List<String> months = new ArrayList<>();

		for (int year = minYear; year <= maxYear; year++) {
			scores.add(formatter.format(averageScores.get(year)));
			days.add(formatter.format(averageTotalDays.get(year)));
			months.add(formatter.format(averageMonths.get(year)));
		}

		addComponent(new Label("Media de notas por curso: " + scores));
		addComponent(new Label("Media de dias por curso: " + days));
		addComponent(new Label("Media de meses por curso: " + months));
	}

	private Map<Integer, Number> getAverageScores() {
		Map<Integer, Number> averageScores = new HashMap<>();
		for (int year = minYear; year <= maxYear; year++) {
			List<Object> project;
			double mean = 0;
			if (newProjects.containsKey(year)) {
				for (int index = 0; index < newProjects.get(year).size(); index++) {
					project = newProjects.get(year).get(index);
					double score = (double) project.get(3);
					mean += score;
				}
			}
			mean = mean / newProjects.get(year).size();
			averageScores.put(year, mean);
		}
		return averageScores;
	}

	private Map<Integer, Number> getAverageTotalDays() {
		Map<Integer, Number> averageTotalDays = new HashMap<>();
		for (int year = minYear; year <= maxYear; year++) {
			List<Object> project;
			double mean = 0;
			if (newProjects.containsKey(year)) {
				for (int index = 0; index < newProjects.get(year).size(); index++) {
					project = newProjects.get(year).get(index);
					int totalDays = (int) project.get(2);
					mean += totalDays;
				}
			}
			mean = mean / newProjects.get(year).size();
			averageTotalDays.put(year, mean);
		}
		return averageTotalDays;
	}

	private void createYearlyTotalStats() {
		List<String> courses = new ArrayList<>();
		List<Number> yearlyAssignedProjects = new ArrayList<>();
		List<Number> yearlyOldProjects = new ArrayList<>();
		List<Number> yearlyPresentedProjects = new ArrayList<>();
		List<Number> yearlyAssignedStudents = new ArrayList<>();
		List<Number> yearlyAssignedTutors = new ArrayList<>();

		for (int year = minYear; year <= maxYear; year++) {
			courses.add(year - 1 + "/" + year);
			yearlyAssignedProjects.add(geProjectsCount(newProjects).get(year));
			yearlyOldProjects.add(geProjectsCount(oldProjects).get(year));
			yearlyAssignedStudents.add(getStudentsCount().get(year));
			yearlyAssignedTutors.add(getTutorsCount().get(year));
			yearlyPresentedProjects.add(getPresentedCountProjects().get(year));
		}

		Label asignedYearlyProjects = new Label(
				"Número total de proyectos asignados por curso: " + yearlyAssignedProjects);
		Label presentedYearlyProjects = new Label(
				"Número total de proyectos presentados por curso: " + yearlyPresentedProjects);
		Label asignedYearlyStudents = new Label(
				"Número total de alumnos asignados por curso: " + yearlyAssignedStudents);
		Label asignedYearlyTutors = new Label(
				"Número total de tutores con nuevas asignaciones por curso: " + yearlyAssignedTutors);
		Label asignedOldProjects = new Label("Número de proyectos ya asignados por curso: " + yearlyOldProjects);
		Label allCourses = new Label("Cursos: " + courses);
		addComponents(asignedYearlyProjects, presentedYearlyProjects, asignedYearlyStudents, asignedYearlyTutors,
				asignedOldProjects, allCourses);
	}

	private Map<Integer, Number> geProjectsCount(Map<Integer, List<List<Object>>> projects) {
		Map<Integer, Number> projectsCount = new HashMap<>();
		for (int year = minYear; year <= maxYear; year++) {
			int totalProjects = 0;
			if (projects.containsKey(year))
				totalProjects += projects.get(year).size();
			projectsCount.put(year, totalProjects);
		}
		return projectsCount;
	}

	private Map<Integer, Number> getPresentedCountProjects() {
		Map<Integer, Number> presentedCountProjects = new HashMap<>();
		for (int year = minYear; year <= maxYear; year++) {
			List<Object> project;
			Number presented = 0;
			if (presentedProjects.containsKey(year)) {
				for (int index = 0; index < presentedProjects.get(year).size(); index++) {
					project = presentedProjects.get(year).get(index);
					presented = presented.intValue() + getPresentedProject(project);
				}
			}
			presentedCountProjects.put(year, presented);
		}
		return presentedCountProjects;
	}

	private int getPresentedProject(List<Object> project) {
		int presented = 0;
		if (project.get(1) != null && !"".equals(project.get(1))) {
			presented = 1;
		}
		return presented;
	}

	private Calendar getYearCourse(Boolean isMinimum) {
		Calendar courseDate = Calendar.getInstance();
		Long dateTime = null;
		if (isMinimum) {
			try {
				dateTime = fachadaDatos.getYear(FECHA_PRESENTACION, HISTORICO, true).getTime();
			} catch (SQLException e) {
				LOGGER.error("Error en obtenerCurso", e);
			}

		} else {
			try {
				dateTime = fachadaDatos.getYear(FECHA_PRESENTACION, HISTORICO, false).getTime();
			} catch (SQLException e) {
				LOGGER.error("Error en obtenerCurso", e);
			}
		}
		courseDate.setTimeInMillis(dateTime);
		return courseDate;
	}

	private void createFilters() {
		Label filtersTitle = new Label(FILTROS);
		filtersTitle.setStyleName(TITLE_STYLE);
		addComponent(filtersTitle);

		HorizontalLayout filters = new HorizontalLayout();
		filters.setSpacing(true);
		filters.setMargin(false);
		filters.setWidth("100%");
		addComponent(filters);

		projectFilter = new TextField("Filtrar por proyectos:");
		filters.addComponent(projectFilter);

		tutorsFilter = new TextField("Filtrar por tutores:");
		filters.addComponent(tutorsFilter);

		assignmentDateFilter = new TextField("Filtrar por fecha de asignación:");
		filters.addComponent(assignmentDateFilter);

		presentationDateFilter = new TextField("Filtrar por fecha de presentación:");
		filters.addComponent(presentationDateFilter);
	}

	private void createHistoricProjectsTable() {
		Label projectsTitle = new Label(DESCRIPCION_PROYECTOS);
		projectsTitle.setStyleName(TITLE_STYLE);
		addComponent(projectsTitle);

		table = new Table();
		addComponent(table);
		table.setWidth("100%");
		table.setPageLength(10);
		table.setColumnCollapsingAllowed(true);
		table.setContainerDataSource(beans);
		addGeneratedColumns();
		table.setVisibleColumns(PROJECTS, TUTORS, NUM_STUDENTS, ASSIGNMENT_DATE, PRESENTATION_DATE, SCORE);
		setTableColumnHeaders();
		setColumnExpandRatios();
		showDescriptionOnClick();
	}

	private void addGeneratedColumns(){
		table.addGeneratedColumn(TUTORS, new TutorsColumnGenerator());
		table.addGeneratedColumn(PROJECTS, new ProjectsColumnGenerator());
		
	}
	
	private void setTableColumnHeaders() {
		table.setColumnHeader(PROJECTS, "Título");
		table.setColumnHeader(TUTORS, "Tutor/es");
		table.setColumnHeader(NUM_STUDENTS, "Nº Alumnos");
		table.setColumnHeader(ASSIGNMENT_DATE, "Fecha Asignación");
		table.setColumnHeader(PRESENTATION_DATE, "Fecha Presentación");
		table.setColumnHeader(SCORE, "Nota");
	}

	private void setColumnExpandRatios() {
		table.setColumnExpandRatio(PROJECTS, 35);
		table.setColumnExpandRatio(TUTORS, 9);
		table.setColumnExpandRatio(NUM_STUDENTS, 4);
		table.setColumnExpandRatio(ASSIGNMENT_DATE, 6);
		table.setColumnExpandRatio(PRESENTATION_DATE, 6);
		table.setColumnExpandRatio(SCORE, 3);
	}

	private void showDescriptionOnClick() {
		table.setSelectable(true);
		table.setMultiSelect(false);
		table.setImmediate(true);
		table.setNullSelectionAllowed(true);
		table.addValueChangeListener(new TableValueChangeListener());
	}

	private class TableValueChangeListener implements Property.ValueChangeListener {
		private static final long serialVersionUID = -5055796506090094836L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			if (event.getProperty().getValue() != null) {
				HistoricProjectBean value = (HistoricProjectBean) event.getProperty().getValue();
				String description = value.getDescription();

				if (description != null && !"".equals(description)) {
					Notification notification = new Notification("", description, Notification.Type.HUMANIZED_MESSAGE);
					notification.setDelayMsec(10000);
					notification.setPosition(Position.BOTTOM_CENTER);
					notification.setIcon(FontAwesome.FILE_TEXT_O);
					notification.show(Page.getCurrent());
				} else {
					Notification.show("Información", "No hay una descripción disponible para ese proyecto",
							Notification.Type.HUMANIZED_MESSAGE);
				}
			}
		}
	}

	private void addFiltersListeners() {
		projectFilter.addTextChangeListener(new SimpleStringFilterListener(table, TITLE));
		tutorsFilter.addTextChangeListener(new OrSimpleStringFilterListener(table, TUTOR1, TUTOR2, TUTOR3));
		assignmentDateFilter.addTextChangeListener(new SimpleStringFilterListener(table, ASSIGNMENT_DATE));
		presentationDateFilter.addTextChangeListener(new SimpleStringFilterListener(table, PRESENTATION_DATE));
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// Se inicializa el contenido de la vista en el constructor
	}
}
