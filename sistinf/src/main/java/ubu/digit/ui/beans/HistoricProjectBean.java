package ubu.digit.ui.beans;

import java.io.Serializable;

public class HistoricProjectBean implements Serializable {

	private static final long serialVersionUID = -4441566584690195452L;

	private String title;

	private String description;

	private String tutor1;

	private String tutor2;

	private String tutor3;

	private String student1;

	private String student2;

	private String student3;

	private int numStudents;

	private String assignmentDate;

	private String presentationDate;

	private String score;

	private int totalDays;

	public HistoricProjectBean() {

	}

	public HistoricProjectBean(String title, String description, String tutor1, String tutor2, String tutor3,
			String student1, String student2, String student3, int numStudents, String assignmentDate,
			String presentationDate, String score, int totalDays) {
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
		this.setScore(score);
		this.totalDays = totalDays;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTutor1() {
		return tutor1;
	}

	public void setTutor1(String tutor1) {
		this.tutor1 = tutor1;
	}

	public String getTutor2() {
		return tutor2;
	}

	public void setTutor2(String tutor2) {
		this.tutor2 = tutor2;
	}

	public String getTutor3() {
		return tutor3;
	}

	public void setTutor3(String tutor3) {
		this.tutor3 = tutor3;
	}

	public String getStudent1() {
		return student1;
	}

	public void setStudent1(String student1) {
		this.student1 = student1;
	}

	public String getStudent2() {
		return student2;
	}

	public void setStudent2(String student2) {
		this.student2 = student2;
	}

	public String getStudent3() {
		return student3;
	}

	public void setStudent3(String student3) {
		this.student3 = student3;
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
}
