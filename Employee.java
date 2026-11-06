public class Employee {
    private int employeeId;
    private String employeeName;
    private String department;
    private String designation;
    private double basicSalary;

    public Employee(String employeeName, String department, String designation, double basicSalary) {
        this.employeeName = employeeName;
        this.department = department;
        this.designation = designation;
        this.basicSalary = basicSalary;
    }

    public Employee(int employeeId, String employeeName, String department, String designation, double basicSalary) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.department = department;
        this.designation = designation;
        this.basicSalary = basicSalary;
    }

    public int getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public String getDepartment() { return department; }
    public String getDesignation() { return designation; }
    public double getBasicSalary() { return basicSalary; }

    @Override
    public String toString() {
        return "ID: " + employeeId + " | Name: " + employeeName + " | Dept: " + department + " | Designation: " + designation + " | Salary: " + basicSalary;
    }
}