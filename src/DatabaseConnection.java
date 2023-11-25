import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    static final String URL = "jdbc:mysql://localhost:3306/RMS_DMS";
    static final String USERNAME = "root";
    static final String PASSWORD = "shujaanazhar";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
