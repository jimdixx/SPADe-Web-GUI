package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;


import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BusinessAsUsualDetector extends AntiPatternDetector {
    @Override
    public boolean analyze(Project analyzedProject, DatabaseConnection databaseConnection) {

        int numberOfIterations = 0;
        int numberOfWikipageWithRetr = 0;
        int numberOfRestrospectiveActivities = 0;

        try {
            ResultSet rs = super.executeQuery(analyzedProject, "./queries/business_as_usual.sql", databaseConnection);
            if (rs != null) {
                while (rs.next()) {
                    numberOfIterations = rs.getInt("numberOfIterations");
                    numberOfWikipageWithRetr = rs.getInt("numberOfWikipageWithRetr");
                    numberOfRestrospectiveActivities = rs.getInt("numberOfRestrospectiveActivities");
                    System.out.println("Project Id: " + analyzedProject.getId());
                    System.out.println("numberOfWikipageWithRetr:" + numberOfWikipageWithRetr);
                    System.out.println("numberOfRestrospectiveActivities:" + numberOfRestrospectiveActivities);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if((numberOfWikipageWithRetr > 0) && (numberOfRestrospectiveActivities > 0)){
            return false;
        } else {
            return true;
        }
    }
}
