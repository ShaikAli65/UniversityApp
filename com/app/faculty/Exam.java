package app.faculty;

import app.Date;
import app.University;
import app.UniversityApp;
import app.academics.AcademicsApp;
import app.admin.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class Exam {
    final private Date date;
    final private Course course;
    final private Map<Student, Integer> marksStudents;

    public Exam(Date d, Course course){
        date = d;
        this.course = course;
        marksStudents = new HashMap<>();
    }

    // Getters

    public String getExamDate() {
        return date.toString();
    }

    public void getMarksEntriesFromInput() {
        System.out.println("Enter Marks of respective students\n");
        Stream<Student> students = AcademicsApp.getStudentsWithCourse(course);
        students.forEach(student -> {
            System.out.print("ROLL NO : " + student.getRollNo() + "\tMarks : ");
            var marks = University.getIntegerFromInput();
            add(student, marks);
        });
        students.close();
    }

    public int getMarks(Student student) {
        if (marksStudents.containsKey(student)){
            return marksStudents.get(student);
        }
        return 0;
    }

    // Printers

    public void printExam() {
        System.out.println(this + "\n");
        for (var entry : marksStudents.entrySet()) {
            System.out.println("ROLL NO:" + entry.getKey().getRollNo() + "\tStudent : " + entry.getKey().getName() + "\tMarks: " + entry.getValue());
        }
    }

    public String toString() {
        return "DATE: " + date.toString() + "\tCOURSE CODE: " + course.getCode()+ "\tCOURSE NAME: " + course.getName();
    }

    // Utility methods

    public void add(Student student, int marks) {
        marksStudents.put(student, marks);
    }

    public void updateExam() {
        printExam();
        System.out.print("Enter the roll number of student to update marks:");
        var rollNo = University.getIntegerFromInput();
        marksStudents.keySet().stream().filter(student -> student.getRollNo() == rollNo)
                .findFirst()
                .ifPresentOrElse(student -> {
                System.out.print("Enter new marks : ");
                var marks = University.getIntegerFromInput();
                marksStudents.put(student, marks);
                }, () -> UniversityApp.getError(6));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exam exam = (Exam) o;
        return Objects.equals(date, exam.date) && Objects.equals(course, exam.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, course);
    }

}