package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.ResultDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    private final String SQL_FILE_NAME = "too_long_sprint.sql";

    // TODO vytáhnout konstanty ZDE


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
     *      1) najít všechny iterace danného projektu
     *      2) odebrat první a poslední iteraci( ty mohou být z důvodu nastartování projektu dlouhé)
     *      3) zjistit jejich délku (rozdíl mezi start date a end date)
     *      4) pokud iterace přesháne délku 21 dní (3 týdny), tak jsou označeny jako moc dlouhé
     *      5) pokud je nalezena jedna nebo více iterací jako dlouhé, tak je anti pattern detekován
     *
     * @param project            analyzovaný project
     * @param databaseConnection databázové připojení
     * @param queries            list sql dotazů
     * @return výsledek detekce
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection, List<String> queries) {

        int numberOfLongIterations = 0;
        int totalCountOfIteration = 0;

        // get results from sql queries
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
            LOGGER.error("Cannot read results from db");
            List<ResultDetail> resultDetails = new ArrayList<>();
            resultDetails.add(new ResultDetail("Problem in reading database", e.toString()));
            return new QueryResultItem(this.antiPattern, true, resultDetails);
        }

        List<ResultDetail> resultDetails = new ArrayList<>();
        resultDetails.add(new ResultDetail("Count of iterations", String.valueOf(totalCountOfIteration)));
        resultDetails.add(new ResultDetail("Number of too long iterations", String.valueOf(numberOfLongIterations)));
        if (numberOfLongIterations >= 1) {
            resultDetails.add(new ResultDetail("Conclusion", "One or more iteration is too long"));
        } else {
            resultDetails.add(new ResultDetail("Conclusion", "All iterations in limit"));
        }

        LOGGER.info(this.antiPattern.getPrintName());
        LOGGER.info(resultDetails.toString());

        return new QueryResultItem(this.antiPattern, numberOfLongIterations >= 1, resultDetails);
    }
}
