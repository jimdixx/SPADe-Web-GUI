package cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.http.*;

import javax.servlet.http.HttpServletRequest;

public class RequestBuilder {

    //@Value("${auth.realm.authenticate}")
    private static String spadeSignature;
    private static Logger logger = Logger.getLogger(RequestBuilder.class.getName());



    public static ResponseEntity<String> sendRequestResponse(String url, Map<String, Object> body) {
        RestTemplate restTemplate = new RestTemplate();
        String json = JSONBuilder.buildJSON(body);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("X-spade-request",spadeSignature);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        return restTemplate.postForEntity(url, entity, String.class);
    }

    public static ResponseEntity<String> sendRequestResponse(String url, String token) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("X-spade-request",spadeSignature);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        return response;
    }

}
