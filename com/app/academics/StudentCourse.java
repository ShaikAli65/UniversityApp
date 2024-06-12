package app.academics;

import app.University;
import app.UniversityApp;
import app.admin.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StudentCourse implements Serializable
{
	// Structure to store the courses registered by the student
	final List<Course> courses;

	private int count;
	private final int noCourses;

	public StudentCourse(Student student) {
		noCourses = student.getNoCourses();
		courses = new ArrayList<>(noCourses);
		count = 0;
	}

	// Course methods

	public void add(Course c) {
		if(noCourses == 0) {
			System.out.println("Choose number of subjects to register in this semester\n");
		}
		if( count <= noCourses)
		{
			if (courses.contains(c)) {
				System.out.println("Course already registered\n");
				return;
			}
			courses.add(c);
			count++;
		}
	}

	public void remove(Course c) {
		if (count > 0) {
			if (!courses.contains(c)) {
				System.out.println("Course not registered\n");
				return;
			}
			courses.remove(c);
			count--;
		}
	}

	public boolean contains(Course c)
	{
		return courses.contains(c);
	}

	// Utility methods
	@Override
	public String toString() {
		return display();
	}

	public String display() {
		StringBuilder result = new StringBuilder("\nRegistered Courses :\n\n");
		int i = 1;
		for (Course course : courses) {
			result.append(i).append(". ").append(course.toString()).append("\n");
		}
		return result.toString();
	}

	// getters

	public Course getCourseChoice() {

		if (courses.isEmpty()) {
			UniversityApp.getError(11);
			return null;
		}

		// Display the list of courses
		System.out.println("Courses registered by the student\n");
		int index = 1;
		for (Course course : courses) {
			System.out.println(index + ". " + course.toString());
			index++;
		}

		// Get user input for course choice
		System.out.print("\nEnter the course index or zero to return: ");
		int choiceIndex = University.getIntegerFromInput() - 1;

		// Validate user input
		if (choiceIndex < 0 || choiceIndex >= courses.size()) {
			System.out.println("Invalid choice");
			return null;
		}

		Course[] courseArray = courses.toArray(new Course[0]);
		return courseArray[choiceIndex];
	}

	public Stream<Course> getCourses() {
		return courses.stream();
	}
	public Course getCourse(String code) {
		return courses.stream()
				.filter(course -> course.getCode().equals(code))
				.findFirst()
				.orElse(null);
	}
	public int getNoCourses() {
		return noCourses;
	}

}