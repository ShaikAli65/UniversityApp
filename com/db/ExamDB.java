package db;

import app.academics.Course;
import app.academics.Exam;
import app.academics.FacultyCourse;
import app.admin.Faculty;
import app.admin.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ExamDB {
    final private static List<Exam> exams = new ArrayList<>();

    public static void add(Exam exam) {
        exams.add(exam);
    }
    public static void update(Exam exam) {

    }
    public static void remove(Exam exam) {
        exams.remove(exam);
    }
    public static void remove(Student student) {
        exams.stream().parallel().forEach(exam -> exam.remove(student));
    }
    public static List<Exam> getExams() {
        return exams;
    }
    public static Stream<Exam> getExams(Course course) {
        return exams.stream()
                .parallel()
                .filter(exam -> exam.withCourse(course));
    }
    public static Stream<Exam> getExams(FacultyCourse course) {
        return exams.stream()
                .parallel()
                .filter(exam -> course.getCourses().contains(exam.getCourse()));
    }
    public static Stream<Exam> getExams(Faculty faculty) {
        var courses =  CourseDB.getCourses(faculty);
        return exams.stream()
                .filter(exam -> courses.contains(exam.getCourse()));
    }
    public static boolean isEmpty() {
        return exams.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public static void loadDatabase(String examFile) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(examFile))) {
            exams.clear();
            var _exams = (List<Exam>) inputStream.readObject();
            exams.addAll(_exams);
        } catch (IOException | ClassNotFoundException ignored) {}
    }
    public static void saveData(String examFile) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(examFile))) {
            outputStream.writeObject(exams);
        } catch (IOException ignore) {}
        System.out.println("Exam Data Saved");
    }
}
