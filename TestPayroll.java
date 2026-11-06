public class TestPayroll {
    public static void main(String[] args) {
        PayrollCalculator payroll = new PayrollCalculator(30000);
        payroll.calculateSalary();
        System.out.println("Basic Salary: " + payroll.getBasicSalary());
        System.out.println("HRA: " + payroll.getHra());
        System.out.println("Other Allowance: " + payroll.getOtherAllowance());
        System.out.println("PF Deduction: " + payroll.getPfDeduction());
        System.out.println("Gross Salary: " + payroll.getGrossSalary());
        System.out.println("Net Salary: " + payroll.getNetSalary());
    }
}