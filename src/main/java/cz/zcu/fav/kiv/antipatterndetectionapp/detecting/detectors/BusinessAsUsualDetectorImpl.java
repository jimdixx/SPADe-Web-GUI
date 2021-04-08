package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BusinessAsUsualDetectorImpl extends AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(BusinessAsUsualDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(3L,
            "Business As Usual",
            "BusinessAsUsual",
            "Absence of a retrospective after individual " +
                    "iterations or after the completion project.");

    private final String sqlFileName = "too_long_sprint.sql";

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

        return new QueryResultItem(this.antiPattern, false, null);
    }
}
