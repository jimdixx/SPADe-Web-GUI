package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RoadToNowhereDetectorImpl extends AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(SpecifyNothingDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(5L,
            "Road To Nowhere",
            "RoadToNowhere",
            "The project is not sufficiently planned and therefore " +
                    "takes place on an ad hoc basis with an uncertain " +
                    "outcome and deadline. There is no project plan in the project.");

    private final String sqlFileName = "road_to_nowhere.sql";

    /* Settings */
    private final int MINIMUM_NUMBER_OF_WIKI_PAGES = 1;
    private final int MINIMUM_NUMBER_OF_ACTIVITIES = 1;

    @Override
    public AntiPattern getAntiPatternModel() {
        return this.antiPattern;
    }

    @Override
    public String getAntiPatternSqlFileName() {
        return this.sqlFileName;
    }

    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection, List<String> queries) {
        int numberOfIssuesForProjectPlan = 0;
        int numberOfWikiPagesForProjectPlan = 0;

        try {
            ResultSet rs = databaseConnection.executeQueries(project, queries);
            if (rs != null) {
                while (rs.next()) {
                    numberOfIssuesForProjectPlan = rs.getInt("numberOfIssuesForProjectPlan");
                    numberOfWikiPagesForProjectPlan = rs.getInt("numberOfWikiPagesForProjectPlan");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if( numberOfIssuesForProjectPlan >= this.MINIMUM_NUMBER_OF_ACTIVITIES || numberOfWikiPagesForProjectPlan >= this.MINIMUM_NUMBER_OF_WIKI_PAGES) {

            return new QueryResultItem(this.antiPattern, false, null);
        } else {
            return new QueryResultItem(this.antiPattern, true, null);
        }
    }
}
