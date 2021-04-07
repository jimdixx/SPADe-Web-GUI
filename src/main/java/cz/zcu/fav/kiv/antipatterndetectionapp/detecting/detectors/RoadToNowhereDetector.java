package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;



import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoadToNowhereDetector extends AntiPatternDetector {
    @Override
    public boolean analyze(Project analyzedProject, DatabaseConnection databaseConnection) {

        int numberOfIssuesForProjectPlan = 0;
        int numberOfWikiPagesForProjectPlan = 0;

        try {
            ResultSet rs = super.executeQuery(analyzedProject, "./queries/road_to_nowhere.sql", databaseConnection);
            if (rs != null) {
                while (rs.next()) {
                    numberOfIssuesForProjectPlan = rs.getInt("numberOfIssuesForProjectPlan");
                    numberOfWikiPagesForProjectPlan = rs.getInt("numberOfWikiPagesForProjectPlan");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfIssuesForProjectPlan <= 0 && numberOfWikiPagesForProjectPlan <= 0;
    }
}
