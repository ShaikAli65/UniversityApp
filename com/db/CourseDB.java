package db;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import app.academics.Course;
import app.academics.FacultyCourse;
import app.academics.StudentCourse;
import app.admin.Faculty;
import app.admin.Student;


public class CourseDB {
    final private static HashMap<Faculty, FacultyCourse> facultyCourses = new HashMap<>();
	final private static HashMap<Student, StudentCourse> studentCourses = new HashMap<>();
    final private static HashMap<String, Course> courses = new HashMap<>();
    public static void addCourse(Course course) {
        courses.put(course.getCode(),course);
    }
    public static void updateCourse(Student student, StudentCourse course) {
        studentCourses.put(student, course);
    }
    public static void updateCourse(Faculty faculty, FacultyCourse course) {
        facultyCourses.put(faculty, course);
    }
    public static void removeCourse(Course course) {
        courses.remove(course.getCode());
    }
    public static StudentCourse getCourses(Student student) {
        return studentCourses.get(student);
    }
    public static FacultyCourse getCourses(Faculty faculty) {
        return facultyCourses.get(faculty);
    }
    public static Stream<Student> getStudentsWithCourse(Course course) {
        return StudentDB.getStudents().stream()
        .filter(studentCourses::containsKey)
        .filter(student -> CourseDB.getCourses(student).contains(course));
    }
    public static Stream<Faculty> getFacultiesForCourse(Course course) {
        return FacultyDB.getFaculties().stream()
                .filter(facultyCourses::containsKey)
                .filter(faculty -> CourseDB.getCourses(faculty).contains(course));
    }
    public static List<Course> getCoursesForSemester(int semester) {
        return courses.values().stream().parallel().filter(course -> course.getSemester() == semester).toList();
    }
    public static List<Course> getCourses() {
        return courses.values().stream().toList();
    }
    public static boolean isEmpty() {
        return courses.isEmpty();
    }
    public static int noOfCourses() {
        return courses.size();
    }
            public static void loadDatabase(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            courses.clear(); // Clear existing data
            List<Course> courses = (List<Course>) inputStream.readObject();
            courses.forEach(CourseDB::addCourse);
        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
        }
    }

    public static void saveDatabase(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            List<Course> _courses = new ArrayList<>(courses.values());
            outputStream.writeObject(_courses);
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }
}
