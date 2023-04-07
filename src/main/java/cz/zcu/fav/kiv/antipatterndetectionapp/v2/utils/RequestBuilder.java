package cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.logging.Logger;

import org.springframework.http.*;

public class RequestBuilder {


    private static Logger logger = Logger.getLogger(RequestBuilder.class.getName());


    public static ResponseEntity<String> sendRequestResponse(String url, HashMap<String,String> body) {
        RestTemplate restTemplate = new RestTemplate();
        String json = JSONBuilder.buildJson(body);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        return response;
    }

}
