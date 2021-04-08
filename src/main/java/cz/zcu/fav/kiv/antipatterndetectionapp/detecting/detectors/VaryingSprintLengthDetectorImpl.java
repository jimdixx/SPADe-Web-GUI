package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class VaryingSprintLengthDetectorImpl extends AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(VaryingSprintLengthDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(2L,
            "Varying Sprint Length",
            "VaryingSprintLength",
            "The length of the sprint changes very often. " +
            "It is clear that iterations will be different " +
                    "lengths at the beginning and end of the project, " +
                    "but the length of the sprint should not change " +
                    "during the project.");

    private final String sqlFileName = "varying_sprint_length.sql";

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
        return new QueryResultItem(this.antiPattern, true, null);
    }
}
