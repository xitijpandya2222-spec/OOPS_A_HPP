import java.util.Objects;

class Point {
    private int x;
    private int y;

    // Constructor
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // toString()
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // equals()
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Point))
            return false;

        Point p = (Point) obj;
        return x == p.x && y == p.y;
    }

    // hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

public class Driver {
    public static void main(String[] args) {

        Point[] points = {
            new Point(1, 2),
            new Point(3, 4),
            new Point(1, 2), // Repeat
            new Point(5, 6),
            new Point(3, 4)  // Repeat
        };

        int distinct = 0;

        for (int i = 0; i < points.length; i++) {
            boolean found = false;

            for (int j = 0; j < i; j++) {
                if (points[i].equals(points[j])) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                distinct++;
            }
        }

        System.out.println("Distinct: " + distinct);
    }
}