package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.ResultDetail;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LongOrNonExistentFeedbackLoopsDetectorImpl implements AntiPatternDetector {

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
    // sql queries loaded from sql file
    private List<String> sqlQueries;

    /**
     * SETTINGS
     */

    @Override
    public AntiPattern getAntiPatternModel() {
        return this.antiPattern;
    }

    @Override
    public String getAntiPatternSqlFileName() {
        return this.SQL_FILE_NAME;
    }

    @Override
    public void setSqlQueries(List<String> queries) {
        this.sqlQueries = queries;
    }

    /**
     * Postup detekce:
     * 1) najít všechny aktivity, které by mohli představovat zákaznické demo (název bude obsahovat substring)
     * 2) zjistit průměrnou délku iterací
     * 3) zjistit počet iterací
     * 4) nejprve porovnat s počtem iterací => iterace a nalezené aktivity by se měly ideálně rovnat (mohou být menší i větší ale né o moc menší)
     * 5) u každých dvou po sobě jdoucích aktivitách udělat rozdíl datumů a porovnat s průměrnou délkou iterace => rozdíl by se neměl moc lišit od průěrné délky iterace
     * 6) pokud u bodu 4) dojde k detekci máleho počtu nalezených aktivit (tým nedělá aktivity na schůzky a může zaznamenávat pouze do wiki)
     * 7) najít všechny wiki stránky a udělat join kdy se měnily (může být použita jedná stránka pro více schůzek) s příslušným názvem
     * 8) udělat group podle dne
     *
     * @param project            analyzovaný project
     * @param databaseConnection databázové připojení
     * @return výsledek detekce
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection) {

        // init values
        long totalNumberIterations = 0;
        int averageIterationLength = 0;
        int numberOfIterationsWhichContainsAtLeastOneActivityForFeedback = 0;
        int numberOfIterationsWhichContainsAtLeastOneWikiPageForFeedback = 0;
        List<Date> feedbackActivityEndDates = new ArrayList<>();
        List<Date> feedbackWikiPagesEndDates = new ArrayList<>();
        Date projectStartDate = null;
        Date projectEndDate = null;

        List<List<Map<String, Object>>> resultSets = databaseConnection.executeQueriesWithMultipleResults(project, this.sqlQueries);
        for (int i = 0; i < resultSets.size(); i++) {
            List<Map<String, Object>> rs = resultSets.get(i);

            switch (i) {
                case 0:
                    totalNumberIterations = (long) rs.get(0).get("numberOfIterations");
                    break;
                case 1:
                    averageIterationLength = ((BigDecimal) rs.get(0).get("averageIterationLength")).intValue();
                    break;
                case 2:
                    if (rs.size() != 0) {
                        numberOfIterationsWhichContainsAtLeastOneActivityForFeedback = ((Long) rs.get(0).get("totalCountOfIterationsWithFeedbackActivity")).intValue();
                    }
                    break;
                case 3:
                    Date activityEndDate;
                    for (Map<String, Object> map : rs) {
                        activityEndDate = (Date) map.get("endDate");
                        feedbackActivityEndDates.add(activityEndDate);
                    }
                    break;
                case 4:
                    projectStartDate = (Date) rs.get(0).get("startDate");
                    break;
                case 5:
                    projectEndDate = (Date) rs.get(0).get("endDate");
                    break;
                case 6:
                    numberOfIterationsWhichContainsAtLeastOneWikiPageForFeedback = rs.size();
                    Date wikiPageEndDate;
                    for (Map<String, Object> map : rs) {
                        wikiPageEndDate = (Date) map.get("appointmentDate");
                        feedbackWikiPagesEndDates.add(wikiPageEndDate);
                    }
                    break;
                default:

            }
        }

        double halfNumberOfIterations = totalNumberIterations / 2.0;

        // pokud je počet iterací, které obsahují alespoň jednu aktivitu s feedbackem, tak je to ideální případ
        if (totalNumberIterations <= numberOfIterationsWhichContainsAtLeastOneActivityForFeedback) {
            List<ResultDetail> resultDetails = Utils.createResultDetailsList(
                    new ResultDetail("Number of iterations", Long.toString(totalNumberIterations)),
                    new ResultDetail("Number of iterations with feedback loops", Integer.toString(numberOfIterationsWhichContainsAtLeastOneActivityForFeedback)),
                    new ResultDetail("Conclusion", "In each iteration is at least one activity that represents feedback loop"));


            return new QueryResultItem(this.antiPattern, false, resultDetails);

            // pokud alespoň v polovině iteracích došlo ke kontaktu se zákazníkem => zkontrolovat rozestupy
        } else if (feedbackActivityEndDates.size() > halfNumberOfIterations) {

            Date firstDate = projectStartDate;
            Date secondDate = null;

            for (Date feedbackActivityDate : feedbackActivityEndDates) {
                secondDate = feedbackActivityDate;
                long daysBetween = Utils.daysBetween(firstDate, secondDate);
                firstDate = secondDate;

                if (daysBetween >= 2 * averageIterationLength) {
                    List<ResultDetail> resultDetails = Utils.createResultDetailsList(
                            new ResultDetail("Days between", Long.toString(daysBetween)),
                            new ResultDetail("Average iteration length", Integer.toString(averageIterationLength)),
                            new ResultDetail("Conclusion", "Customer feedback loop is too long"));


                    return new QueryResultItem(this.antiPattern, true, resultDetails);
                }
            }

            // rozestupy feedbacků jsou ok
            List<ResultDetail> resultDetails = Utils.createResultDetailsList(
                    new ResultDetail("Average iteration length", Integer.toString(averageIterationLength)),
                    new ResultDetail("Conclusion", "Customer feedback has been detected and there is not too much gap between them"));


            return new QueryResultItem(this.antiPattern, false, resultDetails);

            // bylo nalezeno příliš málo aktivit => zkusit se podívat ve wiki stránkách
        } else {

            // pokud je v každé iteraci alespoň jeda wiki stránka naznačující schůzku => ideální případ
            if (numberOfIterationsWhichContainsAtLeastOneWikiPageForFeedback >= totalNumberIterations) {
                List<ResultDetail> resultDetails = Utils.createResultDetailsList(
                        new ResultDetail("Number of iterations", Long.toString(totalNumberIterations)),
                        new ResultDetail("Number of iterations with feedback loops", Integer.toString(numberOfIterationsWhichContainsAtLeastOneWikiPageForFeedback)),
                        new ResultDetail("Conclusion", "In each iteration is created/edited at least one wiki page that represents feedback loop"));

                return new QueryResultItem(this.antiPattern, false, resultDetails);
            }

            // pokud alespoň v polovině iteracích došlo ke kontaktu se zákazníkem => zkontrolovat rozestupy
            Date firstDate = projectStartDate;
            Date secondDate = null;

            for (Date feedbackWikipagesDate : feedbackWikiPagesEndDates) {
                secondDate = feedbackWikipagesDate;
                long daysBetween = Utils.daysBetween(firstDate, secondDate);
                firstDate = secondDate;

                if (daysBetween >= 2 * averageIterationLength) {
                    List<ResultDetail> resultDetails = Utils.createResultDetailsList(
                            new ResultDetail("Days between", Long.toString(daysBetween)),
                            new ResultDetail("Average iteration length", Integer.toString(averageIterationLength)),
                            new ResultDetail("Conclusion", "Customer feedback loop is too long"));


                    return new QueryResultItem(this.antiPattern, true, resultDetails);
                }
            }
            // rozestupy feedbacků jsou ok
            List<ResultDetail> resultDetails = Utils.createResultDetailsList(
                    new ResultDetail("Average iteration length", Integer.toString(averageIterationLength)),
                    new ResultDetail("Conclusion", "Customer feedback has been detected and there is not too much gap between them"));


            return new QueryResultItem(this.antiPattern, false, resultDetails);
        }
    }
}
