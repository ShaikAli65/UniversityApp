package app.academics;

import app.Date;
import app.University;
import app.UniversityApp;
import app.admin.Student;
import db.CourseDB;
import db.StudentDB;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Exam implements Externalizable, Comparable<Exam> {
    @Serial
    private static final long serialVersionUID = 1L;
    public String timeString;
    private Date date;
    private boolean isEvaluated;
    private String courseCode;
    private transient Map<Student, Integer> marks;

    public Exam(Date d, Course course){
        date = d;
        this.courseCode = course.getCode();
        marks = new HashMap<>();
        this.isEvaluated = false;
        timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
    }

    public Exam(Date d, Course course, HashMap<Student, Integer> marksStudents){
        date = d;
        this.courseCode = course.getCode();
        this.marks = marksStudents;
        this.isEvaluated = true;
        timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
    }

    public Exam() {
        timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        marks = new HashMap<>();
    }

    public void add(Student student, int marks) { this.marks.put(student, marks); }

    public void getMarksEntriesFromInput() {
        System.out.println("Enter Marks of respective students\n");
        Stream<Student> students = CourseDB.getStudentsWithCourse(courseCode);
        students.sequential().forEach(student -> {
            System.out.print("ROLL NO : " + student.getRollNo() + "\tMarks : ");
            var marks = University.getIntegerFromInput();
            add(student, marks);
        });
        students.close();
    }
    public int  getMarks(Student student) {
        if (marks.containsKey(student)){
            return marks.get(student);
        }
        return 0;
    }
    public void printExam() {
        StringBuilder sb = new StringBuilder();
        for (var entry : marks.entrySet()) {
            String paddedName = String.format("%-20s", entry.getKey().getName());
            sb.append(entry.getKey().getRollNo())
                    .append("\t")
                    .append(paddedName)
                    .append("\tM: ")
                    .append(entry.getValue())
                    .append("\n");
        }
        sb.append("\n").append("EXAM ").append(this);
        System.out.println(sb);
    }

    public void updateExam() {
        if (marks.isEmpty()) {
            UniversityApp.getError(11);
            return;
        }
        while (true) {
            printExam();
            System.out.print("Enter the roll number of student to update marks 0 to return:");
            var rollNo = University.getStringFromInput(false);
            if (rollNo.isBlank() || rollNo.isEmpty()) continue;
            if (Objects.equals(rollNo, "0")) return;
            marks
                    .keySet()
                    .stream()
                    .filter(student -> student.getRollNo().equals(rollNo))
                    .findFirst()
                    .ifPresentOrElse(
                            student -> {
                                System.out.print("Enter new marks : ");
                                var marks = University.getIntegerFromInput();
                                this.marks.put(student, marks);
                            },
                            () -> UniversityApp.getError(6)
                    );
        }
    }
    public void remove(Student student) { marks.remove(student); }
    public void markEvaluated() { isEvaluated = true; }
    public String getCourseCode() { return courseCode; }
    public Date    getExamDate() { return date; }
    public boolean withCourse(Course course) { return this.courseCode.equals(course.getCode()); }
    public boolean matchExam(String _check) {
        var course = CourseDB.get(courseCode);
        return courseCode.toLowerCase().contains(_check)
                        || course.getName().toLowerCase().contains(_check)
                        || date.toString().contains(_check);
    }
    public String toString() {
        var course = CourseDB.get(courseCode);
        String paddedName = String.format("%-20s", course.getName());
        return date.toString()
                + "\tCOURSE: " + courseCode
                + "\t"+ paddedName
                + "\t"+ "graded: " + (isEvaluated ? "Y" : "N");
    }
    @Override public int hashCode() { return Objects.hash(date, courseCode, timeString); }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exam exam = (Exam) o;
        return Objects.equals(date, exam.date)
                && Objects.equals(courseCode, exam.courseCode)
                && Objects.equals(timeString, exam.timeString);
    }
    @Override public int compareTo(Exam o) { return this.date.compareTo(o.date); }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.timeString);
        out.writeObject(this.courseCode);
        out.writeObject(this.date);
        out.writeObject(this.isEvaluated);
        var stuIDs = this.marks.entrySet()
                 .stream()
                 .collect(Collectors.toMap(
                         entry -> entry.getKey().getRollNo(),
                         Map.Entry::getValue
                 ));
        out.writeObject(stuIDs);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.timeString = (String) in.readObject();
        this.courseCode = (String) in.readObject();
        this.date = (Date) in.readObject();
        this.isEvaluated = (boolean) in.readObject();
        var stuIDs = (HashMap<String,Integer>) in.readObject();
        for (var entry : stuIDs.entrySet()) {
            Student student = StudentDB.getStudent(entry.getKey());
            if (student != null){
                this.marks.put(student, entry.getValue());
            }
        }
    }
    public void readView(ObjectInput in) throws IOException, ClassNotFoundException {
        this.timeString = (String) in.readObject();
        this.courseCode = (String) in.readObject();
        this.date = (Date) in.readObject();
        this.isEvaluated = (boolean) in.readObject();
    }
}