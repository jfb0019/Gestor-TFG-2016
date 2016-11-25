package ubu.digit.ui.beans;

import java.io.Serializable;

public class ActiveProjectBean implements Serializable {

	private static final long serialVersionUID = 7668147008225381085L;

	private String title;

	private String description;

	private String tutor1;

	private String tutor2;

	private String tutor3;

	private String student1;

	private String student2;

	private String student3;

	private String courseAssignment;

	public ActiveProjectBean(String title, String description, String tutor1, String tutor2, String tutor3,
			String student1, String student2, String student3, String courseAssignment) {
		this.title = title;
		this.description = description;
		this.tutor1 = tutor1;
		this.tutor2 = tutor2;
		this.tutor3 = tutor3;
		this.student1 = student1;
		this.student2 = student2;
		this.student3 = student3;
		this.courseAssignment = courseAssignment;
	}
	
	public ActiveProjectBean() {
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

	public String getCourseAssignment() {
		return courseAssignment;
	}

	public void setCourseAssignment(String courseAssignment) {
		this.courseAssignment = courseAssignment;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
