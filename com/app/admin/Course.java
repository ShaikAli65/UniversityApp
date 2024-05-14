package app.admin;

import app.*;

import java.util.Objects;

public class Course //implements University
{
	final static java.util.Scanner scanner = new java.util.Scanner(System.in);
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

	// Utility Functions

	public void readData() {
		System.out.print("\nEnter  Course Code : ");String code = scanner.next();
		System.out.print("\nEnter  name : ");String name = scanner.next();
		System.out.print("\nEnter  Credits : ");
		this.credits = University.getIntegerFromInput();
		System.out.print("\nEnter  Semester : ");
		this.semester = University.getIntegerFromInput();
		this.code = code.toUpperCase();
		this.name= name;
		UniversityApp.makeClear();
	}

	// Getters

    public String getName() {
		return name;
    }

	public int getCredits() {
		return credits;
	}

	public String getCode() {
		return code;
	}

	public int getSemester() {
		return semester;
	}

	// Display Functions

	public String toString()
	{
		return "Code : "+code+"\tName : "+name+"\tCredits : "+credits+"\t Semester : "+semester;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Course course = (Course) o;
		return Objects.equals(code, course.code);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(code);
	}

}

