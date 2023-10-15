package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.configuration;

import com.google.gson.Gson;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.dials.ConfigurationControllerStatusCodes;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.ConfigRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.UserConfigurationJoinRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.user.UserService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.Crypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public ConfigurationControllerStatusCodes addConfiguration(Configuration cfg) {
        return null;
    }
    /**
     * Saves configuration to database
     * the configuration is available to the user who sent the request
     * @param cfg Configuration - JSON format of configuration with antipatterns and thresholds
     * @return ResponseEntity with status code and message with more information about operation
     */
    @Override
    public ConfigurationControllerStatusCodes addConfiguration(UserConfiguration cfg) {
        Configuration configuration = parseUserConfiguration(cfg);
        //if the request is missing the configuration definition then we kill it
        if (configuration == null) {
            return ConfigurationControllerStatusCodes.EMPTY_CONFIGURATION_DEFINITION;
        }
        User user = cfg.getUser();
        String userName = user.getName();
        //fetch the user from db because user in UserConfiguration does not contain id
        user = this.userService.getUserByName(userName);

        //create the hash of the configuration (w/o salting)
        String configHash = Crypto.hashString(configuration.getConfig());
        Configuration existingConfiguration = this.configurationRepository.findConfigurationByConfigHash(configHash);
        //configuration definition does not exist => upload the configuration into database
        if (existingConfiguration == null) {
            configuration.setHash(configHash);
            //save the configuration itself
            this.configurationRepository.save(configuration);
            //can only happen if db server fails or a constraint is breached
            if (configuration == null) {
                return ConfigurationControllerStatusCodes.INSERT_FAILED;
            }
        }
        //already exists, update pointer to instance with id
        else {
            configuration = existingConfiguration;
        }
        //pair the configuration to the user
        pairConfigurationWithUser(user,configuration);
        return ConfigurationControllerStatusCodes.INSERT_SUCCESSFUL;
    }

    /**
     * Parser for user request wrapped around configuration
     * @param cfg UserConfiguration instance - wrapper around user request
     * @return Configuration or Null - null if the request wrapper does not containt json defition of antipatterns
     *         Instace of Configuration if the request is Okay
     */
    private Configuration parseUserConfiguration(UserConfiguration cfg){
        ConfigurationDto configurationDto = cfg.getConfiguration();
        String configurationDefinition = new Gson().toJson(configurationDto);
        String userConfigurationName = cfg.getConfigurationName();
        String defaultConfigName = null;
        if (configurationDefinition == null) {
            return null;
        }
        if (userConfigurationName == null) {
            defaultConfigName = "ahoj_svete";
        }

        return new Configuration(configurationDefinition,null,cfg.getIsDefault(),userConfigurationName,defaultConfigName);
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
    public ConfigurationControllerStatusCodes pairConfigurationWithUser(User user, Configuration configuration) {
        final UserConfigKey key = new UserConfigKey(user.getId(),configuration.getId());
        boolean exists = this.userConfigurationJoinRepository.existsById(key);
        //the configuration pairing already exists, we do not have to do anything
        //request like this should not happen from client, something fishy might be going on
        //or the request is a duplicate
        if (exists) {
            return ConfigurationControllerStatusCodes.CONFIGURATION_PAIRING_EXISTS;
        }
        //save the relation between user and configuration
        this.userConfigurationJoinRepository.save(new UserConfigurationJoin(key,configuration.getConfigurationName()));
        return ConfigurationControllerStatusCodes.CONFIGURATION_PAIRING_CREATED;
    }


    /**
     * Method queries db to retrieve all configurations available to @param user
     * if he/she does not have any of his/her (inclusive for netflix) only default configurations are returned
     * @param user user querying configuration - ONLY username is valid in this object
     * @return List<Configuration> list of available configurations
     */
    @Override
    public List<Configuration> getUserConfigurations(User user) {
        final String userName = user.getName();
        if (userName == null) {
            return null;
        }
        //client can only send his name - he obviously does not know his id in db, we have to query that
        User userInfo = this.userService.getUserByName(userName);
        //fetch all configurations this particular user can see
        //ie all public configs + configurations uploaded by this particular user
        List<Object[]> configurations = this.configurationRepository.getAllUserConfigurations(userInfo.getId());
        List<Configuration> configurationList = new ArrayList<>();
        for (Object[] o : configurations) {
            Configuration cof = (Configuration) (o[0]);
            UserConfigurationJoin cofJoin = (UserConfigurationJoin) (o[1]);
            if (cofJoin != null) {
                cof.setConfigurationName(cofJoin.getConfigurationName());
            }
            configurationList.add(cof);
        }
        return configurationList;
    }
    //Wrappers around db connection to configuration table
    @Override
    public Configuration getConfigurationById(int id) {
        return this.configurationRepository.findConfigurationById(id);
    }

    @Override
    public String getConfigurationName(int userId, int configurationId) {
        return this.configurationRepository.findConfigurationByCompoundKey(new UserConfigKey(userId, configurationId));
    }
}
