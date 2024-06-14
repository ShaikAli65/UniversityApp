package app.faculty;

import app.Choices;
import app.Time;
import app.University;
import app.UniversityApp;
import app.admin.Faculty;
import db.AttendanceDB;
import db.CourseDB;
import db.ExamDB;
import db.SessionDB;

import java.io.Serializable;
import java.util.Arrays;

public class FacultyUser implements University, Serializable
{

    final private char[] passwordArray;
    final private Faculty faculty;

    public FacultyUser(Faculty faculty, char[] passwordArray) {
        this.faculty = faculty;
        this.passwordArray = passwordArray;
    }

    @Override
    public String display() {
        return "";
    }

    public boolean authenticate() {
        printHeader("Authentication");
        if(this.passwordArray == null) {UniversityApp.getError(2);return false;}
        char[] password = University.getPasswordFromInput();
        return Arrays.equals(passwordArray, password);
    }

    // Utility Functions

    public void addSession() {
        var coursesCache = CourseDB.getCourses(this.faculty);
        printHeader("Adding Session");
        Time time = new Time();
        time.addTime();
        Session session = new Session(time, Choices.getCourse(coursesCache, "Adding Session"), this.faculty);
        printHeader("Adding Session > Entering Attendance");
        session.takeAttendanceEntries();
        SessionDB.add(session);
    }

    public void displaySessions() {
        var session = Choices.getSession(this.faculty, "Displaying Sessions");
        if (session == null) {
            UniversityApp.holdNextSlide();
            return;
        }
        printHeader("Displaying Sessions");
        session.displaySession();
        UniversityApp.holdNextSlide();
    }

    void updateSession() {
        var session = Choices.getSession(this.faculty, "Updating Session");
        if (session == null) {
            UniversityApp.holdNextSlide();
            return;
        }
        printHeader("Updating Session >" + session.getTime() + " " + session.getCourse().getCode());
        session.updateSession();
        SessionDB.update(session);
    }

    public void deleteSession() {
        var session = Choices.getSession(this.faculty, "Deleting Session");
        if (session == null) {
            UniversityApp.holdNextSlide();
            return;
        }
        printHeader("Deleting Session");
        for (var entry : session.getAttendees().entrySet()) {
            AttendanceDB.delete(entry.getKey(), session.getCourse(), entry.getValue());
        }
        SessionDB.remove(session);
    }

    public void enterExamData() {
        printHeader("Entering Exam Data");
        var coursesCache = CourseDB.getCourses(this.faculty);
        var course = Choices.getCourse(coursesCache, "Entering Exam Data");
        if (course == null) {
            return;
        }
        var exam = Choices.getExam(course, "Entering Exam Data");
        if (exam == null) {
            return;
        }
        printHeader("Entering Exam Data > Entering Marks");
        try {
            exam.getMarksEntriesFromInput();
            exam.markEvaluated();
            ExamDB.update(exam);
        } catch (Exception e) {
            UniversityApp.getError(17);
        }
    }

    public void displayExams() {
        var exam = Choices.getExam(this.faculty, "Displaying Exams");
        if (exam == null) {
            UniversityApp.holdNextSlide();
            return;
        }
        printHeader("Displaying Exams");
        exam.printExam();
        UniversityApp.holdNextSlide();
    }

    void updateExam() {
        var exam = Choices.getExam(this.faculty, "Updating Exam");
        if (exam == null) {return;}
        printHeader("Updating Exam >" + exam);
        exam.updateExam();
        ExamDB.update(exam);
    }

    public void deleteExam() {
        var exam = Choices.getExam(this.faculty, "Deleting Exam");
        if (exam == null) {
            UniversityApp.holdNextSlide();
            return;
        }
        ExamDB.remove(exam);
    }

    @Override
    public void printHeader(String out) {
        UniversityApp.makeClear();
        System.out.println("----------------- Faculty App -----------------");
        System.out.println("IN :" + out);
        System.out.println("-----------------------------------------------\n");
    }
}