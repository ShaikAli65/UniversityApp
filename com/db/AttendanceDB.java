package db;

import app.academics.Course;
import app.admin.Student;

import java.util.HashMap;

public class AttendanceDB {
    static HashMap<Student, entry> entries = new HashMap<>();

    public static class entry{
        HashMap<Course, subEntry> attendence = new HashMap<>();
        public entry(Course c) {
            attendence.put(c, new subEntry());
        }
        public void add(Course c, boolean what) {
             var sub_entry = attendence.get(c);
            sub_entry.attended += what ? 1 : 0;
            sub_entry.conducted += 1;
        }
        public void update(Course c, boolean what) {
            var sub_entry = attendence.get(c);
            sub_entry.attended += what ? 1 : Math.max(-1, -sub_entry.attended);
        }
        public void delete(Course c, boolean what) {
            var sub_entry = attendence.get(c);
            sub_entry.attended -=  what ? Math.min(1, sub_entry.attended) : 0;
            sub_entry.conducted -= Math.min(1, sub_entry.conducted);
        }

        public String getAttendance(Course c) {
            return attendence.get(c).toString();
        }
        public String getAttendance() {
            StringBuilder sb = new StringBuilder();
            int i = 1;
            for (var element : attendence.entrySet()) {
                sb.append(i)
                        .append(". ")
                        .append(element.getKey())
                        .append(" Attendance ")
                        .append(element.getValue())
                        .append("\n");
                i++;
            }
            return sb.toString();
        }
        private static class subEntry {
            int attended, conducted;

            @Override
            public String toString() {
                var percentage = conducted == 0 ? 0 : (attended * 100) / conducted;
                return attended + '/' + conducted + "(" + percentage +"%)";
            }
        }
    }
    public static void add(Student st, Course course, boolean attendance) {
        var entry = entries.computeIfAbsent(st, k -> new entry(course));
        entry.add(course, attendance);
    }
    public static entry getEntry(Student s) {
        return entries.get(s);
    }
    public static void update(Student st, Course course, boolean what) {
        var entry = entries.computeIfAbsent(st, k -> new entry(course));
        entry.update(course, what);
    }
    public static void delete(Student st, Course c, boolean what) {
        var entry = entries.computeIfAbsent(st, k -> new entry(c));
        entry.delete(c, what);
    }

}

