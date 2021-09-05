package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.Constants;
import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveInteger;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SpecifyNothingDetectorImpl implements AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(SpecifyNothingDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(4L,
            "Specify Nothing",
            "SpecifyNothing",
            "The specification is not done intentionally. Programmers are " +
                    "expected to work better without written specifications.",
            new HashMap<>() {{
                put("minNumberOfWikiPagesWithSpecification", new Configuration<PositiveInteger>("minNumberOfWikiPagesWithSpecification",
                        "Minimum number of wiki pages with project specification",
                        "Number of wiki pages",
                        "Minimum number of wikipages must be positive integer number",
                        new PositiveInteger(1)));
                put("minNumberOfActivitiesWithSpecification", new Configuration<PositiveInteger>("minNumberOfActivitiesWithSpecification",
                        "Minimum number of activities with project specification",
                        "Number of activities",
                        "Minimum number of activities with project specification must be positive integer number",
                        new PositiveInteger(1)));
                put("minAvgLengthOfActivityDescription", new Configuration<PositiveInteger>("minAvgLengthOfActivityDescription",
                        "Minimum average length of activity description",
                        "Minimum average number of character of activity description",
                        "Minimum average length of activity description must be positive integer number",
                        new PositiveInteger(150)));
                put("searchSubstringsWithProjectSpecification", new Configuration<String>("searchSubstringsWithProjectSpecification",
                        "Search substrings with project specification",
                        "Substring that will be search in wikipages and activities",
                        "Maximum number of substrings is ten and must not starts and ends with characters ||",
                        "%dsp%" + Constants.SUBSTRING_DELIMITER +
                                "%specifikace%" + Constants.SUBSTRING_DELIMITER +
                                "%specification%" + Constants.SUBSTRING_DELIMITER +
                                "%vize%proj%" + Constants.SUBSTRING_DELIMITER +
                                "%vize%produ%"));
            }},
            "Specify_Nothing.md");

    private final List<String> SQL_FILE_NAMES = Arrays.asList(
            "set_project_id.sql",
            "set_number_of_wiki_pages_with_substrings.sql",
            "set_number_of_activities_with_substrings.sql",
            "set_average_length_of_activities_description.sql",
            "select_statistics_for_given_project.sql");

    // sql queries loaded from sql file
    private List<String> sqlQueries;

    private int getMinNumberOfWikiPagesWithSpecification() {
        return ((PositiveInteger) antiPattern.getConfigurations().get("minNumberOfWikiPagesWithSpecification").getValue()).intValue();
    }

    private int getMinNumberOfActivitiesWithSpecification() {
        return ((PositiveInteger) antiPattern.getConfigurations().get("minNumberOfActivitiesWithSpecification").getValue()).intValue();
    }

    private int getMinAvgLengthOfActivityDescription() {
        return ((PositiveInteger) antiPattern.getConfigurations().get("minAvgLengthOfActivityDescription").getValue()).intValue();
    }

    private List<String> getSearchSubstringsWithProjectSpecification() {
        return Arrays.asList(((String) antiPattern.getConfigurations().get("searchSubstringsWithProjectSpecification").getValue()).split("\\|\\|"));
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
