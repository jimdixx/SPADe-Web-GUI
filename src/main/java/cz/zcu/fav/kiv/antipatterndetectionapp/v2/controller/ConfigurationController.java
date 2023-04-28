package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.Configuration;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserConfigKey;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserConfiguration;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.UserConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Jiri Trefil
 * Controller providing API for configuration management
 */
@RestController
@RequestMapping("v2/configuration")
public class ConfigurationController {
    @Autowired
    private UserConfigurationRepository userConfigurationRepository ;

    @GetMapping(value="/test")
    public String test(){
        UserConfigKey key = new UserConfigKey(1,1);
        UserConfiguration tmp = userConfigurationRepository.save(new UserConfiguration(key));
        return "test";
    }




}
