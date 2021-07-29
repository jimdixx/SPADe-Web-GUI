package cz.zcu.fav.kiv.antipatterndetectionapp.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors.AntiPatternDetector;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.CacheablesValues;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResult;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.AntiPatternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Service
public class AntiPatternServiceImpl implements AntiPatternService {

    @Autowired
    private AntiPatternRepository antiPatternRepository;

    // class that stores cached values
    private CacheablesValues cacheablesValues = new CacheablesValues();

    @Override
    public List<AntiPatternDetector> getAllAntiPatterns() {
        return antiPatternRepository.getAllAntiPatterns();
    }

    @Override
    public AntiPatternDetector getAntiPatternById(Long id) {
        return antiPatternRepository.getAntiPatternById(id);
    }

    @Override
    public List<AntiPattern> antiPatternsToModel(List<AntiPatternDetector> antiPatternDetectors) {
        List<AntiPattern> antiPatterns = new LinkedList<>();
        for (AntiPatternDetector antiPatternDetector : antiPatternDetectors) {
            antiPatterns.add(antiPatternDetector.getAntiPatternModel());
        }
        return antiPatterns;
    }

    @Override
    public AntiPattern antiPatternToModel(AntiPatternDetector antiPatternDetector) {
        return antiPatternDetector.getAntiPatternModel();
    }

    @Override
    public List<AntiPatternDetector> getAllAntiPatternsForGivenIds(Long[] ids) {
        List<AntiPatternDetector> antiPatternDetectors = new ArrayList<>();

        for (Long id : ids) {
            antiPatternDetectors.add(antiPatternRepository.getAntiPatternById(id));
        }
        return antiPatternDetectors;
    }

    @Override
    public boolean saveNewConfiguration(String[] configNames, String[] configValues) {
        List<AntiPatternDetector> antiPatternDetectors = antiPatternRepository.getAllAntiPatterns();

        for (AntiPatternDetector antiPatternDetector : antiPatternDetectors) {
            // not every anti-pattern should have configuration
            if (antiPatternDetector.getAntiPatternModel().getConfigurations() == null) {
                continue;
            }
            for (int i = 0; i < configNames.length; i++) {
                if (antiPatternDetector.getAntiPatternModel().getConfigurations().containsKey(configNames[i])) {

                    if (antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).getValue().getClass() == Integer.class) {
                        try {
                            antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).setValue((Integer.parseInt(configValues[i])));
                            setConfigurationChanged(true);
                        } catch (NumberFormatException e) {
                            return false;
                        }

                    } else if (antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).getValue().getClass() == Float.class) {
                        try {
                            antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).setValue((Float.parseFloat(configValues[i])));
                            setConfigurationChanged(true);
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    } else if (antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).getValue().getClass() == Double.class) {
                        try {
                            antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).setValue((Double.parseDouble(configValues[i])));
                            setConfigurationChanged(true);
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void saveAnalyzedProjects(String[] selectedProjects, String[] selectedAntiPatterns) {
        this.cacheablesValues.setAnalyzedProjects(selectedProjects);
        this.cacheablesValues.setAnalyzedAntiPatterns(selectedAntiPatterns);
    }

    @Override
    public String[] getAnalyzedProjects() {
        return this.cacheablesValues.getAnalyzedProjects();
    }

    @Override
    public String[] getAnalyzedAntiPatterns() {
        return this.cacheablesValues.getAnalyzedAntiPatterns();
    }

    @Override
    public void setConfigurationChanged(boolean configurationChanged) {
        this.cacheablesValues.setConfigurationChanged(configurationChanged);
    }

    @Override
    public boolean isConfigurationChanged() {
        return this.cacheablesValues.isConfigurationChanged();
    }

    @Override
    public void saveResults(List<QueryResult> results) {
        this.cacheablesValues.setResults(results);
    }

    @Override
    public List<QueryResult> getResults() {
        return this.cacheablesValues.getResults();
    }
}
