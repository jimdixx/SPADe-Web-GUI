package cz.zcu.fav.kiv.antipatterndetectionapp.service.oauth;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.app.User;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface OAuthService {
    public ResponseEntity<String> authenticate(String token);
    ResponseEntity<String> authenticate(HttpServletRequest request);
    public ResponseEntity<String> loginUser(User user);
    public ResponseEntity<String> logoutUser(User user);

    ResponseEntity<String> refreshToken(String token);

}
