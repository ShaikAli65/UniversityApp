package db;
import java.util.ArrayList;
import app.academics.Exam;
import java.util.List;
import app.academics.Course;
import app.academics.FacultyCourse;
import app.admin.Faculty;

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
    public static List<Exam> getExams() {
        return exams;
    }
    public static List<Exam> getExams(Course course) {
        List<Exam> examList = new ArrayList<>();
        exams.stream().parallel().forEach(
            exam -> {
                if(exam.withCourse(course)) {
                    examList.add(exam);
                }
            }
        );
        return examList;
    }
    public static List<Exam> getExams(FacultyCourse course) {
        List<Exam> examList = new ArrayList<>();
        exams.stream().parallel().forEach(
            exam -> {
                if(course.contains(exam.getCourse())) {
                    examList.add(exam);
                }
            }
        );
        return examList;
    }
    public static List<Exam> getExams(Faculty faculty) {
        List<Exam> examList = new ArrayList<>();
        var course = CourseDB.getCourses(faculty);
        exams.stream().parallel().forEach(
            exam -> {
                if(course.contains(exam.getCourse())) {
                    examList.add(exam);
                }
            }
        );
        return examList;
    }
    public static boolean isEmpty() {
        return exams.isEmpty();
    }
}
