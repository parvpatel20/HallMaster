package Strategies;
import Exceptions.HallCapacityExceededException;
import Models.ExamHall;
import Models.Seat;
import Models.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.Collections;

public class SubjectBranchMixingStrategy implements SeatingStrategy {

    @Override
    public void arrangeSeats(List<Student> students, ExamHall hall) throws HallCapacityExceededException {
        if (students.size() > hall.getCapacity()) {
            throw new HallCapacityExceededException("Students exceed hall capacity");
        }

        // Group students by branch
        Map<String, List<Student>> branchGroups = new HashMap<>();
        for (Student student : students) {
            branchGroups.computeIfAbsent(student.getBranch(), k -> new ArrayList<>()).add(student);
        }

        // Create mixed arrangement
        List<Student> mixedArrangement = new ArrayList<>();
        List<String> branches = new ArrayList<>(branchGroups.keySet());

        // Find maximum group size
        int maxGroupSize = branchGroups.values().stream().mapToInt(List::size).max().orElse(0);

        // Round-robin assignment from each branch
        for (int i = 0; i < maxGroupSize; i++) {
            for (String branch : branches) {
                List<Student> branchStudents = branchGroups.get(branch);
                if (i < branchStudents.size()) {
                    mixedArrangement.add(branchStudents.get(i));
                }
            }
        }

        // Assign to seats
        List<Seat> seats = hall.getSeats();
        for (int i = 0; i < seats.size(); i++) {
            seats.get(i).assignStudent(i < mixedArrangement.size() ? mixedArrangement.get(i) : null);
        }
    }

    @Override
    public String getStrategyName() {
        return "Subject/Branch Mixing (Round-robin by branch)";
    }
}
