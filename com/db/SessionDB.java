package db;

import app.academics.Course;
import app.admin.Faculty;
import app.admin.Student;
import app.faculty.Session;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Stream;


public class SessionDB {
    final private static ArrayList<Session> sessions = new ArrayList<>();
    private static Boolean changed = false;
    public static void add(Session session) {changed=true;sessions.add(session);}
    public static void update(Session session) {
        changed=true;
    }
    public static void remove(Session session) {changed=true;sessions.remove(session);}
    public static void remove(Course course) {
        changed=true;
        sessions.parallelStream().forEach(session -> {
            if (session.isOfCourse(course.getCode())) {
                sessions.remove(session);
            }
        });
    }
    public static void remove(Student student) {
        changed=true;
        sessions.parallelStream().forEach(session -> session.remove(student));
    }
    public static Stream<Session> getSessions() {return sessions.stream();}
    public static Stream<Session> getSessions(Faculty faculty) {
        return sessions
                .stream()
                .filter(
                        session -> session.withFaculty(faculty.getEmpCode())
                );

    }
    public static Stream<Session> getSessions(String courseCode) {
        return sessions.stream().filter(session -> session.isOfCourse(courseCode));
    }

    public static boolean isEmpty() {
        return sessions.isEmpty();
    }
    @SuppressWarnings("unchecked")
    public static void loadDatabase(String sessionFile) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(sessionFile))) {
            sessions.clear();
            var _sessions = (ArrayList<Session>) inputStream.readObject();
            sessions.addAll(_sessions);
        } catch (IOException | ClassNotFoundException ignored) {}
    }
    public static void saveData(String sessionFile) {
        if (!changed) return;
        try {
                FileWriter writer = new FileWriter(sessionFile);
                writer.write("");
                writer.close();
            } catch (IOException ignore) {}
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(sessionFile))) {
            outputStream.writeObject(sessions);
        } catch (IOException ignore) {}
        System.out.println("Session data Saved");
    }
}
