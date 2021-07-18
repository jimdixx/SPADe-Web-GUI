package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessAsUsualDetectorImpl implements AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(BusinessAsUsualDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(3L,
            "Business As Usual",
            "BusinessAsUsual",
            "Absence of a retrospective after individual " +
                    "iterations or after the completion project.",
            new HashMap<>() {{
                put("divisionOfIterationsWithRetrospective", new Configuration<Float>("divisionOfIterationsWithRetrospective",
                        "Division of iterations with retrospective",
                        "Minimum percentage of the total number of iterations with a retrospective (0,1)", 0.66666f));
            }},
            "Business_As_Usual.md");

    private final String sqlFileName = "business_as_usual.sql";

    // sql queries loaded from sql file
    private List<String> sqlQueries;
    
    private float getDivisionOfIterationsWithRetrospective() {
        return (float) antiPattern.getConfigurations().get("divisionOfIterationsWithRetrospective").getValue();
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
     *      1) ke každé iteraci najít všechny aktivity které obsahují název "%retr%"=retrospektiva nebo "%revi%"=revize
     *      (mohlo by se dále detekovat že si na této aktivitě logují všichni členové, tato aktivita by měla být bez commitu,
     *      měla by být dokončena někdy ke konci iterace => pokud by se ale vzaly v úvahu všechny tyto atributy, tak nenajedem
     *      žádnou aktivita => příliš přísná kritéria)
     *      2) v každé iteraci by měla být alespoň jedna aktivita představující retrospektivu
     *      3) dále nalézt všechny wiki stránky, které byly upravené nebo vytvořené v dané iteraci
     *      4) zjistit jestli wiki stránka představuje nějaké poznámky z retrospektivy
     *      5) výsledky wiki stránek a aktivit dát dohromady a u každé iterace by měl být alespoň jeden záznam
     *      6) pokud nebude nalezen žádný záznam u více jak jedné třetiny iterací, tak je anti-pattern detekován
     *
     * @param project            analyzovaný project
     * @param databaseConnection databázové připojení
     * @return výsledek detekce
     */
    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection) {

        // init values
        List<ResultDetail> resultDetails = new ArrayList<>();
        Long totalNumberIterations = 0L;
        Map<String, Integer> iterationsResults = new HashMap<>();

        // projít výsledky dotazů a dát do jedné mapy => v této mapě by měly být všechny iterace
        List<List<Map<String, Object>>> resultSets = databaseConnection.executeQueriesWithMultipleResults(project, this.sqlQueries);
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

        int minRetrospectiveLimit =  Math.round(totalNumberIterations * getDivisionOfIterationsWithRetrospective());

        resultDetails.add(new ResultDetail("Min retrospective limit", String.valueOf(minRetrospectiveLimit)));
        resultDetails.add(new ResultDetail("Found retrospectives", String.valueOf(iterationsResults.size())));
        resultDetails.add(new ResultDetail("Total number of iterations", String.valueOf(totalNumberIterations)));


        if ( minRetrospectiveLimit > iterationsResults.size() ){
            resultDetails.add(new ResultDetail("Conclusion", "There is too low number of retrospectives"));
            return new QueryResultItem(this.antiPattern, true, resultDetails);
        } else {
            resultDetails.add(new ResultDetail("Conclusion", "There is right number of retrospectives"));
            return new QueryResultItem(this.antiPattern, false, resultDetails);
        }
    }
}
