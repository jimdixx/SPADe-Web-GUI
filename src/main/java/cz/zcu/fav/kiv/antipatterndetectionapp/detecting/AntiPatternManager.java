package cz.zcu.fav.kiv.antipatterndetectionapp.detecting;


import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResult;

import java.util.List;

public interface AntiPatternManager {

    List<QueryResult> analyze(String[] selectedProjects, String[] selectedAntiPatterns);
}
