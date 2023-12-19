package cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.httpErrorHandler.CustomErrorHandler;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.*;

import javax.servlet.http.HttpServletRequest;

@Component
public class RequestBuilder {
    private static Logger logger = Logger.getLogger(RequestBuilder.class.getName());

    public ResponseEntity<String> resendHttpRequest(HttpServletRequest request, String authenticationUrl) {
        try {
            // Extract request method, headers, and body from the original request
            String method = request.getMethod();
            HttpHeaders headers = extractHeaders(request);
            headers.add(HttpHeaders.HOST, "http://localhost:8080");
            String body = extractBody(request);

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setErrorHandler(new CustomErrorHandler());
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

            // Set Authorization header with the provided token
//            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

            // Create an HttpEntity with headers and body
            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            // Determine the appropriate exchange method based on the original request method
            ResponseEntity<String> response;
            if ("GET".equalsIgnoreCase(method)) {
                response = restTemplate.exchange(authenticationUrl, HttpMethod.GET, entity, String.class);
            } else if ("POST".equalsIgnoreCase(method)) {
                response = restTemplate.exchange(authenticationUrl, HttpMethod.POST, entity, String.class);
            } else {
                // Handle other HTTP methods as needed
                // ...

                // For simplicity, in this example, we assume other methods are handled as POST requests
                response = restTemplate.exchange(authenticationUrl, HttpMethod.POST, entity, String.class);
            }

            logger.log(Level.FINE, "Successfully resent HTTP request to: " + authenticationUrl);
            return response;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error resending HTTP request", e);
            // Handle the exception as needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error resending HTTP request");
        }
    }

    // Helper method to extract headers from HttpServletRequest
    private HttpHeaders extractHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Enumeration<String> headerValues = request.getHeaders(headerName);
            while (headerValues.hasMoreElements()) {
                String headerValue = headerValues.nextElement();
                headers.add(headerName, headerValue);
            }
        }
        return headers;
    }

    // Helper method to extract body from HttpServletRequest
    private String extractBody(HttpServletRequest request) throws IOException {
        try (InputStream inputStream = request.getInputStream()) {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        }
    }

    public ResponseEntity<String> sendRequestResponse(String url, Map<String, Object> body) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomErrorHandler());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        String json = JSONBuilder.buildJSON(body);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("X-spade-request",spadeSignature);
        logger.log(Level.FINE,"Sending http request with body: \n"+json+"\n to: "+url);

        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        return restTemplate.postForEntity(url, entity, String.class);
    }

    public ResponseEntity<String> sendRequestResponse(String url, String token) {
        RestTemplate restTemplate = new RestTemplate();
        //custom error handler to handle http response
        restTemplate.setErrorHandler(new CustomErrorHandler());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("X-spade-request",spadeSignature);
        headers.set("Authorization", "Bearer " + token);
        logger.log(Level.FINE,"Sending http request with no body to: "+url);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        return response;

    }

    public ResponseEntity<String> sendRequestResponse(String url, String token, boolean get) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomErrorHandler());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("X-spade-request",spadeSignature);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response;
    }


    public ResponseEntity<String> sendRequestResponse(String url, Map<String, Object> body, String token) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomErrorHandler());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        String json = JSONBuilder.buildJSON(body);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("X-spade-request",spadeSignature);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        return restTemplate.postForEntity(url, entity, String.class);
    }

}
