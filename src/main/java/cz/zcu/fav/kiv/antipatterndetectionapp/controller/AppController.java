package cz.zcu.fav.kiv.antipatterndetectionapp.controller;


import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.AntiPatternManager;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Query;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.AntiPatternService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AppController {

    private final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

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

    @GetMapping("/configuration")
    public String configuration(Model model) {
        model.addAttribute("antiPatterns", antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns()));
        return "configuration";
    }

    @PostMapping("/configuration")
    public String configurationPost(Model model,
                                    @RequestParam(value = "configValues", required = false) String[] configValues,
                                    @RequestParam(value = "configNames", required = false) String[] configNames) {

        if (antiPatternService.saveNewConfiguration(configNames, configValues)) {
            model.addAttribute("successMessage", "All threshold values has been successfully saved.");
        } else {
            model.addAttribute("errorMessage", "One or more configuration values are not in correct format");
        }

        model.addAttribute("antiPatterns", antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns()));

        return "configuration";
    }

    @PostMapping("/anti-patterns/{id}")
    public String antiPatternsPost(Model model,
                                   @PathVariable Long id,
                                   @RequestParam(value = "configValues", required = false) String[] configValues,
                                   @RequestParam(value = "configNames", required = false) String[] configNames,
                                   RedirectAttributes redirectAttrs) {

        if (antiPatternService.saveNewConfiguration(configNames, configValues)) {
            redirectAttrs.addFlashAttribute("successMessage", "All threshold values has been successfully saved.");
        } else {
            redirectAttrs.addFlashAttribute("errorMessage", "One or more configuration values are not in correct format");
        }

        model.addAttribute("antiPatterns", antiPatternService.antiPatternToModel(antiPatternService.getAntiPatternById(id)));

        return "redirect:/anti-patterns/{id}";
    }


}
