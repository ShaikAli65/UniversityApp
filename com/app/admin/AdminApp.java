package app.admin;

import app.*;
import app.faculty.FacultyApp;
import app.student.StudentApp;

import java.util.ArrayList;


public class AdminApp implements University {

	private static int stu_count;
	private static int fac_count;

	// Structures to store
	final public static ArrayList<Student> Students = new ArrayList<>();
	final public static ArrayList<Faculty> Faculties = new ArrayList<>();

	public AdminApp() {
		System.out.println("\n----------------------------------\n");
		System.out.println("\n\u001B[32m Welcome to Admin Dept of " + UniversityApp.Name + " \u001B[0m\n");
		System.out.println("\n----------------------------------\n");
		stu_count = 0;
		fac_count = 0;
		display();
	}

	@Override
	public String display() {
		loop: while(true)
		{
			showAdminMenu();
			switch(University.getkeyPress())
			{
				case 1  -> addStudent();
				case 2  -> addFaculty();
 				case 3  -> displayStudent();
			    case 4  -> displayFaculty();
				case 5  -> updateStudent();
				case 6  -> updateFaculty();
				case 7  -> deleteStudent();
				case 8  -> deleteFaculty();
				case 0  -> {break loop;}
				default -> UniversityApp.getError(6);
			}
			UniversityApp.makeClear();
		}
		storeData();
		return "";
}

	private void storeData() {

	}

	// Utility Functions

	private void addFaculty() {
		printHeader("Entering the Faculty Details");
		var faculty = new Faculty();
		faculty.readData();
		Faculties.add(faculty);
		FacultyApp.AddNewFaculty(faculty);
		fac_count += 1;
	}

	private void addStudent() {
		printHeader("Entering the Student Details");
		var student = new Student();
		student.readData();
		Students.add(student);
		StudentApp.AddNewStudent(student);
		stu_count += 1;
	}

	void updateStudent()   {
		printHeader("Updating Student Details (not recommended)");
		var student = GetStudentChoice();
		if (student == null){return;}
		printHeader("Updating Student Details > " + student.getName());
		System.out.println(student.display());
		System.out.print("Choose the serial number of category to Update : ");
		switch(University.getIntegerFromInput())
		{
			case 1: System.out.print("Enter the new Name :");
			student.name = scanner.next();
			break;
			case 2: System.out.print("Enter the new DOB :");
			student.setDOB(scanner.next());
			break;
			case 3: System.out.print("Enter the new Email ID :");
			student.emailId = scanner.next();
			break;
			case 4: System.out.print("Enter the new Mobile :");
			student.mobile = University.getLongFromInput();
			break;
			case 5: System.out.print("Enter the new Roll No :");
			student.rollNo = University.getIntegerFromInput();
			break;
			case 6: System.out.print("Enter the new Branch :");
			student.branch = scanner.next();
			break;
			case 7: System.out.print("Enter the new Semester :");
			student.setSemester(University.getIntegerFromInput());
			break;
			case 8: System.out.print("Enter the new CGPA :");
			student.cgpa = University.getIntegerFromInput();
			break;
		}
		System.out.println("Updated Data :");
		System.out.println(student.display());
		UniversityApp.holdNextSlide();
	}

	void updateFaculty()   {
		printHeader("Updating Faculty Details (not recommended)");
		var faculty = GetFacultyChoice();
		if (faculty == null){return;}
		printHeader("Updating Faculty Details > " + faculty.getName());
		System.out.println(faculty.display());
		System.out.print("Choose the serial number of category to Update:");
		switch(University.getIntegerFromInput())
		{
			case 1: System.out.print("Enter the new Name: ");
			faculty.name = scanner.next();
			break;
			case 2: System.out.print("Enter the new DOB: ");
			faculty.setDOB(scanner.next());
			break;
			case 3: System.out.print("Enter the new Email ID: ");
			faculty.emailId = scanner.next();
			break;
			case 4: System.out.print("Enter the new Mobile: ");
			faculty.mobile = University.getLongFromInput();
			break;
			case 5: System.out.print("Enter the new Employee code: ");
			faculty.setEmpCode(University.getIntegerFromInput());
			break;
			case 6: System.out.print("Enter the new Department: ");
			faculty.setDepartment(scanner.next());
			break;
			case 7: System.out.print("Enter the new Experience: ");
			faculty.setExperience(University.getIntegerFromInput());
			break;
			case 8: System.out.print("Enter the new Salary: ");
			faculty.setSalary(University.getIntegerFromInput());
			break;
			case 9: System.out.print("Enter new Number of Teaching Courses: ");
			faculty.setNoCourses(University.getIntegerFromInput());
			break;
		}
		System.out.println("Updated Data :");
		System.out.println(faculty.display());
		UniversityApp.holdNextSlide();
	}

	void deleteStudent()  {
		printHeader("Deleting Student Details");
		if(stu_count == 0){UniversityApp.getError(9);return;}
		var student = GetStudentChoice();
		if(student == null){return;}
		AdminApp.Students.remove(student);
		StudentApp.RemoveStudent(student);
		stu_count -= 1;
	}

	void deleteFaculty()  {
		printHeader("Deleting Faculty Details");
		if(fac_count == 0){UniversityApp.getError(10);return;}
		var faculty = GetFacultyChoice();
		if (faculty == null) {return;}
		AdminApp.Faculties.remove(faculty);
		FacultyApp.RemoveFaculty(faculty);
		fac_count -= 1;
	}

	// Getters

	public static Student GetStudentChoice() {

		System.out.println("Choose one Student");
		ShowSubStudentList();
		System.out.print("\n\t : ");

		int id = University.getIntegerFromInput() - 1;

		try {
			return Students.get(id);
		} catch (IndexOutOfBoundsException ignored) {
			UniversityApp.getError(7);
			return null;
		}
	}

	public static Faculty GetFacultyChoice() {
		System.out.println("Choose one Faculty");
		ShowSubFacultyList();
		System.out.print("\n\tYour Choice : ");

		int id = University.getIntegerFromInput() - 1;

		try {
			return Faculties.get(id);
		} catch (IndexOutOfBoundsException ignored) {
			UniversityApp.getError(8);
			return null;
		}
	}

	// Display Functions

	public static void ShowSubFacultyList() {
		if(Faculties.isEmpty())
		{
			UniversityApp.getError(10);
			return;
		}
		int i = 1;
		for(var facutly: Faculties)
		{
			System.out.println("\n\t\t" + i + ". " + facutly.getName());
			i++;
		}
	}

	public static void ShowSubStudentList() {
		if(Students.isEmpty())
		{
			UniversityApp.getError(10);
			return;
		}
		int i = 1;
		for(var student : Students){
			System.out.print("\n\t\t" + i + ". " +  student.getName());
			i++;
		}
	}

	void displayStudent() {
		printHeader("Displaying the Student Details");
		if (stu_count == 0) {UniversityApp.getError(9);return;}

		var student = GetStudentChoice();
		if (student == null) {return;}

		System.out.println(student.display());
		UniversityApp.holdNextSlide();
	}

	void displayFaculty() {
		printHeader("Displaying the Faculty Details");
		if(fac_count == 0){UniversityApp.getError(10);return;}

		var fac = GetFacultyChoice();
		if(fac == null){return;}

		System.out.println(fac.display());
		UniversityApp.holdNextSlide();
	}

	private static void showAdminMenu() {
		System.out.print("""
                Choose :\
                \s
                \t\t 1. Add Student Details
                \t\t 2. Add Faculty Details
                \t\t 3. Display Student Details
                \t\t 4. Display Faculty Details
                \t\t 5. Update Student Details
                \t\t 6. Update Faculty Details
                \t\t 7. Remove a Student
                \t\t 8. Remove a Faculty
                \t\t 0. Return to University App
                \t:""");
	}

	@Override
	public void printHeader(String s) {
		UniversityApp.makeClear();
		System.out.println("------------------Admin panel------------------");
		System.out.println("IN : " + s);
		System.out.println("-----------------------------------------------\n");
	}
}
