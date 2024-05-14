package app.academics;
import app.University;
import app.UniversityApp;
import app.admin.*;

import java.util.ArrayList;

public class FacultyCourse
{
	// Structure to store
	final ArrayList<Course> course;

	private int count;
	private final int no_courses;

	FacultyCourse(Faculty f) {
		no_courses = f.getNoCourses();
		course = new ArrayList<>(no_courses);
		count = 0;
	}

	// Utility Functions

	public void add(Course c) {
		if(no_courses == 0) {
			System.out.println("Choose number of subjects to register in this semester\n");
		}
		if( count <= no_courses)
		{
			if (course.contains(c)) {
				System.out.println("Course already registered\n");
				return;
			}
			course.add(c);
			count++;
		}
	}

	public void remove(Course c) {
		if (count > 0) {
			if (!course.contains(c)) {
				System.out.println("Course not registered\n");
				return;
		}
			course.remove(c);
			count--;
		}
	}

	public boolean contains(Course c) {
		return course.contains(c);
	}

	// Getters

	public Course getCourseChoice() {
		System.out.println("Courses taught by You : \n");
		int i = 1;
		for(Course c: course) {
			System.out.println(i + ". " + c.toString());
			i++;
		}
		System.out.print("\nEnter the course index to select : ");
		int ch = University.getIntegerFromInput();
		if(ch > course.size() || ch < 1)
		{
			UniversityApp.getError(6);
			return null;
		}
		return course.get(ch-1);
	}

	// Display Functions
	@Override
	public String toString() {
		StringBuilder ret= new StringBuilder("\nTeaching Courses : \n\n");
		int i = 1;
        for (Course value : course) {
            ret.append(i).append(". ").append(value.toString()).append("\n");
			i++;
        }
		return ret.toString();
	}
}