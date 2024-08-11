package app;

import app.academics.Course;
import app.academics.Exam;
import app.academics.FacultyCourses;
import app.academics.StudentCourses;
import app.admin.Faculty;
import app.admin.Student;
import app.faculty.Session;
import db.*;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Choices {
    private static final Scanner scanner = University.scanner ;
    public static Student getStudent(String headerDetail) {
        var students = StudentDB.getStudents().toList();
        return getChoice(students, headerDetail);
    }
    public static Faculty getFaculty(String headerDetail) {
        var faculties = FacultyDB.getFaculties().toList();
        return getChoice(faculties, headerDetail);
    }
    public static Course  getCourse (String headerDetail) {
        var allCourses = CourseDB.getCourses().toList();
        return getChoice(allCourses, headerDetail);
    }
    public static Course  getCourse (StudentCourses sc, String headerDetail) {
        var allCourses = sc.getCourses();
        return getChoice(allCourses,headerDetail);
    }
    public static Course  getCourse (FacultyCourses fc, String headerDetail) {
        var allCourses = fc.getCourses();
        return getChoice(allCourses, headerDetail);
    }
    public static Course  getCourse (int semester, String headerDetail) {
        var courses = CourseDB.getCourses(semester).toList();
        return getChoice(courses, headerDetail);
    }
    public static Exam    getExam(Course course, String headerDetail) {
        var exams = ExamDB.getExams(course).toList();
        if (exams.isEmpty()) {
            UniversityApp.getError(5);
            return null;
        }
        return getChoice(exams, headerDetail);
    }

    public static <T> T getChoice(List<T> originalList, String headerDetail) {
        var list = originalList;
        StringBuilder searchedString = new StringBuilder();
        while (true) {
            printHeader(headerDetail + " > selecting");
            if (list.isEmpty()) {
                System.out.println("No Results Found use, \\ to print all users");
            }
            print(list);
            System.out.println(list.size()+" results for : " + searchedString);
            System.out.print("Enter index (use '/' -> filter '.' -> back): ");

            if(scanner.hasNextInt()){
                int i = University.getIntegerFromInput();
                if (i < 0 || i > list.size()) {
                    UniversityApp.getError(6);
                    UniversityApp.holdNextSlide();
                    continue;
                }
                if (i == 0) {
                    return null;
                }
                return list.get(list.size() - i);
            }

            String input = University.getStringFromInput(true);

            if (input.contains(".")) {
                return null;
            }
            if (input.startsWith("/")) {
                input = input.replace("/", "");
                searchedString.append(" > ").append(input);
            }
            if (input.startsWith("\\")) {
                list = originalList;
                input = input.replace("\\", "");
                searchedString.setLength(0);
                searchedString.append(input);
                if (input.isBlank() || input.isEmpty()) {
                    continue;
                }
            }
            var current = input.strip();
            list = resolveContext(current, list);
            if(list.size() == 1) {
                return list.get(0);
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
            return studentList.parallelStream().filter(
                    student -> student.getBranch().toLowerCase().contains(_check)
            ).toList();
        }
        return studentList.parallelStream().filter(
                student -> student.getName().toLowerCase().contains(_check)
                        || student.getRollNo().toLowerCase().contains(_check)
        ).sorted(Comparator.comparing(student -> {
            String name = student.getName().toLowerCase();
            return name.indexOf(_check.toLowerCase()) == 0 ? 1 : 0;
        })).toList();
    }
    private static List<Faculty> facultyContext(String check, List<Faculty> facultyList){
        String _check = check.toLowerCase();
        if (List.of("cse", "ece", "mec", "civ", "eee", "che", "bme", "arc").contains(_check)){
            return facultyList.parallelStream().filter(
                    student -> student.getDepartment().toLowerCase().contains(_check)
            ).toList();
        }
        return facultyList.stream().filter(
                faculty -> faculty.getName().toLowerCase().contains(_check)
                        || faculty.getEmpCode().toLowerCase().contains(_check)
        ).sorted(Comparator.comparing(student -> {
            String name = student.getName().toLowerCase();
            return name.indexOf(_check.toLowerCase()) == 0 ? 1 : 0;
        })).toList();
    }
    private static List<Course>  courseContext(String check, List<Course> courseList) {
        String _check = check.toLowerCase();
        return courseList.stream().filter(
                course -> course.getName().toLowerCase().contains(_check)
                        || course.getCode().toLowerCase().contains(_check)
        ).sorted(Comparator.comparing(student -> {
            String name = student.getName().toLowerCase();
            return name.indexOf(_check.toLowerCase()) == 0 ? 1 : 0;
        })).toList();
    }
    private static List<Session> sessionContext(String checkFor, List<Session> list) {
        String _check = checkFor.toLowerCase();
        return list.stream().filter(
                session -> session.matchSession(_check)
        ).sorted().toList();
    }
    private static List<Exam>    examContext(String checkFor, List<Exam> list) {
        String _check = checkFor.toLowerCase();
        return list.stream().filter(
                exam -> exam.matchExam(_check)).sorted().toList();
    }

    public static <T> void print(List<T> tList ) {
        String padding = "%" + String.valueOf(tList.size()).length() + "s";
		int i = tList.size();
        StringBuilder sb = new StringBuilder();
        for(var element : tList) {
            var index = String.format(padding, i--) + ". ";
            sb.append(index).append(element).append("\n");
        }
        System.out.println(sb);
    }
    public static void printHeader(String s) {
        UniversityApp.makeClear();
		System.out.println("--------------------- Selection Panel ---------------------");
		System.out.println("IN : " + s + """
                       
                       '.' return
                       '/' filter relatively
                       '\\' filter absolutely""");
		System.out.println("-----------------------------------------------------------\n");
    }

}