package app.academics;

import app.*;

import java.io.Serializable;
import java.util.Objects;

public class Course implements Serializable
{
	private String code;
	private String name;
	private int credits;
	private int semester;

	public Course() {
		this.code="";
		this.name="";
		this.credits=0;
		this.semester=0;
	}

	public Course(String name, String code, int semester, int credits) {
		this.code = code;
		this.name = name;
		this.credits = credits;
		this.semester = semester;
	}
	// Utility Functions

	public void readData() {
		System.out.print("\nEnter  Course Code : ");String code = University.scanner.next();
		System.out.print("\nEnter  name : ");String name = University.scanner.next();
		System.out.print("\nEnter  Credits : ");
		this.credits = University.getIntegerFromInput();
		System.out.print("\nEnter  Semester : ");
		this.semester = University.getIntegerFromInput();
		this.code = code.toUpperCase();
		this.name= name;
		UniversityApp.makeClear();
	}

	// Getters

    public String getName() {return name;}

	public int getCredits() {return credits;}

	public String getCode() {return code;}

	public int getSemester() { return semester; }

	// Display Functions

	public String toString() {
		String paddedCode = String.format("%-7s", code);
		String paddedName = String.format("%-30s", name);
		String paddedCredits = String.format("%-11s", credits);
		String paddedSemester = String.format("%-10s", semester);
	
		return paddedCode +  paddedName + "Credits: " + paddedCredits + "Semester: " + paddedSemester;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Course course = (Course) o;
		return Objects.equals(code, course.code);
	}

	@Override
	public int hashCode() {return Objects.hashCode(code);}

}

