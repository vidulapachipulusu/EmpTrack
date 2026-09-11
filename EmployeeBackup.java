import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EmployeeBackup {

    /**
     * Serializes the employee list to a binary file
     */
    public static void serializeEmployees(List<Employee> employees, String filePath) {
        try {
            // Create directory if it doesn't exist
            Files.createDirectories(Paths.get(filePath).getParent());

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(filePath))) {
                oos.writeObject(employees);
                oos.flush();
                System.out.println("✓ Successfully serialized " + employees.size() + 
                        " employees to: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Error serializing employees: " + e.getMessage());
        }
    }

    /**
     * Deserializes the employee list from a binary file
     */
    @SuppressWarnings("unchecked")
    public static List<Employee> deserializeEmployees(String filePath) {
        List<Employee> employees = new ArrayList<>();
        try {
            if (!Files.exists(Paths.get(filePath))) {
                System.err.println("✗ Backup file not found: " + filePath);
                return employees;
            }

            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(filePath))) {
                employees = (List<Employee>) ois.readObject();
                System.out.println("✓ Successfully deserialized " + employees.size() + 
                        " employees from: " + filePath);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error deserializing employees: " + e.getMessage());
        }
        return employees;
    }

    /**
     * Verifies that the restored list matches the original
     */
    public static boolean verifyBackup(List<Employee> original, List<Employee> restored) {
        if (original.size() != restored.size()) {
            System.err.println("✗ Size mismatch! Original: " + original.size() + 
                    ", Restored: " + restored.size());
            return false;
        }

        for (int i = 0; i < original.size(); i++) {
            Employee origEmp = original.get(i);
            Employee restEmp = restored.get(i);

            // Compare key fields
            if (!origEmp.getEmployeeId().equals(restEmp.getEmployeeId()) ||
                !origEmp.getName().equals(restEmp.getName()) ||
                !origEmp.getDepartment().equals(restEmp.getDepartment()) ||
                origEmp.getSalary() != restEmp.getSalary() ||
                origEmp.getYearsOfExperience() != restEmp.getYearsOfExperience() ||
                origEmp.isActive() != restEmp.isActive()) {

                System.err.println("✗ Data mismatch at index " + i);
                System.err.println("  Original: " + origEmp);
                System.err.println("  Restored: " + restEmp);
                return false;
            }

            // Verify transient field is null after deserialization
            if (restEmp.getAuthToken() != null) {
                System.err.println("✗ Transient field 'authToken' should be null but is: " + 
                        restEmp.getAuthToken());
                return false;
            }
        }

        System.out.println("✓ Backup verification successful! All " + original.size() + 
                " employees match.");
        return true;
    }

    /**
     * Creates a backup and immediately verifies it
     */
    public static boolean createAndVerifyBackup(List<Employee> employees, String filePath) {
        // Serialize
        serializeEmployees(employees, filePath);

        // Deserialize
        List<Employee> restored = deserializeEmployees(filePath);

        // Verify
        return verifyBackup(employees, restored);
    }
}