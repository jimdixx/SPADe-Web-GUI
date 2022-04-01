package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveInteger;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.AntiPatternService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.AntiPatternServiceImpl;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpecifyNothingDetectorImpl implements AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(SpecifyNothingDetectorImpl.class);

    public final String configJsonFileName = "SpecifyNothing.json";

    private AntiPattern antiPattern;

    private final List<String> SQL_FILE_NAMES = Arrays.asList(
            "set_project_id.sql",
            "set_number_of_wiki_pages_with_substrings.sql",
            "set_number_of_activities_with_substrings.sql",
            "set_average_length_of_activities_description.sql",
            "select_statistics_for_given_project.sql");

    // sql queries loaded from sql file
    private List<String> sqlQueries;

    private int getMinNumberOfWikiPagesWithSpecification() {
        return ((PositiveInteger) antiPattern.getThresholds().get("minNumberOfWikiPagesWithSpecification").getValue()).intValue();
    }

    private int getMinNumberOfActivitiesWithSpecification() {
        return ((PositiveInteger) antiPattern.getThresholds().get("minNumberOfActivitiesWithSpecification").getValue()).intValue();
    }

    private int getMinAvgLengthOfActivityDescription() {
        return ((PositiveInteger) antiPattern.getThresholds().get("minAvgLengthOfActivityDescription").getValue()).intValue();
    }

    private List<String> getSearchSubstringsWithProjectSpecification() {
        return Arrays.asList(((String) antiPattern.getThresholds().get("searchSubstringsWithProjectSpecification").getValue()).split("\\|\\|"));
    }

    @Override
    public String getJsonFileName(){
        return this.configJsonFileName;
    }

    @Override
    public void setAntiPattern(AntiPattern antiPattern) {
        this.antiPattern = antiPattern;
    }

    @Override
    public AntiPattern getAntiPatternModel() {
        return this.antiPattern;
    }

    @Override
    public List<String> getSqlFileNames() {
        return this.SQL_FILE_NAMES;
    }

    @Override
    public void setSqlQueries(List<String> queries) {
        this.sqlQueries = queries;
    }

    /**
     * Detection procedure:
     * 1) for each project, try to find out if it contains any wiki pages with the project specification
     * 2) further try to find activities that would indicate that some project specification has arisen
     * 3) further take the average length of the text, which is given as a description of the activity
     * 4) if no activity or wiki page is found, the average length of the activity label is checked
     * 5) if neither works, the anti-pattern is detected
     *
     * @param project            analyzed project
     * @param databaseConnection database connection
     * @return detection result
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection) {

        /* Init values */
        List<ResultDetail> resultDetails = new ArrayList<>();
        int numberOfWikiPages = 0;
        int numberOfActivitiesForSpecification = 0;
        double averageLengthOfIssueDescription = 0;

        try {
            ResultSet rs = databaseConnection.executeQueries(project,
                    Utils.fillQueryWithSearchSubstrings(this.sqlQueries, getSearchSubstringsWithProjectSpecification()));
            if (rs != null) {
                while (rs.next()) {
                    numberOfWikiPages = rs.getInt("numberOfWikiPagesWithSubstrings");
                    numberOfActivitiesForSpecification = rs.getInt("numberOfActivitiesWithSubstrings");
                    averageLengthOfIssueDescription = rs.getDouble("averageLengthOfIssueDescription");
                }
            }

        } catch (SQLException e) {
            LOGGER.error("Cannot read results from db");
            resultDetails.add(new ResultDetail("Problem in reading database", e.toString()));
            return new QueryResultItem(this.antiPattern, true, resultDetails);
        }

        resultDetails.add(new ResultDetail("Number of activities for specification", String.valueOf(numberOfActivitiesForSpecification)));
        resultDetails.add(new ResultDetail("Number of wiki pages for specification", String.valueOf(numberOfWikiPages)));

        if (numberOfActivitiesForSpecification >= getMinNumberOfActivitiesWithSpecification() ||
                numberOfWikiPages >= getMinNumberOfWikiPagesWithSpecification()) {
            resultDetails.add(new ResultDetail("Conclusion", "Found activities or wiki pages that represents creation of specification"));
            return new QueryResultItem(this.antiPattern, false, resultDetails);
        } else {
            if (averageLengthOfIssueDescription > getMinAvgLengthOfActivityDescription()) {
                resultDetails.add(new ResultDetail("Conclusion", "Average length of activity description is grater then minimum"));
                return new QueryResultItem(this.antiPattern, false, resultDetails);
            } else {
                resultDetails.add(new ResultDetail("Conclusion", "Average length of activity description is smaller then minimum"));
                return new QueryResultItem(this.antiPattern, true, resultDetails);
            }
        }
    }
}
