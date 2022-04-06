package cz.zcu.fav.kiv.antipatterndetectionapp.service;

public interface UserAccountService {

    /**
     * Checks if the given credentials are valid
     * @param username Username
     * @param password Password
     * @return True if user credentials are valid
     */
    boolean checkCredentials(String username, String password);

}
