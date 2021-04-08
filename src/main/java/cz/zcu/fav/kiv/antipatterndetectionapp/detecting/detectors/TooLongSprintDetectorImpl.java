package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.ResultDetail;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TooLongSprintDetectorImpl extends AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(TooLongSprintDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(1L,
            "Too Long Sprint",
            "TooLongSprint",
            "Iterations too long. (ideal iteration length is about 1-2 weeks, " +
                    "maximum 3 weeks). It could also be detected here if the length " +
                    "of the iteration does not change often (It can change at the " +
                    "beginning and at the end of the project, but it should not " +
                    "change in the already started project).");

    private final String sqlFileName = "too_long_sprint.sql";

    /**
     * Settings
     */
    private final float DIVISION_OF_TOO_LONG_ITERATIONS = (float) 1/3;


    @Override
    public AntiPattern getAntiPatternModel() {
        return this.antiPattern;
    }

    @Override
    public String getAntiPatternSqlFileName() {
        return this.sqlFileName;
    }

    /**
     * Postup detekce:
     *      1) najít všechny iterace danného projektu
     *      2) odebrat první a poslední iteraci( ty mohou být z důvodu nastartování projektu dlouhé)
     *      2) zjistit jejich délku (rozdíl mezi start date a end date)
     *      3) pokud iterace přesháne délku 21 dní (3 týdny), tak jsou označeny jako moc dlouhé
     *      4) pokud je delší více jak 1/3 ze všech sledovaných iterací, tak je anti pattern detekován
     * @param project analyzovaný project
     * @param databaseConnection    databázové připojení
     * @param queries   list sql dotazů
     * @return
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection, List<String> queries) {

        int numberOfLongIterations = 0;
        int totalCountOfIteration = 0;

        try {
            ResultSet rs = databaseConnection.executeQueries(project, queries);
            if (rs != null) {
                while (rs.next()) {
                    totalCountOfIteration++;
                    boolean isTooLongSprint = rs.getBoolean("isTooLongSprint");
                    if (isTooLongSprint) {
                        numberOfLongIterations++;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        int maxIterationLimit = Math.round(totalCountOfIteration * DIVISION_OF_TOO_LONG_ITERATIONS);

        List<ResultDetail> resultDetails = Utils.createResultDetailsList(
                new ResultDetail("Project id", project.getId().toString()),
                new ResultDetail("Max Iteration limit", String.valueOf(maxIterationLimit)),
                new ResultDetail("Count of iterations", String.valueOf(totalCountOfIteration)),
                new ResultDetail("Number of too long iterations", String.valueOf(numberOfLongIterations)),
                new ResultDetail("Is detected", String.valueOf(numberOfLongIterations >= maxIterationLimit)));

        LOGGER.info(resultDetails.toString());

        return new QueryResultItem(this.antiPattern, numberOfLongIterations >= maxIterationLimit, resultDetails);
    }
}
