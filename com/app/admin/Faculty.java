package app.admin;

import app.University;

import java.util.Objects;

public class Faculty extends Person
{
	private String empCode;
	private String department;
	private int experience;
	private int salary;
	private int noCourses;

	protected Faculty() {
		empCode = "";
		department="";
		experience=0;
		salary=0;
		noCourses =0;
	}
	public Faculty(String name, String department, String empCode, int experience, int salary, int noCourses) {
		super(name);
		this.department = department;
		this.empCode = empCode;
		this.experience = experience;
		this.salary = salary;
		this.noCourses = noCourses;
	}
	// Setters and Getters

	public void setDepartment(String department) {
		this.department = department;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public void setExperience(int experience) {
		this.experience = experience;
	}
	public void setNoCourses(int noCourses) {
		this.noCourses = noCourses;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	public String getEmpCode() {
		return this.empCode;
	}
	public int getNoCourses() {
		return this.noCourses;
	}

	public String getDepartment() {
		return department;
	}
	// Utility functions

	public void readData() {
		System.out.print("\nEnter  Name : "); this.name= University.getStringFromInput(false);
		System.out.print("\nEnter  No of Teaching Courses : "); this.noCourses = University.getIntegerFromInput();
		System.out.print("\nEmp code : "); this.empCode = University.getStringFromInput(false);
		System.out.print("\nEnter  DOB : "); this.setDOB(University.getStringFromInput(true));
		System.out.print("\nEnter  Mobile : "); this.mobile = University.getLongFromInput();
		System.out.print("\nEnter  Email Id : "); this.emailId  = University.scanner.next();
		System.out.print("\nEnter  Department : "); this.department  = University.getStringFromInput(false);
		System.out.print("\nEnter  Experience : "); this.experience = University.getIntegerFromInput();
		System.out.print("\nEnter  Salary : "); this.salary = University.getIntegerFromInput();
		System.out.println();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Faculty faculty = (Faculty) o;
		return Objects.equals(empCode, faculty.empCode); // && Objects.equals(department, faculty.department);
	}

	@Override
	public int hashCode() {
		return Objects.hash(empCode); //, department);
	}

	// Display function

	@Override
	public String toString() {
		String paddedName = String.format("%-20s", name);
		String paddedEmpCode = String.format("%-8s", empCode);
		String paddedDep = String.format("%-11s", department);
		return "Code: " + paddedEmpCode + paddedName + paddedDep;
	}

	public String display() {
		return super.display()
				+"\n5. EmpCode    : " + empCode
				+"\n6. Department : " + department
				+"\n7. Experience : "+experience
				+"\n8. Salary     : "+salary;
	}
}
