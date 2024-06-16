package app.academics;

import app.admin.Student;
import db.CourseDB;

import java.io.*;
import java.util.HashSet;
import java.util.List;

public class StudentCourses implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	private transient HashSet<Course> courses;

	private int count;
	private final int noCourses;

	public StudentCourses(Student student) {
		noCourses = student.getNoCourses();
		courses = new HashSet<>(noCourses);
		count = 0;
	}
	public StudentCourses() {
		noCourses = 0;
		courses = new HashSet<>();
	}
	public StudentCourses(Student student, HashSet<Course> courses) {
		noCourses = student.getNoCourses();
		this.courses = courses;
		count = courses.size();
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
		StringBuilder result = new StringBuilder();
		String padding = "%" + String.valueOf(this.getNoCourses()).length() + "s";
		int i = 1;
		for(var course : courses) {
			result.append(String.format(padding, i)).append(". ").append(course).append('\n');
			i++;
		}
		return result.toString();
	}

	public void display() {
		System.out.println(this);
	}

	public List<Course> getCourses() {
		return courses.stream().toList();
	}
	public Course getCourse(String code) {
		return courses.stream()
				.filter(course -> course.getCode().equalsIgnoreCase(code))
				.findFirst()
				.orElse(null);
	}
	public int getNoCourses() {
		return noCourses;
	}
    @Serial
	private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeInt(courses.size());
        for (Course course : courses) {
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