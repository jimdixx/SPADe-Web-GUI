package cz.zcu.fav.kiv.antipatterndetectionapp.detecting;


import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors.AntiPatternDetector;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResult;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.AntiPatternService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AntiPatternManagerImpl implements AntiPatternManager {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AntiPatternService antiPatternService;

    @Override
    public List<QueryResult> analyze(String[] selectedProjects, String[] selectedAntiPatterns) {

        return this.analyze(antiPatternService.getAllAntiPatterns(), projectService.getAllProjects());
    }

    private List<QueryResult> analyze(List<AntiPatternDetector> antiPatternDetectors, List<Project> projects) {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        List<QueryResult> queryResults = new ArrayList<>();


        for (Project project : projects) {
            QueryResult queryResult = new QueryResult();
            queryResult.setProject(project);
            List<QueryResultItem> queryResultItems = new ArrayList<>();
            for (AntiPatternDetector antiPattern : antiPatternDetectors) {
                queryResultItems.add(antiPattern.analyze(project, databaseConnection));
            }
            queryResult.setQueryResultItems(queryResultItems);
            queryResults.add(queryResult);

        }

        databaseConnection.closeConnection();

        return queryResults;
    }
}
