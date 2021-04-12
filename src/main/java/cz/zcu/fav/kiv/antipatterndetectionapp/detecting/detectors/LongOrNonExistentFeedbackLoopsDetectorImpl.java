package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.ResultDetail;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LongOrNonExistentFeedbackLoopsDetectorImpl extends AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(LongOrNonExistentFeedbackLoopsDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(6L,
            "Long Or Non Existent Feedback Loops",
            "LongOrNonExistentFeedbackLoops",
            "Long spacings between customer feedback or no feedback. The customer " +
                    "enters the project and sees the final result. In the end, the customer " +
                    "may not get what he really wanted. With long intervals of feedback, " +
                    "some misunderstood functionality can be created and we have to spend " +
                    "a lot of effort and time to redo it. ");

    private final String SQL_FILE_NAME = "long_or_non_existent_feedback_loops.sql";

    /**
     * Settings
     */
    private final float DIVISION_LONG_OR_NON_FEEDBACK_ITERATIONS = (float) 1/3;

    /* Settings */
    private final int MINIMUM_NUMBER_OF_WIKI_PAGES = 1;
    private final int MINIMUM_NUMBER_OF_ACTIVITIES = 1;

    @Override
    public AntiPattern getAntiPatternModel() {
        return this.antiPattern;
    }

    @Override
    public String getAntiPatternSqlFileName() {
        return this.SQL_FILE_NAME;
    }

    /**
     * Postup detekce:
     *  A)
     *      1) najít všechny aktivity, které by mohli indikovat schůzku se zákazníkem
     *      2) udělat join na iterace
     *      3) v ideálním případě by měla mít každá iterace jeden nalezený issue
     *      4) pokud nebude nalezen žádný iisue, tak to znamená že tým nezaznamenává schůzky do úkolů => je nutné ještě prověřit wiki stránky
     *      5) zkusit nalézt všechny wiki stránky, které by mohly souviset s demem
     *      6) dle datumu úpravy udělat join na iterace (jedna wiki více schůzek)
     *      7) v ideálním případě by měla mít opět každá iterace jeden záznam
     *      8) dle prahových hodnot vyhodnotit výsledky
     *
     * @param project            analyzovaný project
     * @param databaseConnection databázové připojení
     * @param queries            list sql dotazů
     * @return výsledek detekce
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection, List<String> queries) {
        Long totalNumberIterations = 0L;
        Map<String, Integer> iterationsResults = new HashMap<>();

        List<List<Map<String, Object>>> resultSets = databaseConnection.executeQueriesWithMultipleResults(project, queries);
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
        }
        int minFeedbackLimit =  totalNumberIterations.intValue() - Math.round(totalNumberIterations * DIVISION_LONG_OR_NON_FEEDBACK_ITERATIONS);

        List<ResultDetail> resultDetails = Utils.createResultDetailsList(
                new ResultDetail("Project id", project.getId().toString()));

        if ((totalNumberIterations.intValue() != iterationsResults.size())) {
            if (totalNumberIterations - iterationsResults.size() > minFeedbackLimit) {
                return new QueryResultItem(this.antiPattern, true , resultDetails);
            } else {
                return new QueryResultItem(this.antiPattern, false , resultDetails);

            }
        } else {
            return new QueryResultItem(this.antiPattern, false , resultDetails);
        }
    }
}
