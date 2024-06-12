package db;

import app.admin.Faculty;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FacultyDB implements ContainerInterface{
    private static final ArrayList<Faculty> faculties = new ArrayList<>();
    public static boolean isEmpty() {
        return faculties.isEmpty();
    }
    public static void add(Faculty faculty) {
        faculties.add(faculty);
    }
    public static void update(Faculty faculty) {

    }
    public static void remove(Faculty faculty) {
        faculties.remove(faculty);
    }
    public static List<Faculty> getFaculties() {
        return faculties;
    }

    public static void loadDatabase(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            faculties.clear(); // Clear existing data
            faculties.addAll((ArrayList<Faculty>) inputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
        }
    }

    public static void saveDatabase(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(faculties);
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }
}
