package Strategies;
import Exceptions.HallCapacityExceededException;
import Models.ExamHall;
import Models.Student;
import java.util.List;

public interface SeatingStrategy {
    void arrangeSeats(List<Student> students, ExamHall hall) throws HallCapacityExceededException;
    String getStrategyName();
}