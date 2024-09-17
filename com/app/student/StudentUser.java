package app.student;

import app.Choices;
import app.University;
import app.UniversityApp;
import app.academics.Course;
import app.academics.StudentCourses;
import app.admin.Student;
import app.faculty.Session;
import db.AttendanceDB;
import db.CourseDB;
import db.ExamDB;
import db.SessionDB;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

public class StudentUser implements University, Serializable
{
    final private char[] passwordArray;
    final private Student student;

    public StudentUser(Student student, char[] passwordArray) {
        this.student = student;
        this.passwordArray = passwordArray;
    }

    public boolean authenticate() {
        if (this.passwordArray == null) {UniversityApp.getError(2);return false;}
        char[] password = University.getPasswordFromInput();
        return Arrays.equals(passwordArray, password);
    }

    public void seeAttendance() {
        printHeader("Attendance");
        var entry = AttendanceDB.getEntry(student);
        if (entry == null){
            UniversityApp.getError(19);
            return;
        }
        System.out.println(entry);
        System.out.print("\nenter 0 to return 1 to enter detailed view : ");
        int what = University.getIntegerFromInput();
        if (what == 0) {
            return;
        }
        var courses = retrieveCourses();
        Course course = Choices.getCourse(courses, "Attendance View > Courses");
        if (course == null) {
            return;
        }
        printHeader("Attendance > Detailed View");
        printCourseAttendance(course.getCode());
        var s = entry.getAttendance(course);
        System.out.println("TOTAL SESSIONS   : "+s.getConducted());
        System.out.println("ATTENDED         : "+s.getAttended());
        System.out.println("PECENTAGE        : "+s.getPercentage()+"%");
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
            ExamDB.loadExam(exam);
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

    private StudentCourses retrieveCourses() {return CourseDB.getCourses(this.student);}

    private void printCourseAttendance(String courseCode) {
        System.out.println();
        var sessions = SessionDB.getSessions(courseCode).toList();
        for(Session session : sessions) {
            if (session.contains(student)){
                String resultString = "[" +
                        session.getTime() +
                        "]\t: " +
                        (session.getAttendance(this.student) ? "Present" : "Absent") +
                        "\n";
                System.out.print(resultString);
            }
        }
        System.out.println("\n\nCOURSE :" + CourseDB.get(courseCode) + '\n');
    }

    @Override
    public String display() {return "";}

    public Student getStudent() {
        return student;
    }

    @Override
    public void printHeader(String out) {
        UniversityApp.makeClear();
        System.out.println("----------------------Student App----------------------");
        System.out.println("IN :" + this.student.getName() + " > " + out);
        System.out.println("-------------------------------------------------------\n");
    }
}
