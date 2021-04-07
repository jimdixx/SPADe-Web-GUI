package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;


import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TooLongSprintDetectorDetector extends AntiPatternDetector {


    @Override
    public boolean analyze(Project analyzedProject, DatabaseConnection databaseConnection) {

        int counter = 0;
        int maximumNumberOfTooLongIterations = 2;

        try {
            ResultSet rs = super.executeQuery(analyzedProject, "./queries/too_long_sprint.sql", databaseConnection);
            if (rs != null) {
                while (rs.next()) {
                    boolean isTooLongSprint = rs.getBoolean("isTooLongSprint");
                    if(isTooLongSprint) {
                        counter++;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter >= maximumNumberOfTooLongIterations;
    }
}
