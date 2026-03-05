import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckDBSchema {
    public static void main(String[] args) {
        String URL = "jdbc:sqlserver://localhost:1433;databaseName=PhongKhamNhaKhoa;encrypt=true;trustServerCertificate=true";
        String USER = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "sa";
        String PASS = System.getenv("DB_PASS") != null ? System.getenv("DB_PASS") : "123";

        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'NguoiDung'");
            
            System.out.println("Columns in NguoiDung table:");
            while (rs.next()) {
                System.out.println("- " + rs.getString("COLUMN_NAME"));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
