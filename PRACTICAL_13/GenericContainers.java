
import java.util.Scanner;

// Generic Box class
class Box<T> {
    T value;

    void set(T value) {
        this.value = value;
    }

    T get() {
        return value;
    }
}

// Generic Pair class
class Pair<A, B> {
    A first;
    B second;

    Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    void display() {
        System.out.println("First: " + first);
        System.out.println("Second: " + second);
    }
}

public class GenericContainers {

    // Generic method to return first array element
    static <T> T getFirst(T[] array) {
        return array[0];
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Box containing an Integer
        System.out.print("Enter an integer: ");
        int number = sc.nextInt();

        Box<Integer> box1 = new Box<>();
        box1.set(number);

        System.out.println("Integer Box: " + box1.get());

        // Box containing a String
        sc.nextLine();
        System.out.print("Enter a name: ");
        String name = sc.nextLine();

        Box<String> box2 = new Box<>();
        box2.set(name);

        System.out.println("String Box: " + box2.get());

        // Pair containing different types
        Pair<String, Integer> pair =
                new Pair<>("Age", 20);

        System.out.println("\nPair values:");
        pair.display();

        // Generic method with an Integer array
        Integer[] numbers = {10, 20, 30};
        System.out.println("\nFirst integer: "
                + getFirst(numbers));

        // Generic method with a String array
        String[] names = {"Rahul", "Priya", "Amit"};
        System.out.println("First name: "
                + getFirst(names));

        sc.close();
    }
}
