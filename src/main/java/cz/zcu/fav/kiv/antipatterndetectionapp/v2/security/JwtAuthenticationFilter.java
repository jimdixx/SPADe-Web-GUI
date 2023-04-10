package cz.zcu.fav.kiv.antipatterndetectionapp.v2.security;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.AuthProvider;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.OAuthService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.OAuthServiceImpl;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.JSONBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.security.Security;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private OAuthService oAuthService;

    public JwtAuthenticationFilter(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String token = authorizationHeader.replace("Bearer ", "");
            ResponseEntity<String> responseEntity = oAuthService.authenticate(token);



            UserDetails userDetails = User.builder()
                        .username(responseEntity.getBody())
                        .password("")
                        .authorities(Collections.emptyList())
                        .build();

                Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
    /*
            else {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getOutputStream().println("{\"error\" : \"Token timed out!\"}");
                return;
            }*/
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().println("{\"error\" : \"Some other error related to jwt token!\"}");
            return;
        }

        chain.doFilter(request, response);
    }
}

