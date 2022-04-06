package cz.zcu.fav.kiv.antipatterndetectionapp.detecting;


import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResult;

import java.util.List;
import java.util.Map;

public interface AntiPatternManager {
    /**
     * Method for processing analysis of selected anti-patterns on selected project with certain configuration
     *
     * @param selectedProjects Projects to analyze
     * @param selectedAntiPatterns Anti-patterns to detect
     * @param configuration Current configuration
     * @return Results of analysis stored in a list
     */
    List<QueryResult> analyze(String[] selectedProjects, String[] selectedAntiPatterns, Map<String, Map<String, String>> configuration);
}
