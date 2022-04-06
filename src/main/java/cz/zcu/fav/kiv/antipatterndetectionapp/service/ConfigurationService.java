package cz.zcu.fav.kiv.antipatterndetectionapp.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;

import java.util.List;
import java.util.Map;

public interface ConfigurationService {

    /**
     * Method for getting all configuration names
     *
     * @return List of the configuration
     */
    List<String> getAllConfigurationNames();

    /**
     * Method for getting default configuration names
     * Default configurations cannot be changed in the app
     *
     * @return List of the default configuration
     */
    List<String> getDefaultConfigurationNames();

    Map<String, Map<String, String>> getConfigurationByName(String configurationName);

    /**
     * Method for saving new configuration to the map structure and json file
     *
     * @param antiPatterns List of all anti-patterns
     * @param configurationName Configuration name
     * @param antiPatternNames List of anti-pattern names
     * @param thresholdNames List of threshold names
     * @param thresholdValues List of threshold values
     * @param fullNewConfiguration True if the whole new configuration is created
     * @return List of incorrectly set values
     */
    List<String> saveNewConfiguration(List<AntiPattern> antiPatterns, String configurationName, String[] antiPatternNames, String[] thresholdNames, String[] thresholdValues, boolean fullNewConfiguration);
}
