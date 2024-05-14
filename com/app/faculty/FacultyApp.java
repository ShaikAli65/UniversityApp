package app.faculty;

import app.*;
import app.academics.AcademicsApp;
import app.admin.*;

import java.util.*;


public class FacultyApp implements University {

    final private static HashMap<Faculty, FacultyUser> FacultyUserHashMap = new HashMap<>();

    public FacultyApp() {
        System.out.println("\n----------------------------------\n");
		System.out.println("\n\u001B[32m Welcome to Faculty App of " + UniversityApp.Name + " \u001B[0m\n");
		System.out.println("\n----------------------------------\n");
        display();
    }

    @Override
    public String display() {

        Faculty faculty = AdminApp.GetFacultyChoice();
        if (faculty == null) {return null;}

        var facultyUser = getFacultyHandle(faculty);
        if (facultyUser == null) {UniversityApp.getError(15);return null;}

        var facultyCourse = AcademicsApp.getFacultyCourse(faculty);
        if (facultyCourse == null) {UniversityApp.getError(4);return null;}

        loop: while(true) {
            printHeader(faculty.getName() + " is logged in");
            ShowFacultyAppMenu();
            switch (University.getkeyPress()) {
                case 1 -> facultyUser.addSession(facultyCourse);
                case 2 -> facultyUser.enterExamData(facultyCourse);
                case 3 -> facultyUser.deleteSession();
                case 4 -> facultyUser.deleteExam();
                case 5 -> facultyUser.updateSession();
                case 6 -> facultyUser.updateExam();
                case 7 -> {
                    facultyUser.displaySessions();
                    UniversityApp.holdNextSlide();
                }
                case 8 -> {
                    facultyUser.displayExams();
                    UniversityApp.holdNextSlide();
                }
                case 0 -> {break loop;}
                default-> UniversityApp.getError(6);
            }
        }
        return null;
    }

    // Utility Functions

    public static void AddNewFaculty(Faculty faculty) {
        FacultyUserHashMap.put(faculty, new FacultyUser(faculty));
    }

    public static void RemoveFaculty(Faculty faculty) {
        FacultyUserHashMap.remove(faculty);
    }

    // Getters and Setters

    public static FacultyUser getFacultyUser(Faculty faculty) {
        return FacultyUserHashMap.get(faculty);
    }

    private FacultyUser getFacultyHandle(Faculty faculty) {
        FacultyUser userHandle = FacultyUserHashMap.get(faculty);
        int retryCount = 3;

        while (retryCount > 0) {
            if (userHandle != null && userHandle.authenticate()) {
                return userHandle;
            }
            else {
                UniversityApp.getError(13);
                UniversityApp.holdNextSlide();
                System.out.print("retries left : " + (--retryCount));
            }
        }

        return null;
}

    // Print Functions

    private static void ShowFacultyAppMenu() {
        System.out.print("""
                Choose :\s
                \t\t 1. Add Session
                \t\t 2. Add Exam
                \t\t 3. Delete Session
                \t\t 4. Delete Exam
                \t\t 5. Update Session
                \t\t 6. Update Exam
                \t\t 7. Display Sessions
                \t\t 8. Display Exams
                \t\t 0. Return to University App
                \t:"""
        );
    }

    @Override
    public void printHeader(String out) {
        UniversityApp.makeClear();
        System.out.println("-----------------Faculty App-----------------");
        System.out.println("IN :" + out);
        System.out.println("---------------------------------------------\n");
    }

}

