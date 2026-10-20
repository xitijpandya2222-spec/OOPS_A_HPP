import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Column {
    String name();
}

class Student {
    @Column(name = "id")
    String id;

    @Column(name = "name")
    String name;

    @Column(name = "city")
    String city;

    void display() {
        System.out.println("\nID: " + id);
        System.out.println("Name: " + name);
        System.out.println("City: " + city);
    }
}

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of columns: ");
        int n = sc.nextInt();
        sc.nextLine();

        String[] header = new String[n];
        String[] data = new String[n];

        System.out.println("Enter column names:");
        for (int i = 0; i < n; i++)
            header[i] = sc.nextLine();

        System.out.println("Enter data:");
        for (int i = 0; i < n; i++)
            data[i] = sc.nextLine();

        Student s = new Student();

        for (Field f : Student.class.getDeclaredFields()) {

            Column c = f.getAnnotation(Column.class);
            boolean found = false;

            for (int i = 0; i < n; i++) {
                if (c.name().equals(header[i])) {
                    f.set(s, data[i]);
                    found = true;
                    break;
                }
            }

            if (!found)
                System.out.println("Missing column: " + c.name());
        }

        s.display();
    }
}