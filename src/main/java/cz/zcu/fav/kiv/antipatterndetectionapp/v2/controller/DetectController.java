package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller;

import com.google.gson.Gson;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Query;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.AntiPatternService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v2/detect")
public class DetectController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private AntiPatternService antiPatternService;

    @GetMapping("/list")
    public ResponseEntity<String> getAntipatternsAndProjects(){
        //List<Project> projects = projectService.getAllProjects();
        //List<AntiPattern> antiPatterns = antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns());
        //List<AntiPattern> antiPatterns = antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns());
        return ResponseEntity.ok(new Gson().toJson( new Query(projectService.getAllProjects(), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns()))));
    }

}
