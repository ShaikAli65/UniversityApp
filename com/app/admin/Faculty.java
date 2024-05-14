package app.admin;

import app.*;

import java.util.Objects;

public class Faculty extends Person
{
	private int empCode;
	private String department;
	private int experience;
	private int salary;
	private int noCourses;

	protected Faculty() {
		empCode =0;
		department="";
		experience=0;
		salary=0;
		noCourses =0;
	}

	// Setters and Getters

	public void setDepartment(String department) {
		this.department = department;
	}
	public void setEmpCode(int empCode) {
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

	public int getNoCourses() {
		return this.noCourses;
	}

	// Utility functions

	public void readData() {
		System.out.print("\nEnter  Name : ");this.name= scanner.next();
		System.out.print("\nEnter  No of Teaching Courses : ");this.noCourses = University.getIntegerFromInput();
		System.out.print("\nEmp code : ");this.empCode = University.getIntegerFromInput();
		System.out.print("\nEnter  DOB : ");this.setDOB(scanner.next());
		System.out.print("\nEnter  Mobile : "); this.mobile = University.getLongFromInput();
		System.out.print("\nEnter  Email Id : ");this.emailId  = scanner.next();
		System.out.print("\nEnter  Department : ");this.department  = scanner.next();
		System.out.print("\nEnter  Experience : ");this.experience = University.getIntegerFromInput();
		System.out.print("\nEnter  Salary : ");this.salary = University.getIntegerFromInput();
		System.out.println();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Faculty faculty = (Faculty) o;
		return empCode == faculty.empCode; // && Objects.equals(department, faculty.department);
	}

	@Override
	public int hashCode() {
		return Objects.hash(empCode); //, department);
	}

	// Display function

	public String display() {
		return super.display()+"\n5. Emp Code: "+ empCode +"\n6. Department : "+department+"\n7. Experience : "+experience+"\n8. Salary : "+salary;
	}
}
