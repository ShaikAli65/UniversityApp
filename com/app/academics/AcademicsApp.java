package app.academics;

import app.Choices;
import app.University;
import app.UniversityApp;
import app.admin.Faculty;
import app.admin.Student;
import db.CourseDB;
import db.ExamDB;
import db.FacultyDB;
import db.StudentDB;

import java.util.List;

public class AcademicsApp implements University {


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
			switch(University.getKeyPress())
			{
				case 1 -> addCourse();
				case 2 -> displayCourses();
				case 3 -> displayStudentCourseContext();
				case 4 -> displayFacultyCourseContext();
				case 5 -> addExam();
				case 6 -> displayExams();
                case 0 -> {break loop;}
                default-> UniversityApp.getError(6);
			}
			 try { UniversityApp.makeClear(); }catch(Exception ignored) {}
		}
        return "";
    }


	private void displayStudentCourseContext() {
		loop: while(true)
		{
			printHeader("Student-Course Context");
			System.out.println("Choose :\n");
			System.out.print("\t\t1. Add\t\t2. Display\t\t3. Delete\t\t(0)<-\n\t:");
			switch(University.getKeyPress())
			{
				case 1 -> addStudentCourses();
				case 2 -> displayStudentCourses();
				case 3 -> deleteStudentCourses();
				case 0 -> {break loop;}
				default -> UniversityApp.getError(6);
			}
		}
	}

	private void displayFacultyCourseContext() {
		loop: while(true)
		{
			printHeader("Faculty-Course Context");
			System.out.println("Choose :\n");
			System.out.print("\t\t1. Add\t\t2. Display\t\t3. Delete\t\t(0)<-\n\t:");
			switch(University.getKeyPress())
			{
				case 1 -> addFacultyCourses();
				case 2 -> displayFacultyCourses();
				case 3 -> deleteFacultyCourses();
				case 0 -> {break loop;}
				default -> UniversityApp.getError(6);
			}
		}
	}
	// Utility Functions

	private void addCourse() {
		printHeader("Adding Course Details");
		var course = new Course();
		course.readData();
		CourseDB.addCourse(course);
	}

	public void displayCourses() {
		printHeader("Displaying Course Details");
		if(CourseDB.isEmpty()){UniversityApp.getError(11);return;}
		printCourses();
		UniversityApp.holdNextSlide();
	}

	private void addStudentCourses() {

		if(StudentDB.isEmpty()){UniversityApp.getError(9); return;}
		Student student = Choices.getStudent("Adding Student Courses");
		if (student == null) return;
		var studentCourse = CourseDB.getCourses(student);
		if (studentCourse == null) {
			studentCourse = new StudentCourse(student);
		}

		if(CourseDB.isEmpty()){UniversityApp.getError(12); return;}

//		printHeader("Adding Student Courses");
		System.out.println("\nStudent :"+student.getName());
		System.out.println("\nThese are the courses available in the Semester "+student.getSemester()+"\n");

		List<Course> matched_courses = CourseDB.getCourses(student.getSemester()).toList();
		if(matched_courses.isEmpty()){UniversityApp.getError(18); return;}

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
		CourseDB.updateCourse(student, studentCourse);
	}

	private void addFacultyCourses() {
		if(FacultyDB.isEmpty()){UniversityApp.getError(10);return;}

		var faculty = Choices.getFaculty("Adding Faculty Courses");
		if(faculty == null){return;}

		var facultyCourse = new FacultyCourse(faculty);
		if(CourseDB.isEmpty()){System.out.println("No courses Available");return;}

		printHeader("Adding Faculty Courses");
		System.out.println("\nFaculty :" + faculty.getName());
		var courses = printCourses();
		System.out.println("Select the required courses : (Use Spaces for multiple selection)");

		for(int i = 0; i < Math.min(CourseDB.noOfCourses(), faculty.getNoCourses()); i++){
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
		CourseDB.updateCourse(faculty, facultyCourse);
	}

    public void displayStudentCourses() {
		if(StudentDB.isEmpty()){
			UniversityApp.getError(9);
			return;
		}
		while (true){
			Student student = Choices.getStudent("Displaying Student-Course Details");
			if (student == null) {return;}

			if(CourseDB.getCourses(student) == null){UniversityApp.getError(3);continue;}
			printHeader("Displaying Student-Course Details > " + student.getName());
			System.out.print(CourseDB.getCourses(student).toString());
			UniversityApp.holdNextSlide();
		}
	}

	public void displayFacultyCourses() {
		if(FacultyDB.isEmpty()){UniversityApp.getError(10);return;}

		Faculty fac = Choices.getFaculty("Displaying Faculty - Course Details");
		if (fac == null) {return;}
		printHeader("Displaying Faculty - Course Details");

		if(CourseDB.getCourses(fac) == null){UniversityApp.getError(4);return;}
		printHeader("Displaying Faculty-Course Details > " + fac.getName());
		System.out.print(CourseDB.getCourses(fac).toString());
		UniversityApp.holdNextSlide();
	}

	private void deleteStudentCourses() {

		var student = Choices.getStudent("Deleting Student Courses");
		if(student == null){return;}
		var studentCourse = CourseDB.getCourses(student);
		if(studentCourse == null)
		{
			UniversityApp.getError(3);
			return;
		}
		var course = Choices.getCourse(studentCourse, "Deleting Student Courses");
		if(course == null){return;}
		studentCourse.remove(course);
		CourseDB.updateCourse(student, studentCourse);
	}

	private void deleteFacultyCourses() {
		var faculty = Choices.getFaculty("Deleting Faculty Courses");
		if (faculty == null) return;
		var facultyCourses = CourseDB.getCourses(faculty);
//		printHeader("Deleting Faculty Courses");
		if(facultyCourses == null)
		{
			UniversityApp.getError(4);
			return;
		}
		var course = Choices.getCourse(facultyCourses, "Deleting Faculty Courses");
		if(course == null){return;}
		facultyCourses.remove(course);
		CourseDB.updateCourse(faculty, facultyCourses);
	}

	private void addExam() {
		if(CourseDB.isEmpty()){UniversityApp.getError(12);return;}
		var course = Choices.getCourse("Adding Exam");
		if(course == null){return;}
		printHeader("Adding Exam >" + course.getCode());
		var date = new app.Date();
		date.getNewDateFromStdIn();
		var exam = new Exam(date, course);
		ExamDB.add(exam);
	}

	private void displayExams() {
		if(ExamDB.isEmpty()){UniversityApp.getError(14);return;}
		var exam = Choices.getExam("Displaying Exams");
		if(exam == null){return;}
		printHeader("Displaying Exams > " + exam);
		exam.printExam();
		UniversityApp.holdNextSlide();
	}

	// Printers

	public List<Course> printCourses() {
		if(CourseDB.isEmpty()){UniversityApp.getError(12);return null;}
		System.out.println("These are the courses available :\n");
		List<Course> courses = CourseDB.getCourses().toList();
		int maxIndexDigits = String.valueOf(courses.size()).length();
	
		for (int i = 0; i < courses.size(); i++) {
			String index = String.format("%" + maxIndexDigits + "s", i + 1);
			System.out.println(index + ". " + courses.get(i).toString());
		}
		System.out.println();
	
		return courses;
	}

//	public Course getCourseChoice() {
//		if(CourseDB.isEmpty()){UniversityApp.getError(12);return null;}
//		var courses = printCourses();
//		System.out.print("Enter the course index to select : ");
//		int choice = University.getIntegerFromInput();
//		if(choice < 1 || choice > courses.size()){UniversityApp.getError(6);return null;}
//		return courses.get(choice - 1);
//	}

	private static void printAcademicsMenu() {
		System.out.print("""
                    Choose :\s
                    \t\t 1. Add Course Details
                    \t\t 2. Display Course Details
                    \t\t 3. Student-Course Details
                    \t\t 4. Faculty-Course Details
                    \t\t 5. Conduct Exam
                    \t\t 6. Display Exams
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