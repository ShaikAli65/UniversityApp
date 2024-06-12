package db;

import java.util.HashMap;
import app.admin.Student;
import app.admin.Faculty;
import app.faculty.FacultyUser;
import app.student.StudentUser;


public class UserHandlesDB {
    final private static HashMap<Student, StudentUser> studentUserHashMap = new HashMap<>();
    final private static HashMap<Faculty, FacultyUser> facultyUserHashMap = new HashMap<>();

    public static void add(Student student) {
        studentUserHashMap.put(student, new StudentUser(student));
    }

    public static void add(Faculty faculty) {
        facultyUserHashMap.put(faculty, new FacultyUser(faculty));
    }

    public static void remove(Student student) {
        studentUserHashMap.remove(student);
    }

    public static void remove(Faculty faculty){
        facultyUserHashMap.remove(faculty);
    }

    public static StudentUser getHandle(Student student){
        return studentUserHashMap.get(student);
    }

    public static FacultyUser getHandle(Faculty faculty) {
        return facultyUserHashMap.get(faculty);
    }

    public static boolean contains(Student student) {
        return studentUserHashMap.containsKey(student);
    }
    public static boolean contains(Faculty faculty) {
        return facultyUserHashMap.containsKey(faculty);
    }

}
