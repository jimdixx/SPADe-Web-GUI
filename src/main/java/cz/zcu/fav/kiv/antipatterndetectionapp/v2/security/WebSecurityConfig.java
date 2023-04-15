package cz.zcu.fav.kiv.antipatterndetectionapp.v2.security;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.AuthProvider;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.OAuthService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.OAuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Bean
    public AuthenticationProvider provider() {
        return new AuthProvider(template());
    }

    @Bean
    public RestTemplate template() {
        return new RestTemplate();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }

//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter(OAuthService oAuthService) {
//        return new JwtAuthenticationFilter(oAuthService);
//    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .mvcMatchers("/v2/user/register", "/v2/user/login", "/v2/user/refresh", "/v2/user/logout").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(new OAuthServiceImpl()), UsernamePasswordAuthenticationFilter.class);
    }

}
