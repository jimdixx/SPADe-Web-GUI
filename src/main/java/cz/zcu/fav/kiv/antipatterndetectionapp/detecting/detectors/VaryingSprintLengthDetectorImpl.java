package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;

public class VaryingSprintLengthDetectorImpl implements AntiPatternDetector {

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
    public Long getAntiPatternId() {
        return this.antiPattern.getId();
    }

    @Override
    public String getAntiPatternName() {
        return this.antiPattern.getName();
    }

    @Override
    public String getAntiPatternPrintName() {
        return this.antiPattern.getPrintName();
    }

    @Override
    public String getAntiPatternDescription() {
        return this.antiPattern.getDescription();
    }

    @Override
    public AntiPattern getAntiPatternModel() {
        return this.antiPattern;
    }

    @Override
    public QueryResultItem analyze(Project project, DatabaseConnection databaseConnection) {
        return new QueryResultItem(this.antiPattern, true, null);
    }
}
