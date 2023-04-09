package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Service which communicate with OAuth application
 */
@Service
public class OAuthServiceImpl implements OAuthService, UserDetailsService, AuthenticationEntryPoint {

    /**
     * URL path to authenticate endpoint of OAuth application
     */
//    @Value("${auth.realm.authenticate}")
    private String AUTH_URL_AUTH = "http://localhost:8081/authenticate";

    /**
     * URL path to login endpoint of OAuth application
     */
//    @Value("${auth.realm.login}")
    private String AUTH_URL_LOGIN = "http://localhost:8081/login";

    /**
     * URL path to logout endpoint of OAuth application
     */
//    @Value("${auth.realm.logout}")
    private String AUTH_URL_LOGOUT = "http://localhost:8081/logout";

    /**
     *
     */
    @Autowired
    private UserService userService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || authorizationHeader.length() < 7) {
            return;
        }
        String token = authorizationHeader.substring(7);

        ResponseEntity<String> responseEntity = authenticate(token);
         if (token != null && responseEntity.getBody().contains("OK")) {
            // Token is valid, proceed with the request
            response.setStatus(501);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    public ResponseEntity<String> authenticate(String token) {
        HashMap<String, String> requestBody = new HashMap<>();

        requestBody.put("name", "userName");
        requestBody.put("token", token);

        return RequestBuilder.sendRequestResponse(AUTH_URL_AUTH, requestBody);
    }

    public ResponseEntity<String> loginUser(User user) {
        final String userName = user.getName();

        if(userName == null) {
            return null;
        }
        //HttpURLConnection con = RequestBuilder.createConnection(AUTH_URL);
        HashMap<String, String> requestBody = new HashMap<>();

        requestBody.put("name", userName);

        return RequestBuilder.sendRequestResponse(AUTH_URL_LOGIN, requestBody);
    }

    public ResponseEntity<String> logoutUser(User user) {
        final String userName = user.getName();
        final String token = user.getToken();

        if(userName == null || token == null) {
            return null;
        }

        //HttpURLConnection con = RequestBuilder.createConnection(AUTH_URL);
        HashMap<String, String> requestBody = new HashMap<>();

        requestBody.put("name", userName);
        requestBody.put("token", token);

        return RequestBuilder.sendRequestResponse(AUTH_URL_LOGOUT, requestBody);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        final User user = this.userService.getUserByName(s);
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),new ArrayList<>());
    }
}
