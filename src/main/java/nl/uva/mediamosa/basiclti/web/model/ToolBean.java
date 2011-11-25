package nl.uva.mediamosa.basiclti.web.model;

public class ToolBean {
	
	private boolean instructor = false;
	private String contexTitle = "";
	private String instructorName = "";
	private String mmUsername = "basiclti";

	public boolean isInstructor() {
		return instructor;
	}

	public void setInstructor(boolean instructor) {
		this.instructor = instructor;
	}

	public String getContextTitle() {
		return contexTitle;
	}

	public void setContextTitle(String contexTitle) {
		this.contexTitle = contexTitle;
	}

	public String getInstructorName() {
		return instructorName;
	}

	public void setInstructorName(String instructorName) {
		this.instructorName = instructorName;
	}

	public String getMmUsername() {
		return mmUsername;
	}

	public void setMmUsername(String mmUsername) {
		this.mmUsername = mmUsername;
	}
	
}
