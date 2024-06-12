package db;

import app.academics.Course;
import app.admin.Faculty;
import app.faculty.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class SessionDB implements ContainerInterface{
    private static final ArrayList<Session> sessions = new ArrayList<>();
    public static void add(Session session) {
        sessions.add(session);
    }
    public static void update(Session session) {

    }
    public static void remove(Session session) {
        sessions.remove(session);
    }
    public static ArrayList<Session> getSessions() {
        return sessions;
    }
    public static List<Session> getSessions(Faculty faculty) {
        List<Session> sessionList = new ArrayList<>();
        sessions.stream().parallel().forEach(
            session -> {
                if(session.withFaculty(faculty)) {
                    sessionList.add(session);
                }
            }
        );
        return sessionList;
    }
    public static boolean isEmpty() {
        return sessions.isEmpty();
    }

    public static Stream<Session> getSessions(Course course) {
        return sessions.stream().filter(session -> session.isOfCourse(course));
    }
}
