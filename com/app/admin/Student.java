package app.admin;
import app.*;

import java.util.Objects;


public class  Student extends Person
{
	protected int rollNo;
	protected String branch;
	private int semester;
	public int no_courses;
	protected double cgpa;

	public Student() {
		rollNo =0;
		branch="";
		semester=0;
		no_courses=0;
		cgpa=0;
	}

	// Getters

	public int getRollNo() {return rollNo;}

	public int getSemester() {return semester;}

	public int getNoCourses(){return this.no_courses;}

	// Setters

	public void setSemester(int semester) {
		this.semester = semester;
	}

	// Utility Methods

	public String display(){return super.display()+"\n5. Roll No: "+ rollNo +"\n6. Branch : "+branch+"\n7. Semester : "+semester+"\n8. CGPA : "+cgpa;}

	public void readData() {
		System.out.print("\nEnter  Name : ");this.name  = scanner.next();
		System.out.print("\nEnter  Roll No : ");this.rollNo = University.getIntegerFromInput();
		System.out.print("\nEnter  Semester : "); this.semester = University.getIntegerFromInput();
		System.out.print("\nEnter  No of Courses : "); this.no_courses = University.getIntegerFromInput();
		System.out.print("\nEnter  DOB : ");this.setDOB(scanner.next());
		System.out.print("\nEnter  Mobile : ");this.mobile = University.getLongFromInput();
		System.out.print("\nEnter  Email Id : ");this.emailId= scanner.next();
		System.out.print("\nEnter  Branch : ");this.branch = scanner.next();
		System.out.print("\nEnter  CGPA : ");this.cgpa = University.getDoubleFromInput();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Student student = (Student) o;
		return rollNo == student.rollNo; // && semester == student.semester && Objects.equals(name, student.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(rollNo); //, semester, name);
	}
}