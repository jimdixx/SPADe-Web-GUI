package cz.zcu.fav.kiv.antipatterndetectionapp.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Component
public class AuthConfiguration {

    @Autowired
    private JwtAuthenticationTokenConverter jwtAuthenticationTokenConverter;

    @Bean
    public SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .authorizeRequests(authz -> authz //TODO odstranit detecting
                        .antMatchers("/v2/app/metadata/**", "/v2/user/register", "/v2/detecting/**").permitAll() // Allow all requests to /v2/app/metadata/**
                        .antMatchers("/v2/**").hasAnyRole("spade_basic")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationTokenConverter)))
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .csrf().disable();
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "OPTIONS", "PUT", "DELETE", "PATCH")
                        .allowedHeaders("authorization", "content-type")
                        .allowedOrigins("http://localhost:3000")
                        .allowCredentials(true);
            }
        };
    }
}
