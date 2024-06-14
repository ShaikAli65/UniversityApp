package app.admin;

import app.Choices;
import app.University;
import app.UniversityApp;
import app.faculty.FacultyApp;
import app.student.StudentApp;
import db.*;


public class AdminApp implements University {

	public AdminApp() {
		System.out.println("\n----------------------------------\n");
		System.out.println("\n\u001B[32m Welcome to Admin Dept of " + UniversityApp.Name + " \u001B[0m\n");
		System.out.println("\n----------------------------------\n");
		display();
	}

	@Override
	public String display() {
		loop: while(true)
		{
			showAdminMenu();
			switch(University.getKeyPress())
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
		return "";
	}

	private void addFaculty() {
		printHeader("Entering the Faculty Details");
		var faculty = new Faculty();
		faculty.readData();
		FacultyDB.add(faculty);
		FacultyApp.AddNewFaculty(faculty);
	}

	private void addStudent() {
		printHeader("Entering the Student Details");
		var student = new Student();
		student.readData();
		StudentDB.add(student);
		StudentApp.addNewStudent(student);
	}

	void updateStudent()   {
		var student = Choices.getStudent("Updating Student Details (not recommended)");
		if (student == null){return;}
		loop : while (true) {
		printHeader("Updating Student Details > " + student.getName());
			System.out.println(student.display());
			System.out.print("Choose the serial number of category to Update or zero to return: ");
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
				case 5: System.out.print("Not allowed to change Roll No.");
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
				case 0: break loop;
				}
		}
		StudentDB.update(student);
	}

	void updateFaculty()   {
		var faculty = Choices.getFaculty("Updating Faculty Details (not recommended)");
		if (faculty == null){return;}
		loop: while (true) {
			printHeader("Updating Faculty Details > " + faculty.getName());
			System.out.println(faculty.display());
			System.out.print("Choose the serial number of category to Update or zero to return:");
			switch (University.getIntegerFromInput()) {
				case 1:
					System.out.print("Enter the new Name: ");
					faculty.name = scanner.next();
					break;
				case 2:
					System.out.print("Enter the new DOB: ");
					faculty.setDOB(scanner.next());
					break;
				case 3:
					System.out.print("Enter the new Email ID: ");
					faculty.emailId = scanner.next();
					break;
				case 4:
					System.out.print("Enter the new Mobile: ");
					faculty.mobile = University.getLongFromInput();
					break;
				case 5:
					System.out.print("Not Allowed to change Employee code: ");
					break;
				case 6:
					System.out.print("Enter the new Department: ");
					faculty.setDepartment(scanner.next());
					break;
				case 7:
					System.out.print("Enter the new Experience: ");
					faculty.setExperience(University.getIntegerFromInput());
					break;
				case 8:
					System.out.print("Enter the new Salary: ");
					faculty.setSalary(University.getIntegerFromInput());
					break;
				case 9:
					System.out.print("Enter new Number of Teaching Courses: ");
					faculty.setNoCourses(University.getIntegerFromInput());
					break;
				case 0: break loop;
			}
		}
		FacultyDB.update(faculty);
	}

	void deleteStudent()  {
		var student = Choices.getStudent("Deleting Student Details");
		if(student == null){return;}
		printHeader("Deleting Student Details");
		StudentDB.remove(student);
		StudentApp.removeStudent(student);
		AttendanceDB.remove(student);
		SessionDB.remove(student);
		ExamDB.remove(student);
	}

	void deleteFaculty()  {
		var faculty = Choices.getFaculty("Deleting Faculty Details");
		if (faculty == null) {return;}
		printHeader("Deleting Faculty Details");
		FacultyDB.remove(faculty);
		FacultyApp.remove(faculty);
	}

	void displayStudent() {
		printHeader("Displaying the Student Details");
		while (true) {
			var student = Choices.getStudent("Displaying the Student Details");
			if (student == null) {return;}
			printHeader("Displaying the Student Details");
			System.out.println(student.display());
			UniversityApp.holdNextSlide();
		}
	}
	void displayFaculty() {
		printHeader("Displaying the Faculty Details");
		while (true) {
			var fac = Choices.getFaculty("Displaying the Faculty Details");
			if (fac == null) {return;}
			printHeader("Displaying the Faculty Details");
			System.out.println(fac.display());
			UniversityApp.holdNextSlide();
		}
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

	public void printHeader(String s) {
		UniversityApp.makeClear();
		System.out.println("------------------Admin panel------------------");
		System.out.println("IN : " + s);
		System.out.println("-----------------------------------------------\n");
	}
}
