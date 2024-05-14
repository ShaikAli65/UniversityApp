package app.faculty;

import app.Time;
import app.University;
import app.UniversityApp;
import app.academics.AcademicsApp;
import app.admin.*;

import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Stream;


public class Session {
    final private Time time;
    final private Course course;
    final private Faculty faculty;
    final private HashMap<Student,Boolean> attendees;
    public Session(Time time, Course course, Faculty faculty) {
        this.time = time;
        this.course = course;
        this.attendees = new HashMap<>();
        this.faculty = faculty;
    }

    public Session(Time time, Course course, Faculty faculty, HashMap<Student, Boolean> attendees) {
        this.time = time;
        this.course = course;
        this.faculty = faculty;
        this.attendees = attendees;
    }

    public Session(Session session) {
        this(session.time, session.course, session.faculty, session.attendees);
    }

    // Utility Functions

    public boolean isOfCourse(Course course) {
        return this.course.equals(course);
    }

    public void makePresent(Student student) {
        attendees.put(student, true);
    }

    public void makeAbsent(Student student)  {
        attendees.put(student, false);
    }

    public void updateSession() {
        var students = printSession();
        while (true) {
            System.out.print("\nEnter index to change attendance or zero to return : ");
            int choice = University.getIntegerFromInput();
            if (choice == 0) break;
            if (choice > attendees.size()) {
                UniversityApp.getError(6);
                continue;
            }
            var student = students.get(choice);
            if (attendees.get(student)) makeAbsent(student); else makePresent(student);
            AcademicsApp.getStudentCourse(student).editAttendance(course, attendees.get(student));
        }

    }

    // Getters

    public String getSessionTime() {
        return time.toString();
    }
    public void takeAttendanceEntries() {
        System.out.println("Enter Attendance of respective students :");
        Stream<Student> students = AcademicsApp.getStudentsWithCourse(course);
        students.forEach(student -> {
            System.out.print("ROLL NO : " + student.getRollNo() + "\tAttendance (1/0):");
            var attendance = University.getIntegerFromInput() != 0;

            if (attendance) makePresent(student); else makeAbsent(student);
            AcademicsApp.getStudentCourse(student).addAttendance(course, attendance);
        });
        students.close();
    }
    public Course getCourse() {
        return course;
    }
    public HashMap<Student, Boolean> getAttendees() {
        return attendees;
    }
    public Faculty getFaculty() {
        return faculty;
    }
    public Time getTime() {
        return time;
    }
    public boolean getAttendance(Student s) {
        return attendees.get(s);
    }

    // Printers

    public HashMap<Integer,Student> printSession() {
        System.out.println(this + "\t" + course.getCredits() + "\t" + course.getSemester());
        HashMap<Integer,Student> studentMap = new HashMap<>();
        int i = 1;
        for (var value : attendees.entrySet()) {
            System.out.println(i+". ROLL NO:" + value.getKey().getRollNo() + "\tStudent : " + value.getKey().getName() + "\tAttendance: " + (value.getValue() ? "Present" : "Absent"));
            studentMap.put(i, value.getKey());
            i++;
        }
        return studentMap;
    }

    public String toString() {
        return "TIME: " + time.toString() + "\tCOURSE CODE: " + course.getCode()+ "\tCOURSE NAME: " + course.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(time, session.time) && Objects.equals(course, session.course) && Objects.equals(faculty, session.faculty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, course, faculty);
    }
}