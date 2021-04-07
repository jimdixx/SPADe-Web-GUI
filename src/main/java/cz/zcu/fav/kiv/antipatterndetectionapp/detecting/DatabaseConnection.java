package cz.zcu.fav.kiv.antipatterndetectionapp.detecting;

import cz.zcu.fav.kiv.antipatterndetectionapp.ApplicationProperties;
import cz.zcu.fav.kiv.antipatterndetectionapp.SpringApplicationContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private Connection databaseConnection;

    public DatabaseConnection() {
        ApplicationProperties applicationProperties = ((ApplicationProperties) SpringApplicationContext.getContext()
                .getBean("applicationProperties"));
        String connectionUrl = applicationProperties.getDataSourceUrl();
        String dataSourceUsername = applicationProperties.getDataSourceUsername();
        String dataSourcePassword = applicationProperties.getDataSourcePassword();
        this.databaseConnection = createConnection(connectionUrl, dataSourceUsername, dataSourcePassword);
    }

    private Connection createConnection(String connectionUrl, String dataSourceUsername, String dataSourcePassword) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connectionUrl, dataSourceUsername, dataSourcePassword);

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
