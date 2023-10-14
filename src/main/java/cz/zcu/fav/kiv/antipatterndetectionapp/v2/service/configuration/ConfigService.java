package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.configuration;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.dials.ConfigurationControllerStatusCodes;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.Configuration;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserConfiguration;

import java.util.List;
public interface ConfigService {
    //upload configuration
    ConfigurationControllerStatusCodes addConfiguration(Configuration cfg);
    ConfigurationControllerStatusCodes addConfiguration(UserConfiguration cfg);

    ConfigurationControllerStatusCodes pairConfigurationWithUser(User user, Configuration configuration);


    List<Configuration> getUserConfigurations(User user);

    Configuration getConfigurationById(int id);
    String getConfigurationName(int userId, int configurationId);


}
