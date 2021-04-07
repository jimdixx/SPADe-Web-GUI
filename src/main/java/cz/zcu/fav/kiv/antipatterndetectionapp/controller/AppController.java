package cz.zcu.fav.kiv.antipatterndetectionapp.controller;


import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.AntiPatternManager;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Query;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.AntiPatternService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AppController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AntiPatternService antiPatternService;

    @Autowired
    private AntiPatternManager antiPatternManager;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("query", new Query(projectService.getAllProjects(), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns())));
        return "index";
    }

    @GetMapping("/projects/{id}")
    public String getProjectById(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.getProjectById(id));
        return "project";
    }

    @GetMapping("/anti-patterns")
    public @ResponseBody
    List<AntiPattern> getAllAntiPatterns() {
        return antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns());
    }

    @GetMapping("/anti-patterns/{id}")
    public String getAntiPatternById(@PathVariable Long id, Model model) {
        model.addAttribute("antiPattern", antiPatternService.getAntiPatternById(id).getAntiPatternModel());
        return "anti-pattern";
    }

    @PostMapping("/analyze")
    public String analyze(Model model,
                          @RequestParam(value = "selectedProjects", required = false) String[] selectedProjects,
                          @RequestParam(value = "selectedAntiPatterns", required = false) String[] selectedAntiPatterns
    ) {

        if (selectedProjects == null) {
            model.addAttribute("errorMessage", "No project selected." +
                    " Select at least one project.");
            model.addAttribute("query", new Query(projectService.getAllProjects(), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns())));
            return "index";
        }

        if (selectedAntiPatterns == null) {
            model.addAttribute("errorMessage", "No anti-pattern selected." +
                    " Select at least one anti-pattern.");
            model.addAttribute("query", new Query(projectService.getAllProjects(), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns())));
            return "index";
        }

        model.addAttribute("queryResults", antiPatternManager.analyze(selectedProjects, selectedAntiPatterns));

        return "result";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
