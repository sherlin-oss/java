package java;


public class PayrollCalculator {

    private double basicSalary;
    private double hra;
    private double otherAllowance;
    private double pfDeduction;
    private double grossSalary;
    private double netSalary;

    public PayrollCalculator(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public void calculateSalary() {
        hra = basicSalary * 0.20;
        otherAllowance = basicSalary * 0.10;
        pfDeduction = basicSalary * 0.12;

        grossSalary = basicSalary + hra + otherAllowance;
        netSalary = grossSalary - pfDeduction;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public double getHra() {
        return hra;
    }

    public double getOtherAllowance() {
        return otherAllowance;
    }

    public double getPfDeduction() {
        return pfDeduction;
    }

    public double getGrossSalary() {
        return grossSalary;
    }

    public double getNetSalary() {
        return netSalary;
    }
}