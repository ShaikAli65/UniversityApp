package app.faculty;

import app.Time;
import app.University;
import app.UniversityApp;
import app.academics.Course;
import app.admin.Faculty;
import app.admin.Student;
import db.AttendanceDB;
import db.CourseDB;
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
    private final String facCode;
    private final String courseCode;
    private transient Map<Student, Boolean> attendees;
//    private final Map<Student, Boolean> attendees;

    public Session(Time time, Course course, Faculty faculty) {
        this.time = time;
        this.courseCode = course.getCode();
        this.attendees = new HashMap<>();
        this.facCode = faculty.getEmpCode();
    }
    public Session(Time time, Course course, Faculty faculty, Map<Student, Boolean> attendees) {
        this.time = time;
        this.courseCode = course.getCode();
        this.facCode = faculty.getEmpCode();
        this.attendees = attendees;
    }

    public void takeAttendanceEntries() {
        System.out.println("Enter Attendance of respective students :");
        var students = CourseDB.getStudentsWithCourse(courseCode);
        AtomicInteger present = new AtomicInteger();
        students.sequential().forEach(student -> {
            System.out.print("ROLL NO : " + student.getRollNo() + "\tAttendance (1/0):");
            var attendance = University.getIntegerFromInput() != 0;
            attendees.put(student, attendance);
            present.addAndGet(attendance ? 1 : 0);
            AttendanceDB.add(student, courseCode, attendance);
        });
        students.close();
        System.out.println("STATS : " + present + " PRESENT\t" + (attendees.size() - present.get()) + " ABSENT");
    }
    public HashMap<Integer, Student> printSession() {
        var course = CourseDB.get(courseCode);
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
        var course = CourseDB.get(courseCode);
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
            AttendanceDB.update(student, courseCode, !attendees.get(student));
            attendees.put(student, !attendees.get(student));
            printSession();
        }
    }
    public void remove(Student student) {attendees.remove(student);}
    public void delete() {
        for (var entry : attendees.entrySet()) {
            AttendanceDB.delete(entry.getKey(), courseCode, entry.getValue());
        }
        SessionDB.remove(this);
    }

    public Map<Student, Boolean> getAttendees() {return attendees;}
    public String  getCourse() {return courseCode;}
    public String getFaculty() {return facCode;}
    public Time    getTime() {return time;}
    public boolean getAttendance(Student s) {return attendees.get(s);}
    public boolean isOfCourse(String cCode) {return this.courseCode.equals(cCode);}
    public boolean withFaculty(String facCode) {return this.facCode.equals(facCode);}
    public boolean matchContains(String _check) {
        var course = CourseDB.get(courseCode);
        return courseCode.toLowerCase().contains(_check)
                || course.getName().toLowerCase().contains(_check)
                || time.toString().contains(_check);
    }
    public String toString() {
        var course = CourseDB.get(courseCode);
        return "[" + time + "]" + "\tCOURSE : " + courseCode + "\t" + course.getName();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(time, session.time) && Objects.equals(courseCode, session.courseCode) && Objects.equals(facCode, session.facCode);
    }
    @Override
    public int hashCode() {return Objects.hash(time, courseCode, facCode);}
    @Override
    public int compareTo(Session o) {return this.time.compareTo(o.time);}
    @Serial
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();

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
        var attendeeIds = this.attendees.entrySet()
                 .stream()
                 .collect(Collectors.toMap(
                         entry -> entry.getKey().getRollNo(),
                         Map.Entry::getValue
                 ));
        oos.writeObject(attendeeIds);
    }
}