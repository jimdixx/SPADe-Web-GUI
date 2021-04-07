package cz.zcu.fav.kiv.antipatterndetectionapp.detecting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String connectionUrl = "jdbc:mysql://localhost:3306/ppicha";
    private static final String user = "root";
    private static final String password = "";

    private Connection databaseConnection;

    public DatabaseConnection() {
        this.databaseConnection = createConnection();
    }

    private Connection createConnection() {
        Connection conn = null;
        try {
             conn = DriverManager.getConnection(connectionUrl, user, password);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void closeConnection() {
        try {
            this.databaseConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getDatabaseConnection() {
        return databaseConnection;
    }
}
