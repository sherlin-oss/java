public class TestPayrollDAO {
    public static void main(String[] args) {
        PayrollDAO payrollDAO = new PayrollDAO();
        System.out.println("Testing PayrollDAO...");
        payrollDAO.viewPayroll();
        System.out.println("PayrollDAO test completed.");
    }
}