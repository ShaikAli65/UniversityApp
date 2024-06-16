package app.faculty;

import app.Time;
import app.University;
import app.UniversityApp;
import app.academics.Course;
import app.admin.Faculty;
import app.admin.Student;
import db.AttendanceDB;
import db.CourseDB;
import db.FacultyDB;
import db.SessionDB;
import db.StudentDB;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class Session implements Serializable, Comparable<Session> {
    @Serial
    private static final long serialVersionUID = 1L;

    final private Time time;
    private transient Faculty faculty;
    private transient Course course;
    private transient Map<Student, Boolean> attendees;

    public Session(Time time, Course course, Faculty faculty) {
        this.time = time;
        this.course = course;
        this.attendees = new HashMap<>();
        this.faculty = faculty;
    }
    public Session(Time time, Course course, Faculty faculty, Map<Student, Boolean> attendees) {
        this.time = time;
        this.course = course;
        this.faculty = faculty;
        this.attendees = attendees;
    }

    public void takeAttendanceEntries() {
        System.out.println("Enter Attendance of respective students :");
        var students = CourseDB.getStudentsWithCourse(course);
        AtomicInteger present = new AtomicInteger();
        students.forEach(student -> {
            System.out.print("ROLL NO : " + student.getRollNo() + "\tAttendance (1/0):");
            var attendance = University.getIntegerFromInput() != 0;
            attendees.put(student, attendance);
            present.addAndGet(attendance ? 1 : 0);
            AttendanceDB.add(student, course, attendance);
        });
        students.close();
        System.out.println("STATS : " + present + " PRESENT\t" + (attendees.size() - present.get()) + " ABSENT");

    }
    public HashMap<Integer, Student> printSession() {
        System.out.println(this + "\tCR: " + course.getCredits() + "\tSEM: " + course.getSemester() + "\n");
        HashMap<Integer, Student> studentMap = new HashMap<>();
        int i = 1;
        for (var value : attendees.entrySet()) {
            String paddedRollNo = String.format("%-10s", value.getKey().getRollNo());
            String paddedName = String.format("%-20s", value.getKey().getName());
            System.out.println(
                    i + ". ROLL: " + paddedRollNo
                            + "\t" + paddedName
                            + (value.getValue() ? "P" : "A")
            );
            studentMap.put(i, value.getKey());
            i++;
        }
        return studentMap;
    }
    public void displaySession() {
        System.out.println(this + "\tCR: " + course.getCredits() + "\tSEM: " + course.getSemester() + "\n");
        StringBuilder sb = new StringBuilder();
        int p = 0;
        for (var value : attendees.entrySet()) {
            String paddedRollNo = String.format("%-10s", value.getKey().getRollNo());
            String paddedName = String.format("%-20s", value.getKey().getName());
            sb.append("ROLL: ").append(paddedRollNo)
                    .append("\t").append(paddedName)
                    .append(value.getValue() ? "PRE" : "AB")
                    .append("\n");
            if (value.getValue()) p++;
        }
        sb.append("\nTOTAL  : ")
                .append(attendees.size())
                .append("\tPRESENT: ").append(p)
                .append("\tABSENT : ").append(attendees.size() - p);
        System.out.println(sb);
    }
    public void updateSession() {
        var students = printSession();
        if (students.isEmpty()) {
            UniversityApp.getError(11);
            return;
        }
        while (true) {
            System.out.print("\nEnter index to flip attendance or zero to return : ");
            int choice = University.getIntegerFromInput();
            if (choice == 0) break;
            if (choice > attendees.size()) {
                UniversityApp.getError(6);
                continue;
            }
            var student = students.get(choice);
            AttendanceDB.update(student, course, !attendees.get(student));
            attendees.put(student, !attendees.get(student));
            printSession();
        }
    }
    public void remove(Student student) {attendees.remove(student);}
    public void delete() {
        for (var entry : attendees.entrySet()) {
            AttendanceDB.delete(entry.getKey(), course, entry.getValue());
        }
        SessionDB.remove(this);
    }

    public Map<Student, Boolean> getAttendees() {return attendees;}
    public Course  getCourse() {return course;}
    public Faculty getFaculty() {return faculty;}
    public Time    getTime() {return time;}
    public boolean getAttendance(Student s) {return attendees.get(s);}
    public boolean isOfCourse(Course course) {return this.course.equals(course);}
    public boolean withFaculty(Faculty faculty) {return this.faculty.equals(faculty);}

    public String toString() {
        return "[" + time + "]" + "\tCOURSE : " + course.getCode() + "\t" + course.getName();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(time, session.time) && Objects.equals(course, session.course) && Objects.equals(faculty, session.faculty);
    }
    @Override
    public int hashCode() {return Objects.hash(time, course, faculty);}
    @Override
    public int compareTo(Session o) {return this.time.compareTo(o.time);}
    @Serial
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        this.course = CourseDB.get((String) ois.readObject());
        this.faculty = FacultyDB.getFaculty((String) ois.readObject());

        try {
            this.attendees = new HashMap<>();
            var attendeeIds = (HashMap<String, Boolean>) ois.readObject();
            for (var entry : attendeeIds.entrySet()) {
                Student student = StudentDB.getStudent(entry.getKey());
                this.attendees.put(student, entry.getValue());
            }
        }
        catch (ClassNotFoundException e) {
            this.attendees = new HashMap<>();
        }
    }
    @Serial
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(this.course.getCode());
        oos.writeObject(this.faculty.getEmpCode());
        var attendeeIds = this.attendees.entrySet()
                 .stream()
                 .collect(Collectors.toMap(
                         entry -> entry.getKey().getRollNo(),
                         Map.Entry::getValue
                 ));
        oos.writeObject(attendeeIds);
//        System.out.println("Session data Saved"+this.time+" "+this.course.getCode()); // Debug
    }
}