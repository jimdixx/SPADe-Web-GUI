package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;

public interface AntiPatternDetector {


    Long getAntiPatternId();

    String getAntiPatternName();

    String getAntiPatternPrintName();

    String getAntiPatternDescription();

    AntiPattern getAntiPatternModel();

    QueryResultItem analyze(Project project, DatabaseConnection databaseConnection);
}
