package app.admin;

import app.University;

import java.util.Objects;
import java.util.Scanner;


public class  Student extends Person implements Comparable<Student>//, Serializable
{
	private static final Scanner scanner = University.scanner;
	protected String rollNo;
	protected String branch;
	private int semester;
	public int no_courses;
	protected double cgpa;

	public Student() {
		super();
	}
	public Student(String name, String rollNo, String branch, int semester, int no_courses, double cgpa) {
		super(name);
		this.rollNo = rollNo;
		this.branch = branch;
		this.semester = semester;
		this.no_courses = no_courses;
		this.cgpa = cgpa;
	}
	// Getters

	public String getRollNo() {return rollNo;}

	public int getSemester() {return semester;}

	public int getNoCourses(){return this.no_courses;}

	public String getBranch() {
		return branch;
	}
	// Setters

	public void setSemester(int semester) { this.semester = semester; }

	public void setRollNo(String rollNo) { this.rollNo = rollNo; }

	public void setBranch(String branch) { this.branch = branch; }

	public void setNo_courses(int no_courses) { this.no_courses = no_courses; }

	public void setCgpa(double cgpa) { this.cgpa = cgpa; }
	// Utility Methods

	public String display() {
		return super.display()
				+ "\n5. Roll No    : " + rollNo
				+ "\n6. Branch     : " + branch
				+ "\n7. Semester   : " + semester
				+ "\n8. CGPA       : " + cgpa;
	}

	public void readData() {
		System.out.print("\nEnter  Name : "); this.name  = University.getStringFromInput(false);
		System.out.print("\nEnter  Roll No : "); this.rollNo = University.getStringFromInput(false);
		System.out.print("\nEnter  Semester : "); this.semester = University.getIntegerFromInput();
		System.out.print("\nEnter  No of Courses : "); this.no_courses = University.getIntegerFromInput();
		System.out.print("\nEnter  DOB : "); this.setDOB(University.getStringFromInput(true));
		System.out.print("\nEnter  Mobile : "); this.mobile = University.getLongFromInput();
		System.out.print("\nEnter  Email Id : "); this.emailId= scanner.next();
		System.out.print("\nEnter  Branch : "); this.branch = University.getStringFromInput(false);
	}

	@Override
	public String toString() {
		String paddedName = String.format("%-20s", name);
		String paddedRollNo = String.format("%-8s", rollNo);
		String paddedBranch = String.format("%-11s", branch);
		String paddedSemester = String.format("%-3s", semester);
		return paddedName + paddedRollNo + paddedBranch + paddedSemester;
	}

	@Override
	public int compareTo(Student o) {
        return this.name.compareTo(o.name);
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Student student = (Student) o;
		return Objects.equals(this.name, student.name); // && semester == student.semester && Objects.equals(name, student.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(rollNo); //, semester, name);
	}
}