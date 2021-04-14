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
     * SETTINGS
     */
    private final int MAXIMUM_DAYS_DIFFERENCE = 7; // one week
    private final int MAXIMUM_ITERATION_CHANGE = 2; // how many times can iteration significantly change

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
     *      1) najít všechny iterace pro danný projekt seřazené dle name (start date má divné hodnoty a nejdou iterace po sobě)
     *      2) odebrání první a poslední iterace z důvodu možných výkyvů
     *      3) zjistit jejich délku (rozdíl mezi start date a end date)
     *      4) vždy porovnat dvě po sobě jdoucí iterace
     *      5) pokud se délka porovnávaných iterací liší o více než 7 dní, tak je zvednut counter
     *      6) pokud counter překročí hodnotu 2, tak je anti patern detekován
     * <p>
     * Alternativa (sledovat rozptyl délek jednotlivých iterací a pokud překročí nějakou hodnotu, tak detevat)
     *
     * @param project            analyzovaný project
     * @param databaseConnection databázové připojení
     * @param queries            list sql dotazů
     * @return výsledek detekce
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection, List<String> queries) {

        // init values
        List<ResultDetail> resultDetails = new ArrayList<>();
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
            LOGGER.error("Cannot read results from db");
            resultDetails.add(new ResultDetail("Problem in reading database", e.toString()));
            return new QueryResultItem(this.antiPattern, true, resultDetails);
        }

        resultDetails.add(new ResultDetail("Maximum iteration length change", String.valueOf(MAXIMUM_ITERATION_CHANGE)));
        resultDetails.add(new ResultDetail("Count of iterations", String.valueOf(numberOfIterations)));
        resultDetails.add(new ResultDetail("Iteration length changed", String.valueOf(counter)));


        if (counter >= MAXIMUM_ITERATION_CHANGE) {
            resultDetails.add(new ResultDetail("Conclusion", "Iteration length changed significantly too often"));
        } else {
            resultDetails.add(new ResultDetail("Conclusion", "Varying iteration length is all right"));
        }

        LOGGER.info(this.antiPattern.getPrintName());
        LOGGER.info(resultDetails.toString());

        return new QueryResultItem(this.antiPattern, (counter >= MAXIMUM_ITERATION_CHANGE), resultDetails);
    }
}
