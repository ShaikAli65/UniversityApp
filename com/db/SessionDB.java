package db;

import app.academics.Course;
import app.admin.Faculty;
import app.admin.Student;
import app.faculty.Session;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class SessionDB {
    final private static List<Session> sessions = new ArrayList<>();

    public static void add(Session session) {sessions.add(session);}
    public static void update(Session session) {

    }
    public static void remove(Session session) {sessions.remove(session);}
    public static void remove(Course course) {
        sessions.stream().parallel().forEach(session -> {
            if (session.isOfCourse(course)) {
                sessions.remove(session);
            }
        });
    }
    public static void remove(Student student) {sessions.stream().parallel().forEach(session -> session.remove(student));}
    public static Stream<Session> getSessions() {return sessions.stream();}
    public static Stream<Session> getSessions(Faculty faculty) {
        return sessions
                .stream()
                .filter(
                        session -> session.withFaculty(faculty)
                ).sorted();

    }
    public static Stream<Session> getSessions(Course course) {
        return sessions.stream().filter(session -> session.isOfCourse(course));
    }

    public static boolean isEmpty() {
        return sessions.isEmpty();
    }
    @SuppressWarnings("unchecked")
    public static void loadDatabase(String sessionFile) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(sessionFile))) {
            sessions.clear();
            var _sessions = (List<Session>) inputStream.readObject();
            sessions.addAll(_sessions);
        } catch (IOException | ClassNotFoundException ignored) {}
        System.out.println("Session data Loaded");
    }
    public static void saveData(String sessionFile) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(sessionFile))) {
            outputStream.writeObject(sessions);
        } catch (IOException ignore) {}
        System.out.println("Session data Saved");
    }
}
