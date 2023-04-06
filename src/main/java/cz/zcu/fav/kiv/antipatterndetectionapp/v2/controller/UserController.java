package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.dials.UserModelStatusCodes;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for providing basic support for user management
 * @version 1.0
 * @author Vaclav Hrabik, Jiri Trefil
 */
@RestController
@RequestMapping("v2/user")
public class UserController {

    /**
     * Service for work with user management
     */
    @Autowired
    private UserService userService;

    /**
     * Method to register new user in the app
     * @param user  User who wants register in to the app
     * @return      message
     */
    @PostMapping(value = "/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        UserModelStatusCodes statusCode = userService.registerUser(user);
        return getResponseEntity(statusCode);
    }

    /**
     * Method to login user in to the app
     * @param user  User who wants to login in to the app
     * @return      message
     */
    @PostMapping(value = "/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        UserModelStatusCodes statusCode = userService.loginUser(user);
        return getResponseEntity(statusCode);
    }

    /**
     * Method to logout user from the app
     * @param user  User who wants logout from the app
     * @return      message after logout
     */
    @PostMapping(value = "/logout")
    public ResponseEntity<String> logoutUser(@RequestBody User user) {
        UserModelStatusCodes statusCode = userService.logoutUser(user);
        return getResponseEntity(statusCode);
    }

    /**
     * Method to create response
     * @param statusCode    UserModelStatusCodes code
     * @return              ResponseEntity with code and msg
     */
    private ResponseEntity<String> getResponseEntity(UserModelStatusCodes statusCode) {
        String message = this.generateResponseObject(statusCode);
        int code = statusCode.statusCode;
        return new ResponseEntity<>(message, HttpStatus.valueOf(code));
    }

    /**
     * Method to create JSON object
     * @param code  UserModelStatusCodes code
     * @return      String that represents JSON object
     */
    private String generateResponseObject(UserModelStatusCodes code){
        HashMap<String, String> json = new HashMap<>();
        json.put("msg", code.label);
        return JSONBuilder.buildJson(json);

    }



}
