package app.student;

import app.academics.AcademicsApp;
import app.academics.StudentCourse;
import app.admin.Course;
import app.admin.Student;
import app.*;
import app.faculty.FacultyApp;
import app.faculty.FacultyUser;

import java.util.Arrays;


public class StudentUser implements University
{
    final private char[] passwordArray;
    final private Student student;

    public StudentUser(Student student) {
        this.student = student;
        passwordArray = University.getPasswordFromInput();
    }

    public boolean authenticate() {
        printHeader("Authentication");
        if (this.passwordArray == null) {UniversityApp.getError(2);return false;}
        char[] password = University.getPasswordFromInput();
        return Arrays.equals(passwordArray, password);
    }

    // Utility Functions

    public void seeAttendance(StudentCourse studentCourse) {
        printHeader("Attendance");

        studentCourse.getCourses().forEach(course -> System.out.println(course.toString() + "\tAttendance: " + studentCourse.getAttendance(course)));

        System.out.print("\nEnter course code to get Detailed view or 0 to return: ");
        String courseCode = University.scanner.next().toUpperCase();
        
        if(courseCode.equals("0")) return;
        printHeader("Attendance > Detailed View");
        
        studentCourse.getCourses().parallel()
                .filter(course -> course.getCode().equals(courseCode))
                .distinct()
                .forEach(course -> System.out.println(getCourseAttendance(course)));
        
        UniversityApp.holdNextSlide();
    }

    public void seeExam(StudentCourse studentCourse) {
        printHeader("Exams");
        Course course = studentCourse.getCourseChoice();
        var exams = AcademicsApp.getExams(course);
        if (exams == null) {
            UniversityApp.getError(11);
            return;
        }
        printHeader("Exams > " + course.toString());
        int i = 1;
        for (var exam : exams) {
            System.out.println(i + ". " + exam.toString() + " Score: " + exam.getMarks(this.student));
            i++;
        }
        System.out.print("\nEnter the exam number you want to select or zero to exit :");
        int choice = University.getIntegerFromInput() - 1;
        if (choice == -1) return;
        printHeader("Exam > Marks");
        try {
            var exam = exams.get(choice);
            exam.printExam();
        } catch (Exception e) {
            UniversityApp.getError(17);
            return;
        }
        UniversityApp.holdNextSlide();
    }

    public void generateReport(StudentCourse studentCourse) {
        printHeader("Report");
        UniversityApp.holdNextSlide();
    }

    public void generateFullReport() {
        printHeader("Full Report");
        UniversityApp.holdNextSlide();
    }

    // Getters

    private String getCourseAttendance(Course course) {
        StringBuilder result = new StringBuilder("\nAttendance for " + course.toString() + "\n\n");

        AcademicsApp.getFacultyForCourse(course).forEach(faculty -> {
            FacultyUser facultyUser = FacultyApp.getFacultyUser(faculty);
            if (facultyUser == null) return;

            facultyUser.getSessionStream().filter(session -> session.isOfCourse(course))
                    .forEach(session -> result.append("Session Time: ").append(session.getSessionTime())
                    .append("\t: ").append(session.getAttendance(this.student) ? "Present" : "Absent").append("\n"));
            });

        return result.toString();
    }

    // Display Functions

    @Override
    public String display() {
        return "";
    }

    @Override
    public void printHeader(String out) {
        UniversityApp.makeClear();
        System.out.println("----------------------Student App----------------------");
        System.out.println("IN :" + out);
        System.out.println("-------------------------------------------------------\n");
    }
}
