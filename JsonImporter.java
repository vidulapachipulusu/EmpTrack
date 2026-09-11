import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonImporter {

    /**
     * Reads JSON file and returns content as string
     */
    public static String readJsonFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try {
            if (!Files.exists(Paths.get(filePath))) {
                System.err.println("✗ JSON file not found: " + filePath);
                return "";
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }
        return content.toString();
    }

    /**
     * Parses JSON string manually and reconstructs Employee objects
     * Note: This is a simplified parser. Handles basic JSON structure only.
     */
    public static List<Employee> importFromJsonString(String jsonString) {
        List<Employee> employees = new ArrayList<>();

        try {
            // Remove outer brackets and clean up
            jsonString = jsonString.trim();
            if (jsonString.startsWith("[")) {
                jsonString = jsonString.substring(1);
            }
            if (jsonString.endsWith("]")) {
                jsonString = jsonString.substring(0, jsonString.length() - 1);
            }

            // Split by employee objects (simplified approach)
            String[] objects = jsonString.split("\\},\\s*\\{");

            for (int i = 0; i < objects.length; i++) {
                String obj = objects[i];

                // Clean up braces
                obj = obj.replace("{", "").replace("}", "").trim();

                // Parse individual fields
                String employeeId = extractJsonValue(obj, "employeeId");
                String name = extractJsonValue(obj, "name");
                String department = extractJsonValue(obj, "department");
                String designation = extractJsonValue(obj, "designation");
                double salary = Double.parseDouble(extractJsonValue(obj, "salary"));
                int yearsOfExperience = Integer.parseInt(extractJsonValue(obj, "yearsOfExperience"));
                boolean isActive = Boolean.parseBoolean(extractJsonValue(obj, "isActive"));

                // Create employee and add to list
                Employee emp = new Employee(employeeId, name, department, designation, 
                        salary, yearsOfExperience, isActive);
                employees.add(emp);
            }

            System.out.println("✓ Successfully imported " + employees.size() + 
                    " employees from JSON string");
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
        }

        return employees;
    }

    /**
     * Imports employees from a JSON file
     */
    public static List<Employee> importFromJsonFile(String filePath) {
        String jsonString = readJsonFile(filePath);
        if (jsonString.isEmpty()) {
            return new ArrayList<>();
        }

        List<Employee> employees = importFromJsonString(jsonString);
        System.out.println("✓ Successfully imported from JSON file: " + filePath);
        return employees;
    }

    /**
     * Helper method to extract value for a given key from JSON object string
     * Handles both string and numeric values
     */
    private static String extractJsonValue(String jsonObj, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*";
        int startIndex = jsonObj.indexOf(key);

        if (startIndex == -1) {
            return "";
        }

        startIndex = jsonObj.indexOf(":", startIndex) + 1;
        int endIndex = jsonObj.indexOf(",", startIndex);

        if (endIndex == -1) {
            endIndex = jsonObj.length();
        }

        String value = jsonObj.substring(startIndex, endIndex).trim();

        // Remove quotes if present
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }

        return value;
    }

    /**
     * Displays imported employees
     */
    public static void displayEmployees(List<Employee> employees) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("IMPORTED EMPLOYEES");
        System.out.println("=".repeat(80));
        System.out.printf("%-10s %-20s %-15s %-20s %-10s\n", 
                "ID", "Name", "Department", "Designation", "Salary");
        System.out.println("-".repeat(80));

        for (Employee emp : employees) {
            System.out.printf("%-10s %-20s %-15s %-20s %-10.2f\n",
                    emp.getEmployeeId(),
                    emp.getName(),
                    emp.getDepartment(),
                    emp.getDesignation(),
                    emp.getSalary());
        }

        System.out.println("=".repeat(80));
        System.out.println("Total Employees: " + employees.size());
        System.out.println("=".repeat(80) + "\n");
    }
}