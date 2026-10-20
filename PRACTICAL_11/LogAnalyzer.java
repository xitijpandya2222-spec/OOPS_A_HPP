
import java.io.IOException;
import java.nio.file.*;
import java.util.Scanner;

public class LogAnalyzer {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of text files: ");
        int n = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter keyword to search: ");
        String keyword = sc.nextLine();

        int totalLines = 0;
        int matchingLines = 0;

        for (int i = 1; i <= n; i++) {
            System.out.print("Enter file name " + i + ": ");
            String fileName = sc.nextLine();

            Path path = Paths.get(fileName);

            try {
                int fileLines = 0;
                int fileMatches = 0;

                // Read file line by line
                for (String line : Files.readAllLines(path)) {
                    fileLines++;

                    if (line.contains(keyword)) {
                        fileMatches++;
                    }
                }

                // Get file size
                long size = Files.size(path);

                System.out.println("\nFile: " + fileName);
                System.out.println("Number of lines: " + fileLines);
                System.out.println("Matching lines: " + fileMatches);
                System.out.println("File size: " + size + " bytes");

                totalLines += fileLines;
                matchingLines += fileMatches;

            } catch (IOException e) {
                System.out.println("Error reading file: " + fileName);
            }
        }

        System.out.println("\n--- Final Report ---");
        System.out.println("Total lines: " + totalLines);
        System.out.println("Lines containing keyword: " + matchingLines);

        sc.close();
    }
}

