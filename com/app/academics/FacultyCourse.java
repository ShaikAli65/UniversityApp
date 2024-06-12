package app.academics;

import app.University;
import app.UniversityApp;
import app.admin.Faculty;

import java.io.Serializable;
import java.util.HashSet;

public class FacultyCourse implements Serializable
{
	// Structure to store
	final HashSet<Course> courses;

	private int count;
	private final int no_courses;

	public FacultyCourse(Faculty f) {
		no_courses = f.getNoCourses();
		courses = new HashSet<>(no_courses);
		count = 0;
	}
	public FacultyCourse(Faculty f, HashSet<Course> c) {
		no_courses = f.getNoCourses();
		courses = c;
		count = c.size();
	}

	// Utility Functions

	public void add(Course c) {
		if(no_courses == 0) {
			System.out.println("Choose number of subjects to register in this semester\n");
		}
		if( count <= no_courses)
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

	public boolean contains(Course c) {
		return courses.contains(c);
	}

	// Getters

	public Course getCourseChoice() {
		System.out.println("Courses taught by You : \n");
		int i = 1;
		for(Course c: courses) {
			System.out.println(i + ". " + c.toString());
			i++;
		}
		System.out.print("\nEnter the course index to select : ");
		int ch = University.getIntegerFromInput();
		if(ch > courses.size() || ch < 1)
		{
			UniversityApp.getError(6);
			return null;
		}
		return (Course) courses.toArray()[ch-1];
	}

	public boolean hasCourse(Course course) {
		return courses.contains(course);
	}
	// Display Functions
	@Override
	public String toString() {
		StringBuilder ret= new StringBuilder("\nTeaching Courses : \n\n");
		int i = 1;
        for (Course value : courses) {
            ret.append(i).append(". ").append(value.toString()).append("\n");
			i++;
        }
		return ret.toString();
	}
}