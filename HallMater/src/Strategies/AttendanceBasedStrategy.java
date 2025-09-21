package Strategies;
import Exceptions.HallCapacityExceededException;
import Models.ExamHall;
import Models.Seat;
import Models.Student;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AttendanceBasedStrategy implements SeatingStrategy {
    private final double lowAttendanceThreshold;

    public AttendanceBasedStrategy(double lowAttendanceThreshold) {
        this.lowAttendanceThreshold = lowAttendanceThreshold;
    }

    @Override
    public void arrangeSeats(List<Student> students, ExamHall hall) throws HallCapacityExceededException {
        if (students.size() > hall.getCapacity()) {
            throw new HallCapacityExceededException("Students exceed hall capacity");
        }

        // Clear hall first
        hall.clearAllSeats();

        List<Student> lowAttendance = new ArrayList<>();
        List<Student> highAttendance = new ArrayList<>();

        for (Student s : students) {
            if (s.isLowAttendance(lowAttendanceThreshold)) {
                lowAttendance.add(s);
            } else {
                highAttendance.add(s);
            }
        }

        Comparator<Student> byRoll = Comparator.comparing(Student::getRollNumber);
        lowAttendance.sort(byRoll);
        highAttendance.sort(byRoll);

        // Low attendance students in front
        List<Student> orderedStudents = new ArrayList<>();
        orderedStudents.addAll(lowAttendance);
        orderedStudents.addAll(highAttendance);

        List<Seat> seats = hall.getSeats();
        for (int i = 0; i < orderedStudents.size(); i++) {
            seats.get(i).assignStudent(orderedStudents.get(i));
        }
    }

    @Override
    public String getStrategyName() {
        return "Attendance Based (Low attendance in front)";
    }
}