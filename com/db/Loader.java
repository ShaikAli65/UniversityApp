package db;

import app.Date;
import app.Time;
import app.academics.Course;
import app.academics.Exam;
import app.academics.FacultyCourses;
import app.academics.StudentCourses;
import app.admin.Faculty;
import app.admin.Student;
import app.faculty.Session;

import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Loader {
   private static final String SOURCE_PATH = Paths.get(System.getProperty("user.dir"), "storage").toString();
   public static final Path SessionsDir = Paths.get(SOURCE_PATH,"Sessions");
   public static final Path TRASHDIR = Paths.get(SOURCE_PATH,"retarded");
   public static final Path ExamsDir = Paths.get(SOURCE_PATH,"Exams");
   private static final String[] FILE_PATHS = Stream.of(
        "studentDB.ser",
        "facultyDB.ser",
        "courseDB.ser",
        "studentCourseDB.ser",
        "facultyCourseDB.ser",
        "studentUserDB.ser",
        "facultyUserDB.ser",
        "attendanceDB.ser"
    )
    .map(fileName -> Paths.get(SOURCE_PATH, fileName).toString())
    .toArray(String[]::new);

    public static void loadDataBases() {
        checkPaths();
        System.out.println(SOURCE_PATH);
        System.out.print("Loading Databases");
        StudentDB.loadDatabase(FILE_PATHS[0]);
        System.out.print('.');
        FacultyDB.loadDatabase(FILE_PATHS[1]);
        System.out.print('.');
        CourseDB.loadDatabase(new String[]{
            FILE_PATHS[2],
            FILE_PATHS[3],
            FILE_PATHS[4]
            });
        System.out.print('.');
        UserHandlesDB.loadDatabase(new String[]{
            FILE_PATHS[5],
            FILE_PATHS[6]
            });
        System.out.print("\r                    ");
        System.out.print("\rLoading Databases");
        ExamDB.loadDatabase(ExamsDir.toString());
        System.out.print('.');
        AttendanceDB.loadDatabase(FILE_PATHS[7]);
        System.out.print("..");
    }

    private static void checkPaths() {
        if(! Files.exists(TRASHDIR)) {
            var v = TRASHDIR.toFile().mkdirs();
//                Files.createDirectory(TRASHDIR);
        }
    }

    public static void storeDataBases() {
        StudentDB.saveData(FILE_PATHS[0]);
        FacultyDB.saveData(FILE_PATHS[1]);
        CourseDB.saveData(new String[]{
                FILE_PATHS[2],
                FILE_PATHS[3],
                FILE_PATHS[4]}
        );
        UserHandlesDB.saveData(new String[]{
                FILE_PATHS[5],
                FILE_PATHS[6]
        });
        ExamDB.saveData(ExamsDir.toString());
        AttendanceDB.saveData(FILE_PATHS[7]);
//        SessionDB.saveData(SessionsDir.toString());
        SessionDB.saveData(Paths.get(SOURCE_PATH, "sessionDB.ser").toString());
    }

    public static void dupData() {
        System.out.println("Duplicating Data...");
        dupStudentsAndFaculties();
        System.out.println("Duplicated Students and Faculties");
        dupCourses();
        System.out.println("Duplicated Courses");
        dupCourseRelations();
        System.out.println("Duplicated Course Relations");
        dupExams();
        System.out.println("Duplicated Exams");
        dupSessions();
        System.out.println("Duplicated Sessions");
    }
    private static void dupStudentsAndFaculties() {
                 List<String> names = Arrays.asList("Aarav", "Aarush", "Abhinav", "Aditya", "Akhil", "Aryan",
         "Ayush", "Dhruv", "Ishaan", "Kabir", "Mohit", "Nakul", "Naman",
          "Parth", "Pranav", "Raghav", "Rahul", "Rohit", "Sahil", "Samarth",
           "Shivam", "Siddharth", "Vedant", "Yash", "Yuvraj", "Aaradhya", "Aarohi",
            "Ananya", "Anika", "Anushka", "Avni", "Diya", "Ishani", "Ishika", "Ishita",
             "Kavya", "Kiara", "Mahi", "Mehak", "Navya", "Nisha", "Pari", "Prisha", "Riya",
              "Saumya", "Shreya", "Siya", "Tanvi", "Tanya", "Vanya", "Vidhi", "Aarav", "Aarush",
               "Abhinav", "Aditya", "Akhil", "Aryan", "Ayush", "Dhruv", "Ishaan", "Kabir", "Mohit",
                "Nakul", "Naman", "Parth", "Pranav", "Raghav", "Rahul", "Rohit", "Sahil", "Samarth",
                 "Shivam", "Siddharth", "Vedant", "Yash", "Yuvraj", "Aaradhya", "Aarohi", "Ananya",
                  "Anika", "Anushka", "Avni", "Diya", "Ishani", "Ishika", "Ishita",
                  "Kavya", "Kiara", "Mahi", "Mehak", "Navya", "Nisha", "Pari", "Prisha",
                   "Riya", "Saumya", "Shreya", "Siya", "Tanvi", "Tanya", "Vanya", "Vidhi",
                    "Aarav", "Aarush", "Abhinav", "Aditya", "Akhil", "Aryan", "Ayush", "Dhruv",
                     "Ishaan", "Kabir", "Mohit", "Nakul", "Naman", "Parth", "Pranav");
         var codes = new String[]{"CSE", "ECE", "EEE", "MEC"};
        for (int i = 1; i <= 1400; i++) {
             var student = new Student(
                     names.get(i % 100),
                     String.valueOf(i),
                     codes[(int) (Math.random() * 10) % 4],
                     i % 4 + 1,
                     4,
                     0
             );
            StudentDB.add(student);
            UserHandlesDB.add(student, new char[]{'0'});
        }
        for (int i = 1; i <= 100; i++) {
            var facutly = new Faculty(
                    names.get(i % 100).toUpperCase(),
                    codes[(int) (Math.random() * 10) % 4],
                    String.valueOf(i),
                    i % 8,
                    i*1000,
                    4
            );
            FacultyDB.add(facutly);
            UserHandlesDB.add(facutly, new char[]{'1'});
        }
    }
    private static void dupCourses() {
                // a random list of 40 courses
         var codes = new String[]{"CSE", "ECE", "EEE", "MEC"};
         List<String> courseNames = Arrays.asList("Data Structures", "Algorithms",
          "Operating Systems", "Computer Networks", "Database Management Systems",
          "Software Engineering", "Computer Organization", "Computer Architecture",
          "Digital Logic Design", "Computer Graphics", "Artificial Intelligence",
             "Machine Learning", "Deep Learning", "Natural Language Processing",
              "Computer Vision", "Robotics", "Internet of Things", "Cyber Security",
                 "Blockchain", "Quantum Computing", "Big Data", "Cloud Computing",
                  "Mobile Computing", "Web Development", "Game Development", "Virtual Reality",
                 "Augmented Reality", "Mixed Reality", "Data Science", "Data Analytics",
                  "Data Mining", "Data Warehousing", "Data Visualization", "Business Intelligence",
                     "Information Retrieval", "Information Security", "Information Privacy",
                      "Information Theory", "Information Systems", "Information Technology",
                     "Information Management", "Information Science");

        try {
            for (int i = 1; i <= 40; i++) {
                var random = (int) (Math.random() * 10) % 4;
                var course = new Course(courseNames.get(i), codes[random] + i, i % 4 + 1, 4);
                CourseDB.addCourse(course);
                ExamDB.register(course);
            }
        } catch (Exception e) {
//            System.out.println(e.toString());

        }
    }
    private static void dupCourseRelations() {
        var students = StudentDB.getStudents();
        System.out.println("Duplicating Student Courses");
        students.forEach(student -> {
            var courses = CourseDB.getCourses(student.getSemester());
            // get a random 4 courses
            var selectedCourses = courses.collect(Collectors.toList());
            Collections.shuffle(selectedCourses);
            var sc = new StudentCourses(student, new HashSet<>(selectedCourses.subList(0, 4)));
            CourseDB.updateCourse(student, sc);
        });
        System.out.println("Duplicating Faculty Courses");
        var faculties = FacultyDB.getFaculties();
        faculties.sequential().forEach(faculty -> {
            var courses = CourseDB.getCourses();
            // get a random 4 courses
            var selectedCourses = courses.collect(Collectors.toList());
            Collections.shuffle(selectedCourses);
            var fc = new FacultyCourses(faculty, new HashSet<>(selectedCourses.subList(0, 4)));
            CourseDB.updateCourse(faculty, fc);
        });
    }
    private static void dupExams() {
        var courses = CourseDB.getCourses();
        var days = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10
                , 11, 12, 13, 14, 15, 16, 17, 18, 19, 20
                , 21, 22, 23, 24, 25, 26, 27, 28, 29, 30);
        var months = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        courses.forEach(course -> {
            Collections.shuffle(days);
            Collections.shuffle(months);
            for(int i = 0;i < 3;i++) {
                var marks = new HashMap<Student, Integer>();
                var students = CourseDB.getStudentsWithCourse(course);
                students.forEach(student -> marks.put(student, (int) (Math.random() * 100)));
                var exam = new Exam(new Date(days.get(i), months.get(i),2022), course, marks);
                ExamDB.add(exam);
            }
        });
    }
    private static void dupSessions() {
        var faculties = FacultyDB.getFaculties();
        var days = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        , 11, 12, 13, 14, 15, 16, 17, 18, 19, 20
        , 21, 22, 23, 24, 25, 26, 27, 28, 29, 30);
        var months = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        
        for(var faculty : faculties.toList()) {
            if(CourseDB.getCourses(faculty) == null) {
                System.out.println("No courses for faculty " + faculty);
                continue;
            }
            var courses = new ArrayList<>(CourseDB.getCourses(faculty).getCourses());
            for(int i = 0;i < 50;i++) {
                Collections.shuffle(courses);
                Collections.shuffle(days);
                Collections.shuffle(months);
                var attendees = new HashMap<Student, Boolean>();
                var course = courses.get(0);
                var students = CourseDB.getStudentsWithCourse(course);
                students.forEach(student -> {
                    var attendance = Math.random() > 0.2;
                    attendees.put(student, attendance);
                    AttendanceDB.add(student, course.getCode(), attendance);
                });
                var s =  new Session(new Time(days.get(1), days.get(0), months.get(0),2022), course, faculty, attendees);
                SessionDB.add(s);
            }
        }
    }

    public static String stripExtension(Path filePath) {
        String fileName = filePath.toString();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fileName;
        } else {
            return fileName.substring(0, lastDotIndex);
        }
    }
}