
import java.io.*;
import java.util.Scanner;

class Student implements Serializable {
    int rollNo;
    String name;
    transient String password;

    Student(int rollNo, String name, String password) {
        this.rollNo = rollNo;
        this.name = name;
        this.password = password;
    }

    void display() {
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Password: " + password);
        System.out.println();
    }
}

public class SerializationDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter number of students: ");
            int n = sc.nextInt();
            sc.nextLine();

            Student[] students = new Student[n];

            // Take student details
            for (int i = 0; i < n; i++) {
                System.out.println("\nEnter details of student " + (i + 1));

                System.out.print("Enter roll number: ");
                int roll = sc.nextInt();
                sc.nextLine();

                System.out.print("Enter name: ");
                String name = sc.nextLine();

                System.out.print("Enter password: ");
                String password = sc.nextLine();

                students[i] = new Student(roll, name, password);
            }

            // Save objects to file
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream("students.dat"));

            out.writeObject(students);
            out.close();

            System.out.println("\nObjects saved successfully.");

            // Read objects from file
            ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream("students.dat"));

            Student[] savedStudents = (Student[]) in.readObject();
            in.close();

            System.out.println("\nStudent details after reading file:");

            for (Student s : savedStudents) {
                s.display();
            }

        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found.");
        }

        sc.close();
    }
}
