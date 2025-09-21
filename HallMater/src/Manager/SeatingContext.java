package Manager;
import java.util.List;
import java.util.Map;
import Models.*;
import Exceptions.*;
import Strategies.SeatingStrategy;

public class SeatingContext {

    public void generateMultiHallSeating(List<Student> students, Map<ExamHall, SeatingStrategy> hallStrategyMap) {
        int totalCapacity = 0;
        for (ExamHall hall : hallStrategyMap.keySet()) {
            totalCapacity += hall.getCapacity();
        }

        if (students.size() > totalCapacity) {
            throw new HallCapacityExceededException("Total students exceed combined hall capacity");
        }

        // Distribute students across halls based on capacity
        List<Student> remainingStudents = List.copyOf(students);
        int studentIndex = 0;

        // var = Map.entry<ExamHall, SeatingStrategy>

        for (var entry : hallStrategyMap.entrySet()) {
            ExamHall hall = entry.getKey();
            SeatingStrategy strategy = entry.getValue();

            int hallCapacity = hall.getCapacity();
            int studentsForThisHall = Math.min(hallCapacity, remainingStudents.size() - studentIndex);

            if (studentsForThisHall > 0) {
                List<Student> hallStudents = remainingStudents.subList(studentIndex, studentIndex + studentsForThisHall);
                strategy.arrangeSeats(hallStudents, hall); // called concrete class method.
                studentIndex += studentsForThisHall;
            }
        }
    }
}

/* check constraints -> all students -> take halls one by one and assign students there -> return when all students
assigned */