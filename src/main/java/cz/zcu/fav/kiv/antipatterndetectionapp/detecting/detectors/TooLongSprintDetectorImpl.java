package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TooLongSprintDetectorImpl implements AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(TooLongSprintDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(1L,
            "Too Long Sprint",
            "TooLongSprint",
            "Iterations too long. (ideal iteration length is about 1-2 weeks, " +
                    "maximum 3 weeks). It could also be detected here if the length " +
                    "of the iteration does not change often (It can change at the " +
                    "beginning and at the end of the project, but it should not " +
                    "change in the already started project).",
            new HashMap<>() {{
                put("maxIterationLength", new Configuration<PositiveInteger>("maxIterationLength",
                        "Max Iteration Length",
                        "Maximum iteration length in days",
                        "Max iteration length must be positive integer",
                        new PositiveInteger(21)));
                put("maxNumberOfTooLongIterations", new Configuration<PositiveInteger>("maxNumberOfTooLongIterations",
                        "Max number of foo long iterations",
                        "Maximum number of too long iterations in project",
                        "Max number of too long iterations must be positive integer",
                        new PositiveInteger(0)));
            }}
    );

    private final List<String> SQL_FILE_NAMES = Arrays.asList(
            "set_project_id.sql",
            "set_first_iteration_id.sql",
            "set_last_iteration_id.sql",
            "select_all_iterations_with_lengths_without_first_and_last.sql");

    // sql queries loaded from sql file
    private List<String> sqlQueries;

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

    private Integer getMaxIterationLength(){
        return ((PositiveInteger) this.antiPattern.getConfigurations().get("maxIterationLength").getValue()).intValue();
    }

    private Integer getMaxNumberOfTooLongIterations(){
        return ((PositiveInteger) this.antiPattern.getConfigurations().get("maxNumberOfTooLongIterations").getValue()).intValue();
    }

    /**
     * Postup detekce:
     *      1) najít všechny iterace danného projektu
     *      2) odebrat první a poslední iteraci ( ty mohou být z důvodu nastartování projektu dlouhé)
     *      3) zjistit jejich délku (rozdíl mezi start date a end date)
     *      4) pokud iterace přesháne délku 21 dní (nastavitelná prahová hodnota), tak jsou označeny jako moc dlouhé
     *      5) pokud je nalezena jedna nebo více iterací jako dlouhé, tak je anti pattern detekován
     *
     * @param project            analyzovaný project
     * @param databaseConnection databázové připojení
     * @return výsledek detekce
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection) {

        // get configuration
        int maxIterationLength = getMaxIterationLength();
        int maxNumberOfTooLongIterations = getMaxNumberOfTooLongIterations();

        // auxiliary variables
        int numberOfLongIterations = 0;
        int totalCountOfIteration = 0;

        List<List<Map<String, Object>>> resultSets = databaseConnection.executeQueriesWithMultipleResults(project, this.sqlQueries);
        List<Map<String, Object>> rs = resultSets.get(0);

        for (Map<String, Object> iterationLengths : rs) {
            totalCountOfIteration++;
            if (!iterationLengths.containsKey("iterationLength") || iterationLengths.get("iterationLength") == null)
                continue;
            int iterationLength = (int) iterationLengths.get("iterationLength");
            if (iterationLength > maxIterationLength) {
                numberOfLongIterations++;
            }
        }
        List<ResultDetail> resultDetails = new ArrayList<>();
        resultDetails.add(new ResultDetail("Count of iterations without first and last", String.valueOf(totalCountOfIteration)));
        resultDetails.add(new ResultDetail("Number of too long iterations", String.valueOf(numberOfLongIterations)));
        if (numberOfLongIterations > maxNumberOfTooLongIterations) {
            resultDetails.add(new ResultDetail("Conclusion", "One or more iteration is too long"));
        } else {
            resultDetails.add(new ResultDetail("Conclusion", "All iterations in limit"));
        }

        LOGGER.info(this.antiPattern.getPrintName());
        LOGGER.info(resultDetails.toString());

        return new QueryResultItem(this.antiPattern, numberOfLongIterations > maxNumberOfTooLongIterations, resultDetails);
    }
}
