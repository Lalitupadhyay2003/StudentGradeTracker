import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentGradeTracker {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Student Grade Tracker ===\n");

        int n = 0;
        while (n <= 0) {
            System.out.print("How many students would you like to enter? ");
            if (sc.hasNextInt()) {
                n = sc.nextInt();
                if (n <= 0) {
                    System.out.println("Please enter a positive number.");
                }
            } else {
                sc.next();
                System.out.println("Invalid input. Please enter an integer.");
            }
        }

        sc.nextLine();

        GradeManager gm = new GradeManager();

        for (int i = 1; i <= n; i++) {
            System.out.printf("Student #%d name: ", i);
            String name = sc.nextLine().trim();

            int score = -1;
            while (score < 0 || score > 100) {
                System.out.printf("Enter %s's score (0–100): ", name);
                if (sc.hasNextInt()) {
                    score = sc.nextInt();
                    if (score < 0 || score > 100) {
                        System.out.println("Score must be between 0 and 100.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter an integer score.");
                }
                sc.nextLine();
            }

            gm.addStudent(name, score);
        }

        System.out.println("\n--- Summary Report ---");
        gm.printReport();
        sc.close();
    }
    
    static class GradeManager {
        private final List<Student> list = new ArrayList<>();

        public void addStudent(String name, int score) {
            list.add(new Student(name, score));
        }

        public double getAverage() {
            if (list.isEmpty()) return 0;
            int sum = 0;
            for (Student s : list) {
                sum += s.getScore();
            }
            return (double) sum / list.size();
        }

        public int getHighest() {
            if (list.isEmpty()) return 0;
            int high = list.get(0).getScore();
            for (Student s : list) {
                if (s.getScore() > high) {
                    high = s.getScore();
                }
            }
            return high;
        }

        public int getLowest() {
            if (list.isEmpty()) return 0;
            int low = list.get(0).getScore();
            for (Student s : list) {
                if (s.getScore() < low) {
                    low = s.getScore();
                }
            }
            return low;
        }

        public void printReport() {
            System.out.printf("%-20s | %s%n", "Student Name", "Score");
            System.out.println("---------------------+-------");
            for (Student s : list) {
                System.out.printf("%-20s | %3d%n", s.getName(), s.getScore());
            }
            System.out.println("---------------------+-------");
            System.out.printf("Average Score : %.2f%n", getAverage());
            System.out.printf("Highest Score : %d%n", getHighest());
            System.out.printf("Lowest  Score : %d%n", getLowest());
        }
    }
    
    static class Student {
        private final String name;
        private final int score;

        public Student(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() { return name; }
        public int getScore() { return score; }
    }
}
