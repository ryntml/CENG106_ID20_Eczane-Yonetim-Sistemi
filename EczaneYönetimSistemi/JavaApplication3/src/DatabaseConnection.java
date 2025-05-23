import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/eczane_db?useUnicode=true&characterEncoding=utf8";
    private static final String USER = "root"; // kendi kullanıcı adınızı yazın
    private static final String PASSWORD = "14531789"; // kendi şifrenizi yazın

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver bulunamadı!", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}