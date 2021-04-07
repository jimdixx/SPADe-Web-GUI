package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;



import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpecifyNothingDetector extends AntiPatternDetector {

    @Override
    public boolean analyze(Project analyzedProject, DatabaseConnection databaseConnection) {

        /* Settings */
        int minimumNumberOfWikiPages = 1;
        int minimumNumberOfActivities = 1;
        double minimumAverageLengthOfIssueDescription = 200;

        /* Init values */
        int numberOfWikiPages = 0;
        int numberOfActivitiesForSpecification = 0;
        double averageLengthOfIssueDescription = 0;

        try {
            ResultSet rs = super.executeQuery(analyzedProject, "./queries/specify_nothing.sql", databaseConnection);
            if (rs != null) {
                while (rs.next()) {
                    numberOfWikiPages = rs.getInt("numberOfWikiPages");
                    numberOfActivitiesForSpecification = rs.getInt("numberOfActivitiesForSpecification");
                    averageLengthOfIssueDescription = rs.getDouble("averageLengthOfIssueDescription");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numberOfWikiPages < minimumNumberOfWikiPages
                && numberOfActivitiesForSpecification < minimumNumberOfActivities
                && !(averageLengthOfIssueDescription >= minimumAverageLengthOfIssueDescription);
    }
}
