package cz.zcu.fav.kiv.antipatterndetectionapp.detecting;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.spring.ApplicationProperties;
import cz.zcu.fav.kiv.antipatterndetectionapp.spring.SpringApplicationContext;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;

import java.sql.*;
import java.util.*;

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

    public ResultSet executeQueries(Project project, List<String> queries) {
        Statement stmt;
        ResultSet resultSet = null;
        try {
            stmt = this.getDatabaseConnection().createStatement();

            for (String query : queries) {
                if(queries.indexOf(query) != queries.size()-1){
                    if(query.contains("?"))
                        query = query.replace("?", project.getId().toString());
                    stmt.executeQuery(query);
                } else {
                    resultSet = stmt.executeQuery(query);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public List<List<Map<String,Object>>> executeQueriesWithMultipleResults(Project project, List<String> queries) {
        Statement stmt;
        List<List<Map<String,Object>>> allResults = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            stmt = this.getDatabaseConnection().createStatement();

            for (String query : queries) {
                if(queries.indexOf(query) != queries.size()-1){
                    if(query.contains("?"))
                        query = query.replace("?", project.getId().toString());
                    resultSet = stmt.executeQuery(query);
                } else {
                    resultSet = stmt.executeQuery(query);
                }

                if (query.toLowerCase().startsWith("select")) {
                    allResults.add(resultSetToArrayList(resultSet));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allResults;
    }

    private List<Map<String,Object>> resultSetToArrayList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<Map<String, Object>> list = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
        }

        return list;
    }
}
