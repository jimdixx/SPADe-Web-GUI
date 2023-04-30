package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.ConfigRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.UserConfigurationJoinRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Jiri Trefil
 * Controller providing API for configuration management
 */
@RestController
@RequestMapping("v2/configuration")
public class ConfigurationController {
    @Autowired
    private ConfigurationService configurationService;

    @PostMapping(value="/upload_configuration")
    public ResponseEntity<String> test(@RequestBody UserConfiguration userConfiguration) {
        ResponseEntity<String> response = configurationService.addConfiguration(userConfiguration);
        return response;
    }

    @GetMapping(value="/get_configuration")
    public ResponseEntity<String> getUserConfigurations(@RequestParam(name="name") String userName){
        User user = new User(userName);
        ResponseEntity<String> response = this.configurationService.getUserConfigurations(user);
        return response;
    }





}
