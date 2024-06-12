package app.student;

import app.University;
import app.UniversityApp;
import app.academics.Course;
import app.academics.StudentCourse;
import app.admin.Student;
import db.AttendanceDB;
import db.CourseDB;
import db.ExamDB;
import db.SessionDB;

import java.io.Serializable;
import java.util.Arrays;


public class StudentUser implements University, Serializable
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

    private StudentCourse retrieveCourses() {
        return CourseDB.getCourses(this.student);
    }

    public void seeAttendance() {
        printHeader("Attendance");
        var studentEntry = AttendanceDB.getEntry(student);
        String attendance = studentEntry.getAttendance();
        System.out.println(attendance);

        System.out.print("\nEnter course code to get Detailed view or 0 to return: ");
        String courseCode = University.scanner.next().toUpperCase();
        if(courseCode.equals("0")) return;

        printHeader("Attendance > Detailed View");
        var courses = retrieveCourses();
        var choosenCourse = courses.getCourse(courseCode);
        System.out.println(getCourseAttendance(choosenCourse));
        UniversityApp.holdNextSlide();
    }

    private String getCourseAttendance(Course course) {
        StringBuilder resultString = new StringBuilder("\nAttendance for ");
        resultString.append(course).append("\n\n");
        var sessions = SessionDB.getSessions(course);

        sessions.forEach(session ->
                resultString.append("Session Time: ")
                            .append(session.getTime())
                            .append("\t: ")
                            .append(session.getAttendance(this.student) ? "Present" : "Absent")
                            .append("\n"));
        return resultString.toString();
    }

    public void seeExam() {
        printHeader("Exams");
        var courses = retrieveCourses();
        Course course = courses.getCourseChoice();
        if (course == null) {
            return;
        }
        var exams = ExamDB.getExams(course);
        if (exams.isEmpty()) {
            UniversityApp.getError(11);
            return;
        }

        printHeader("Exams > " + course);
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

    public void generateReport() {
        printHeader("Report");
        UniversityApp.holdNextSlide();
    }

    public void generateFullReport() {
        printHeader("Full Report");
        UniversityApp.holdNextSlide();
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
