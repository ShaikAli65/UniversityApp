package app.academics;

import app.University;
import app.UniversityApp;
import app.admin.*;

import java.util.HashMap;
import java.util.stream.Stream;

public class StudentCourse
{
	//Structure to store the courses registered by the student
	final HashMap<Course, String> courses;

	private int count;
	private final int noCourses;

	StudentCourse(Student student) {
		noCourses = student.getNoCourses();
		courses = new HashMap<>(noCourses);
		count = 0;
	}

	// Attendance methods

	public void addAttendance(Course course, boolean attendance) {
		if (courses.containsKey(course)) {
			String[] presentConducted = courses.get(course).split("/");
			courses.put(course, (Integer.parseInt(presentConducted[0]) + (attendance ? 1 : 0))
					+ "/" + (Integer.parseInt(presentConducted[1]) + 1));
		}
	}

	public void removeAttendance(Course course, boolean attendance) {
		if (courses.containsKey(course)) {
			String[] presentConducted = courses.get(course).split("/");
			int present = Integer.parseInt(presentConducted[0]) - (attendance ? 1 : 0);
			int conducted = Integer.parseInt(presentConducted[1]) - 1;

			courses.put(course, present + "/" + conducted);
		}
	}

	public void toggleAttendance(Course course) {
		if (courses.containsKey(course)) {
			String[] presentConducted = courses.get(course).split("/");
			int present = Integer.parseInt(presentConducted[0]);
			int conducted = Integer.parseInt(presentConducted[1]);
			courses.put(course, (conducted - present) + "/" + conducted);
		}
	}

	public void editAttendance(Course course, boolean attendance) {
		if (courses.containsKey(course)) {
			String[] presentConducted = courses.get(course).split("/");
			int present = Integer.parseInt(presentConducted[0]);
			int conducted = Integer.parseInt(presentConducted[1]);
			courses.put(course, (attendance ? present + 1 : present - 1) + "/" + conducted);
		}
	}

	// Course methods

	public void add(Course c) {
		if(noCourses == 0) {
			System.out.println("Choose number of subjects to register in this semester\n");
		}
		if( count < noCourses)
		{
			if (courses.containsKey(c)) {
				System.out.println("Course already registered\n");
				return;
			}
			courses.put(c, "0/0");
			count++;
		}
	}

	public void remove(Course c) {
		if (count > 0) {
			if (!courses.containsKey(c)) {
				System.out.println("Course not registered\n");
				return;
			}
			courses.remove(c);
		}
	}

	public boolean contains(Course c) {
		return courses.containsKey(c);
	}

	// Utility methods
	@Override
	public String toString() {
		return display();
	}

	public String display() {
		StringBuilder result = new StringBuilder("\nRegistered Courses :\n\n");
		int i = 1;
		for (Course course : courses.keySet()) {
			result.append(i).append(". ").append(course.toString()).append("\n");
		}
		return result.toString();
	}

	// getters

	public Stream<Course> getCourses() {
		return courses.keySet().stream();
	}

	public String getAttendance(Course course) {
		if (courses.containsKey(course)) {
			String[] presentConducted = courses.get(course).split("/");
			int present = Integer.parseInt(presentConducted[0]);
			int conducted = Integer.parseInt(presentConducted[1]);
			return present + "/" + conducted + " (" + (conducted == 0 ? 0 : (present * 100) / conducted) + "%)";
		}
        return "0/0 (0%)";
}

	public Course getCourseChoice() {

		if (courses.isEmpty()) {
			UniversityApp.getError(11);
			return null;
		}

		// Display the list of courses
		System.out.println("Courses registered by the student\n");
		int index = 1;
		for (Course course : courses.keySet()) {
			System.out.println(index + ". " + course.toString());
			index++;
		}

		// Get user input for course choice
		System.out.print("\nEnter the course index: ");
		int choiceIndex = University.getIntegerFromInput() - 1;

		// Validate user input
		if (choiceIndex < 0 || choiceIndex >= courses.size()) {
			System.out.println("Invalid choice");
			return null;
		}

		Course[] courseArray = courses.keySet().toArray(new Course[0]);
		return courseArray[choiceIndex];

	}

	public int getNoCourses() {
		return noCourses;
	}

}