import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PayrollDAO {
    public boolean generatePayroll(int employeeId, String payrollMonth, double basicSalary) {
        PayrollCalculator calculator = new PayrollCalculator(basicSalary);
        calculator.calculateSalary();
        String sql = "INSERT INTO payroll (employee_id, payroll_month, basic_salary, hra, other_allowance, pf_deduction, gross_salary, net_salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ps.setString(2, payrollMonth);
            ps.setBigDecimal(3, BigDecimal.valueOf(basicSalary));
            ps.setBigDecimal(4, BigDecimal.valueOf(calculator.getHra()));
            ps.setBigDecimal(5, BigDecimal.valueOf(calculator.getOtherAllowance()));
            ps.setBigDecimal(6, BigDecimal.valueOf(calculator.getPfDeduction()));
            ps.setBigDecimal(7, BigDecimal.valueOf(calculator.getGrossSalary()));
            ps.setBigDecimal(8, BigDecimal.valueOf(calculator.getNetSalary()));
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Payroll generated successfully!");
                System.out.println("Gross Salary: " + calculator.getGrossSalary());
                System.out.println("Net Salary: " + calculator.getNetSalary());
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Payroll Error: " + e.getMessage());
        }
        return false;
    }

    public int getPayrollCount() {
        String sql = "SELECT COUNT(*) FROM payroll";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public BigDecimal getMonthlyGrossPayroll(String payrollMonth) {
        String sql = "SELECT COALESCE(SUM(gross_salary), 0) FROM payroll WHERE payroll_month = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, payrollMonth);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getMonthlyNetPayroll(String payrollMonth) {
        String sql = "SELECT COALESCE(SUM(net_salary), 0) FROM payroll WHERE payroll_month = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, payrollMonth);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    public void viewPayroll() {
        String sql = "SELECT p.payroll_id, e.employee_name, p.payroll_month, p.basic_salary, p.gross_salary, p.net_salary FROM payroll p JOIN employees e ON p.employee_id = e.employee_id ORDER BY p.payroll_month DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("===== PAYROLL REPORTS =====");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Payroll ID: " + rs.getInt("payroll_id") + " | Employee: " + rs.getString("employee_name") + " | Month: " + rs.getString("payroll_month") + " | Basic: " + rs.getBigDecimal("basic_salary") + " | Gross: " + rs.getBigDecimal("gross_salary") + " | Net: " + rs.getBigDecimal("net_salary"));
            }
            if (!found) System.out.println("No payroll records found.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}