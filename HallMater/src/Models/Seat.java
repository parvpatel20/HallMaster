package Models;

public class Seat {
private final int row;
private final int column;
private Student assignedStudent;

// For seat creation
public Seat(int row, int column) {
    this.row = row;
    this.column = column;
}

public int getRow() { return row; }
public int getColumn() { return column; }
public Student getAssignedStudent() { return assignedStudent; }

// To assign a student on current seat
public void assignStudent(Student s) {
    this.assignedStudent = s;
}

public boolean isEmpty() {
    return assignedStudent == null;
}


public void clearSeat() {
    this.assignedStudent = null;
}

@Override
public String toString() {
    return String.format("R%dC%d : %s", row, column, (isEmpty() ? "EMPTY" : assignedStudent.toString()));
}
}