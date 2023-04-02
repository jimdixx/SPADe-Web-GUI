package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service provides functionality for user login/register/logout
 * @author Vaclav Hrabik, Jiri Trefil
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Method attempts to register a user
     * @param user serialized JSON object representing user
     * @return Integer flag:
     *  1 - if the user is registered successfully
     *  0 - if the user request violates constraints of column(s)
     *  -1 - if the username is already taken
     *
     */
    @Override
    public int registerUser(User user) {
        final String email = user.getEmail();
        final String name = user.getName();
        //if client request violates constraints - kill the request. User will not be registered.
        if (email.length() > User.getEmailConstraint() || name.length() > User.getNameConstraint()) {
            return 0;
        }
        //if the username is taken, kill the request aswell
        if (userRepository.findByName(user.getName()) != null) {
            return -1;
        }
        //save the user
        User u = userRepository.save(user);

        //TODO request to OAuth for token - send user info to the oauth app for token
        //return okay status code, the user was created
        return 1;
    }

    /**
     * Method
     * @param user serialized JSON object representing user
     * @return Integer Flag:
     *      0 - if username is not registered
     *      1 - if user is logged successfully
     */
    @Override
    public int loginUser(User user) {
        User u = userRepository.findByName(user.getName());
        //TODO request to OAuth for authentication
        return u == null ? 0 : 1;
    }

    /**
     * Methods invalidates JWT token in OAuth application
     * @param user serialized JSON object representing user
     * @return
     */
    @Override
    public int logoutUser(User user) {
        //TODO with OAuth app
        return 0;
    }
}
