package app.faculty;

import app.Time;
import app.University;
import app.UniversityApp;
import app.academics.Exam;
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

    public FacultyUser(Faculty faculty) {
        passwordArray = University.getPasswordFromInput();
        this.faculty = faculty;
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
        var courses_cache = CourseDB.getCourses(this.faculty);
        printHeader("Adding Session");
        Time time = new Time();
        time.addTime();
        Session session = new Session(time, courses_cache.getCourseChoice(), this.faculty);
        printHeader("Adding Session > Entering Attendance");
        session.takeAttendanceEntries();
        SessionDB.add(session);
    }

    public void displaySessions() {
        printHeader("Displaying Sessions");
        var session = getSessionChoice();
        if (session == null) {
            return;
        }
        session.printSession();
    }

    void updateSession() {
        printHeader("Updating Session");
        var session = getSessionChoice();
        if (session == null) {return;}
        printHeader("Updating Session >" + session);
        session.updateSession();
        SessionDB.update(session);
    }

    public void deleteSession() {
        printHeader("Deleting Session");
        var session = getSessionChoice();
        if (session == null) {
            return;
        }
        for (var entry : session.getAttendees().entrySet()) {
//            Student student = entry.getKey();
//            Boolean attendance = entry.getValue();
            AttendanceDB.delete(entry.getKey(), session.getCourse(), entry.getValue());
        }
        SessionDB.remove(session);
    }

    public Session getSessionChoice() {
        var session_cache = SessionDB.getSessions(this.faculty);
        if (session_cache.isEmpty()) {UniversityApp.getError(19);return null;}

        System.out.println("These are the sessions you have: ");
        int i = 1;
        for (Session session : session_cache) {
            System.out.println(i + ". " + session.getTime());
            i++;
        }

        System.out.print("\nEnter the session number you want to select or zero to return: ");
        int choice = University.getIntegerFromInput() - 1;
        if(choice == -1) {return null;}
        try {
            return session_cache.get(choice);
        } catch (Exception e) {
            return null;
        }
    }

    public void enterExamData() {
        printHeader("Entering Exam Data");
        var courses_cache = CourseDB.getCourses(this.faculty);
        var course = courses_cache.getCourseChoice();
        if (course == null) {
            return;
        }
        var exams = ExamDB.getExams(course);
        if (exams.isEmpty()) {
            UniversityApp.getError(17);
            return;
        }

        printHeader("Entering Exam Data > Entering Marks");
        System.out.println("Here are the exams for the course: " + course.getName());
        int i = 1;
        for (Exam exam : exams) {
            System.out.println(i + ". " + exam.getExamDate());
            i++;
        }

        System.out.print("\nEnter the exam number you want to select or zero to exit: ");
        int choice = University.getIntegerFromInput() - 1;
        if (choice == -1) return;
        printHeader("Entering Exam Data > Entering Marks");
        try {
            var exam = exams.get(choice);
            exam.getMarksEntriesFromInput();
            ExamDB.update(exam);
        } catch (Exception e) {
            UniversityApp.getError(17);
        }
    }

    public void displayExams() {
        printHeader("Displaying Exams");
        var exam = getExamChoice();
        if (exam == null) {
            return;
        }
        exam.printExam();
    }

    void updateExam() {
        printHeader("Updating Exam");
        var exam = getExamChoice();
        if (exam == null) {return;}
        printHeader("Updating Exam >" + exam);
        exam.updateExam();
        ExamDB.update(exam);
    }

    public void deleteExam() {
        printHeader("Deleting Exam");
        var exam = getExamChoice();
        if (exam == null) {
            return;
        }
        ExamDB.remove(exam);
    }

    public Exam getExamChoice() {
        var exams_cache = ExamDB.getExams(this.faculty);
        if (exams_cache.isEmpty()) {
            UniversityApp.getError(17);
            return null;
        }
        System.out.println("These are the exams conducted:");
        int i = 1;
        for (Exam exam : exams_cache) {
            System.out.println(i + ". " + exam.getExamDate());
            i++;
        }
        System.out.println("Enter the exam number you want to select:");
        int choice = University.getIntegerFromInput() - 1;
        try {
            return exams_cache.get(choice);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void printHeader(String out) {
        UniversityApp.makeClear();
        System.out.println("----------------- Faculty App -----------------");
        System.out.println("IN :" + out);
        System.out.println("-----------------------------------------------\n");
    }
}