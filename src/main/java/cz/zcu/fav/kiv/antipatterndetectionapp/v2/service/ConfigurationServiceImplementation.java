package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.ConfigRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.UserConfigurationJoinRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.Crypto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.JSONBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigurationServiceImplementation implements ConfigService {
    //repository which represents connection to database and the configuration table in particular
    @Autowired
    private ConfigRepository configurationRepository;
    //repository for Join table, necessary to add associations between users and configurations
    @Autowired
    private UserConfigurationJoinRepository userConfigurationJoinRepository;
    //user service is also necessary for retrieving information about users (primarily database query for fetching id)
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

        Map<String,Object> json = new HashMap<>();
        String userName = user.getName();
        //fetch the user from db because user in UserConfiguration does not contain id
        user = this.userService.getUserByName(userName);
        String configurationDefinition = configuration.getConfig();
        //if the request is missing the configuration definition then we kill it
        if(configurationDefinition == null){
            json.put("message","no configuration definition provided.");
            String jsonString = JSONBuilder.buildJSON(json);
            return new ResponseEntity<>(jsonString,HttpStatus.BAD_REQUEST);
        }
        //create the hash of the configuration (w/o salting)
        String configHash = Crypto.hashString(configurationDefinition);
        Configuration existingConfiguration = this.configurationRepository.findConfigurationByConfigHash(configHash);
        //configuration definition does not exist => upload the configuration into database
        if(existingConfiguration == null){
            configuration.setHash(configHash);
            //save the configuration itself
            Configuration tmp = this.configurationRepository.save(configuration);
            //can only happen if db server fails or a constraint is breached
            if(tmp == null){
                json.put("message","fatal server failure");
                return new ResponseEntity<>(JSONBuilder.buildJSON(json),HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        //pair the configuration to the user
        pairConfigurationWithUser(user,configuration);
        json.put("message","configuration uploaded successfully");
        return new ResponseEntity<>(JSONBuilder.buildJSON(json),HttpStatus.OK);


    }

    /**
     * This method saves user and configuration id into join table
     * creates association between user and configuration in the sense of: "User @param user owns configuration @param configuration"
     * (Multiple users can own the same configuration but the configuration is not public)
     * @param user User - user who will be associated with configuration @param configuration.
     * @param configuration Configuration - the configuration that will be associated with user (just the id is necessary)
     * @return ResponseEntity<String> - Http response with status code and message about the operation
     */
    @Override
    public ResponseEntity<String> pairConfigurationWithUser(User user, Configuration configuration) {
        Map<String,Object> json = new HashMap<>();
        final UserConfigKey key = new UserConfigKey(user.getId(),configuration.getId());
        boolean exists = this.userConfigurationJoinRepository.existsById(key);
        //the configuration pairing already exists, we do not have to do anything
        //request like this should not happen from client, something fishy might be going on
        //or the request is a duplicate
        if(exists){
            json.put("message","configuration already exists in your collection!");
            return new ResponseEntity<>(JSONBuilder.buildJSON(json),HttpStatus.BAD_REQUEST);
        }
        //save the relation between user and configuration
        this.userConfigurationJoinRepository.save(new UserConfigurationJoin(key,configuration.getConfigurationName()));
        json.put("message","configuration added to collection.");
        return new ResponseEntity<>(JSONBuilder.buildJSON(json),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> getUserConfigurations(User user) {
        final String userName = user.getName();
        //client can only send his name - he obviously does not know his id in db, we have to query that
        User userInfo = this.userService.getUserByName(userName);
        //fetch all configurations this particular user can see
        //ie all public configs + configurations uploaded by this particular user
        List<Configuration> configurations = this.configurationRepository.getAllUserConfigurations(userInfo.getId());
        Map<String,Object> json = new HashMap<>();
        json.put("message","configuration retrived");
        json.put("configurations",configurations);
        String jsonString = JSONBuilder.buildJSON(json);
        return new ResponseEntity<>(jsonString,HttpStatus.OK);
    }

    @Override
    public List<String> getConfigurationNames(User user) {
        final String userName = user.getName();
        if(userName == null){
            return null;
        }
        //client can only send his name - he obviously does not know his id in db, we have to query that
        User userInfo = this.userService.getUserByName(userName);
        //fetch all configurations this particular user can see
        //ie all public configs + configurations uploaded by this particular user
        List<String> configurationNames = this.configurationRepository.getAllUserConfigurationNames(userInfo.getId());
        return configurationNames;
    }

    @Override
    public Configuration getConfigurationById(int id) {
        return this.configurationRepository.findConfigurationById(id);
    }

    @Override
    public String getConfigurationName(int userId, int configurationId) {
        return this.configurationRepository.findConfigurationByCompoundKey(new UserConfigKey(userId, configurationId));
    }
}
