package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SpecifyNothingDetectorImpl extends AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(SpecifyNothingDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(4L,
            "Specify Nothing",
            "SpecifyNothing",
            "The specification is not done intentionally. Programmers are " +
                    "expected to work better without written specifications.");

    private final String sqlFileName = "specify_nothing.sql";

    /* Settings */
    private final int MINIMUM_NUMBER_OF_WIKI_PAGES = 1;
    private final int MINIMUM_NUMBER_OF_ACTIVITIES = 1;
    private final double MINIMUM_AVERAGE_LENGTH_OF_ACTIVITY_DESCRIPTION = 150;

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
     *      1) u každého projektu zkusit nalézt jestli obsahuje nějaké wiki stránky se specifikací projektu
     *      2) dále zkusit najít aktivity, které by naznačovali, že vznikl nějaká specifikace projektu
     *      3) dále vzít průměrnou délku textu, která je uvedena jako popis u aktivity
     *      4) pokud nebude nalezena žádná aktivity ani wiki stránka, tak se zkontroluje průměrná délka popisku aktivity
     *      5) pokud ani jedno nezabere, tak je anti-pattern detekován
     *
     * @param project            analyzovaný project
     * @param databaseConnection databázové připojení
     * @param queries            list sql dotazů
     * @return výsledek detekce
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection, List<String> queries) {

        /* Init values */
        int numberOfWikiPages = 0;
        int numberOfActivitiesForSpecification = 0;
        double averageLengthOfIssueDescription = 0;

        try {
            ResultSet rs = databaseConnection.executeQueries(project, queries);
            if (rs != null) {
                while (rs.next()) {
                    numberOfWikiPages = rs.getInt("numberOfWikiPages");
                    numberOfActivitiesForSpecification = rs.getInt("numberOfActivitiesForSpecification");
                    averageLengthOfIssueDescription = rs.getDouble("averageLengthOfIssueDescription");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        LOGGER.info("Project id: " + project.getId());
        LOGGER.info("actvitiy:" + numberOfActivitiesForSpecification);
        LOGGER.info("wiki:" + numberOfWikiPages);
        LOGGER.info("average:" + averageLengthOfIssueDescription);

        if (numberOfActivitiesForSpecification >= MINIMUM_NUMBER_OF_ACTIVITIES ||
                numberOfWikiPages >= MINIMUM_NUMBER_OF_WIKI_PAGES) {
            return new QueryResultItem(this.antiPattern, false, null);
        } else {
            if (averageLengthOfIssueDescription > MINIMUM_AVERAGE_LENGTH_OF_ACTIVITY_DESCRIPTION) {
                return new QueryResultItem(this.antiPattern, false, null);
            } else {
                return new QueryResultItem(this.antiPattern, true, null);
            }
        }
    }
}
