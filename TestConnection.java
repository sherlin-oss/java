package java;



import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {

    public static void main(String[] args) {

        try (Connection connection =
                DBConnection.getConnection()) {

            if (connection != null &&
                    !connection.isClosed()) {

                System.out.println(
                        "Connected to MySQL successfully!");

                System.out.println(
                        "Database: " +
                        connection.getCatalog());
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database connection failed.");

            System.out.println(
                    "Error: " + e.getMessage());
        }
    }
}
