package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class OAuthServiceImpl implements OAuthService, UserDetailsService {


    @Value("${auth.realm.authenticate}")
    private String AUTH_URL_AUTH;

    @Value("${auth.realm.login}")
    private String AUTH_URL_LOGIN;

    @Autowired
    private UserService userService;

    public ResponseEntity<String> authenticate(User user) {
        final String userName = user.getName();
        final String token = user.getToken();

        if(userName == null || token == null) {
            return null;
        }

        //HttpURLConnection con = RequestBuilder.createConnection(AUTH_URL);
        HashMap<String, String> requestBody = new HashMap<>();

        requestBody.put("name", userName);
        requestBody.put("token", token);

        ResponseEntity<String> response = RequestBuilder.sendRequestResponse(AUTH_URL_AUTH, requestBody);

        return response;
    }

    public ResponseEntity<String> loginUser(User user) {
        final String userName = user.getName();

        if(userName == null) {
            return null;
        }
        //HttpURLConnection con = RequestBuilder.createConnection(AUTH_URL);
        HashMap<String, String> requestBody = new HashMap<>();

        requestBody.put("name", userName);

        ResponseEntity<String> response = RequestBuilder.sendRequestResponse(AUTH_URL_LOGIN, requestBody);

        return response;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        final User user = this.userService.getUserByName(s);
        return new org.springframework.security.core.userdetails.User(user.getName(),user.getPassword(),new ArrayList<>());
    }
}
