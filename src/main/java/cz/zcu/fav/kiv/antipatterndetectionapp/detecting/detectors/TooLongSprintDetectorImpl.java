package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TooLongSprintDetectorImpl extends AntiPatternDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(TooLongSprintDetectorImpl.class);

    private final AntiPattern antiPattern = new AntiPattern(1L,
            "Too Long Sprint",
            "TooLongSprint",
            "Iterations too long. (ideal iteration length is about 1-2 weeks, " +
            "maximum 3 weeks). It could also be detected here if the length " +
            "of the iteration does not change often (It can change at the " +
            "beginning and at the end of the project, but it should not " +
            "change in the already started project).");

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
