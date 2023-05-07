package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.ConfigService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.JSONBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Jiri Trefil
 * Controller providing API for configuration management
 */
@RestController
@RequestMapping("v2/configuration")
public class ConfigurationController {
    @Autowired
    private ConfigService configurationService;

    @PostMapping(value="/upload_configuration")
    public ResponseEntity<String> test(@RequestBody UserConfiguration userConfiguration) {
        ResponseEntity<String> response = configurationService.addConfiguration(userConfiguration);
        return response;
    }

    @PostMapping(value="/configuration")
    public ResponseEntity<String> getUserConfigurations(@RequestBody User user) {
        ResponseEntity<String> response = this.configurationService.getUserConfigurations(user);
        return response;
    }

    @PostMapping(value="/configuration_name")
    public ResponseEntity<String> getConfigurationNames(@RequestBody User user) {
        Map<String, Object> json = new HashMap<>();
        List<String> configuration = this.configurationService.getConfigurationNames(user);
        if(configuration == null) {
            json.put("message", "internal sever error");
            return new ResponseEntity<>(JSONBuilder.buildJSON(json), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        json.put("message", "ok");
        json.put("configuration_names", configuration);
        String jsonString = JSONBuilder.buildJSON(json);
        return new ResponseEntity<>(jsonString, HttpStatus.OK);
    }








}
