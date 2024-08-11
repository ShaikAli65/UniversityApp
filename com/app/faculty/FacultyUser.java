package app.faculty;

import java.io.Serializable;
import java.util.Arrays;

import app.Choices;
import app.Time;
import app.University;
import app.UniversityApp;
import app.admin.Faculty;
import db.CourseDB;
import db.ExamDB;
import db.SessionDB;

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
        var selectedCourse = Choices.getCourse(coursesCache, "Adding Session");
        Session session = new Session(time, selectedCourse, this.faculty);
        printHeader("Adding Session > Entering Attendance");
        session.takeAttendanceEntries();
        UniversityApp.holdNextSlide();
        SessionDB.add(session);
    }

    public void displaySessions() {
        var course = Choices.getCourse(CourseDB.getCourses(this.faculty),"Displaying Sessions");
        if (course == null) {
            return;
        }
        var sessions = SessionDB.getSessions(this.faculty,course);
        var session = Choices.getChoice(sessions, "Displaying Sessions > "+course.getCode());
        if (session == null) {
            UniversityApp.getError(5);
            UniversityApp.holdNextSlide();
            return;
        }
        printHeader("Displaying Sessions");
        SessionDB.loadSession(session);
        session.displaySession();
        UniversityApp.holdNextSlide();
    }

    void updateSession() {
        var course = Choices.getCourse(CourseDB.getCourses(this.faculty),"Displaying Sessions");
        if (course == null) {
            return;
        }
        var session = Choices.getChoice(SessionDB.getSessions(this.faculty,course),
                "Updating Session > "+course.getCode());
        if (session == null) {
            return;
        }
        printHeader("Updating Session > " + session.getTime() + " " + course.getCode());
        SessionDB.loadSession(session);
        session.updateSession();
        SessionDB.update(session);
    }

    public void deleteSession() {
        var course = Choices.getCourse(CourseDB.getCourses(this.faculty),"Displaying Sessions");
        if (course == null) {
            return;
        }
        var session = Choices.getChoice(SessionDB.getSessions(this.faculty,course), "Deleting Session");
        if (session == null) {
            return;
        }
        session.delete();
    }

    public void enterExamData() {
        printHeader("Entering Exam Data");
        var courses = CourseDB.getCourses(this.faculty);
        var course = Choices.getCourse(courses, "Entering Exam Data");
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
        var course = Choices.getCourse(CourseDB.getCourses(faculty),"Displaying Exams");
        if (course == null) {
            return;
        }
        var exam = Choices.getExam(course, "Displaying Exams");
        if (exam == null) {
            return;
        }
        printHeader("Displaying Exams");
        ExamDB.loadExam(exam);
        exam.printExam();
        UniversityApp.holdNextSlide();
    }

    void updateExam() {
        var course = Choices.getCourse(CourseDB.getCourses(faculty),"Displaying Exams");
        if (course == null) {
            return;
        }
        var exam = Choices.getExam(course, "Updating Exam");
        if (exam == null) {return;}
        printHeader("Updating Exam >" + exam);
        ExamDB.loadExam(exam);
        exam.updateExam();
        ExamDB.update(exam);
    }

    public void deleteExam() {
        var course = Choices.getCourse(CourseDB.getCourses(faculty),"Displaying Exams");
        if (course == null) {
            return;
        }
        var exam = Choices.getExam(course, "Deleting Exam");
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