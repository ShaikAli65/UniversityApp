package db;

import app.academics.Course;
import app.academics.Exam;
import app.admin.Faculty;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ExamDB {
    final private static int maxSize = 30;
    private static Boolean changed = false;
    final public static Path ExamsDir = Loader.ExamsDir;
    private static String examString(Exam e) {return e.getExamDate()+"$"+e.timeString+".ser";}
    final private static Cache<Exam> examsCache = new Cache<>(maxSize, ExamDB::examString);
    public static final ArrayList<Exam> retarded = new ArrayList<>();
    public static void register(Course course) {
        // creates a folder for course
        try {
            var path = ExamsDir.resolve(course.getCode());
            if (! Files.exists(path))
                Files.createDirectory(path);
        } catch (IOException ignore) {}
    }
    public static void add(Exam exam) {
        changed = true;
        writeExam(exam);
        examsCache.cache(exam);
    }
    public static void update(Exam exam) {
        changed = true;
        writeExam(exam);
    }
    public static void remove(Exam exam) {
        changed = true;
        examsCache.remove(exam);
        var file = resolveExamFile(exam);
        if (file.exists())
            file.delete();
    }
    public static boolean isEmpty() {
        return false;
    }
    
    public static Stream<Exam> getExams(Course course) {
        var dirPath = resolveExamCourseDir(course);
        return loadExamViews(dirPath);
    }
    public static List<Exam> getExams(Faculty faculty) {
        var courses =  CourseDB.getCourses(faculty);
        AtomicReference<Stream<Exam>> stream = new AtomicReference<>(Stream.of());
        courses.getCourses().forEach(course -> stream.set(Stream.concat(stream.get(),getExams(course))));
        return stream.get().collect(Collectors.toList());
    }
    private static void writeExam(Exam exam) {
        var filePath = resolveExamFile(exam);
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            exam.writeExternal(outputStream);
        } catch (IOException ignore) {}
    }

    public static void loadExam(Exam exam) {
        var sessionPath = resolveExamFile(exam);
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(sessionPath))) {
            exam.readExternal(inputStream);
            examsCache.cache(exam);
        } catch (IOException | ClassNotFoundException ignored) {retarded.add(exam);}
    }
    private static File resolveExamFile(Exam exam) {
        Path coursePath = ExamsDir.resolve(exam.getCourseCode());
        Path examFilePath = coursePath.resolve(examString(exam));
        if (!Files.exists(coursePath)) {
            try {
                Files.createDirectories(coursePath);
            } catch (IOException ignore) {
                return Loader.TRASHDIR.resolve(exam.getCourseCode()+"$"+exam.timeString+".ser").toFile();
            }
        }
        return examFilePath.toFile();
    }
    private static Path resolveExamCourseDir(Course course) {
        Path coursePath = ExamsDir.resolve(course.getCode());
        if (!Files.exists(coursePath)) {
            try {
                Files.createDirectories(coursePath);
            } catch (IOException ignore) {
                return Loader.TRASHDIR.resolve(course.getCode());
            }
        }
        return coursePath;
    }
    private static Stream<Exam> loadExamViews(Path parentDir) {
        var exams = new ArrayList<Exam>();
        try (var stream = Files.walk(parentDir)){
            stream.filter(Files::isRegularFile).forEach(
                    file -> loadExamViewFromFile(file).ifPresent(exams::add)
            );
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return exams.stream();
    }

    private static Optional<Exam> loadExamViewFromFile(Path path) {
        try {
            Exam exam = examsCache.get(path.toFile().getName());
            if (exam == null) {
                try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                    exam = new Exam();
                    exam.readView(inputStream);
                }
            }
            return Optional.of(exam);
        } catch (IOException | ClassNotFoundException e) {
            return Optional.empty();
        }
    }

//    @SuppressWarnings("unchecked")
    public static void loadDatabase(String examsDir) {

    }
    public static void saveData(String examDir) {
        if (!changed) return;

        System.out.println("Exam Data Saved");
    }
}
