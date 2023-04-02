package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String registerUser(@RequestBody User user) {
        int statusCode = userService.registerUser(user);
        return "user register";
    }

    /**
     * Method to login user in to the app
     * @param user  User who wants to login in to the app
     * @return      message
     */
    @PostMapping(value = "/login")
    public String loginUser(@RequestBody User user) {
        int statusCode = userService.loginUser(user);
        return "user logged";
    }

    /**
     * Method to logout user from the app
     * @param user  User who wants logout from the app
     * @return      message after logout
     */
    @PostMapping(value = "/logout")
    public String logoutUser(@RequestBody User user) {
        int statusCode = userService.logoutUser(user);
        return "user logout";
    }

}
