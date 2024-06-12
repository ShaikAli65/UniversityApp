package db;
import app.admin.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDB {
    private static final ArrayList<Student> students = new ArrayList<>();
    public static boolean isEmpty() {
        return students.isEmpty();
    }
    public static void add(Student student) {
        students.add(student);
    }
    public static void update(Student student) {

    }
    public static void remove(Student student) {
        students.remove(student);
    }
    public static List<Student> getStudents() {
        return students;
    }
        public static void loadDatabase(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            students.clear(); // Clear existing data
            students.addAll((ArrayList<Student>) inputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
        }
    }

    public static void saveDatabase(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(students);
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }
}
