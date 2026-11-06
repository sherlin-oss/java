package java;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class PayrollDAO {

    // Generate payroll for an employee
    public boolean generatePayroll(int employeeId, String payrollMonth,
                                   double basicSalary) {

        PayrollCalculator calculator =
                new PayrollCalculator(basicSalary);

        calculator.calculateSalary();

        String sql = "INSERT INTO payroll "
                + "(employee_id, payroll_month, basic_salary, hra, "
                + "other_allowance, pf_deduction, gross_salary, net_salary) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, employeeId);
            statement.setString(2, payrollMonth);
            statement.setBigDecimal(3, BigDecimal.valueOf(
                    calculator.getBasicSalary()));
            statement.setBigDecimal(4, BigDecimal.valueOf(
                    calculator.getHra()));
            statement.setBigDecimal(5, BigDecimal.valueOf(
                    calculator.getOtherAllowance()));
            statement.setBigDecimal(6, BigDecimal.valueOf(
                    calculator.getPfDeduction()));
            statement.setBigDecimal(7, BigDecimal.valueOf(
                    calculator.getGrossSalary()));
            statement.setBigDecimal(8, BigDecimal.valueOf(
                    calculator.getNetSalary()));

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Payroll generated successfully!");
                System.out.println("Gross Salary: "
                        + calculator.getGrossSalary());
                System.out.println("Net Salary: "
                        + calculator.getNetSalary());
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Failed to generate payroll: "
                    + e.getMessage());
        }

        return false;
    }

    // Get the total number of payroll records
    public int getPayrollCount() {

        String sql = "SELECT COUNT(*) FROM payroll";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            if (result.next()) {
                return result.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve payroll count: "
                    + e.getMessage());
        }

        return 0;
    }

    // Get total gross payroll for a specific month
    public BigDecimal getMonthlyGrossPayroll(String payrollMonth) {

        String sql = "SELECT COALESCE(SUM(gross_salary), 0) "
                + "FROM payroll WHERE payroll_month = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, payrollMonth);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getBigDecimal(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve monthly payroll: "
                    + e.getMessage());
        }

        return BigDecimal.ZERO;
    }

    // Display payroll reports
    public void viewPayroll() {

        String sql = "SELECT p.payroll_id, e.employee_name, "
                + "p.payroll_month, p.basic_salary, "
                + "p.gross_salary, p.net_salary "
                + "FROM payroll p JOIN employees e "
                + "ON p.employee_id = e.employee_id "
                + "ORDER BY p.payroll_month DESC, p.payroll_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            System.out.println("--- Payroll Reports ---");

            boolean found = false;

            while (result.next()) {
                found = true;

                System.out.println(
                        "Payroll ID: " + result.getInt("payroll_id")
                        + " | Employee: "
                        + result.getString("employee_name")
                        + " | Month: "
                        + result.getString("payroll_month")
                        + " | Basic: "
                        + result.getBigDecimal("basic_salary")
                        + " | Gross: "
                        + result.getBigDecimal("gross_salary")
                        + " | Net: "
                        + result.getBigDecimal("net_salary"));
            }

            if (!found) {
                System.out.println("No payroll records found.");
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve payroll: "
                    + e.getMessage());
        }
    }
    
public BigDecimal getMonthlyNetPayroll(String payrollMonth) {

    String sql = "SELECT COALESCE(SUM(net_salary), 0) "
            + "FROM payroll WHERE payroll_month = ?";

    try (Connection connection = DBConnection.getConnection();
         PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        statement.setString(1, payrollMonth);

        try (ResultSet result = statement.executeQuery()) {
            if (result.next()) {
                return result.getBigDecimal(1);
            }
        }

    } catch (SQLException e) {
        System.out.println("Failed to retrieve monthly net payroll: "
                + e.getMessage());
    }

    return BigDecimal.ZERO;
}

}
