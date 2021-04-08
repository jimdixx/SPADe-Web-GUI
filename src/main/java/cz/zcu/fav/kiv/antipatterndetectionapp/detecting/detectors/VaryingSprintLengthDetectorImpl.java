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

public class VaryingSprintLengthDetectorImpl extends AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(VaryingSprintLengthDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(2L,
            "Varying Sprint Length",
            "VaryingSprintLength",
            "The length of the sprint changes very often. " +
                    "It is clear that iterations will be different " +
                    "lengths at the beginning and end of the project, " +
                    "but the length of the sprint should not change " +
                    "during the project.");

    private final String sqlFileName = "varying_sprint_length.sql";

    /**
     *
     * @return
     */
    private final int  MAXIMUM_DAYS_DIFFERENCE = 5;
    private final float DIVISION_OF_VARYING_SPRINT_LENGTH = (float) 1/3;

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
     *      1) najít všechny iterace pro danný projekt seřazené dle start date
     *      2) odebrání první a poslední iterace z důvodu možných výkyvů
     *      3) zjistit jejich délku (rozdíl mezi start date a end date)
     *      4) vždy porovnat dvě po sobě jdoucí iterace
     *      5) pokud se délka porovnávaných iterací liší o více než 5 dní, tak je zvednut counter
     *      6) pokud counter překročí 1/3 ze všech sledovaných iterací, tak je anti pattern detekován
     *
     *      Alternativa (sledovat rozptyl délek jednotlivých iterací a pokud překročí nějakou hodnotu, tak detevat)
     * @param project analyzovaný project
     * @param databaseConnection    databázové připojení
     * @param queries   list sql dotazů
     * @return výsledek detekce
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection, List<String> queries) {

        int counter = 0;
        int numberOfIterations = 0;

        try {
            ResultSet rs = databaseConnection.executeQueries(project, queries);
            if (rs != null) {
                int firstIterationLength = Integer.MIN_VALUE;
                int secondIterationLength;
                while (rs.next()) {
                    int iterationLength = rs.getInt("iterationLength");
                    numberOfIterations++;
                    if (firstIterationLength == Integer.MIN_VALUE) {
                        firstIterationLength = iterationLength;
                        continue;
                    } else {
                        secondIterationLength = iterationLength;
                    }

                    if (Math.abs(firstIterationLength - secondIterationLength) >= MAXIMUM_DAYS_DIFFERENCE) {
                        counter = counter + 1;
                    }
                    firstIterationLength = secondIterationLength;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        int maxIterationLimit = Math.round(numberOfIterations * DIVISION_OF_VARYING_SPRINT_LENGTH);

        List<ResultDetail> resultDetails = Utils.createResultDetailsList(
                new ResultDetail("Project id", project.getId().toString()),
                new ResultDetail("Max Iteration limit", String.valueOf(maxIterationLimit)),
                new ResultDetail("Count of iterations", String.valueOf(numberOfIterations)),
                new ResultDetail("Number of significant change of iteration length", String.valueOf(counter)),
                new ResultDetail("Is detected", String.valueOf((counter >= maxIterationLimit))));

        LOGGER.info(this.antiPattern.getPrintName());
        LOGGER.info(resultDetails.toString());

        return new QueryResultItem(this.antiPattern, (counter >= maxIterationLimit), resultDetails);
    }
}
