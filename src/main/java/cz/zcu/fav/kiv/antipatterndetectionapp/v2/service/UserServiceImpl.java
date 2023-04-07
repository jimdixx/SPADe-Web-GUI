package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.dials.UserModelStatusCodes;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

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
     * @return  Enum code UserModelStatusCodes.
     *      INVALID_USER_ARGUMENTS      if user send invalid name, email or password
     *      USER_EXISTS                 if user send name, which is already taken
     *      HASH_FAILED                 if hash function failed
     *      USER_CREATION_FAILED        if there is problem with database
     *      USER_CREATED                if user were created (happy-day)
     */
    @Override
    public UserModelStatusCodes registerUser(User user) {
        final String email = user.getEmail();
        final String name = user.getName();
        final String password = user.getPassword();

        if (!verifyUserParameters(name, email, password))
            return UserModelStatusCodes.INVALID_USER_ARGUMENTS;

        //if the username is taken, kill the request as well
        if (userRepository.findByName(user.getName()) != null) {
            return UserModelStatusCodes.USER_EXISTS;
        }

        String passwordHash = hashPassword(password);
        //server side fault
        if (passwordHash == null) {
            return UserModelStatusCodes.HASH_FAILED;
        }
        //save the user
        User u = userRepository.save(new User(name, email, passwordHash));
        //database insert failed for some reason
        if(u == null)
            return UserModelStatusCodes.USER_CREATION_FAILED;
        //TODO request to OAuth for token - send user info to the oauth app for token
        //return okay status code, the user was created
        return UserModelStatusCodes.USER_CREATED;
    }

    /**
     * @param email String - user email provided by client
     * @param name String - username provided by client
     * @return true if @param User is valid
     */
    private boolean verifyUserParameters(String name, String email, String password) {
        //if client send somehow not whole register request.
        if (email == null || name == null || password == null) {
            return false;
        }
        //if client request violates constraints - kill the request. User will not be registered.
        if (email.length() > User.getEmailConstraint() || name.length() > User.getNameConstraint()) {
            return false;
        }
        //regex for email validation
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        final Pattern emailPattern = Pattern.compile(emailRegex);
        //return true if email is valid (pattern wise)
        return emailPattern.matcher(email).find();
    }

    /**
     * Method to hash password
     * @param password  password from client
     * @return          hashed password for database
     */
    private String hashPassword(String password) {
        //standard java security encryption module
        MessageDigest digest = null;
        try{
            //try to instance the class - throws an error if algorithm
            digest = MessageDigest.getInstance("SHA3-256");
        }
        catch(NoSuchAlgorithmException exception){
            exception.printStackTrace();
            return null;
        }
        byte [] tmp = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        //convert byte array into string
        return (new HexBinaryAdapter()).marshal(tmp);
    }

    /**
     * Method
     * @param user serialized JSON object representing user
     * @return  Enum flag UserModelStatusCodes.
     *      USER_LOGGED_IN     if user has right name and password
     *      USER_LOGIN_FAILED  if user has wrong name or password
     */

    @Override
    public UserModelStatusCodes loginUser(User user) {
        final String name = user.getName();
        final String password = user.getPassword();
        if(name == null || password == null) {
            return UserModelStatusCodes.USER_LOGIN_FAILED;
        }

        User u = userRepository.findByName(user.getName());

        if (u == null) {
            return UserModelStatusCodes.USER_LOGIN_FAILED;
        }
        //TODO request to OAuth for authentication
        final boolean passwordMatches = comparePassword(password,u.getPassword());

        return (!passwordMatches ? UserModelStatusCodes.USER_LOGIN_FAILED : UserModelStatusCodes.USER_LOGGED_IN);
    }

    /**
     * Method compares user password with stored hash
     * @param password String user provided password
     * @param hash String hash saved in database
     * @return true if hashes are the same
     */
    boolean comparePassword(String password, String hash){
        final String passwordHash = this.hashPassword(password);
        return hash.equals(passwordHash);
    }

    /**
     * Methods invalidates JWT token in OAuth application
     * @param user serialized JSON object representing user
     * @return UserModelStatusCodes user logged out status flag
     */
    @Override
    public UserModelStatusCodes logoutUser(User user) {
        //TODO with OAuth app
        return UserModelStatusCodes.USER_LOGGED_OUT;
    }
}
