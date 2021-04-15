package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;

import java.util.List;

public abstract class AntiPatternDetector {

    public abstract AntiPattern getAntiPatternModel();

    public abstract String getAntiPatternSqlFileName();

    public abstract void setSqlQueries(List<String> queries);

    public abstract QueryResultItem analyze(Project project, DatabaseConnection databaseConnection);
}
