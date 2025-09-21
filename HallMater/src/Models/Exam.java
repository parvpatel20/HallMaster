package Models;
import Strategies.SeatingStrategy;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Exam {
    private final String examId;
    private final String subject;
    private final LocalDate date;
    private final List<Student> students;
    private final Map<ExamHall, SeatingStrategy> hallStrategyMap;

    public Exam(String examId, String subject, LocalDate date, List<Student> students, Map<ExamHall, SeatingStrategy> hallStrategyMap) {
        this.examId = examId;
        this.subject = subject;
        this.date = date;
        this.students = students;
        this.hallStrategyMap = hallStrategyMap;
    }

    public String getExamId() { return examId; }
    public String getSubject() { return subject; }
    public LocalDate getDate() { return date; }
    public List<Student> getStudents() { return students; }
    public Map<ExamHall, SeatingStrategy> getHallStrategyMap() { return hallStrategyMap; }
}
