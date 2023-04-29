package cz.zcu.fav.kiv.antipatterndetectionapp.v2.security;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.OAuthService;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final OAuthService oAuthService;

    public JwtAuthenticationFilter(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getOutputStream().println("{\"error\" : \"Some other error related to jwt token!\"}");
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
        }
        //4xx or 5xx http response from auth application
        //basically copies the response from auth app and send it to client
        catch (HttpClientErrorException e){
            SecurityContextHolder.clearContext();
            //read response body
            String responseBody = e.getResponseBodyAsString();
            response.setStatus(e.getStatusCode().value());
            response.getOutputStream().println(responseBody);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
            String path = request.getRequestURI().substring(request.getContextPath().length());
        return path.startsWith("/v2/user/login") || path.startsWith("/v2/user/register");
    }


}

