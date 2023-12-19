package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.dials.UserModelStatusCodes;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.oauth.OAuthService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.user.UserService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.JSONBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Controller for providing basic support for user management
 *
 * @author Vaclav Hrabik, Jiri Trefil
 * @version 1.0
 */
@RestController
@RequestMapping("v2/user")
public class UserController {


    /**
     * Method to create response
     *
     * @param statusCode UserModelStatusCodes code
     * @return ResponseEntity with code and msg
     */
    private ResponseEntity<String> getResponseEntity(UserModelStatusCodes statusCode, String jwtToken) {
        String message = this.generateResponseObject(statusCode, jwtToken);
        int code = statusCode.getStatusCode();
        return new ResponseEntity<>(message, HttpStatus.valueOf(code));
    }

    /**
     * Method to create JSON object
     *
     * @param code UserModelStatusCodes code
     * @return String that represents JSON object
     */
    private String generateResponseObject(UserModelStatusCodes code, String jwtToken) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("message", code.getLabel());
        if (jwtToken != null) {
            json.put("jwtToken", jwtToken);
        }
        return JSONBuilder.buildJSON(json);
    }


}
