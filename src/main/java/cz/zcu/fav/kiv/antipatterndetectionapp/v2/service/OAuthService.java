package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import org.springframework.http.ResponseEntity;

public interface OAuthService {
    public ResponseEntity<String> authenticate(User user);
    public ResponseEntity<String> loginUser(User user);

}
