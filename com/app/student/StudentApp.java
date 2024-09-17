package app.student;

import app.Choices;
import app.University;
import app.UniversityApp;
import app.admin.Student;
import db.UserHandlesDB;

public class StudentApp implements University {

    public StudentApp(){
        System.out.println("\n----------------------------------\n");
		System.out.println("\n\u001B[32m Welcome to Student App of " + UniversityApp.Name + " \u001B[0m\n");
		System.out.println("\n----------------------------------\n");
        display();
    }

    @Override
    public String display() {
        Student student = Choices.getStudent("Student App > login");
        if (student == null) {return null;}

        var studentUser = getStudentHandle(student);
        printHeader("Student App >"+studentUser.getStudent().getName()+" >login");
        int retryCount = 3;
        while (retryCount > 0) {
            if (studentUser.authenticate()) {
                break;
            }
            else{
                UniversityApp.getError(13);
                printHeader("Student App >"+studentUser.getStudent().getName()+" >login retries left("+(--retryCount)+")");
            }
        }
        if (retryCount == 0) {
            return null;
        }

        loop: while(true) {
            printHeader(student.getName() + " is logged in");
            showStudentMenu();
            switch (University.getKeyPress()) {
                case 1 -> studentUser.seeAttendance();
                case 2 -> studentUser.seeExam();
                case 3 -> studentUser.generateReport();
                case 4 -> studentUser.generateFullReport();
                case 0 -> {break loop;}
                default-> UniversityApp.getError(6);
            }
        }
        return null;
    }

    // Utility Functions

    public static void addNewStudent(Student student) {
        var passwordArray = University.getPasswordFromInput();
        UserHandlesDB.add(student, passwordArray);
    }

    public static void removeStudent(Student student) {
        UserHandlesDB.remove(student);
    }

    // Getters

    private StudentUser getStudentHandle(Student student) {
        return UserHandlesDB.getHandle(student);
    }

    private boolean authenticate(StudentUser userHandle) {
        int retryCount = 3;
        while (retryCount > 0) {
            if (userHandle != null && userHandle.authenticate()) {
                return true;
            }
            else{
                UniversityApp.getError(13);
                System.out.println("retries left : " + (--retryCount));
                UniversityApp.holdNextSlide();
            }
        }
        UniversityApp.getError(15);
        return false;
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
