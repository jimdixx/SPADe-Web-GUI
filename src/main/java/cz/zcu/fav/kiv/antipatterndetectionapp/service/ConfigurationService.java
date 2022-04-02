package cz.zcu.fav.kiv.antipatterndetectionapp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ConfigurationService {

    List<String> getAllConfigurationNames();

    List<String> getDefaultConfigurationNames();

    Map<String, Map<String, String>> getConfigurationByName(String configurationName);
}
