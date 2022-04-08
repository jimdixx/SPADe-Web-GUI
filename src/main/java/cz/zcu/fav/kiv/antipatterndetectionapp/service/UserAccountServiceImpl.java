package cz.zcu.fav.kiv.antipatterndetectionapp.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.controller.AppController;
import cz.zcu.fav.kiv.antipatterndetectionapp.spring.ApplicationProperties;
import cz.zcu.fav.kiv.antipatterndetectionapp.spring.SpringApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Properties;

@Service
public class UserAccountServiceImpl implements UserAccountService{

    private final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    @Override
    public boolean checkCredentials(String username, String password) {
        ApplicationProperties applicationProperties = ((ApplicationProperties) SpringApplicationContext.getContext()
                .getBean("applicationProperties"));

        String accountName = applicationProperties.getAccountUserName();
        String accountPass = applicationProperties.getAccountUserPassword();

        if(username.equals(accountName) && password.equals(accountPass))
            return true;

        return false;
    }
}
