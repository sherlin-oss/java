public class TestEmployeeDAO {
    public static void main(String[] args) {
        Employee employee = new Employee("Arun Kumar", "IT", "Software Developer", 30000.00);
        EmployeeDAO dao = new EmployeeDAO();
        if (dao.addEmployee(employee)) {
            System.out.println("Employee ID will be assigned by MySQL.");
        }
        dao.viewEmployees();
    }
}