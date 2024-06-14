package db;

import app.academics.Course;
import app.admin.Student;

import java.io.*;
import java.util.HashMap;

public class AttendanceDB {
    // String = StudentId (roll no)
    static HashMap<String, entry> entries = new HashMap<>();
    static Boolean changed = false;
    public static void add(Student st, Course course, boolean attendance) {
        changed = true;
        entry _entry = entries.computeIfAbsent(st.getRollNo(), k ->new entry(course));
        _entry.add(course, attendance);
    }
    public static String getEntry(Student s) {

        if (!entries.containsKey(s.getRollNo())) return "No Attendance Data Found";
        return entries.get(s.getRollNo()).toString();
    }
    public static void update(Student st, Course course, boolean what) {
        changed = true;
        var entry = entries.computeIfAbsent(st.getRollNo(), k -> new entry(course));
        entry.update(course, what);
    }
    public static void delete(Student st, Course c, boolean what) {
        changed = true;
        var entry = entries.computeIfAbsent(st.getRollNo(), k -> new entry(c));
        entry.delete(c, what);
    }
    public static void remove(Student st) {
        changed = true;
        entries.remove(st.getRollNo());
    }
    public static void delete(Course c) {changed = true;entries.values().forEach(entry -> entry.remove(c));}
    @SuppressWarnings("unchecked")
    public static void loadDatabase(String attendanceFile) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(attendanceFile))) {
            entries = (HashMap<String, entry>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException ignored) {}
    }
    public static void saveData(String attendanceFile) {
        if (!changed) return;
        try {
                FileWriter writer = new FileWriter(attendanceFile);
                writer.write("");
                writer.close();
            } catch (IOException ignore) {}

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(attendanceFile))) {
            outputStream.writeObject(entries);
        } catch (IOException ignored) {}
        System.out.println("Attendance Data Saved");
    }
}

class entry implements Serializable {
        @Serial
    private static final long serialVersionUID = 1L;
        // String = CourseCode
    private final HashMap<String, subEntry> attendance = new HashMap<>();

    private static class subEntry implements Serializable {
        int attended, conducted;

        @Override
        public String toString() {
            var percentage = conducted == 0 ? 0 : (attended * 100) / conducted;
            var stringPadded = String.format("%8s", String.valueOf(attended) + '/' + conducted);
            return stringPadded + "(" + percentage +"%)";
        }
    }

    public entry(Course c) {
//            attendance = new HashMap<>();
        attendance.put(c.getCode(), new subEntry());
    }
    public void add(Course c, boolean what) {
        var subEntry = attendance.computeIfAbsent(c.getCode(), k -> new subEntry());
        subEntry.attended += what ? 1 : 0;
        subEntry.conducted += 1;
    }
    public String getAttendance(Course c) {
        return attendance.get(c.getCode()).toString();
    }
    public String getAttendance() {
        StringBuilder sb = new StringBuilder();
        for (var element : attendance.entrySet()) {
            String courseCodePadded = String.format("%-6s", element.getKey());
                    sb.append(courseCodePadded)
                    .append(" Attendance ")
                    .append(element.getValue())  // SubEntry P/A(%)
                    .append("\n");
        }
        return sb.toString();
    }
    public void update(Course c, boolean what) {
        var sub_entry = attendance.get(c.getCode());
        sub_entry.attended += what ? 1 : Math.max(-1, -sub_entry.attended);
    }
    public void delete(Course c, boolean what) {
        var sub_entry = attendance.get(c.getCode());
        sub_entry.attended -=  what ? Math.min(1, sub_entry.attended) : 0;
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
