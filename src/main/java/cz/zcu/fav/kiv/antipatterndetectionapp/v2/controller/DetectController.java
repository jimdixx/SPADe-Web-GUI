package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller;

import com.google.gson.Gson;
import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.AntiPatternManager;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Query;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResult;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.AntiPatternService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ConfigurationService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserDetectionDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.DetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("v2/detect")
public class DetectController {
    @Autowired
    DetectionService detectionService;

    @GetMapping("/list")
    public ResponseEntity<String> getAntipatternsAndProjects() {
        //List<Project> projects = projectService.getAllProjects();
        //List<AntiPattern> antiPatterns = antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns());
        //List<AntiPattern> antiPatterns = antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns());
        Query query = detectionService.getAllProjectsAndAntipatterns();
        return ResponseEntity.ok(new Gson().toJson(query));
    }

    @PostMapping("/analyze")
    public ResponseEntity<String> detect(@RequestBody UserDetectionDto detectionRequest) {

        List<QueryResult> results = detectionService.analyze(detectionRequest);
        //Map<String, Map<String, String>> currentConfiguration = configurationService.getConfigurationByName();
        return ResponseEntity.ok(new Gson().toJson(results));

    }


}
