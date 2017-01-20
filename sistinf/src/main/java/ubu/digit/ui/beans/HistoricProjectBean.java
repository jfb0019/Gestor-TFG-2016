package ubu.digit.ui.beans;

import java.io.Serializable;
import java.time.LocalDate;

public class HistoricProjectBean extends ProjectBean implements Serializable {

	private static final long serialVersionUID = -4441566584690195452L;

	private int numStudents;

	private int numTutors;

	private LocalDate assignmentDate;

	private LocalDate presentationDate;

	private Double score;

	private int totalDays;

	private String repositoryLink;

	public HistoricProjectBean() {
		// Constructor vacío (convención JavaBean)
	}

	public HistoricProjectBean(String title, String description, String tutor1, String tutor2, String tutor3,
			String student1, String student2, String student3, int numStudents, int numTutors, LocalDate assignmentDate,
			LocalDate presentationDate, Double score, int totalDays, String repositoryLink) {
		this.title = title;
		this.description = description;
		this.tutor1 = tutor1;
		this.tutor2 = tutor2;
		this.tutor3 = tutor3;
		this.student1 = student1;
		this.student2 = student2;
		this.student3 = student3;
		this.numStudents = numStudents;
		this.assignmentDate = assignmentDate;
		this.presentationDate = presentationDate;
		this.score = score;
		this.totalDays = totalDays;
		this.repositoryLink = repositoryLink;
		this.numTutors = numTutors;
	}

	public int getNumStudents() {
		return numStudents;
	}

	public void setNumStudents(int numStudents) {
		this.numStudents = numStudents;
	}

	public LocalDate getAssignmentDate() {
		return assignmentDate;
	}

	public void setAssignmentDate(LocalDate assignmentDate) {
		this.assignmentDate = assignmentDate;
	}

	public LocalDate getPresentationDate() {
		return presentationDate;
	}

	public void setPresentationDate(LocalDate presentationDate) {
		this.presentationDate = presentationDate;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public int getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
	}

	public String getRepositoryLink() {
		return repositoryLink;
	}

	public void setRepositoryLink(String repositoryLink) {
		this.repositoryLink = repositoryLink;
	}

	public int getNumTutors() {
		return numTutors;
	}

	public void setNumTutors(int numTutors) {
		this.numTutors = numTutors;
	}
}
