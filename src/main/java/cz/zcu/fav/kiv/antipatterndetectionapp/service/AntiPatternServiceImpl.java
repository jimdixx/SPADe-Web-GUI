package cz.zcu.fav.kiv.antipatterndetectionapp.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors.AntiPatternDetector;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.CacheablesValues;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Configuration;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResult;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.Percentage;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveFloat;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveInteger;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.AntiPatternRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


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
            AntiPattern antiPattern = antiPatternDetector.getAntiPatternModel();

            // set to default value
            for (Map.Entry<String, Configuration> config : antiPattern.getConfigurations().entrySet()) {
                config.getValue().setErrorMessageShown(false);
            }
            antiPatterns.add(antiPattern);
        }
        return antiPatterns;
    }

    @Override
    public AntiPattern antiPatternToModel(AntiPatternDetector antiPatternDetector) {
        AntiPattern antiPattern = antiPatternDetector.getAntiPatternModel();
        // set to default value
        for (Map.Entry<String, Configuration> config : antiPattern.getConfigurations().entrySet()) {
            config.getValue().setErrorMessageShown(false);
        }
        return antiPattern;
    }

    @Override
    public List<AntiPatternDetector> getAllAntiPatternsForGivenIds(Long[] ids) {
        List<AntiPatternDetector> antiPatternDetectors = new ArrayList<>();

        for (Long id : ids) {
            antiPatternDetectors.add(antiPatternRepository.getAntiPatternById(id));
        }
        return antiPatternDetectors;
    }

    public List<String> saveNewConfiguration(String[] configNames, String[] configValues) {
        List<AntiPatternDetector> antiPatternDetectors = antiPatternRepository.getAllAntiPatterns();
        List<String> incorrectParameters = new ArrayList<>();

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
                            incorrectParameters.add(configNames[i]);
                        }

                    } else if (antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).getValue().getClass() == Percentage.class) {
                        try {
                            antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).setValue((Percentage.parsePercentage(configValues[i])));
                            setConfigurationChanged(true);
                        } catch (NumberFormatException e) {
                            incorrectParameters.add(configNames[i]);
                        }
                    } else if (antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).getValue().getClass() == PositiveInteger.class) {
                        try {
                            antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).setValue((PositiveInteger.parsePositiveInteger(configValues[i])));
                            setConfigurationChanged(true);
                        } catch (NumberFormatException e) {
                            incorrectParameters.add(configNames[i]);
                        }
                    } else if (antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).getValue().getClass() == PositiveFloat.class) {
                        try {
                            antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).setValue((PositiveFloat.parsePositiveFloat(configValues[i])));
                            setConfigurationChanged(true);
                        } catch (NumberFormatException e) {
                            incorrectParameters.add(configNames[i]);
                        }
                    } else if (antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).getValue().getClass() == Float.class) {
                        try {
                            antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).setValue((Float.parseFloat(configValues[i])));
                            setConfigurationChanged(true);
                        } catch (NumberFormatException e) {
                            incorrectParameters.add(configNames[i]);
                        }
                    } else if (antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).getValue().getClass() == Double.class) {
                        try {
                            antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).setValue((Double.parseDouble(configValues[i])));
                            setConfigurationChanged(true);
                        } catch (NumberFormatException e) {
                            incorrectParameters.add(configNames[i]);
                        }
                    } else if (antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).getValue().getClass() == String.class) {
                        if (Utils.checkStringSubstrings(configValues[i])) {
                            antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).setValue((configValues[i]));
                            setConfigurationChanged(true);

                        } else {
                            incorrectParameters.add(configNames[i]);
                        }
                    }
                }
            }
        }
        return incorrectParameters;
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
    public boolean isConfigurationChanged() {
        return this.cacheablesValues.isConfigurationChanged();
    }

    @Override
    public void setConfigurationChanged(boolean configurationChanged) {
        this.cacheablesValues.setConfigurationChanged(configurationChanged);
    }

    @Override
    public void saveResults(List<QueryResult> results) {
        this.cacheablesValues.setResults(results);
    }

    @Override
    public List<QueryResult> getResults() {
        return this.cacheablesValues.getResults();
    }

    @Override
    public List<AntiPattern> setErrorMessages(List<AntiPattern> antiPatterns, List<String> wrongParameters) {
        for (AntiPattern antiPattern : antiPatterns) {
            for (Map.Entry<String, Configuration> config : antiPattern.getConfigurations().entrySet()) {
                if (wrongParameters.contains(config.getKey())) {
                    config.getValue().setErrorMessageShown(true);
                } else {
                    config.getValue().setErrorMessageShown(false);
                }
            }
        }
        return antiPatterns;
    }

    @Override
    public AntiPattern setErrorMessages(AntiPattern antiPattern, List<String> wrongParameters) {
        List<AntiPattern> antiPatterns = new ArrayList<>();
        antiPatterns.add(antiPattern);
        return setErrorMessages(antiPatterns, wrongParameters).get(0);
    }
}
