package Models;
import java.util.ArrayList;
import java.util.List;

public class ExamHall {
    private final String hallId;
    private final int rows;
    private final int columns;
    private final List<Seat> seats = new ArrayList<>();

    public ExamHall(String hallId, int rows, int columns) {
        this.hallId = hallId;
        this.rows = rows;
        this.columns = columns;
        createSeats(); // calls constructor of seats where row and column is needed.
    }

    private void createSeats() {
        seats.clear();
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= columns; c++) {
                seats.add(new Seat(r, c));
            }
        }
    }

    public String getHallId() { return hallId; }
    public int getRows() { return rows; }
    public int getColumns() { return columns; }
    public int getCapacity() { return seats.size(); }
    public List<Seat> getSeats() { return seats; }

    public void clearAllSeats() {
        for (Seat seat : seats) {
            seat.clearSeat();
        }
    }

    @Override
    public String toString() {
        return String.format("Hall %s (%d seats) [%dx%d]", hallId, getCapacity(), rows, columns);
    }
}