package db;

import app.academics.Course;
import app.admin.Faculty;
import app.faculty.Session;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


public class SessionDB {
    private static Boolean changed = false;
    private static final int maxSize = 50;
    private static final Cache<Session> sessionsCache = new Cache<>(maxSize, SessionDB::sessionString);
    public static Path SessionsDir = Loader.SessionsDir;
    public static final ArrayList<Session> retarded = new ArrayList<>();
    public static String sessionString(Session session) {
        return session.getCourseCode()
                +"$"+session.getTime()+".ser";
    }
    public static void register(Faculty f) {
        try {
            var path = SessionsDir.resolve(f.getEmpCode());
            if (! Files.exists(path))
                Files.createDirectory(path);
        } catch (IOException ignore) {}
    }
    public static void add(Session session) {
        changed=true;
        sessionsCache.cache(session);
        writeSession(session);
    }
    public static void update(Session session) {
        changed=true;
        writeSession(session);
    }
    public static void remove(Session session) {
        changed=true;
        sessionsCache.remove(session);
        var filePath = resolveSessionFile(session);
        if (filePath.exists())
            filePath.delete();
    }


    public static List<Session> getSessions(Faculty f, Course c) {
        var dirPath = resolveUptoCourseDir(f,c.getCode());
        return loadSessionViews(dirPath);
    }

    public static Stream<Session> getSessions(String courseCode) {
        var faculties = CourseDB.getFacultiesForCourse(courseCode);
        return faculties.flatMap(
                    faculty -> loadSessions(
                            resolveUptoCourseDir(faculty, courseCode)
                    ).stream()
                )
            .sorted();
    }

    public static List<Session> loadSessions(Path sessionsDir) {
        try (Stream<Path> stream = Files.walk(sessionsDir)) {
            return stream
                    .filter(Files::isRegularFile)
                    .map(SessionDB::readSessionFromFile)
                    .filter(Optional::isPresent)
                    .map(Optional::get).toList();
        } catch (IOException e) {
            System.err.println("Error walking the file tree: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private static Path resolveUptoCourseDir(Faculty f,String c) {
        Path facultyPath = SessionsDir.resolve(f.getEmpCode());
        Path coursePath = facultyPath.resolve(c);
        if (!Files.exists(coursePath)) {
            try {
                Files.createDirectories(coursePath);
            } catch (IOException ignore) {
                return Loader.TRASHDIR;
            }
        }
        return coursePath;
    }

    private static Optional<Session> readSessionFromFile(Path path) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            Session s = new Session();
            s.readExternal(inputStream);
            sessionsCache.cache(s);
            return Optional.of(s);
        } catch (IOException | ClassNotFoundException e) {return Optional.empty();}
    }

    public static void loadSession(Session s) {
        var sessionFile = resolveSessionFile(s);
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(sessionFile))) {
            s.readExternal(inputStream);
            sessionsCache.cache(s);
        } catch (IOException | ClassNotFoundException ignored) {
            retarded.add(s);
        }
    }

    private static File resolveSessionFile(Session s) {
        Path facultyPath = SessionsDir.resolve(s.getFacultyCode());
        Path coursePath = facultyPath.resolve(s.getCourseCode());
        Path sessionFilePath = coursePath.resolve(sessionString(s));
        if (!Files.exists(coursePath)) {
            try {
                Files.createDirectories(coursePath);
            } catch (IOException ignore) {
                return SessionsDir.resolve(sessionString(s)).toFile();
            }
        }
        return sessionFilePath.toFile();
    }

    public static List<Session> loadSessionViews(Path sessionsDir) {
        try (Stream<Path> stream = Files.walk(sessionsDir)) {
            return stream.filter(Files::isRegularFile)
                         .map(SessionDB::loadSessionViewFromFile)
                         .flatMap(Optional::stream)
                         .toList();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    private static Optional<Session> loadSessionViewFromFile(Path path) {
        Session s = new Session();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            s.readView(inputStream);
            sessionsCache.cache(s);
            return Optional.of(s);
        } catch (IOException | ClassNotFoundException ignored) {
            retarded.add(s);
            return Optional.empty();
        }
    }

    private static void writeSession(Session s) {
        var filePath = resolveSessionFile(s);
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            s.writeExternal(outputStream);
        } catch (IOException ignore) {retarded.add(s);}
    }
    public static void saveData(String sessionFile) {
        if (!changed) return;
//        try {
//                FileWriter writer = new FileWriter(sessionFile);
//                writer.write("");
//                writer.close();
//            } catch (IOException ignore) {}
//        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(sessionFile))) {
//            outputStream.writeObject(sessionsCache);
//        } catch (IOException ignore) {}
        retarded.forEach(s -> {
            var filePath = Loader.TRASHDIR.resolve(s.getFacultyCode()+"$"+s.getCourseCode()+"$"+s.getTime());
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
                s.writeExternal(outputStream);
            } catch (IOException ignore) {}
        });
        System.out.println("Session data Saved");
    }
}
