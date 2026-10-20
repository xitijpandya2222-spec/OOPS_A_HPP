
import java.util.Scanner;

public class BoundedStats {

    // Generic method to calculate average
    static <N extends Number> double average(N[] values) {
        double sum = 0;

        for (N value : values) {
            sum = sum + value.doubleValue();
        }

        return sum / values.length;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Integer array
        System.out.print("Enter number of integers: ");
        int n = sc.nextInt();

        Integer[] intValues = new Integer[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Enter integer: ");
            intValues[i] = sc.nextInt();
        }

        System.out.println("Integer average: "
                + average(intValues));

        // Double array
        System.out.print("\nEnter number of double values: ");
        int m = sc.nextInt();

        Double[] doubleValues = new Double[m];

        for (int i = 0; i < m; i++) {
            System.out.print("Enter double: ");
            doubleValues[i] = sc.nextDouble();
        }

        System.out.println("Double average: "
                + average(doubleValues));

        // Long array
        System.out.print("\nEnter number of long values: ");
        int p = sc.nextInt();

        Long[] longValues = new Long[p];

        for (int i = 0; i < p; i++) {
            System.out.print("Enter long: ");
            longValues[i] = sc.nextLong();
        }

        System.out.println("Long average: "
                + average(longValues));

        sc.close();
    }
}
