package cz.zcu.fav.kiv.antipatterndetectionapp.service;

public interface UserAccountService {

    boolean checkCredentials(String username, String password);

}
