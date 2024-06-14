package app.academics;

import app.Date;
import app.University;
import app.UniversityApp;
import app.admin.*;
import db.CourseDB;
import db.StudentDB;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Exam implements Serializable, Comparable<Exam> {
    @Serial
    private static final long serialVersionUID = 1L;

    final private Date date;
    private boolean isEvaluated;
    private transient Course course;
    private transient Map<Student, Integer> marksStudents;

    public Exam(Date d, Course course){
        date = d;
        this.course = course;
        marksStudents = new HashMap<>();
        this.isEvaluated = false;
    }

    public Exam(Date d, Course course, HashMap<Student, Integer> marksStudents){
        date = d;
        this.course = course;
        this.marksStudents = marksStudents;
        this.isEvaluated = true;
    }

    public void add(Student student, int marks) { marksStudents.put(student, marks); }

    public void getMarksEntriesFromInput() {
        System.out.println("Enter Marks of respective students\n");
        Stream<Student> students = CourseDB.getStudentsWithCourse(course);
        students.forEach(student -> {
            System.out.print("ROLL NO : " + student.getRollNo() + "\tMarks : ");
            var marks = University.getIntegerFromInput();
            add(student, marks);
        });
        students.close();
    }
    public int  getMarks(Student student) {
        if (marksStudents.containsKey(student)){
            return marksStudents.get(student);
        }
        return 0;
    }
    public void printExam() {
        StringBuilder sb = new StringBuilder();
        for (var entry : marksStudents.entrySet()) {
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
        if (marksStudents.isEmpty()) {
            UniversityApp.getError(11);
            return;
        }
        printExam();
        System.out.print("Enter the roll number of student to update marks:");
        var rollNo = University.scanner.next();
        marksStudents
                .keySet()
                .stream()
                .filter(student -> student.getRollNo().equals(rollNo))
                .findFirst()
                .ifPresentOrElse(
                        student -> {
                                System.out.print("Enter new marks : ");
                                var marks = University.getIntegerFromInput();
                                marksStudents.put(student, marks);
                        },
                        () -> UniversityApp.getError(6)
                );
    }
    public void remove(Student student) { marksStudents.remove(student); }
    public void markEvaluated() { isEvaluated = true; }
    public Course  getCourse() { return course; }
    public Date    getExamDate() { return date; }
    public boolean withCourse(Course course) { return this.course.equals(course); }

    public String toString() {
        String paddedName = String.format("%-20s", course.getName());
        return date.toString()
                + "\tCOURSE: " + course.getCode()
                + "\t"+ paddedName
                + "\t"+ "graded: " + (isEvaluated ? "Y" : "N");
    }
    @Override public int hashCode() { return Objects.hash(date, course); }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exam exam = (Exam) o;
        return Objects.equals(date, exam.date) && Objects.equals(course, exam.course);
    }
    @Override public int compareTo(Exam o) { return this.date.compareTo(o.date); }

    @Serial
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        var courseCode = (String) ois.readObject();
        this.course = CourseDB.get(courseCode);
        try {
            this.marksStudents = new HashMap<>();
            var marksStudents = (HashMap<String, Integer>) ois.readObject();
            for (var entry : marksStudents.entrySet()) {
                Student student = StudentDB.getStudent(entry.getKey());
                this.marksStudents.put(student, entry.getValue());
            }
        }
        catch (ClassCastException e) {
            this.marksStudents = new HashMap<>();
        }
    }
    @Serial
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(this.course.getCode());
        var attendeeIds = this.marksStudents.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getRollNo(),
                        Map.Entry::getValue
                ));
        oos.writeObject(attendeeIds);
//        System.out.println("Exam data Saved"+this.date+" "+this.course.getCode()); // Debug
    }
}