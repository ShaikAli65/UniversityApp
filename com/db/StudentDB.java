package db;
import app.admin.Student;

import java.io.*;
import java.util.HashMap;
import java.util.stream.Stream;

public class StudentDB {
    private static final HashMap<String, Student> students = new HashMap<>();
    private static Boolean changed = false;
    public static boolean isEmpty() {
        return students.isEmpty();
    }
    public static void add(Student student) {
        changed = true;
        students.put(student.getRollNo(), student);
    }
    public static void update(Student student) {
        changed = true;
//        students.put(student.getRollNo(), student);
    }
    public static void remove(Student student) {
        changed = true;
        students.remove(student.getRollNo());
    }
    public static Stream<Student> getStudents() {
        return students.values().stream();
    }
    public static Stream<String> getStudentsRollNos() {
        return students.values().stream().map(Student::getRollNo);
    }
    public static Student getStudent(String rollNo) {
        return students.get(rollNo);
    }

    @SuppressWarnings("unchecked")
    public static void loadDatabase(String fileName) {
        try (var inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            students.clear(); // Clear existing data
            students.putAll((HashMap<String, Student>) inputStream.readObject());
        } catch (IOException | ClassNotFoundException ignored) {}
    }
    public static void saveData(String fileName) {
        if (!changed) return;
        try {
                FileWriter writer = new FileWriter(fileName);
                writer.write("");
                writer.close();
            } catch (IOException ignore) {}
        try (var outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(students);
        } catch (IOException ignored) {}
        System.out.println("Student data Saved");
    }
}
