package db;

import app.admin.Faculty;

import java.io.*;
import java.util.HashMap;
import java.util.stream.Stream;


public class FacultyDB {
    private static final HashMap<String, Faculty> faculties = new HashMap<>();
    private static Boolean changed = false;
    public static boolean isEmpty() { return faculties.isEmpty(); }
    public static void add(Faculty faculty) {
        changed = true;
        faculties.put(faculty.getEmpCode(), faculty);
        SessionDB.register(faculty);
    }
    public static void update(Faculty faculty) {
        changed = true;
        faculties.put(faculty.getEmpCode(), faculty);
    }
    public static void remove(Faculty faculty) {
        changed = true;
        faculties.remove(faculty.getEmpCode());
    }
    public static Stream<Faculty> getFaculties() {
        return faculties.values().parallelStream();
    }
    public static Faculty getFaculty(String empCode) {
        return faculties.get(empCode);
    }

    @SuppressWarnings("unchecked")
    public static void loadDatabase(String filePath) {
        try (var inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            faculties.clear(); // Clear existing data
            faculties.putAll((HashMap<String, Faculty>) inputStream.readObject());
        } catch (IOException | ClassNotFoundException ignored) {}
    }

    public static void saveData(String filePath) {
        if (!changed) return;
        try {
                FileWriter writer = new FileWriter(filePath);
                writer.write("");
                writer.close();
            } catch (IOException ignore) {}
        try (var outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(faculties);
        } catch (IOException ignored) {}
        System.out.println("Faculty data Saved");
    }
}
