
import java.util.*;

public class WordFrequency {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter a paragraph:");
        String paragraph = sc.nextLine();

        // Convert to lowercase and split into words
        String[] words = paragraph.toLowerCase().split("\\W+");

        // Count word frequency
        HashMap<String, Integer> map = new HashMap<>();

        for (String word : words) {
            if (!word.isEmpty()) {
                map.put(word, map.getOrDefault(word, 0) + 1);
            }
        }

        // Convert map entries to a list
        ArrayList<Map.Entry<String, Integer>> list =
                new ArrayList<>(map.entrySet());

        // Sort by frequency, highest first
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> a,
                               Map.Entry<String, Integer> b) {
                return b.getValue().compareTo(a.getValue());
            }
        });

        // Display result
        System.out.println("\nWord Frequencies:");

        for (Map.Entry<String, Integer> entry : list) {
            System.out.println(entry.getKey() + " : "
                    + entry.getValue());
        }

        sc.close();
    }
}
