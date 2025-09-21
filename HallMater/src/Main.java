import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Models.*;
import Strategies.*;
import java.util.Scanner;

public class Main {
    private static final String OUTPUT_FILE = "seating_report.txt"; // Fixed output file

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("EXAM SEATING MANAGEMENT SYSTEM");
        System.out.println("=".repeat(50));

        // ONLY file upload option
        System.out.print("Enter CSV file path with student data: ");
        String inputFile = scanner.nextLine().trim();

        if (inputFile.isEmpty()) {
            System.out.println("File path cannot be empty!");
            scanner.close();
            return;
        }

        // Fixed configuration - halls and strategies
        List<ExamHall> availableHalls = createAvailableHalls();
        Map<String, SeatingStrategy> hallStrategyMap = createHallStrategyMapping();

        // Process exam from file
        ExamSeatingSystem seatingSystem = new ExamSeatingSystem();
        boolean success = seatingSystem.processExamFromFile(inputFile, OUTPUT_FILE, availableHalls, hallStrategyMap);

        if (success) {
            System.out.println("SUCCESS! Check output file: " + OUTPUT_FILE);
        } else {
            System.out.println("Process failed. Please check your input file.");
        }

        scanner.close();
    }

    // Fixed halls configuration
    private static List<ExamHall> createAvailableHalls() {
        List<ExamHall> halls = new ArrayList<>();
        halls.add(new ExamHall("H-101", 3, 3)); // 9 seats
        halls.add(new ExamHall("H-102", 2, 4)); // 8 seats
        halls.add(new ExamHall("H-201", 2, 3)); // 6 seats
        return halls;
    }

    // Fixed strategy mapping
    private static Map<String, SeatingStrategy> createHallStrategyMapping() {
        Map<String, SeatingStrategy> strategyMap = new HashMap<>();
        strategyMap.put("H-101", new AttendanceBasedStrategy(60.0));
        strategyMap.put("H-102", new RollNumberZigzagStrategy());
        strategyMap.put("H-201", new SubjectBranchMixingStrategy());
        return strategyMap;
    }
}