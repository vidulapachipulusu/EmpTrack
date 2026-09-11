import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ReportWriter {

    /**
     * Writes a formatted department report to a file
     */
    public static void writeDepartmentReport(String dept, List<Employee> employees, String filePath) {
        try {
            // Create directory if it doesn't exist
            Files.createDirectories(Paths.get(filePath).getParent());

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write("============================================\n");
                writer.write(" DEPARTMENT REPORT: " + dept + "\n");
                writer.write(" Generated: " + LocalDate.now() + "\n");
                writer.write("============================================\n");
                writer.write(" ID       NAME              DESIGNATION       SALARY\n");
                writer.write("--------------------------------------------\n");

                double totalSalary = 0;
                int count = 0;

                for (Employee emp : employees) {
                    if (emp.getDepartment().equalsIgnoreCase(dept)) {
                        writer.write(String.format(" %-8s %-17s %-18s %s\n",
                                emp.getEmployeeId(),
                                emp.getName(),
                                emp.getDesignation(),
                                formatSalary(emp.getSalary())));
                        totalSalary += emp.getSalary();
                        count++;
                    }
                }

                writer.write("--------------------------------------------\n");
                writer.write(" Headcount : " + count + "\n");
                writer.write(" Total Bill : " + formatSalary(totalSalary) + "\n");
                double avgSalary = count > 0 ? totalSalary / count : 0;
                writer.write(" Avg Salary : " + formatSalary(avgSalary) + "\n");
                writer.write("============================================\n");

                System.out.println("✓ Department report written to: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Error writing department report: " + e.getMessage());
        }
    }

    /**
     * Writes a full report with all employees grouped by department
     */
    public static void writeFullReport(List<Employee> employees, String filePath) {
        try {
            // Create directory if it doesn't exist
            Files.createDirectories(Paths.get(filePath).getParent());

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write("============================================\n");
                writer.write(" FULL EMPLOYEE REPORT\n");
                writer.write(" Generated: " + LocalDate.now() + "\n");
                writer.write("============================================\n\n");

                // Group employees by department
                Map<String, List<Employee>> groupedByDept = new EmployeeProcessor(employees)
                        .groupByDepartment();

                for (String dept : groupedByDept.keySet()) {
                    List<Employee> deptEmployees = groupedByDept.get(dept);

                    writer.write(">>> DEPARTMENT: " + dept + "\n");
                    writer.write("ID       | NAME              | DESIGNATION       | SALARY      | STATUS\n");
                    writer.write("---------|-------------------|-------------------|-------------|----------\n");

                    double totalSalary = 0;
                    for (Employee emp : deptEmployees) {
                        String status = emp.isActive() ? "Active" : "Inactive";
                        writer.write(String.format("%-8s | %-17s | %-17s | %-11s | %s\n",
                                emp.getEmployeeId(),
                                emp.getName(),
                                emp.getDesignation(),
                                formatSalary(emp.getSalary()),
                                status));
                        totalSalary += emp.getSalary();
                    }

                    double avgSalary = deptEmployees.size() > 0 ? totalSalary / deptEmployees.size() : 0;
                    writer.write("---------|-------------------|-------------------|-------------|----------\n");
                    writer.write("Headcount: " + deptEmployees.size() + " | Total Bill: " +
                            formatSalary(totalSalary) + " | Average: " + formatSalary(avgSalary) + "\n\n");
                }

                writer.write("============================================\n");
                writer.write(" OVERALL STATISTICS\n");
                writer.write("============================================\n");
                writer.write("Total Employees: " + employees.size() + "\n");

                System.out.println("✓ Full report written to: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Error writing full report: " + e.getMessage());
        }
    }

    /**
     * Writes a summary report with key statistics
     */
    public static void writeSummaryReport(EmployeeProcessor processor, String filePath) {
        try {
            // Create directory if it doesn't exist
            Files.createDirectories(Paths.get(filePath).getParent());

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write("============================================\n");
                writer.write(" SUMMARY REPORT\n");
                writer.write(" Generated: " + LocalDate.now() + "\n");
                writer.write("============================================\n\n");

                // Overall statistics
                writer.write("OVERALL STATISTICS:\n");
                writer.write("- Total Headcount: " + processor.getTotalEmployeeCount() + "\n");
                writer.write("- Active Employees: " + processor.getActiveEmployeeCount() + "\n");
                writer.write("- Inactive Employees: " + processor.getInactiveEmployeeCount() + "\n");

                if (processor.getAverageSalary().isPresent()) {
                    writer.write("- Average Salary: " + formatSalary(processor.getAverageSalary().getAsDouble()) + "\n");
                }

                if (processor.getHighestPaid().isPresent()) {
                    Employee highest = processor.getHighestPaid().get();
                    writer.write("- Highest Paid Employee: " + highest.getName() + " (" +
                            highest.getDesignation() + ") - " + formatSalary(highest.getSalary()) + "\n");
                }

                writer.write("\n");
                writer.write("DEPARTMENT-WISE SALARY BILL:\n");
                Map<String, Long> deptCount = processor.getDepartmentWiseCount();
                for (String dept : deptCount.keySet()) {
                    double bill = processor.getSalaryBill(dept);
                    long count = deptCount.get(dept);
                    double avg = bill / count;
                    writer.write("- " + dept + ": " + formatSalary(bill) +
                            " (Count: " + count + ", Avg: " + formatSalary(avg) + ")\n");
                }

                writer.write("\n");
                writer.write("PROMOTION ELIGIBLE EMPLOYEES:\n");
                processor.getEligibleForPromotion().stream()
                        .limit(5)
                        .forEach(emp -> {
                            try {
                                writer.write("- " + emp.getName() + " (" + emp.getDesignation() + ") - " +
                                        emp.getYearsOfExperience() + " years experience\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                writer.write("\n============================================\n");

                System.out.println("✓ Summary report written to: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Error writing summary report: " + e.getMessage());
        }
    }

    /**
     * Helper method to format salary with commas
     */
    private static String formatSalary(double salary) {
        return String.format("%,.0f", salary);
    }
}