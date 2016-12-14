package ubu.digit.ui.beans;

import java.io.Serializable;

public class ActiveProjectBean extends ProjectBean implements Serializable {

	private static final long serialVersionUID = 7668147008225381085L;

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
		// Constructor vacío (convención JavaBean)
	}

	public String getCourseAssignment() {
		return courseAssignment;
	}

	public void setCourseAssignment(String courseAssignment) {
		this.courseAssignment = courseAssignment;
	}
}
