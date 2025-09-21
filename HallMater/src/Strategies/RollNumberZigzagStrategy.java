package Strategies;
import Exceptions.HallCapacityExceededException;
import Models.ExamHall;
import Models.Seat;
import Models.Student;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RollNumberZigzagStrategy implements SeatingStrategy {

    @Override
    public void arrangeSeats(List<Student> students, ExamHall hall) throws HallCapacityExceededException {
        if (students.size() > hall.getCapacity()) {
            throw new HallCapacityExceededException("Students exceed hall capacity");
        }

        // Sort students by roll number
        List<Student> sorted = new ArrayList<>(students);
        Comparator<Student> byRoll = Comparator.comparing(Student::getRollNumber);
        sorted.sort(byRoll);

        List<Seat> seats = hall.getSeats();
        List<Student> arrangement = new ArrayList<>();

        // Create zigzag pattern: alternate between low and high roll numbers
        int left = 0, right = sorted.size() - 1;
        boolean pickFromLeft = true;

        while (left <= right) {
            if (pickFromLeft) {
                arrangement.add(sorted.get(left++));
            } else {
                arrangement.add(sorted.get(right--));
            }
            pickFromLeft = !pickFromLeft;
        }

        // Assign to seats in row-major order
        for (int i = 0; i < seats.size(); i++) {
            seats.get(i).assignStudent(i < arrangement.size() ? arrangement.get(i) : null);
        }
    }

    @Override
    public String getStrategyName() {
        return "Roll Number Zigzag (Alternating pattern)";
    }
}
