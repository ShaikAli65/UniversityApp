package db;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

import app.academics.Course;
import app.admin.Student;

public class AttendanceDB {
    // String = StudentId (roll no)
    static HashMap<String, entry> entries = new HashMap<>();
    static Boolean changed = false;

    public static class entry implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        // String = CourseCode
        private final HashMap<String, subEntry> attendance = new HashMap<>();

        public static class subEntry implements Serializable {
            int attended, conducted;

            @Override
            public String toString() {
                var percentage = getPercentage();
                var stringPadded = String.format("%8s", String.valueOf(attended) + '/' + conducted);
                return stringPadded + "(" + percentage + "%)";
            }

            public float getPercentage() {
                return conducted == 0 ? 0 : (float) (attended * 100) / conducted;
            }

            public int getAttended() {
                return attended;
            }

            public int getConducted() {
                return conducted;
            }
        }

        public entry(String cCode) {
            attendance.put(cCode, new subEntry());
        }

        public void add(String c, boolean what) {
            var subEntry = attendance.computeIfAbsent(c, k -> new subEntry());
            subEntry.attended += what ? 1 : 0;
            subEntry.conducted += 1;
        }

        public subEntry getAttendance(Course c) {
            return attendance.get(c.getCode());
        }

        public String getAttendance() {
            StringBuilder sb = new StringBuilder();
            for (var element : attendance.entrySet()) {
                String courseCodePadded = String.format("%-6s", element.getKey());
                sb.append(courseCodePadded)
                        .append(" Attendance ")
                        .append(element.getValue()) // SubEntry P/A(%)
                        .append("\n");
            }
            return sb.toString();
        }

        public void update(String cCode, boolean what) {
            var sub_entry = attendance.get(cCode);
            sub_entry.attended += what ? 1 : Math.max(-1, -sub_entry.attended);
        }

        public void delete(String cCode, boolean what) {
            var sub_entry = attendance.get(cCode);
            sub_entry.attended -= what ? Math.min(1, sub_entry.attended) : 0;
            sub_entry.conducted -= Math.min(1, sub_entry.conducted);
        }

        public void remove(Course c) {
            attendance.remove(c.getCode());
        }

        @Override
        public String toString() {
            return getAttendance();
        }
    }

    public static void add(Student st, String course, boolean attendance) {
        changed = true;
        entry _entry = entries.computeIfAbsent(st.getRollNo(), k -> new entry(course));
        _entry.add(course, attendance);
    }

    public static entry getEntry(Student s) {
        if (!entries.containsKey(s.getRollNo()))
            return null;
        return entries.get(s.getRollNo());
    }

    public static void update(Student st, String courseCode, boolean what) {
        changed = true;
        var entry = entries.computeIfAbsent(st.getRollNo(), k -> new entry(courseCode));
        entry.update(courseCode, what);
    }

    public static void delete(Student st, String cCode, boolean what) {
        changed = true;
        var entry = entries.computeIfAbsent(st.getRollNo(), k -> new entry(cCode));
        entry.delete(cCode, what);
    }

    public static void remove(Student st) {
        changed = true;
        entries.remove(st.getRollNo());
    }

    public static void delete(Course c) {
        changed = true;
        entries.values().forEach(entry -> entry.remove(c));
    }

    @SuppressWarnings("unchecked")
    public static void loadDatabase(String attendanceFile) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(attendanceFile))) {
            entries = (HashMap<String, entry>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException ignored) {
        }
    }

    public static void saveData(String attendanceFile) {
        if (!changed)
            return;
        try {
            FileWriter writer = new FileWriter(attendanceFile);
            writer.write("");
            writer.close();
        } catch (IOException ignore) {
        }

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(attendanceFile))) {
            outputStream.writeObject(entries);
        } catch (IOException ignored) {
        }
        System.out.println("Attendance Data Saved");
    }
}