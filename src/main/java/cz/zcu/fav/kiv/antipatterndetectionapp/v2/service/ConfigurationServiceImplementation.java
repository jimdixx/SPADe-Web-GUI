package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.ConfigRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.UserConfigurationJoinRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.UserRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.Crypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigurationServiceImplementation implements ConfigurationService{

    @Autowired
    private ConfigRepository configurationRepository;
    @Autowired
    private UserConfigurationJoinRepository userConfigurationJoinRepository;

    @Autowired
    private UserService userService;
    /**
     * Saves configuration to database
     * the configuration is default to everyone
     * @param cfg Configuration - JSON format of configuration with antipatterns and thresholds
     * @return ResponseEntity with status code and message with more information about operation
     */
    @Override
    public ResponseEntity<String> addConfiguration(Configuration cfg) {
        return null;
    }
    /**
     * Saves configuration to database
     * the configuration is available to the user who sent the request
     * @param cfg Configuration - JSON format of configuration with antipatterns and thresholds
     * @return ResponseEntity with status code and message with more information about operation
     */
    @Override
    public ResponseEntity<String> addConfiguration(UserConfiguration cfg) {
        User user = cfg.getUser();
        Configuration configuration = cfg.getConfiguration();

        String userName = user.getName();
        user = this.userService.getUserByName(userName);
        String configurationDefinition = configuration.getConfig();
        if(configurationDefinition == null){
            //todo konfigurace neni poslana, chyba requestu

        }
        String configHash = Crypto.hashString(configurationDefinition);
        Configuration existingConfiguration = this.configurationRepository.findConfigurationByConfigHash(configHash);
        //configuration definition does not exist => upload the configuration into database
        if(existingConfiguration == null){
            configuration.setHash(configHash);
            //save the configuration itself
            Configuration tmp = this.configurationRepository.save(configuration);
            if(tmp == null){
                //todo selhal insert do databaze
            }
        }

        //pair the configuration to the user
        return pairConfigurationWithUser(user,configuration);


    }

    @Override
    public ResponseEntity<String> pairConfigurationWithUser(User user, Configuration configuration) {
        final UserConfigKey key = new UserConfigKey(user.getId(),configuration.getId());
        boolean exists = this.userConfigurationJoinRepository.existsById(key);
        if(exists){
            //todo uzivatel se chce sparovat s konfiguraci se kterou je jiz sparovan, jenom poslat ok nebo error
        }
        //save the relation between user and configuration
        this.userConfigurationJoinRepository.save(new UserConfigurationJoin(key));
        return null;
    }

    @Override
    public ResponseEntity<String> getUserConfigurations(User user) {
        final String userName = user.getName();
        //client can only send his name - he obviously does not know his id in db, we have to query that
        User userInfo = this.userService.getUserByName(userName);
        //fetch all configurations this particular user can see
        //ie all public configs + configurations uploaded by this particular user
        List<Configuration> configurations = this.configurationRepository.getAllUserConfigurations(userInfo.getId());
        return null;
    }
}
