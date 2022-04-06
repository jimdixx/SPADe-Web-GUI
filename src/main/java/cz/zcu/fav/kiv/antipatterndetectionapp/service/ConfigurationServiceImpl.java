package cz.zcu.fav.kiv.antipatterndetectionapp.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.Percentage;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveFloat;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveInteger;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.ConfigurationRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Override
    public List<String> getAllConfigurationNames() {
        List<String> configList = new ArrayList<String>();

        // get all external configurations
        for (String key : configurationRepository.allConfigurations.keySet() ) {
            configList.add(key);
        }

        return configList;
    }

    @Override
    public List<String> getDefaultConfigurationNames() {
        List<String> configList = new ArrayList<String>();

        // insert only default configurations
        configList.add("Default");

        return configList;
    }

    @Override
    public Map<String, Map<String, String>> getConfigurationByName(String configurationName) {
        return configurationRepository.allConfigurations.get(configurationName);
    }

    @Override
    public List<String> saveNewConfiguration(List<AntiPattern> antiPatterns, String configurationName, String[] antiPatternNames, String[] thresholdNames, String[] thresholdValues, boolean fullNewConfiguration){
        return configurationRepository.saveNewConfiguration(antiPatterns, configurationName, antiPatternNames, thresholdNames, thresholdValues, fullNewConfiguration);
    }
}
