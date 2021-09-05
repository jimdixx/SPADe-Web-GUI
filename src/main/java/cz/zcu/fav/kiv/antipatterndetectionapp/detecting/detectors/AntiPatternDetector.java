package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;

import java.util.List;

public interface AntiPatternDetector {

    AntiPattern getAntiPatternModel();

    List<String> getSqlFileNames();

    void setSqlQueries(List<String> queries);

    QueryResultItem analyze(Project project, DatabaseConnection databaseConnection);
}
