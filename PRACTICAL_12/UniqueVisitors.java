
import java.util.*;

public class UniqueVisitors {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of login names: ");
        int n = sc.nextInt();
        sc.nextLine();

        HashSet<String> set = new HashSet<>();
        LinkedHashSet<String> orderedSet = new LinkedHashSet<>();

        // Read login names
        for (int i = 0; i < n; i++) {
            System.out.print("Enter login name: ");
            String name = sc.nextLine();

            set.add(name);
            orderedSet.add(name);
        }

        // Print unique count
        System.out.println("\nUnique visitors: " + set.size());

        // Print first three distinct names
        System.out.println("First three distinct names:");

        int count = 0;

        for (String name : orderedSet) {
            if (count == 3) {
                break;
            }

            System.out.println(name);
            count++;
        }

        sc.close();
    }
}
