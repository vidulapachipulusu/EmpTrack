import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonExporter {

    /**
     * Converts employee list to JSON string manually (without library)
     */
    public static String exportToJsonString(List<Employee> employees) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            json.append("  {\n");
            json.append("    \"employeeId\": \"").append(emp.getEmployeeId()).append("\",\n");
            json.append("    \"name\": \"").append(emp.getName()).append("\",\n");
            json.append("    \"department\": \"").append(emp.getDepartment()).append("\",\n");
            json.append("    \"designation\": \"").append(emp.getDesignation()).append("\",\n");
            json.append("    \"salary\": ").append(emp.getSalary()).append(",\n");
            json.append("    \"yearsOfExperience\": ").append(emp.getYearsOfExperience()).append(",\n");
            json.append("    \"isActive\": ").append(emp.isActive()).append("\n");
            json.append("  }");

            if (i < employees.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("]\n");
        return json.toString();
    }

    /**
     * Writes employee list to JSON file
     */
    public static void exportToJsonFile(List<Employee> employees, String filePath) {
        try {
            // Create directory if it doesn't exist
            Files.createDirectories(Paths.get(filePath).getParent());

            String jsonString = exportToJsonString(employees);

            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(jsonString);
                System.out.println("✓ Successfully exported " + employees.size() + 
                        " employees to JSON file: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Error exporting to JSON: " + e.getMessage());
        }
    }

    /**
     * Displays the JSON string
     */
    public static void displayJson(String json) {
        System.out.println(json);
    }
}