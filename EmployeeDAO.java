package java;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public boolean addEmployee(Employee employee) {

        String sql = "INSERT INTO employees "
                + "(employee_name, department, designation, basic_salary) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, employee.getEmployeeName());
            ps.setString(2, employee.getDepartment());
            ps.setString(3, employee.getDesignation());
            ps.setDouble(4, employee.getBasicSalary());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println(
                        "Employee registered successfully.");
                return true;
            }

        } catch (SQLException e) {
            System.out.println(
                    "Employee Error: " + e.getMessage());
        }

        return false;
    }

    public List<Employee> getAllEmployees() {

        List<Employee> employees = new ArrayList<>();

        String sql = "SELECT * FROM employees "
                + "ORDER BY employee_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Employee employee = new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("employee_name"),
                        rs.getString("department"),
                        rs.getString("designation"),
                        rs.getDouble("basic_salary")
                );

                employees.add(employee);
            }

        } catch (SQLException e) {
            System.out.println(
                    "Retrieve Error: " + e.getMessage());
        }

        return employees;
    }

    public void viewEmployees() {

        List<Employee> employees = getAllEmployees();

        if (employees.isEmpty()) {
            System.out.println("No employee records found.");
            return;
        }

        System.out.println("===== EMPLOYEE RECORDS =====");

        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    public int getEmployeeCount() {

        String sql = "SELECT COUNT(*) FROM employees";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println(
                    "Count Error: " + e.getMessage());
        }

        return 0;
    }
}
