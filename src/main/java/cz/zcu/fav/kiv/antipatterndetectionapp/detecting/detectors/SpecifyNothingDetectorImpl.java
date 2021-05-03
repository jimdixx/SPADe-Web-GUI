package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpecifyNothingDetectorImpl implements AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(SpecifyNothingDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(4L,
            "Specify Nothing",
            "SpecifyNothing",
            "The specification is not done intentionally. Programmers are " +
                    "expected to work better without written specifications.",
            new HashMap<>() {{
                put("minNumberOfWikiPagesWithSpecification", new Configuration<Integer>("minNumberOfWikiPagesWithSpecification",
                        "Minimum number of wiki pages with project specification",
                        "Number of wiki pages", 1));
                put("minNumberOfActivitiesWithSpecification", new Configuration<Integer>("minNumberOfActivitiesWithSpecification",
                        "Minimum number of activities with project specification",
                        "Number of activities", 1));
                put("minAvgLengthOfActivityDescription", new Configuration<Integer>("minAvgLengthOfActivityDescription",
                        "Minimum average length of activity description",
                        "Minimum average number of character of activity description", 150));
            }});

    private final String sqlFileName = "specify_nothing.sql";
    // sql queries loaded from sql file
    private List<String> sqlQueries;

    private int getMinNumberOfWikiPagesWithSpecification() {
        return (int) antiPattern.getConfigurations().get("minNumberOfWikiPagesWithSpecification").getValue();
    }

    private int getMinNumberOfActivitiesWithSpecification() {
        return (int) antiPattern.getConfigurations().get("minNumberOfActivitiesWithSpecification").getValue();
    }

    private int getMinAvgLengthOfActivityDescription() {
        return (int) antiPattern.getConfigurations().get("minAvgLengthOfActivityDescription").getValue();
    }

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
     *      1) u každého projektu zkusit nalézt jestli obsahuje nějaké wiki stránky se specifikací projektu
     *      2) dále zkusit najít aktivity, které by naznačovali, že vznikl nějaká specifikace projektu
     *      3) dále vzít průměrnou délku textu, která je uvedena jako popis u aktivity
     *      4) pokud nebude nalezena žádná aktivity ani wiki stránka, tak se zkontroluje průměrná délka popisku aktivity
     *      5) pokud ani jedno nezabere, tak je anti-pattern detekován
     *
     * @param project            analyzovaný project
     * @param databaseConnection databázové připojení
     * @return výsledek detekce
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection) {

        /* Init values */
        List<ResultDetail> resultDetails = new ArrayList<>();
        int numberOfWikiPages = 0;
        int numberOfActivitiesForSpecification = 0;
        double averageLengthOfIssueDescription = 0;

        try {
            ResultSet rs = databaseConnection.executeQueries(project, this.sqlQueries);
            if (rs != null) {
                while (rs.next()) {
                    numberOfWikiPages = rs.getInt("numberOfWikiPages");
                    numberOfActivitiesForSpecification = rs.getInt("numberOfActivitiesForSpecification");
                    averageLengthOfIssueDescription = rs.getDouble("averageLengthOfIssueDescription");
                }
            }

        } catch (SQLException e) {
            LOGGER.error("Cannot read results from db");
            resultDetails.add(new ResultDetail("Problem in reading database", e.toString()));
            return new QueryResultItem(this.antiPattern, true, resultDetails);
        }

        resultDetails.add(new ResultDetail("Number of activities for specification", String.valueOf(numberOfActivitiesForSpecification)));
        resultDetails.add(new ResultDetail("Number of wiki pages for specification", String.valueOf(numberOfWikiPages)));

        if (numberOfActivitiesForSpecification >= getMinNumberOfActivitiesWithSpecification() ||
                numberOfWikiPages >= getMinNumberOfWikiPagesWithSpecification()) {
            resultDetails.add(new ResultDetail("Conclusion", "Found activities or wiki pages that represents creation of specification"));
            return new QueryResultItem(this.antiPattern, false, resultDetails);
        } else {
            if (averageLengthOfIssueDescription > getMinAvgLengthOfActivityDescription()) {
                resultDetails.add(new ResultDetail("Conclusion", "Average length of activity description is grater then minimum"));
                return new QueryResultItem(this.antiPattern, false, resultDetails);
            } else {
                resultDetails.add(new ResultDetail("Conclusion", "Average length of activity description is smaller then minimum"));
                return new QueryResultItem(this.antiPattern, true, resultDetails);
            }
        }
    }
}
