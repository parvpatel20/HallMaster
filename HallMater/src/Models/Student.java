package Models;
import java.util.Objects;

public class Student {
    private final String id;
    private final String name;
    private final String rollNumber;
    private final String department;
    private final String branch;
    private final double attendancePercentage;
    private final String SubjectCode; // Which exam this student is appearing for

    public Student(String id, String name, String rollNumber, String department, String branch, double attendancePercentage, String SubjectCode) {
        this.id = id;
        this.name = name;
        this.rollNumber = rollNumber;
        this.department = department;
        this.branch = branch;
        this.attendancePercentage = attendancePercentage;
        this.SubjectCode = SubjectCode;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getRollNumber() { return rollNumber; }
    public String getDepartment() { return department; }
    public String getBranch() { return branch; }
    public double getAttendancePercentage() { return attendancePercentage; }
    public String getExamSubject() { return SubjectCode; }

    public boolean isLowAttendance(double threshold) {
        return attendancePercentage < threshold;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)[%s-%s]%.1f%%", name, rollNumber, branch, SubjectCode, attendancePercentage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student s = (Student) o;
        return Objects.equals(id, s.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}