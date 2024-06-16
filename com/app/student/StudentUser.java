package app.student;

import app.Choices;
import app.University;
import app.UniversityApp;
import app.academics.Course;
import app.academics.StudentCourse;
import app.admin.Student;
import db.AttendanceDB;
import db.CourseDB;
import db.SessionDB;

import java.io.Serializable;
import java.util.Arrays;


public class StudentUser implements University, Serializable
{
    final private char[] passwordArray;
    final private Student student;

    public StudentUser(Student student, char[] passwordArray) {
        this.student = student;
        this.passwordArray = passwordArray;
    }

    public boolean authenticate() {
        printHeader("Authentication");
        if (this.passwordArray == null) {UniversityApp.getError(2);return false;}
        char[] password = University.getPasswordFromInput();
        return Arrays.equals(passwordArray, password);
    }

    public void seeAttendance() {
        printHeader("Attendance");
        System.out.println(AttendanceDB.getEntry(student));
        System.out.print("\nEnter course code to get Detailed view or . to return: ");
        String courseCode = University.scanner.next().toUpperCase();
        if(courseCode.contains(".")) return;

        printHeader("Attendance > Detailed View");
        var courses = retrieveCourses();
        var choosenCourse = courses.getCourse(courseCode);
        System.out.println(getCourseAttendance(choosenCourse));
        UniversityApp.holdNextSlide();
    }
    public void seeExam() {
        var courses = retrieveCourses();
        Course course = Choices.getCourse(courses, "Exams View > Courses");
        if (course == null) {
            return;
        }
        try {
            var exam = Choices.getExam(course, "Exams > Detailed View");
            if (exam == null) {return;}
            printHeader("Exams");
            exam.printExam();
        } catch (Exception e) {
            UniversityApp.getError(17);
            return;
        }
        UniversityApp.holdNextSlide();
    }
    public void generateReport() {
        printHeader("Report");
        UniversityApp.holdNextSlide();
    }
    public void generateFullReport() {
        printHeader("Full Report");
        UniversityApp.holdNextSlide();
    }

    private StudentCourse retrieveCourses() {return CourseDB.getCourses(this.student);}
    private String getCourseAttendance(Course course) {
        StringBuilder resultString = new StringBuilder();
        resultString.append("\n\n");
        var sessions = SessionDB.getSessions(course);

        sessions.sorted().forEach(session ->
                resultString.append("[")
                            .append(session.getTime())
                            .append("]\t: ")
                            .append(session.getAttendance(this.student) ? "Present" : "Absent")
                            .append("\n"));
        resultString.append("\n\nCOURSE :").append(course).append('\n');
        return resultString.toString();
    }

    @Override
    public String display() {return "";}
    @Override
    public void printHeader(String out) {
        UniversityApp.makeClear();
        System.out.println("----------------------Student App----------------------");
        System.out.println("IN :" + this.student.getName() + " > " + out);
        System.out.println("-------------------------------------------------------\n");
    }
}
