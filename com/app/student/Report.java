package app.student;

import app.Date;
import app.academics.Course;
import app.academics.StudentCourse;


public class Report
{
    private final Course course;
    private final Date date;
    private final int attendance;
    private final int totalSessions;
    private final int totalExams;
    private final int totalMarks;
    private final int totalCredits;

    public Report(StudentCourse studentCourse, Course course, Date date, int attendance, int totalSessions, int totalExams, int totalMarks, int totalCredits) {
        this.course = course;
        this.date = date;
        this.attendance = attendance;
        this.totalSessions = totalSessions;
        this.totalExams = totalExams;
        this.totalMarks = totalMarks;
        this.totalCredits = totalCredits;
    }

    public void printReport() {
        printHeader();
        System.out.println("Course: " + course);
        System.out.println("Date: " + date);
        System.out.println("Attendance: " + attendance + "/" + totalSessions);
        System.out.println("Exams: " + totalExams);
        System.out.println("Marks: " + totalMarks);
        System.out.println("Credits: " + totalCredits);
    }

    private void printHeader() {
        System.out.println("""


                Report""");
        System.out.println("=".repeat("Report".length()));
    }
}