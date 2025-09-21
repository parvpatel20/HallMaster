import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import Exceptions.HallCapacityExceededException;
import Models.*;
import Strategies.SeatingStrategy;

class ExamSeatingSystem {

    public boolean processExamFromFile(String inputCSVPath, String outputReportPath,
                                       List<ExamHall> availableHalls,
                                       Map<String, SeatingStrategy> hallStrategyMap) {

        // Read students from CSV file
        List<Student> allStudents = FileHandler.readStudentsFromCSV(inputCSVPath);

        if (allStudents.isEmpty()) {
            System.out.println("❌ No students found in file. Process failed.");
            return false;
        }

        // Generate seating plan
        if (generateSeatingPlan(allStudents, availableHalls, hallStrategyMap)) {
            // Write results to output file
            FileHandler.writeSeatingPlanToFile(outputReportPath, availableHalls, hallStrategyMap, allStudents);
            System.out.println("✅ Seating plan generated successfully!");
            System.out.println("📄 Report saved to: " + outputReportPath);
            return true;
        }

        return false;
    }

    // Internal processing
    private boolean generateSeatingPlan(List<Student> allStudents, List<ExamHall> availableHalls,
                                        Map<String, SeatingStrategy> hallStrategyMap) {

        // Validate total capacity
        int totalStudents = allStudents.size();
        int totalCapacity = availableHalls.stream().mapToInt(ExamHall::getCapacity).sum();

        if (totalStudents > totalCapacity) {
            System.out.println("❌ ERROR: " + totalStudents + " students exceed capacity of " + totalCapacity + " seats");
            return false;
        }

        // Clear all halls first
        for (ExamHall hall : availableHalls) {
            hall.clearAllSeats();
        }

        // Distribute students across halls
        distributeStudentsAcrossHalls(allStudents, availableHalls, hallStrategyMap);

        return true;
    }

    // Silent distribution
    private void distributeStudentsAcrossHalls(List<Student> allStudents, List<ExamHall> availableHalls,
                                               Map<String, SeatingStrategy> hallStrategyMap) {

        List<Student> remainingStudents = new ArrayList<>(allStudents);
        int studentIndex = 0;

        for (ExamHall hall : availableHalls) {
            if (studentIndex >= remainingStudents.size()) break;

            String hallId = hall.getHallId();
            SeatingStrategy strategy = hallStrategyMap.get(hallId);

            if (strategy == null) continue;

            int hallCapacity = hall.getCapacity();
            int studentsForThisHall = Math.min(hallCapacity, remainingStudents.size() - studentIndex);

            if (studentsForThisHall > 0) {
                List<Student> hallStudents = remainingStudents.subList(studentIndex, studentIndex + studentsForThisHall);

                try {
                    strategy.arrangeSeats(hallStudents, hall);
                    studentIndex += studentsForThisHall;
                } catch (HallCapacityExceededException e) {
                    // Silent error handling
                }
            }
        }
    }

}
