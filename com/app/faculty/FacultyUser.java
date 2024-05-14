package app.faculty;

import app.*;
import app.academics.AcademicsApp;
import app.academics.FacultyCourse;
import app.admin.Faculty;
import app.admin.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class FacultyUser implements University
{
    final private ArrayList<Session> session_list;
    final private ArrayList<Exam> exams_list;
    final private char[] passwordArray;
    final private Faculty faculty;

    public FacultyUser(Faculty faculty) {
        session_list = new ArrayList<>(1);
        exams_list = new ArrayList<>(1);
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

    public void addSession(FacultyCourse facCourse) {
        if (facCourse == null) return;
        printHeader("Adding Session");
        Time time = new Time();
        time.addTime();
        Session session = new Session(time, facCourse.getCourseChoice(), this.faculty);
        printHeader("Adding Session > Entering Attendance");
        session.takeAttendanceEntries();
        session_list.add(session);
    }

    public void enterExamData(FacultyCourse facultyCourse) {
        if (facultyCourse == null) return;
        printHeader("Entering Exam Data");

        var course = facultyCourse.getCourseChoice();

        List<Exam> exams = AcademicsApp.getExams(course);
        if (exams == null) {
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

        System.out.print("\nEnter the exam number you want to select or zero to exit:");
        int choice = University.getIntegerFromInput() - 1;
        if (choice == -1) return;
        printHeader("Entering Exam Data > Entering Marks");

        try {
            var exam = exams.get(choice);
            exam.getMarksEntriesFromInput();
            exams_list.add(exam);
        } catch (Exception e) {
            UniversityApp.getError(17);
        }
    }

    public void deleteSession() {
        printHeader("Deleting Session");
        if(session_list.isEmpty()) {UniversityApp.getError(19);return;}
        var session = getSessionChoice();
        if (session == null) {
            return;
        }

        for (Map.Entry<Student, Boolean> entry : session.getAttendees().entrySet()) {
            Student student = entry.getKey();
            Boolean attendance = entry.getValue();
            AcademicsApp.getStudentCourse(student).removeAttendance(session.getCourse(), attendance);
        }

        session_list.remove(session);
    }

    public void deleteExam() {
        printHeader("Deleting Exam");
        var exam = getExamChoice();
        if (exam == null) {
            return;
        }
        exams_list.remove(exam);
    }

    void updateSession() {
        printHeader("Updating Session");
         if(session_list.isEmpty()) {UniversityApp.getError(19);return;}
        var session = getSessionChoice();
        if (session == null) {return;}
        printHeader("Updating Session >" + session);
        session.updateSession();
    }

    void updateExam() {
        printHeader("Updating Exam");
        var exam = getExamChoice();
        if (exam == null) {return;}
        printHeader("Updating Exam >" + exam);
        exam.updateExam();
    }

    // Getters

    public Exam getExamChoice() {
        if (exams_list.isEmpty()) {
            UniversityApp.getError(17);
            return null;
        }
        System.out.println("These are the exams conducted:");
        printExams();
        System.out.println("Enter the exam number you want to select:");
        int choice = University.getIntegerFromInput() - 1;
        try {
            return exams_list.get(choice);
        } catch (Exception e) {
            return null;
        }
    }

    public Session getSessionChoice() {
        if (session_list.isEmpty()) {UniversityApp.getError(19);return null;}

        System.out.println("These are the sessions you have: ");
        printSessions();

        System.out.print("\nEnter the session number you want to select or zero to return: ");
        int choice = University.getIntegerFromInput() - 1;
        if(choice == -1) {return null;}
        try {
            return session_list.get(choice);
        } catch (Exception e) {
            return null;
        }
    }

    public Stream<Session> getSessionStream() {
        return this.session_list.stream();
    }

    // Print Functions

    public void displaySessions() {
        printHeader("Displaying Sessions");

        if(session_list.isEmpty()) {UniversityApp.getError(19);return;}
        var session = getSessionChoice();

        if (session == null) {
            return;
        }
        session.printSession();
    }

    public void displayExams() {
        printHeader("Displaying Exams");
        var exam = getExamChoice();
        if (exam == null) {
            return;
        }
        exam.printExam();
    }

    public void printExams() {
        int i = 1;
        for (Exam exam : exams_list) {
            System.out.println(i + ". " + exam.getExamDate());
            i++;
        }
    }

    public void printSessions() {
        int i = 1;
        for (Session session : session_list) {
            System.out.println(i + ". " + session.getSessionTime());
            i++;
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