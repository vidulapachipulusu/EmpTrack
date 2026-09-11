import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;

public class Main {

    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println(" EmpTrack - Employee Report Generator");
        System.out.println(" Stream API | File Handling | Serialization");
        System.out.println("=".repeat(80) + "\n");

        // Create sample employee data
        List<Employee> employees = createSampleEmployees();

        // Display raw employee data
        displayEmployeeList(employees);

        // Initialize processor
        EmployeeProcessor processor = new EmployeeProcessor(employees);

        // Task 1: Stream API Operations
        System.out.println("\n" + "=".repeat(80));
        System.out.println(" TASK 1: STREAM API OPERATIONS");
        System.out.println("=".repeat(80));

        // Get active employees
        System.out.println("\n>>> Active Employees:");
        processor.getActiveEmployees().forEach(emp -> 
            System.out.println("  • " + emp.getName() + " (" + emp.getDesignation() + ")"));

        // Get employees by department
        System.out.println("\n>>> Engineering Department Employees:");
        processor.getByDepartment("Engineering").forEach(emp ->
            System.out.println("  • " + emp.getName() + " - ₹" + emp.getSalary()));

        // Average salary
        OptionalDouble avgSalary = processor.getAverageSalary();
        if (avgSalary.isPresent()) {
            System.out.println("\n>>> Average Salary: ₹" + String.format("%.2f", avgSalary.getAsDouble()));
        }

        // Highest paid employee
        Optional<Employee> highestPaid = processor.getHighestPaid();
        if (highestPaid.isPresent()) {
            Employee emp = highestPaid.get();
            System.out.println(">>> Highest Paid Employee: " + emp.getName() + 
                    " (" + emp.getDesignation() + ") - ₹" + emp.getSalary());
        }

        // Department-wise salary bill
        System.out.println("\n>>> Department-wise Salary Bill:");
        Map<String, Long> deptCount = processor.getDepartmentWiseCount();
        for (String dept : deptCount.keySet()) {
            double bill = processor.getSalaryBill(dept);
            System.out.println("  • " + dept + ": ₹" + String.format("%.2f", bill));
        }

        // Employees eligible for promotion
        System.out.println("\n>>> Employees Eligible for Promotion (>3 years, salary <60000):");
        List<Employee> promotionEligible = processor.getEligibleForPromotion();
        if (promotionEligible.isEmpty()) {
            System.out.println("  • No employees currently eligible");
        } else {
            promotionEligible.forEach(emp ->
                System.out.println("  • " + emp.getName() + " - " + emp.getYearsOfExperience() + 
                        " years experience - ₹" + emp.getSalary()));
        }

        // Task 2: File Handling and Report Writing
        System.out.println("\n" + "=".repeat(80));
        System.out.println(" TASK 2: FILE HANDLING & REPORT WRITING");
        System.out.println("=".repeat(80) + "\n");

        String reportsDir = "reports/";

        // Write department reports
        ReportWriter.writeDepartmentReport("Engineering", employees, reportsDir + "engineering_report.txt");
        ReportWriter.writeDepartmentReport("HR", employees, reportsDir + "hr_report.txt");
        ReportWriter.writeDepartmentReport("Finance", employees, reportsDir + "finance_report.txt");

        // Write full report
        ReportWriter.writeFullReport(employees, reportsDir + "full_report.txt");

        // Write summary report
        ReportWriter.writeSummaryReport(processor, reportsDir + "summary_report.txt");

        // Read and display a report
        System.out.println("\n>>> Reading Engineering Department Report:");
        ReportReader.readAndDisplayReport(reportsDir + "engineering_report.txt");

        // Task 3: Serialization and JSON
        System.out.println("\n" + "=".repeat(80));
        System.out.println(" TASK 3: SERIALIZATION & JSON HANDLING");
        System.out.println("=".repeat(80) + "\n");

        String backupDir = "backup/";
        String backupFile = backupDir + "employees_backup.ser";

        // Serialize employees
        System.out.println(">>> SERIALIZATION (Binary Backup):");
        EmployeeBackup.serializeEmployees(employees, backupFile);

        // Deserialize and verify
        System.out.println("\n>>> DESERIALIZATION & VERIFICATION:");
        List<Employee> restoredEmployees = EmployeeBackup.deserializeEmployees(backupFile);
        boolean backupValid = EmployeeBackup.verifyBackup(employees, restoredEmployees);

        // Verify transient fields
        if (!restoredEmployees.isEmpty()) {
            System.out.println("\n>>> Transient Field Check (authToken):");
            Employee testEmp = restoredEmployees.get(0);
            if (testEmp.getAuthToken() == null) {
                System.out.println("  ✓ Transient field correctly excluded from serialization");
                System.out.println("  ✓ authToken is null after deserialization");
            } else {
                System.out.println("  ✗ WARNING: Transient field was serialized!");
            }
        }

        // JSON Export
        System.out.println("\n>>> JSON EXPORT (Manual Parsing):");
        String jsonFile = backupDir + "employees.json";
        JsonExporter.exportToJsonFile(employees, jsonFile);

        System.out.println("\n>>> JSON Output Preview:");
        String jsonString = JsonExporter.exportToJsonString(employees);
        String[] lines = jsonString.split("\n");
        for (int i = 0; i < Math.min(15, lines.length); i++) {
            System.out.println(lines[i]);
        }
        if (lines.length > 15) {
            System.out.println("  ... (" + (lines.length - 15) + " more lines)");
        }

        // JSON Import
        System.out.println("\n>>> JSON IMPORT (Manual Parsing):");
        List<Employee> importedEmployees = JsonImporter.importFromJsonFile(jsonFile);
        if (!importedEmployees.isEmpty()) {
            System.out.println("✓ Successfully imported " + importedEmployees.size() + " employees from JSON");
            System.out.println("\n>>> Imported Employee Details:");
            JsonImporter.displayEmployees(importedEmployees);
        }

        // Summary Statistics
        System.out.println("\n" + "=".repeat(80));
        System.out.println(" SUMMARY STATISTICS");
        System.out.println("=".repeat(80));
        System.out.println("\n>>> Overall Statistics:");
        System.out.println("  • Total Employees: " + processor.getTotalEmployeeCount());
        System.out.println("  • Active Employees: " + processor.getActiveEmployeeCount());
        System.out.println("  • Inactive Employees: " + processor.getInactiveEmployeeCount());
        if (processor.getAverageSalary().isPresent()) {
            System.out.println("  • Average Salary: ₹" + 
                    String.format("%.2f", processor.getAverageSalary().getAsDouble()));
        }

        System.out.println("\n>>> Department-wise Statistics:");
        Map<String, Double> deptAvgSalary = processor.getDepartmentWiseAverageSalary();
        for (String dept : deptAvgSalary.keySet()) {
            long count = processor.getDepartmentWiseCount().get(dept);
            double avgSal = deptAvgSalary.get(dept);
            System.out.println("  • " + dept + ": " + count + " employees, Avg: ₹" + 
                    String.format("%.2f", avgSal));
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println(" EXECUTION COMPLETED SUCCESSFULLY!");
        System.out.println("=".repeat(80) + "\n");
    }

    /**
     * Creates sample employee data for demonstration
     */
    private static List<Employee> createSampleEmployees() {
        List<Employee> employees = new ArrayList<>();

        // Engineering Department
        employees.add(new Employee("EMP-001", "Rahul Sharma", "Engineering", "Senior Developer", 85000, 6, true));
        employees.add(new Employee("EMP-002", "Ananya Iyer", "Engineering", "Junior Developer", 42000, 2, true));
        employees.add(new Employee("EMP-003", "Neha Singh", "Engineering", "Developer", 65000, 4, true));
        employees.add(new Employee("EMP-004", "Vikram Desai", "Engineering", "Tech Lead", 90000, 8, true));

        // HR Department
        employees.add(new Employee("EMP-005", "Priya Verma", "HR", "HR Manager", 55000, 5, true));
        employees.add(new Employee("EMP-006", "Sneha Kapoor", "HR", "HR Executive", 52000, 3, true));
        employees.add(new Employee("EMP-007", "Arjun Reddy", "HR", "Recruiter", 50000, 2, false));

        // Finance Department
        employees.add(new Employee("EMP-008", "Amit Patel", "Finance", "Finance Manager", 72000, 7, true));
        employees.add(new Employee("EMP-009", "Meera Nair", "Finance", "Accountant", 68000, 5, true));
        employees.add(new Employee("EMP-010", "Rohan Gupta", "Finance", "Junior Accountant", 48000, 1, false));

        return employees;
    }

    /**
     * Displays all employees in a formatted table
     */
    private static void displayEmployeeList(List<Employee> employees) {
        System.out.println("SAMPLE EMPLOYEE DATA");
        System.out.println("=".repeat(110));
        System.out.printf("%-10s %-20s %-15s %-20s %-10s %-8s %-10s\n",
                "ID", "Name", "Department", "Designation", "Salary", "Years", "Status");
        System.out.println("-".repeat(110));

        for (Employee emp : employees) {
            String status = emp.isActive() ? "Active" : "Inactive";
            System.out.printf("%-10s %-20s %-15s %-20s %-10s %-8d %-10s\n",
                    emp.getEmployeeId(),
                    emp.getName(),
                    emp.getDepartment(),
                    emp.getDesignation(),
                    String.format("₹%.0f", emp.getSalary()),
                    emp.getYearsOfExperience(),
                    status);
        }

        System.out.println("=".repeat(110));
        System.out.println("Total Records: " + employees.size() + "\n");
    }
}