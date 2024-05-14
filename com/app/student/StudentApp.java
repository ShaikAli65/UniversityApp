package app.student;

import app.University;
import app.UniversityApp;
import app.academics.AcademicsApp;
import app.admin.AdminApp;
import app.admin.Student;
import java.util.HashMap;


public class StudentApp implements University {

    final private static HashMap<Student, StudentUser> studentUserHashMap = new HashMap<>();

    public StudentApp(){
        System.out.println("\n----------------------------------\n");
		System.out.println("\n\u001B[32m Welcome to Student App of " + UniversityApp.Name + " \u001B[0m\n");
		System.out.println("\n----------------------------------\n");
        display();
    }

    @Override
    public String display() {
        Student student = AdminApp.GetStudentChoice();
        if (student == null) {return null;}

        var studentUser = getStudentHandle(student);
        if (studentUser == null) {UniversityApp.getError(15);return null;}

        var studentCourse = AcademicsApp.getStudentCourse(student);
        if (studentCourse == null) {UniversityApp.getError(3);return null;}

        loop: while(true) {
            printHeader(student.getName() + " is logged in");
            showStudentMenu();
            switch (University.getkeyPress()) {
                case 1 -> studentUser.seeAttendance(studentCourse);
                case 2 -> studentUser.seeExam(studentCourse);
                case 3 -> studentUser.generateReport(studentCourse);
                case 4 -> studentUser.generateFullReport();
                case 0 -> {break loop;}
                default-> UniversityApp.getError(6);
            }
        }
        return null;
    }

    // Utility Functions

    public static void AddNewStudent(Student student) {
        studentUserHashMap.put(student, new StudentUser(student));
    }

    public static void RemoveStudent(Student student) {
        studentUserHashMap.remove(student);
    }

    // Getters

    private StudentUser getStudentHandle(Student student) {
        StudentUser userHandle = studentUserHashMap.get(student);
        int retryCount = 3;

        while (retryCount > 0) {
            if (userHandle != null && userHandle.authenticate()) {
                return userHandle;
            }
            else{
                UniversityApp.getError(13);
                UniversityApp.holdNextSlide();
                System.out.print("retries left : " + (--retryCount));
            }
        }

        return null;
    }

    // Display Functions

    private void showStudentMenu() {
        System.out.print("""
                Choose :\s
                \t\t1. See Attendance
                \t\t2. See Exam
                \t\t3. Get Report (Developing)
                \t\t4. Get Full Report (Developing)
                \t\t0. Return to University App
                \t:""");
    }

    @Override
    public void printHeader(String out) {
        UniversityApp.makeClear();
        System.out.println("-----------------Student App-----------------");
        System.out.println("IN :" + out);
        System.out.println("---------------------------------------------\n");
    }
}
