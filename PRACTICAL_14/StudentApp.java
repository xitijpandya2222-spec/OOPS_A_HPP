
import java.util.ArrayList;
import java.util.Scanner;

// Model class
class Student {
    private int id;
    private String name;

    Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "ID: " + id + ", Name: " + name;
    }
}

// Service class
class StudentService {
    private ArrayList<Student> students = new ArrayList<>();

    // Add a student
    public void addStudent(Student student) {
        students.add(student);
    }

    // Return number of students
    public int getCount() {
        return students.size();
    }

    // Return all students
    public ArrayList<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    // Perform an action
    public void performAction(String action) {
        if (action.equalsIgnoreCase("add")) {
            addStudent(new Student(1, "Rahul"));
        } else if (action.equalsIgnoreCase("show")) {
            for (Student student : students) {
                System.out.println(student);
            }
        } else {
            throw new IllegalArgumentException("Invalid action!");
        }
    }
}

// Main class
public class StudentApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentService service = new StudentService();

        while (true) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. Add Student");
            System.out.println("2. Show Students");
            System.out.println("3. Show Count");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter student ID: ");
                int id = sc.nextInt();
                sc.nextLine();

                System.out.print("Enter student name: ");
                String name = sc.nextLine();

                service.addStudent(new Student(id, name));
                System.out.println("Student added successfully!");

            } else if (choice == 2) {
                service.performAction("show");

            } else if (choice == 3) {
                System.out.println("Total students: "
                        + service.getCount());

            } else if (choice == 4) {
                System.out.println("Program ended.");
                break;

            } else {
                System.out.println("Invalid menu choice!");
            }
        }

        sc.close();
    }
}
