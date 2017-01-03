package ubu.digit.ui.beans;

import java.io.Serializable;

public class HistoricProjectBean extends ProjectBean implements Serializable {

	private static final long serialVersionUID = -4441566584690195452L;

	private int numStudents;

	private String assignmentDate;

	private String presentationDate;

	private String score;

	private int totalDays;
	
	private String repositoryLink;

	public HistoricProjectBean() {
		// Constructor vacío (convención JavaBean)
	}

	public HistoricProjectBean(String title, String description, String tutor1, String tutor2, String tutor3,
			String student1, String student2, String student3, int numStudents, String assignmentDate,
			String presentationDate, String score, int totalDays, String repositoryLink) {
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
	}

	public int getNumStudents() {
		return numStudents;
	}

	public void setNumStudents(int numStudents) {
		this.numStudents = numStudents;
	}

	public String getAssignmentDate() {
		return assignmentDate;
	}

	public void setAssignmentDate(String assignmentDate) {
		this.assignmentDate = assignmentDate;
	}

	public String getPresentationDate() {
		return presentationDate;
	}

	public void setPresentationDate(String presentationDate) {
		this.presentationDate = presentationDate;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
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
}
