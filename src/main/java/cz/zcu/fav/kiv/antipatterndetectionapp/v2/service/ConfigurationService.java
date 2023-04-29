package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.Configuration;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserConfiguration;
import org.springframework.http.ResponseEntity;

public interface ConfigurationService {
    //upload configuration
    ResponseEntity<String> addConfiguration(Configuration cfg);
    ResponseEntity<String> addConfiguration(UserConfiguration cfg);

    ResponseEntity<String> pairConfigurationWithUser(User user, Configuration configuration);
    //get all configurations available to user
    ResponseEntity<String> getUserConfigurations(User user);






}
