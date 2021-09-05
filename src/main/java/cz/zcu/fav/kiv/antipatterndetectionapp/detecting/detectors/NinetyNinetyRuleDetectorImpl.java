package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveFloat;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class NinetyNinetyRuleDetectorImpl implements AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(BusinessAsUsualDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(7L,
            "Ninety Ninety Rule",
            "NinetyNinetyRule",
            "The first 90 percent of the code represents the first 90 percent of development time. The " +
                    "remaining 10 percent of the code represents another 90 percent of development time. " +
                    "Then decide on a long delay of the project compared to the original estimate. " +
                    "The functionality is almost done, some number is already closed and is only waiting " +
                    "for one activity to close, but it has been open for a long time.",
            new HashMap<>() {{
                put("maxDivisionRange", new Configuration<PositiveFloat>("maxDivisionRange",
                        "Maximum ration value",
                        "Maximum ratio value of spent and estimated time",
                        "Ration values must be positive float number",
                        new PositiveFloat(1.25f)));
                put("maxBadDivisionLimit", new Configuration<PositiveInteger>("maxBadDivisionLimit",
                        "Maximum iterations thresholds",
                        "Maximum number of consecutive iterations where the thresholds were exceeded",
                        "Maximum number of consecutive iterations must be positive integer number",
                        new PositiveInteger(2)));
            }});

    private final List<String> SQL_FILE_NAMES = Arrays.asList(
            "set_project_id.sql",
            "select_all_iterations_with_sum_estimated_and_spent_time.sql");

    // sql queries loaded from sql file
    private List<String> sqlQueries;

    private double getMaxDivisionRange() {
        return ((PositiveFloat) antiPattern.getConfigurations().get("maxDivisionRange").getValue()).doubleValue();
    }

    private int getMaxBadDivisionLimit() {
        return ((PositiveInteger) antiPattern.getConfigurations().get("maxBadDivisionLimit").getValue()).intValue();
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
     * Postup detekce:
     * 1) pro každou iteraci udělat součet stráveného a odhadovaného času přes všechny aktivity
     * 2) udělat podíl strávený čas / odhadovaný čas
     * 3) pokud všechny výsledky podílů budou menší než 1.2 => vše ok
     * 4) pokud předchozí bod nezabere, tak iterovat přes všechny podíly
     * 5) pokud budou nalezeny tři iterace po sobě, kde se stále zhoršují odhady => detekováno
     *
     * @param project            analyzovaný project
     * @param databaseConnection databázové připojení
     * @return výsledek detekce
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection) {

        List<ResultDetail> resultDetails = new ArrayList<>();
        List<Double> divisionsResults = new ArrayList<>();
        boolean isAllInRange = true;

        List<List<Map<String, Object>>> resultSets = databaseConnection.executeQueriesWithMultipleResults(project, this.sqlQueries);
        for (List<Map<String, Object>> rs : resultSets) {
            for (Map<String, Object> map : rs) {
                Double resultDivision = (Double) map.get("timeDivision");
                // some divisions can by null
                if (resultDivision == null) {
                    continue;
                }
                divisionsResults.add(resultDivision);
                // if is one division is out of range set boolean to false
                if (resultDivision > getMaxDivisionRange()) {
                    isAllInRange = false;
                }
            }
        }

        if (isAllInRange) {
            resultDetails.add(new ResultDetail("Conclusion",
                    "All divisions of estimated and spent time are in range"));
            return new QueryResultItem(this.antiPattern, false, resultDetails);
        }

        int counterOverEstimated = 0;
        for (Double divisionResult : divisionsResults) {
            if (divisionResult > getMaxDivisionRange()) {
                counterOverEstimated++;
            } else {
                counterOverEstimated = 0;
            }

            if (counterOverEstimated > getMaxBadDivisionLimit()) {
                resultDetails.add(new ResultDetail("Conclusion",
                        getMaxBadDivisionLimit() + " or more consecutive iterations has a bad trend in estimates"));
                return new QueryResultItem(this.antiPattern, true, resultDetails);
            }

        }

        resultDetails.add(new ResultDetail("Conclusion",
                "No significant trend was found for estimation and time worked on activities"));
        return new QueryResultItem(this.antiPattern, false, resultDetails);
    }
}
