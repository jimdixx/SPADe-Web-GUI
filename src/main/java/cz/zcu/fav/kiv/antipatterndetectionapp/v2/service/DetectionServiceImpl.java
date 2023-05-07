package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.AntiPatternManager;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Query;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResult;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.AntiPatternService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ConfigurationService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.Configuration;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserDetectionDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.JSONBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DetectionServiceImpl implements DetectionService{
    @Autowired
    ConfigService configService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AntiPatternService antiPatternService;

    @Autowired
    private AntiPatternManager antiPatternManager;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public Query getAllProjectsAndAntipatterns() {
        Query q = new Query(projectService.getAllProjects(), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns()));
        return q;
    }

    @Override
    public List<QueryResult> analyze(UserDetectionDto detectionRequest) {
        //User user = userService.getUserByName(detectionRequest.getUserName());
        int configurationId = detectionRequest.getConfigurationId();
        //String currentConfigurationName = this.configService.getConfigurationName(user.getId(), configurationId);
        Configuration cfg = this.configService.getConfigurationById(configurationId);

        //namapovat cfg do jsonu
        Map<String, Map<String,String>> currentConfiguration = JSONBuilder.createMapFromString(cfg.getConfig());

        String[] selectedProjects = detectionRequest.getSelectedProjects();
        String[] selectedAntiPatterns = detectionRequest.getSelectedAntipatterns();
        List<QueryResult> results = antiPatternManager.analyze(selectedProjects, selectedAntiPatterns, currentConfiguration);
        return results;
    }
}
