package cz.zcu.fav.kiv.antipatterndetectionapp.controller;


import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.AntiPatternManager;
import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.AntiPatternsEnum;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Query;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AppController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AntiPatternManager antiPatternManager;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("query", createQuery());
        return "index";
    }

    @GetMapping("/projects/{id}")
    public String getProjectById(@PathVariable Long id, Model model) {
        Optional<Project> foundProject = projectRepository.findById(id);
        if (foundProject.isPresent()) {
            Project project = foundProject.get();
            model.addAttribute("project", project);
        }
        return "project";
    }

    @GetMapping("/anti-patterns")
    public @ResponseBody
    List<AntiPattern> getAllAntiPatterns() {
        List<AntiPattern> antiPatterns = new ArrayList<>();
        for (AntiPatternsEnum antiPatternEnum : AntiPatternsEnum.values()) {
            AntiPattern antiPattern = new AntiPattern(antiPatternEnum.id, antiPatternEnum.printName, antiPatternEnum.name, antiPatternEnum.description);
            antiPatterns.add(antiPattern);
        }
        return antiPatterns;
    }

    @GetMapping("/anti-patterns/{id}")
    public String getAntiPatternById(@PathVariable Long id, Model model) {
        for (AntiPatternsEnum antiPatternEnum : AntiPatternsEnum.values()) {
            if (antiPatternEnum.id.equals(id)) {
                model.addAttribute("antiPattern", new AntiPattern(antiPatternEnum.id, antiPatternEnum.printName, antiPatternEnum.name, antiPatternEnum.description));
            }
        }
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
            model.addAttribute("query", createQuery());
            return "index";
        }

        if (selectedAntiPatterns == null) {
            model.addAttribute("errorMessage", "No anti-pattern selected." +
                    " Select at least one anti-pattern.");
            model.addAttribute("query", createQuery());
            return "index";
        }

        model.addAttribute("queryResults", antiPatternManager.analyze(createQueryToAnalyze(selectedProjects, selectedAntiPatterns)));

        return "result";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    private Query createQuery() {
        List<AntiPattern> antiPatterns = new ArrayList<>();
        for (AntiPatternsEnum antiPatternEnum : AntiPatternsEnum.values()) {
            AntiPattern antiPattern = new AntiPattern(antiPatternEnum.id, antiPatternEnum.printName, antiPatternEnum.name, antiPatternEnum.description);
            antiPatterns.add(antiPattern);
        }
        List<Project> projects = new ArrayList<>();
        for (Project project : projectRepository.findAll()) {
            projects.add(project);
        }

        return new Query(projects, antiPatterns);
    }

    private Query createQueryToAnalyze(String[] selectedProjects, String[] selectedAntiPatterns) {
        List<Project> projects = new ArrayList<>();
        for (String selectedProject : selectedProjects) {
            Optional<Project> project = projectRepository.findById(Long.parseLong(selectedProject));
            project.ifPresent(projects::add);
        }

        List<AntiPattern> antiPatterns = new ArrayList<>();
        for (String selectedAntiPattern : selectedAntiPatterns) {
            for (AntiPatternsEnum antiPatternEnum : AntiPatternsEnum.values()) {
                if (antiPatternEnum.id.equals(Long.parseLong(selectedAntiPattern))) {
                    antiPatterns.add(new AntiPattern(antiPatternEnum.id, antiPatternEnum.printName, antiPatternEnum.name, antiPatternEnum.description));
                }
            }
        }

        return new Query(projects, antiPatterns);
    }
}
