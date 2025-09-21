# HallMaster - Exam Seating Generator

**A Java OOP application that auto-arranges students across multiple exam halls using smart seating strategies to ensure fairness and prevent cheating.**

---

## ✨ Key Highlights

* **Multi-Hall Support** – distribute students seamlessly across halls
* **3 Seating Strategies** – Attendance-based, Roll No. Zigzag, Branch/Subject Mixing
* **Professional Reports** – clean, PDF-ready seating plans with statistics
* **Capacity Management** – prevents overcrowding with custom exceptions

---

## 🧑‍💻 SOLID Principles in Action

* **S**ingle Responsibility → `Student`, `ExamHall`, `FileHandler`, `ExamSeatingSystem` each handle one responsibility
* **O**pen/Closed → New seating strategies can be added without changing core code
* **L**iskov Substitution → All strategies implement the same interface seamlessly
* **I**nterface Segregation → `SeatingStrategy` keeps strategy contracts minimal
* **D**ependency Inversion → High-level modules depend on abstractions, not concrete strategies

---

## 🔧 Design Patterns

* **Strategy Pattern** – plug-and-play seating algorithms (`AttendanceBased`, `RollNumberZigzag`, `BranchMixing`)
* **Factory Pattern** – flexible hall creation & strategy assignment

---

## 🛡️ Robust Exception Handling

* **File Errors** → `IOException`, `NumberFormatException` handled gracefully
* **Capacity Errors** → `HallCapacityExceededException` with user-friendly messages
* **Graceful Degradation** → skips invalid entries, continues execution

---

## 📄 File Handling

* **Input** – CSV parsing with validation, trims whitespace, skips malformed rows
* **Output** – professional formatted reports (UTF-8 box-drawing characters)
* **Fresh Reports** – always overwrites instead of appending

---

## 🚀 Usage

```bash
javac Main.java  
java Main  
```

Input: `students.csv` → Output: `seating_report.txt`
