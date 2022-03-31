package cz.zcu.fav.kiv.antipatterndetectionapp.service;

import java.util.List;

public interface ConfigurationService {

    List<String> getAllConfigurationNames();

    List<String> getDefaultConfigurationNames();


}
