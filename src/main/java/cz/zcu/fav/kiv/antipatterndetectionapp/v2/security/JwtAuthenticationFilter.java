package cz.zcu.fav.kiv.antipatterndetectionapp.v2.security;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.AuthProvider;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.OAuthService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.OAuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private OAuthService oAuthService;

    public JwtAuthenticationFilter(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        System.out.println("------------------DO FILTER INTERNAL---------------------------");
//
//        String authorizationHeader = request.getHeader("Authorization");
//        if(authorizationHeader == null || authorizationHeader.length() < 7) {
//            filterChain.doFilter(request, response);
//            return;
//        }
////        OAuthServiceImpl oAuthService = new OAuthServiceImpl();
//
//        String token = authorizationHeader.substring(7);
//
//        ResponseEntity<String> oAuthResponse =  oAuthService.authenticate(token);
//        response.setContentType("application/json");
//        response.setStatus(oAuthResponse.getStatusCodeValue());
//
//        BufferedWriter out = new BufferedWriter(response.getWriter());
//        out.write(Objects.requireNonNull(oAuthResponse.getBody()));
//        out.flush();
//        out.close();
//
//        filterChain.doFilter(request, response);
//
//    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String authorizationHeader = request.getHeader("Authorization");
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String token = authorizationHeader.substring(7);
//            try {
//                Authentication authentication = new UsernamePasswordAuthenticationToken(oAuthService.authenticate(token).getBody(), null);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            } catch (Exception e) {
//                SecurityContextHolder.clearContext();
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
//            }
//        }
////        String authorizationHeader = request.getHeader("Authorization");
////
////        if(authorizationHeader == null || authorizationHeader.length() < 7) {
////            filterChain.doFilter(request, response);
////            return;
////        }
////
////        RestTemplate restTemplate = new RestTemplate();
////        HttpHeaders headers = new HttpHeaders();
////        headers.setContentType(MediaType.APPLICATION_JSON);
////
////
////        HashMap<String, String> requestBody = new HashMap<>();
////
////        requestBody.put("name", "userName");
////
////        // set the JWT token in the authorization header
////
////        headers.set("Authorization", authorizationHeader);
////        requestBody.put("token", authorizationHeader.substring(7));
////        //
////
////        String json = JSONBuilder.buildJson(requestBody);
////
////        HttpEntity<String> entity = new HttpEntity<>(json, headers);
////        //RequestBuilder.sendRequestResponse("http://localhost:8081/authenticate",token);
////        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8081/authenticate", HttpMethod.POST, entity, String.class);
////
////        // do something with the response body
////
////        if (responseEntity.getBody().contains("Invalid token")) {
////            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
////
////        } else {
////            return;
//////            Authentication authentication = new UsernamePasswordAuthenticationToken("name", null);
//////            SecurityContextHolder.getContext().setAuthentication(authentication);
////        }
////
//////        filterChain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || authorizationHeader.length() < 7) {
            chain.doFilter(request, response);
            return;
        }
        String token = authorizationHeader.substring(7);

        ResponseEntity<String> responseEntity = oAuthService.authenticate(token);
        if (token != null && responseEntity.getBody().contains("OK")) {
            // Token is valid, proceed with the request
            chain.doFilter(request, response);
        } else {
            // Token is invalid or not present, authenticate the request with external authentication provider
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            String requestBody = "{\"token\": \"" + token + "\"}";
//            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
//
//            ResponseEntity<AuthenticationResponse> authenticationResponseEntity =
//                    restTemplate.postForEntity(AUTHENTICATION_URL, entity, AuthenticationResponse.class);
//
//            if (authenticationResponseEntity.getStatusCode() == HttpStatus.OK
//                    && authenticationResponseEntity.getBody().isAuthenticated()) {
//                // Request is authenticated, proceed with the request
//                chain.doFilter(request, response);
//            } else {
                // Request is not authenticated, set response status to 401 Unauthorized
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            }
        }
    }
}