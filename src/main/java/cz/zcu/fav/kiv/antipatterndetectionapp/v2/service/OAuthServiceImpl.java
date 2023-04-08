package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Service which communicate with OAuth application
 */
@Service
public class OAuthServiceImpl implements OAuthService, UserDetailsService {

    /**
     * URL path to authenticate endpoint of OAuth application
     */
    @Value("${auth.realm.authenticate}")
    private String AUTH_URL_AUTH;

    /**
     * URL path to login endpoint of OAuth application
     */
    @Value("${auth.realm.login}")
    private String AUTH_URL_LOGIN;

    /**
     * URL path to logout endpoint of OAuth application
     */
    @Value("${auth.realm.logout}")
    private String AUTH_URL_LOGOUT;

    /**
     *
     */
    @Autowired
    private UserService userService;

    public ResponseEntity<String> authenticate(String token) {

        return RequestBuilder.sendRequestResponse(AUTH_URL_AUTH, null, token);
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
