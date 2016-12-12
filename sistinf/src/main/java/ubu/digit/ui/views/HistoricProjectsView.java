package ubu.digit.ui.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import ubu.digit.util.ExternalProperties;

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
	
	private String estiloTitulo;

	private Table table;

	private NumberFormat formatter;

	private Map<Integer, List<List<Object>>> yearOfProjects;

	private Map<Integer, List<List<Object>>> newProjects;

	private Map<Integer, List<List<Object>>> oldProjects;

	private Map<Integer, List<List<Object>>> presentedProjects;

	private int minYear;
	private int maxYear;

	public HistoricProjectsView() {
		fachadaDatos = SistInfData.getInstance();
		config = ExternalProperties.getInstance("/WEB-INF/classes/config.properties", false);
		estiloTitulo = "lbl-title";
		formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(2);
		setMargin(true);
		setSpacing(true);
		NavigationBar navBar = new NavigationBar();
		addComponent(navBar);

		createDataModel();
		createGlobalMetrics();
		createYearlyMetrics();
		createHistoricProjectsTable();

		Footer footer = new Footer("Historico.csv");
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

	private void createGlobalMetrics() {
		Label metricsTitle = new Label("Métricas");
		metricsTitle.setStyleName(estiloTitulo);
		addComponent(metricsTitle);
		try {
			Number totalProjectsNumber = fachadaDatos.getTotalNumber("Titulo", "Historico");
			Label totalProjects = new Label("Número total de proyectos: " + totalProjectsNumber.intValue());

			String[] studentColumnNames = { "Alumno1", "Alumno2", "Alumno3" };
			Number totalStudentNumber = fachadaDatos.getTotalNumber(studentColumnNames, "Historico");
			Label totalStudents = new Label("Número total de alumnos: " + totalStudentNumber.intValue());

			Number avgScore = fachadaDatos.getAvgColumn("Nota", "Historico");
			Number minScore = fachadaDatos.getMinColumn("Nota", "Historico");
			Number maxScore = fachadaDatos.getMaxColumn("Nota", "Historico");
			Number stdvScore = fachadaDatos.getStdvColumn("Nota", "Historico");
			List<String> scores = new ArrayList<>();
			scores.add(formatter.format(avgScore));
			scores.add(formatter.format(minScore));
			scores.add(formatter.format(maxScore));
			scores.add(formatter.format(stdvScore));
			Label scoreStats = new Label("Calificación [media,min,max,stdv]: " + scores);
			
			Number avgDays = fachadaDatos.getAvgColumn("TotalDias", "Historico");
			Number minDays = fachadaDatos.getMinColumn("TotalDias", "Historico");
			Number maxDays = fachadaDatos.getMaxColumn("TotalDias", "Historico");
			Number stdvDays = fachadaDatos.getStdvColumn("TotalDias", "Historico");
			List<String> days = new ArrayList<>();
			days.add(formatter.format(avgDays));
			days.add(formatter.format(minDays));
			days.add(formatter.format(maxDays));
			days.add(formatter.format(stdvDays));
			Label daysStats = new Label("Tiempo/días [media,min,max,stdv]: "+ days);

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
				yearOfProjects.put(year, fachadaDatos.getProjectsCurso("FechaAsignacion", "FechaPresentacion",
						"TotalDias", "Nota", "Historico", year));
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
			List<Object> currentProject = new ArrayList<>();
			for (int index = 0; index < yearOfProjects.get(year).size(); index++) {
				currentProject = (List<Object>) yearOfProjects.get(year).get(index);
				Calendar assignmentDate = Calendar.getInstance();
				assignmentDate.setTime((Date) currentProject.get(0));
				Calendar startDate = Calendar.getInstance();
				startDate.set(year, Integer.parseInt(config.getSetting("mesInicio")),
						Integer.parseInt(config.getSetting("diaInicio")));
				int totalDays = (int) currentProject.get(2);
				int totalYearNumber = totalDays / 360;

				if (assignmentDate.getTime().before(startDate.getTime())) {
					for (int yearCount = 0; yearCount <= totalYearNumber; yearCount++) {
						assignProject(year, yearCount, currentProject, true);
					}
				} else {
					for (int yearCount = 0; yearCount <= totalYearNumber; yearCount++) {
						assignProject(year, yearCount, currentProject, false);
					}
				}
				buildPresentedProjects(currentProject);
			}
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
		startDate.set(presentedDate.get(Calendar.YEAR), Calendar.OCTOBER, 1);

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
					project = (List<Object>) newProjects.get(year).get(index);
					if (!"".equals(project.get(4)) && project.get(4) != null) {
						numStudents++;
					}
					if (!"".equals(project.get(5)) && project.get(5) != null) {
						numStudents++;
					}
					if (!"".equals(project.get(6)) && project.get(6) != null) {
						numStudents++;
					}
				}
			}
			studentsCount.put(year, numStudents);
		}
		return studentsCount;
	}

	private Map<Integer, Number> getTutorsCount() {
		Map<Integer, Number> tutorsCount = new HashMap<>();
		for (int year = minYear; year <= maxYear; year++) {
			List<Object> current;
			int numTutors = 0;
			if (newProjects.containsKey(year)) {
				for (int index = 0; index < newProjects.get(year).size(); index++) {
					current = (List<Object>) newProjects.get(year).get(index);
					if (!"".equals(current.get(7)) && current.get(7) != null) {
						numTutors++;
					}
					if (!"".equals(current.get(8)) && current.get(8) != null) {
						numTutors++;
					}
					if (!"".equals(current.get(9)) && current.get(9) != null) {
						numTutors++;
					}
				}
			}
			tutorsCount.put(year, numTutors);
		}
		return tutorsCount;
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
					project = (List<Object>) newProjects.get(year).get(index);
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
					project = (List<Object>) newProjects.get(year).get(index);
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
		List<Number> yearlyAssignedProjects = new ArrayList<>();
		List<Number> yearlyPresentedProjects = new ArrayList<>();
		List<Number> yearlyAssignedStudents = new ArrayList<>();
		List<Number> yearlyAssignedTutors = new ArrayList<>();

		for (int year = minYear; year <= maxYear; year++){
			yearlyAssignedProjects.add(newProjects.get(year).size());
			yearlyAssignedStudents.add(getStudentsCount().get(year));
			yearlyAssignedTutors.add(getTutorsCount().get(year));
			yearlyPresentedProjects.add(getPresentedCountProjects().get(year));
		}
		
		Label asignedProjects = new Label("Número total de proyectos asignados por curso: " + yearlyAssignedProjects);
		Label presentedProjects = new Label("Número total de proyectos presentados por curso: " + yearlyPresentedProjects);
		Label asignedStudents = new Label("Número total de alumnos asignados por curso: " + yearlyAssignedStudents);
		Label asignedTutors = new Label("Número total de tutores con nuevas asignaciones por curso: " + yearlyAssignedTutors);
		addComponents(asignedProjects,presentedProjects,asignedStudents,asignedTutors);
	}

	private Map<Integer, Number> getPresentedCountProjects() {
		Map<Integer, Number> presentedCountProjects = new HashMap<>();
		for (int year = minYear; year <= maxYear; year++) {
			List<Object> project;
			Number presented = 0;
			if (presentedProjects.containsKey(year)) {
				for (int index = 0; index < presentedProjects.get(year).size(); index++) {
					project = (List<Object>) presentedProjects.get(year).get(index);
					if (project.get(1) != null && !"".equals(project.get(1))) {
						presented = presented.intValue() + 1;
					}
				}
			}
			presentedCountProjects.put(year, presented);
		}
		return presentedCountProjects;
	}

	private Calendar getYearCourse(Boolean isMinimum) {
		Calendar courseDate = Calendar.getInstance();
		Long dateTime = null;
		if (isMinimum) {
			try {
				dateTime = fachadaDatos.getYear("FechaPresentacion", "Historico", true).getTime();
			} catch (SQLException e) {
				LOGGER.error("Error en obtenerCurso", e);
			}

		} else if (!isMinimum) {
			try {
				dateTime = fachadaDatos.getYear("FechaPresentacion", "Historico", false).getTime();
			} catch (SQLException e) {
				LOGGER.error("Error en obtenerCurso", e);
			}
		}
		courseDate.setTimeInMillis(dateTime);
		return courseDate;
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
