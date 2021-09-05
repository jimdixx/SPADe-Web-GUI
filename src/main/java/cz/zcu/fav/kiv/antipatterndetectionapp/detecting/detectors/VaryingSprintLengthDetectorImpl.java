package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class VaryingSprintLengthDetectorImpl implements AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(VaryingSprintLengthDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(2L,
            "Varying Sprint Length",
            "VaryingSprintLength",
            "The length of the sprint changes very often. " +
                    "It is clear that iterations will be different " +
                    "lengths at the beginning and end of the project, " +
                    "but the length of the sprint should not change " +
                    "during the project.",
            new HashMap<>() {{
                put("maxDaysDifference", new Configuration<PositiveInteger>("maxDaysDifference",
                        "Max days difference",
                        "Maximum distance of two consecutive iterations in days",
                        "Maximum distance must be positive integer number",
                        new PositiveInteger(7)));
                put("maxIterationChanged", new Configuration<PositiveInteger>("maxIterationChanged",
                        "Max number of iteration changed",
                        "Maximum allowed number of significant changes in iteration lengths",
                        "Maximum number of iterations changed must be positive integer number",
                        new PositiveInteger(1)));
            }});

    private final List<String> SQL_FILE_NAMES = Arrays.asList(
            "set_project_id.sql",
            "set_first_iteration_id.sql",
            "set_last_iteration_id.sql",
            "select_all_iterations_with_lengths_without_first_and_last.sql");

    // sql queries loaded from sql file
    private List<String> sqlQueries;

    private Integer getMaxDaysDifference(){
        return ((PositiveInteger) this.antiPattern.getConfigurations().get("maxDaysDifference").getValue()).intValue();
    }

    private Integer getMaxIterationChanged(){
        return ((PositiveInteger) this.antiPattern.getConfigurations().get("maxIterationChanged").getValue()).intValue();
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
     * @return výsledek detekce
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection) {

        // init values
        List<ResultDetail> resultDetails = new ArrayList<>();
        int iterationLengthChanged = 0;
        int numberOfIterations = 0;

        try {
            ResultSet rs = databaseConnection.executeQueries(project, this.sqlQueries);
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

                    if (Math.abs(firstIterationLength - secondIterationLength) >= getMaxDaysDifference()) {
                        iterationLengthChanged = iterationLengthChanged + 1;
                    }
                    firstIterationLength = secondIterationLength;
                }
            }

        } catch (SQLException e) {
            LOGGER.error("Cannot read results from db");
            resultDetails.add(new ResultDetail("Problem in reading database", e.toString()));
            return new QueryResultItem(this.antiPattern, true, resultDetails);
        }

        resultDetails.add(new ResultDetail("Maximum iteration length change", String.valueOf(getMaxIterationChanged())));
        resultDetails.add(new ResultDetail("Count of iterations", String.valueOf(numberOfIterations)));
        resultDetails.add(new ResultDetail("Iteration length changed", String.valueOf(iterationLengthChanged)));


        if (iterationLengthChanged > getMaxIterationChanged()) {
            resultDetails.add(new ResultDetail("Conclusion", "Iteration length changed significantly too often"));
        } else {
            resultDetails.add(new ResultDetail("Conclusion", "Varying iteration length is all right"));
        }

        LOGGER.info(this.antiPattern.getPrintName());
        LOGGER.info(resultDetails.toString());

        return new QueryResultItem(this.antiPattern, (iterationLengthChanged > getMaxIterationChanged()), resultDetails);
    }
}
