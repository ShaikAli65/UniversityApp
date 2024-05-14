package app.academics;
import java.util.*;
import java.util.stream.Stream;

import app.*;
import app.admin.*;
import app.faculty.Exam;


public class AcademicsApp implements University {

	static int c_count;

	// Structures to store
	final public static ArrayList<Course> courses = new ArrayList<>();
	final public static HashMap<Faculty,FacultyCourse> facultyCourses = new HashMap<>();
	final public static HashMap<Student,StudentCourse> studentCourses = new HashMap<>();
	final public static HashMap<Course,List<Exam>> allExams = new HashMap<>();

	public AcademicsApp() {
		System.out.println("\n--------------------------------------\n");
		System.out.println("\n\u001B[32m  Welcome to Academics Dept of " + UniversityApp.Name + " \u001B[0m\n");
		System.out.println("\n--------------------------------------\n");
		display();
	}

	@Override
	public String display() {
		loop: while(true)
		{
			printHeader("Home");
			printAcademicsMenu();
			switch(University.getkeyPress())
			{
				case 1 -> addCourse();
				case 2 -> displayCourses();
				case 3 -> displayCourseChoices(1);
				case 4 -> displayCourseChoices(2);
				case 5 -> addExam();
                case 0 -> {break loop;}
                default-> UniversityApp.getError(6);
			}
			 try { UniversityApp.makeClear(); }catch(Exception ignored) {}
		}
        return "";
    }

	// Utility Functions

	private void addCourse() {
		printHeader("Adding Course Details");
		var course = new Course();
		course.readData();
		courses.add(course);
		c_count++;
	}

	public void displayCourses() {
		printHeader("Displaying Course Details");
		if(c_count == 0){UniversityApp.getError(11);return;}
		printCourses();
		UniversityApp.holdNextSlide();
	}

	public void displayCourseChoices(int in){
		if(courses.isEmpty()){UniversityApp.getError(12);return;}
		if(AdminApp.Students.isEmpty() && in == 1){UniversityApp.getError(9);return;}
		if(AdminApp.Faculties.isEmpty() && in == 2){UniversityApp.getError(10);return;}

		if(in == 1){
			loop : while (true){
				printHeader("Visiting Course Details > Student");
				System.out.println("Choose\n ");
				System.out.print("\t\t1. Add\t\t2. Display\t\t3.Delete\t\t(0)<-\n\t:");
				int choice = University.getkeyPress();
				switch(choice)
				{
					case 1 -> addStudentCourses();
					case 2 -> displayStudentCourses();
					case 3 -> deleteStudentCourses();
					case 0 -> {break loop;}
					default -> UniversityApp.getError(6);
				}
			}
		}
		else
		{
			loop : while (true)
			{
				printHeader("Visiting Course Details > Faculty");
				System.out.println("Choose : \n ");
				System.out.print("\t\t1. Add\t\t2. Display\t\t3.Delete\t\t(0)<-\n\t:");
				int choice = University.getkeyPress();
				switch(choice)
				{
					case 1 -> addFacultyCourses();
					case 2 -> displayFacultyCourses();
					case 3 -> deleteFacultyCourses();
					case 0 -> {break loop;}
					default -> UniversityApp.getError(6);
				}
			}
		}
//		UniversityApp.holdNextSlide();
}

	private void addStudentCourses() {
		printHeader("Adding Student Courses");

		if(AdminApp.Students.isEmpty()){UniversityApp.getError(9);return;}
		Student student = AdminApp.GetStudentChoice();
		if (student == null) return;

		StudentCourse studentCourse = new StudentCourse(student);
		if(courses.isEmpty()){UniversityApp.getError(12);return;}

		printHeader("Adding Student Courses");
		System.out.println("\nStudent :"+student.getName());
		System.out.println("\nThese are the courses available in the Semester("+student.getSemester()+")\n");

		List<Course> matched_courses = courses.stream().filter(course -> course.getSemester() == student.getSemester()).distinct().toList();
		if(matched_courses.isEmpty()){UniversityApp.getError(18);return;}

		int choice = 1;
		for(Course co : matched_courses)
		{
			System.out.println((choice) + ". " + co.toString() + "\n");
			choice++;
		}

        System.out.println("Select the required courses (Enter the respective index):");

		for(int i = 0; i < Math.min(matched_courses.size(), student.getNoCourses()); i++)
		{
			try {
				int k = University.getIntegerFromInput() - 1;
				var selected_course = matched_courses.get(k);
				studentCourse.add(selected_course);
			}
			catch (IndexOutOfBoundsException e) {
				UniversityApp.getError(1);
				i--;
			}
		}
		studentCourses.put(student, studentCourse);
}

	private void addFacultyCourses() {
		printHeader("Adding Faculty Courses");
		if(AdminApp.Faculties.isEmpty()){UniversityApp.getError(10);return;}

		var faculty = AdminApp.GetFacultyChoice();
		if(faculty == null){return;}

		var facultyCourse = new FacultyCourse(faculty);
		if(courses.isEmpty()){System.out.println("No courses Available");return;}

		printHeader("Adding Faculty Courses");
		System.out.println("\nFaculty :" + faculty.getName());
		printCourses();
		System.out.println("Select the required courses : (Enter the respective index)");

		for(int i = 0; i < Math.min(courses.size(), faculty.getNoCourses()); i++){
			while(true){
				try {
					int k = University.getIntegerFromInput() - 1;
					var selected_course = courses.get(k);
					facultyCourse.add(selected_course);
					break;
				}
				catch (Exception e) {
					UniversityApp.getError(1);
					i--;
				}
			}
		}
		facultyCourses.put(faculty, facultyCourse);
	}

    public void displayStudentCourses() {
		printHeader("Displaying Student - Course Details");
		if(AdminApp.Students.isEmpty()){
			UniversityApp.getError(9);
			return;
		}
		Student student = AdminApp.GetStudentChoice();
        if (student == null) {return;}

		if(studentCourses.get(student) == null){UniversityApp.getError(3);return;}

		System.out.print(studentCourses.get(student).toString());
		UniversityApp.holdNextSlide();
	}

	public void displayFacultyCourses() {
		printHeader("Displaying Faculty - Course Details");
		if(AdminApp.Faculties.isEmpty()){UniversityApp.getError(10);return;}

		Faculty fac = AdminApp.GetFacultyChoice();
		if (fac == null) {return;}

		if(facultyCourses.get(fac) == null){UniversityApp.getError(4);return;}
		System.out.print(facultyCourses.get(fac).toString());

		UniversityApp.holdNextSlide();
	}

	private void deleteStudentCourses() {
		printHeader("Deleting Student Courses");

		var student = AdminApp.GetStudentChoice();
		if(student == null){return;}
		var student_course = studentCourses.get(student);
		if(student_course == null)
		{
			UniversityApp.getError(3);
			return;
		}
		var course = student_course.getCourseChoice();
		if(course == null){return;}
		student_course.remove(course);
	}

	private void deleteFacultyCourses() {
		printHeader("Deleting Faculty Courses");
		var faculty = AdminApp.GetFacultyChoice();
		if (faculty == null) return;
		var faculty_course = facultyCourses.get(faculty);
		if(faculty_course == null)
		{
			UniversityApp.getError(4);
			return;
		}
		var course = faculty_course.getCourseChoice();
		if(course == null){return;}
		faculty_course.remove(course);
	}

	private void addExam() {
		printHeader("Adding Exam");
		if(courses.isEmpty()){UniversityApp.getError(12);return;}
		var course = getCourseChoice();
		if(course == null){return;}
		var date = new app.Date();
		date.newDate();
		var exam = new Exam(date, course);
		if(allExams.containsKey(course)){
			allExams.get(course).add(exam);
		}
		else{
			var exams = new ArrayList<Exam>();
			exams.add(exam);
			allExams.put(course, exams);
		}
	}

	// Getters

	public static FacultyCourse getFacultyCourse(Faculty faculty) {
		return facultyCourses.get(faculty);
	}

	public static StudentCourse getStudentCourse(Student student) {
		return studentCourses.get(student);
	}

	public static Stream<Student> getStudentsWithCourse(Course course) {
		return AdminApp.Students.stream()
        .filter(studentCourses::containsKey)
        .filter(student -> studentCourses.get(student).contains(course));
	}

	public static Stream<Faculty> getFacultyForCourse(Course course) {
		return AdminApp.Faculties.stream()
				.filter(facultyCourses::containsKey)
				.filter(faculty -> facultyCourses.get(faculty).contains(course));
	}

	public static List<Exam> getExams(Course course) {
		return allExams.get(course);
	}

	// Printers

	public void printCourses() {
		if(courses.isEmpty()){UniversityApp.getError(12);return;}
		System.out.println("These are the courses available :\n");

		StringBuilder indexPadding = new StringBuilder();
        indexPadding.append(" ".repeat(String.valueOf(courses.size() - 1).length()));

		for(int i = 1; i <= courses.size(); i++) {
			String index = indexPadding.substring(0, indexPadding.length() - String.valueOf(i).length()) + i;
			System.out.println(index + ". " + courses.get(i - 1).toString());
		}
		System.out.println();
	}

	public Course getCourseChoice() {
		if(courses.isEmpty()){UniversityApp.getError(12);return null;}
		printCourses();
		System.out.print("Enter the course index to select : ");
		int choice = University.getIntegerFromInput();
		if(choice < 1 || choice > courses.size()){UniversityApp.getError(6);return null;}
		return courses.get(choice - 1);
	}

	private static void printAcademicsMenu() {
		System.out.print("""
                    Choose :\s
                    \t\t 1. Add Course Details
                    \t\t 2. Display Course Details
                    \t\t 3. Student-Course Details
                    \t\t 4. Faculty-Course Details
                    \t\t 5. Conduct Exam
                    \t\t 0. Return to University App
                    \t:""");

	}

	public void printHeader(String out) {
		UniversityApp.makeClear();
		System.out.println("--------------Academics Panel--------------");
		System.out.println("IN: " + out);
		System.out.println("-------------------------------------------\n");
	}

}