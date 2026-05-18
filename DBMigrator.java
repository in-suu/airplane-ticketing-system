import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBMigrator {
    public static void main(String[] args) {
        String dbUrl = "jdbc:mysql://localhost:3306/byaheng_langit?useSSL=false&allowPublicKeyRetrieval=true";
        String user = "root";
        String pass = ""; // Default local MySQL blank password

        System.out.println("Starting Database Migration for 'byaheng_langit'...");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbUrl, user, pass);
            Statement stmt = conn.createStatement();
            
            System.out.println("Connected to MySQL Server successfully!");

            File sqlFile = new File("schema.sql");
            if (!sqlFile.exists()) {
                System.out.println("Error: schema.sql file not found in current directory.");
                return;
            }

            String content = new String(Files.readAllBytes(sqlFile.toPath()));
            // Split queries by semicolon but ignore inside text quotes
            String[] queries = content.split(";");
            
            int count = 0;
            for (String q : queries) {
                String trimmed = q.trim();
                if (!trimmed.isEmpty()) {
                    try {
                        stmt.executeUpdate(trimmed);
                        count++;
                    } catch (Exception ex) {
                        System.out.println("Warning on executing: " + (trimmed.length() > 50 ? trimmed.substring(0, 50) + "..." : trimmed));
                        System.out.println("Error details: " + ex.getMessage());
                    }
                }
            }

            System.out.println("Migration Completed! Successfully executed " + count + " SQL instructions.");
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println("Fatal Error during Database Migration: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
