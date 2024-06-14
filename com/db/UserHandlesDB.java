package db;

import app.admin.Faculty;
import app.admin.Student;
import app.faculty.FacultyUser;
import app.student.StudentUser;

import java.io.*;
import java.util.HashMap;


public class UserHandlesDB {
    final private static HashMap<String, char[]> studentUserHashMap = new HashMap<>();
    final private static HashMap<String, char[]> facultyUserHashMap = new HashMap<>();
    private static Boolean changed = false;
    public static void add(Student student, char[] passwordArray) {
        changed = true;
        studentUserHashMap.put(student.getRollNo(), passwordArray);
    }
    public static void add(Faculty faculty, char[] passwordArray) {
        changed = true;
        facultyUserHashMap.put(faculty.getEmpCode(), passwordArray);
    }
    public static void remove(Student student) {
        changed = true;
        studentUserHashMap.remove(student.getRollNo());
    }
    public static void remove(Faculty faculty){
        changed = true;
        facultyUserHashMap.remove(faculty.getEmpCode());
    }

    public static StudentUser getHandle(Student student){
        return new StudentUser(student, studentUserHashMap.get(student.getRollNo()));
    }

    public static FacultyUser getHandle(Faculty faculty) {
        return new FacultyUser(faculty, facultyUserHashMap.get(faculty.getEmpCode()));
    }

    public static boolean contains(Student student) {
        return studentUserHashMap.containsKey(student.getRollNo());
    }
    public static boolean contains(Faculty faculty) {
        return facultyUserHashMap.containsKey(faculty.getEmpCode());
    }

    @SuppressWarnings("unchecked")
    public static void loadDatabase(String[] fileNames) {
        try (var inputStream = new ObjectInputStream(new FileInputStream(fileNames[0]))) {
            studentUserHashMap.clear(); // Clear existing data
            studentUserHashMap.putAll((HashMap<String, char[]>) inputStream.readObject());
        } catch (IOException | ClassNotFoundException ignored) {}
        try (var inputStream = new ObjectInputStream(new FileInputStream(fileNames[1]))) {
            facultyUserHashMap.clear(); // Clear existing data
            facultyUserHashMap.putAll((HashMap<String, char[]>) inputStream.readObject());
        } catch (IOException | ClassNotFoundException ignored) {}

    }
    public static void saveData(String[] fileNames) {
        if (!changed) return;
        try {
                FileWriter writer = new FileWriter(fileNames[0]);
                writer.write("");
                writer.close();
                writer = new FileWriter(fileNames[1]);
                writer.write("");
                writer.close();
            } catch (IOException ignore) {}
        try (var outputStream = new ObjectOutputStream(new FileOutputStream(fileNames[0]))) {
            outputStream.writeObject(studentUserHashMap);
        } catch (IOException ignored) {}
        try (var outputStream = new ObjectOutputStream(new FileOutputStream(fileNames[1]))) {
            outputStream.writeObject(facultyUserHashMap);
        } catch (IOException ignored) {}
        System.out.println("User data Saved");
    }
}
