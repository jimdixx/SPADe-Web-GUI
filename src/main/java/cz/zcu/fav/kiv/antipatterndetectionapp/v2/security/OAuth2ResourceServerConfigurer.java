package cz.zcu.fav.kiv.antipatterndetectionapp.v2.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public class OAuth2ResourceServerConfigurer {
    public static void jwt(
            org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer<HttpSecurity>
                    httpSecurityOAuth2ResourceServerConfigurer) {

    }
}
