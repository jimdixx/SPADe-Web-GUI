package cz.zcu.fav.kiv.antipatterndetectionapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {

    @Autowired
    private Environment environment;

    public String getDataSourceUrl () {
        return environment.getProperty("spring.datasource.url");
    }

    public String getDataSourceUsername() {
        return environment.getProperty("spring.datasource.username");
    }

    public String getDataSourcePassword() {
        return environment.getProperty("spring.datasource.password");
    }
}
