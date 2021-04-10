package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.ResultDetail;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessAsUsualDetectorImpl extends AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(BusinessAsUsualDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(3L,
            "Business As Usual",
            "BusinessAsUsual",
            "Absence of a retrospective after individual " +
                    "iterations or after the completion project.");

    private final String sqlFileName = "business_as_usual.sql";

    /**
     * Settings
     */
    private final float DIVISION_BUSINESS_AS_USUAL_ITERATIONS = (float) 1/3;

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
     * 1) ke každé iteraci najít všechny aktivity které obsahují název "%retr%"=retrospektiva nebo "%revi%"=revize
     * (mohlo by se dále detekovat že si na této aktivitě logují všichni členové, tato aktivita by měla být bez commitu,
     * měla by být dokončena někdy ke konci iterace => pokud by se ale vzaly v úvahu všechny tyto atributy, tak nenajedem
     * žádnou aktivita => příliš přísná kritéria)
     * 2) v každé iteraci by měla být alespoň jedna aktivita představující retrospektivu
     * 3) dále nalézt všechny wiki stránky, které byly upravené nebo vytvořené v dané iteraci
     * 4) zjistit jestli wiki stránka představuje nějaké poznámky z retrospektivy
     * 5) výsledky wiki stránek a aktivit dát dohromady a u každé iterace by měl být alespoň jeden záznam
     * 6) pokud nebude nalezen žádný záznam u více jak jedné třetiny iterací, tak je anti-pattern detekován
     *
     * @param project            analyzovaný project
     * @param databaseConnection databázové připojení
     * @param queries            list sql dotazů
     * @return výsledek detekce
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection, List<String> queries) {
        Long totalNumberIterations = 0L;
        Map<String, Integer> iterationsResults = new HashMap<>();

        // projít výsledky dotazů a dát do jedné mapy => v této mapě by měly být všechny iterace
        List<List<Map<String, Object>>> resultSets = databaseConnection.executeQueriesWithMultipleResults(project, queries);
        for (int i = 0; i < resultSets.size(); i++) {
            List<Map<String, Object>> rs = resultSets.get(i);

            if (i == 0) {
                totalNumberIterations = (Long) rs.get(0).get("numberOfIterations");
            }

            if (i == 1) {
                String iterationName;
                for (Map<String, Object> map : rs) {
                    iterationName = (String) map.get("iterationName");
                    iterationsResults.put(iterationName, 1);
                }
            }

            if (i == 2) {
                String iterationName;
                for (Map<String, Object> map : rs) {
                    iterationName = (String) map.get("iterationName");
                    iterationsResults.put(iterationName, 2);
                }
            }

        }


        int minRetrospectiveLimit =  totalNumberIterations.intValue() - Math.round(totalNumberIterations * DIVISION_BUSINESS_AS_USUAL_ITERATIONS);

        List<ResultDetail> resultDetails = Utils.createResultDetailsList(
                new ResultDetail("Project id", project.getId().toString()),
                new ResultDetail("Min retrospective loimit", String.valueOf(minRetrospectiveLimit)),
                new ResultDetail("Found retrospectives", String.valueOf(iterationsResults.size())));
        LOGGER.info(this.antiPattern.getPrintName());
        LOGGER.info(resultDetails.toString());

        return new QueryResultItem(this.antiPattern, minRetrospectiveLimit > iterationsResults.size(), resultDetails);
    }
}
