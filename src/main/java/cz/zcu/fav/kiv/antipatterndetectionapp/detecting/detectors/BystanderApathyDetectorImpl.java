package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.ResultDetail;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.Percentage;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BystanderApathyDetectorImpl implements AntiPatternDetector {
    private final Logger LOGGER = LoggerFactory.getLogger(BystanderApathyDetectorImpl.class);

    public final String configJsonFileName = "BystanderApathy.json";

    private AntiPattern antiPattern;

    private final List<String> SQL_FILE_NAMES = Arrays.asList(
            "set_project_id.sql",
            "select_work_units_from_project.sql",
            "select_number_of_work_unit_other_contributors.sql"
    );

    // sql queries loaded from sql file
    private List<String> sqlQueries;

    private List<String> getSearchSubstringsInvalidContributors(Map<String, String> thresholds) {
        return Arrays.asList(thresholds.get("searchSubstringsInvalidContributors").split("\\|\\|"));
    }

    private float getMaximumPercentageOfTasksWithoutTeamwork(Map<String, String> thresholds) {
        return new Percentage(Float.parseFloat(thresholds.get("maximumPercentageOfTasksWithoutTeamwork"))).getValue();
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
     *
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection, Map<String, String> thresholds) {
        List<String> queriesFirstPart = this.sqlQueries.subList(0, 2);
        List<String> queriesSecondPart = this.sqlQueries.subList(2, 3);

        List<List<Map<String, Object>>> resultSets = databaseConnection.executeQueriesWithMultipleResults(project, queriesFirstPart);

        int bystanderAP = 0;
        int workUnitsTotalCount = 0;
        for(Map<String, Object> result : resultSets.get(0)) {
            workUnitsTotalCount++;

            List<String> parameters = new ArrayList<>();
            parameters.add(result.get("id").toString());
            parameters.add(result.get("authorId").toString());

            List<String> substringsInvalidContributors = getSearchSubstringsInvalidContributors(thresholds);
            for(String substring : substringsInvalidContributors)
                parameters.add(substring);

            List<List<Map<String, Object>>> resultsForInvalidIds = databaseConnection.executeQueriesWithMultipleResults(project, Utils.fillQueryWithSearchSubstrings(queriesSecondPart, parameters));

            if(Integer.parseInt(resultsForInvalidIds.get(0).get(0).get("otherContributorsNumber").toString()) == 1)
                bystanderAP++;
        }

        List<ResultDetail> resultDetails = new ArrayList<>();
        resultDetails.add(new ResultDetail("Bystander tasks number", String.valueOf(bystanderAP)));

        float totalRatioOfBystanderTasks = (float) bystanderAP / workUnitsTotalCount;
        resultDetails.add(new ResultDetail("Bystander tasks ratio", String.format("%.02f", totalRatioOfBystanderTasks)));

        if(totalRatioOfBystanderTasks > getMaximumPercentageOfTasksWithoutTeamwork(thresholds)) {
            resultDetails.add(new ResultDetail("Conclusion", "Tasks without other contributors besides author were detected."));
            return new QueryResultItem(this.antiPattern, true, resultDetails);
        }
        resultDetails.add(new ResultDetail("Conclusion", "Any tasks without other contributors besides author were not detected."));
        return new QueryResultItem(this.antiPattern, false, resultDetails);
    }
}
