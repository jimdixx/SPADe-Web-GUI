package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.Constants;
import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.Percentage;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BusinessAsUsualDetectorImpl implements AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(BusinessAsUsualDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(3L,
            "Business As Usual",
            "BusinessAsUsual",
            "Absence of a retrospective after individual " +
                    "iterations or after the completion project.",
            new HashMap<>() {{
                put("divisionOfIterationsWithRetrospective", new Configuration<Percentage>("divisionOfIterationsWithRetrospective",
                        "Division of iterations with retrospective",
                        "Minimum percentage of the total number of iterations with a retrospective (0,100)",
                        "Percentage must be float number between 0 and 100",
                        new Percentage(66.66f)));
                put("searchSubstringsWithRetrospective", new Configuration<String>("searchSubstringsWithRetrospective",
                        "Search substrings with retrospective",
                        "Substring that will be search in wikipages and activities",
                        "Maximum number of substrings is ten and must not starts and ends with characters || ",
                        "%retr%" + Constants.SUBSTRING_DELIMITER +
                                "%revi%" + Constants.SUBSTRING_DELIMITER +
                                "%week%scrum%"));
            }},
            "Business_As_Usual.md");

    private final List<String> SQL_FILE_NAMES = Arrays.asList(
            "set_project_id.sql",
            "select_number_of_iterations.sql",
            "select_iterations_with_substrings.sql",
            "select_all_wikipages_that_is_updated_in_iteration.sql");

    // sql queries loaded from sql file
    private List<String> sqlQueries;

    private float getDivisionOfIterationsWithRetrospective() {
        return ((Percentage) antiPattern.getConfigurations().get("divisionOfIterationsWithRetrospective").getValue()).getValue();
    }

    private List<String> getSearchSubstringsWithRetrospective() {
        return Arrays.asList(((String) antiPattern.getConfigurations().get("searchSubstringsWithRetrospective").getValue()).split("\\|\\|"));
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
     * Detection procedure
     * 1) for each iteration find all activities that contain the name "% retr%" = retrospective or "% revi%" = revision
     * (it could be further detected that all members log in to this activity, this activity should be without a commit,
     * should be completed sometime at the end of the iteration => but if all these attributes are taken into account,
     * then we will not eatno activity => too strict criteria)
     * 2) there should be at least one retrospective activity in each iteration
     * 3) also find all wiki pages that have been modified or created in the given iteration
     * 4) find out if the wiki page presents any notes from the retrospective
     * 5) put the results of wiki pages and activities together and there should be at least one entry for each iteration
     * 6) if no record is found in more than one third of the iterations, the anti-pattern is detected
     *
     * @param project            analyzed project
     * @param databaseConnection db connection
     * @return results of detection
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection) {

        // init values
        List<ResultDetail> resultDetails = new ArrayList<>();
        Long totalNumberIterations = 0L;
        Map<String, Integer> iterationsResults = new HashMap<>();

        // go through the results of the queries and put in one map => in this map should be all iterations
        List<List<Map<String, Object>>> resultSets = databaseConnection.executeQueriesWithMultipleResults(project,
                Utils.fillQueryWithSearchSubstrings(this.sqlQueries, getSearchSubstringsWithRetrospective()));
        for (int i = 0; i < resultSets.size(); i++) {
            List<Map<String, Object>> rs = resultSets.get(i);

            if (i == 0) {
                totalNumberIterations = (Long) rs.get(0).get("numberOfIterations");
            }

            if (i == 1) {
                String iterationName;
                for (Map<String, Object> map : rs) {
                    iterationName = (String) map.get("iterationName");
                    iterationsResults.put(iterationName, 1);
                }
            }

            if (i == 2) {
                String iterationName;
                for (Map<String, Object> map : rs) {
                    iterationName = (String) map.get("iterationName");
                    iterationsResults.put(iterationName, 2);
                }
            }

        }

        int minRetrospectiveLimit = Math.round(totalNumberIterations * getDivisionOfIterationsWithRetrospective());

        resultDetails.add(new ResultDetail("Min retrospective limit", String.valueOf(minRetrospectiveLimit)));
        resultDetails.add(new ResultDetail("Found retrospectives", String.valueOf(iterationsResults.size())));
        resultDetails.add(new ResultDetail("Total number of iterations", String.valueOf(totalNumberIterations)));


        if (minRetrospectiveLimit > iterationsResults.size()) {
            resultDetails.add(new ResultDetail("Conclusion", "There is too low number of retrospectives"));
            return new QueryResultItem(this.antiPattern, true, resultDetails);
        } else {
            resultDetails.add(new ResultDetail("Conclusion", "There is right number of retrospectives"));
            return new QueryResultItem(this.antiPattern, false, resultDetails);
        }
    }
}
