package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.ResultDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class NinetyNinetyRuleDetectorImpl extends AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(BusinessAsUsualDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(7L,
            "Ninety Ninety Rule",
            "NinetyNinetyRule",
            "TODO");

    private final String sqlFileName = "ninety_ninety_rule.sql";

    @Override
    public AntiPattern getAntiPatternModel() {
        return this.antiPattern;
    }

    @Override
    public String getAntiPatternSqlFileName() {
        return this.sqlFileName;
    }

    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection, List<String> sql) {
        List<ResultDetail> resultDetails = new ArrayList<>();
        return new QueryResultItem(this.antiPattern, false, resultDetails);
    }
}
