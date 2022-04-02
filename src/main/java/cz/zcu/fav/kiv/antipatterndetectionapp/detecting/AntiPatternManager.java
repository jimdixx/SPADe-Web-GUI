package cz.zcu.fav.kiv.antipatterndetectionapp.detecting;


import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResult;

import java.util.List;
import java.util.Map;

public interface AntiPatternManager {

    List<QueryResult> analyze(String[] selectedProjects, String[] selectedAntiPatterns, Map<String, Map<String, String>> configuration);
}
