package cz.zcu.fav.kiv.antipatterndetectionapp.v2.security;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.AuthProvider;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.OAuthService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.OAuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.stereotype.Component;
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



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("------------------DO FILTER INTERNAL---------------------------");

        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader == null || authorizationHeader.length() < 7) {
            filterChain.doFilter(request, response);
            return;
        }
        OAuthServiceImpl oAuthService = new OAuthServiceImpl();

        String token = authorizationHeader.substring(7);

        ResponseEntity<String> oAuthResponse =  oAuthService.authenticate(token);
        response.setContentType("application/json");
        response.setStatus(oAuthResponse.getStatusCodeValue());

        BufferedWriter out = new BufferedWriter(response.getWriter());
        out.write(Objects.requireNonNull(oAuthResponse.getBody()));
        out.flush();
        out.close();

        filterChain.doFilter(request, response);

    }


}