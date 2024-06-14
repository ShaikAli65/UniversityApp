package app;

import app.academics.Course;
import app.academics.Exam;
import app.academics.FacultyCourse;
import app.academics.StudentCourse;
import app.admin.Faculty;
import app.admin.Student;
import app.faculty.Session;
import db.*;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Choices {
    private static final Scanner scanner = new Scanner(System.in);
    public static Student getStudent(String headerDetail) {
        var students = StudentDB.getStudents().toList();
        return getChoice(students, headerDetail);
    }
    public static Faculty getFaculty(String headerDetail) {
        var faculties = FacultyDB.getFaculties().toList();
        return getChoice(faculties, headerDetail);
    }
    public static Course  getCourse(String headerDetail) {
        var allCourses = CourseDB.getCourses().toList();
        return getChoice(allCourses, headerDetail);
    }
    public static Course  getCourse(StudentCourse sc, String headerDetail) {
        var allCourses = sc.getCourses();
        return getChoice(allCourses,headerDetail);
    }
    public static Course  getCourse(FacultyCourse fc, String headerDetail) {
        var allCourses = fc.getCourses();
        return getChoice(allCourses, headerDetail);
    }
    public static Course  getCourse(int semester, String headerDetail) {
        var courses = CourseDB.getCourses(semester).toList();
        return getChoice(courses, headerDetail);
    }
    public static Session getSession(Faculty faculty, String headerDetail) {
        var sessions= SessionDB.getSessions(faculty).toList();
        if (sessions.isEmpty()) {
            UniversityApp.getError(5);
            return null;
        }
        var sortedSessions = sessions.stream().sorted().toList();
        return getChoice(sortedSessions, headerDetail);
    }
    public static Exam    getExam(String headerDetail) {
        var exams = ExamDB.getExams();
        if (exams.isEmpty()) {
            UniversityApp.getError(5);
            return null;
        }
        return getChoice(exams, headerDetail);
    }
    public static Exam    getExam(Faculty faculty, String headerDetail) {
        var exams = ExamDB.getExams(faculty).toList();
        if (exams.isEmpty()) {
            UniversityApp.getError(5);
            return null;
        }
        return getChoice(exams, headerDetail);
    }
    public static Exam    getExam(Course course, String headerDetail) {
        var exams = ExamDB.getExams(course).toList();
        if (exams.isEmpty()) {
            UniversityApp.getError(5);
            return null;
        }
        return getChoice(exams, headerDetail);
    }

    private static <T> T getChoice(List<T> originalList, String headerDetail) {
        var list = originalList;
        StringBuilder searchedString = new StringBuilder();
        while (true) {
            printHeader(headerDetail + " > selecting");
            print(list);
            System.out.println("\nresults for : " + searchedString);
            System.out.print("Enter index (use '/' for filter) or '.' to return: ");
            if(scanner.hasNextInt()){
                int i = Integer.parseInt(scanner.next());
                if (i < 0 || i > list.size()) {
                    UniversityApp.getError(6);
                    UniversityApp.holdNextSlide();
                    continue;
                }
                if (i == 0) {
                    return null;
                }
                return list.get(i - 1);
            }
            var input = scanner.nextLine();
            if (input.contains(".")) {
                return null;
            }
            if (input.startsWith("/")) {
                input = input.replace("/", "");
                searchedString.append(input).append(" > ");
            }
            if (input.startsWith("\\")) {
                list = originalList;
                input = input.replace("\\", "");
                searchedString.setLength(0);
                searchedString.append(input);
            }
            var current = input.strip();
            list = resolveContext(current, list);
            if (list.isEmpty()) {
                list = originalList;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T>   resolveContext(String checkFor, List<T> list) {
        if (list.get(0) instanceof Student) {
            return (List<T>) studentContext(checkFor, (List<Student>) list);
        }
        if (list.get(0) instanceof Faculty) {
            return (List<T>) facultyContext(checkFor,(List<Faculty>) list);
        }
        if (list.get(0) instanceof Course) {
            return (List<T>) courseContext(checkFor, (List<Course>) list);
        }
        if (list.get(0) instanceof Session) {
            return (List<T>) sessionContext(checkFor, (List<Session>) list);
        }
        if (list.get(0) instanceof Exam) {
            return (List<T>) examContext(checkFor, (List<Exam>) list);
        }
        return List.of();
    }
    private static List<Student> studentContext(String check, List<Student> studentList){
        String _check = check.toLowerCase();
        if (List.of("cse", "ece", "mec", "civ", "eee", "che", "bme", "arc").contains(_check)){
            return studentList.stream().parallel().filter(
                    student -> student.getBranch().toLowerCase().contains(_check)
            ).toList();
        }
        return studentList.stream().parallel().filter(
                student -> student.getName().toLowerCase().contains(_check)
                        || student.getRollNo().toLowerCase().contains(_check)
        ).toList();
    }
    private static List<Faculty> facultyContext(String check, List<Faculty> facultyList){
        String _check = check.toLowerCase();
        if (List.of("cse", "ece", "mec", "civ", "eee", "che", "bme", "arc").contains(_check)){
            return facultyList.stream().parallel().filter(
                    student -> student.getDepartment().toLowerCase().contains(_check)
            ).toList();
        }
        return facultyList.stream().filter(
                faculty -> faculty.getName().toLowerCase().contains(_check)
                        || faculty.getEmpCode().toLowerCase().contains(_check)

        ).toList();
    }
    private static List<Course>  courseContext(String check, List<Course> courseList) {
        String _check = check.toLowerCase();
        return courseList.stream().filter(
                course -> course.getName().toLowerCase().contains(_check)
                        || course.getCode().toLowerCase().contains(_check)
        ).toList();
    }
    private static List<Session> sessionContext(String checkFor, List<Session> list) {
        String _check = checkFor.toLowerCase();
        return list.stream().filter(
                session -> session.getCourse().getCode().toLowerCase().contains(_check)
                || session.getCourse().getName().toLowerCase().contains(_check)
                || session.getTime().toString().contains(_check)
        ).sorted().toList();
    }
    private static List<Exam>    examContext(String checkFor, List<Exam> list) {
        String _check = checkFor.toLowerCase();
        return list.stream().filter(
                exam -> exam.getCourse().getCode().toLowerCase().contains(_check)
                        || exam.getCourse().getName().toLowerCase().contains(_check)
                        || exam.getExamDate().toString().contains(_check)
        ).sorted().toList();
    }

    public static <T> void print(List<T> tList ) {
        String padding = "%" + String.valueOf(tList.size()).length() + "s";
		int i = 1;
        for(var element : tList) {
            var index = String.format(padding, i++) + ". ";
            System.out.println(index + element);
        }
    }
    public static void printHeader(String s) {
        UniversityApp.makeClear();
		System.out.println("--------------------- Selection Panel ---------------------");
		System.out.println("IN : " + s + """
                       
                       '.' return
                       '/' filter relatively
                       '\\' filter absolutely""");
		System.out.println("-----------------------------------------------------------\n");
//        System.out.println("\n" + subfix + "\n");
    }
}
