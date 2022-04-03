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
import java.util.HashMap;
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
    public List<String> saveNewConfiguration(List<AntiPattern> antiPatterns, String configurationName, String[] antiPatternNames, String[] thresholdNames, String[] thresholdValues){
        Map<String, Map<String, String>> newConfiguration = new HashMap<>();
        List<String> incorrectParameters = new ArrayList<>();

        for(AntiPattern antiPattern : antiPatterns){
            if(antiPattern.getThresholds() == null)
                continue;

            for(int i = 0; i < thresholdNames.length; i++){
                if(antiPattern.getThresholds().containsKey(thresholdNames[i])){
                    if(antiPattern.getThresholds().get(thresholdNames[i]).getValue().getClass() == Integer.class){
                        try {
                            Integer.parseInt(thresholdValues[i]);
                        }
                        catch(NumberFormatException e){
                            incorrectParameters.add(thresholdNames[i]);
                        }
                    }
                    else if(antiPattern.getThresholds().get(thresholdNames[i]).getValue().getClass() == Percentage.class){
                        try {
                            Percentage.parsePercentage(thresholdValues[i]);
                        }
                        catch(NumberFormatException e){
                            incorrectParameters.add(thresholdNames[i]);
                        }
                    }
                    else if(antiPattern.getThresholds().get(thresholdNames[i]).getValue().getClass() == PositiveInteger.class){
                        try {
                            PositiveInteger.parsePositiveInteger(thresholdValues[i]);
                        }
                        catch(NumberFormatException e){
                            incorrectParameters.add(thresholdNames[i]);
                        }
                    }
                    else if(antiPattern.getThresholds().get(thresholdNames[i]).getValue().getClass() == PositiveFloat.class){
                        try {
                            PositiveFloat.parsePositiveFloat(thresholdValues[i]);
                        }
                        catch(NumberFormatException e){
                            incorrectParameters.add(thresholdNames[i]);
                        }
                    }
                    else if(antiPattern.getThresholds().get(thresholdNames[i]).getValue().getClass() == Float.class){
                        try {
                            Float.parseFloat(thresholdValues[i]);
                        }
                        catch(NumberFormatException e){
                            incorrectParameters.add(thresholdNames[i]);
                        }
                    }
                    else if(antiPattern.getThresholds().get(thresholdNames[i]).getValue().getClass() == Double.class){
                        try {
                            Double.parseDouble(thresholdValues[i]);
                        }
                        catch(NumberFormatException e){
                            incorrectParameters.add(thresholdNames[i]);
                        }
                    }
                    else if(antiPattern.getThresholds().get(thresholdNames[i]).getValue().getClass() == String.class){
                        if (Utils.checkStringSubstrings(thresholdValues[i]) == false) {
                            incorrectParameters.add(thresholdNames[i]);
                        }
                    }
                }
            }
        }

        if(incorrectParameters.size() != 0)
            return incorrectParameters;

        for(int i = 0; i < antiPatternNames.length; i++){
            if(newConfiguration.get(antiPatternNames[i]) == null)
                newConfiguration.put(antiPatternNames[i], new HashMap<>());

            newConfiguration.get(antiPatternNames[i]).put(thresholdNames[i], thresholdValues[i]);
        }

        if(configurationRepository.allConfigurations.get(configurationName) == null)
            configurationRepository.allConfigurations.put(configurationName, newConfiguration);
        else
            configurationRepository.allConfigurations.replace(configurationName, newConfiguration);

        configurationRepository.saveConfigurationToFile(configurationName, newConfiguration);

        return incorrectParameters;
    }
}
