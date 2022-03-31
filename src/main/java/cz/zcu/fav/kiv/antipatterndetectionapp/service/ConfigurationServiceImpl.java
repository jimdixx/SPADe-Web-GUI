package cz.zcu.fav.kiv.antipatterndetectionapp.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Override
    public List<String> getAllConfigurationNames() {
        List<String> configList = new ArrayList<String>();

        // get all configurations
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

}
