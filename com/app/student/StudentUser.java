package app.student;

import app.Choices;
import app.University;
import app.UniversityApp;
import app.academics.Course;
import app.academics.StudentCourses;
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
        String s = AttendanceDB.getEntry(student);
        if (s == null){
            System.out.println("No Attendance data found");
            UniversityApp.holdNextSlide();
        }
        var courses = retrieveCourses();
        Course course = Choices.getCourse(courses, "Attendance View > Courses");
        if (course == null) {
            return;
        }
//        String courseCode = University.getStringFromInput(true);
//        if(courseCode.contains(".")) return;

        printHeader("Attendance > Detailed View");
        String attendenceString = getCourseAttendance(course.getCode());
        System.out.println(attendenceString);
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

    private String getCourseAttendance(String courseCode) {
        StringBuilder resultString = new StringBuilder();
        resultString.append("\n\n");
        var sessions = SessionDB.getSessions(courseCode);
        sessions.forEach(session ->{
                    if (session.contains(student)){
                        resultString.append("[")
                            .append(session.getTime())
                            .append("]\t: ")
                            .append(session.getAttendance(this.student) ? "Present" : "Absent")
                            .append("\n");
                    }
        });
        resultString.append("\n\nCOURSE :").append(CourseDB.get(courseCode)).append('\n');
        return resultString.toString();
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
