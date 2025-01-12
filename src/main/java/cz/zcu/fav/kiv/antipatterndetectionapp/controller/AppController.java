package cz.zcu.fav.kiv.antipatterndetectionapp.controller;


import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.AntiPatternManager;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Query;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResult;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.AntiPatternService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * This class contains all endpoints of the web application.
 */
@Controller
public class AppController {

    private final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AntiPatternService antiPatternService;

    @Autowired
    private AntiPatternManager antiPatternManager;

    /**
     *  This method is called by the GET method and initializes
     *  the main page of the application (index). Loads all projects
     *  stored in the db and all implemented AP.
     *
     * @param model object for passing data to the UI
     * @return html file name for thymeleaf template
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("query", new Query(projectService.getAllProjects(), antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns())));
        return "index";
    }

    /**
     *  Method for obtaining project by ID.
     *
     * @param id project ID
     * @param model object for passing data to the UI
     * @return html file name for thymeleaf template
     */
    @GetMapping("/projects/{id}")
    public String getProjectById(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.getProjectById(id));
        return "project";
    }

    /**
     * Method for obtaining all AP.
     * @return list of AP
     */
    @GetMapping("/anti-patterns")
    public @ResponseBody List<AntiPattern> getAllAntiPatterns() {
        return antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns());
    }

    /**
     * Method for obtaining AP by ID.
     *
     * @param id AP ID
     * @param model object for passing data to the UI
     * @return html file name for thymeleaf template
     */
    @GetMapping("/anti-patterns/{id}")
    public String getAntiPatternById(@PathVariable Long id, Model model) {
        model.addAttribute("antiPattern", antiPatternService.getAntiPatternById(id).getAntiPatternModel());
        return "anti-pattern";
    }

    /**
     * Method that processes requirements for analyzing selected projects and AP.
     *
     * @param model object for passing data to the UI
     * @param selectedProjects selected project to analyze
     * @param selectedAntiPatterns selected AP to analyze
     * @return html file name for thymeleaf template
     */
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

        List<QueryResult> results = antiPatternManager.analyze(selectedProjects, selectedAntiPatterns);
        antiPatternService.saveAnalyzedProjects(selectedProjects, selectedAntiPatterns);
        antiPatternService.saveResults(results);
        antiPatternService.setConfigurationChanged(false);

        model.addAttribute("queryResults", results);
        model.addAttribute("recalculationTime", DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.now()));

        return "result";
    }

    /**
     * Method for checking the change of configuration values ​​after pressing the back button in the browser.
     *
     * @param model object for passing data to the UI
     * @return html file name for thymeleaf template
     */
    @GetMapping("/analyze")
    public String analyzeGet(Model model) {
        if (antiPatternService.isConfigurationChanged()) {
            model.addAttribute("isConfigurationChanged", true);
        }
        antiPatternService.setConfigurationChanged(false);

        model.addAttribute("queryResults", antiPatternService.getResults());
        model.addAttribute("recalculationTime", DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.now()));

        return "result";
    }

    /**
     * Method for recalculating the results after a configuration change.
     *
     * @param model object for passing data to the UI
     * @return html file name for thymeleaf template
     */
    @GetMapping("/recalculate")
    public String recalculateGet(Model model) {
        return analyzeGet(model);
    }

    /**
     * Method for recalculating the results after a configuration change.
     *
     * @param model object for passing data to the UI
     * @return html file name for thymeleaf template
     */
    @PostMapping("/recalculate")
    public String resultRecalculate(Model model) {

        List<QueryResult> results = antiPatternManager.analyze(antiPatternService.getAnalyzedProjects(),
                antiPatternService.getAnalyzedAntiPatterns());

        antiPatternService.saveResults(results);
        antiPatternService.setConfigurationChanged(false);

        model.addAttribute("queryResults", results);
        model.addAttribute("recalculationTime", DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.now()));
        return "result";
    }

    /**
     * Method for showing about page.
     *
     * @return html file name for thymeleaf template
     */
    @GetMapping("/about")
    public String about() {
        return "about";
    }

    /**
     * Method for getting all configuration from app and set it to the model class.
     *
     * @param model object for passing data to the UI
     * @return html file name for thymeleaf template
     */
    @GetMapping("/configuration")
    public String configuration(Model model) {
        model.addAttribute("antiPatterns", antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns()));
        return "configuration";
    }

    /**
     * Method of storing new configurations for individual AP.
     *
     * @param model object for passing data to the UI
     * @param configValues changed configuration values
     * @param configNames changed configuration names
     * @return html file name for thymeleaf template
     */
    @PostMapping("/configuration")
    public String configurationPost(Model model,
                                    @RequestParam(value = "configValues", required = false) String[] configValues,
                                    @RequestParam(value = "configNames", required = false) String[] configNames) {

        List<AntiPattern> antiPatterns = antiPatternService.antiPatternsToModel(antiPatternService.getAllAntiPatterns());
        List<String> wrongParameters = antiPatternService.saveNewConfiguration(configNames, configValues);

        if (wrongParameters.isEmpty()) {
            model.addAttribute("successMessage", "All configuration values has been successfully saved.");
        } else {
            model.addAttribute("errorMessage", "One or more configuration values are not in correct format, see messages below.");
            antiPatternService.setErrorMessages(antiPatterns, wrongParameters);
        }

        model.addAttribute("antiPatterns", antiPatterns);
        return "configuration";
    }

    /**
     * Method for storing configuration values ​​for the respective AP.
     *
     * @param model object for passing data to the UI
     * @param id id of AP
     * @param configValues new config values
     * @param configNames configuration names
     * @param redirectAttrs attributes for redirection
     * @return redirected html file name for thymeleaf template
     */
    @PostMapping("/anti-patterns/{id}")
    public String antiPatternsPost(Model model,
                                   @PathVariable Long id,
                                   @RequestParam(value = "configValues", required = false) String[] configValues,
                                   @RequestParam(value = "configNames", required = false) String[] configNames,
                                   RedirectAttributes redirectAttrs) {

        AntiPattern antiPattern = antiPatternService.antiPatternToModel(antiPatternService.getAntiPatternById(id));
        List<String> wrongParameters = antiPatternService.saveNewConfiguration(configNames, configValues);

        if (wrongParameters.isEmpty()) {
            redirectAttrs.addFlashAttribute("successMessage", "All threshold values has been successfully saved.");
        } else {
            redirectAttrs.addFlashAttribute("errorMessage", "One or more configuration values are not in correct format, see messages below.");
            antiPatternService.setErrorMessages(antiPattern, wrongParameters);
        }

        model.addAttribute("antiPatterns", antiPattern);

        return "redirect:/anti-patterns/{id}";
    }


}
