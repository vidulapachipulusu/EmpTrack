import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReportReader {

    /**
     * Reads a report file line by line and prints it to console
     */
    public static void readAndDisplayReport(String filePath) {
        try {
            // Check if file exists
            if (!Files.exists(Paths.get(filePath))) {
                System.err.println("✗ File not found: " + filePath);
                return;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                System.out.println("\n" + "=".repeat(50));
                System.out.println("REPORT CONTENTS");
                System.out.println("=".repeat(50) + "\n");

                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                System.out.println("\n" + "=".repeat(50));
                System.out.println("✓ Report read successfully from: " + filePath);
                System.out.println("=".repeat(50) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading report: " + e.getMessage());
        }
    }

    /**
     * Reads a report and returns it as a string
     */
    public static String readReportAsString(String filePath) {
        StringBuilder content = new StringBuilder();
        try {
            if (!Files.exists(Paths.get(filePath))) {
                return "";
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading report: " + e.getMessage());
        }
        return content.toString();
    }

    /**
     * Reads a report and returns the number of lines
     */
    public static int getReportLineCount(String filePath) {
        int lineCount = 0;
        try {
            if (!Files.exists(Paths.get(filePath))) {
                return 0;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                while (reader.readLine() != null) {
                    lineCount++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading report: " + e.getMessage());
        }
        return lineCount;
    }

    /**
     * Reads specific lines from a report file
     */
    public static void readReportLines(String filePath, int startLine, int endLine) {
        try {
            if (!Files.exists(Paths.get(filePath))) {
                System.err.println("✗ File not found: " + filePath);
                return;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                int currentLine = 0;

                while ((line = reader.readLine()) != null) {
                    currentLine++;
                    if (currentLine >= startLine && currentLine <= endLine) {
                        System.out.println(line);
                    }
                    if (currentLine > endLine) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading report: " + e.getMessage());
        }
    }
}