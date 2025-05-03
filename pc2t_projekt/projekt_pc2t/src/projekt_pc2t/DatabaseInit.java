package projekt_pc2t;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseInit {
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:studenti.db");
        } catch (SQLException e) {
            System.out.println("Chyba při připojení k databázi: " + e.getMessage());
        }
        return conn;
    }
}
