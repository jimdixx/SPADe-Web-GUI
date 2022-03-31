package cz.zcu.fav.kiv.antipatterndetectionapp.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors.AntiPatternDetector;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResult;

import java.util.List;

public interface AntiPatternService {

    List<AntiPatternDetector> getAllAntiPatterns();

    AntiPatternDetector getAntiPatternById(Long id);

    List<AntiPattern> antiPatternsToModel(List<AntiPatternDetector> antiPatternDetectors);

    AntiPattern antiPatternToModel(AntiPatternDetector antiPatternDetector);

    List<AntiPatternDetector> getAllAntiPatternsForGivenIds(Long[] ids);

    List<String> saveNewConfiguration(String[] configNames, String[] configValues);

    void saveAnalyzedProjects(String[] selectedProjects, String[] selectedAntiPatterns);

    String[] getAnalyzedProjects();

    String[] getAnalyzedAntiPatterns();

    boolean isConfigurationChanged();

    void setConfigurationChanged(boolean configurationChanged);

    void saveResults(List<QueryResult> results);

    List<QueryResult> getResults();

    List<AntiPattern> setErrorMessages(List<AntiPattern> antiPatterns, List<String> wrongParameters);

    AntiPattern setErrorMessages(AntiPattern antiPattern, List<String> wrongParameters);

    AntiPattern getAntiPatternFromJsonFile(String fileName);

    String getDescriptionFromCatalogue(long id);
}
