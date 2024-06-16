package app.academics;

import app.admin.Faculty;
import db.CourseDB;

import java.io.*;
import java.util.HashSet;
import java.util.List;

public class FacultyCourses implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
    private transient HashSet<Course> courses;

	private int count;
	private final int noCourses;

	public FacultyCourses(Faculty f) {
		noCourses = f.getNoCourses();
		courses = new HashSet<>(noCourses);
		count = 0;
	}
	public FacultyCourses() {
		noCourses = 0;
		courses = new HashSet<>();
	}
	public FacultyCourses(Faculty f, HashSet<Course> c) {
		noCourses = f.getNoCourses();
		courses = c;
		count = c.size();
	}

	// Utility Functions

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

	public boolean contains(Course c) {
		return courses.contains(c);
	}
	public boolean contains(String cCode) {
		var c = CourseDB.get(cCode);
		return courses.contains(c);
	}
	// Getters

	public boolean hasCourse(Course course) {
		return courses.contains(course);
	}
	public List<Course> getCourses() {
		return courses.stream().toList();
	}

	public int getNoCourses() {
		return noCourses;
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
    @Serial
	private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
		assert courses != null;
        oos.writeInt(courses.size());
        for (Course course : courses) {
			assert course != null;
            oos.writeObject(course.getCode());
        }
    }

    @Serial
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        int size = ois.readInt();
        this.courses = new HashSet<>(size);
        for (int i = 0; i < size; i++) {
            String courseCode = (String) ois.readObject();
            Course course = CourseDB.get(courseCode);
            courses.add(course);
        }
    }
}