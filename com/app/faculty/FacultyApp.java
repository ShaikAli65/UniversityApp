package app.faculty;

import app.Choices;
import app.University;
import app.UniversityApp;
import app.admin.Faculty;
import db.UserHandlesDB;


public class FacultyApp implements University {


    public FacultyApp() {
        System.out.println("\n----------------------------------\n");
		System.out.println("\n\u001B[32m Welcome to Faculty App of " + UniversityApp.Name + " \u001B[0m\n");
		System.out.println("\n----------------------------------\n");
        display();
    }

    @Override
    public String display() {

        Faculty faculty = Choices.getFaculty("FacultyApp > Logging");
        if (faculty == null) {return null;}
        printHeader("logging > " + faculty.getName());
        var facultyUser = getFacultyHandle(faculty);
        if (facultyUser == null) {UniversityApp.getError(15);return null;}

        loop: while(true) {
            printHeader(faculty.getName() + " is logged in");
            ShowFacultyAppMenu();
            switch (University.getKeyPress()) {
                case 1 -> facultyUser.addSession();
                case 2 -> facultyUser.enterExamData();
                case 3 -> facultyUser.deleteSession();
                case 4 -> facultyUser.deleteExam();
                case 5 -> facultyUser.updateSession();
                case 6 -> facultyUser.updateExam();
                case 7 -> facultyUser.displaySessions();
                case 8 -> facultyUser.displayExams();
                case 0 -> {break loop;}
                default-> UniversityApp.getError(6);
            }
        }
        return null;
    }

    // Utility Functions

    public static void AddNewFaculty(Faculty faculty) {
        var passwordArray = University.getPasswordFromInput();
        UserHandlesDB.add(faculty, passwordArray);
    }

    public static void remove(Faculty faculty) {
        UserHandlesDB.remove(faculty);
    }

    // Getters and Setters

    private FacultyUser getFacultyHandle(Faculty faculty) {
        FacultyUser userHandle = UserHandlesDB.getHandle(faculty);
        return authenticate(userHandle);
    }

    private FacultyUser authenticate(FacultyUser userHandle) {
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

