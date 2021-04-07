package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;


import cz.zcu.fav.kiv.antipatterndetectionapp.Utils;
import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;


public abstract class AntiPatternDetector {
    public abstract boolean analyze(Project analyzedProject, DatabaseConnection databaseConnection);

    ResultSet executeQuery(Project project, String queryFileName, DatabaseConnection databaseConnection){
        Statement stmt;
        ResultSet resultSet = null;
        try {
            stmt = databaseConnection.getDatabaseConnection().createStatement();

        List<String> queries = Utils.loadQueryFromFile(queryFileName);
        ResultSet rs = null;
        for (String query : queries) {
            if(queries.indexOf(query) != queries.size()-1){
                if(query.contains("?"))
                    query = Utils.bindValues(query, Arrays.asList(project.getId().toString()));
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
}
