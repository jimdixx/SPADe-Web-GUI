package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.ResultDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NinetyNinetyRuleDetectorImpl extends AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(BusinessAsUsualDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(7L,
            "Ninety Ninety Rule",
            "NinetyNinetyRule",
            "TODO");

    private final String sqlFileName = "ninety_ninety_rule.sql";
    // sql queries loaded from sql file
    private List<String> sqlQueries;

    /**
     * SETTINGS
     */
    private static final double MAX_DIVISION_RANGE = 1.2;
    private static final double MIN_DIVISION_RANGE = 0.8;
    private static final int MAX_BAD_ITERATION_LIMIT = 3;

    @Override
    public AntiPattern getAntiPatternModel() {
        return this.antiPattern;
    }

    @Override
    public String getAntiPatternSqlFileName() {
        return this.sqlFileName;
    }

    @Override
    public void setSqlQueries(List<String> queries) {
        this.sqlQueries = queries;
    }

    /**
     * Postup detekce:
     *      1) pro každou iteraci udělat součet stráveného a odhadovaného času přes všechny aktivity
     *      2) udělat podíl strávený čas / odhadovaný čas
     *      3) pokud všechny výsledky podílů budou v rozsahu 0.8 - 1.2 => vše ok
     *      4) pokud předchozí bod nezabere, tak iterovat přes všechny podíly
     *      5) pokud budou nalezeny tři iterace po sobě, které se stále zhoršují stejným směrem => detekce
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
                if (resultDivision > MAX_DIVISION_RANGE || resultDivision < MIN_DIVISION_RANGE) {
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
        int counterUnderEstimated = 0;
        for (Double divisionResult : divisionsResults) {
            if (divisionResult > MAX_DIVISION_RANGE) {
                counterOverEstimated++;
                counterUnderEstimated = 0;
            }

            if (divisionResult < MIN_DIVISION_RANGE) {
                counterUnderEstimated++;
                counterOverEstimated = 0;
            }

            if (counterOverEstimated >= MAX_BAD_ITERATION_LIMIT) {
                resultDetails.add(new ResultDetail("Conclusion",
                        "Found bad significant trend in estimated time - over estimated."));
                return new QueryResultItem(this.antiPattern, false, resultDetails);
            }

            if (counterUnderEstimated >= MAX_BAD_ITERATION_LIMIT) {
                resultDetails.add(new ResultDetail("Conclusion",
                        "Found bad significant trend in estimated time - under estimated."));
                return new QueryResultItem(this.antiPattern, false, resultDetails);
            }
        }

        resultDetails.add(new ResultDetail("Conclusion",
                "No significant trend was found for estimation and time worked on activities"));
        return new QueryResultItem(this.antiPattern, false, resultDetails);
    }
}
