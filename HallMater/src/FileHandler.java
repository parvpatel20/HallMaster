import Models.ExamHall;
import Models.Seat;
import Models.Student;
import Strategies.SeatingStrategy;
import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class FileHandler {

    // Read students from CSV and store them into List<Student>.

    public static List<Student> readStudentsFromCSV(String filePath) {
        List<Student> students = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            System.out.println("Processing file: " + filePath);

            while ((line = br.readLine()) != null) {
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split(",");
                if (data.length >= 7) {
                    try {
                        // Trim whitespace from all fields
                        String id = data[0].trim();
                        String name = data[1].trim();
                        String rollNumber = data[2].trim();
                        String department = data[3].trim();
                        String branch = data[4].trim();
                        double attendance = Double.parseDouble(data[5].trim());
                        String examSubject = data[6].trim();

                        students.add(new Student(id, name, rollNumber, department, branch, attendance, examSubject));
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid line: " + line);
                    }
                }
            }

            System.out.println("Loaded " + students.size() + " students from file");

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return students;
    }

    // Write seating plan to file.

    public static void writeSeatingPlanToFile(String outputFilePath, List<ExamHall> halls,
                                              Map<String, SeatingStrategy> hallStrategyMap,
                                              List<Student> originalStudents) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath, false))) { // false = overwrite mode

            // Write professional header with timestamp
            writeProfessionalHeader(writer, originalStudents);

            // Write detailed seating plan for all halls
            writeDetailedSeatingPlan(writer, halls, hallStrategyMap);


        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // Header.
    private static void writeProfessionalHeader(PrintWriter writer, List<Student> students) {
        writer.println("╔" + "═".repeat(78) + "╗");
        writer.println("║" + centerText("EXAMINATION SEATING ARRANGEMENT REPORT", 78) + "║");
        writer.println("╠" + "═".repeat(78) + "╣");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        writer.println("║ Generated On: " + String.format("%-61s", now.format(formatter)) + "║");
        writer.println("║ Total Students: " + String.format("%-59s", students.size()) + "║");
        writer.println("║ System: Exam Seating Management System v1.0" + " ".repeat(33) + "║");
        writer.println("╚" + "═".repeat(78) + "╝");
        writer.println();
    }

    // Seating arrangement.
    private static void writeDetailedSeatingPlan(PrintWriter writer, List<ExamHall> halls,
                                                 Map<String, SeatingStrategy> hallStrategyMap) {

        writer.println("DETAILED SEATING ARRANGEMENT");
        writer.println("═".repeat(80));

        for (int hallIndex = 0; hallIndex < halls.size(); hallIndex++) {
            ExamHall hall = halls.get(hallIndex);
            String strategyName = hallStrategyMap.get(hall.getHallId()).getStrategyName();

            writer.println();
            writer.println("╔" + "═".repeat(78) + "╗");
            writer.println("║ HALL: " + String.format("%-25s", hall.getHallId()) +
                    " │ STRATEGY: " + String.format("%-35s", strategyName) + " ║");
            writer.println("║ CAPACITY: " + String.format("%-20s", hall.getCapacity() + " seats") +
                    " │ LAYOUT: " + String.format("%-38s", hall.getRows() + "×" + hall.getColumns()) + " ║");
            writer.println("╚" + "═".repeat(78) + "╝");

            // Write seating grid with visual layout
            writeHallSeatingGrid(writer, hall);

            // Write detailed hall statistics
            writeHallStatistics(writer, hall);

            // Add separator between halls (except for last hall)
            if (hallIndex < halls.size() - 1) {
                writer.println("\n" + "─".repeat(80));
            }
        }
    }

    // Hall grid.
    private static void writeHallSeatingGrid(PrintWriter writer, ExamHall hall) {
        writer.println("\n🪑 SEATING ARRANGEMENT:");
        writer.println("   " + "─".repeat(hall.getColumns() * 18 + 1));

        for (int row = 1; row <= hall.getRows(); row++) {
            writer.printf("Row %d:", row);

            for (int col = 1; col <= hall.getColumns(); col++) {
                Seat seat = null;
                for (Seat s : hall.getSeats()) {
                    if (s.getRow() == row && s.getColumn() == col) {
                        seat = s;
                        break;
                    }
                }

                if (seat != null && !seat.isEmpty()) {
                    Student student = seat.getAssignedStudent();
                    writer.printf("│ %-15s ", String.format("%s(%s)",
                            student.getName(), student.getRollNumber()));
                } else {
                    writer.printf("│ %-15s ", "EMPTY");
                }
            }
            writer.println("│");
        }
        writer.println("   " + "─".repeat(hall.getColumns() * 18 + 1));
    }

    // Hall Statistics.
    private static void writeHallStatistics(PrintWriter writer, ExamHall hall) {
        Map<String, Integer> subjectCount = new HashMap<>();
        Map<String, Integer> branchCount = new HashMap<>();
        int occupiedSeats = 0;

        for (Seat seat : hall.getSeats()) {
            if (!seat.isEmpty()) {
                Student student = seat.getAssignedStudent();
                occupiedSeats++;

                subjectCount.put(student.getExamSubject(),
                        subjectCount.getOrDefault(student.getExamSubject(), 0) + 1);
                branchCount.put(student.getBranch(),
                        branchCount.getOrDefault(student.getBranch(), 0) + 1);
            }
        }

        writer.println("\nHALL STATISTICS:");
        writer.printf("   Occupancy: %d/%d seats (%.1f%% occupied)\n",
                occupiedSeats, hall.getCapacity(),
                (occupiedSeats * 100.0) / hall.getCapacity());


        writer.println("\nExam Subjects in this hall:");
        for (Map.Entry<String, Integer> entry : subjectCount.entrySet()) {
            writer.printf("      %-20s: %d students\n", entry.getKey(), entry.getValue());
        }

        writer.println("\nAcademic Branches in this hall:");
        for (Map.Entry<String, Integer> entry : branchCount.entrySet()) {
            writer.printf("      %-20s: %d students\n", entry.getKey(), entry.getValue());
        }
    }

    /**
     * Helper method to center text within a given width
     */
    private static String centerText(String text, int width) {
        if (text.length() >= width) return text;
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - text.length() - padding);
    }

    /**
     * Create sample CSV file for testing purposes
     */
//    public static void createSampleCSVFile(String filePath) {
//        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
//            writer.println("ID,Name,RollNumber,Department,Branch,AttendancePercentage,ExamSubject");
//
//            // Mathematics students
//            writer.println("m1,Aman Kumar,CSE001,Engineering,CSE,45.0,Mathematics");
//            writer.println("m2,Bilal Ahmed,CSE002,Engineering,CSE,75.0,Mathematics");
//            writer.println("m3,Chirag Patel,ECE001,Engineering,ECE,55.0,Mathematics");
//            writer.println("m4,Deepa Singh,ME001,Engineering,ME,65.0,Mathematics");
//            writer.println("m5,Esha Gupta,CSE003,Engineering,CSE,35.0,Mathematics");
//
//            // Physics students
//            writer.println("p1,Fahad Khan,ECE002,Engineering,ECE,85.0,Physics");
//            writer.println("p2,Garima Sharma,ME002,Engineering,ME,70.0,Physics");
//            writer.println("p3,Harsh Verma,CSE004,Engineering,CSE,40.0,Physics");
//            writer.println("p4,Isha Reddy,ECE003,Engineering,ECE,90.0,Physics");
//            writer.println("p5,Jatin Joshi,ME003,Engineering,ME,50.0,Physics");
//
//            // Engineering Drawing students
//            writer.println("d1,Kavya Nair,CE001,Engineering,CE,78.0,Engineering Drawing");
//            writer.println("d2,Laxman Rao,CE002,Engineering,CE,65.0,Engineering Drawing");
//            writer.println("d3,Maya Iyer,EE001,Engineering,EE,45.0,Engineering Drawing");
//            writer.println("d4,Naman Saxena,EE002,Engineering,EE,82.0,Engineering Drawing");
//            writer.println("d5,Ojas Pandey,CE003,Engineering,CE,58.0,Engineering Drawing");
//
//            System.out.println("📄 Sample CSV file created: " + filePath);
//            System.out.println("   File contains 15 students across 3 exam subjects");
//
//        } catch (IOException e) {
//            System.out.println("Error creating sample file: " + e.getMessage());
//        }
//    }

}
