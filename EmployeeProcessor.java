import java.util.*;
import java.util.stream.Collectors;

public class EmployeeProcessor {
    private List<Employee> employees;

    public EmployeeProcessor(List<Employee> employees) {
        this.employees = employees;
    }

    /**
     * Returns only active employees using Stream API
     */
    public List<Employee> getActiveEmployees() {
        return employees.stream()
                .filter(Employee::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Filters employees by department using Stream API
     */
    public List<Employee> getByDepartment(String dept) {
        return employees.stream()
                .filter(emp -> emp.getDepartment().equalsIgnoreCase(dept))
                .collect(Collectors.toList());
    }

    /**
     * Returns the average salary using Stream API
     */
    public OptionalDouble getAverageSalary() {
        return employees.stream()
                .mapToDouble(Employee::getSalary)
                .average();
    }

    /**
     * Returns the highest paid employee using Stream API
     */
    public Optional<Employee> getHighestPaid() {
        return employees.stream()
                .max(Comparator.comparingDouble(Employee::getSalary));
    }

    /**
     * Returns total salary bill for a department using Stream API
     */
    public double getSalaryBill(String dept) {
        return employees.stream()
                .filter(emp -> emp.getDepartment().equalsIgnoreCase(dept))
                .mapToDouble(Employee::getSalary)
                .sum();
    }

    /**
     * Groups employees by department using Collectors.groupingBy()
     */
    public Map<String, List<Employee>> groupByDepartment() {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
    }

    /**
     * Returns employees eligible for promotion:
     * - More than 3 years experience
     * - Salary below 60000
     * - Sorted by experience descending
     */
    public List<Employee> getEligibleForPromotion() {
        return employees.stream()
                .filter(emp -> emp.getYearsOfExperience() > 3)
                .filter(emp -> emp.getSalary() < 60000)
                .sorted(Comparator.comparingInt(Employee::getYearsOfExperience).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Returns total count of employees
     */
    public long getTotalEmployeeCount() {
        return employees.stream().count();
    }

    /**
     * Returns count of active employees
     */
    public long getActiveEmployeeCount() {
        return employees.stream()
                .filter(Employee::isActive)
                .count();
    }

    /**
     * Returns count of inactive employees
     */
    public long getInactiveEmployeeCount() {
        return employees.stream()
                .filter(emp -> !emp.isActive())
                .count();
    }

    /**
     * Returns highest salary in a department
     */
    public OptionalDouble getHighestSalaryInDepartment(String dept) {
        return employees.stream()
                .filter(emp -> emp.getDepartment().equalsIgnoreCase(dept))
                .mapToDouble(Employee::getSalary)
                .max();
    }

    /**
     * Returns lowest salary in a department
     */
    public OptionalDouble getLowestSalaryInDepartment(String dept) {
        return employees.stream()
                .filter(emp -> emp.getDepartment().equalsIgnoreCase(dept))
                .mapToDouble(Employee::getSalary)
                .min();
    }

    /**
     * Returns department-wise employee count
     */
    public Map<String, Long> getDepartmentWiseCount() {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.counting()
                ));
    }

    /**
     * Returns department-wise average salary
     */
    public Map<String, Double> getDepartmentWiseAverageSalary() {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.averagingDouble(Employee::getSalary)
                ));
    }
}