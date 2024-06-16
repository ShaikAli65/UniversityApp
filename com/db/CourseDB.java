package db;

import app.academics.Course;
import app.academics.FacultyCourse;
import app.academics.StudentCourse;
import app.admin.Faculty;
import app.admin.Student;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;


public class CourseDB {
    final private static HashMap<String, FacultyCourse> facultyCourses = new HashMap<>();
	final private static HashMap<String, StudentCourse> studentCourses = new HashMap<>();
    final private static HashMap<String, Course> courses = new HashMap<>();
    private static Boolean changed = false;
    public static void addCourse(Course course) {
        changed = true;
        courses.put(course.getCode(),course);
    }
    public static void updateCourse(Student student, StudentCourse course) {
        changed = true;
        studentCourses.put(student.getRollNo(), course);
    }
    public static void updateCourse(Faculty faculty, FacultyCourse course) {
        changed = true;
        facultyCourses.put(faculty.getEmpCode(), course);
    }
    public static void removeCourse(Course course) {
        changed = true;
        courses.remove(course.getCode());
    }
    public static StudentCourse getCourses(Student student) {
        return studentCourses.get(student.getRollNo());
    }
    public static FacultyCourse getCourses(Faculty faculty) {
        return facultyCourses.get(faculty.getEmpCode());
    }
    public static Stream<Course>  getCourses (int semester) {
        return courses.values().parallelStream().filter(course -> course.getSemester() == semester);
    }
    public static Stream<Student> getStudentsWithCourse(Course course) {
        return StudentDB.getStudents().filter(
                student -> studentCourses.containsKey(student.getRollNo())
                        && studentCourses.get(student.getRollNo()).contains(course)
        );
    }
    public static Stream<Student> getStudentsWithCourse(String courseCode) {
        return StudentDB.getStudents().filter(
                student -> studentCourses.containsKey(student.getRollNo())
                        && studentCourses.get(student.getRollNo()).contains(courses.get(courseCode))
        );
    }
    public static Stream<Faculty> getFacultiesForCourse(Course course) {
        return FacultyDB.getFaculties().filter(
                faculty -> facultyCourses.containsKey(faculty.getEmpCode())
                        && facultyCourses.get(faculty.getEmpCode()).contains(course)
        );
    }
    public static Stream<Faculty> getFacultiesForCourse(String courseId) {
        var course = courses.get(courseId);
        return FacultyDB.getFaculties().filter(
                faculty -> facultyCourses.containsKey(faculty.getEmpCode())
                        && facultyCourses.get(faculty.getEmpCode()).contains(course)
        );
    }

    public static Stream<Course> getCourses() {
        return courses.values().stream();
    }
    public static Course get(String code) {
        return courses.get(code);
    }
    public static boolean isEmpty() {
        return courses.isEmpty();
    }
    public static int noOfCourses() {
        return courses.size();
    }

    @SuppressWarnings("unchecked")
    public static void loadDatabase(String[] fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName[0]))) {
            courses.clear();
            var _courses = (HashMap<String, Course>) inputStream.readObject();
            courses.putAll(_courses);
        } catch (IOException | ClassNotFoundException ignore) {}

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName[1]))) {
            studentCourses.clear();
            var _studentCourses = (HashMap<String, StudentCourse>) inputStream.readObject();
            studentCourses.putAll(_studentCourses);
        } catch (IOException | ClassNotFoundException ignored) {}

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName[2]))) {
            facultyCourses.clear();
            var _courses = (HashMap<String, FacultyCourse>) inputStream.readObject();
            facultyCourses.putAll(_courses);
        } catch (IOException | ClassNotFoundException ignored) {}
    }

    public static void saveData(String[] fileNames) {
        if (!changed) return;
        Arrays.asList(fileNames).forEach(fileName -> {
            try {
                FileWriter writer = new FileWriter(Paths.get(fileName).toString());
                writer.write("");
                writer.close();
            } catch (IOException ignore) {}
        });

        System.out.println("Saving Course Data");
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileNames[0]))) {
            outputStream.writeObject(courses);
        } catch (IOException e) {
            System.out.println("Error in saving Course Data" + e);
        }
        System.out.println("Saving Student Course Data");
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileNames[1]))) {
            outputStream.writeObject(studentCourses);
        } catch (IOException e) {
            System.out.println("Error in saving Student Course Data" + e);
        }
        System.out.println("Saving Faculty Course Data");
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileNames[2]))) {
            outputStream.writeObject(facultyCourses);
        } catch (IOException e) {
            System.out.println("Error in saving Faculty Course Data" + e);
        }
        System.out.println(
                """
                        Course Data Saved
                        Student Course Data Saved
                        Faculty Course Data Saved""");
    }
}
