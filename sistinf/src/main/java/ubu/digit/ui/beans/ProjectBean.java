package ubu.digit.ui.beans;

import java.io.Serializable;

public class ProjectBean implements Serializable {

	private static final long serialVersionUID = -843095073424042061L;

	protected String title;

	protected String description;

	protected String tutor1;

	protected String tutor2;

	protected String tutor3;

	protected String student1;

	protected String student2;

	protected String student3;

	public ProjectBean() {
		// Constructor vacío (convención JavaBean)
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
}
