import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    public boolean addEmployee(Employee employee) {
        String sql = "INSERT INTO employees (employee_name, department, designation, basic_salary) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, employee.getEmployeeName());
            statement.setString(2, employee.getDepartment());
            statement.setString(3, employee.getDesignation());
            statement.setBigDecimal(4, java.math.BigDecimal.valueOf(employee.getBasicSalary()));
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Employee registered successfully.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Failed to register employee: " + e.getMessage());
        }
        return false;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT employee_id, employee_name, department, designation, basic_salary FROM employees ORDER BY employee_id";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                employees.add(new Employee(
                        result.getInt("employee_id"),
                        result.getString("employee_name"),
                        result.getString("department"),
                        result.getString("designation"),
                        result.getBigDecimal("basic_salary").doubleValue()));
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve employees: " + e.getMessage());
        }
        return employees;
    }

    public void viewEmployees() {
        List<Employee> employees = getAllEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employee records found.");
            return;
        }
        System.out.println("\n========== EMPLOYEE RECORDS ==========");
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    public int getEmployeeCount() {
        String sql = "SELECT COUNT(*) FROM employees";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            if (result.next()) return result.getInt(1);
        } catch (SQLException e) {
            System.out.println("Failed to count employees: " + e.getMessage());
        }
        return 0;
    }
}