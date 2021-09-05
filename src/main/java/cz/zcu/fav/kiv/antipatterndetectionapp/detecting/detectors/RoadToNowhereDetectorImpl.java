package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.Constants;
import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveInteger;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RoadToNowhereDetectorImpl implements AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(SpecifyNothingDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(5L,
            "Road To Nowhere",
            "RoadToNowhere",
            "The project is not sufficiently planned and therefore " +
                    "takes place on an ad hoc basis with an uncertain " +
                    "outcome and deadline. There is no project plan in the project.",
            new HashMap<>() {{
                put("minNumberOfWikiPagesWithProjectPlan", new Configuration<PositiveInteger>("minNumberOfWikiPagesWithProjectPlan",
                        "Minimum number of wiki pages with project plan",
                        "Number of wiki pages",
                        "Minimum number of wikipages must be positive integer number",
                        new PositiveInteger(1)));
                put("minNumberOfActivitiesWithProjectPlan", new Configuration<PositiveInteger>("minNumberOfActivitiesWithProjectPlan",
                        "Minimum number of activities with project plan",
                        "Number of activities",
                        "Minimum number of activities must be positive integer number",
                        new PositiveInteger(1)));
                put("searchSubstringsWithProjectPlan", new Configuration<String>("searchSubstringsWithProjectPlan",
                        "Search substrings with project plan",
                        "Substring that will be search in wikipages and activities",
                        "Maximum number of substrings is ten and must not starts and ends with characters ||",
                        "%plán projektu%" + Constants.SUBSTRING_DELIMITER +
                                "%project plan%" + Constants.SUBSTRING_DELIMITER +
                                "%plan project%" + Constants.SUBSTRING_DELIMITER +
                                "%projektový plán%"));
            }},
            "Road_To_Nowhere.md");

    private final List<String> SQL_FILE_NAMES = Arrays.asList(
            "set_project_id.sql",
            "set_first_iteration_start_date.sql",
            "set_second_iteration_start_date.sql",
            "set_number_of_activities_with_substrings.sql",
            "set_number_of_wiki_pages_with_substrings.sql",
            "select_number_of_activities_and_wiki_pages_with_substrings.sql");

    // sql queries loaded from sql file
    private List<String> sqlQueries;

    private int getMinNumberOfWikiPagesWithProjectPlan() {
        return ((PositiveInteger) antiPattern.getConfigurations().get("minNumberOfWikiPagesWithProjectPlan").getValue()).intValue();
    }

    private int getMinNumberOfActivitiesWithProjectPlan() {
        return ((PositiveInteger) antiPattern.getConfigurations().get("minNumberOfActivitiesWithProjectPlan").getValue()).intValue();
    }

    private List<String> getSearchSubstringsWithProjectPlan() {
        return Arrays.asList(((String) antiPattern.getConfigurations().get("searchSubstringsWithProjectPlan").getValue()).split("\\|\\|"));
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
     * 1) u každého projektu zkusit nalézt jestli obsahuje nějaké wiki stránky s projektovým plánem
     * 2) dále zkusit najít aktivity, které by naznačovali, že vznikl nějaký projektový plán
     * 3) pokud nebude nalezena žádná aktivita nebo wiki stránka, tak je antivzor detekován
     *
     * @param project            analyzovaný project
     * @param databaseConnection databázové připojení
     * @return výsledek detekce
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection) {

        /* Init values */
        List<ResultDetail> resultDetails = new ArrayList<>();
        int numberOfIssuesForProjectPlan = 0;
        int numberOfWikiPagesForProjectPlan = 0;

        try {
            ResultSet rs = databaseConnection.executeQueries(project,
                    Utils.fillQueryWithSearchSubstrings(this.sqlQueries, getSearchSubstringsWithProjectPlan()));
            if (rs != null) {
                while (rs.next()) {
                    numberOfIssuesForProjectPlan = rs.getInt("numberOfActivitiesWithSubstrings");
                    numberOfWikiPagesForProjectPlan = rs.getInt("numberOfWikiPagesWithSubstrings");
                }
            }

        } catch (SQLException e) {
            LOGGER.error("Cannot read results from db");
            resultDetails.add(new ResultDetail("Problem in reading database", e.toString()));
            return new QueryResultItem(this.antiPattern, true, resultDetails);
        }

        resultDetails.add(new ResultDetail("Number of issues for creating project plan", String.valueOf(numberOfIssuesForProjectPlan)));
        resultDetails.add(new ResultDetail("Number of wiki pages for creating project plan", String.valueOf(numberOfWikiPagesForProjectPlan)));

        if (numberOfIssuesForProjectPlan >= getMinNumberOfActivitiesWithProjectPlan() || numberOfWikiPagesForProjectPlan >= getMinNumberOfWikiPagesWithProjectPlan()) {
            resultDetails.add(new ResultDetail("Conclusion", "Found some activities or wiki pages for project plan in first two iterations"));
            return new QueryResultItem(this.antiPattern, false, resultDetails);
        } else {
            resultDetails.add(new ResultDetail("Conclusion", "No wiki pages and activities for project plan in first two iterations"));
            return new QueryResultItem(this.antiPattern, true, resultDetails);
        }
    }
}
